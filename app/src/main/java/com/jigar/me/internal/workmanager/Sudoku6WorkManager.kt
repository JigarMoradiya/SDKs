package com.jigar.me.internal.workmanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.jigar.me.MyApplication
import com.jigar.me.data.local.db.sudoku.SudokuDB
import com.jigar.me.data.model.dbtable.suduko.Suduko
import com.jigar.me.data.model.dbtable.suduko.SudukoPlay
import com.jigar.me.data.pref.AppPreferencesHelper
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.Constants
import com.jigar.me.utils.sudoku.SudokuConst4
import com.jigar.me.utils.sudoku.SudokuConst6
import com.jigar.me.utils.sudoku.SudukoConst
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import java.util.*

@HiltWorker
class Sudoku6WorkManager @AssistedInject constructor(
    @Assisted contexts: Context,
    @Assisted params: WorkerParameters,
    val dataManager : SudokuDB
) : CoroutineWorker(contexts, params) {
    private val context : Context = contexts
    private val TAG = "jigar_HelloWorldWorker"
    private var workProgress = 0
    private var temp_list_number = ArrayList<String>()
    private val list_suduko = ArrayList<String>()
    private var level : String = ""
    private var roomId: String = "9999999"
    private var count = 0
    private val list_suduko_random = ArrayList<Int>()
    private var list_suduko_result: ArrayList<Suduko> = ArrayList<Suduko>()
    private lateinit var prefManager : AppPreferencesHelper

    override suspend fun doWork(): Result = coroutineScope {
        prefManager = AppPreferencesHelper(context, Constants.PREF_NAME)
        level = inputData.getString(SudokuConst4.Level) ?: SudukoConst.Level_6By6
        roomId = inputData.getInt(SudokuConst4.RoomId, 9999999).toString()
        Log.e(TAG, "Starting Work. level = "+level+" roomId = "+roomId)
        val jobs = try {
            async {
                createSudoStart()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@coroutineScope Result.retry()
        }

        // awaitAll will throw an exception if a download fails, which CoroutineWorker will treat as a failure
        jobs.await()
        Result.success()
    }
    private fun updateProgress(message: String) {
        workProgress += 10
    }
    private fun createSudoStart() = runBlocking {
        updateProgress("createSudoStart.")
        dataManager.deleteSuduko(SudokuConst4.Room1)
        dataManager.deleteSudukoPlay(roomId)
        if (dataManager.getSudukoPlay(roomId).isEmpty()){
            temp_list_number.clear()
            list_suduko.clear()

            for (i in 0..5) {
                list_suduko.add(i.toString() + "")
                temp_list_number.add(i.toString() + "")
                for (j in 0..5) {
                    dataManager.insertSuduko(Suduko(0, i.toString() + "" + j, "", SudokuConst4.Room1, ""))
                    dataManager.insertSudukoPlay(SudukoPlay(0,i.toString() + "" + j,"",roomId,"",level,"N",""))
                }
            }
            count = 0
            genrateSudokuCell_00()
        }else{
            createSudo()
        }
    }
    private fun createSudo(){
        val intent = Intent(AppConstants.WORK_MANAGER_SUDUKO_CREATE_STATUS)
        intent.putExtra("startAgain", true)
        intent.putExtra(SudokuConst4.RoomId ,roomId)
        intent.putExtra(SudokuConst4.Level ,level)
        LocalBroadcastManager.getInstance(this.applicationContext).sendBroadcast(intent)
    }
    private fun genrateSudokuCell_00() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudokuConst6.Cell_00,
            SudokuConst6.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_01()
    }

    private fun genrateSudokuCell_01() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudokuConst6.Cell_01,
            SudokuConst6.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_02()
    }

    private fun genrateSudokuCell_02() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudokuConst6.Cell_02,
            SudokuConst6.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_03()
    }

    private fun genrateSudokuCell_03() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudokuConst6.Cell_03,
            SudokuConst6.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_04()
    }

    private fun genrateSudokuCell_04() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudokuConst6.Cell_04,
            SudokuConst6.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_05()
    }

    private fun genrateSudokuCell_05() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudokuConst6.Cell_05,
            SudokuConst6.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_10()
    }

    private fun genrateSudokuCell_10() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst6.Room1,
            SudokuConst6.str_Row0,list_suduko1)
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_10,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_11()
    }

    private fun genrateSudokuCell_11() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudokuConst6.Cell_11,
            SudokuConst6.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_12()
    }

    private fun genrateSudokuCell_12() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudokuConst6.Cell_12,
            SudokuConst6.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_13()
    }

    private fun genrateSudokuCell_13() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst6.Room1,
            SudokuConst6.str_Row1 + "," +SudokuConst6.str_Grid1,list_suduko1)
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_13,
            SudukoConst.Room1
        )
        genrateSudokuCell_14()
    }

    private fun genrateSudokuCell_14() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst6.Room1,
            SudokuConst6.str_Row1 + "," +SudokuConst6.str_Grid1,list_suduko1)
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_14,
            SudukoConst.Room1
        )
        genrateSudokuCell_15()
    }

    private fun genrateSudokuCell_15() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst6.Room1,
            SudokuConst6.str_Row1 + "," +SudokuConst6.str_Grid1,list_suduko1)
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_15,
            SudukoConst.Room1
        )
        genrateSudokuCell_20()
    }

    private fun genrateSudokuCell_20() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst6.Room1,
            SudokuConst6.str_Grid2 + "," +SudokuConst6.str_Column0,list_suduko1)
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_20,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        if (temp_list_number.isNotEmpty()){
            genrateSudokuCell_23()
        }else{
            createSudo()
        }
    }

    private fun genrateSudokuCell_23() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudokuConst6.Cell_23,
            SudokuConst6.Room1
        )
        genrateSudokuCell_21()
    }

    private fun genrateSudokuCell_21() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst6.Room1,
            SudokuConst6.str_Grid2 + "," +SudokuConst6.str_Column1,list_suduko1)
        if (temp_list_number.isNotEmpty()){
            val random: Int = genrateRandomNumber()
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_21,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            genrateSudokuCell_24()
        }else{
            createSudo()
        }

    }

    private fun genrateSudokuCell_24() = runBlocking {
        if (temp_list_number.isNotEmpty()){
            val random: Int = genrateRandomNumber()
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudokuConst6.Cell_24,
                SudokuConst6.Room1
            )
            genrateSudokuCell_22()
        }else{
            createSudo()
        }

    }

    private fun genrateSudokuCell_22() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst6.Room1,
            SudokuConst6.str_Grid2 + "," +SudokuConst6.str_Column2,list_suduko1)
        if (temp_list_number.isNotEmpty()){
            val random: Int = genrateRandomNumber()
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_22,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            genrateSudokuCell_25()
        }else{
            createSudo()
        }
    }

    private fun genrateSudokuCell_25() = runBlocking {
        if (temp_list_number.isNotEmpty()){
            val random: Int = genrateRandomNumber()
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudokuConst6.Cell_25,
                SudokuConst6.Room1
            )
            genrateSudokuCell_40()
        }else{
            createSudo()
        }
    }

    private fun genrateSudokuCell_40() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst6.Room1,
            SudokuConst6.str_Grid4 + "," +SudokuConst6.str_Column0,list_suduko1)
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_40,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_43()
    }

    private fun genrateSudokuCell_43() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudokuConst6.Cell_43,
            SudokuConst6.Room1
        )
        genrateSudokuCell_41()
    }

    private fun genrateSudokuCell_41() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst6.Room1,
            SudokuConst6.str_Grid4 + "," +SudokuConst6.str_Column1,list_suduko1)
        if (temp_list_number.isNotEmpty()){

            val random: Int = genrateRandomNumber()
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_41,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            genrateSudokuCell_44()
        }else{
            createSudo()
        }
    }

    private fun genrateSudokuCell_44() = runBlocking {
        if (temp_list_number.isNotEmpty()){

            val random: Int = genrateRandomNumber()
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudokuConst6.Cell_44,
                SudokuConst6.Room1
            )
            genrateSudokuCell_42()
        }else{
            createSudo()
        }
    }

    private fun genrateSudokuCell_42() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst6.Room1,
            SudokuConst6.str_Grid4 + "," +SudokuConst6.str_Column2,list_suduko1)
        if (temp_list_number.isNotEmpty()){

            val random: Int = genrateRandomNumber()
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_42,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            genrateSudokuCell_45()
        }else{
            createSudo()
        }
    }

    private fun genrateSudokuCell_45() = runBlocking {
        if (temp_list_number.isNotEmpty()){

            val random: Int = genrateRandomNumber()
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudokuConst6.Cell_45,
                SudokuConst6.Room1
            )
            genrateSudokuCell_30()
        }else{
            createSudo()
        }
    }

    private fun genrateSudokuCell_30() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst6.Room1,
            SudokuConst6.str_Grid3 + "," +SudokuConst6.str_Column3+ "," +SudokuConst6.str_Row2
            ,list_suduko1)
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(temp_list_number[random] + "",SudukoConst.Cell_30,SudukoConst.Room1)
        genrateSudokuCell_31()
    }

    private fun genrateSudokuCell_31() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst6.Room1,
            SudokuConst6.str_Grid3 + "," +SudokuConst6.str_Column4+ "," +SudokuConst6.str_Row2
            ,list_suduko1)
        if (temp_list_number.isNotEmpty()){
            val random: Int = genrateRandomNumber()
            dataManager.updateCellValue(temp_list_number[random] + "",SudukoConst.Cell_31,SudukoConst.Room1)
            genrateSudokuCell_32()
        }else{
            createSudo()
        }
    }

    private fun genrateSudokuCell_32() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst6.Room1,
            SudokuConst6.str_Grid3 + "," +SudokuConst6.str_Column5+ "," +SudokuConst6.str_Row2
            ,list_suduko1)
        if (temp_list_number.isNotEmpty()){
            val random: Int = genrateRandomNumber()
            dataManager.updateCellValue(temp_list_number[random] + "",SudukoConst.Cell_32,SudukoConst.Room1)
            genrateSudokuCell_33()
        }else{
            createSudo()
        }
    }

    private fun genrateSudokuCell_33() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst6.Room1,
            SudokuConst6.str_Grid3 + "," +SudokuConst6.str_Column3+ "," +SudokuConst6.str_Row3
            ,list_suduko1)
        if (temp_list_number.isNotEmpty()){
            val random: Int = genrateRandomNumber()
            dataManager.updateCellValue(temp_list_number[random] + "",SudukoConst.Cell_33,SudukoConst.Room1)
            genrateSudokuCell_34()
        }else{
            createSudo()
        }
    }

    private fun genrateSudokuCell_34() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst6.Room1,
            SudokuConst6.str_Grid3 + "," +SudokuConst6.str_Column4+ "," +SudokuConst6.str_Row3
            ,list_suduko1)
        if (temp_list_number.isNotEmpty()){
            val random: Int = genrateRandomNumber()
            dataManager.updateCellValue(temp_list_number[random] + "",SudukoConst.Cell_34,SudukoConst.Room1)
            genrateSudokuCell_35()
        }else{
            createSudo()
        }
    }
    private fun genrateSudokuCell_35() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst6.Room1,
            SudokuConst6.str_Grid3 + "," +SudokuConst6.str_Column5+ "," +SudokuConst6.str_Row3
            ,list_suduko1)
        if (temp_list_number.isNotEmpty()){
            val random: Int = genrateRandomNumber()
            dataManager.updateCellValue(temp_list_number[random] + "",SudukoConst.Cell_35,SudukoConst.Room1)
            genrateSudokuCell_50()
        }else{
            createSudo()
        }
    }
    private fun genrateSudokuCell_50() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst6.Room1,
            SudokuConst6.str_Grid5 + "," +SudokuConst6.str_Column3+ "," +SudokuConst6.str_Row4
            ,list_suduko1)
        if (temp_list_number.isNotEmpty()){
            val random: Int = genrateRandomNumber()
            dataManager.updateCellValue(temp_list_number[random] + "",SudukoConst.Cell_50,SudukoConst.Room1)
            genrateSudokuCell_51()
        }else{
            createSudo()
        }
    }
    private fun genrateSudokuCell_51() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst6.Room1,
            SudokuConst6.str_Grid5 + "," +SudokuConst6.str_Column4+ "," +SudokuConst6.str_Row4
            ,list_suduko1)

        if (temp_list_number.isNotEmpty()){
            val random: Int = genrateRandomNumber()
            dataManager.updateCellValue(temp_list_number[random] + "",SudukoConst.Cell_51,SudukoConst.Room1)
            genrateSudokuCell_52()
        }else{
            createSudo()
        }
    }
    private fun genrateSudokuCell_52() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst6.Room1,
            SudokuConst6.str_Grid5 + "," +SudokuConst6.str_Column5+ "," +SudokuConst6.str_Row4
            ,list_suduko1)
        if (temp_list_number.isNotEmpty()){
            val random: Int = genrateRandomNumber()
            dataManager.updateCellValue(temp_list_number[random] + "",SudukoConst.Cell_52,SudukoConst.Room1)
            genrateSudokuCell_53()
        }else{
            createSudo()
        }
    }
    private fun genrateSudokuCell_53() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst6.Room1,
            SudokuConst6.str_Grid5 + "," +SudokuConst6.str_Column3+ "," +SudokuConst6.str_Row5
            ,list_suduko1)
        if (temp_list_number.isNotEmpty()){
            val random: Int = genrateRandomNumber()
            dataManager.updateCellValue(temp_list_number[random] + "",SudukoConst.Cell_53,SudukoConst.Room1)
            genrateSudokuCell_54()
        }else{
            createSudo()
        }
    }
    private fun genrateSudokuCell_54() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst6.Room1,
            SudokuConst6.str_Grid5 + "," +SudokuConst6.str_Column4+ "," +SudokuConst6.str_Row5
            ,list_suduko1)
        if (temp_list_number.isNotEmpty()){
            val random: Int = genrateRandomNumber()
            dataManager.updateCellValue(temp_list_number[random] + "",SudukoConst.Cell_54,SudukoConst.Room1)
            genrateSudokuCell_55()
        }else{
            createSudo()
        }

    }
    private fun genrateSudokuCell_55() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst6.Room1,
            SudokuConst6.str_Grid5 + "," +SudokuConst6.str_Column5+ "," +SudokuConst6.str_Row5
            ,list_suduko1)

        if (temp_list_number.isNotEmpty()){
            val random: Int = genrateRandomNumber()
            dataManager.updateCellValue(temp_list_number[random] + "",SudukoConst.Cell_55,SudukoConst.Room1)
            genrateSudoko()
        }else{
            createSudo()
        }
    }


    private fun genrateSudoko() = runBlocking {
        prefManager.setCustomParam(SudokuConst4.SelectedBox, "")
        list_suduko_random.clear()
        list_suduko_random.add(2)
        list_suduko_random.add(3)
        list_suduko_random.add(4)
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        for (i in 0..5) {
            list_suduko_result.clear()
            list_suduko_result =
                dataManager.getRoomID_CellValue(SudokuConst6.Room1, i.toString() + "")

            val random = genrateRandomNumber1()
            for (j in 0 until list_suduko_random[random]) {
                val random1 = genrateRandomNumber2()
                dataManager.updateCellValuePlay(
                    list_suduko_result[random1].cellValue,
                    list_suduko_result[random1].cellPosition, roomId, "Y"
                )
                list_suduko_result.removeAt(random1)
            }

            if (list_suduko_random[random] == 2 || list_suduko_random[random] == 3) {
                list_suduko_random.removeAt(random)
            }

            if (i == 5) {
                Log.e(TAG,"donneee")
                prefManager.setCustomParamInt(SudokuConst4.totalSudoku,roomId.toInt())

                // todo maintain 50 record history
                dataManager.deletePreviousSudukoPlay(level)
                sendBroadcastOfCompletion()
            }
        }
    }
    private fun genrateRandomNumber(): Int {
        var randomInt = 0
        try {
            val randomGenerator = Random()
            randomInt = randomGenerator.nextInt(temp_list_number.size)
            //            Log.e("GeneratedNumber: ", temp_list_number.size() + "::" + randomInt);
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return randomInt
    }
    private fun genrateRandomNumber2(): Int {
        var randomInt = 0
        try {
            val randomGenerator = Random()
            randomInt = randomGenerator.nextInt(list_suduko_result.size)
            //            Log.e("GeneratedNumber: ", temp_list_number.size() + "::" + randomInt);
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return randomInt
    }

    private fun genrateRandomNumber1(): Int {
        var randomInt = 0
        try {
            val randomGenerator = Random()
            randomInt = randomGenerator.nextInt(list_suduko_random.size)
            //            Log.e("GeneratedNumber: ", temp_list_number.size() + "::" + randomInt);
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return randomInt
    }
    private fun sendBroadcastOfCompletion() {
        Log.e(TAG, "Work Complete.")
        // event log for sudoku
        MyApplication.logEvent(AppConstants.FirebaseEvents.Sudoku, Bundle().apply {
            putString(AppConstants.FirebaseEvents.deviceId, prefManager.getDeviceId())
            putString(AppConstants.FirebaseEvents.SudokuLevel, level)
        })

        val intent = Intent(AppConstants.WORK_MANAGER_SUDUKO_CREATE_STATUS)
        intent.putExtra("startAgain", false)
        intent.putExtra(SudokuConst4.Level, level)
        intent.putExtra(SudokuConst4.RoomId ,roomId)
        LocalBroadcastManager.getInstance(this.applicationContext).sendBroadcast(intent)
    }

}

