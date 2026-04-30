Food Storage Tracker

<img width="383" height="777" alt="image" src="https://github.com/user-attachments/assets/409ffe34-3de5-4528-ad98-5355227a207d" />


Project Summary

Food Storage Tracker is a modern Android application built to help individuals and households manage their food inventory, reduce waste, and track expiration dates. By providing a centralized place to log groceries, users can easily see what they have in stock, what needs to be consumed immediately, and what has already expired. The app uses a clean, intuitive interface to make kitchen management feel less like a chore and more like a streamlined habit.

This application is built with Jetpack Compose and follows the MVVM (Model-View-ViewModel) architectural pattern. It features a robust, multi-user authentication system powered by a Room SQLite database. Using relational data mapping with CASCADE operations, the app ensures that each user’s data remains private, secure, and perfectly synchronized, even when account details like emails or passwords are changed.

Key Features

Multi-User Authentication: Secure Login and Account Creation screens to keep your inventory private.
Dynamic Inventory Tracking: Add, update, and delete food items with ease.
Smart Categorization: Automatically filters food into "All," "Store," "Eat Soon," and "Expired" lists.
Account Management: Full control over user settings, including the ability to update credentials or wipe all data.
Reactive UI: A fluid bottom navigation system with custom icons for quick access to all app features.

Technical Stack

Language: Kotlin
UI Framework: Jetpack Compose
Database: Room (SQLite) with relational foreign keys.
Architecture: MVVM with Repository pattern.
Concurrency: Kotlin Coroutines and StateFlow for real-time data updates.
Navigation: Compose Navigation with a central NavHost.

How to Run the App
Clone this repository to your local machine.
Open the project in Android Studio (Ladybug or newer).
Ensure you have the Extended Material Icons library synced via Gradle.
Run the app on an emulator or physical device running API 26 (Oreo) or higher.
Create a new account to start tracking your food!
