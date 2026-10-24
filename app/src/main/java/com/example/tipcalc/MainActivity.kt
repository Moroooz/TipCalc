package com.example.tipcalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipcalc.ui.theme.TipCalcTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalcTheme {
                Surface (
                    modifier = Modifier.fillMaxSize()
                ) {
                    CalculatorScreen()
                }
            }
        }
    }
}


@Composable
fun CalculatorScreen() {
    var orderAmount by remember { mutableStateOf("") }
    var dishesCountText by remember { mutableStateOf("") }
    var tipPercentage by remember { mutableStateOf(5f) }

    val dishesCount = dishesCountText.toIntOrNull() ?: 0
    val selectedDiscount = when {
        dishesCount in 1..2 -> 3
        dishesCount in 3..5 -> 5
        dishesCount in 6..10 -> 7
        dishesCount > 10 -> 10
        else -> 0
    }

    val baseAmount = orderAmount.toDoubleOrNull() ?: 0.0
    val discountMultiplier = (100 - selectedDiscount) / 100.0
    val tipMultiplier = tipPercentage.toInt() / 100.0

    val finalTotal = (baseAmount * discountMultiplier) + (baseAmount * tipMultiplier)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 35.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(text = "Сумма заказа: ", fontSize = 16.sp)
            OutlinedTextField(
                value = orderAmount,
                onValueChange = {orderAmount = it},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(200.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFFFBFCC),
                    focusedContainerColor = Color(0xFFFFBFCC)
                )
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(text = "Количество блюд: ", fontSize = 16.sp)
            OutlinedTextField(
                value = dishesCountText,
                onValueChange = {dishesCountText = it},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(80.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFFFBFCC),
                    focusedContainerColor = Color(0xFFFFBFCC)
                )
            )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Чаевые", fontSize = 16.sp)
            Slider(
                value = tipPercentage,
                onValueChange = {tipPercentage = it},
                valueRange = 0f..25f,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "0", fontSize = 16.sp)
                Text(text = "${tipPercentage.toInt()}%", fontSize = 14.sp, color = Color.Gray)
                Text(text = "25", fontSize = 16.sp)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "Скидка:",
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 6.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Row(
                modifier = Modifier
                    .weight(1f)
                    .selectableGroup(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val discountOptions = listOf(3, 5, 7, 10)
                discountOptions.forEach { discount ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        RadioButton(
                            selected = (selectedDiscount == discount),
                            onClick = null
                        )
                        Text(text = "$discount%", fontSize = 16.sp)
                    }
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = Color.LightGray,
            thickness = 1.dp
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Итого к оплате:",
                fontSize = 18.sp,
                color = Color.Gray
            )

            Text(
                text = String.format("%.2f ₽", finalTotal),
                fontSize = 32.sp,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            // Краткая детализация расчета для удобства
            if (baseAmount > 0) {
                Text(
                    text = "Скидка: $selectedDiscount% | Чаевые: ${tipPercentage.toInt()}%",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TipCalcTheme {
       CalculatorScreen()
    }
}