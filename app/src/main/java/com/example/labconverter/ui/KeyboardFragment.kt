package com.example.labconverter.ui

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.labconverter.R
import kotlinx.android.synthetic.main.fragment_data.*
import kotlinx.android.synthetic.main.fragment_data.view.*
import kotlinx.android.synthetic.main.fragment_keyboard.*


class KeyboardFragment : Fragment(R.layout.fragment_keyboard) {

   // private val viewModel by viewModels<MainViewModel>() //передаём бремя создания на функцию
    private val viewModel: MainViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        n0.setOnClickListener { viewModel.onNumClick("0")}
        n1.setOnClickListener { viewModel.onNumClick("1") }
        n2.setOnClickListener { viewModel.onNumClick("2") }
        n3.setOnClickListener { viewModel.onNumClick("3") }
        n4.setOnClickListener { viewModel.onNumClick("4") }
        n5.setOnClickListener { viewModel.onNumClick("5")}
        n6.setOnClickListener { viewModel.onNumClick("6") }
        n7.setOnClickListener { viewModel.onNumClick("7") }
        n8.setOnClickListener { viewModel.onNumClick("8")}
        n9.setOnClickListener { viewModel.onNumClick("9") }
        point.setOnClickListener { viewModel.onDotClick() }
        clear.setOnClickListener { viewModel.onDelClick()}


    }
}