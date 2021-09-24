package com.example.kotlincurrencyconverter.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlincurrencyconverter.data.model.ConversionRates
import com.example.kotlincurrencyconverter.util.DispatcherProvider
import com.example.kotlincurrencyconverter.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class MainViewModel @Inject constructor(
    //we want to inject our repository
    //we can also pass another repository that implements the MainRepository interface
    //so for testing purposes, we can pass in a special repository in this constructor for testing purposes
    private val repository: MainRepository,
    //inject coroutine dispatchers
    //we can also pass default or special coroutine dispatcher here depending on usage or testing purposes
    //instead of hardcoding our dispatcher
    private val dispatcher: DispatcherProvider
) : ViewModel() {

    //create currency event class that just represent these events we sent from the view model to activity
    sealed class CurrencyEvent {
        class Success(val resultText: String) : CurrencyEvent()
        class Failure(val errorText: String) : CurrencyEvent()
        object Loading : CurrencyEvent()
        object Empty : CurrencyEvent()
    }

    //state flow
    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion: StateFlow<CurrencyEvent> = _conversion

    //function that converts the currency and makes the request
    fun convert(amountStr: String, fromCurrency: String, toCurrency: String) {
        val fromAmount = amountStr.toFloatOrNull()
        if (fromAmount == null) {
            _conversion.value = CurrencyEvent.Failure("Not a valid amount")
            return
        }

        viewModelScope.launch(dispatcher.io) {
            //show progress bar
            _conversion.value = CurrencyEvent.Loading
            //expression to get rates
            when (val ratesResponse = repository.getRates(fromCurrency)) {
                is Resource.Error -> _conversion.value =
                    CurrencyEvent.Failure(ratesResponse.errorMessage!!)
                is Resource.Success -> {
                    val rates = ratesResponse.data!!.conversion_rates
                    val rate = getRateForCurrency(toCurrency, rates)
                    if (rate == null) {
                        _conversion.value = CurrencyEvent.Failure("Unexpected Error")
                    } else {
                        //round to two decimal places
                        val convertedCurrency = round(fromAmount * rate * 100) / 100
                        _conversion.value =
                            CurrencyEvent.Success("$fromAmount $fromCurrency = $convertedCurrency $toCurrency")
                    }
                }
            }
        }
    }

    //takes currency and takes rates response and returns the corresponding value of the rates class
    private fun getRateForCurrency(currency: String, rates: ConversionRates) = when (currency) {
        "CAD" -> rates.CAD
        "HKD" -> rates.HKD
        "ISK" -> rates.ISK
        "EUR" -> rates.EUR
        "PHP" -> rates.PHP
        "DKK" -> rates.DKK
        "HUF" -> rates.HUF
        "CZK" -> rates.CZK
        "AUD" -> rates.AUD
        "RON" -> rates.RON
        "SEK" -> rates.SEK
        "IDR" -> rates.IDR
        "INR" -> rates.INR
        "BRL" -> rates.BRL
        "RUB" -> rates.RUB
        "HRK" -> rates.HRK
        "JPY" -> rates.JPY
        "THB" -> rates.THB
        "CHF" -> rates.CHF
        "SGD" -> rates.SGD
        "PLN" -> rates.PLN
        "BGN" -> rates.BGN
        "CNY" -> rates.CNY
        "NOK" -> rates.NOK
        "NZD" -> rates.NZD
        "ZAR" -> rates.ZAR
        "USD" -> rates.USD
        "MXN" -> rates.MXN
        "ILS" -> rates.ILS
        "GBP" -> rates.GBP
        "KRW" -> rates.KRW
        "MYR" -> rates.MYR
        else -> null
    }
}