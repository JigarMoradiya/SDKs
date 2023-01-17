package com.jigar.me.data.local.db.sodoku

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jigar.me.data.model.dbtable.suduko.Suduko

@Dao
interface SudukoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Suduko)

    @Query("SELECT * FROM Suduko WHERE roomID = :roomID AND cellValue = :cellValue")
    suspend fun getRoomID_CellValue(roomID: String, cellValue : String): List<Suduko>

    @Query("SELECT * FROM Suduko WHERE roomID = :roomID AND cellPosition IN (:cellPosition) AND LENGTH(cellValue) > 0 GROUP BY cellValue")
    suspend fun getRoomID_CellPosition_IN_List_For_Add(roomID: String, cellPosition :List<String> ): List<Suduko>

    @Query("SELECT cellValue FROM Suduko WHERE roomID = :roomID AND cellPosition = :cellPosition")
    suspend fun getRoomID_CellPositionString(roomID: String, cellPosition : String): String

    @Query("SELECT CASE WHEN (COUNT(*) > 0) THEN 1 ELSE 0 END FROM Suduko WHERE roomID = :roomID AND cellPosition = :cellPosition AND LENGTH(cellValue) == 0")
    suspend fun getRoomID_CellPositionString_Set_OR_Not(roomID: String, cellPosition : String): Boolean

    @Query("SELECT CASE WHEN (COUNT(*) > 0) THEN 0 ELSE 1 END FROM Suduko WHERE roomID = :roomID AND cellPosition IN (:cellPosition) AND cellValue = :randomNumber")
    suspend fun getRoomID_CellPosition_IN(roomID: String, cellPosition : List<String>, randomNumber : String): Boolean

    @Query("SELECT cellValue FROM Suduko WHERE roomID = :roomID AND cellPosition IN (:cellPosition) AND LENGTH(cellValue) > 0")
    suspend fun getRoomID_CellPosition_IN_List_For_RemoveSuduko(roomID: String, cellPosition : List<String>): List<String>

    @Query("SELECT cellValue FROM Suduko WHERE roomID = :roomID AND cellPosition IN (:cellPosition) GROUP BY cellValue")
    suspend fun getRoomID_CellPosition_IN_List_For_Add_String(roomID: String, cellPosition : List<String>): List<String>

    @Query("DELETE FROM Suduko WHERE roomID = :roomID")
    suspend fun deleteAll(roomID: String)

    @Query("UPDATE Suduko SET cellValue = :cellValue where roomID = :roomID AND cellPosition = :cellPosition")
    suspend fun updateCellValue(cellValue : String,cellPosition : String, roomID :String)

    @Query("UPDATE Suduko SET cellValue = :cellValue where roomID = :roomID AND cellPosition IN (:cellPosition)")
    suspend fun updateCellValue_IN(cellValue : String,cellPosition : List<String>, roomID :String)

}