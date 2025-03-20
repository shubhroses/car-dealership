# Premium Auto Dealership Application

## Live Demo
[Premium Auto Dealership](https://car-dealership-production-a328.up.railway.app/dealership/)

## Project Overview
Premium Auto Dealership is a full-featured Spring Boot web application designed to showcase and manage a vehicle inventory. The application provides both customer-facing features for browsing vehicles and admin functionalities for managing the dealership operations.

## Features

### Customer Features
- **Vehicle Browsing**: Search and filter vehicles by make, model, and price range
- **Vehicle Details**: View comprehensive information about each vehicle
- **User Authentication**: Register and login to access personalized features
- **User Dashboard**: Access personal information and preferences
- **Contact Form**: Communicate directly with dealership staff

### Administrative Features
- **Admin Dashboard**: Centralized management interface
- **Inventory Management**: Add, update, and remove vehicles from inventory
- **User Management**: View and manage user accounts
- **Role-Based Access Control**: Different permission levels (Customer, Staff, Admin)
- **H2 Console Access**: Direct database management (development environment only)

## Technical Stack

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Database access and ORM
- **H2 Database**: In-memory database for development
- **Thymeleaf**: Server-side templating engine
- **Maven**: Dependency management and build

### Frontend
- **Bootstrap 5**: Responsive design framework
- **HTML5/CSS3**: Frontend structure and styling
- **JavaScript**: Client-side interactivity
- **Thymeleaf Fragments**: Reusable page components

### Deployment
- **Railway**: Cloud platform hosting
- **Git/GitHub**: Version control and source code management

## Project Structure
```
car-dealership/
├── src/
│   ├── main/
│   │   ├── java/com/dealership/
│   │   │   ├── config/               # Configuration classes
│   │   │   ├── controller/           # MVC controllers
│   │   │   ├── model/                # Entity classes
│   │   │   ├── repository/           # Data access interfaces
│   │   │   ├── security/             # Security configuration
│   │   │   ├── service/              # Business logic
│   │   │   └── CarDealershipApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/              # Stylesheets
│   │       │   ├── js/               # JavaScript files
│   │       │   └── images/           # Image assets
│   │       ├── templates/            # Thymeleaf templates
│   │       │   ├── admin/            # Admin page templates
│   │       │   ├── fragments/        # Reusable page components
│   │       │   └── *.html            # Page templates
│   │       └── application.properties # Application configuration
│   └── test/                         # Test classes
├── railway.json                      # Railway deployment configuration
├── system.properties                 # Java version specification
├── pom.xml                           # Maven dependencies
└── README.md                         # Project documentation
```

## Setup & Installation

### Prerequisites
- Java 17+
- Maven 3.6+
- Git

### Local Development
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/car-dealership.git
   cd car-dealership
   ```

2. Build the application:
   ```bash
   mvn clean package
   ```

3. Run the application:
   ```bash
   java -jar target/cardealership-0.0.1-SNAPSHOT.jar
   ```

4. Access the application:
   ```
   http://localhost:8080/dealership
   ```

5. Access the H2 Console (dev only):
   ```
   http://localhost:8080/dealership/h2-console
   ```
   - JDBC URL: `jdbc:h2:mem:dealershipdb`
   - Username: `sa`
   - Password: `password`

### Default Credentials

#### Admin User
- Username: admin
- Password: admin

#### Customer User
- Username: customer
- Password: customer

### Deployment on Railway

1. Ensure you have the following files in your repository:
   - `railway.json` - Configuration for Railway deployment
   - `system.properties` - Java version specification

2. Push your code to GitHub.

3. Connect Railway to your GitHub repository:
   - Create a new project in Railway
   - Select "Deploy from GitHub repo"
   - Configure the deployment settings:
     - Build Command: `mvn clean package -DskipTests`
     - Start Command: `java -Xmx512m -Xms256m -jar target/cardealership-0.0.1-SNAPSHOT.jar`

4. Set the following environment variables in Railway:
   - `PORT`: `8080`
   - `SERVER_CONTEXT_PATH`: `/dealership`
   - `SPRING_DATASOURCE_URL`: `jdbc:h2:mem:dealershipdb`
   - `SPRING_DATASOURCE_USERNAME`: `sa`
   - `SPRING_DATASOURCE_PASSWORD`: `password`
   - `ADMIN_USERNAME`: Your admin username
   - `ADMIN_PASSWORD`: Your admin password

5. Generate a domain in Railway's networking settings.

6. Access your deployed application at:
   ```
   https://car-dealership-production-a328.up.railway.app/dealership
   ```

## Key Components

### Security Configuration
- Role-based access control with three user roles:
  - `ROLE_CUSTOMER`: Regular users
  - `ROLE_STAFF`: Dealership staff
  - `ROLE_ADMIN`: Administrators with full access
- Secured endpoints with appropriate authorization
- Login/logout functionality
- Password encryption

### Database
- H2 in-memory database (development)
- JPA entity relationships:
  - User and Role entities
  - Vehicle information
  - Customer data

### Controllers
- HomeController: Main pages
- VehicleController: Vehicle inventory management
- UserController: User account management
- AdminController: Administrative functions

### Templating
- Thymeleaf templates with HTML5
- Reusable fragments for consistent components
- Responsive design with Bootstrap

## Troubleshooting

### Common Issues
1. **Memory Issues in Railway Deployment**
   - Solution: Added memory constraints in `railway.json` file
   - Set JVM options: `-Xmx512m -Xms256m`

2. **Image Display Problems**
   - Solution: Corrected paths in templates for profile pictures and logos
   - Updated Thymeleaf syntax to use proper context paths

3. **Database Access**
   - Solution: Configured H2 console access
   - Updated database connection parameters

4. **Role-Based Access**
   - Solution: Added proper security configuration
   - Created mechanisms to add admin roles to users

## Future Enhancements
- Persistent database for production
- Image upload functionality
- Vehicle reservation system
- Payment processing integration
- Analytics dashboard

## Acknowledgements
- Spring Boot framework
- Bootstrap for responsive design
- Railway for cloud hosting
- H2 Database for development simplicity

---

© 2023 Premium Auto Dealership. All rights reserved. 