package com.example.foodstorage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FoodViewModel(
    private val repository: FoodRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private var currentUserEmail = sessionManager.getCurrentEmail()

    private val _allItems = MutableStateFlow<List<FoodItem>>(emptyList())
    val allItems: StateFlow<List<FoodItem>> = _allItems.asStateFlow()

    private var databaseJob: Job? = null

    init {
        listenToDatabaseForUser(currentUserEmail)
    }

    private fun listenToDatabaseForUser(email: String) {
        databaseJob?.cancel()

        databaseJob = viewModelScope.launch {
            repository.getItemsForUser(email).collect { items ->
                _allItems.value = items
            }
        }
    }

    fun addItem(name: String, quantity: Int, grouping: String, expiration: Long, timeToEat: Int) {
        viewModelScope.launch {
            val newItem = FoodItem(
                name = name,
                quantity = quantity,
                grouping = grouping,
                expirationDateMillis = expiration,
                timeToEat1UnitDays = timeToEat,
                ownerEmail = currentUserEmail
            )
            repository.insertItem(newItem)
        }
    }

    fun updateItemQuantity(item: FoodItem, newQuantity: Int) {
        viewModelScope.launch {
            repository.insertItem(item.copy(quantity = newQuantity))
        }
    }

    fun deleteItem(item: FoodItem) {
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }

    fun registerUser(email: String, pass: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = User(email, pass)
            val rowId = repository.insertUser(user)

            if (rowId != -1L) {
                currentUserEmail = email
                listenToDatabaseForUser(email)
            }
            onResult(rowId != -1L)
        }
    }

    fun loginUser(email: String, pass: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = repository.authenticateUser(email, pass)

            if (user != null) {
                currentUserEmail = email
                listenToDatabaseForUser(email)
            }
            onResult(user != null)
        }
    }

    fun changeUserEmail(newEmail: String) {
        viewModelScope.launch {
            repository.updateEmail(currentUserEmail, newEmail)

            currentUserEmail = newEmail
            listenToDatabaseForUser(newEmail)
        }
    }

    fun deleteUserAccount() {
        viewModelScope.launch {
            repository.deleteUser(currentUserEmail)
            sessionManager.logoutUser()

            currentUserEmail = ""
            databaseJob?.cancel()
            _allItems.value = emptyList()
        }
    }

    fun changeUserPassword(newPassword: String) {
        viewModelScope.launch {
            repository.updatePassword(currentUserEmail, newPassword)
        }
    }
}