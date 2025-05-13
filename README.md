# Crypto Trading Simulator 

This is a simulated cryptocurrency trading platform built with Java and Spring Boot. It allows users to:

- View real-time prices of the top 20 cryptocurrencies using the Kraken WebSocket API
- Maintain a virtual USD account balance
- Buy and sell cryptocurrencies
- Track their portfolio holdings
- View transaction history including profit/loss
- Reset their account to the original state

---

##  Tech Stack

### Backend
- **Language:** Java 17
- **Framework:** Spring Boot
- **WebSocket Integration:** Kraken V2 WebSocket API (Ticker Level 1)
- **Data Access:** Raw SQL using `JdbcTemplate` ( No ORM used as per requirements)
-  **Security:** Spring Security
- **Testing:** JUnit
- **Error Handling:** Centralized via `@RestControllerAdvice`
- **API Documentation:** Swagger/OpenAPI - http://localhost:8080/swagger-ui/index.html

### Database -  PostgreSQL

### FrontEnd
- **Language:** JavaScript
- **Framework:** React
-  Styling: CSS3
- Charts: Chart.js for price trends
- Routing: React Router
- UI Components: Material UI (MUI)
- JWT Decoding: For authentication simulation


Dependencies:

Backend:
Spring Boot Starter Web

Spring Security

Spring JDBC

Lombok

MapStruct

Swagger/OpenAPI

JJWT (JSON Web Token)


  
Frontend:
React Router

JWT Decode

MUI (Material UI)

Chart.js

##  Features Implemented

###  Real-time Prices
- Displays live prices for the top 20 cryptocurrencies via Kraken WebSocket
- Price data is cached in-memory and updated dynamically
- also implemented 24 hour and 7 days charts of the coins prices.

###  User Management
- Ability to create new users with initial balance
- Each user is isolated (multi-user capable)

###  Buying / Selling Crypto
- Buys: Deducts balance, updates holdings, logs transaction
- Sells: Adds balance, reduces holdings, logs transaction
- Prevents purchases with insufficient funds
- Prevents selling more than the current holding
- All trades based on real-time price at the moment

###  Holdings
- View your current cryptocurrency holdings and quantities

###  Transaction History
- All buys and sells are stored with:
  - Coin name and symbol
  - Price at the time
  - Quantity
  - Total value
  - Profit/Loss (for sell transactions)

Transaction filltering based on Transaction Type - Sell, Buy and ALL.

###  Reset Functionality
- Clears all holdings and transactions
- Resets user balance to $10,000

###  Robust Error Handling
- Custom exceptions:
  - `UserNotFoundException`
  - `CoinNotFoundException`
  - `PriceUnavailableException`
  - `InsufficientBalanceException`
  - `InsufficientHoldingsException`
  - `InvalidQuantityException`
  - `DuplicateEmailException`
- Centralized handler returns clean and consistent error responses

---

##  Testing

- JUnit tests for service-level components (Buy/Sell logic, transaction recording, etc.)
- Focused on edge cases and expected behaviors

---


tables:
| Table              | Purpose                         |
| ------------------ | ------------------------------- |
| `users`            | Stores username and USD balance |
| `wallet_balances`  | Tracks current crypto holdings  |
| `transactions`     | Stores all buy/sell activity    |
| `cryptocurrencies` | Stores coin name/symbol/rank    |
| `price_snapshots`  | Stores periodic price points    |

Also a transaction type enum - with types - Buy and Sell.

Screenshots:

Login Page:
![image](https://github.com/user-attachments/assets/701b018f-7f19-4a5b-93fb-d4609f52ece6)
Register Page:
![image](https://github.com/user-attachments/assets/8478bad2-484c-42e5-8c61-591e4e6740e0)
Home Page:
![image](https://github.com/user-attachments/assets/7f79eb02-f06c-4d29-b05c-ecd4baf0dc43)
![image](https://github.com/user-attachments/assets/8147ad0f-6519-4849-9f44-36e4dbf8711f)
Coin Page:
![image](https://github.com/user-attachments/assets/ec98172e-f65b-48b1-9a88-a6af66b14bb5)
![image](https://github.com/user-attachments/assets/68e2acbe-12d1-4535-b1ec-509a98bb6722)
Portfolio Page:
![image](https://github.com/user-attachments/assets/f73cc39d-b20d-4938-9e72-d2842d2c598e)
![image](https://github.com/user-attachments/assets/14ea8b90-2df2-434c-bd60-be0e6260ef03)


##  How to Run

### Prerequisites
- Java 17+
- PostgreSQL running
- Maven or Gradle installed
- Node.js & npm (for frontend)

###  Setup PostgreSQL
```sql
CREATE DATABASE crypto_sim;

Connect it in:
application.properties

spring.datasource.url=jdbc:postgresql://localhost:5432/crypto_sim
spring.datasource.username=postgres
spring.datasource.password=yourpasswor
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=none
spring.sql.init.mode=always

Running Backend:
build - ./mvnw spring-boot:run

Running Frontend:
npm install
npm run dev
http://localhost:5173/register
