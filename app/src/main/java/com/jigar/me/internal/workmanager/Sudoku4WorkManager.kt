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
import com.jigar.me.utils.sudoku.SudukoConst
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import java.util.*

@HiltWorker
class Sudoku4WorkManager @AssistedInject constructor(
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
        prefManager = AppPreferencesHelper(context, AppConstants.PREF_NAME)
        level = inputData.getString(SudokuConst4.Level) ?: SudukoConst.Level_4By4
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
        temp_list_number.clear()
        list_suduko.clear()


        if (dataManager.getSudukoPlay(roomId).isEmpty()){
            for (i in 0..3) {
                list_suduko.add(i.toString() + "")
                temp_list_number.add(i.toString() + "")
                for (j in 0..3) {
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
            SudokuConst4.Cell_00,
            SudokuConst4.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_01()
    }

    private fun genrateSudokuCell_01() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudokuConst4.Cell_01,
            SudokuConst4.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_02()
    }

    private fun genrateSudokuCell_02() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudokuConst4.Cell_02,
            SudokuConst4.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_03()
    }

    private fun genrateSudokuCell_03() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudokuConst4.Cell_03,
            SudokuConst4.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_10()
    }

    private fun genrateSudokuCell_10() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst4.Room1,
            SudokuConst4.str_Row0,list_suduko1)
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
            SudokuConst4.Cell_11,
            SudokuConst4.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_12()
    }

    private fun genrateSudokuCell_12() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst4.Room1,SudokuConst4.str_Row1 + "," +SudokuConst4.str_Grid1,list_suduko1)
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_12,
            SudukoConst.Room1
        )
        genrateSudokuCell_13()
    }

    private fun genrateSudokuCell_13() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst4.Room1,
            SudokuConst4.str_Row1,
            list_suduko1)
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_13,
            SudukoConst.Room1
        )
        genrateSudokuCell_20()
    }

    private fun genrateSudokuCell_20() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst4.Room1,
            SudokuConst4.str_Column0,list_suduko1)
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_20,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_22()
    }

    private fun genrateSudokuCell_22() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudokuConst4.Cell_22,
            SudokuConst4.Room1
        )
        genrateSudokuCell_21()
    }

    private fun genrateSudokuCell_21() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst4.Room1,
            SudokuConst4.str_Grid2 + "," +SudokuConst4.str_Column1,list_suduko1)
        if (temp_list_number.isNotEmpty()){
            val random: Int = genrateRandomNumber()
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_21,
                SudukoConst.Room1
            )
            genrateSudokuCell_23()
        }else{
            createSudo()
        }
    }

    private fun genrateSudokuCell_23() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst4.Room1,
            SudokuConst4.str_Grid2,list_suduko1)
        if (temp_list_number.isNotEmpty()){
            val random: Int = genrateRandomNumber()
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_23,
                SudukoConst.Room1
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
            SudokuConst4.Room1,
            SudokuConst4.str_Column2 + "," +SudokuConst4.str_Row2,list_suduko1)
        if (temp_list_number.isNotEmpty()){
            val random: Int = genrateRandomNumber()
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_30,
                SudukoConst.Room1
            )
            genrateSudokuCell_31()
        }else{
            createSudo()
        }
    }

    private fun genrateSudokuCell_31() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudokuConst4.Room1,SudokuConst4.str_Row2,list_suduko1)
        if (temp_list_number.isNotEmpty()){
            val random: Int = genrateRandomNumber()
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_31,
                SudukoConst.Room1
            )
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
            SudokuConst4.Room1,
            SudokuConst4.str_Grid3 + "," +SudokuConst4.str_Column2 + "," +SudokuConst4.str_Row3,list_suduko1)
        if (temp_list_number.isNotEmpty()){
            val random: Int = genrateRandomNumber()
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_32,
                SudukoConst.Room1
            )
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
            SudokuConst4.Room1,
            SudokuConst4.str_Grid3 ,list_suduko1)
        if (temp_list_number.isNotEmpty()){
            val random: Int = genrateRandomNumber()
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_33,
                SudukoConst.Room1
            )
            genrateSudoko()
        }else{
            createSudo()
        }
    }


    private fun genrateSudoko() = runBlocking {
        prefManager.setCustomParam(SudokuConst4.SelectedBox, "")
        list_suduko_random.clear()
        list_suduko_random.add(2)
        list_suduko_random.add(2)
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        for (i in 0..3) {
            list_suduko_result.clear()
            list_suduko_result =
                dataManager.getRoomID_CellValue(SudokuConst4.Room1, i.toString() + "")

            val random = genrateRandomNumber1()
            for (j in 0 until list_suduko_random[random]) {
                val random1 = genrateRandomNumber2()
                dataManager.updateCellValuePlay(
                    list_suduko_result[random1].cellValue,
                    list_suduko_result[random1].cellPosition, roomId, "Y"
                )
                list_suduko_result.removeAt(random1)
            }

            if (i == 3) {
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

