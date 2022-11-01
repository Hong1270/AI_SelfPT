package com.example.cap.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [ExerciseInfo::class], version = 1, exportSchema = false)
abstract class ExerciseDatabase: RoomDatabase() {
    abstract fun exerciseDatabaseDao(): ExerciseDatabaseDao

    private class ExerciseDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.exerciseDatabaseDao())
                }
            }
        }

        suspend fun populateDatabase(exerciseDao: ExerciseDatabaseDao) {
            // Delete all content here.
            exerciseDao.deleteAll()

            // Add sample words.
            var exercise = ExerciseInfo(exerciseName = "랫풀다운")
            exerciseDao.insert(exercise)
            exercise = ExerciseInfo(exerciseName = "벤치프레스")
            exerciseDao.insert(exercise)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ExerciseDatabase? = null

        fun getDatabase(context: Context,
                        scope: CoroutineScope): ExerciseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExerciseDatabase::class.java,
                    "exercise_database"
                ).addCallback(ExerciseDatabaseCallback(scope)).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}