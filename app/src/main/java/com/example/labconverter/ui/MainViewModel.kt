package com.example.labconverter.ui

import android.R
import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import kotlin.math.pow


class MainViewModel : ViewModel() {
    val liveInput = MutableLiveData<String>()
    val liveOutput = MutableLiveData<String>()
    val spinnerUnitId = MutableLiveData<Int>()
    val spinnerBaseId =  MutableLiveData<Int>()
    val spinnerConvertedId =  MutableLiveData<Int>()

    lateinit var base:String
    lateinit var converted:String

    private var conversionRate = 0.0

    init {
        liveInput.value = ""
        liveOutput.value = ""
        spinnerUnitId.value = 0
        spinnerConvertedId.value = 0
        spinnerBaseId.value = 0
    }

    fun onNumClick(num: String) {
        if(liveInput.value!!.length!=10){
        liveInput.value += num
            }
    }

    fun onDotClick() {
        if (!liveInput.value?.contains(".")!!) {
            liveInput.value += "."
        }
    }

    fun onDelClick() {
        if (liveInput.value?.isNotEmpty()!!) {
            liveInput.value = (liveInput.value!!.substring(0, liveInput.value!!.length - 1))
        } else {
            liveOutput.value = ""
        }
    }

    fun getWeightAdapter(context: Context): ArrayAdapter<String> {
        val weightList = context.resources.getStringArray(com.example.labconverter.R.array.Weight)
        val adapterWeight = ArrayAdapter(
            context,
            R.layout.simple_spinner_dropdown_item,
            weightList
        )
        return adapterWeight
    }
    fun getDistanceAdapter(context: Context): ArrayAdapter<String> {
        val distanceList = context.resources.getStringArray(com.example.labconverter.R.array.Distance)
        val adapterDistance = ArrayAdapter(
            context,
            R.layout.simple_spinner_dropdown_item,
            distanceList
        )
        return adapterDistance
    }
    fun getCurrencyAdapter(context: Context): ArrayAdapter<String> {
        val currencyList = context.resources.getStringArray(com.example.labconverter.R.array.Currency)
        val adapterCurrency = ArrayAdapter(
            context,
            R.layout.simple_spinner_dropdown_item,
            currencyList
        )
        return adapterCurrency
    }

    fun swapValues(context: Context){
        var temp = spinnerBaseId.value
        spinnerBaseId.value = spinnerConvertedId.value
        spinnerConvertedId.value = temp
        convert(context)
    }

    fun convert(context:Context) {
        if (liveInput.value?.isNotEmpty()!!) {
            if (spinnerBaseId.value == spinnerConvertedId.value) {
                Toast.makeText(
                    context,
                    "Please pick a unit to convert",
                    Toast.LENGTH_SHORT
                ).show()
                liveOutput.value = ""
            } else {
                if (base == "EUR" || base == "USD" || base == "RUB") {
                    getApiResult()
                } else {
                    conversionRate = getRate()
                    val text = (((liveInput.value)?.toDouble())!! * conversionRate).toString()

                    liveOutput.value = text
                }
            }
        } else {
            liveOutput.value = ""
        }
    }


    private fun getRate(): Double {
        val unitMap = mapOf(
            "Millimeter" to 0,
            "Meter" to 1,
            "Kilometer" to 2,
            "Gram" to 0,
            "Kilogram" to 1,
            "Ton" to 2
        )
        val power = unitMap.getValue(base) - unitMap.getValue(converted)
        return 1000.0.pow(power)
    }


    private fun getApiResult() {
        val api =
            "https://api.ratesapi.io/api/latest?base=$base&symbols=$converted"

        GlobalScope.launch(Dispatchers.IO) {

            try {

                val apiResult = URL(api).readText()
                val jsonObject = JSONObject(apiResult)
                conversionRate =
                    jsonObject.getJSONObject("rates").getString(converted)
                        .toDouble()

                withContext(Dispatchers.Main) {
                    val text = (((liveInput.value)?.toDouble())!! * conversionRate).toString()

                    liveOutput.value = text
                }

            } catch (e: Exception) {
                Log.e("Error!!!!", "" , e)
            }
        }
    }
}