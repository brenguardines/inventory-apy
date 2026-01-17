# Inventory Management – REST API with Spring Boot

## Description

RESTful API built with Java and Spring Boot for managing products and categories through full CRUD operations.
It uses an in-memory H2 database, input validation, global exception handling, and a clean layered architecture
(controllers, services, repositories, DTOs, and entities).

---

## Tech Stack

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Spring Validation (Jakarta Validation)
- H2 Database (in-memory)
- Maven
- JUnit 5

---

## Setup Instructions

### 1. Clone the repository
```bash
git clone git@github.com:brenguardines/inventory-apy.git
cd inventory-apy
(If you prefer HTTPS: https://github.com/brenguardines/inventory-apy.git)
```


### 2. Run the project

From terminal or IntelliJ IDEA you can run:

```bash
mvn spring-boot:run
```
Or run the main class directly:
`InventoryApiApplication`.

### 3. H2 Database Console

Once the application is running, you can access the in-memory database:

- H2 Console: `http://localhost:8080/h2-console`

Use the following configuration:

- JDBC URL: `jdbc:h2:mem:inventorydb`

- User Name: `sa`

Password: (empty)

## API Endpoints
📦 Products

- POST `/products` – Create a product
  Example request body:
```json
{
  "name": "Mechanical Keyboard",
  "description": "Compact mechanical keyboard with brown switches.",
  "price": 120.50,
  "stock": 15,
  "categoryId": 1
}
```

- GET `/products` – Get all products (optional filter by category)

    - GET `/products`

    - GET `/products?category=Electronics`

- GET `/products/{id}` – Get product by ID

- PUT `/products/{id}` – Update a product
```json
{
  "name": "Mechanical Keyboard",
  "description": "Updated description for the product.",
  "price": 115.00,
  "stock": 20,
  "categoryId": 1
}
```


- DELETE /products/{id} – Delete a product

🏷️ Categories

- POST /categories – Create a category
```json
{
  "name": "Electronics",
  "description": "Devices, accessories and components."
}

```

- GET /categories – Get all categories

- GET /categories/{id} – Get category by ID

- PUT /categories/{id} – Update a category
```json
{
  "name": "Office Supplies",
  "description": "Products used in office environments."
}


```

- DELETE /categories/{id} – Delete a category

## Estructura general del proyecto

```
📦 inventory-apy
 ┣ 📂src
 ┃ ┣ 📂main
 ┃ ┃ ┣ 📂java
 ┃ ┃ ┃ ┗ 📂com.brenda.inventory
 ┃ ┃ ┃   ┣ 📂controllers
 ┃ ┃ ┃   ┃ ┣ 📜 CategoryController.java
 ┃ ┃ ┃   ┃ ┗ 📜 ProductController.java
 ┃ ┃ ┃   ┣ 📂dto
 ┃ ┃ ┃   ┃ ┣ 📜 CategoryCreateRequest.java
 ┃ ┃ ┃   ┃ ┣ 📜 CategoryResponse.java
 ┃ ┃ ┃   ┃ ┣ 📜 CategoryUpdateRequest.java
 ┃ ┃ ┃   ┃ ┣ 📜 ProductCreateRequest.java
 ┃ ┃ ┃   ┃ ┣ 📜 ProductResponse.java
 ┃ ┃ ┃   ┃ ┗ 📜 ProductUpdateRequest.java
 ┃ ┃ ┃   ┣ 📂entity
 ┃ ┃ ┃   ┃ ┣ 📜 Category.java
 ┃ ┃ ┃   ┃ ┗ 📜 Product.java
 ┃ ┃ ┃   ┣ 📂exceptions
 ┃ ┃ ┃   ┃ ┣ 📜 GlobalExceptionHandler.java
 ┃ ┃ ┃   ┃ ┗ 📜 ResourceNotFoundException.java
 ┃ ┃ ┃   ┣ 📂repositories
 ┃ ┃ ┃   ┃ ┣ 📜 CategoryRepository.java
 ┃ ┃ ┃   ┃ ┗ 📜 ProductRepository.java
 ┃ ┃ ┃   ┣ 📂services
 ┃ ┃ ┃   ┃ ┣ 📜 CategoryService.java
 ┃ ┃ ┃   ┃ ┗ 📜 ProductService.java
 ┃ ┃ ┃   ┗ 📜 InventoryApiApplication.java
 ┃ ┃ ┗ 📂resources
 ┃ ┃     ┗ 📜 application.properties
 ┃ ┗ 📂test
 ┃   ┗ 📂java
 ┃     ┗ 📂com.brenda.inventory
 ┃       ┣ 📂controllers
 ┃       ┃ ┣ 📜 CategoryControllerTest.java
 ┃       ┃ ┗ 📜 ProductControllerTest.java
 ┃       ┣ 📂repositories
 ┃       ┃ ┣ 📜 CategoryRepositoryTest.java
 ┃       ┃ ┗ 📜 ProductRepositoryTest.java
 ┃       ┗ 📜 InventoryApiApplicationTests.java
 ┣ 📜 pom.xml
 ```

## Running Tests
```bash
./mvnw test
```
If you get a permission error on Unix-based systems, run:
```bash
chmod +x mvnw
./mvnw test
```

👩‍💻 Developed by [Brenda Guardines](https://www.linkedin.com/in/brenda-guardines)