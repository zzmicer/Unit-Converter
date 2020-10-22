package com.example.labconverter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.labconverter.R

class DataFragment : Fragment(R.layout.fragment_data) {
    private val viewModel by viewModels<MainViewModel>() //передаём бремя создания на функцию
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}