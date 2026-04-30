package com.example.foodstorage

import kotlinx.coroutines.flow.Flow

class FoodRepository(
    private val foodDao: FoodDao,
    private val userDao: UserDao
) {

    // FOOD OPERATIONS

    fun getItemsForUser(email: String): Flow<List<FoodItem>> {
        return foodDao.getItemsForUser(email)
    }

    suspend fun insertItem(item: FoodItem) {
        foodDao.insertItem(item)
    }

    suspend fun deleteItem(item: FoodItem) {
        foodDao.deleteItem(item)
    }

    // USER ACCOUNT OPERATIONS

    suspend fun insertUser(user: User): Long {
        return userDao.insertUser(user)
    }

    suspend fun authenticateUser(email: String, pass: String): User? {
        return userDao.authenticateUser(email, pass)
    }

    suspend fun updateEmail(oldEmail: String, newEmail: String) {
        userDao.updateEmail(oldEmail, newEmail)
    }

    suspend fun updatePassword(email: String, newPass: String) {
        userDao.updatePassword(email, newPass)
    }

    suspend fun deleteUser(email: String) {
        userDao.deleteUser(email)
    }
}