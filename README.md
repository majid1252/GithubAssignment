
<p float="left">
  <img src="app/src/main/1.jpg" width="300" />
  <img src="app/src/main/2.jpg" width="300" />
  <img src="app/src/main/3.jpg" width="300" />
</p>


# GitHub Users Android Application

This GitHub Users Android Application is a comprehensive solution designed for fetching and displaying a list of GitHub users and their profile information. Developed by a professional developer, this application employs best practices in Android development and demonstrates expertise in utilizing modern libraries and frameworks.

## Features

1. Retrieves a list of GitHub users and displays it in a user-friendly and efficient manner.
2. Presents the profile information of a selected user in a separate, dedicated view.
3. Implements local search functionality for the users list, making it convenient to find specific users.
4. Supports offline data access and automatically reloads data when the internet connection is restored.
5. Utilizes Room library for local data storage and persistence.
6. Adheres to the MVVM architecture, utilizing Kotlin Coroutines for clean and maintainable code.
7. Handles screen rotation seamlessly.
8. Includes unit tests to ensure data processing logic and Room models are accurate and reliable.

## Requirements

- Kotlin 1.6.x
- Android Studio
- Room for data persistence
- ConstraintLayout and/or Jetpack Compose for the user interface
- Android Paging Library for efficient pagination

## Installation

1. Clone the repository.
2. Open the project in Android Studio.
3. Run the app on an emulator or a physical device.

## ⚠️ GitHub API Key and Rate Limiting

To prevent rate-limiting issues and to ensure seamless performance and successful data fetch, you need to obtain a GitHub API Key. Follow the steps below:

    Go to GitHub Developer Settings
    Click on "New OAuth App" or "New GitHub App"
    Fill in the required fields and create your app
    Copy the generated API Key
    Open the project in Android Studio
    Replace the value of {Constants.GITHUB_API_KEY} and run the project

With the GitHub API Key in place, you can now enjoy a higher rate limit and a better user experience.

> :warning: **Note:** Any github token in the project has been automatically invalidated by Github 

## Usage

1. Launch the application and view the fetched GitHub users list.
2. Scroll through the list to load additional users.
3. Select a user to view their profile information.
4. Use the search functionality to filter users by username or note.

## Dependencies

This project relies on the following dependencies:

- AndroidX Core and AppCompat
- ConstraintLayout
- CoordinatorLayout
- Room for data persistence
- Coroutines
- LiveData and ViewModel
- Flow
- Paging Library
- Glide
- Material 3 Components
- Retrofit
- OkHttp
- Gson
- Timber
- Hilt
- Kotlin Serialization
- LeakCanary for debugging memory leaks
- JUnit, Mockito, and Espresso for a robust test suite

For a comprehensive list of dependencies and their respective versions, please refer to the [build.gradle](app/build.gradle) file.

## Acknowledgments

* [GitHub API](https://api.github.com) for providing the user data that powers this application.
* [Android Jetpack](https://developer.android.com/jetpack) for its extensive libraries and components that facilitate app development.
* [Wireframe](wireframe.png) provided as a reference for the application's user interface design.
