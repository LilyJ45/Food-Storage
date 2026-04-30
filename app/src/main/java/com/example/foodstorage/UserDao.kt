package com.example.foodstorage

import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun authenticateUser(email: String, password: String): User?

    @Query("UPDATE users SET email = :newEmail WHERE email = :oldEmail")
    suspend fun updateEmail(oldEmail: String, newEmail: String)

    @Query("UPDATE users SET password = :newPassword WHERE email = :email")
    suspend fun updatePassword(email: String, newPassword: String)

    @Query("DELETE FROM users WHERE email = :email")
    suspend fun deleteUser(email: String)
}