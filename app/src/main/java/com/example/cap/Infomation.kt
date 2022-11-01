/*package com.example.cap

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment


class Infomation : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(com.example.cap.R.layout.infomaition, container, false)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val latpull: ImageButton = view.findViewById(com.example.cap.R.id.info_latpull)
        latpull.setOnClickListener {
            val nextIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/CAwf7n6Luuc"))
            startActivity(nextIntent)
        }

        val bench: ImageButton = view.findViewById(com.example.cap.R.id.info_bench)
        bench.setOnClickListener {
            val nextIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/vthMCtgVtFw"))
            startActivity(nextIntent)
        }
        val squat : ImageButton = view.findViewById(R.id.info_squat)
        squat.setOnClickListener {
            val nextIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/bEv6CCg2BC8"))
            startActivity(nextIntent)
        }
        val deadlift : ImageButton = view.findViewById(R.id.info_dead)
        deadlift.setOnClickListener {
            val nextIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/VL5Ab0T07e4"))
            startActivity(nextIntent)
        }
    }
}
*/