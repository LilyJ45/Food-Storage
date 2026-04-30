package com.example.foodstorage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


@Composable
fun ListAllScreen(viewModel: FoodViewModel) {
    val items by viewModel.allItems.collectAsState()
    GenericListScreen(title = "All Items", items = items, viewModel = viewModel) { item, now, startEat ->
        val status = when {
            now > item.expirationDateMillis -> "Expired"
            now >= startEat -> "Eat"
            else -> "Store"
        }
        Text("Status: $status", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun StorageListScreen(viewModel: FoodViewModel) {
    val items by viewModel.allItems.collectAsState()
    val now = System.currentTimeMillis()
    val storageItems = items.filter { item ->
        val startEat = item.expirationDateMillis - (item.quantity * item.timeToEat1UnitDays * 86400000L)
        now < startEat && now <= item.expirationDateMillis
    }
    GenericListScreen(title = "Storage List", items = storageItems, viewModel = viewModel) { item, _, startEat ->
        Text("Start Eating: ${DateUtils.formatMillisToDate(startEat)}", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun EatListScreen(viewModel: FoodViewModel) {
    val items by viewModel.allItems.collectAsState()
    val now = System.currentTimeMillis()
    val eatItems = items.filter { item ->
        val startEat = item.expirationDateMillis - (item.quantity * item.timeToEat1UnitDays * 86400000L)
        now in startEat..item.expirationDateMillis
    }
    GenericListScreen(title = "Eat List", items = eatItems, viewModel = viewModel)
}

@Composable
fun ExpiredListScreen(viewModel: FoodViewModel) {
    val items by viewModel.allItems.collectAsState()
    val now = System.currentTimeMillis()
    val expiredItems = items.filter { it.expirationDateMillis < now }
    GenericListScreen(title = "Expired List", items = expiredItems, viewModel = viewModel)
}


@Composable
fun GenericListScreen(
    title: String,
    items: List<FoodItem>,
    viewModel: FoodViewModel,
    extraContent: @Composable (FoodItem, Long, Long) -> Unit = { _, _, _ -> }
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredItems = items.filter { it.name.contains(searchQuery, ignoreCase = true) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search by item name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(title, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(filteredItems) { item ->
                FoodItemCard(item, viewModel, extraContent)
            }
        }
    }
}

@Composable
fun FoodItemCard(
    item: FoodItem,
    viewModel: FoodViewModel,
    extraContent: @Composable (FoodItem, Long, Long) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var editQty by remember { mutableStateOf(item.quantity.toString()) }
    val now = System.currentTimeMillis()
    val startEat = item.expirationDateMillis - (item.quantity * item.timeToEat1UnitDays * 86400000L)

    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, style = MaterialTheme.typography.titleMedium)
                if (item.grouping.isNotBlank()) Text("Group: ${item.grouping}", style = MaterialTheme.typography.bodySmall)
                Text("Exp: ${DateUtils.formatMillisToDate(item.expirationDateMillis)}", style = MaterialTheme.typography.bodySmall)

                if (isEditing) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(value = editQty, onValueChange = { editQty = it }, label = { Text("Qty") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.width(80.dp))
                        TextButton(onClick = {
                            val newQ = editQty.toIntOrNull() ?: item.quantity
                            viewModel.updateItemQuantity(item, newQ)
                            isEditing = false
                        }) { Text("Save") }
                    }
                } else {
                    Text("Qty: ${item.quantity}", style = MaterialTheme.typography.bodyMedium)
                }

                extraContent(item, now, startEat)
            }

            IconButton(onClick = { isEditing = !isEditing }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = { viewModel.deleteItem(item) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}