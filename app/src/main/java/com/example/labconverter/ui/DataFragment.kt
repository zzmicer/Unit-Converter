package com.example.labconverter.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.labconverter.R
import kotlinx.android.synthetic.main.fragment_data.*

class DataFragment : Fragment(R.layout.fragment_data) {

    //private val viewModel by viewModels<MainViewModel>() //передаём бремя создания на функцию
    private val viewModel: MainViewModel by activityViewModels()
    lateinit var currUnit: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.liveInput.observe(
            viewLifecycleOwner,
            Observer { et_firstConversion.setText(it) })
        viewModel.liveOutput.observe(
            viewLifecycleOwner,
            Observer { et_secondConversion.setText(it) })
        viewModel.spinnerUnitId.observe(
            viewLifecycleOwner,
            Observer { spinner_chooseUnit.setSelection(it) })
        viewModel.spinnerBaseId.observe(
            viewLifecycleOwner,
            Observer { spinner_firstConversion.setSelection(it) })
        viewModel.spinnerConvertedId.observe(
            viewLifecycleOwner,
            Observer { spinner_secondConversion.setSelection(it) })


        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.units,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_chooseUnit.adapter = adapter
        }

        swap.setOnClickListener { viewModel.swapValues(requireContext()) }
        copy.setOnClickListener {
            var myClipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            var myClip: ClipData = ClipData.newPlainText("note_copy", viewModel.liveOutput.value)
            myClipboard.setPrimaryClip(myClip)
            Toast.makeText(
                context,
                "Output copied!",
                Toast.LENGTH_SHORT
            ).show()
        }

        /**!GOVNOCODE ALLERT!*/
        spinner_chooseUnit.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.spinnerUnitId.value = position
                currUnit = parent?.getItemAtPosition(position).toString()

                when (currUnit) {
                    "Currency" -> {
                        spinner_firstConversion.adapter = context?.let {
                            viewModel.getCurrencyAdapter(
                                it
                            )
                        }
                        spinner_secondConversion.adapter = context?.let {
                            viewModel.getCurrencyAdapter(
                                it
                            )
                        }
                        spinner_firstConversion.setSelection(viewModel.spinnerBaseId.value!!)
                        spinner_secondConversion.setSelection(viewModel.spinnerConvertedId.value!!)
                        viewModel.base = "USD"
                        viewModel.converted = "EUR"
                    }
                    "Weight" -> {

                        spinner_firstConversion.adapter = context?.let {
                            viewModel.getWeightAdapter(
                                it
                            )
                        }
                        spinner_secondConversion.adapter = context?.let {
                            viewModel.getWeightAdapter(
                                it
                            )
                        }
                        spinner_firstConversion.setSelection(viewModel.spinnerBaseId.value!!)
                        spinner_secondConversion.setSelection(viewModel.spinnerConvertedId.value!!)
                        viewModel.base = "Gram"
                        viewModel.converted = "Kilogram"
                    }
                    "Distance" -> {
                        spinner_firstConversion.adapter = context?.let {
                            viewModel.getDistanceAdapter(
                                it
                            )
                        }
                        spinner_secondConversion.adapter = context?.let {
                            viewModel.getDistanceAdapter(
                                it
                            )
                        }
                        spinner_firstConversion.setSelection(viewModel.spinnerBaseId.value!!)
                        spinner_secondConversion.setSelection(viewModel.spinnerConvertedId.value!!)
                        viewModel.base = "Millimeter"
                        viewModel.converted = "Meter"
                    }
                }
            }
        })
        spinner_firstConversion.onItemSelectedListener =
            (object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.spinnerBaseId.value = position
                    viewModel.base = parent?.getItemAtPosition(position).toString()
                    viewModel.convert(requireContext())
                }

            })
        spinner_secondConversion.onItemSelectedListener =
            (object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.spinnerConvertedId.value = position
                    viewModel.converted = parent?.getItemAtPosition(position).toString()
                    viewModel.convert(requireContext())
                }

            })



        et_firstConversion.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    viewModel.convert(requireContext())
                } catch (e: Exception) {
                    Toast.makeText(context, "Type a value", Toast.LENGTH_SHORT).show()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("Main", "Before Text Changed")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("Main", "OnTextChanged")
            }

        })

    }
}