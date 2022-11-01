package com.example.cap.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDatabaseDao {
    @Insert
    suspend fun insert(exercise: ExerciseInfo)

    @Update
    suspend fun update(exercise: ExerciseInfo)

    @Query("SELECT * from daily_exercise_info_table")
    fun get(): Flow<List<ExerciseInfo>>

    @Query("SELECT * from daily_exercise_info_table WHERE date = :date")
    fun getToday(date: Long): ExerciseInfo

    @Query("DELETE FROM daily_exercise_info_table")
    suspend fun deleteAll()
}