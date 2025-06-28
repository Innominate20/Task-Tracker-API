# Task-Tracker-API

## Description

Task-Tracker-API is a simple demo application designed to showcase basic project and task management features.  
Managers can create notes about projects, manage projects, and assign tasks to users.  
Users can view and update their own tasks.  
Admins have full access to all resources.

This application serves as a demonstration of role-based access and task tracking capabilities.

## Roles and Permissions Overview

The application supports three main user roles, each with specific permissions:

- **Admin**  
  ✦ Full control over the application  
  ✦ Can manage **all** users, projects, tasks, and notes  
  ✦ Responsible for overall system administration

- **Manager**  
  ✦ Can create and manage projects and notes  
  ✦ Assign tasks to users and monitor their progress  
  ✦ Acts as a team lead or project coordinator

- **User**  
  ✦ Limited to viewing and updating **their own** assigned tasks  
  ✦ Cannot access other users' tasks or project settings

| Role    | Permissions                                       |
|---------|-------------------------------------------------|
| 🛡️ **Admin**   | Full access to all resources                       |
| 👔 **Manager** | Create/manage projects, assign tasks               |
| 👤 **User**    | View and update their own assigned tasks           |


## 📝 API Documentation URL

The **API documentation JSON** is accessible at:

`http://localhost:8080/v3/api-docs`

You can use this URL to load the API definition into an online Swagger UI or any OpenAPI-compatible tool for interactive API exploration.


## 🔐 How Authentication Works

This application uses **JWT (JSON Web Token)** based authentication to secure API endpoints:

- Users log in via the `/api/v1/auth/**` endpoints and receive a JWT token.
- The token must be included in the `Authorization` header of subsequent API requests as:  
  `Authorization: Bearer <token>`
- A custom JWT filter validates the token on every request, checking:
  - Token validity and expiration
  - User identity extracted from the token
- If the token is valid, the user’s authentication details are loaded and set in the security context, granting access based on user roles.
- Endpoints are secured according to roles:
  - **Admin**: Full access to all resources
  - **Manager**: Can create and assign tasks, manage projects and notes
  - **User**: Can view and update only their own tasks


## Postman Collection

You can import the API requests using the Postman collection below:

[Download Postman Collection](postman/my-api-collection.json)



## 🚀 How to Run the Application

Follow these steps to get the application up and running locally:

1. **Clone the Repository**

   ```bash
   git clone https://github.com/your-username/Task-Tracker-API.git
   cd Task-Tracker-API

2. **Run application (example of terminal command)**
   ```bash
   DB_NAME=taskdb DB_USER=admin DB_PASSWORD=secret docker compose up




## API Endpoints 📍

### Authentication
- **POST** `/api/v1/auth/register`  
  Register a new user by role (ADMIN, MANAGER, USER).  
  Request body: `RegisterRequest` JSON

- **POST** `/api/v1/auth/login`  
  Authenticate user and get JWT token.  
  Request body: `LoginRequest` JSON

### Projects
- **POST** `/api/v1/projects`  
  Create a new project (authenticated user).  
  Request body: `ProjectCreateRequest` JSON

- **GET** `/api/v1/projects`  
  List all projects for the current user (Admin or Manager).

- **GET** `/api/v1/projects/{id}`  
  Get details of a project by ID (access restricted by ownership for Managers).

- **PUT** `/api/v1/projects/{id}`  
  Update a project by ID (only project owner can update).  
  Request body: `ProjectUpdateRequest` JSON

- **DELETE** `/api/v1/projects/{id}`  
  Delete a project by ID (only project owner can delete).