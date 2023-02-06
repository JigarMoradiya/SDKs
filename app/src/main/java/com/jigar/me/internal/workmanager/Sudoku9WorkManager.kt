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
class Sudoku9WorkManager @AssistedInject constructor(
    @Assisted contexts: Context,
    @Assisted params: WorkerParameters,
    val dataManager : SudokuDB
) : CoroutineWorker(contexts, params) {
    private val context : Context = contexts
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
        level = inputData.getString(SudokuConst4.Level) ?: SudukoConst.Level_9By9
        roomId = inputData.getInt(SudokuConst4.RoomId, 9999999).toString()
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
        dataManager.deleteSuduko(SudukoConst.Room1)
        dataManager.deleteSudukoPlay(roomId)
        if (dataManager.getSudukoPlay(roomId).isEmpty()){
            temp_list_number.clear()
            list_suduko.clear()
            for (i in 0..8) {
                list_suduko.add(i.toString() + "")
                temp_list_number.add(i.toString() + "")
                for (j in 0..8) {
                    dataManager.insertSuduko(Suduko(0, i.toString() + "" + j, "", SudukoConst.Room1, ""))
                    dataManager.insertSudukoPlay(SudukoPlay(0,i.toString() + "" + j,"",roomId,"",level,"N",""))
                }
            }
            count = 0
            genrateSudokuCell_0()
        }else{
            createSudo()
        }
    }
    private fun createSudo(){
        val intent = Intent(AppConstants.WORK_MANAGER_SUDUKO_CREATE_STATUS)
        intent.putExtra("startAgain", true)
        intent.putExtra(SudukoConst.RoomId ,roomId)
        intent.putExtra(SudukoConst.Level ,level)
        LocalBroadcastManager.getInstance(this.applicationContext).sendBroadcast(intent)
    }
    private fun genrateSudokuCell_0() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_00,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_1()
    }

    private fun genrateSudokuCell_1() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_01,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_2()
    }

    private fun genrateSudokuCell_2() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_02,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_3()
    }

    private fun genrateSudokuCell_3() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_10,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_4()
    }

    private fun genrateSudokuCell_4() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_11,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_5()
    }


    private fun genrateSudokuCell_5() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_12,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_6()
    }

    private fun genrateSudokuCell_6() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_20,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_7()
    }

    private fun genrateSudokuCell_7() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_21,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_8()
    }


    private fun genrateSudokuCell_8() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_22,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_03()
    }

    private fun genrateSudokuCell_03() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(SudukoConst.Room1,SudukoConst.str_Grid0,list_suduko1)
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_03,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_04()
    }


    private fun genrateSudokuCell_04() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_04,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_05()
    }

    private fun genrateSudokuCell_05() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_05,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_06()
    }

    private fun genrateSudokuCell_06() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_06,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_07()
    }

    private fun genrateSudokuCell_07() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_07,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_08()
    }

    private fun genrateSudokuCell_08() = runBlocking {
        val random: Int = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_08,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_13_Temp()
    }

    private fun genrateSudokuCell_13_Temp() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        val temp_list_: ArrayList<String> = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudukoConst.Room1,
            SudukoConst.str_Row1 + "," + SudukoConst.str_Grid1,
            list_suduko1
        )
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Add(
            SudukoConst.Room1,
            SudukoConst.str_Row1_G2_G0,
            temp_list_
        )
        if (temp_list_number.size > 0) {
            if (temp_list_number.size == 1) {
                dataManager.updateCellValue(
                    temp_list_number[0] + "",
                    SudukoConst.Cell_13,
                    SudukoConst.Room1
                )
                temp_list_.remove(temp_list_number[0])
                temp_list_number.removeAt(0)
                temp_list_number.addAll(temp_list_)
                genrateSudokuCell_14()
            } else if (temp_list_number.size == 2) {
                val random = genrateRandomNumber()
                dataManager.updateCellValue(
                    temp_list_number[random] + "",
                    SudukoConst.Cell_13,
                    SudukoConst.Room1
                )
                temp_list_.remove(temp_list_number[random])
                temp_list_number.removeAt(random)
                val random1 = genrateRandomNumber()
                dataManager.updateCellValue(
                    temp_list_number[random1] + "",
                    SudukoConst.Cell_14,
                    SudukoConst.Room1
                )
                temp_list_.remove(temp_list_number[random1])
                temp_list_number.removeAt(random1)
                temp_list_number.clear()
                temp_list_number.addAll(temp_list_)
                genrateSudokuCell_15()
            } else if (temp_list_number.size == 3) {
                val random = genrateRandomNumber()
                dataManager.updateCellValue(
                    temp_list_number[random] + "",
                    SudukoConst.Cell_13,
                    SudukoConst.Room1
                )
                temp_list_.remove(temp_list_number[random])
                temp_list_number.removeAt(random)
                val random1 = genrateRandomNumber()
                dataManager.updateCellValue(
                    temp_list_number[random1] + "",
                    SudukoConst.Cell_14,
                    SudukoConst.Room1
                )
                temp_list_.remove(temp_list_number[random1])
                temp_list_number.removeAt(random1)
                val random2 = genrateRandomNumber()
                dataManager.updateCellValue(
                    temp_list_number[random2] + "",
                    SudukoConst.Cell_15,
                    SudukoConst.Room1
                )
                temp_list_.remove(temp_list_number[random2])
                temp_list_number.removeAt(random2)
                genrateSudokuCell_16()
            }else{}
        } else {
            temp_list_number.addAll(temp_list_)
            genrateSudokuCell_13()
        }
    }

    private fun genrateSudokuCell_13() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_13,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_14()
    }

    private fun genrateSudokuCell_14() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_14,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_15()
    }

    private fun genrateSudokuCell_15() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_15,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_16()
    }

    private fun genrateSudokuCell_16() = runBlocking {
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number.clear()
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudukoConst.Room1,
            SudukoConst.str_Grid1, list_suduko1
        )
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_16,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_17()
    }

    private fun genrateSudokuCell_17() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_17,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_18()
    }

    private fun genrateSudokuCell_18() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_18,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_23()
    }

    private fun genrateSudokuCell_23() = runBlocking {
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number.clear()
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudukoConst.Room1,
            SudukoConst.str_Row1, list_suduko1
        )
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_23,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_24()
    }

    private fun genrateSudokuCell_24() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_24,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_25()
    }

    private fun genrateSudokuCell_25() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_25,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_26()
    }

    private fun genrateSudokuCell_26() = runBlocking {
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number.clear()
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudukoConst.Room1,
            SudukoConst.str_Row2, list_suduko1
        )
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_26,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_27()
    }

    private fun genrateSudokuCell_27() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_27,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_28()
    }

    private fun genrateSudokuCell_28() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_28,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_30()
    }

    private fun genrateSudokuCell_30() = runBlocking {
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number.clear()
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudukoConst.Room1,
            SudukoConst.str_Column0, list_suduko1
        )
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_30,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_33()
    }

    private fun genrateSudokuCell_33() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_33,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_36()
    }

    private fun genrateSudokuCell_36() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_36,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_60()
    }

    private fun genrateSudokuCell_60() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_60,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_63()
    }

    private fun genrateSudokuCell_63() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_63,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_66()
    }

    private fun genrateSudokuCell_66() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_66,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_31_Temp()
    }

    private fun genrateSudokuCell_31_Temp() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        val temp_list_: ArrayList<String> = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudukoConst.Room1,
            SudukoConst.str_Column1 + "," + SudukoConst.str_Grid3,
            list_suduko1
        )
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Add(
            SudukoConst.Room1,
            SudukoConst.str_Column1_G0_G6,
            temp_list_
        )
        //        Log.e("temp_list_number_31", temp_list_.size() + "same" + temp_list_number.size());
        if (temp_list_number.size > 0) {
            if (temp_list_number.size == 1) {
                dataManager.updateCellValue(
                    temp_list_number[0] + "",
                    SudukoConst.Cell_31,
                    SudukoConst.Room1
                )
                temp_list_.remove(temp_list_number[0])
                temp_list_number.removeAt(0)
                temp_list_number.addAll(temp_list_)
                genrateSudokuCell_34()
            } else if (temp_list_number.size == 2) {
                val random = genrateRandomNumber()
                dataManager.updateCellValue(
                    temp_list_number[random] + "",
                    SudukoConst.Cell_31,
                    SudukoConst.Room1
                )
                temp_list_.remove(temp_list_number[random])
                temp_list_number.removeAt(random)
                val random1 = genrateRandomNumber()
                dataManager.updateCellValue(
                    temp_list_number[random1] + "",
                    SudukoConst.Cell_34,
                    SudukoConst.Room1
                )
                temp_list_.remove(temp_list_number[random1])
                temp_list_number.removeAt(random1)
                temp_list_number.clear()
                temp_list_number.addAll(temp_list_)
                genrateSudokuCell_37()
            } else if (temp_list_number.size == 3) {
                val random = genrateRandomNumber()
                dataManager.updateCellValue(
                    temp_list_number[random] + "",
                    SudukoConst.Cell_31,
                    SudukoConst.Room1
                )
                temp_list_.remove(temp_list_number[random])
                temp_list_number.removeAt(random)
                val random1 = genrateRandomNumber()
                dataManager.updateCellValue(
                    temp_list_number[random1] + "",
                    SudukoConst.Cell_34,
                    SudukoConst.Room1
                )
                temp_list_.remove(temp_list_number[random1])
                temp_list_number.removeAt(random1)
                val random2 = genrateRandomNumber()
                dataManager.updateCellValue(
                    temp_list_number[random2] + "",
                    SudukoConst.Cell_37,
                    SudukoConst.Room1
                )
                temp_list_.remove(temp_list_number[random2])
                temp_list_number.removeAt(random2)
                genrateSudokuCell_61()
            }else{}
        } else {
            temp_list_number.addAll(temp_list_)
            genrateSudokuCell_31()
        }
    }

    private fun genrateSudokuCell_31() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_31,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_34()
    }

    private fun genrateSudokuCell_34() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_34,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_37()
    }

    private fun genrateSudokuCell_37() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_37,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_61()
    }

    private fun genrateSudokuCell_61() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudukoConst.Room1,
            SudukoConst.str_Column1,
            list_suduko1
        )
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_61,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_64()
    }

    private fun genrateSudokuCell_64() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_64,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_67()
    }

    private fun genrateSudokuCell_67() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_67,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_32()
    }

    private fun genrateSudokuCell_32() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudukoConst.Room1,
            SudukoConst.str_Grid3 + "," + SudukoConst.str_Column2, list_suduko1
        )
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_32,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_35()
    }

    private fun genrateSudokuCell_35() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_35,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_38()
    }

    private fun genrateSudokuCell_38() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_38,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_62()
    }

    private fun genrateSudokuCell_62() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudukoConst.Room1,
            SudukoConst.str_Grid6,
            list_suduko1
        )
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_62,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_65()
    }
    private fun genrateSudokuCell_65() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_65,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_68()
    }

    private fun genrateSudokuCell_68() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_68,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_40()
    }

    private suspend fun genrateSudokuCell_40() {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudukoConst.Room1,
            SudukoConst.str_Column3 + "," + SudukoConst.str_Row3,
            list_suduko1
        )
        if (temp_list_number.size > 0) {
            val random = genrateRandomNumber()
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_40,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            genrateSudokuCell_41()
        }else{

        }
    }



    private fun genrateSudokuCell_41() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudukoConst.Room1,
            SudukoConst.str_Column4 + "," + SudukoConst.str_Row3,
            list_suduko1
        )
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_41,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_42()
    }


    private fun genrateSudokuCell_42() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudukoConst.Room1,
            SudukoConst.str_Column5 + "," + SudukoConst.str_Row3,
            list_suduko1
        )
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_42,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        genrateSudokuCell_50()
    }

    private fun genrateSudokuCell_50() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudukoConst.Room1,
            SudukoConst.str_Row3,
            list_suduko1
        )
        val random = genrateRandomNumber()
        if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Column6,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_50,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            if (temp_list_number.size > 0) {
                val random1 = genrateRandomNumber()
                if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Column7,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_51,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Column8,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_52,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_43_Temp()
                        } else {
                            val temp_list_number_te = ArrayList<String>()
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1, SudukoConst.str_Column3,
                                    temp_list_number[0]
                                )
                            ) {
                                temp_list_number_te.add("40")
                            }
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1, SudukoConst.str_Column4,
                                    temp_list_number[0]
                                )
                            ) {
                                temp_list_number_te.add("41")
                            }
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1, SudukoConst.str_Column5,
                                    temp_list_number[0]
                                )
                            ) {
                                temp_list_number_te.add("42")
                            }
                            temp_list_number.clear()
                            temp_list_number.addAll(temp_list_number_te)
                            val random2 = genrateRandomNumber()

//                            Log.e("j_temp_list_number1_", "" + temp_list_number.size());
                            val str: String = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1: ArrayList<String> =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row3, list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column8,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_52,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_43_Temp()
                            } else {
                                temp_list_number.removeAt(random2)
                                val random3 = genrateRandomNumber()
                                val str1: String = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[random3]
                                )
                                if (dataManager.getRoomID_CellPosition_IN(
                                        SudukoConst.Room1,
                                        SudukoConst.str_Column8,
                                        str1
                                    )
                                ) {
                                    dataManager.updateCellValue(
                                        str1 + "",
                                        SudukoConst.Cell_52,
                                        SudukoConst.Room1
                                    )
                                    dataManager.updateCellValue(
                                        temp_list_number1[0] + "",
                                        temp_list_number[random3], SudukoConst.Room1
                                    )
                                    GenrateSudokuCell_43_Temp()
                                } else {
                                    temp_list_number.removeAt(random3)
                                    if (temp_list_number.size != 0) {
                                        val str2: String = dataManager.getRoomID_CellPositionString(
                                            SudukoConst.Room1,
                                            temp_list_number[0]
                                        )
                                        if (dataManager.getRoomID_CellPosition_IN(
                                                SudukoConst.Room1,
                                                SudukoConst.str_Column8,
                                                str2
                                            )
                                        ) {
                                            dataManager.updateCellValue(
                                                str1 + "",
                                                SudukoConst.Cell_52,
                                                SudukoConst.Room1
                                            )
                                            dataManager.updateCellValue(
                                                temp_list_number1[0] + "",
                                                temp_list_number[0], SudukoConst.Room1
                                            )
                                            GenrateSudokuCell_43_Temp()
                                        } else {
                                            StartAgain40()
                                        }
                                    } else {
                                        StartAgain40()
                                    }
                                }
                            }
                        }
                    }else{}
                } else if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Column8,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_52,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Column7,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_51,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_43_Temp()
                        } else {
                            val temp_list_number_te = ArrayList<String>()
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1, SudukoConst.str_Column3,
                                    temp_list_number[0]
                                )
                            ) {
                                temp_list_number_te.add("40")
                            }
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1, SudukoConst.str_Column4,
                                    temp_list_number[0]
                                )
                            ) {
                                temp_list_number_te.add("41")
                            }
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1, SudukoConst.str_Column5,
                                    temp_list_number[0]
                                )
                            ) {
                                temp_list_number_te.add("42")
                            }
                            temp_list_number.clear()
                            temp_list_number.addAll(temp_list_number_te)
                            val random2 = genrateRandomNumber()
                            val str: String = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1: ArrayList<String> =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row3, list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column7,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_51,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_43_Temp()
                            } else {
                                temp_list_number.removeAt(random2)
                                val random3 = genrateRandomNumber()
                                val str1: String = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[random3]
                                )
                                if (dataManager.getRoomID_CellPosition_IN(
                                        SudukoConst.Room1,
                                        SudukoConst.str_Column7,
                                        str1
                                    )
                                ) {
                                    dataManager.updateCellValue(
                                        str1 + "",
                                        SudukoConst.Cell_51,
                                        SudukoConst.Room1
                                    )
                                    dataManager.updateCellValue(
                                        temp_list_number1[0] + "",
                                        temp_list_number[random3], SudukoConst.Room1
                                    )
                                    GenrateSudokuCell_43_Temp()
                                } else {
                                    temp_list_number.removeAt(random3)
                                    if (temp_list_number.size != 0) {
                                        val str2: String = dataManager.getRoomID_CellPositionString(
                                            SudukoConst.Room1,
                                            temp_list_number[0]
                                        )
                                        if (dataManager.getRoomID_CellPosition_IN(
                                                SudukoConst.Room1,
                                                SudukoConst.str_Column7,
                                                str2
                                            )
                                        ) {
                                            dataManager.updateCellValue(
                                                str1 + "",
                                                SudukoConst.Cell_51,
                                                SudukoConst.Room1
                                            )
                                            dataManager.updateCellValue(
                                                temp_list_number1[0] + "",
                                                temp_list_number[0], SudukoConst.Room1
                                            )
                                            GenrateSudokuCell_43_Temp()
                                        } else {
                                            StartAgain40()
                                        }
                                    } else {
                                        StartAgain40()
                                    }
                                }
                            }
                        }
                    }else{}
                }else{}
            }else{}
        } else if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Column7,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_51,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            if (temp_list_number.size > 0) {
                val random1 = genrateRandomNumber()
                if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Column6,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_50,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Column8,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_52,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_43_Temp()
                        } else {
                            val temp_list_number_te = ArrayList<String>()
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1, SudukoConst.str_Column3,
                                    temp_list_number[0]
                                )
                            ) {
                                temp_list_number_te.add("40")
                            }
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1, SudukoConst.str_Column4,
                                    temp_list_number[0]
                                )
                            ) {
                                temp_list_number_te.add("41")
                            }
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1, SudukoConst.str_Column5,
                                    temp_list_number[0]
                                )
                            ) {
                                temp_list_number_te.add("42")
                            }
                            temp_list_number.clear()
                            temp_list_number.addAll(temp_list_number_te)
                            val random2 = genrateRandomNumber()
                            //                            Log.e("j_temp_list_number3_", "" + temp_list_number.size());
                            val str: String = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1: ArrayList<String> =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row3, list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column8,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_52,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_43_Temp()
                            } else {
                                temp_list_number.removeAt(random2)
                                val random3 = genrateRandomNumber()
                                val str1: String = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[random3]
                                )
                                if (dataManager.getRoomID_CellPosition_IN(
                                        SudukoConst.Room1,
                                        SudukoConst.str_Column8,
                                        str1
                                    )
                                ) {
                                    dataManager.updateCellValue(
                                        str1 + "",
                                        SudukoConst.Cell_52,
                                        SudukoConst.Room1
                                    )
                                    dataManager.updateCellValue(
                                        temp_list_number1[0] + "",
                                        temp_list_number[random3], SudukoConst.Room1
                                    )
                                    GenrateSudokuCell_43_Temp()
                                } else {
                                    temp_list_number.removeAt(random3)
                                    if (temp_list_number.size != 0) {
                                        val str2: String = dataManager.getRoomID_CellPositionString(
                                            SudukoConst.Room1,
                                            temp_list_number[0]
                                        )
                                        if (dataManager.getRoomID_CellPosition_IN(
                                                SudukoConst.Room1,
                                                SudukoConst.str_Column8,
                                                str2
                                            )
                                        ) {
                                            dataManager.updateCellValue(
                                                str1 + "",
                                                SudukoConst.Cell_52,
                                                SudukoConst.Room1
                                            )
                                            dataManager.updateCellValue(
                                                temp_list_number1[0] + "",
                                                temp_list_number[0], SudukoConst.Room1
                                            )
                                            GenrateSudokuCell_43_Temp()
                                        } else {
                                            StartAgain40()
                                        }
                                    } else {
                                        StartAgain40()
                                    }
                                }
                            }
                        }
                    }else{}
                } else if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Column8,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_52,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Column6,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_50,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_43_Temp()
                        } else {
                            val temp_list_number_te = ArrayList<String>()
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1, SudukoConst.str_Column3,
                                    temp_list_number[0]
                                )
                            ) {
                                temp_list_number_te.add("40")
                            }
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1, SudukoConst.str_Column4,
                                    temp_list_number[0]
                                )
                            ) {
                                temp_list_number_te.add("41")
                            }
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1, SudukoConst.str_Column5,
                                    temp_list_number[0]
                                )
                            ) {
                                temp_list_number_te.add("42")
                            }
                            temp_list_number.clear()
                            temp_list_number.addAll(temp_list_number_te)
                            val random2 = genrateRandomNumber()
                            //                            Log.e("j_temp_list_number4_", "" + temp_list_number.size());
                            val str: String = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1: ArrayList<String> =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row3, list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column6,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_50,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_43_Temp()
                            } else {
                                temp_list_number.removeAt(random2)
                                val random3 = genrateRandomNumber()
                                val str1: String = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[random3]
                                )
                                if (dataManager.getRoomID_CellPosition_IN(
                                        SudukoConst.Room1,
                                        SudukoConst.str_Column6,
                                        str1
                                    )
                                ) {
                                    dataManager.updateCellValue(
                                        str1 + "",
                                        SudukoConst.Cell_50,
                                        SudukoConst.Room1
                                    )
                                    dataManager.updateCellValue(
                                        temp_list_number1[0] + "",
                                        temp_list_number[random3], SudukoConst.Room1
                                    )
                                    GenrateSudokuCell_43_Temp()
                                } else {
                                    temp_list_number.removeAt(random3)
                                    if (temp_list_number.size != 0) {
                                        val str2: String = dataManager.getRoomID_CellPositionString(
                                            SudukoConst.Room1,
                                            temp_list_number[0]
                                        )
                                        if (dataManager.getRoomID_CellPosition_IN(
                                                SudukoConst.Room1,
                                                SudukoConst.str_Column6,
                                                str2
                                            )
                                        ) {
                                            dataManager.updateCellValue(
                                                str1 + "",
                                                SudukoConst.Cell_50,
                                                SudukoConst.Room1
                                            )
                                            dataManager.updateCellValue(
                                                temp_list_number1[0] + "",
                                                temp_list_number[0], SudukoConst.Room1
                                            )
                                            GenrateSudokuCell_43_Temp()
                                        } else {
                                            StartAgain40()
                                        }
                                    } else {
                                        StartAgain40()
                                    }
                                }
                            }
                        }
                    }else{}
                }else{}
            }else{}
        } else if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Column8,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_52,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            if (temp_list_number.size > 0) {
                val random1 = genrateRandomNumber()
                if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Column6,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_50,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Column7,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_51,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_43_Temp()
                        } else {
                            val temp_list_number_te = ArrayList<String>()
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1, SudukoConst.str_Column3,
                                    temp_list_number[0]
                                )
                            ) {
                                temp_list_number_te.add("40")
                            }
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1, SudukoConst.str_Column4,
                                    temp_list_number[0]
                                )
                            ) {
                                temp_list_number_te.add("41")
                            }
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1, SudukoConst.str_Column5,
                                    temp_list_number[0]
                                )
                            ) {
                                temp_list_number_te.add("42")
                            }
                            temp_list_number.clear()
                            temp_list_number.addAll(temp_list_number_te)
                            val random2 = genrateRandomNumber()
                            //                            Log.e("j_temp_list_number5_", "" + temp_list_number.size());
                            val str: String = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1: ArrayList<String> =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row3, list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column7,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_51,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_43_Temp()
                            } else {
                                temp_list_number.removeAt(random2)
                                val random3 = genrateRandomNumber()
                                val str1: String = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[random3]
                                )
                                if (dataManager.getRoomID_CellPosition_IN(
                                        SudukoConst.Room1,
                                        SudukoConst.str_Column7,
                                        str1
                                    )
                                ) {
                                    dataManager.updateCellValue(
                                        str1 + "",
                                        SudukoConst.Cell_51,
                                        SudukoConst.Room1
                                    )
                                    dataManager.updateCellValue(
                                        temp_list_number1[0] + "",
                                        temp_list_number[random3], SudukoConst.Room1
                                    )
                                    GenrateSudokuCell_43_Temp()
                                } else {
                                    temp_list_number.removeAt(random3)
                                    if (temp_list_number.size != 0) {
                                        val str2: String = dataManager.getRoomID_CellPositionString(
                                            SudukoConst.Room1,
                                            temp_list_number[0]
                                        )
                                        if (dataManager.getRoomID_CellPosition_IN(
                                                SudukoConst.Room1,
                                                SudukoConst.str_Column7,
                                                str2
                                            )
                                        ) {
                                            dataManager.updateCellValue(
                                                str1 + "",
                                                SudukoConst.Cell_51,
                                                SudukoConst.Room1
                                            )
                                            dataManager.updateCellValue(
                                                temp_list_number1[0] + "",
                                                temp_list_number[0], SudukoConst.Room1
                                            )
                                            GenrateSudokuCell_43_Temp()
                                        } else {
                                            StartAgain40()
                                        }
                                    } else {
                                        StartAgain40()
                                    }
                                }
                            }
                        }
                    }else{}
                } else if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Column7,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_51,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Column6,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_50,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_43_Temp()
                        } else {
                            val temp_list_number_te = ArrayList<String>()
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1, SudukoConst.str_Column3,
                                    temp_list_number[0]
                                )
                            ) {
                                temp_list_number_te.add("40")
                            }
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1, SudukoConst.str_Column4,
                                    temp_list_number[0]
                                )
                            ) {
                                temp_list_number_te.add("41")
                            }
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1, SudukoConst.str_Column5,
                                    temp_list_number[0]
                                )
                            ) {
                                temp_list_number_te.add("42")
                            }
                            temp_list_number.clear()
                            temp_list_number.addAll(temp_list_number_te)
                            val random2 = genrateRandomNumber()
                            //                            Log.e("j_temp_list_number6_", "" + temp_list_number.size());
                            val str: String = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1: ArrayList<String> =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row3, list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column6,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_50,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_43_Temp()
                            } else {
                                temp_list_number.removeAt(random2)
                                val random3 = genrateRandomNumber()
                                val str1: String = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[random3]
                                )
                                if (dataManager.getRoomID_CellPosition_IN(
                                        SudukoConst.Room1,
                                        SudukoConst.str_Column6,
                                        str1
                                    )
                                ) {
                                    dataManager.updateCellValue(
                                        str1 + "",
                                        SudukoConst.Cell_50,
                                        SudukoConst.Room1
                                    )
                                    dataManager.updateCellValue(
                                        temp_list_number1[0] + "",
                                        temp_list_number[random3], SudukoConst.Room1
                                    )
                                    GenrateSudokuCell_43_Temp()
                                } else {
                                    temp_list_number.removeAt(random3)
                                    if (temp_list_number.size != 0) {
                                        val str2: String = dataManager.getRoomID_CellPositionString(
                                            SudukoConst.Room1,
                                            temp_list_number[0]
                                        )
                                        if (dataManager.getRoomID_CellPosition_IN(
                                                SudukoConst.Room1,
                                                SudukoConst.str_Column6,
                                                str2
                                            )
                                        ) {
                                            dataManager.updateCellValue(
                                                str1 + "",
                                                SudukoConst.Cell_50,
                                                SudukoConst.Room1
                                            )
                                            dataManager.updateCellValue(
                                                temp_list_number1[0] + "",
                                                temp_list_number[0], SudukoConst.Room1
                                            )
                                            GenrateSudokuCell_43_Temp()
                                        } else {
                                            StartAgain40()
                                        }
                                    } else {
                                        StartAgain40()
                                    }
                                }
                            }
                        }
                    }else{}
                }else{}
            }else{}
        } else {
            StartAgain40()
        }
    }


    private fun GenrateSudokuCell_43_Temp() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        val temp_list_total_remain: ArrayList<String> =
            dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                SudukoConst.Room1,
                SudukoConst.str_Row4
                        + "," + SudukoConst.str_Grid4, list_suduko1
            )
        val temp_list__grid: ArrayList<String> = dataManager.getRoomID_CellPosition_IN_List_For_Add(
            SudukoConst.Room1,
            SudukoConst.str_Row3_G5, temp_list_total_remain
        )
        //        Log.e("temp_list_Grid", temp_list_total_remain.size() + "::same::" + temp_list__grid.size());
        if (temp_list__grid.size > 0) {
            if (temp_list__grid.size == 1) {
                if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_C3_G1,
                        temp_list__grid[0]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list__grid[0] + "",
                        SudukoConst.Cell_43,
                        SudukoConst.Room1
                    )
                } else if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_C4_G1,
                        temp_list__grid[0]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list__grid[0] + "",
                        SudukoConst.Cell_44,
                        SudukoConst.Room1
                    )
                } else if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_C5_G1,
                        temp_list__grid[0]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list__grid[0] + "",
                        SudukoConst.Cell_45,
                        SudukoConst.Room1
                    )
                }
                GenrateSudokuCell_43()
            } else if (temp_list__grid.size == 2) {
                temp_list_number.addAll(temp_list__grid)
                val random = genrateRandomNumber()
                if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_C3_G1,
                        temp_list_number[random]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random] + "",
                        SudukoConst.Cell_43,
                        SudukoConst.Room1
                    )
                    temp_list_number.remove(temp_list_number[random])
                    if (dataManager.getRoomID_CellPosition_IN(
                            SudukoConst.Room1, SudukoConst.str_C4_G1,
                            temp_list_number[0]
                        )
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number[0] + "",
                            SudukoConst.Cell_44,
                            SudukoConst.Room1
                        )
                    } else if (dataManager.getRoomID_CellPosition_IN(
                            SudukoConst.Room1, SudukoConst.str_C5_G1,
                            temp_list_number[0]
                        )
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number[0] + "",
                            SudukoConst.Cell_45,
                            SudukoConst.Room1
                        )
                    }
                    GenrateSudokuCell_43()
                } else if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_C4_G1,
                        temp_list_number[random]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random] + "",
                        SudukoConst.Cell_44,
                        SudukoConst.Room1
                    )
                    temp_list_number.remove(temp_list_number[random])
                    if (dataManager.getRoomID_CellPosition_IN(
                            SudukoConst.Room1, SudukoConst.str_C3_G1,
                            temp_list_number[0]
                        )
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number[0] + "",
                            SudukoConst.Cell_43,
                            SudukoConst.Room1
                        )
                    } else if (dataManager.getRoomID_CellPosition_IN(
                            SudukoConst.Room1, SudukoConst.str_C5_G1,
                            temp_list_number[0]
                        )
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number[0] + "",
                            SudukoConst.Cell_45,
                            SudukoConst.Room1
                        )
                    }
                    GenrateSudokuCell_43()
                } else if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_C5_G1,
                        temp_list_number[0]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random] + "",
                        SudukoConst.Cell_45,
                        SudukoConst.Room1
                    )
                    temp_list_number.remove(temp_list_number[random])
                    if (dataManager.getRoomID_CellPosition_IN(
                            SudukoConst.Room1, SudukoConst.str_C3_G1,
                            temp_list_number[0]
                        )
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number[0] + "",
                            SudukoConst.Cell_43,
                            SudukoConst.Room1
                        )
                    } else if (dataManager.getRoomID_CellPosition_IN(
                            SudukoConst.Room1, SudukoConst.str_C4_G1,
                            temp_list_number[0]
                        )
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number[0] + "",
                            SudukoConst.Cell_44,
                            SudukoConst.Room1
                        )
                    }
                    GenrateSudokuCell_43()
                }
            } else if (temp_list__grid.size == 3) {
                temp_list_number.clear()
                temp_list_number.addAll(temp_list__grid)
                val random = genrateRandomNumber()
                //                Log.e("jigar_random_1", "" + temp_list_number.get(random));
                if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_C3_G1,
                        temp_list_number[random]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random] + "",
                        SudukoConst.Cell_43,
                        SudukoConst.Room1
                    )
                    temp_list_number.remove(temp_list_number[random])
                    val random1 = genrateRandomNumber()
                    //                    Log.e("jigar_random_2", "" + temp_list_number.get(random1));
                    if (dataManager.getRoomID_CellPosition_IN(
                            SudukoConst.Room1, SudukoConst.str_C4_G1,
                            temp_list_number[random1]
                        )
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number[random1] + "",
                            SudukoConst.Cell_44,
                            SudukoConst.Room1
                        )
                        temp_list_number.remove(temp_list_number[random1])
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_C5_G1,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_45,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_43()
                        } else {
                            val temp = temp_list_number[0]
                            temp_list_number.clear()
                            temp_list_number.add("43")
                            temp_list_number.add("44")
                            GenrateSudokuCell_45_Temp_Swipe("45", temp)
                        }
                    } else if (dataManager.getRoomID_CellPosition_IN(
                            SudukoConst.Room1, SudukoConst.str_C5_G1,
                            temp_list_number[random1]
                        )
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number[random1] + "",
                            SudukoConst.Cell_45,
                            SudukoConst.Room1
                        )
                        temp_list_number.remove(temp_list_number[random1])
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_C4_G1,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_44,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_43()
                        } else {
                            val temp = temp_list_number[0]
                            temp_list_number.clear()
                            temp_list_number.add("43")
                            temp_list_number.add("45")
                            GenrateSudokuCell_45_Temp_Swipe("44", temp)
                        }
                    }
                } else if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_C4_G1,
                        temp_list_number[random]
                    )
                ) {
//                    Log.e("jigar_random_3", "" + temp_list_number.get(random));
                    dataManager.updateCellValue(
                        temp_list_number[random] + "",
                        SudukoConst.Cell_44,
                        SudukoConst.Room1
                    )
                    temp_list_number.remove(temp_list_number[random])
                    val random1 = genrateRandomNumber()
                    //                    Log.e("jigar_random_3_1", "" + temp_list_number.get(random1));
                    if (dataManager.getRoomID_CellPosition_IN(
                            SudukoConst.Room1, SudukoConst.str_C3_G1,
                            temp_list_number[random1]
                        )
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number[random1] + "",
                            SudukoConst.Cell_43,
                            SudukoConst.Room1
                        )
                        temp_list_number.remove(temp_list_number[random1])
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_C5_G1,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_45,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_43()
                        } else {
                            val temp = temp_list_number[0]
                            temp_list_number.clear()
                            temp_list_number.add("43")
                            temp_list_number.add("44")
                            GenrateSudokuCell_45_Temp_Swipe("45", temp)
                        }
                    } else if (dataManager.getRoomID_CellPosition_IN(
                            SudukoConst.Room1, SudukoConst.str_C5_G1,
                            temp_list_number[random1]
                        )
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number[random1] + "",
                            SudukoConst.Cell_45,
                            SudukoConst.Room1
                        )
                        temp_list_number.remove(temp_list_number[random1])
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_C3_G1,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_43,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_43()
                        } else {
                            val temp = temp_list_number[0]
                            temp_list_number.clear()
                            temp_list_number.add("45")
                            temp_list_number.add("44")
                            GenrateSudokuCell_45_Temp_Swipe("43", temp)
                        }
                    }
                } else if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_C5_G1,
                        temp_list_number[random]
                    )
                ) {
//                    Log.e("jigar_random_4", "" + temp_list_number.get(random));
                    dataManager.updateCellValue(
                        temp_list_number[random] + "",
                        SudukoConst.Cell_45,
                        SudukoConst.Room1
                    )
                    temp_list_number.remove(temp_list_number[random])
                    val random1 = genrateRandomNumber()
                    //                    Log.e("jigar_random_4_", "" + temp_list_number.get(random1));
                    if (dataManager.getRoomID_CellPosition_IN(
                            SudukoConst.Room1, SudukoConst.str_C3_G1,
                            temp_list_number[random1]
                        )
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number[random1] + "",
                            SudukoConst.Cell_43,
                            SudukoConst.Room1
                        )
                        temp_list_number.remove(temp_list_number[random1])
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_C4_G1,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_44,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_43()
                        } else {
                            val temp = temp_list_number[0]
                            temp_list_number.clear()
                            temp_list_number.add("43")
                            temp_list_number.add("45")
                            GenrateSudokuCell_45_Temp_Swipe("44", temp)
                        }
                    } else if (dataManager.getRoomID_CellPosition_IN(
                            SudukoConst.Room1, SudukoConst.str_C4_G1,
                            temp_list_number[random]
                        )
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number[random1] + "",
                            SudukoConst.Cell_44,
                            SudukoConst.Room1
                        )
                        temp_list_number.remove(temp_list_number[random1])
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_C3_G1,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_43,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_43()
                        } else {
                            val temp = temp_list_number[0]
                            temp_list_number.clear()
                            temp_list_number.add("45")
                            temp_list_number.add("44")
                            GenrateSudokuCell_45_Temp_Swipe("43", temp)
                        }
                    }
                }
            }
        } else {
            GenrateSudokuCell_43()
        }
    }

    private fun GenrateSudokuCell_45_Temp_Swipe(position: String, temp: String) = runBlocking {
        if (temp_list_number.size > 0) {

//            Log.e("GenrateSudoku_45_Swipe", position + "::" + temp);
            val random1 = genrateRandomNumber()
            val value = dataManager.getRoomID_CellPositionString(
                SudukoConst.Room1,
                temp_list_number[random1]
            )
            if (dataManager.getRoomID_CellPosition_IN(
                    SudukoConst.Room1,
                    SudukoConst.str_C5_G1,
                    value
                )
            ) {
                dataManager.updateCellValue(value + "", position, SudukoConst.Room1)
                dataManager.updateCellValue(temp + "", temp_list_number[random1], SudukoConst.Room1)
                GenrateSudokuCell_43()
            } else {
                temp_list_number.remove(temp_list_number[random1])
                val value1 = dataManager.getRoomID_CellPositionString(
                    SudukoConst.Room1,
                    temp_list_number[0]
                )
                dataManager.updateCellValue(value1 + "", position, SudukoConst.Room1)
                dataManager.updateCellValue(temp + "", temp_list_number[0], SudukoConst.Room1)
                GenrateSudokuCell_43()
            }
        }
    }

    private fun GenrateSudokuCell_43() = runBlocking {
        if (dataManager.getRoomID_CellPositionString_Set_OR_Not(
                SudukoConst.Room1,
                SudukoConst.Cell_43
            )
        ) {
            val list_suduko1 = ArrayList<String>()
            list_suduko1.addAll(list_suduko)
            temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                SudukoConst.Room1,
                SudukoConst.str_Column3
                        + "," + SudukoConst.str_Grid4 + "," + SudukoConst.str_Row4, list_suduko1
            )

//            Log.e("jigar_cellpos_1_43", "" + temp_list_number.size());
            if (temp_list_number.size == 0) {
                val temp_list_number1: ArrayList<Suduko> =
                    dataManager.getRoomID_CellPosition_IN_List_For_Add(
                        SudukoConst.Room1,
                        SudukoConst.str_44_45
                    )
                //                Log.e("jigar_cellpos_2_43", "" + temp_list_number1.size());
                if (temp_list_number1.size == 1) {
                    val list_suduko2 = ArrayList<String>()
                    list_suduko2.addAll(list_suduko)
                    if (temp_list_number1[0].cellPosition == SudukoConst.Cell_45
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number1[0].cellValue.toString() + "",
                            SudukoConst.Cell_43,
                            SudukoConst.Room1
                        )
                        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                            SudukoConst.Room1,
                            (SudukoConst.str_Column5
                                    + "," + SudukoConst.str_Grid4 + "," + SudukoConst.str_Row4),
                            list_suduko2
                        )
                        val random1 = genrateRandomNumber()
                        dataManager.updateCellValue(
                            temp_list_number.get(random1) + "",
                            SudukoConst.Cell_45,
                            SudukoConst.Room1
                        )
                        GenrateSudokuCell_44()
                    } else {
                        dataManager.updateCellValue(
                            temp_list_number1[0].cellValue.toString() + "",
                            SudukoConst.Cell_43,
                            SudukoConst.Room1
                        )
                        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                            SudukoConst.Room1,
                            (SudukoConst.str_Column4
                                    + "," + SudukoConst.str_Grid4 + "," + SudukoConst.str_Row4),
                            list_suduko2
                        )
                        val random1 = genrateRandomNumber()
                        dataManager.updateCellValue(
                            temp_list_number.get(random1) + "",
                            SudukoConst.Cell_44,
                            SudukoConst.Room1
                        )
                        GenrateSudokuCell_44()
                    }
                } else if (temp_list_number1.size == 2) {
                    temp_list_number.clear()
                    temp_list_number.add("0")
                    temp_list_number.add("1")
                    val random1 = genrateRandomNumber()
                    val cellpos: String = temp_list_number1[random1].cellPosition
                    //                    Log.e("jigar_cellpos_random", "" + cellpos);
                    val list_suduko2 = ArrayList<String>()
                    list_suduko2.addAll(list_suduko)
                    if (cellpos.equals(SudukoConst.Cell_45, ignoreCase = true)) {
                        temp_list_number.clear()
                        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                            SudukoConst.Room1,
                            (SudukoConst.str_Column5
                                    + "," + SudukoConst.str_Grid4 + "," + SudukoConst.str_Row4),
                            list_suduko2
                        )

//                        for (int i = 0; i < temp_list_number.size(); i++) {
//                            Log.e("jigar_cellpos45", temp_list_number.get(i));
//                        }
                        val random = genrateRandomNumber()
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Column3,
                                temp_list_number1[random1].cellValue
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number1[random1].cellValue.toString() + "",
                                SudukoConst.Cell_43, SudukoConst.Room1
                            )
                            dataManager.updateCellValue(
                                temp_list_number.get(random) + "",
                                SudukoConst.Cell_45,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_44()
                        } else {
                            temp_list_number1.removeAt(random1)
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1, SudukoConst.str_Column3,
                                    temp_list_number1[0].cellValue
                                )
                            ) {
                                dataManager.updateCellValue(
                                    temp_list_number1[0].cellValue.toString() + "",
                                    SudukoConst.Cell_43, SudukoConst.Room1
                                )
                                temp_list_number.clear()
                                temp_list_number =
                                    dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                        SudukoConst.Room1,
                                        (SudukoConst.str_Column4
                                                + "," + SudukoConst.str_Grid4 + "," + SudukoConst.str_Row4),
                                        list_suduko2
                                    )
                                val random3 = genrateRandomNumber()
                                dataManager.updateCellValue(
                                    temp_list_number.get(random3) + "", SudukoConst.Cell_44,
                                    SudukoConst.Room1
                                )
                                GenrateSudokuCell_44()
                            }
                        }
                    } else {
                        val list_suduko22 = ArrayList<String>()
                        list_suduko22.addAll(list_suduko)
                        temp_list_number.clear()
                        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                            SudukoConst.Room1,
                            (SudukoConst.str_Column4
                                    + "," + SudukoConst.str_Grid4 + "," + SudukoConst.str_Row4),
                            list_suduko22
                        )

//                        for (int i = 0; i < temp_list_number.size(); i++) {
//                            Log.e("jigar_cellpos44", temp_list_number.get(i));
//                        }
                        val random = genrateRandomNumber()
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Column3,
                                temp_list_number1[random1].cellValue
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number1[random1].cellValue.toString() + "",
                                SudukoConst.Cell_43, SudukoConst.Room1
                            )
                            dataManager.updateCellValue(
                                temp_list_number.get(random) + "",
                                SudukoConst.Cell_44,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_44()
                        } else {
                            temp_list_number1.removeAt(random1)
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1, SudukoConst.str_Column3,
                                    temp_list_number1[0].cellValue
                                )
                            ) {
                                dataManager.updateCellValue(
                                    temp_list_number1[0].cellValue.toString() + "",
                                    SudukoConst.Cell_43, SudukoConst.Room1
                                )
                                temp_list_number.clear()
                                val list_suduko23 = ArrayList<String>()
                                list_suduko23.addAll(list_suduko)
                                temp_list_number =
                                    dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                        SudukoConst.Room1,
                                        (SudukoConst.str_Column5
                                                + "," + SudukoConst.str_Grid4 + "," + SudukoConst.str_Row4),
                                        list_suduko23
                                    )


//                                Log.e("jig_temp_list_number", "" + temp_list_number.size());
                                val random3 = genrateRandomNumber()
                                dataManager.updateCellValue(
                                    temp_list_number.get(random3) + "", SudukoConst.Cell_45,
                                    SudukoConst.Room1
                                )
                                GenrateSudokuCell_44()
                            }
                        }
                    }
                }
            } else {
                val random1 = genrateRandomNumber()
                dataManager.updateCellValue(
                    temp_list_number.get(random1) + "",
                    SudukoConst.Cell_43,
                    SudukoConst.Room1
                )
                GenrateSudokuCell_44()
            }
        } else {
            GenrateSudokuCell_44()
        }
    }


    private fun GenrateSudokuCell_44()  = runBlocking {
        if (dataManager.getRoomID_CellPositionString_Set_OR_Not(
                SudukoConst.Room1,
                SudukoConst.Cell_44
            )
        ) {
            val list_suduko1 = ArrayList<String>()
            list_suduko1.addAll(list_suduko)
            temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                SudukoConst.Room1, (SudukoConst.str_Column4
                        + "," + SudukoConst.str_Grid4 + "," + SudukoConst.str_Row4), list_suduko1
            )

//            Log.e("jigar_cellpos_1_44", "" + temp_list_number.size());
            if (temp_list_number.size == 0) {
                val temp_list_number1: ArrayList<Suduko> =
                    dataManager.getRoomID_CellPosition_IN_List_For_Add(
                        SudukoConst.Room1,
                        SudukoConst.str_43_45
                    )
                //                Log.e("jigar_cellpos_2_44", "" + temp_list_number1.size());
                if (temp_list_number1.size == 1) {
                    val list_suduko2 = ArrayList<String>()
                    list_suduko2.addAll(list_suduko)
                    if (temp_list_number1[0].cellPosition == SudukoConst.Cell_43
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number1[0].cellValue.toString() + "",
                            SudukoConst.Cell_44,
                            SudukoConst.Room1
                        )
                        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                            SudukoConst.Room1,
                            (SudukoConst.str_Column3
                                    + "," + SudukoConst.str_Grid4 + "," + SudukoConst.str_Row4),
                            list_suduko2
                        )
                        val random1 = genrateRandomNumber()
                        dataManager.updateCellValue(
                            temp_list_number.get(random1) + "",
                            SudukoConst.Cell_43,
                            SudukoConst.Room1
                        )
                        GenrateSudokuCell_45()
                    } else {
                        dataManager.updateCellValue(
                            temp_list_number1[0].cellValue.toString() + "",
                            SudukoConst.Cell_44,
                            SudukoConst.Room1
                        )
                        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                            SudukoConst.Room1,
                            (SudukoConst.str_Column5
                                    + "," + SudukoConst.str_Grid4 + "," + SudukoConst.str_Row4),
                            list_suduko2
                        )
                        val random1 = genrateRandomNumber()
                        dataManager.updateCellValue(
                            temp_list_number.get(random1) + "",
                            SudukoConst.Cell_45,
                            SudukoConst.Room1
                        )
                        GenrateSudokuCell_45()
                    }
                } else if (temp_list_number1.size == 2) {
                    temp_list_number.clear()
                    temp_list_number.add("0")
                    temp_list_number.add("1")
                    val random1 = genrateRandomNumber()
                    val cellpos: String = temp_list_number1[random1].cellPosition
                    //                    Log.e("jigar_cellpos_random", "" + cellpos);
                    val list_suduko2 = ArrayList<String>()
                    list_suduko2.addAll(list_suduko)
                    if (cellpos.equals(SudukoConst.Cell_43, ignoreCase = true)) {
                        temp_list_number.clear()
                        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                            SudukoConst.Room1,
                            (SudukoConst.str_Column3
                                    + "," + SudukoConst.str_Grid4 + "," + SudukoConst.str_Row4),
                            list_suduko2
                        )

//                        for (int i = 0; i < temp_list_number.size(); i++) {
//                            Log.e("jigar_cellpos43", temp_list_number.get(i));
//                        }
                        val random = genrateRandomNumber()
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Column4,
                                temp_list_number1[random1].cellValue
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number1[random1].cellValue.toString() + "",
                                SudukoConst.Cell_44, SudukoConst.Room1
                            )
                            dataManager.updateCellValue(
                                temp_list_number.get(random) + "",
                                SudukoConst.Cell_43,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_45()
                        } else {
                            temp_list_number1.removeAt(random1)
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1, SudukoConst.str_Column4,
                                    temp_list_number1[0].cellValue
                                )
                            ) {
                                dataManager.updateCellValue(
                                    temp_list_number1[0].cellValue.toString() + "",
                                    SudukoConst.Cell_44, SudukoConst.Room1
                                )
                                temp_list_number.clear()
                                temp_list_number =
                                    dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                        SudukoConst.Room1,
                                        (SudukoConst.str_Column5
                                                + "," + SudukoConst.str_Grid4 + "," + SudukoConst.str_Row4),
                                        list_suduko2
                                    )
                                val random3 = genrateRandomNumber()
                                dataManager.updateCellValue(
                                    temp_list_number.get(random3) + "", SudukoConst.Cell_45,
                                    SudukoConst.Room1
                                )
                                GenrateSudokuCell_45()
                            }
                        }
                    } else {
                        val list_suduko22 = ArrayList<String>()
                        list_suduko22.addAll(list_suduko)
                        temp_list_number.clear()
                        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                            SudukoConst.Room1,
                            (SudukoConst.str_Column5
                                    + "," + SudukoConst.str_Grid4 + "," + SudukoConst.str_Row4),
                            list_suduko22
                        )

//                        for (int i = 0; i < temp_list_number.size(); i++) {
//                            Log.e("jigar_cellpos44", temp_list_number.get(i));
//                        }
                        val random = genrateRandomNumber()
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Column4,
                                temp_list_number1[random1].cellValue
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number1[random1].cellValue.toString() + "",
                                SudukoConst.Cell_44, SudukoConst.Room1
                            )
                            dataManager.updateCellValue(
                                temp_list_number.get(random) + "",
                                SudukoConst.Cell_45,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_45()
                        } else {
                            temp_list_number1.removeAt(random1)
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1, SudukoConst.str_Column4,
                                    temp_list_number1[0].cellValue
                                )
                            ) {
                                dataManager.updateCellValue(
                                    temp_list_number1[0].cellValue.toString() + "",
                                    SudukoConst.Cell_44, SudukoConst.Room1
                                )
                                temp_list_number.clear()
                                val list_suduko23 = ArrayList<String>()
                                list_suduko23.addAll(list_suduko)
                                temp_list_number =
                                    dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                        SudukoConst.Room1,
                                        (SudukoConst.str_Column3
                                                + "," + SudukoConst.str_Grid4 + "," + SudukoConst.str_Row4),
                                        list_suduko23
                                    )


//                                Log.e("jig_temp_list_number", "" + temp_list_number.size());
                                val random3 = genrateRandomNumber()
                                dataManager.updateCellValue(
                                    temp_list_number.get(random3) + "", SudukoConst.Cell_43,
                                    SudukoConst.Room1
                                )
                                GenrateSudokuCell_45()
                            }
                        }
                    }
                }
            } else {
                val random1 = genrateRandomNumber()
                dataManager.updateCellValue(
                    temp_list_number.get(random1) + "",
                    SudukoConst.Cell_44,
                    SudukoConst.Room1
                )
                GenrateSudokuCell_45()
            }
        } else {
            GenrateSudokuCell_45()
        }
    }

    private fun GenrateSudokuCell_45() = runBlocking {
        if (dataManager.getRoomID_CellPositionString_Set_OR_Not(
                SudukoConst.Room1,
                SudukoConst.Cell_45
            )
        ) {
            val list_suduko1 = ArrayList<String>()
            list_suduko1.addAll(list_suduko)
            temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                SudukoConst.Room1, (SudukoConst.str_Column5
                        + "," + SudukoConst.str_Grid4 + "," + SudukoConst.str_Row4), list_suduko1
            )
            //            Log.e("jigar_cellpos_1_45", "" + temp_list_number.size());
            if (temp_list_number.size == 0) {
                val temp_list_number1: ArrayList<Suduko> =
                    dataManager.getRoomID_CellPosition_IN_List_For_Add(
                        SudukoConst.Room1,
                        SudukoConst.str_43_44
                    )
                //                Log.e("jigar_cellpos_2_45", "" + temp_list_number1.size());
                if (temp_list_number1.size == 1) {
                    val list_suduko2 = ArrayList<String>()
                    list_suduko2.addAll(list_suduko)
                    if (temp_list_number1[0].cellPosition == SudukoConst.Cell_43
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number1[0].cellValue.toString() + "",
                            SudukoConst.Cell_45,
                            SudukoConst.Room1
                        )
                        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                            SudukoConst.Room1,
                            (SudukoConst.str_Column3
                                    + "," + SudukoConst.str_Grid4 + "," + SudukoConst.str_Row4),
                            list_suduko2
                        )
                        val random1 = genrateRandomNumber()
                        dataManager.updateCellValue(
                            temp_list_number.get(random1) + "",
                            SudukoConst.Cell_43,
                            SudukoConst.Room1
                        )
                        GenrateSudokuCell_53()
                    } else {
                        dataManager.updateCellValue(
                            temp_list_number1[0].cellValue.toString() + "",
                            SudukoConst.Cell_45,
                            SudukoConst.Room1
                        )
                        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                            SudukoConst.Room1,
                            (SudukoConst.str_Column4
                                    + "," + SudukoConst.str_Grid4 + "," + SudukoConst.str_Row4),
                            list_suduko2
                        )
                        val random1 = genrateRandomNumber()
                        dataManager.updateCellValue(
                            temp_list_number.get(random1) + "",
                            SudukoConst.Cell_44,
                            SudukoConst.Room1
                        )
                        GenrateSudokuCell_53()
                    }
                } else if (temp_list_number1.size == 2) {
                    temp_list_number.clear()
                    temp_list_number.add("0")
                    temp_list_number.add("1")
                    val random1 = genrateRandomNumber()
                    val cellpos: String = temp_list_number1[random1].cellPosition
                    //                    Log.e("jigar_cellpos_random", "" + cellpos);
                    val list_suduko2 = ArrayList<String>()
                    list_suduko2.addAll(list_suduko)
                    if (cellpos.equals(SudukoConst.Cell_43, ignoreCase = true)) {
                        temp_list_number.clear()
                        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                            SudukoConst.Room1,
                            (SudukoConst.str_Column3
                                    + "," + SudukoConst.str_Grid4 + "," + SudukoConst.str_Row4),
                            list_suduko2
                        )

//                        for (int i = 0; i < temp_list_number.size(); i++) {
//                            Log.e("jigar_cellpos43", temp_list_number.get(i));
//                        }
                        val random = genrateRandomNumber()
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Column5,
                                temp_list_number1[random1].cellValue
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number1[random1].cellValue.toString() + "",
                                SudukoConst.Cell_45, SudukoConst.Room1
                            )
                            dataManager.updateCellValue(
                                temp_list_number.get(random) + "",
                                SudukoConst.Cell_43,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_53()
                        } else {
                            temp_list_number1.removeAt(random1)
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1, SudukoConst.str_Column5,
                                    temp_list_number1[0].cellValue
                                )
                            ) {
                                dataManager.updateCellValue(
                                    temp_list_number1[0].cellValue.toString() + "",
                                    SudukoConst.Cell_45, SudukoConst.Room1
                                )
                                val list_suduko22 = ArrayList<String>()
                                list_suduko22.addAll(list_suduko)
                                temp_list_number.clear()
                                temp_list_number =
                                    dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                        SudukoConst.Room1,
                                        (SudukoConst.str_Column4
                                                + "," + SudukoConst.str_Grid4 + "," + SudukoConst.str_Row4),
                                        list_suduko22
                                    )
                                val random3 = genrateRandomNumber()
                                dataManager.updateCellValue(
                                    temp_list_number.get(random3) + "", SudukoConst.Cell_44,
                                    SudukoConst.Room1
                                )
                                GenrateSudokuCell_53()
                            }
                        }
                    } else {
                        val list_suduko22 = ArrayList<String>()
                        list_suduko22.addAll(list_suduko)
                        temp_list_number.clear()
                        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                            SudukoConst.Room1,
                            (SudukoConst.str_Column4
                                    + "," + SudukoConst.str_Grid4 + "," + SudukoConst.str_Row4),
                            list_suduko22
                        )

//                        for (int i = 0; i < temp_list_number.size(); i++) {
//                            Log.e("jigar_cellpos44", temp_list_number.get(i));
//                        }
                        val random = genrateRandomNumber()
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Column5,
                                temp_list_number1[random1].cellValue
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number1[random1].cellValue.toString() + "",
                                SudukoConst.Cell_45, SudukoConst.Room1
                            )
                            dataManager.updateCellValue(
                                temp_list_number.get(random) + "",
                                SudukoConst.Cell_44,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_53()
                        } else {
                            temp_list_number1.removeAt(random1)
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1, SudukoConst.str_Column5,
                                    temp_list_number1[0].cellValue
                                )
                            ) {
                                dataManager.updateCellValue(
                                    temp_list_number1[0].cellValue.toString() + "",
                                    SudukoConst.Cell_45, SudukoConst.Room1
                                )
                                temp_list_number.clear()
                                val list_suduko23 = ArrayList<String>()
                                list_suduko23.addAll(list_suduko)
                                temp_list_number =
                                    dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                        SudukoConst.Room1,
                                        (SudukoConst.str_Column3
                                                + "," + SudukoConst.str_Grid4 + "," + SudukoConst.str_Row4),
                                        list_suduko23
                                    )


//                                Log.e("jig_temp_list_number", "" + temp_list_number.size());
                                val random3 = genrateRandomNumber()
                                dataManager.updateCellValue(
                                    temp_list_number.get(random3) + "", SudukoConst.Cell_43,
                                    SudukoConst.Room1
                                )
                                GenrateSudokuCell_53()
                            }
                        }
                    }
                }
            } else {
                val random1 = genrateRandomNumber()
                dataManager.updateCellValue(
                    temp_list_number.get(random1) + "",
                    SudukoConst.Cell_45,
                    SudukoConst.Room1
                )
                GenrateSudokuCell_53()
            }
        } else {
            GenrateSudokuCell_53()
        }
    }


    private fun GenrateSudokuCell_53() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudukoConst.Room1,
            SudukoConst.str_Row4,
            list_suduko1
        )
        if (temp_list_number.size > 0) {

//            for (int i = 0; i < temp_list_number.size(); i++) {
//                Log.e("GenrateSudoku_53_total", "" + temp_list_number.get(i));
//            }
            genrateSudokuCell_53_temp()
        }
    }

    private fun genrateSudokuCell_53_temp() = runBlocking {
        val random = genrateRandomNumber()
        //        Log.e("GenrateSudokuCell_53_1", "" + temp_list_number.get(random));
        if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Column6,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number.get(random) + "",
                SudukoConst.Cell_53,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            val random1 = genrateRandomNumber()
            //            Log.e("GenrateSudokuCell_53_2", "" + temp_list_number.get(random1));
            if (dataManager.getRoomID_CellPosition_IN(
                    SudukoConst.Room1, SudukoConst.str_Column7,
                    temp_list_number[random1]
                )
            ) {
                dataManager.updateCellValue(
                    temp_list_number.get(random1) + "",
                    SudukoConst.Cell_54,
                    SudukoConst.Room1
                )
                temp_list_number.removeAt(random1)
                if (temp_list_number.size > 0) {
                    if (dataManager.getRoomID_CellPosition_IN(
                            SudukoConst.Room1, SudukoConst.str_Column8,
                            temp_list_number[0]
                        )
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number.get(0) + "",
                            SudukoConst.Cell_55,
                            SudukoConst.Room1
                        )
                        GenrateSudokuCell_46()
                    } else {
                        temp_list_number.clear()
                        temp_list_number.add("53")
                        temp_list_number.add("54")
                        val random2 = genrateRandomNumber()
                        val str = dataManager.getRoomID_CellPositionString(
                            SudukoConst.Room1,
                            temp_list_number[random2]
                        )
                        val list_suduko11 = ArrayList<String>()
                        list_suduko11.addAll(list_suduko)
                        val temp_list_number1 =
                            dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                SudukoConst.Room1,
                                SudukoConst.str_Row4,
                                list_suduko11
                            )
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1,
                                SudukoConst.str_Column8,
                                str
                            )
                        ) {
                            dataManager.updateCellValue(
                                str + "",
                                SudukoConst.Cell_55,
                                SudukoConst.Room1
                            )
                            dataManager.updateCellValue(
                                temp_list_number1.get(0) + "",
                                temp_list_number[random2], SudukoConst.Room1
                            )
                            GenrateSudokuCell_46()
                        } else {
                            temp_list_number.removeAt(random2)
                            val str1 = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[0]
                            )
                            dataManager.updateCellValue(
                                str1 + "",
                                SudukoConst.Cell_55,
                                SudukoConst.Room1
                            )
                            dataManager.updateCellValue(
                                temp_list_number1.get(0) + "",
                                temp_list_number[0], SudukoConst.Room1
                            )
                            GenrateSudokuCell_46()
                        }
                    }
                }
            } else if (dataManager.getRoomID_CellPosition_IN(
                    SudukoConst.Room1, SudukoConst.str_Column8,
                    temp_list_number[random1]
                )
            ) {
                dataManager.updateCellValue(
                    temp_list_number.get(random1) + "",
                    SudukoConst.Cell_55,
                    SudukoConst.Room1
                )
                temp_list_number.removeAt(random1)
                if (temp_list_number.size > 0) {
                    if (dataManager.getRoomID_CellPosition_IN(
                            SudukoConst.Room1, SudukoConst.str_Column7,
                            temp_list_number[0]
                        )
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number.get(0) + "",
                            SudukoConst.Cell_54,
                            SudukoConst.Room1
                        )
                        GenrateSudokuCell_46()
                    } else {
                        temp_list_number.clear()
                        temp_list_number.add("53")
                        temp_list_number.add("55")
                        val random2 = genrateRandomNumber()
                        val str = dataManager.getRoomID_CellPositionString(
                            SudukoConst.Room1,
                            temp_list_number[random2]
                        )
                        val list_suduko11 = ArrayList<String>()
                        list_suduko11.addAll(list_suduko)
                        val temp_list_number1 =
                            dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                SudukoConst.Room1,
                                SudukoConst.str_Row4,
                                list_suduko11
                            )
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1,
                                SudukoConst.str_Column8,
                                str
                            )
                        ) {
                            dataManager.updateCellValue(
                                str + "",
                                SudukoConst.Cell_54,
                                SudukoConst.Room1
                            )
                            dataManager.updateCellValue(
                                temp_list_number1.get(0) + "",
                                temp_list_number[random2], SudukoConst.Room1
                            )
                            GenrateSudokuCell_46()
                        } else {
                            temp_list_number.removeAt(random2)
                            val str1 = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[0]
                            )
                            dataManager.updateCellValue(
                                str1 + "",
                                SudukoConst.Cell_54,
                                SudukoConst.Room1
                            )
                            dataManager.updateCellValue(
                                temp_list_number1.get(0) + "",
                                temp_list_number[0], SudukoConst.Room1
                            )
                            GenrateSudokuCell_46()
                        }
                    }
                }
            }
        } else if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Column7,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number.get(random) + "",
                SudukoConst.Cell_54,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            if (temp_list_number.size > 0) {
                val random1 = genrateRandomNumber()
                //                Log.e("GenrateSudokuCell_53_3", "" + temp_list_number.get(random1));
                if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Column6,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number.get(random1) + "",
                        SudukoConst.Cell_53,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    //
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Column8,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number.get(0) + "",
                                SudukoConst.Cell_55,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_46()
                        } else {
                            temp_list_number.clear()
                            temp_list_number.add("53")
                            temp_list_number.add("54")
                            val random2 = genrateRandomNumber()
                            val str = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1 =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row4,
                                    list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column8,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_55,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1.get(0) + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_46()
                            } else {
                                temp_list_number.removeAt(random2)
                                val str1 = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[0]
                                )
                                dataManager.updateCellValue(
                                    str1 + "",
                                    SudukoConst.Cell_55,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1.get(0) + "",
                                    temp_list_number[0], SudukoConst.Room1
                                )
                                GenrateSudokuCell_46()
                            }
                        }
                    }
                } else if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Column8,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number.get(random1) + "",
                        SudukoConst.Cell_55,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Column6,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number.get(0) + "",
                                SudukoConst.Cell_53,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_46()
                        } else {
                            temp_list_number.clear()
                            temp_list_number.add("54")
                            temp_list_number.add("55")
                            val random2 = genrateRandomNumber()
                            val str = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1 =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row4,
                                    list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column8,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_53,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1.get(0) + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_46()
                            } else {
                                temp_list_number.removeAt(random2)
                                val str1 = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[0]
                                )
                                dataManager.updateCellValue(
                                    str1 + "",
                                    SudukoConst.Cell_53,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1.get(0) + "",
                                    temp_list_number[0], SudukoConst.Room1
                                )
                                GenrateSudokuCell_46()
                            }
                        }
                    }
                }
            }
        } else if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Column8,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number.get(random) + "",
                SudukoConst.Cell_55,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            if (temp_list_number.size > 0) {
                val random1 = genrateRandomNumber()
                //                Log.e("GenrateSudokuCell_53_4", "" + temp_list_number.get(random1));
                if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Column6,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number.get(random1) + "",
                        SudukoConst.Cell_53,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Column7,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number.get(0) + "",
                                SudukoConst.Cell_54,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_46()
                        } else {
                            temp_list_number.clear()
                            temp_list_number.add("53")
                            temp_list_number.add("55")
                            val random2 = genrateRandomNumber()
                            val str = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1 =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row4,
                                    list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column8,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_54,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1.get(0) + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_46()
                            } else {
                                temp_list_number.removeAt(random2)
                                val str1 = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[0]
                                )
                                dataManager.updateCellValue(
                                    str1 + "",
                                    SudukoConst.Cell_54,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1.get(0) + "",
                                    temp_list_number[0], SudukoConst.Room1
                                )
                                GenrateSudokuCell_46()
                            }
                        }
                    }
                } else if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Column7,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number.get(random1) + "",
                        SudukoConst.Cell_54,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Column6,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number.get(0) + "",
                                SudukoConst.Cell_53,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_46()
                        } else {
                            temp_list_number.clear()
                            temp_list_number.add("55")
                            temp_list_number.add("54")
                            val random2 = genrateRandomNumber()
                            val str = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1 =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row4,
                                    list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column8,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_53,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1.get(0) + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_46()
                            } else {
                                temp_list_number.removeAt(random2)
                                val str1 = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[0]
                                )
                                dataManager.updateCellValue(
                                    str1 + "",
                                    SudukoConst.Cell_53,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1.get(0) + "",
                                    temp_list_number[0], SudukoConst.Room1
                                )
                                GenrateSudokuCell_46()
                            }
                        }
                    }
                }
            }
        } else {
        }
    }


    private fun GenrateSudokuCell_46() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudukoConst.Room1,
            SudukoConst.str_Grid4,
            list_suduko1
        )
        if (temp_list_number.size > 0) {
            genrateSudokuCell_46_Temp()
        }
    }

    private fun genrateSudokuCell_46_Temp() = runBlocking {
        val random = genrateRandomNumber()
        //        Log.e("GenrateSudokuCell_46_1", "" + temp_list_number.get(random));
        if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Column3,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_46,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            val random1 = genrateRandomNumber()
            //            Log.e("GenrateSudokuCell_46_2", "" + temp_list_number.get(random1));
            if (dataManager.getRoomID_CellPosition_IN(
                    SudukoConst.Room1, SudukoConst.str_Column4,
                    temp_list_number[random1]
                )
            ) {
                dataManager.updateCellValue(
                    temp_list_number[random1] + "",
                    SudukoConst.Cell_47,
                    SudukoConst.Room1
                )
                temp_list_number.removeAt(random1)
                if (temp_list_number.size > 0) {
                    if (dataManager.getRoomID_CellPosition_IN(
                            SudukoConst.Room1, SudukoConst.str_Column5,
                            temp_list_number[0]
                        )
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number[0] + "",
                            SudukoConst.Cell_48,
                            SudukoConst.Room1
                        )
                        GenrateSudokuCell_56()
                    } else {
                        StartAgain40()
                    }
                }else{}
            } else if (dataManager.getRoomID_CellPosition_IN(
                    SudukoConst.Room1, SudukoConst.str_Column5,
                    temp_list_number[random1]
                )
            ) {
                dataManager.updateCellValue(
                    temp_list_number[random1] + "",
                    SudukoConst.Cell_48,
                    SudukoConst.Room1
                )
                temp_list_number.removeAt(random1)
                if (temp_list_number.size > 0) {
                    if (dataManager.getRoomID_CellPosition_IN(
                            SudukoConst.Room1, SudukoConst.str_Column4,
                            temp_list_number[0]
                        )
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number[0] + "",
                            SudukoConst.Cell_47,
                            SudukoConst.Room1
                        )
                        GenrateSudokuCell_56()
                    } else {
                        StartAgain40()
                    }
                }else{}
            }else{}
        } else if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Column4,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_47,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            if (temp_list_number.size > 0) {
                val random1 = genrateRandomNumber()
                //                Log.e("GenrateSudokuCell_46_3", "" + temp_list_number.get(random1));
                if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Column3,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_46,
                        SudukoConst.Room1
                    )
                    //                    Log.e("GenrateSudokuCell_46_3_", "" + temp_list_number.get(random1));
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Column5,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_48,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_56()
                        } else {
                            StartAgain40()
                        }
                    }else{}
                } else if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Column5,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_48,
                        SudukoConst.Room1
                    )
                    //                    Log.e("GenrateSudokuCell_46_3_", "::" + temp_list_number.get(random1));
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Column3,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_46,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_56()
                        } else {
                            StartAgain40()
                        }
                    }else{}
                }else{}
            }else{}
        } else if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Column5,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_48,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            if (temp_list_number.size > 0) {
                val random1 = genrateRandomNumber()
                //                Log.e("GenrateSudokuCell_46_4", "" + temp_list_number.get(random1));
                if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Column3,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_46,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Column4,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_47,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_56()
                        } else {
                            StartAgain40()
                        }
                    }else{}
                } else if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Column4,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_47,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Column3,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_46,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_56()
                        } else {
                            StartAgain40()
                        }
                    }else{}
                }else{}
            }else{}
        } else {
        }
    }

    private fun GenrateSudokuCell_56() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudukoConst.Room1,
            SudukoConst.str_Row5,
            list_suduko1
        )
        if (temp_list_number.size > 0) {
            GenrateSudokuCell_56_Temp()
        }
    }

    private fun GenrateSudokuCell_56_Temp() = runBlocking {
        val random = genrateRandomNumber()
        //        Log.e("GenrateSudokuCell_53_1", "" + temp_list_number.get(random));
        if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Column6,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_56,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            val random1 = genrateRandomNumber()
            //            Log.e("GenrateSudokuCell_56_2", "" + temp_list_number.get(random1));
            if (dataManager.getRoomID_CellPosition_IN(
                    SudukoConst.Room1, SudukoConst.str_Column7,
                    temp_list_number[random1]
                )
            ) {
                dataManager.updateCellValue(
                    temp_list_number[random1] + "",
                    SudukoConst.Cell_57,
                    SudukoConst.Room1
                )
                temp_list_number.removeAt(random1)
                if (temp_list_number.size > 0) {
                    if (dataManager.getRoomID_CellPosition_IN(
                            SudukoConst.Room1, SudukoConst.str_Column8,
                            temp_list_number[0]
                        )
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number[0] + "",
                            SudukoConst.Cell_58,
                            SudukoConst.Room1
                        )
                        GenrateSudokuCell_70()
                    } else {
                        temp_list_number.clear()
                        temp_list_number.add("56")
                        temp_list_number.add("57")
                        val random2 = genrateRandomNumber()
                        val str = dataManager.getRoomID_CellPositionString(
                            SudukoConst.Room1,
                            temp_list_number[random2]
                        )
                        val list_suduko11 = ArrayList<String>()
                        list_suduko11.addAll(list_suduko)
                        val temp_list_number1 =
                            dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                SudukoConst.Room1,
                                SudukoConst.str_Row5,
                                list_suduko11
                            )
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1,
                                SudukoConst.str_Column8,
                                str
                            )
                        ) {
                            dataManager.updateCellValue(
                                str + "",
                                SudukoConst.Cell_58,
                                SudukoConst.Room1
                            )
                            dataManager.updateCellValue(
                                temp_list_number1[0] + "",
                                temp_list_number[random2], SudukoConst.Room1
                            )
                            GenrateSudokuCell_70()
                        } else {
                            temp_list_number.removeAt(random2)
                            val str1 = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[0]
                            )
                            dataManager.updateCellValue(
                                str1 + "",
                                SudukoConst.Cell_58,
                                SudukoConst.Room1
                            )
                            dataManager.updateCellValue(
                                temp_list_number1[0] + "",
                                temp_list_number[0], SudukoConst.Room1
                            )
                            GenrateSudokuCell_70()
                        }
                    }
                }
            } else if (dataManager.getRoomID_CellPosition_IN(
                    SudukoConst.Room1, SudukoConst.str_Column8,
                    temp_list_number[random1]
                )
            ) {
                dataManager.updateCellValue(
                    temp_list_number[random1] + "",
                    SudukoConst.Cell_58,
                    SudukoConst.Room1
                )
                temp_list_number.removeAt(random1)
                if (temp_list_number.size > 0) {
                    if (dataManager.getRoomID_CellPosition_IN(
                            SudukoConst.Room1, SudukoConst.str_Column7,
                            temp_list_number[0]
                        )
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number[0] + "",
                            SudukoConst.Cell_57,
                            SudukoConst.Room1
                        )
                        GenrateSudokuCell_70()
                    } else {
                        temp_list_number.clear()
                        temp_list_number.add("56")
                        temp_list_number.add("58")
                        val random2 = genrateRandomNumber()
                        val str = dataManager.getRoomID_CellPositionString(
                            SudukoConst.Room1,
                            temp_list_number[random2]
                        )
                        val list_suduko11 = ArrayList<String>()
                        list_suduko11.addAll(list_suduko)
                        val temp_list_number1 =
                            dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                SudukoConst.Room1,
                                SudukoConst.str_Row5,
                                list_suduko11
                            )
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1,
                                SudukoConst.str_Column8,
                                str
                            )
                        ) {
                            dataManager.updateCellValue(
                                str + "",
                                SudukoConst.Cell_57,
                                SudukoConst.Room1
                            )
                            dataManager.updateCellValue(
                                temp_list_number1[0] + "",
                                temp_list_number[random2], SudukoConst.Room1
                            )
                            GenrateSudokuCell_70()
                        } else {
                            temp_list_number.removeAt(random2)
                            val str1 = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[0]
                            )
                            dataManager.updateCellValue(
                                str1 + "",
                                SudukoConst.Cell_57,
                                SudukoConst.Room1
                            )
                            dataManager.updateCellValue(
                                temp_list_number1[0] + "",
                                temp_list_number[0], SudukoConst.Room1
                            )
                            GenrateSudokuCell_70()
                        }
                    }
                }
            }
        } else if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Column7,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_57,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            if (temp_list_number.size > 0) {
                val random1 = genrateRandomNumber()
                //                Log.e("GenrateSudokuCell_56_3", "" + temp_list_number.get(random1));
                if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Column6,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_56,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    //
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Column8,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_58,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_70()
                        } else {
                            temp_list_number.clear()
                            temp_list_number.add("56")
                            temp_list_number.add("57")
                            val random2 = genrateRandomNumber()
                            val str = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1 =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row5,
                                    list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column8,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_58,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_70()
                            } else {
                                temp_list_number.removeAt(random2)
                                val str1 = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[0]
                                )
                                dataManager.updateCellValue(
                                    str1 + "",
                                    SudukoConst.Cell_58,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[0], SudukoConst.Room1
                                )
                                GenrateSudokuCell_70()
                            }
                        }
                    }
                } else if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Column8,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_58,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Column6,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_56,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_70()
                        } else {
                            temp_list_number.clear()
                            temp_list_number.add("57")
                            temp_list_number.add("58")
                            val random2 = genrateRandomNumber()
                            val str = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1 =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row5,
                                    list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column8,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_56,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_70()
                            } else {
                                temp_list_number.removeAt(random2)
                                val str1 = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[0]
                                )
                                dataManager.updateCellValue(
                                    str1 + "",
                                    SudukoConst.Cell_56,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[0], SudukoConst.Room1
                                )
                                GenrateSudokuCell_70()
                            }
                        }
                    }
                }
            }
        } else if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Column8,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_58,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            if (temp_list_number.size > 0) {
                val random1 = genrateRandomNumber()
                //                Log.e("GenrateSudokuCell_56_4", "" + temp_list_number.get(random1));
                if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Column6,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_56,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Column7,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_57,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_70()
                        } else {
                            temp_list_number.clear()
                            temp_list_number.add("56")
                            temp_list_number.add("58")
                            val random2 = genrateRandomNumber()
                            val str = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1 =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row5,
                                    list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column8,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_57,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_70()
                            } else {
                                temp_list_number.removeAt(random2)
                                val str1 = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[0]
                                )
                                dataManager.updateCellValue(
                                    str1 + "",
                                    SudukoConst.Cell_57,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[0], SudukoConst.Room1
                                )
                                GenrateSudokuCell_70()
                            }
                        }
                    }
                } else if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Column7,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_57,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Column6,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_56,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_70()
                        } else {
                            temp_list_number.clear()
                            temp_list_number.add("58")
                            temp_list_number.add("57")
                            val random2 = genrateRandomNumber()
                            val str = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1 =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row5,
                                    list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column8,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_56,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_70()
                            } else {
                                temp_list_number.removeAt(random2)
                                val str1 = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[0]
                                )
                                dataManager.updateCellValue(
                                    str1 + "",
                                    SudukoConst.Cell_56,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[0], SudukoConst.Room1
                                )
                                GenrateSudokuCell_70()
                            }
                        }
                    }
                }
            }
        } else {
        }
    }

    // TODO 70
    private suspend fun GenrateSudokuCell_70() {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudukoConst.Room1,
            SudukoConst.str_Column3,
            list_suduko1
        )

        if (temp_list_number.size > 0) {
            GenrateSudokuCell_70_Temp()
        }else{
            createSudo()
        }
    }

    private fun GenrateSudokuCell_70_Temp() = runBlocking {
        val random = genrateRandomNumber()
        //        Log.e("GenrateSudokuCell_70_1", "" + temp_list_number.get(random));
        if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Row6,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_70,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            val random1 = genrateRandomNumber()
            //            Log.e("GenrateSudokuCell_70_2", "" + temp_list_number.get(random1));
            if (dataManager.getRoomID_CellPosition_IN(
                    SudukoConst.Room1, SudukoConst.str_Row7,
                    temp_list_number[random1]
                )
            ) {
                dataManager.updateCellValue(
                    temp_list_number[random1] + "",
                    SudukoConst.Cell_73,
                    SudukoConst.Room1
                )
                temp_list_number.removeAt(random1)
                if (temp_list_number.size > 0) {
                    if (dataManager.getRoomID_CellPosition_IN(
                            SudukoConst.Room1, SudukoConst.str_Row8,
                            temp_list_number[0]
                        )
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number[0] + "",
                            SudukoConst.Cell_76,
                            SudukoConst.Room1
                        )
                        GenrateSudokuCell_71()
                    } else {
                        temp_list_number.clear()
                        temp_list_number.add("70")
                        temp_list_number.add("73")
                        val random2 = genrateRandomNumber()
                        val str = dataManager.getRoomID_CellPositionString(
                            SudukoConst.Room1,
                            temp_list_number[random2]
                        )
                        val list_suduko11 = ArrayList<String>()
                        list_suduko11.addAll(list_suduko)
                        val temp_list_number1 =
                            dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                SudukoConst.Room1,
                                SudukoConst.str_Column3, list_suduko11
                            )
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1,
                                SudukoConst.str_Row8,
                                str
                            )
                        ) {
                            dataManager.updateCellValue(
                                str + "",
                                SudukoConst.Cell_76,
                                SudukoConst.Room1
                            )
                            dataManager.updateCellValue(
                                temp_list_number1[0] + "",
                                temp_list_number[random2], SudukoConst.Room1
                            )
                            GenrateSudokuCell_71()
                        } else {
                            temp_list_number.removeAt(random2)
                            val str1 = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[0]
                            )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row8,
                                    str1
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str1 + "",
                                    SudukoConst.Cell_76,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[0], SudukoConst.Room1
                                )
                                GenrateSudokuCell_71()
                            } else {
                                StartAgain60()
                            }
                        }
                    }
                }else{}
            } else if (dataManager.getRoomID_CellPosition_IN(
                    SudukoConst.Room1, SudukoConst.str_Row8,
                    temp_list_number[random1]
                )
            ) {
                dataManager.updateCellValue(
                    temp_list_number[random1] + "",
                    SudukoConst.Cell_76,
                    SudukoConst.Room1
                )
                temp_list_number.removeAt(random1)
                if (temp_list_number.size > 0) {
                    if (dataManager.getRoomID_CellPosition_IN(
                            SudukoConst.Room1, SudukoConst.str_Row7,
                            temp_list_number[0]
                        )
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number[0] + "",
                            SudukoConst.Cell_73,
                            SudukoConst.Room1
                        )
                        GenrateSudokuCell_71()
                    } else {
                        temp_list_number.clear()
                        temp_list_number.add("70")
                        temp_list_number.add("76")
                        val random2 = genrateRandomNumber()
                        val str = dataManager.getRoomID_CellPositionString(
                            SudukoConst.Room1,
                            temp_list_number[random2]
                        )
                        val list_suduko11 = ArrayList<String>()
                        list_suduko11.addAll(list_suduko)
                        val temp_list_number1 =
                            dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                SudukoConst.Room1,
                                SudukoConst.str_Column3,
                                list_suduko11
                            )
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1,
                                SudukoConst.str_Row7,
                                str
                            )
                        ) {
                            dataManager.updateCellValue(
                                str + "",
                                SudukoConst.Cell_73,
                                SudukoConst.Room1
                            )
                            dataManager.updateCellValue(
                                temp_list_number1[0] + "",
                                temp_list_number[random2], SudukoConst.Room1
                            )
                            GenrateSudokuCell_71()
                        } else {
                            temp_list_number.removeAt(random2)
                            val str1 = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[0]
                            )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row7,
                                    str1
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str1 + "",
                                    SudukoConst.Cell_73,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[0], SudukoConst.Room1
                                )
                                GenrateSudokuCell_71()
                            } else {
                                StartAgain60()
                            }
                        }
                    }
                }else{}
            } else {
                StartAgain60()
            }
        } else if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Row7,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_73,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            if (temp_list_number.size > 0) {
                val random1 = genrateRandomNumber()
                //                Log.e("GenrateSudokuCell_70_3", "" + temp_list_number.get(random1));
                if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Row6,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_70,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    //
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Row8,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_76,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_71()
                        } else {
                            temp_list_number.clear()
                            temp_list_number.add("70")
                            temp_list_number.add("73")
                            val random2 = genrateRandomNumber()
                            val str = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1 =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column3,
                                    list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row8,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_76,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_71()
                            } else {
                                temp_list_number.removeAt(random2)
                                val str1 = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[0]
                                )
                                if (dataManager.getRoomID_CellPosition_IN(
                                        SudukoConst.Room1,
                                        SudukoConst.str_Row8,
                                        str1
                                    )
                                ) {
                                    dataManager.updateCellValue(
                                        str1 + "",
                                        SudukoConst.Cell_76,
                                        SudukoConst.Room1
                                    )
                                    dataManager.updateCellValue(
                                        temp_list_number1[0] + "",
                                        temp_list_number[0], SudukoConst.Room1
                                    )
                                    GenrateSudokuCell_71()
                                } else {
                                    StartAgain60()
                                }
                            }
                        }
                    }else{}
                } else if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Row8,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_76,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Row6,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_70,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_71()
                        } else {
                            temp_list_number.clear()
                            temp_list_number.add("73")
                            temp_list_number.add("76")
                            val random2 = genrateRandomNumber()
                            val str = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1 =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column3,
                                    list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row6,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_70,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_71()
                            } else {
                                temp_list_number.removeAt(random2)
                                val str1 = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[0]
                                )
                                if (dataManager.getRoomID_CellPosition_IN(
                                        SudukoConst.Room1,
                                        SudukoConst.str_Row6,
                                        str1
                                    )
                                ) {
                                    dataManager.updateCellValue(
                                        str1 + "",
                                        SudukoConst.Cell_70,
                                        SudukoConst.Room1
                                    )
                                    dataManager.updateCellValue(
                                        temp_list_number1[0] + "",
                                        temp_list_number[0], SudukoConst.Room1
                                    )
                                    GenrateSudokuCell_71()
                                } else {
                                    StartAgain60()
                                }
                            }
                        }
                    }else{}
                } else {
                    StartAgain60()
                }
            }else{}
        } else if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Row8,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_76,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            if (temp_list_number.size > 0) {
                val random1 = genrateRandomNumber()
                //                Log.e("GenrateSudokuCell_70_4", "" + temp_list_number.get(random1));
                if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Row6,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_70,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Row7,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_73,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_71()
                        } else {
                            temp_list_number.clear()
                            temp_list_number.add("70")
                            temp_list_number.add("76")
                            val random2 = genrateRandomNumber()
                            val str = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1 =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column3,
                                    list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row7,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_73,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_71()
                            } else {
                                temp_list_number.removeAt(random2)
                                val str1 = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[0]
                                )
                                if (dataManager.getRoomID_CellPosition_IN(
                                        SudukoConst.Room1,
                                        SudukoConst.str_Row7,
                                        str1
                                    )
                                ) {
                                    dataManager.updateCellValue(
                                        str1 + "",
                                        SudukoConst.Cell_73,
                                        SudukoConst.Room1
                                    )
                                    dataManager.updateCellValue(
                                        temp_list_number1[0] + "",
                                        temp_list_number[0], SudukoConst.Room1
                                    )
                                    GenrateSudokuCell_71()
                                } else {
                                    StartAgain60()
                                }
                            }
                        }
                    }else{}
                } else if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Row7,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_73,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Row6,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_70,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_71()
                        } else {
                            temp_list_number.clear()
                            temp_list_number.add("76")
                            temp_list_number.add("73")
                            val random2 = genrateRandomNumber()
                            val str = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1 =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column3,
                                    list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row6,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_70,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_71()
                            } else {
                                temp_list_number.removeAt(random2)
                                val str1 = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[0]
                                )
                                if (dataManager.getRoomID_CellPosition_IN(
                                        SudukoConst.Room1,
                                        SudukoConst.str_Row6,
                                        str1
                                    )
                                ) {
                                    dataManager.updateCellValue(
                                        str1 + "",
                                        SudukoConst.Cell_70,
                                        SudukoConst.Room1
                                    )
                                    dataManager.updateCellValue(
                                        temp_list_number1[0] + "",
                                        temp_list_number[0], SudukoConst.Room1
                                    )
                                    GenrateSudokuCell_71()
                                } else {
                                    StartAgain60()
                                }
                            }
                        }
                    }else{}
                } else {
                    StartAgain60()
                }
            }else{}
        } else {
            StartAgain60()
        }
    }

    private fun GenrateSudokuCell_71() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudukoConst.Room1,
            SudukoConst.str_Column4,
            list_suduko1
        )

//        for (int i = 0; i < temp_list_number.size(); i++) {
//            Log.e("GenrateSudoku_71_total", "" + temp_list_number.get(i));
//        }
        if (temp_list_number.size > 0) {
            GenrateSudokuCell_71_Temp()
        }
    }

    private fun GenrateSudokuCell_71_Temp() = runBlocking {
        val random = genrateRandomNumber()
        //        Log.e("GenrateSudokuCell_71_1", "" + temp_list_number.get(random));
        if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Row6 + "," + SudukoConst.str_Grid7,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_71,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            val random1 = genrateRandomNumber()
            //            Log.e("GenrateSudokuCell_71_2", "" + temp_list_number.get(random1));
            if (dataManager.getRoomID_CellPosition_IN(
                    SudukoConst.Room1, SudukoConst.str_Row7 + "," + SudukoConst.str_Grid7,
                    temp_list_number[random1]
                )
            ) {
                dataManager.updateCellValue(
                    temp_list_number[random1] + "",
                    SudukoConst.Cell_74,
                    SudukoConst.Room1
                )
                temp_list_number.removeAt(random1)
                if (temp_list_number.size > 0) {
                    if (dataManager.getRoomID_CellPosition_IN(
                            SudukoConst.Room1, SudukoConst.str_Row8 + "," + SudukoConst.str_Grid7,
                            temp_list_number[0]
                        )
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number[0] + "",
                            SudukoConst.Cell_77,
                            SudukoConst.Room1
                        )
                        GenrateSudokuCell_72()
                    } else {
                        temp_list_number.clear()
                        temp_list_number.add("71")
                        temp_list_number.add("74")
                        val random2 = genrateRandomNumber()
                        val str = dataManager.getRoomID_CellPositionString(
                            SudukoConst.Room1,
                            temp_list_number[random2]
                        )
                        val list_suduko11 = ArrayList<String>()
                        list_suduko11.addAll(list_suduko)
                        val temp_list_number1 =
                            dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                SudukoConst.Room1,
                                SudukoConst.str_Column4, list_suduko11
                            )

//                        Log.e("GenrateSudokuCell_71_2", "::" + str);
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1,
                                SudukoConst.str_Row8 + "," + SudukoConst.str_Grid7,
                                str
                            )
                        ) {
                            dataManager.updateCellValue(
                                str + "",
                                SudukoConst.Cell_77,
                                SudukoConst.Room1
                            )
                            dataManager.updateCellValue(
                                temp_list_number1[0] + "",
                                temp_list_number[random2], SudukoConst.Room1
                            )
                            GenrateSudokuCell_72()
                        } else {
                            temp_list_number.removeAt(random2)
                            val str1 = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[0]
                            )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row8 + "," + SudukoConst.str_Grid7,
                                    str1
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str1 + "",
                                    SudukoConst.Cell_77,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[0], SudukoConst.Room1
                                )
                                GenrateSudokuCell_72()
                            } else {
                                StartAgain60()
                            }
                        }
                    }
                }else{}
            } else if (dataManager.getRoomID_CellPosition_IN(
                    SudukoConst.Room1, SudukoConst.str_Row8 + "," + SudukoConst.str_Grid7,
                    temp_list_number[random1]
                )
            ) {
                dataManager.updateCellValue(
                    temp_list_number[random1] + "",
                    SudukoConst.Cell_77,
                    SudukoConst.Room1
                )
                temp_list_number.removeAt(random1)
                if (temp_list_number.size > 0) {
                    if (dataManager.getRoomID_CellPosition_IN(
                            SudukoConst.Room1, SudukoConst.str_Row7 + "," + SudukoConst.str_Grid7,
                            temp_list_number[0]
                        )
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number[0] + "",
                            SudukoConst.Cell_74,
                            SudukoConst.Room1
                        )
                        GenrateSudokuCell_72()
                    } else {
                        temp_list_number.clear()
                        temp_list_number.add("71")
                        temp_list_number.add("77")
                        val random2 = genrateRandomNumber()
                        val str = dataManager.getRoomID_CellPositionString(
                            SudukoConst.Room1,
                            temp_list_number[random2]
                        )
                        val list_suduko11 = ArrayList<String>()
                        list_suduko11.addAll(list_suduko)
                        val temp_list_number1 =
                            dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                SudukoConst.Room1,
                                SudukoConst.str_Column4,
                                list_suduko11
                            )
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1,
                                SudukoConst.str_Row7 + "," + SudukoConst.str_Grid7,
                                str
                            )
                        ) {
                            dataManager.updateCellValue(
                                str + "",
                                SudukoConst.Cell_74,
                                SudukoConst.Room1
                            )
                            dataManager.updateCellValue(
                                temp_list_number1[0] + "",
                                temp_list_number[random2], SudukoConst.Room1
                            )
                            GenrateSudokuCell_72()
                        } else {
                            temp_list_number.removeAt(random2)
                            val str1 = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[0]
                            )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row7 + "," + SudukoConst.str_Grid7,
                                    str1
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str1 + "",
                                    SudukoConst.Cell_74,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[0], SudukoConst.Room1
                                )
                                GenrateSudokuCell_72()
                            } else {
                                StartAgain60()
                            }
                        }
                    }
                }else{}
            } else {
                StartAgain60()
            }
        } else if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Row7 + "," + SudukoConst.str_Grid7,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_74,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            if (temp_list_number.size > 0) {
                val random1 = genrateRandomNumber()
                //                Log.e("GenrateSudokuCell_71_3", "" + temp_list_number.get(random1));
                if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Row6 + "," + SudukoConst.str_Grid7,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_71,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    //
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1,
                                SudukoConst.str_Row8 + "," + SudukoConst.str_Grid7,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_77,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_72()
                        } else {
                            temp_list_number.clear()
                            temp_list_number.add("71")
                            temp_list_number.add("74")
                            val random2 = genrateRandomNumber()
                            val str = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1 =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column4,
                                    list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row8 + "," + SudukoConst.str_Grid7,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_77,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_72()
                            } else {
                                temp_list_number.removeAt(random2)
                                val str1 = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[0]
                                )
                                if (dataManager.getRoomID_CellPosition_IN(
                                        SudukoConst.Room1,
                                        SudukoConst.str_Row8 + "," + SudukoConst.str_Grid7,
                                        str1
                                    )
                                ) {
                                    dataManager.updateCellValue(
                                        str1 + "",
                                        SudukoConst.Cell_77,
                                        SudukoConst.Room1
                                    )
                                    dataManager.updateCellValue(
                                        temp_list_number1[0] + "",
                                        temp_list_number[0], SudukoConst.Room1
                                    )
                                    GenrateSudokuCell_72()
                                } else {
                                    StartAgain60()
                                }
                            }
                        }
                    }else{}
                } else if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Row8 + "," + SudukoConst.str_Grid7,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_77,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1,
                                SudukoConst.str_Row6 + "," + SudukoConst.str_Grid7,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_71,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_72()
                        } else {
                            temp_list_number.clear()
                            temp_list_number.add("74")
                            temp_list_number.add("77")
                            val random2 = genrateRandomNumber()
                            val str = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1 =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column4,
                                    list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row6 + "," + SudukoConst.str_Grid7,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_71,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_72()
                            } else {
                                temp_list_number.removeAt(random2)
                                val str1 = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[0]
                                )
                                if (dataManager.getRoomID_CellPosition_IN(
                                        SudukoConst.Room1,
                                        SudukoConst.str_Row6 + "," + SudukoConst.str_Grid7,
                                        str1
                                    )
                                ) {
                                    dataManager.updateCellValue(
                                        str1 + "",
                                        SudukoConst.Cell_71,
                                        SudukoConst.Room1
                                    )
                                    dataManager.updateCellValue(
                                        temp_list_number1[0] + "",
                                        temp_list_number[0], SudukoConst.Room1
                                    )
                                    GenrateSudokuCell_72()
                                } else {
                                    StartAgain60()
                                }
                            }
                        }
                    }else{}
                } else {
                    StartAgain60()
                }
            }else{}
        } else if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Row8 + "," + SudukoConst.str_Grid7,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_77,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            if (temp_list_number.size > 0) {
                val random1 = genrateRandomNumber()
                //                Log.e("GenrateSudokuCell_71_4", "" + temp_list_number.get(random1));
                if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Row6 + "," + SudukoConst.str_Grid7,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_71,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1,
                                SudukoConst.str_Row7 + "," + SudukoConst.str_Grid7,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_74,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_72()
                        } else {
                            temp_list_number.clear()
                            temp_list_number.add("71")
                            temp_list_number.add("77")
                            val random2 = genrateRandomNumber()
                            val str = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1 =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column4,
                                    list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row7 + "," + SudukoConst.str_Grid7,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_74,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_72()
                            } else {
                                temp_list_number.removeAt(random2)
                                val str1 = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[0]
                                )
                                if (dataManager.getRoomID_CellPosition_IN(
                                        SudukoConst.Room1,
                                        SudukoConst.str_Row7 + "," + SudukoConst.str_Grid7,
                                        str1
                                    )
                                ) {
                                    dataManager.updateCellValue(
                                        str1 + "",
                                        SudukoConst.Cell_74,
                                        SudukoConst.Room1
                                    )
                                    dataManager.updateCellValue(
                                        temp_list_number1[0] + "",
                                        temp_list_number[0], SudukoConst.Room1
                                    )
                                    GenrateSudokuCell_72()
                                } else {
                                    StartAgain60()
                                }
                            }
                        }
                    }else{}
                } else if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Row7 + "," + SudukoConst.str_Grid7,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_74,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1,
                                SudukoConst.str_Row6 + "," + SudukoConst.str_Grid7,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_71,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_72()
                        } else {
                            temp_list_number.clear()
                            temp_list_number.add("77")
                            temp_list_number.add("74")
                            val random2 = genrateRandomNumber()
                            val str = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1 =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column4,
                                    list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row6 + "," + SudukoConst.str_Grid7,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_71,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_72()
                            } else {
                                temp_list_number.removeAt(random2)
                                val str1 = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[0]
                                )
                                if (dataManager.getRoomID_CellPosition_IN(
                                        SudukoConst.Room1,
                                        SudukoConst.str_Row6 + "," + SudukoConst.str_Grid7,
                                        str1
                                    )
                                ) {
                                    dataManager.updateCellValue(
                                        str1 + "",
                                        SudukoConst.Cell_71,
                                        SudukoConst.Room1
                                    )
                                    dataManager.updateCellValue(
                                        temp_list_number1[0] + "",
                                        temp_list_number[0], SudukoConst.Room1
                                    )
                                    GenrateSudokuCell_72()
                                } else {
                                    StartAgain60()
                                }
                            }
                        }
                    }else{}
                } else {
                    StartAgain60()
                }
            }else{}
        } else {
            StartAgain60()
        }
    }

    private fun GenrateSudokuCell_72() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudukoConst.Room1,
            SudukoConst.str_Column5,
            list_suduko1
        )

//        for (int i = 0; i < temp_list_number.size(); i++) {
//            Log.e("GenrateSudoku_70_total", "" + temp_list_number.get(i));
//        }
        if (temp_list_number.size > 0) {
            GenrateSudokuCell_72_Temp()
        }
    }

    private fun GenrateSudokuCell_72_Temp() = runBlocking {
        val random = genrateRandomNumber()
        //        Log.e("GenrateSudokuCell_72_1", "" + temp_list_number.get(random));
        if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Row6 + "," + SudukoConst.str_Grid7,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_72,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            val random1 = genrateRandomNumber()
            //            Log.e("GenrateSudokuCell_72_2", "" + temp_list_number.get(random1));
            if (dataManager.getRoomID_CellPosition_IN(
                    SudukoConst.Room1, SudukoConst.str_Row7 + "," + SudukoConst.str_Grid7,
                    temp_list_number[random1]
                )
            ) {
                dataManager.updateCellValue(
                    temp_list_number[random1] + "",
                    SudukoConst.Cell_75,
                    SudukoConst.Room1
                )
                temp_list_number.removeAt(random1)
                if (temp_list_number.size > 0) {
                    if (dataManager.getRoomID_CellPosition_IN(
                            SudukoConst.Room1, SudukoConst.str_Row8 + "," + SudukoConst.str_Grid7,
                            temp_list_number[0]
                        )
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number[0] + "",
                            SudukoConst.Cell_78,
                            SudukoConst.Room1
                        )
                        GenrateSudokuCell_80()
                    } else {
                        temp_list_number.clear()
                        temp_list_number.add("72")
                        temp_list_number.add("75")
                        val random2 = genrateRandomNumber()
                        val str = dataManager.getRoomID_CellPositionString(
                            SudukoConst.Room1,
                            temp_list_number[random2]
                        )
                        val list_suduko11 = ArrayList<String>()
                        list_suduko11.addAll(list_suduko)
                        val temp_list_number1 =
                            dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                SudukoConst.Room1,
                                SudukoConst.str_Column5,
                                list_suduko11
                            )
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1,
                                SudukoConst.str_Row8 + "," + SudukoConst.str_Grid7,
                                str
                            )
                        ) {
                            dataManager.updateCellValue(
                                str + "",
                                SudukoConst.Cell_78,
                                SudukoConst.Room1
                            )
                            dataManager.updateCellValue(
                                temp_list_number1[0] + "",
                                temp_list_number[random2], SudukoConst.Room1
                            )
                            GenrateSudokuCell_80()
                        } else {
                            temp_list_number.removeAt(random2)
                            val str1 = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[0]
                            )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row8 + "," + SudukoConst.str_Grid7,
                                    str1
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str1 + "",
                                    SudukoConst.Cell_78,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[0], SudukoConst.Room1
                                )
                                GenrateSudokuCell_80()
                            } else {
                                StartAgain60()
                            }
                        }
                    }
                }else{}
            } else if (dataManager.getRoomID_CellPosition_IN(
                    SudukoConst.Room1, SudukoConst.str_Row8 + "," + SudukoConst.str_Grid7,
                    temp_list_number[random1]
                )
            ) {
                dataManager.updateCellValue(
                    temp_list_number[random1] + "",
                    SudukoConst.Cell_78,
                    SudukoConst.Room1
                )
                temp_list_number.removeAt(random1)
                if (temp_list_number.size > 0) {
                    if (dataManager.getRoomID_CellPosition_IN(
                            SudukoConst.Room1, SudukoConst.str_Row7 + "," + SudukoConst.str_Grid7,
                            temp_list_number[0]
                        )
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number[0] + "",
                            SudukoConst.Cell_75,
                            SudukoConst.Room1
                        )
                        GenrateSudokuCell_80()
                    } else {
                        temp_list_number.clear()
                        temp_list_number.add("72")
                        temp_list_number.add("78")
                        val random2 = genrateRandomNumber()
                        val str = dataManager.getRoomID_CellPositionString(
                            SudukoConst.Room1,
                            temp_list_number[random2]
                        )
                        val list_suduko11 = ArrayList<String>()
                        list_suduko11.addAll(list_suduko)
                        val temp_list_number1 =
                            dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                SudukoConst.Room1,
                                SudukoConst.str_Column5,
                                list_suduko11
                            )
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1,
                                SudukoConst.str_Row7 + "," + SudukoConst.str_Grid7,
                                str
                            )
                        ) {
                            dataManager.updateCellValue(
                                str + "",
                                SudukoConst.Cell_75,
                                SudukoConst.Room1
                            )
                            dataManager.updateCellValue(
                                temp_list_number1[0] + "",
                                temp_list_number[random2], SudukoConst.Room1
                            )
                            GenrateSudokuCell_80()
                        } else {
                            temp_list_number.removeAt(random2)
                            val str1 = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[0]
                            )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row7 + "," + SudukoConst.str_Grid7,
                                    str1
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str1 + "",
                                    SudukoConst.Cell_75,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[0], SudukoConst.Room1
                                )
                                GenrateSudokuCell_80()
                            } else {
                                StartAgain60()
                            }
                        }
                    }
                }else{}
            } else {
                StartAgain60()
            }
        } else if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Row7 + "," + SudukoConst.str_Grid7,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_75,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            if (temp_list_number.size > 0) {
                val random1 = genrateRandomNumber()
                //                Log.e("GenrateSudokuCell_72_3", "" + temp_list_number.get(random1));
                if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Row6 + "," + SudukoConst.str_Grid7,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_72,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    //
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1,
                                SudukoConst.str_Row8 + "," + SudukoConst.str_Grid7,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_78,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_80()
                        } else {
                            temp_list_number.clear()
                            temp_list_number.add("72")
                            temp_list_number.add("75")
                            val random2 = genrateRandomNumber()
                            val str = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1 =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column5,
                                    list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row8 + "," + SudukoConst.str_Grid7,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_78,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_80()
                            } else {
                                temp_list_number.removeAt(random2)
                                val str1 = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[0]
                                )
                                if (dataManager.getRoomID_CellPosition_IN(
                                        SudukoConst.Room1,
                                        SudukoConst.str_Row8 + "," + SudukoConst.str_Grid7,
                                        str1
                                    )
                                ) {
                                    dataManager.updateCellValue(
                                        str1 + "",
                                        SudukoConst.Cell_78,
                                        SudukoConst.Room1
                                    )
                                    dataManager.updateCellValue(
                                        temp_list_number1[0] + "",
                                        temp_list_number[0], SudukoConst.Room1
                                    )
                                    GenrateSudokuCell_80()
                                } else {
                                    StartAgain60()
                                }
                            }
                        }
                    }else{}
                } else if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Row8 + "," + SudukoConst.str_Grid7,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_78,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1,
                                SudukoConst.str_Row6 + "," + SudukoConst.str_Grid7,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_72,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_80()
                        } else {
                            temp_list_number.clear()
                            temp_list_number.add("75")
                            temp_list_number.add("78")
                            val random2 = genrateRandomNumber()
                            val str = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1 =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column5,
                                    list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row6 + "," + SudukoConst.str_Grid7,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_72,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_80()
                            } else {
                                temp_list_number.removeAt(random2)
                                val str1 = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[0]
                                )
                                if (dataManager.getRoomID_CellPosition_IN(
                                        SudukoConst.Room1,
                                        SudukoConst.str_Row6 + "," + SudukoConst.str_Grid7,
                                        str1
                                    )
                                ) {
                                    dataManager.updateCellValue(
                                        str1 + "",
                                        SudukoConst.Cell_72,
                                        SudukoConst.Room1
                                    )
                                    dataManager.updateCellValue(
                                        temp_list_number1[0] + "",
                                        temp_list_number[0], SudukoConst.Room1
                                    )
                                    GenrateSudokuCell_80()
                                } else {
                                    StartAgain60()
                                }
                            }
                        }
                    }else{}
                } else {
                    StartAgain60()
                }
            }else{}
        } else if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Row8 + "," + SudukoConst.str_Grid7,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_78,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            if (temp_list_number.size > 0) {
                val random1 = genrateRandomNumber()
                //                Log.e("GenrateSudokuCell_72_4", "" + temp_list_number.get(random1));
                if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Row6 + "," + SudukoConst.str_Grid7,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_72,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1,
                                SudukoConst.str_Row7 + "," + SudukoConst.str_Grid7,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_75,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_80()
                        } else {
                            temp_list_number.clear()
                            temp_list_number.add("72")
                            temp_list_number.add("78")
                            val random2 = genrateRandomNumber()
                            val str = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1 =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column5,
                                    list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row7 + "," + SudukoConst.str_Grid7,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_75,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_80()
                            } else {
                                temp_list_number.removeAt(random2)
                                val str1 = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[0]
                                )
                                if (dataManager.getRoomID_CellPosition_IN(
                                        SudukoConst.Room1,
                                        SudukoConst.str_Row7 + "," + SudukoConst.str_Grid7,
                                        str1
                                    )
                                ) {
                                    dataManager.updateCellValue(
                                        str1 + "",
                                        SudukoConst.Cell_75,
                                        SudukoConst.Room1
                                    )
                                    dataManager.updateCellValue(
                                        temp_list_number1[0] + "",
                                        temp_list_number[0], SudukoConst.Room1
                                    )
                                    GenrateSudokuCell_80()
                                } else {
                                    StartAgain60()
                                }
                            }
                        }
                    }else{}
                } else if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Row7 + "," + SudukoConst.str_Grid7,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_75,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1,
                                SudukoConst.str_Row6 + "," + SudukoConst.str_Grid7,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_72,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_80()
                        } else {
                            temp_list_number.clear()
                            temp_list_number.add("78")
                            temp_list_number.add("75")
                            val random2 = genrateRandomNumber()
                            val str = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1 =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column5,
                                    list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row6 + "," + SudukoConst.str_Grid7,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_72,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_80()
                            } else {
                                temp_list_number.removeAt(random2)
                                val str1 = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[0]
                                )
                                if (dataManager.getRoomID_CellPosition_IN(
                                        SudukoConst.Room1,
                                        SudukoConst.str_Row6 + "," + SudukoConst.str_Grid7,
                                        str1
                                    )
                                ) {
                                    dataManager.updateCellValue(
                                        str1 + "",
                                        SudukoConst.Cell_72,
                                        SudukoConst.Room1
                                    )
                                    dataManager.updateCellValue(
                                        temp_list_number1[0] + "",
                                        temp_list_number[0], SudukoConst.Room1
                                    )
                                    GenrateSudokuCell_80()
                                } else {
                                    StartAgain60()
                                }
                            }
                        }
                    }else{}
                } else {
                    StartAgain60()
                }
            }else{}
        } else {
            StartAgain60()
        }
    }

    private fun GenrateSudokuCell_80() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudukoConst.Room1,
            SudukoConst.str_Column6,
            list_suduko1
        )
        if (temp_list_number.size > 0) {
            GenrateSudokuCell_80_Temp()
        }
    }

    private fun GenrateSudokuCell_80_Temp() = runBlocking {
        val random = genrateRandomNumber()
        //        Log.e("GenrateSudokuCell_80_1", "" + temp_list_number.get(random));
        if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Row6,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_80,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            val random1 = genrateRandomNumber()
            //            Log.e("GenrateSudokuCell_80_2", "" + temp_list_number.get(random1));
            if (dataManager.getRoomID_CellPosition_IN(
                    SudukoConst.Room1, SudukoConst.str_Row7,
                    temp_list_number[random1]
                )
            ) {
                dataManager.updateCellValue(
                    temp_list_number[random1] + "",
                    SudukoConst.Cell_83,
                    SudukoConst.Room1
                )
                temp_list_number.removeAt(random1)
                if (temp_list_number.size > 0) {
                    if (dataManager.getRoomID_CellPosition_IN(
                            SudukoConst.Room1, SudukoConst.str_Row8,
                            temp_list_number[0]
                        )
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number[0] + "",
                            SudukoConst.Cell_86,
                            SudukoConst.Room1
                        )
                        GenrateSudokuCell_87()
                    } else {
                        temp_list_number.clear()
                        temp_list_number.add("80")
                        temp_list_number.add("83")
                        val random2 = genrateRandomNumber()
                        val str = dataManager.getRoomID_CellPositionString(
                            SudukoConst.Room1,
                            temp_list_number[random2]
                        )
                        val list_suduko11 = ArrayList<String>()
                        list_suduko11.addAll(list_suduko)
                        val temp_list_number1 =
                            dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                SudukoConst.Room1,
                                SudukoConst.str_Column6,
                                list_suduko11
                            )
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1,
                                SudukoConst.str_Row8,
                                str
                            )
                        ) {
                            dataManager.updateCellValue(
                                str + "",
                                SudukoConst.Cell_86,
                                SudukoConst.Room1
                            )
                            dataManager.updateCellValue(
                                temp_list_number1[0] + "",
                                temp_list_number[random2], SudukoConst.Room1
                            )
                            GenrateSudokuCell_87()
                        } else {
                            temp_list_number.removeAt(random2)
                            val str1 = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[0]
                            )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row8,
                                    str1
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str1 + "",
                                    SudukoConst.Cell_86,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[0], SudukoConst.Room1
                                )
                                GenrateSudokuCell_87()
                            } else {
                                StartAgain60()
                            }
                        }
                    }
                }else{}
            } else if (dataManager.getRoomID_CellPosition_IN(
                    SudukoConst.Room1, SudukoConst.str_Row8,
                    temp_list_number[random1]
                )
            ) {
                dataManager.updateCellValue(
                    temp_list_number[random1] + "",
                    SudukoConst.Cell_86,
                    SudukoConst.Room1
                )
                temp_list_number.removeAt(random1)
                if (temp_list_number.size > 0) {
                    if (dataManager.getRoomID_CellPosition_IN(
                            SudukoConst.Room1, SudukoConst.str_Row7,
                            temp_list_number[0]
                        )
                    ) {
                        dataManager.updateCellValue(
                            temp_list_number[0] + "",
                            SudukoConst.Cell_83,
                            SudukoConst.Room1
                        )
                        GenrateSudokuCell_87()
                    } else {
                        temp_list_number.clear()
                        temp_list_number.add("80")
                        temp_list_number.add("86")
                        val random2 = genrateRandomNumber()
                        val str = dataManager.getRoomID_CellPositionString(
                            SudukoConst.Room1,
                            temp_list_number[random2]
                        )
                        val list_suduko11 = ArrayList<String>()
                        list_suduko11.addAll(list_suduko)
                        val temp_list_number1 =
                            dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                SudukoConst.Room1,
                                SudukoConst.str_Column6,
                                list_suduko11
                            )
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1,
                                SudukoConst.str_Row7,
                                str
                            )
                        ) {
                            dataManager.updateCellValue(
                                str + "",
                                SudukoConst.Cell_83,
                                SudukoConst.Room1
                            )
                            dataManager.updateCellValue(
                                temp_list_number1[0] + "",
                                temp_list_number[random2], SudukoConst.Room1
                            )
                            GenrateSudokuCell_87()
                        } else {
                            temp_list_number.removeAt(random2)
                            val str1 = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[0]
                            )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row7,
                                    str1
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str1 + "",
                                    SudukoConst.Cell_83,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[0], SudukoConst.Room1
                                )
                                GenrateSudokuCell_87()
                            } else {
                                StartAgain60()
                            }
                        }
                    }
                }else{}
            } else {
                StartAgain60()
            }
        } else if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Row7,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_83,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            if (temp_list_number.size > 0) {
                val random1 = genrateRandomNumber()
                //                Log.e("GenrateSudokuCell_80_3", "" + temp_list_number.get(random1));
                if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Row6,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_80,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    //
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Row8,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_86,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_87()
                        } else {
                            temp_list_number.clear()
                            temp_list_number.add("80")
                            temp_list_number.add("83")
                            val random2 = genrateRandomNumber()
                            val str = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1 =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column6,
                                    list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row8,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_86,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_87()
                            } else {
                                temp_list_number.removeAt(random2)
                                val str1 = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[0]
                                )
                                if (dataManager.getRoomID_CellPosition_IN(
                                        SudukoConst.Room1,
                                        SudukoConst.str_Row8,
                                        str1
                                    )
                                ) {
                                    dataManager.updateCellValue(
                                        str1 + "",
                                        SudukoConst.Cell_86,
                                        SudukoConst.Room1
                                    )
                                    dataManager.updateCellValue(
                                        temp_list_number1[0] + "",
                                        temp_list_number[0], SudukoConst.Room1
                                    )
                                    GenrateSudokuCell_87()
                                } else {
                                    StartAgain60()
                                }
                            }
                        }
                    }else{}
                } else if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Row8,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_86,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Row6,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_80,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_87()
                        } else {
                            temp_list_number.clear()
                            temp_list_number.add("83")
                            temp_list_number.add("86")
                            val random2 = genrateRandomNumber()
                            val str = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1 =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column6,
                                    list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row6,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_80,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_87()
                            } else {
                                temp_list_number.removeAt(random2)
                                val str1 = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[0]
                                )
                                if (dataManager.getRoomID_CellPosition_IN(
                                        SudukoConst.Room1,
                                        SudukoConst.str_Row6,
                                        str1
                                    )
                                ) {
                                    dataManager.updateCellValue(
                                        str1 + "",
                                        SudukoConst.Cell_80,
                                        SudukoConst.Room1
                                    )
                                    dataManager.updateCellValue(
                                        temp_list_number1[0] + "",
                                        temp_list_number[0], SudukoConst.Room1
                                    )
                                    GenrateSudokuCell_87()
                                } else {
                                    StartAgain60()
                                }
                            }
                        }
                    }else{}
                } else {
                    StartAgain60()
                }
            }else{}
        } else if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Row8,
                temp_list_number[random]
            )
        ) {
//            Log.e("GenrateSudokuCel_80_4_1", "" + temp_list_number.get(random));
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_86,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            if (temp_list_number.size > 0) {
                val random1 = genrateRandomNumber()
                //                Log.e("GenrateSudokuCell_80_4", "" + temp_list_number.get(random1));
                if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Row6,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_80,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Row7,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_83,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_87()
                        } else {
                            temp_list_number.clear()
                            temp_list_number.add("80")
                            temp_list_number.add("86")
                            val random2 = genrateRandomNumber()
                            val str = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1 =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column6,
                                    list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row7,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_83,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_87()
                            } else {
                                temp_list_number.removeAt(random2)
                                val str1 = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[0]
                                )
                                if (dataManager.getRoomID_CellPosition_IN(
                                        SudukoConst.Room1,
                                        SudukoConst.str_Row7,
                                        str1
                                    )
                                ) {
                                    dataManager.updateCellValue(
                                        str1 + "",
                                        SudukoConst.Cell_83,
                                        SudukoConst.Room1
                                    )
                                    dataManager.updateCellValue(
                                        temp_list_number1[0] + "",
                                        temp_list_number[0], SudukoConst.Room1
                                    )
                                    GenrateSudokuCell_87()
                                } else {
                                    StartAgain60()
                                }
                            }
                        }
                    }else{}
                } else if (dataManager.getRoomID_CellPosition_IN(
                        SudukoConst.Room1, SudukoConst.str_Row7,
                        temp_list_number[random1]
                    )
                ) {
                    dataManager.updateCellValue(
                        temp_list_number[random1] + "",
                        SudukoConst.Cell_83,
                        SudukoConst.Room1
                    )
                    temp_list_number.removeAt(random1)
                    if (temp_list_number.size > 0) {
                        if (dataManager.getRoomID_CellPosition_IN(
                                SudukoConst.Room1, SudukoConst.str_Row6,
                                temp_list_number[0]
                            )
                        ) {
                            dataManager.updateCellValue(
                                temp_list_number[0] + "",
                                SudukoConst.Cell_80,
                                SudukoConst.Room1
                            )
                            GenrateSudokuCell_87()
                        } else {
                            temp_list_number.clear()
                            temp_list_number.add("86")
                            temp_list_number.add("83")
                            val random2 = genrateRandomNumber()
                            val str = dataManager.getRoomID_CellPositionString(
                                SudukoConst.Room1,
                                temp_list_number[random2]
                            )
                            val list_suduko11 = ArrayList<String>()
                            list_suduko11.addAll(list_suduko)
                            val temp_list_number1 =
                                dataManager.getRoomID_CellPosition_IN_List_For_Remove(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Column6,
                                    list_suduko11
                                )
                            if (dataManager.getRoomID_CellPosition_IN(
                                    SudukoConst.Room1,
                                    SudukoConst.str_Row6,
                                    str
                                )
                            ) {
                                dataManager.updateCellValue(
                                    str + "",
                                    SudukoConst.Cell_80,
                                    SudukoConst.Room1
                                )
                                dataManager.updateCellValue(
                                    temp_list_number1[0] + "",
                                    temp_list_number[random2], SudukoConst.Room1
                                )
                                GenrateSudokuCell_87()
                            } else {
                                temp_list_number.removeAt(random2)
                                val str1 = dataManager.getRoomID_CellPositionString(
                                    SudukoConst.Room1,
                                    temp_list_number[0]
                                )
                                if (dataManager.getRoomID_CellPosition_IN(
                                        SudukoConst.Room1,
                                        SudukoConst.str_Row6,
                                        str1
                                    )
                                ) {
                                    dataManager.updateCellValue(
                                        str1 + "",
                                        SudukoConst.Cell_80,
                                        SudukoConst.Room1
                                    )
                                    dataManager.updateCellValue(
                                        temp_list_number1[0] + "",
                                        temp_list_number[0], SudukoConst.Room1
                                    )
                                    GenrateSudokuCell_87()
                                } else {
                                    StartAgain60()
                                }
                            }
                        }
                    }else{}
                } else {
                    StartAgain60()
                }
            }else{}
        } else {
            StartAgain60()
        }
    }

    private fun GenrateSudokuCell_87() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudukoConst.Room1,
            SudukoConst.str_Row8,
            list_suduko1
        )

//        for (int i = 0; i < temp_list_number.size(); i++) {
//            Log.e("GenrateSudoku_87_total", "" + temp_list_number.get(i));
//        }
        if (temp_list_number.size > 0) {
            GenrateSudokuCell_87_Temp()
        }
    }

    private fun GenrateSudokuCell_87_Temp() = runBlocking {
        val random = genrateRandomNumber()
        //        Log.e("GenrateSudokuCell_87_1", "" + temp_list_number.get(random));
        if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Column7 + "," + SudukoConst.str_Grid8,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_87,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            if (dataManager.getRoomID_CellPosition_IN(
                    SudukoConst.Room1, SudukoConst.str_Column8 + "," + SudukoConst.str_Grid8,
                    temp_list_number[0]
                )
            ) {
                dataManager.updateCellValue(
                    temp_list_number[0] + "",
                    SudukoConst.Cell_88,
                    SudukoConst.Room1
                )
                GenrateSudokuCell_81()
            } else {
                StartAgain60()
            }
        } else if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Column8 + "," + SudukoConst.str_Grid8,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_88,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            if (dataManager.getRoomID_CellPosition_IN(
                    SudukoConst.Room1, SudukoConst.str_Column7 + "," + SudukoConst.str_Grid8,
                    temp_list_number[0]
                )
            ) {
                dataManager.updateCellValue(
                    temp_list_number[0] + "",
                    SudukoConst.Cell_87,
                    SudukoConst.Room1
                )
                GenrateSudokuCell_81()
            } else {
                StartAgain60()
            }
        } else {
            StartAgain60()
        }
    }

    private fun GenrateSudokuCell_81() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudukoConst.Room1,
            SudukoConst.str_Row6,
            list_suduko1
        )

//        for (int i = 0; i < temp_list_number.size(); i++) {
//            Log.e("GenrateSudoku_81_total", "" + temp_list_number.get(i));
//        }
        if (temp_list_number.size > 0) {
            GenrateSudokuCell_81_Temp()
        }
    }

    private fun GenrateSudokuCell_81_Temp() = runBlocking {
        val random = genrateRandomNumber()
        //        Log.e("GenrateSudokuCell_81_1", "" + temp_list_number.get(random));
        if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Column7 + "," + SudukoConst.str_Grid8,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_81,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            if (dataManager.getRoomID_CellPosition_IN(
                    SudukoConst.Room1, SudukoConst.str_Column8 + "," + SudukoConst.str_Grid8,
                    temp_list_number[0]
                )
            ) {
                dataManager.updateCellValue(
                    temp_list_number[0] + "",
                    SudukoConst.Cell_82,
                    SudukoConst.Room1
                )
                GenrateSudokuCell_84()
            } else {
                StartAgain60()
            }
        } else if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Column8 + "," + SudukoConst.str_Grid8,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_82,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            if (dataManager.getRoomID_CellPosition_IN(
                    SudukoConst.Room1, SudukoConst.str_Column7 + "," + SudukoConst.str_Grid8,
                    temp_list_number[0]
                )
            ) {
                dataManager.updateCellValue(
                    temp_list_number[0] + "",
                    SudukoConst.Cell_81,
                    SudukoConst.Room1
                )
                GenrateSudokuCell_84()
            } else {
                StartAgain60()
            }
        } else {
            StartAgain60()
        }
    }

    private fun GenrateSudokuCell_84() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudukoConst.Room1,
            SudukoConst.str_Row7,
            list_suduko1
        )

//        for (int i = 0; i < temp_list_number.size(); i++) {
//            Log.e("GenrateSudoku_87_total", "" + temp_list_number.get(i));
//        }
        if (temp_list_number.size > 0) {
            GenrateSudokuCell_84_Temp()
        }
    }


    private fun StartAgain40() = runBlocking {
        createSudo()

//        dataManager.updateCellValue_IN(
//            "", SudukoConst.str_Grid4 + "," + SudukoConst.str_Grid5
//                    + "," + SudukoConst.str_Grid7 + "," + SudukoConst.str_Grid8, SudukoConst.Room1
//        )
//        genrateSudokuCell_40()


    }

    private fun StartAgain60() = runBlocking {
//        dataManager.updateCellValue_IN(
//            "", SudukoConst.str_Grid6
//                    + "," + SudukoConst.str_Grid7 + "," + SudukoConst.str_Grid8, SudukoConst.Room1
//        )
//        GenrateSudokuCell_60_new()
        createSudo()
    }

    private fun GenrateSudokuCell_60_new() = runBlocking {
        count++
        //        Log.e("GenrateSudoku_60_new", "GenrateSudokuCell_60_new");
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudukoConst.Room1,
            SudukoConst.str_Column0,
            list_suduko1
        )
        if (temp_list_number.size > 0){
            val random = genrateRandomNumber()
            if(random < temp_list_number.size){
                dataManager.updateCellValue(
                    temp_list_number[random] + "",
                    SudukoConst.Cell_60,
                    SudukoConst.Room1
                )
                temp_list_number.removeAt(random)
                GenrateSudokuCell_63_new()
            }else{
                StartAgain40()
            }
        }else{
            StartAgain40()
        }

    }

    private fun GenrateSudokuCell_63_new() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_63,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        GenrateSudokuCell_66_new()
    }

    private fun GenrateSudokuCell_66_new() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_66,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        GenrateSudokuCell_61_new()
    }

    private fun GenrateSudokuCell_61_new() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudukoConst.Room1,
            SudukoConst.str_Column1,
            list_suduko1
        )
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_61,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        GenrateSudokuCell_64_new()
    }

    private fun GenrateSudokuCell_64_new() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_64,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        GenrateSudokuCell_67_new()
    }

    private fun GenrateSudokuCell_67_new() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_67,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        GenrateSudokuCell_62_new()
    }

    private fun GenrateSudokuCell_62_new() = runBlocking {
        temp_list_number.clear()
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        temp_list_number = dataManager.getRoomID_CellPosition_IN_List_For_Remove(
            SudukoConst.Room1,
            SudukoConst.str_Column2,
            list_suduko1
        )
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_62,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        GenrateSudokuCell_65_new()
    }

    private fun GenrateSudokuCell_65_new() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_65,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)
        GenrateSudokuCell_68_new()
    }

    private fun GenrateSudokuCell_68_new() = runBlocking {
        val random = genrateRandomNumber()
        dataManager.updateCellValue(
            temp_list_number[random] + "",
            SudukoConst.Cell_68,
            SudukoConst.Room1
        )
        temp_list_number.removeAt(random)

        createSudo()
//        if (count == 2) {
//            count = 0
//            StartAgain40()
//        } else {
//            GenrateSudokuCell_70()
//        }
    }

    private fun GenrateSudokuCell_84_Temp() = runBlocking {
        val random = genrateRandomNumber()
        //        Log.e("GenrateSudokuCell_84_1", "" + temp_list_number.get(random));
        if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Column7 + "," + SudukoConst.str_Grid8,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_84,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            if (dataManager.getRoomID_CellPosition_IN(
                    SudukoConst.Room1, SudukoConst.str_Column8 + "," + SudukoConst.str_Grid8,
                    temp_list_number[0]
                )
            ) {
                dataManager.updateCellValue(
                    temp_list_number[0] + "",
                    SudukoConst.Cell_85,
                    SudukoConst.Room1
                )
                //                progress.cancel();
                Log.e("Sudoku", "Finish")
                genrateSudoko()
            } else {
                StartAgain60()
            }
        } else if (dataManager.getRoomID_CellPosition_IN(
                SudukoConst.Room1, SudukoConst.str_Column8 + "," + SudukoConst.str_Grid8,
                temp_list_number[random]
            )
        ) {
            dataManager.updateCellValue(
                temp_list_number[random] + "",
                SudukoConst.Cell_85,
                SudukoConst.Room1
            )
            temp_list_number.removeAt(random)
            if (dataManager.getRoomID_CellPosition_IN(
                    SudukoConst.Room1, SudukoConst.str_Column7 + "," + SudukoConst.str_Grid8,
                    temp_list_number[0]
                )
            ) {
                dataManager.updateCellValue(
                    temp_list_number[0] + "",
                    SudukoConst.Cell_84,
                    SudukoConst.Room1
                )
                Log.e("Sudoku", "Finish")
                //                progress.cancel();
                genrateSudoko()
            } else {

                StartAgain60()
            }
        } else {

            StartAgain60()
        }
    }
    private fun genrateSudoko() = runBlocking {
        prefManager.setCustomParam(SudukoConst.SelectedBox, "")
        list_suduko_random.clear()
        if (level.equals(SudukoConst.Level_Easy, ignoreCase = true)) {
            list_suduko_random.add(6)
            list_suduko_random.add(7)
        } else if (level.equals(SudukoConst.Level_Medium, ignoreCase = true)) {
            list_suduko_random.add(5)
            list_suduko_random.add(6)
        } else if (level.equals(SudukoConst.Level_Hard, ignoreCase = true)) {
            list_suduko_random.add(2)
            list_suduko_random.add(3)
            list_suduko_random.add(5)
            list_suduko_random.add(6)
        } else if (level.equals(SudukoConst.Level_Very_Hard, ignoreCase = true)) {
            list_suduko_random.add(2)
            list_suduko_random.add(3)
            list_suduko_random.add(4)
            list_suduko_random.add(5)
            list_suduko_random.add(6)
        }
        val list_suduko1 = ArrayList<String>()
        list_suduko1.addAll(list_suduko)
        for (i in 0..8) {
            list_suduko_result.clear()
            list_suduko_result =
                dataManager.getRoomID_CellValue(SudukoConst.Room1, i.toString() + "")

            val random = genrateRandomNumber1()
            for (j in 0 until list_suduko_random[random]) {
                val random1 = genrateRandomNumber2()
                dataManager.updateCellValuePlay(
                    list_suduko_result[random1].cellValue,
                    list_suduko_result[random1].cellPosition, roomId, "Y"
                )
                list_suduko_result.removeAt(random1)
            }
            if (level.equals(SudukoConst.Level_Hard, ignoreCase = true)) {
                if (list_suduko_random[random] == 2 || list_suduko_random[random] == 3) {
                    list_suduko_random.removeAt(random)
                }
            } else if (level.equals(SudukoConst.Level_Very_Hard, ignoreCase = true)) {
                if (list_suduko_random[random] == 2 || list_suduko_random[random] == 3 || list_suduko_random[random] == 6) {
                    list_suduko_random.removeAt(random)
                }
            }
        }
        prefManager.setCustomParamInt(SudukoConst.totalSudoku,roomId.toInt())

        // todo maintain 50 record history
        dataManager.deletePreviousSudukoPlay(level)
        sendBroadcastOfCompletion()
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
        // event log for sudoku
        MyApplication.logEvent(AppConstants.FirebaseEvents.Sudoku, Bundle().apply {
            putString(AppConstants.FirebaseEvents.deviceId, prefManager.getDeviceId())
            putString(AppConstants.FirebaseEvents.SudokuLevel, level)
        })

        val intent = Intent(AppConstants.WORK_MANAGER_SUDUKO_CREATE_STATUS)
        intent.putExtra("startAgain", false)
        intent.putExtra(SudukoConst.Level, level)
        intent.putExtra(SudukoConst.RoomId ,roomId)
        LocalBroadcastManager.getInstance(this.applicationContext).sendBroadcast(intent)
    }

}

