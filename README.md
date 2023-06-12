# Fetch Data Android App

This Android app fetches data from a JSON API and displays it in a RecyclerView grouped by ListId. The app allows you to filter the items based on the selected ListId using a dropdown list.

![App Demo](demo.gif)

## Features

- Fetches data from a remote JSON API
- Filters out items with blank or null names
- Groups the items by ListId
- Sorts the items first by ListId and then by the numeric part of the name
- Displays the items in a RecyclerView
- Provides a dropdown list to select the desired ListId for filtering
- Updates the RecyclerView based on the selected ListId

## Libraries Used

The following libraries were used in this project:

- Volley
- Gson
- RecyclerView
- Spinner

## Getting Started

To get started with the app, follow these steps:

1. Clone the repository.
   ```shell
   git clone <repository-url>

2. Open the project in Android Studio.

3. Build and run the app on an Android device or emulator.
