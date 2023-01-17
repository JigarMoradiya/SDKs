package com.jigar.me.data.local.db.sudoku

import com.jigar.me.data.local.db.sodoku.SudukoAnswerStatusDao
import com.jigar.me.data.local.db.sodoku.SudukoDao
import com.jigar.me.data.local.db.sodoku.SudukoLevelDao
import com.jigar.me.data.model.dbtable.suduko.Suduko
import com.jigar.me.data.model.dbtable.suduko.SudukoAnswerStatus
import com.jigar.me.data.model.dbtable.suduko.SudukoLevel
import com.jigar.me.data.model.dbtable.suduko.SudukoPlay
import com.jigar.me.utils.Constants
import com.jigar.me.utils.sudoku.SudukoConst
import javax.inject.Inject

class SudokuDB @Inject constructor(
    private val sudokuDao: SudukoDao,
    private val sudokuLevelDao: SudukoLevelDao,
    private val sudokuPlayDao: SudukoPlayDao,
    private val sudokuAnswerStatusDao: SudukoAnswerStatusDao
) {

    // suduko
    private fun getFinalListAfterRemove(
        listSuduko1: ArrayList<String>,
        finalList: ArrayList<String>
    ): ArrayList<String> {
        finalList.map {
            listSuduko1.remove(it)
        }
        return listSuduko1
    }

    private fun getFinalListAfterAdd(
        listSuduko1: ArrayList<String>,
        finalList: ArrayList<String>
    ): ArrayList<String> {
        val newList = ArrayList<String>()
        finalList.map {
            if (listSuduko1.contains(it)) {
                newList.add(it)
            }
        }
        return newList
    }

    suspend fun insertSuduko(data: Suduko) {
        sudokuDao.insert(data)
    }

    suspend fun insertSudukoAnswer(data: SudukoAnswerStatus) {
        sudokuAnswerStatusDao.insert(data)
    }

    suspend fun deleteSuduko(roomId: String) {
        sudokuDao.deleteAll(roomId)
    }

    suspend fun deleteSudukoPlay(roomId: String) {
        sudokuPlayDao.deleteAll(roomId)
    }

    suspend fun deletePreviousSudukoPlay(level: String) {
        val list = if (level == SudukoConst.Level_4By4 || level == SudukoConst.Level_6By6) {
            sudokuPlayDao.getAllLevel(level)
        } else {
            sudokuPlayDao.getAllLevel9()
        }
        if (list.size > Constants.sudokuMaxRecordHistory) {
            sudokuPlayDao.deleteAll(list[0].roomID)
        }

    }

    suspend fun deleteAllAnswer(roomId: String) {
        sudokuAnswerStatusDao.deleteAll(roomId)
    }

    suspend fun updateCellValue(cellValue1: String, cellPosition1: String, roomId: String) {
        sudokuDao.updateCellValue(cellValue1, cellPosition1, roomId)
    }

    suspend fun updateCellValue_IN(cellValue1: String, cellPosition1: String, roomId: String) {
        val result = cellPosition1.replace("'", "").split(",").map { it.trim() }
        sudokuDao.updateCellValue_IN(cellValue1, result, roomId)
    }

    suspend fun getRoomID_CellPosition_IN(
        roomId: String,
        cellPosition1: String,
        randome_number: String
    ): Boolean {
        val result = cellPosition1.replace("'", "").split(",").map { it.trim() }
        return sudokuDao.getRoomID_CellPosition_IN(roomId, result, randome_number)
    }

    suspend fun getRoomID_CellPositionString_Set_OR_Not(
        roomId: String,
        cellPosition1: String
    ): Boolean {
        return sudokuDao.getRoomID_CellPositionString_Set_OR_Not(roomId, cellPosition1)
    }

    suspend fun getRoomID_CellPositionString(roomId: String, cellPosition1: String): String {
        return sudokuDao.getRoomID_CellPositionString(roomId, cellPosition1)
    }

    suspend fun getRoomID_CellValue(roomId: String, cellValue1: String): ArrayList<Suduko> {
        return sudokuDao.getRoomID_CellValue(roomId, cellValue1) as ArrayList<Suduko>
    }

    suspend fun getRoomID_CellPosition_IN_List_For_Remove(
        roomId: String,
        cellPosition1: String,
        list: ArrayList<String>
    ): ArrayList<String> {
        val result = cellPosition1.replace("'", "").split(",").map { it.trim() }
        val listResult = sudokuDao.getRoomID_CellPosition_IN_List_For_RemoveSuduko(roomId, result)
        return getFinalListAfterRemove(list, listResult as ArrayList<String>)
    }

    suspend fun getRoomID_CellPosition_IN_List_For_Add(
        roomId: String,
        cellPosition1: String
    ): ArrayList<Suduko> {
        val result = cellPosition1.replace("'", "").split(",").map { it.trim() }
        return sudokuDao.getRoomID_CellPosition_IN_List_For_Add(roomId, result) as ArrayList<Suduko>
    }

    suspend fun getRoomID_CellPosition_IN_List_For_Add(
        roomId: String,
        cellPosition1: String,
        list: ArrayList<String>
    ): ArrayList<String> {
        val result = cellPosition1.replace("'", "").split(",").map { it.trim() }
        val listResult = sudokuDao.getRoomID_CellPosition_IN_List_For_Add_String(roomId, result)
        return getFinalListAfterAdd(list, listResult as ArrayList<String>)
    }

    // play
    suspend fun getRoomID_CellPosition_IN_List_For_Remove_SudokuPlay(
        roomId: String,
        cellPosition1: String,
        list: ArrayList<String>
    ): ArrayList<String> {
        val result = cellPosition1.replace("'", "").split(",").map { it.trim() }
        val listResult = sudokuPlayDao.getRoomID_CellPosition_IN_List_For_Remove(roomId, result)
        return getFinalListAfterRemove(list, listResult as ArrayList<String>)
    }

    suspend fun insertSudukoPlay(data: SudukoPlay) {
        sudokuPlayDao.insert(data)
    }

    suspend fun getRoomID_CellPosition_SudokuPlay(
        roomId: String,
        cellPosition: String
    ): List<SudukoPlay> {
        return sudokuPlayDao.getRoomID_CellPosition(roomId, cellPosition)
    }

    suspend fun getSudukoPlay(roomId: String): List<SudukoPlay> {
        return sudokuPlayDao.getRoomID(roomId)
    }

    suspend fun getRoomID_CellPosition_IN_SudokuPlay(
        cellValue1: String,
        roomId: String,
        cellPosition1: String
    ): List<String> {
        val result = cellPosition1.replace("'", "").split(",").map { it.trim() }
        return sudokuPlayDao.getRoomID_CellPosition_IN(cellValue1, roomId, result)
    }

    suspend fun getRoomID_CellPosition_IN_Quate_SudokuPlay(
        cellValue1: String,
        roomId: String,
        cellPosition1: String
    ): List<String> {
        val result = cellPosition1.replace("'", "").split(",").map { it.trim() }
        val finalResult = sudokuPlayDao.getRoomID_CellPosition_IN_Quate(cellValue1, roomId, result)
        val newResult: ArrayList<String> = arrayListOf()
        finalResult.map {
            newResult.add("'$it'")
        }
        return newResult as List<String>
    }

    suspend fun getRoomID_DefaultSet_SudokuPlay(
        roomId: String,
        defaultSet: String
    ): List<SudukoPlay> {
        return sudokuPlayDao.getRoomID_DefaultSet(roomId, defaultSet)
    }

    suspend fun getCellNotes_SudokuPlay(roomId: String, cellPosition: String): String {
        return sudokuPlayDao.getCellNotes(roomId, cellPosition)
    }

    fun getLevel_SudokuPlay(type: String): List<SudukoPlay> {
        return if (type == SudukoConst.Level_9By9) {
            sudokuPlayDao.getLevel()
        } else {
            sudokuPlayDao.getLevel(type)
        }

    }

    suspend fun getCellValue_SudokuPlay(roomId: String, cellPosition: String): String {
        return sudokuPlayDao.getCellValue(roomId, cellPosition)
    }

    suspend fun getRoomID_CellValue_SudokuPlay(
        roomId: String,
        cellValue: String
    ): List<SudukoPlay> {
        return sudokuPlayDao.getRoomID_CellValue(roomId, cellValue)
    }

    suspend fun updateCellValuePlay(
        cellValue1: String,
        cellPosition1: String,
        roomId: String,
        defaultSet1: String
    ) {
        sudokuPlayDao.updateCellValue(cellValue1, cellPosition1, roomId, defaultSet1)
    }

    suspend fun updateCellValuePlay(cellValue1: String, cellPosition1: String, roomId: String) {
        sudokuPlayDao.updateCellValue(cellValue1, cellPosition1, roomId)
    }

    suspend fun updateCellNotesPlay(notes1: String, cellPosition1: String, roomId: String) {
        sudokuPlayDao.updateCellNotes(notes1, cellPosition1, roomId)
    }

    // answer
    suspend fun getRoomID_SudokuAnswer(
        roomId: String,
        otherCellPosition: String
    ): List<SudukoAnswerStatus> {
        return sudokuAnswerStatusDao.getRoomID(roomId, otherCellPosition)
    }

    suspend fun getRoomID_SudokuAnswer_CheckCellValue(
        roomId: String,
        cellValue: String
    ): List<SudukoAnswerStatus> {
        return sudokuAnswerStatusDao.getRoomIDCellValue(roomId, cellValue)
    }

    suspend fun getRoomID_SudokuAnswer(roomId: String): List<SudukoAnswerStatus> {
        return sudokuAnswerStatusDao.getRoomID(roomId)
    }

    // level
    suspend fun getRoomID_SudokuLevel(roomId: String): List<SudukoLevel> {
        return sudokuLevelDao.getRoomID(roomId)
    }

    suspend fun insert_SudokuLevel(data: SudukoLevel) {
        return sudokuLevelDao.insert(data)
    }

    suspend fun updatePlayTime_SudokuLevel(playTime: String, roomId: String) {
        return sudokuLevelDao.updatePlayTime(playTime, roomId)
    }

    suspend fun updateStatus_SudokuLevel(status: String, roomId: String) {
        return sudokuLevelDao.updateStatus(status, roomId)
    }

}
