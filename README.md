# Train-Booking-System


# Train System

Train System is a Java-based application that allows users to book train trips, manage bookings, and provides administrative functionalities for managing trains and trips.

## Features

- **User Registration and Authentication:** Users can create an account and log in to access the system.
- **Booking Management:** Users can search for available train trips, book a trip, and cancel their bookings.
- **Trip Information:** Users can view details about available train trips, including departure and arrival times, fares, and seat availability.
- **User Profile:** Users can update their personal information, such as name, contact details, and password.
- **Admin Panel:** Admin users have additional functionalities for managing trains and trips.
  - **Train Management:** Admin users can add new trains, delete trains, and view the list of all trains.
  - **Trip Management:** Admin users can add new trips, delete trips, and view the list of all trips.
  - **Application Management:** Admin can generate reports showing all the information about all the user

## Technologies Used

- **Java:** Programming language used for developing the application.
- **SQL Server:** Database system for storing user and trip information.
- **Java Swing:** GUI library used for building the graphical user interface.
- **JDBC:** Java Database Connectivity API for connecting and interacting with the SQL Server database.

## ScreenShoots
![Home Screen](https://github.com/OmarGaafar1/Train-Booking-System/assets/92587188/aa77672d-cb06-40f1-8bcc-08830dd41aa9)
![User-Admin-SignIn](https://github.com/OmarGaafar1/Train-Booking-System/assets/92587188/9a837fa5-e41b-4f95-93b4-604452443053)
![Register Form](https://github.com/OmarGaafar1/Train-Booking-System/assets/92587188/6b74a643-5971-47e4-a72a-1bf6cb198007)

![LoggedIn-User](https://github.com/OmarGaafar1/Train-Booking-System/assets/92587188/ac22bdea-9286-48b6-97b4-c107ba6d06d7)

![LoggedIn-Admin](https://github.com/OmarGaafar1/Train-Booking-System/assets/92587188/4e57cdfd-e4ff-4e93-9450-2566b8877771)
![Add Trip (Admin)](https://github.com/OmarGaafar1/Train-Booking-System/assets/92587188/e85682bc-230a-4506-9f0f-73bc1d6c941e)
![Add Train (Admin)](https://github.com/OmarGaafar1/Train-Booking-System/assets/92587188/99324e16-5971-4d02-bebd-25e928b5e4c8)
![BookTrip (User) 1](https://github.com/OmarGaafar1/Train-Booking-System/assets/92587188/594c8a45-b68a-4d65-ab1e-b8d56c5cb5ee)
![BookTrip (User) 2](https://github.com/OmarGaafar1/Train-Booking-System/assets/92587188/0527714d-2e80-4f6b-8efc-7bbe88df90c5)
![Delete Trip and Delete Train (Admin)](https://github.com/OmarGaafar1/Train-Booking-System/assets/92587188/3d579cb8-f9f5-4424-b744-dd600db71be1)




## Prerequisites

- Java Development Kit (JDK) version XYZ or higher.
- SQL Server database with the appropriate schema and tables set up.
- Additional libraries or dependencies that need to be installed or configured.

## Installation and Usage

1. Set up the SQL Server database and ensure it is running.

2. Update the database connection settings in the configuration file (config.properties) to match your SQL Server configuration.

3. Build the project using your preferred build tool (e.g., Maven, Gradle) or compile the Java source files manually.

4. Run the application.

5. Follow the on-screen instructions to use the Train System application. Sign in as a user or admin to access the respective   functionalities.



## SQLJDBC configuration

- Add the jar file in setup folder attached above as an dependency for java projcet

- Open your environment variables and locate your user path and add the .dll file attached in setup folder

## Database Setup

- Create a new SQL Server database for the Train System project.

- Import the database schema and initial data by following these steps:

1. Locate the TrainSystem.sql file in the setup folder.
2. Open SQL Server Management Studio or any other suitable SQL client.
3. Connect to your SQL Server instance and select the newly created Train System database.
4. Open the TrainSystem.sql file and execute its contents to create the necessary tables, views, and initial data.
5. Update the database connection settings in the configuration file (config.properties) to match your SQL Server configuration (host, port, database name, username, password).

- Make sure the SQL Server instance is running before launching the Train System application.

- By following these steps, you will have the necessary database structure and initial data set up for the Train System project. Users can then interact with the application and perform various operations related to train booking, trip management, and user administration.


## Configuration

- To configure the Train System application, please follow these steps:

1. Locate the database.txt file in the project directory.

2. Open the file using a text editor.

3. Update the first line of the file with the server name of your SQL Server instance. Replace it with the appropriate server name.

4. Update the second line of the file with the database name for the Train System project. Replace it with the actual database name.

Example database.txt file:
  ```bash
localhost
TrainSystemDB
```
5 Save the file after making the necessary updates.

- By following these steps, you will ensure that the Train System application can read the correct server name and database name from the database.txt file when establishing a connection to the SQL Server.
