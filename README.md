# 🍽️ SmartDine – Restaurant Management System

SmartDine is a full-stack restaurant management application designed to simplify online food ordering and restaurant operations. It provides a backend REST API built with Java and Spring Boot, with MySQL used for data storage.

## 📌 Project Overview

SmartDine allows customers to browse menu items, place food orders, and track their order status. The project is designed to provide a simple and user-friendly restaurant ordering experience.

## ✨ Features

* **Menu Management:** Retrieve restaurant menu items through REST APIs.
* **Food Ordering:** Place food orders and manage order details.
* **Order Tracking:** Track order status through API endpoints.
* **Database Integration:** Store menu and order information in MySQL.
* **RESTful APIs:** Communicate with the frontend through HTTP requests.
* **Environment-Based Configuration:** Configure database credentials using environment variables.

## 🛠️ Technologies Used

| Technology            | Purpose                                 |
| --------------------- | --------------------------------------- |
| Java 17               | Backend programming language            |
| Spring Boot           | Backend application framework           |
| Spring Data JPA       | Database access and ORM                 |
| MySQL                 | Relational database                     |
| Maven                 | Build and dependency management         |
| HTML, CSS, JavaScript | Frontend technologies                   |
| Git and GitHub        | Version control and source code hosting |

## 📂 Project Structure

```text
SmartDine-Backend/
├── .mvn/
├── src/
│   ├── main/
│   │   ├── java/com/smartdine/restaurant/
│   │   │   ├── controller/
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── RestaurantApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── .gitignore
├── mvnw
├── mvnw.cmd
└── pom.xml
```

## ⚙️ Prerequisites

Install the following before running the backend:

* Java JDK 17 or compatible version
* Maven Wrapper (included in the project)
* MySQL database or a compatible hosted MySQL service
* Git (optional, for cloning the repository)

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/vangapallibhavya-28/SmartDine-Backend.git
cd SmartDine-Backend
```

### 2. Configure Database Environment Variables

The application uses environment variables for database configuration.

Set the following variables according to your database connection:

| Variable      | Description                   |
| ------------- | ----------------------------- |
| `DB_URL`      | JDBC connection URL for MySQL |
| `DB_USERNAME` | MySQL database username       |
| `DB_PASSWORD` | MySQL database password       |

Example JDBC URL for a local MySQL database:

```text
jdbc:mysql://localhost:3306/smartdine
```

Configure these variables in your terminal or deployment platform. **Do not commit database passwords or other secrets to GitHub.**

### 3. Build the Application

On Windows PowerShell:

```powershell
.\mvnw.cmd clean package -DskipTests
```

On Linux or macOS:

```bash
chmod +x mvnw
./mvnw clean package -DskipTests
```

### 4. Run the Application

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

On Linux or macOS:

```bash
./mvnw spring-boot:run
```

The backend uses port `8080` by default unless configured otherwise.

Once the application starts, open:

```text
http://localhost:8080
```

## 🔌 API Overview

The backend provides REST endpoints for menu items, food orders, and order tracking.

| Feature                 | Endpoint                            |
| ----------------------- | ----------------------------------- |
| Retrieve menu           | `/api/menu`                         |
| Create an order         | `/api/orders`                       |
| Order tracking          | `/api/orders/{id}/tracking`         |
| Advance tracking status | `/api/orders/{id}/tracking/advance` |

The exact HTTP methods and request formats depend on the controller implementation.

## 🗄️ Database

SmartDine uses MySQL to store application data. Spring Data JPA handles database operations, and Hibernate can automatically update the database schema when configured to do so.

Database connection details should be supplied through environment variables rather than hard-coded credentials.

## 🌐 Deployment

The backend can be deployed to a Java-compatible hosting platform such as Render and connected to a hosted MySQL database such as Aiven.

The frontend can be hosted separately and configured to communicate with the deployed backend API.

## 🔒 Security Notes

* Keep database credentials private.
* Store production configuration in environment variables.
* Configure CORS appropriately when the frontend and backend use different domains.
* Use HTTPS in production.
* Review API validation and authorization before exposing the application publicly.

## 🔮 Future Enhancements

* Customer registration and login
* Online payment integration
* Restaurant administration dashboard
* Menu and inventory management
* Reservation and scheduled pre-order management
* Sales reports and analytics
* Email or SMS order notifications

## 👩‍💻 Author

**SmartDine Project**

GitHub: [vangapallibhavya-28](https://github.com/vangapallibhavya-28)

## 📄 License

This project is intended for educational and development purposes. Add a license file if you intend to distribute or reuse the project under specific terms.
