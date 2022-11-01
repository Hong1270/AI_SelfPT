package com.example.cap.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_exercise_info_table")
data class ExerciseInfo (
    @PrimaryKey(autoGenerate = true)
    var exerciseId: Long = 0L,

    @ColumnInfo(name = "date")
    val date: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "exercise_name")
    var exerciseName: String = "랫풀다운",

    @ColumnInfo(name = "achievement")
    var achievement: Int = 0
)