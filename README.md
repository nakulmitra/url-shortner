# URL Shortener

A full-stack URL Shortener web application built using **Angular**, **Spring Boot (Java 8)**, and **PostgreSQL**.  
This application allows users to:
- Enter a long/original URL and get a shortened version.
- Provide a short URL and retrieve the original long URL.
- Keep track of how many times each shortened URL is accessed.

## Tech Stack

| Layer         | Technology              |
|--------------|--------------------------|
| Frontend     | HTML, CSS, JavaScript, Angular |
| Backend      | Spring Boot, Java 8       |
| Database     | PostgreSQL                |
| Build Tools  | Maven (for backend), Angular CLI (for frontend) |
| Logging      | SLF4J  |

## Project Structure

```
url-shortener/
├── backend/
│   ├── src/main/java/com/devportal/...
│   ├── src/main/resources/
│   └── pom.xml
├── frontend/
│   ├── src/
│   ├── angular.json
│   └── package.json
└── README.md

```

## Backend Setup (Spring Boot)

1. Navigate to the `backend/` directory:
```bash
cd backend
```

2. Configure PostgreSQL in `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

3. Run the application:

```bash
mvn spring-boot:run
```

## Frontend Setup (Angular)

1. Navigate to the `frontend/` directory:

```bash
cd frontend
```

2. Install dependencies:

```bash
npm install
```

3. Run the Angular app:

```bash
ng serve
```

4. Access it via:

```
http://localhost:4200
```

## Features

* Shorten any long URL.
* Retrieve original URL using short code.
* Track and display hit count per short URL.
* Centralized API response format with timestamp, status, and messages.
* Exception-safe and debug/error logs.

## Sample API Endpoints

| Method | Endpoint                 | Description               |
| ------ | -------------------------| ------------------------- |
| POST   | `/api/shorten`           | Create short URL          |
| POST   | `/api/expand`            | Retrieve original URL     |
| GET    | `/api/getAllURLMappings` | Retrive all original URL  |

## API Response Structure

```json
{
  "status": "CREATED",
  "statusCode": 201,
  "message": "Data created successfuly",
  "success": true,
  "timestamp": "2025-05-23T13:02:32.275+00:00",
  "shortURL": "http://devportal.com/hYUPPR"
}
```

## Logging Format

Example console output:

```
2025-05-23 18:41:49.657 ERROR 22536 --- [nio-8081-exec-3] com.devportal.util.Util : Invalid short URL prefix for URL: https://start.spring.io/
```

## TODO

* Add custom domain configuration.
* Add user authentication (optional).
* Dockerize the application.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## Author

**Dev Portal**
Follow for more backend-focused tutorials and projects.
* LinkedIn: [LinkedIn Profile](https://www.linkedin.com/in/nakul-mitra-microservices-spring-boot-java-postgresql/)
* YouTube: [Dev Portal](https://www.youtube.com/@DevPortal2114)