package com.example.cap

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cap.adapter.ExerciseListAdapter
import com.example.cap.database.ExerciseInfoViewModel
import com.example.cap.database.ExerciseViewModelFactory
import com.example.cap.database.ExercisesApplication
import com.skyhope.eventcalenderlibrary.CalenderEvent
import com.skyhope.eventcalenderlibrary.listener.CalenderDayClickListener
import com.skyhope.eventcalenderlibrary.model.Event


class Mypage : Fragment() {
    private val TAG = "Mypage"

    private val exerciseViewModel: ExerciseInfoViewModel by viewModels {
        ExerciseViewModelFactory((activity?.application as ExercisesApplication).repository)
    }

    override fun onCreateView(
       inflater: LayoutInflater, container: ViewGroup?,
       savedInstanceState:Bundle?
    ):View? {
        val view = inflater.inflate(R.layout.mypage, container, false)

        val calendarEvent = view.findViewById<CalenderEvent>(R.id.calendar_event)

        calendarEvent.initCalderItemClickCallback(CalenderDayClickListener { dayContainerModel ->
            Log.d(
                TAG,
                dayContainerModel.date
            )
        })

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = ExerciseListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        exerciseViewModel.allExercise.observe(viewLifecycleOwner, Observer { exercises ->
           exercises?.let { adapter.submitList(it) }
            Log.d(TAG, "뷰모델 결과: ")
            for (e in exercises) {
                Log.d(TAG, "${e.exerciseId}")
                Log.d(TAG, "성취도: ${e.achievement}")
                val success = e.achievement >= 75
                val event = Event(
                    e.date,
                    if (success) "●" else "▲",
                    if (success) Color.BLUE else Color.RED)
                calendarEvent.addEvent(event)
            }
        })

        return view
    }
}