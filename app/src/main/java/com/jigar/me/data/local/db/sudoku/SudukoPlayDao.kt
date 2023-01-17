package com.jigar.me.data.local.db.sudoku

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jigar.me.data.model.dbtable.suduko.SudukoPlay

@Dao
interface SudukoPlayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: SudukoPlay)

    @Query("SELECT * FROM Suduko_Play WHERE level != '4 By 4' AND level != '6 By 6' GROUP BY roomID ORDER BY id DESC")
    fun getLevel(): List<SudukoPlay>

    @Query("SELECT * FROM Suduko_Play WHERE level == :type GROUP BY roomID ORDER BY id DESC")
    fun getLevel(type : String): List<SudukoPlay>

    @Query("SELECT * FROM Suduko_Play WHERE level != '4 By 4' AND level != '6 By 6' GROUP BY roomID ORDER BY id ASC")
    suspend fun getAllLevel9(): List<SudukoPlay>

    @Query("SELECT * FROM Suduko_Play WHERE level == :type GROUP BY roomID ORDER BY id ASC")
    suspend fun getAllLevel(type : String): List<SudukoPlay>

    @Query("SELECT * FROM Suduko_Play WHERE roomID = :roomID")
    suspend fun getRoomID(roomID: String): List<SudukoPlay>

    @Query("SELECT * FROM Suduko_Play WHERE roomID = :roomID AND cellPosition = :cellPosition")
    suspend fun getRoomID_CellPosition(roomID: String, cellPosition : String): List<SudukoPlay>

    @Query("SELECT cellPosition FROM Suduko_Play WHERE cellPosition IN (:cellPosition) AND roomID = :roomID AND cellValue = :cellValue")
    suspend fun getRoomID_CellPosition_IN_Quate(cellValue: String, roomID: String, cellPosition : List<String>): List<String>

    @Query("SELECT cellPosition FROM Suduko_Play WHERE roomID = :roomID AND cellValue = :cellValue AND cellPosition IN (:cellPosition)")
    suspend fun getRoomID_CellPosition_IN(cellValue: String, roomID : String, cellPosition : List<String>): List<String>

    @Query("SELECT * FROM Suduko_Play WHERE roomID = :roomID AND defaultSet = :defaultSet AND LENGTH(cellValue)>0")
    suspend fun getRoomID_DefaultSet(roomID: String, defaultSet : String): List<SudukoPlay>

    @Query("SELECT cellValue FROM Suduko_Play WHERE roomID = :roomID AND cellPosition = :cellPosition")
    suspend fun getCellValue(roomID: String, cellPosition : String): String

    @Query("SELECT notes FROM Suduko_Play WHERE roomID = :roomID AND cellPosition = :cellPosition")
    suspend fun getCellNotes(roomID: String, cellPosition : String): String

    @Query("SELECT * FROM Suduko_Play WHERE roomID = :roomID AND cellValue = :cellValue")
    suspend fun getRoomID_CellValue(roomID: String, cellValue : String): List<SudukoPlay>

    @Query("DELETE FROM Suduko_Play WHERE roomID = :roomID")
    suspend fun deleteAll(roomID: String)

    @Query("UPDATE Suduko_Play SET cellValue = :cellValue,defaultSet = :defaultSet where roomID = :roomID AND cellPosition = :cellPosition")
    suspend fun updateCellValue(cellValue : String,cellPosition : String, roomID :String,defaultSet : String)

    @Query("UPDATE Suduko_Play SET cellValue = :cellValue where roomID = :roomID AND cellPosition = :cellPosition")
    suspend fun updateCellValue(cellValue : String,cellPosition : String, roomID :String)

    @Query("UPDATE Suduko_Play SET notes = :notes where roomID = :roomID AND cellPosition = :cellPosition")
    suspend fun updateCellNotes(notes : String,cellPosition : String, roomID :String)

    @Query("SELECT cellValue FROM Suduko_Play WHERE roomID = :roomID AND cellPosition IN (:cellPosition) AND LENGTH(cellValue)>0")
    suspend fun getRoomID_CellPosition_IN_List_For_Remove(roomID: String, cellPosition : List<String>): List<String>
}