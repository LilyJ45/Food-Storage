package com.example.foodstorage

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [FoodItem::class, User::class], version = 1, exportSchema = false)
abstract class FoodDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun userDao(): UserDao
}