package com.example.ricewise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class HomeCategory(
    val name: String,
    val category: String // "recommended", "top", or "my"
)
class HomeFragment : Fragment(R.layout.fragment_home) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recommended = listOf(
            HomeCategory("RC-216", "recommended"),
            HomeCategory("IR64", "recommended"),
            HomeCategory("NSIC Rc222", "recommended"),
            HomeCategory("PSB Rc10", "recommended")
        )

        val top = listOf(
            HomeCategory("SL-8H", "top"),
            HomeCategory("Rc160", "top"),
            HomeCategory("Rc480", "top"),
            HomeCategory("Rc82", "top")
        )

        val myVarieties = listOf(
            HomeCategory("MyRc-1", "my"),
            HomeCategory("MyRc-2", "my"),
            HomeCategory("MyRc-3", "my"),
            HomeCategory("MyRc-4", "my")
        )

        // Dummy weather data
        view.findViewById<TextView>(R.id.locationText).text = "Philippines"
        view.findViewById<TextView>(R.id.temperatureText).text = "32°C - Sunny"
        view.findViewById<TextView>(R.id.dateText).text = getCurrentDate()

        setupRecycler(view.findViewById(R.id.recyclerRecommended), recommended)
        setupRecycler(view.findViewById(R.id.recyclerTop), top)
        setupRecycler(view.findViewById(R.id.recyclerMy), myVarieties)
    }

    private fun setupRecycler(recyclerView: RecyclerView, data: List<HomeCategory>) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = VarietyHomeAdapter(data)
    }

    private fun getCurrentDate(): String {
        val formatter = SimpleDateFormat("EEEE, MMM d", Locale.getDefault())
        return formatter.format(Date())
    }
}

