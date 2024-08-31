package com.example.jirodivisas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jirodivisas.Utils.Conversor
import com.example.jirodivisas.Utils.CurrencyViewModel
import com.example.jirodivisas.api.CRUD
import com.example.jirodivisas.currency.ExchangeRates
import com.example.jirodivisas.ui.theme.JiroDivisasTheme
import kotlinx.coroutines.async

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JiroDivisasTheme {
                ShowCurrencyConverter()
            }
        }
    }
}

@Preview
@Composable
private fun ShowCurrencyConverter() {
    Column {
        ShowCurrencies()
        ShowResults()
    }
}


private var currencyFrom = ""
private var currencyTo = ""

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShowCurrencies() {
    val viewModel = CurrencyViewModel()

    val fromCurrency = viewModel.CurrencyListScreen(viewModel)
    val endCurrency = viewModel.CurrencyListScreen(viewModel)
    var expanded1 by remember { mutableStateOf(false) }
    var expanded2 by remember { mutableStateOf(false) }
    var selectedText1 by remember { mutableStateOf(fromCurrency[0]) }
    var selectedText2 by remember { mutableStateOf(endCurrency.find { item -> item == "COP" }) }

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
        Column(Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "From",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)/*.background(color = Color.Red)*/,
                    style = TextStyle(
                        textAlign = TextAlign.Center, color = Color.Black, fontSize = 25.sp
                    )
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier.padding(25.dp)
                ) {
                    ExposedDropdownMenuBox(expanded = expanded1, onExpandedChange = {
                        expanded1 = !expanded1
                    }) {
                        TextField(
                            value = selectedText1,
                            onValueChange = { selectedText1 = it },
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded1) },
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(expanded = expanded1,
                            onDismissRequest = { expanded1 = false }) {
                            fromCurrency.forEach { item ->
                                DropdownMenuItem(text = { Text(text = item) }, onClick = {
                                    selectedText1 = item
                                    expanded1 = false
//                                Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                })
                            }
                        }
                    }
                }
            }
        }

        Column(Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "To",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)/*.background(color = Color.Red)*/,
                    style = TextStyle(
                        textAlign = TextAlign.Center, color = Color.Black, fontSize = 25.sp
                    )
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier.padding(25.dp)
                ) {
                    ExposedDropdownMenuBox(expanded = expanded2, onExpandedChange = {
                        expanded2 = !expanded2
                    }) {
                        TextField(
                            value = selectedText2!!,
                            onValueChange = { selectedText2 = it },
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded2) },
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(expanded = expanded2,
                            onDismissRequest = { expanded2 = false }) {
                            endCurrency.forEach { item ->
                                DropdownMenuItem(text = { Text(text = item) }, onClick = {
                                    selectedText2 = item
                                    expanded2 = false
//                                Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                })
                            }
                        }
                    }
                }
            }
        }
    }
    currencyFrom = selectedText1
    currencyTo = selectedText2!!

}

var result = 0.0
@Composable
private fun ShowResults() {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    var exchangeRates = reloadResult()

    Row(
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End
    ) {
        Column(Modifier.weight(1f)) {
            Box(
                modifier = Modifier.padding(32.dp)
            ) {
                //Controlar aqui el cambio de texto tras transformar divisa
                TextField(value = text, onValueChange = { newText ->
                    text = newText
                })
            }
        }

//        if (text.text.isNotEmpty() && text.text.toDoubleOrNull() != null && exchangeRates != null) {
//            result = Conversor().currencyConversor(
//                currencyFrom, text.text.toDouble(), currencyTo, exchangeRates.conversionRates
//            )
//        }
        updateResults(text, exchangeRates)

        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(30.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (exchangeRates != null) {
                    //Esto es lo que se muestra si se puede hacer la conversion
                    Text(text = result.toString())
                } else {
                    //Esto se muestra si no se se puede hacer la conversion
                    Text("Cargando datos...")
                }
            }
        }
    }
}

@Composable
private fun updateResults(text: TextFieldValue, exchangeRates: ExchangeRates?) {
    if (text.text.isNotEmpty() && text.text.toDoubleOrNull() != null && exchangeRates != null) {
        result = Conversor().currencyConversor(
            currencyFrom, text.text.toDouble(), currencyTo, exchangeRates.conversionRates
        )
    }
}

@Composable
private fun reloadResult(): ExchangeRates? {
    var exchangeRates by remember { mutableStateOf<ExchangeRates?>(null) }

    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = currencyFrom) {
        exchangeRates = scope.async {
            CRUD().getLatestRates(currencyFrom)
        }.await()
    }
    return exchangeRates
}