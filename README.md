# Restaurant System Backend
A RESTful API system to manage operations in a restaurant such as:

Access the Live Swagger UI here: [API Documentation](https://restaurant-system-backend-7eh4.onrender.com/swagger-ui/index.html)

## Features
* **RESTful Architecture:** Contains endpoints for frontend integration
* **User Management:** Logic for handling staff members
* **Order Processing:** Logic for handling customers' orders

## Tech stack
- **Language:** Java 21
- **Framework:** Spring Boot
- **Database:** Postgres
- **Containerization:** Docker

## Setup
1. Clone the repository
```commandline
git clone https://github.com/Jim-03/restaurant-system-backend
cd restaurant-system-backend
```

2. Create a file `.env` with the following contents:
```dotenv
DB_HOST=localhost # Database host
DB_NAME=restaurant # The name of the database 
DB_PASSWORD=password # Password to the database
DB_PORT=5432 # Port to the database
DB_USERNAME=user # Database username
SPRING_PROFILE=dev # Profile to run the app: dev or prod
```

>> NOTE: `dev` profile is set to create a new database environment and may lead to data loss

3. Build the docker container
```commandline
docker build -t restaurant-backend .
```
>> NOTE: Some systems e.g. Ubuntu require running as a superuser, `sudo`
>
4. Run the container
```commandline
docker run -p 8080:8080 --env-file .env restaurant-backend 
```

On a successful launch: Visit [this link](http://localhost:8080/swagger-ui/index.html) on your browser to access the Swagger UI 