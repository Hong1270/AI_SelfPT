package com.example.cap.database

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class ExerciseInfoViewModel(private val repository: ExerciseRepository) : ViewModel() {

    val allExercise: LiveData<List<ExerciseInfo>> = repository.allExercise.asLiveData()

    fun insert(exercise: ExerciseInfo) = viewModelScope.launch {
        repository.insert(exercise)
    }

    fun getToday(today: Long) = repository.getToday(today)

    private val _navigateToExerciseInfo = MutableLiveData<Boolean?>()
}

class ExerciseViewModelFactory(private val repository: ExerciseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExerciseInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExerciseInfoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}