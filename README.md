# PocketExpress
[![Ask DeepWiki](https://devin.ai/assets/askdeepwiki.png)](https://deepwiki.com/SamuelAlac/PocketExpress)

PocketExpress is a native Android application that functions as a straightforward Point of Sale (POS) and inventory management system. It's built entirely with Java and utilizes a local SQLite database to manage all data, making it a self-contained solution for small-scale retail operations without needing an internet connection.

## Features

- **Staff Management:** Secure registration and login for staff members. The app tracks which staff member processes each order.
- **Inventory Control:** Easily add, view, update, and delete inventory items. Each item includes a name, description, stock quantity, price, and a custom image uploaded from the device.
- **Point of Sale (POS):** A clear, grid-based interface for browsing available products and adding them to a new order.
- **Shopping Cart:** A dedicated screen to review items in the current order, update quantities, or remove items before finalizing the purchase.
- **Transaction Processing:** Automatically calculates the total price. The staff can input the cash received from the customer to compute the correct change.
- **Order History:** Access a comprehensive history of all completed transactions. Users can select a past order to view the specific items, quantities, and prices included in that sale.
- **Local Data Persistence:** All data, including staff accounts, inventory, and order history, is stored locally on the device in an SQLite database.

## Technical Overview

- **Language:** Java
- **Database:** SQLite is used for all local data storage. The database schema and all CRUD (Create, Read, Update, Delete) operations are managed by the `DatabaseHelper` class.
- **Architecture:** The application is structured around standard Android Activities, with each screen representing a distinct function (e.g., login, inventory management, shopping cart). Data is displayed in lists using `RecyclerView` and custom `Adapters` for efficient rendering.

### Core Components

- **`entity` package:** Contains the data model classes (POJOs) that represent the database tables:
  - `Staff`: Represents a user account.
  - `Item`: Represents a product in the inventory.
  - `Order`: Represents a completed transaction.
  - `Cart` & `CartItem`: Represent the items within an active or completed order.
- **`helper` package:**
  - `DatabaseHelper`: A robust class that handles the creation of the SQLite database, schema definition, and all data manipulation queries.
  - `Adapters` (`ItemAdapter`, `SaleAdapter`, `CartItemsAdapter`, etc.): These classes bridge the data from the database to the `RecyclerViews` that display it on the UI.
- **Activities:** The application's user flow is managed through a series of activities:
  - `LoginActivity` & `RegisterActivity`: Handle staff authentication.
  - `MainActivity`: The main dashboard for navigating to different features.
  - `ManageItems`, `CreateItems`, `UpdateItems`: The complete set of screens for inventory management.
  - `OrderItems` & `OrderItemInfo`: Screens for creating a new sale.
  - `ShoppingCart`: The checkout screen for finalizing a transaction.
  - `OrderHistory` & `OrderedItemHistory`: Screens for reviewing past sales.

## Getting Started

To get the project up and running on your local machine, follow these steps:

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/SamuelAlac/PocketExpress.git
    ```

2.  **Open in Android Studio:**
    - Launch Android Studio.
    - Select `File` > `Open...` and navigate to the cloned `PocketExpress` directory.

3.  **Sync and Build:**
    - Wait for Android Studio to automatically sync the Gradle project and download the required dependencies.
    - Build the project by selecting `Build` > `Make Project` from the top menu.

4.  **Run the App:**
    - Select a target device (an Android emulator or a physical device with API level 31 or higher).
    - Click the `Run` button (▶) to build and install the application on your target device.
