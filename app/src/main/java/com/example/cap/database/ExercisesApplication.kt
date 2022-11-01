package com.example.cap.database

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ExercisesApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { ExerciseDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { ExerciseRepository(database.exerciseDatabaseDao()) }
}