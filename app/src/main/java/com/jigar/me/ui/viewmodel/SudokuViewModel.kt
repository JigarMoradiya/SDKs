package com.jigar.me.ui.viewmodel

import androidx.lifecycle.*
import com.jigar.me.data.local.db.sudoku.SudokuDB
import com.jigar.me.data.model.MainAPIResponseArray
import com.jigar.me.data.model.dbtable.exam.ExamHistory
import com.jigar.me.data.model.dbtable.inapp.InAppSkuDetails
import com.jigar.me.data.model.dbtable.suduko.Suduko
import com.jigar.me.data.model.dbtable.suduko.SudukoAnswerStatus
import com.jigar.me.data.model.dbtable.suduko.SudukoLevel
import com.jigar.me.data.model.dbtable.suduko.SudukoPlay
import com.jigar.me.data.repositories.ApiRepository
import com.jigar.me.data.repositories.DBRepository
import com.jigar.me.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SudokuViewModel @Inject constructor(private val sudokuDB: SudokuDB) : ViewModel() {
    // sudoku
    suspend fun getRoomID_CellValue(roomId : String, cellValue1 : String) = sudokuDB.getRoomID_CellValue(roomId, cellValue1)

    // level
    suspend fun getRoomID_SudokuLevel1(roomId : String) : List<SudukoLevel> = sudokuDB.getRoomID_SudokuLevel(roomId)
    suspend fun getRoomID_SudokuLevel(roomId : String) = sudokuDB.getRoomID_SudokuLevel(roomId)
    suspend fun insert_SudokuLevel(data : SudukoLevel) = sudokuDB.insert_SudokuLevel(data)
    suspend fun updatePlayTime_SudokuLevel(playTime : String, roomId : String) = sudokuDB.updatePlayTime_SudokuLevel(playTime,roomId)
    suspend fun updateStatus_SudokuLevel(status : String, roomId : String) = sudokuDB.updateStatus_SudokuLevel(status, roomId)
    // answer
    suspend fun getRoomID_SudokuAnswer1(roomId : String) = sudokuDB.getRoomID_SudokuAnswer(roomId)
    suspend fun getRoomID_SudokuAnswer(roomId : String) = sudokuDB.getRoomID_SudokuAnswer(roomId)
    suspend fun getRoomID_SudokuAnswer(roomId : String,otherCellPosition : String) : List<SudukoAnswerStatus> = sudokuDB.getRoomID_SudokuAnswer(roomId,otherCellPosition)
    suspend fun getRoomID_SudokuAnswer_CheckCellValue(roomId : String, cellValue : String) = sudokuDB.getRoomID_SudokuAnswer_CheckCellValue(roomId,cellValue)


    suspend fun insertSudukoAnswer(data : SudukoAnswerStatus) = sudokuDB.insertSudukoAnswer(data)
    suspend fun deleteAllAnswer(roomId: String) = sudokuDB.deleteAllAnswer(roomId)

    // play
    suspend fun getSudokuList(type : String)  = sudokuDB.getLevel_SudokuPlay(type)
    suspend fun getRoomID_CellPosition_SudokuPlay(roomId : String, cellPosition : String) : List<SudukoPlay> = sudokuDB.getRoomID_CellPosition_SudokuPlay(roomId, cellPosition)
    suspend fun getRoomID_CellValue_SudokuPlay(roomId : String, cellValue : String) = sudokuDB.getRoomID_CellValue_SudokuPlay(roomId,cellValue)
    suspend fun getCellNotes_SudokuPlay(roomId : String, cellPosition : String) : String = sudokuDB.getCellNotes_SudokuPlay(roomId,cellPosition)
    suspend fun getCellValue_SudokuPlay(roomId : String, cellPosition : String) = sudokuDB.getCellValue_SudokuPlay(roomId, cellPosition)
    suspend fun getRoomID_CellPosition_IN_List_For_Remove_SudokuPlay(roomId : String, cellPosition1 : String, list : ArrayList<String>) = sudokuDB.getRoomID_CellPosition_IN_List_For_Remove_SudokuPlay(roomId, cellPosition1, list)
    suspend fun getRoomID_DefaultSet_SudokuPlay(roomId : String, defaultSet : String) : List<SudukoPlay> = sudokuDB.getRoomID_DefaultSet_SudokuPlay(roomId, defaultSet)
    suspend fun getRoomID_CellPosition_IN_Quate_SudokuPlay(cellValue1: String, roomId : String, cellPosition : String) : List<String> = sudokuDB.getRoomID_CellPosition_IN_Quate_SudokuPlay(cellValue1, roomId, cellPosition)
    suspend fun getRoomID_CellPosition_IN_SudokuPlay(cellValue1: String, roomId : String, cellPosition1 : String) : List<String> = sudokuDB.getRoomID_CellPosition_IN_SudokuPlay(cellValue1, roomId, cellPosition1)
    suspend fun updateCellValuePlay(cellValue1 : String, cellPosition1 : String, roomId : String,defaultSet1 : String) = sudokuDB.updateCellValuePlay(cellValue1, cellPosition1, roomId,defaultSet1)
    suspend fun updateCellValuePlay(cellValue1 : String, cellPosition1 : String, roomId : String) = sudokuDB.updateCellValuePlay(cellValue1, cellPosition1, roomId)
    suspend fun updateCellNotesPlay(notes1 : String, cellPosition1 : String, roomId : String) = sudokuDB.updateCellNotesPlay(notes1, cellPosition1, roomId)
}