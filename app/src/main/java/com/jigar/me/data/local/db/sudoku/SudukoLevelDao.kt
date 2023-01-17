package com.jigar.me.data.local.db.sodoku

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jigar.me.data.model.dbtable.suduko.SudukoLevel

@Dao
interface SudukoLevelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: SudukoLevel)

    @Query("SELECT * FROM Suduko_Level WHERE roomID = :roomID")
    fun getRoomID(roomID: String): List<SudukoLevel>

    @Query("DELETE FROM Suduko_Level WHERE roomID = :roomID")
    suspend fun deleteAll(roomID: String)

    @Query("UPDATE Suduko_Level SET status = :status where roomID = :roomID")
    suspend fun updateStatus(status: String,roomID :String)

    @Query("UPDATE Suduko_Level SET playTime = :playTime where roomID = :roomID")
    suspend fun updatePlayTime(playTime : String,roomID :String)
}