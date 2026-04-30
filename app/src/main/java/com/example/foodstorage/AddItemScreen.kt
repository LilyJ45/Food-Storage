package com.example.foodstorage

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AddItemScreen(viewModel: FoodViewModel) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var group by remember { mutableStateOf("") }
    var expiration by remember { mutableStateOf("") }
    var timeToEat by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Add New Food Item", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Item Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = quantity, onValueChange = { quantity = it }, label = { Text("Quantity") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = group, onValueChange = { group = it }, label = { Text("Optional Grouping") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = expiration, onValueChange = { expiration = it }, label = { Text("Expiration Date (MM/dd/yyyy)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = timeToEat, onValueChange = { timeToEat = it }, label = { Text("Days to eat 1 unit") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val qtyInt = quantity.toIntOrNull() ?: 1
                val timeInt = timeToEat.toIntOrNull() ?: 1
                val expMillis = DateUtils.parseDateToMillis(expiration)

                if (name.isNotBlank() && expMillis > 0L) {
                    viewModel.addItem(name, qtyInt, group, expMillis, timeInt)
                    Toast.makeText(context, "Item Added!", Toast.LENGTH_SHORT).show()
                    name = ""; quantity = ""; group = ""; expiration = ""; timeToEat = ""
                } else {
                    Toast.makeText(context, "Please enter a valid name and date", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Item")
        }
    }
}