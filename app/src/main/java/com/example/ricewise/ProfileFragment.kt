package com.example.ricewise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ScrollView

class ProfileFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileScrollView = view.findViewById<ScrollView>(R.id.profileScrollView)
        val editProfileScrollView = view.findViewById<ScrollView>(R.id.editProfileScrollView)

        val btnEditProfile = view.findViewById<Button>(R.id.btnEditProfile)
        val btnBackToProfile = view.findViewById<Button>(R.id.cancelBtn)

        btnEditProfile.setOnClickListener {
            profileScrollView.visibility = View.GONE
            editProfileScrollView.visibility = View.VISIBLE
        }

        btnBackToProfile.setOnClickListener {
            editProfileScrollView.visibility = View.GONE
            profileScrollView.visibility = View.VISIBLE
        }
    }
}