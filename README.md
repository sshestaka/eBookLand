![Online-Book_Shop.PNG](Images%2FOnline-Book_Shop.PNG)
# Online Book Shop

 **Introducing my Java-based "Online Book Shop" backend app, an efficient solution for book enthusiasts!
 Users experience seamless Registration and Login processes, accessing a well-organized catalog of Books sorted by Categories.
 Admins wield authority to effortlessly manage products and categories. 
 Users can explore books, either via a complete list or specific parameters. 
 The Shopping Cart feature enables easy addition, modification, and viewing of selected items. 
 Ordering is simplified, allowing users to seamlessly transition from cart to order. 
 Keeping users informed, order status monitoring ensures a transparent and satisfying shopping experience.** 

####  Welcome to a streamlined online book shopping journey with "Online Book Shop."

## Technologies and tools:
* JDK 17
* Apache Maven 3.11.0
* Spring Boot Starter Web 3.1.4
* Spring Security Test 3.1.4
* Spring Security Test 3.1.4 
* Spring Boot Starter Data JPA 3.1.4 
* Spring Boot Starter Validation 3.1.4
* MySQL Connector 8.x 
* Hibernate Validator 8.0.1.Final 
* H2 Database (for testing)
* Liquibase 4.20.0
* Tomcat 9.0.78
* jjwt-api 0.11.5 
* jjwt-impl 0.11.5 
* jjwt-jackson 0.11.5 
* Testcontainers 1.18.0 (for Docker-based testing)
* Testcontainers MySQL 
* Project Lombok 4.20.0 
* Lombok MapStruct Binding 0.2.0
* Springdoc-openapi-starter-webmvc-ui 2.2.0 
* Springdoc-openapi-ui 1.6.12 
* JSON Web Token (JWT)
* MapStruct 1.5.5.Final 
* Spring Boot Docker Compose 
* Maven Checkstyle Plugin 3.3.0 
* Maven Compiler Plugin 3.11.0 
* Testcontainers BOM (Bill of Materials) 1.18.0 

## Features:

**1. Authentication and Authorization**<br>
The Online Book Store is a robust Java web application adhering to fundamental principles of web 
development, including REST architecture. Ensuring secure access, the application utilizes Spring 
Security JWT Authentication & Authorization with a Bearer Token mechanism.<br>

**2. Models:**
*    Book
*    Category
*    User
*    Role
*    ShoppingCart
*    CartItem
*    Order
*    OrderItem

**3. Controller Level:**<br>
* **AuthorizationController:** Manages authentication and authorization.<br><br>
* **BookController:** Handles book-related operations.<br>
    - createBook - "Create a new book", hasRole('ADMIN'),
    - updateBook - "Update a book by id according to the parameters", hasRole('ADMIN'),
    - delete - "Delete a book by id", hasRole('ADMIN'),
    - getAll - "Get a list of all available books",
    - findById - "Get a book by id if it's available",
    - searchBooks - "Get a list of all books according to the parameters".<br><br>
* **CategoryController:** Manages book categories.<br>
    - createCategory - "Create a new category", hasRole('ADMIN'),
    - updateCategory - "Update a category by id according to the parameters", hasRole('ADMIN'),
    - deleteCategory - "Delete a category by id", hasRole('ADMIN'),
    - getAll - "Get a list of all available categories",
    - findById - "Get a category by id if it's available",
    - getBooksByCategoryId - "Get all books by category id if it's available".<br><br>
* **OrderController:** Manages user orders.<br>
    - createOrder - "Add new item to the shopping card",
    - getAll - "Get a list of all available user's orders",
    - updateOrderStatus - "Update the status of the order by id according to the parameters", hasRole('ADMIN'),
    - getAllOrderItemsByOrderId - "Get a list of all available order's items",
    - getOrderItemByOrderIdAndItemId - "Get item by itemID and orderId".<br><br>
* **ShoppingCartController:** Manages shopping cart operations.<br>
    - getShoppingCartByUserEmail - "Get user's shopping cart with all available items",
    - addItemToShoppingCart - "Add new item to the shopping card",
    - updateQuantityByItemId - "Update the quantity of the item in the shopping cart",
    - delete - "Remove the item from user's shopping cart by id".<br><br>
* **UserController:** Handles user-related operations.<br>
    - delete - "Delete a user by id", hasRole('ADMIN').

**4. Repository Level:**<br>
Repository: Handles repositories Interfaces and Classes for all models<br>

**5. Find Book by parameters**<br>
Provides find a book by parameters: "Author", "Price" and "Title"<br>

**6. DTO Level.**<br>
Handles different DTO models for all controllers.<br>

**7. Mapper Level.**<br>
Handles Mappers for all models.

**8. Security Level:**<br>
AuthenticationService: Implements user authentication.<br>
Token Logic: Handles the creation and validation of tokens.<br>

**9. Service Level:**<br>
 Service Classes: Implements business logic for all controllers.<br>

**10. Validation:**<br>
   Custom Annotations: Custom validation annotations.<br>

**11. Exceptions:**<br>
   CustomGlobalExceptionHandler: Handles custom exceptions globally.<br>

**12. Resources:**<br>
   Data Setup: YAML files for creating tables and inserting data.<br>
   Changelog: Liquibase changelog file (db.changelog-master.yaml).<br>

**13. Configuration:**<br>
    application.properties: Application-specific configurations.<br>

**14. Liquibase Configuration:**<br>
    liquibase.properties: Configuration for Liquibase.<br>

**15. Testing:**<br>
    Controller Tests: Unit tests for controller logic.<br>
    Service Tests: Unit tests for service classes.<br>
    Repository Tests: Unit tests for data access logic.<br>


## Work with Application

**1. For use the application you should already have MySQL DB installed.<br>**
**2. Clone the repository.<br>**
**3. Use IDE to build and run an application.<br>**
**4. Use Postman and/or Swagger to work with the application.<br>**

**Swagger will be available by URL:**
http://localhost:8081/swagger-ui/index.html#/

![Swagger.PNG](Images%2FSwagger.PNG)

**Add a new user:**
![AddNewUser_1.PNG](Images%2FAddNewUser_1.PNG)
![AddNewUser_2.PNG](Images%2FAddNewUser_2.PNG)

**Login:**
![Login_1.PNG](Images%2FLogin_1.PNG)
![Login_2.PNG](Images%2FLogin_2.PNG)

**Get User's Shopping Cart:**
![GetUserShoppingCart_1.PNG](Images%2FGetUserShoppingCart_1.PNG)
![GetUserShoppingCart_2.PNG](Images%2FGetUserShoppingCart_2.PNG)

**Please feel free to check all the functionality using Swagger!**<br><br>

**Also, you will be able to work with the application by using Postman.**

**You will get a few already saved users and books:<br>
ADMIN<br>
login: sergey@gmail.com<br>
password: 1234<br>
USER<br>
login: kate@gmail.com<br>
password: 1234<br>**

**You can try all the functionality of the application using these pre-saved users.<br><br>**
**Add a new User:<br>**
![AddNewUser_1.PNG](Images%2FAddNewUser_1.PNG)

**Received Bearer Token:<br>**
![Bearer_Token.PNG](Images%2FBearer_Token.PNG)

**Get user's Shopping Cart:<br>**
![GetUserShoppingCart.PNG](Images%2FGetUserShoppingCart.PNG)

**Get all books:<br>**
![GetAllBooks.PNG](Images%2FGetAllBooks.PNG)<br>

**Here is a list of the Postman request collections you can work with:<br>**
[OnlineBookStorePostmanCollection.json](Images%2FOnlineBookStorePostmanCollection.json)
<br>
<br>
**Here is a link to a video with a brief description of the functionality of my application:**<br>
**https://www.loom.com/share/19cd5c522677448ca0b560db6cb8012f?sid=3130d735-207e-4bc8-989f-62df11577e81**
