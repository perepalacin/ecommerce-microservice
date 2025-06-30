# 🛍️ E-Commerce Site Showcase

This repo contains an e-commerce platform using **Astro** for the frontend and microservices with **Spring Boot** for the backend. Below is an overview of the system's design and features.

## 📦 Technologies

- **Frontend**: Astro
- **Backend**: Spring Boot
- **API Gateway**: Spring Cloud Gateway
- **Databases**: MySQL
- **Authentication**: Keycloak, Spring Security
- **Message Queue**: Kafka
- **Containerization**: Docker

## 🛠️ System Architecture

### Microservices Overview

1. **API Gateway**: Routes requests to various services.
2. **Product Service**: Handles product information, mainly focused on watches. Public access with some role-based restrictions. 
3. **Auth Service**: Manages user authentication with Spring Security and Keycloak.
4. **Cart Service**: Separate from other services; uses HTTP requests for product info.
5. **Order Service**: Processes orders, uses HTTP requests to fetch cart details.
6. **Notification Service**: Uses Kafka to send emails for account registration and stock notifications.

### Databases

- **Product, Cart and Orders**: Use separate MySQL databases to maintain independence in the microservices. 
- **Communication**: Services interact through HTTP requests, except for notifications handled by Kafka.

### Docker Setup

- **Docker Compose**: Configures MySQL, Keycloak, and other dependencies required for the application.

## 🚀 Features

- **Product Browsing**: Public access to explore products.
- **User Authentication**: Sign-up and verification through emails via Keycloak.
- **Cart Functionality**: Add items to a cart with HTTP-based interactions.
- **Order Processing**: Orders are processed with notifications sent out using Kafka.
- **Stock Alerts**: Users can subscribe to notifications for out-of-stock items.

## 🔍 Filtering and Ordering

- **Dynamic Filters**: URL encoded filters for brand and size.
- **Pagination**: Options for results per page are available, adapting dynamically.

## 🖥️ User Interface

- Basic UI allows product browsing, filtering, cart management, and order processing.
- Login features implemented with UI elements for user account management.

## 🚧 Future Improvements

- Complete the checkout process on the UI.
- Explore more features using Astro.
- Enhance UI for better user experience.

## 🏃 Running the Project

1. **Docker Setup**: Use the provided `docker-compose.yml` to run MySQL, Keycloak, and other services.
2. **Frontend**: Run Astro on port 4321.
3. **API Gateway**: Runs on port 8080, directing traffic to services.
4. **Run all Microservices**: Run the rest of microservices, make sure they all run in the port specified at their env.properties file.
5. **Database**: Set up MySQL databases for Product and Cart services.

Explore this robust e-commerce platform showcasing a microservices architecture for seamless scalability and maintenance. Thank you for your interest!

## 🍿 Showcase

Watch a demonstration here: [Video](https://youtu.be/MVkPzm404Bo)
![Alt Text - Whatsapp Clone Architecture image](https://github.com/perepalacin/ecommerce-microservice/blob/main/microservices-excalidraw.svg)