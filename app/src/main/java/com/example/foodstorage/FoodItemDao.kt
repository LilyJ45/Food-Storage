package com.example.foodstorage

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * FROM food_items WHERE ownerEmail = :email ORDER BY expirationDateMillis ASC")
    fun getItemsForUser(email: String): Flow<List<FoodItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: FoodItem)

    @Delete
    suspend fun deleteItem(item: FoodItem)
}