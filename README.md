# **Web Application Firewall (WAF) Backend**

A **Spring Boot** backend that acts as a Web Application Firewall (WAF), designed to monitor, filter, and block malicious HTTP traffic. The system protects against vulnerabilities such as SQL Injection and XSS and provides a rule-based mechanism for traffic filtering.

## **Features**
- **Dynamic Route Blocking**: Block or allow requests to specific routes based on database-stored rules.
- **Request Body Filtering**: Analyze user input to detect and block SQL Injection and XSS payloads.
- **Comprehensive Logging**: Logs every request, marking malicious requests for further analysis.
- **CRUD Rule Management**: Add, edit, or delete filtering rules directly from the database.

## **Tech Stack**
- **Language**: Java
- **Framework**: Spring Boot
- **Database**: MySQL
- **Build Tool**: Maven
- **Dependencies**:
  - Spring Boot Starter Web
  - Spring Boot Starter Data JPA
  - Lombok

## **Setup and Installation**

### **Prerequisites**
- Java 17 or higher
- Maven 3.8+
- MySQL Server

### **Clone the Repository**
```bash
git clone https://github.com/your-username/waf-backend.git](https://github.com/NabilMouzouna/Web-App-Firewall.git
cd Web-App-Firewall
```
## **Configure the Database**
1.	Create a MySQL database:
   ```bash
CREATE DATABASE firewall;
```
2. Update the database credentials in src/main/resources/application.properties:
   ```bash
   spring.datasource.url=jdbc:mysql://localhost:3306/firewall
   spring.datasource.username=your_mysql_username
   spring.datasource.password=your_mysql_password
   ```
## **API Endpoints**

### **Rule Management**
| HTTP Method | Endpoint         | Description                        |
|-------------|------------------|------------------------------------|
| `GET`       | `/api/rules`     | Fetch all rules                   |
| `POST`      | `/api/rules`     | Add a new rule                    |
| `PUT`       | `/api/rules`     | Update an existing rule           |
| `DELETE`    | `/api/rules/{id}` | Delete a rule by ID              |

### **Logs**
| HTTP Method | Endpoint         | Description                        |
|-------------|------------------|------------------------------------|
| `GET`       | `/api/logs`      | Fetch all logs                    |

---

## **Example Test**

### **SQL Injection Block**
1. Add a rule to block SQL Injection in the database:
   ```sql
   INSERT INTO rules (type, action, value) VALUES ('REQUEST-BODY', 'BLOCK', 'DROP TABLE');
