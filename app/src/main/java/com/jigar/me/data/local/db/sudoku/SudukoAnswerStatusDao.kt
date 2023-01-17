package com.jigar.me.data.local.db.sodoku

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jigar.me.data.model.dbtable.suduko.SudukoAnswerStatus

@Dao
interface SudukoAnswerStatusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: SudukoAnswerStatus)

    @Query("SELECT * FROM Suduko_AnswerStatus WHERE roomID = :roomID AND otherCellPosition = :otherCellPosition")
    fun getRoomID(roomID: String, otherCellPosition : String): List<SudukoAnswerStatus>

    @Query("SELECT * FROM Suduko_AnswerStatus WHERE roomID = :roomID AND cellValue = :cellValue")
    fun getRoomIDCellValue(roomID: String, cellValue : String): List<SudukoAnswerStatus>

    @Query("SELECT * FROM Suduko_AnswerStatus WHERE roomID = :roomID")
    fun getRoomID(roomID: String): List<SudukoAnswerStatus>

    @Query("DELETE FROM Suduko_AnswerStatus WHERE roomID = :roomID")
    suspend fun deleteAll(roomID: String)
}