# Car Dealership Web Application

A full-featured web application for car dealership management with dual customer and admin interfaces.

## Features

### Customer Portal
- Browse available vehicles with advanced filtering
- View detailed vehicle information
- Submit inquiries about vehicles
- User registration and account management
- Track inquiry status

### Admin Portal
- Manage vehicle inventory (add, edit, remove vehicles)
- Respond to customer inquiries
- Dashboard with key metrics
- User management

## Technology Stack

- **Backend**: Java with Spring Boot
- **Frontend**: Thymeleaf templates with Bootstrap
- **Database**: H2 Database (in-memory for development)
- **Security**: Spring Security for authentication and authorization
- **Build Tool**: Maven

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. Clone the repository
```
git clone https://github.com/yourusername/car-dealership.git
cd car-dealership
```

2. Build the application
```
mvn clean install
```

3. Run the application
```
mvn spring-boot:run
```

4. Access the application
   - Customer Portal: http://localhost:8080/dealership/
   - Admin Portal: http://localhost:8080/dealership/admin/dashboard

### Default Credentials

#### Admin User
- Username: admin
- Password: admin

#### Customer User
- Username: customer
- Password: customer

## Project Structure

- `src/main/java/com/dealership/cardealership`: Java source files
  - `controller`: MVC controllers
  - `model`: Domain entities
  - `repository`: Data access interfaces
  - `service`: Business logic
  - `config`: Configuration classes
- `src/main/resources`: Configuration and static resources
  - `templates`: Thymeleaf templates
  - `static`: CSS, JavaScript, images
  - `application.properties`: Application configuration

## Development Notes

- H2 Console is enabled at: http://localhost:8080/dealership/h2-console
- The application automatically initializes with sample data on startup 