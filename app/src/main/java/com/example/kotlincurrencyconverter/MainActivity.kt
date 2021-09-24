package com.example.kotlincurrencyconverter

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.example.kotlincurrencyconverter.databinding.ActivityMainBinding
import com.example.kotlincurrencyconverter.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


//annotate in order to inject dependencies into this activity and we do it with the help of viewModel
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var sameValueStr: String
    //this is how to inject viewModels using dagger hilt
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnConvert.setOnClickListener {
            if (binding.etFrom.text.toString().isNotEmpty()) {
                if (binding.spFromCurrency.selectedItem.toString() == binding.spToCurrency.selectedItem.toString()) {
                    sameValueStr = "${ binding.etFrom.text.toString()} ${binding.spFromCurrency.selectedItem}" +
                            " = ${binding.etFrom.text.toString()} ${binding.spFromCurrency.selectedItem}"
                    binding.tvResult.text = sameValueStr
                } else{
                    viewModel.convert(
                        binding.etFrom.text.toString(),
                        binding.spFromCurrency.selectedItem.toString(),
                        binding.spToCurrency.selectedItem.toString(),
                    )
                }
            } else{
                viewModel.convert(
                    binding.etFrom.text.toString(),
                    binding.spFromCurrency.selectedItem.toString(),
                    binding.spToCurrency.selectedItem.toString(),
                )
            }

        }

        //collect events from stateflow
        //launch coroutine in lifecycle scope
        lifecycleScope.launchWhenStarted {
            viewModel.conversion.collect { event ->
                when (event) {
                    is MainViewModel.CurrencyEvent.Success -> {
                        binding.progressBar.isVisible = false
                        binding.tvResult.setTextColor(Color.BLACK)
                        binding.tvResult.text = event.resultText
                    }
                    is MainViewModel.CurrencyEvent.Failure -> {
                        binding.progressBar.isVisible = false
                        binding.tvResult.setTextColor(Color.RED)
                        binding.tvResult.text = event.errorText
                    }
                    is MainViewModel.CurrencyEvent.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    //unit means do nothing / void
                    else -> Unit
                }
            }
        }
    }
}