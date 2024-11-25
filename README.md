# OAuth 2.0 Server Implementation in Java

## Overview
This project implements an OAuth 2.0 authorization server using Java with embedded Jetty. It provides a complete OAuth 2.0 flow implementation with database persistence using PostgreSQL and connection pooling via HikariCP.

## Features
- OAuth 2.0 Authorization Server implementation
- Token Management
- Client Credentials Flow Support
- Database-backed persistence
- RESTful API endpoints
- Dependency Injection using Google Guice
- JSON Processing with Jackson
- Secure logging with Log4j2

## Technology Stack
- Java 19
- Embedded Jetty Server
- PostgreSQL Database
- JOOQ for database operations
- HikariCP for connection pooling
- Jackson for JSON processing
- Google Guice for dependency injection
- Apache HTTP Client for external communications
- Log4j2 for logging

## Prerequisites
- Java 19 or higher
- PostgreSQL database
- Maven



## Request Processing Pipeline

### Filter Chain Architecture
The application implements a filter chain for OAuth2.0 endpoints with two main security layers:

```ascii
+---------------+     +-------------------------+     +----------------------+
|               |     |                         |     |                      |
| Client Request|---->| AuthenticationFilter    |---->| AuthenticationServlet|
|               |     |                         |     |                      |
+---------------+     +-------------------------+     +----------------------+
                                  |
                                  |
                                  v
+---------------+     +-------------------------+     +----------------------+
|               |     |                         |     |                      |
| Token Request |---->| TokenValidationFilter   |---->| TokenServlet        |
|               |     |                         |     |                      |
+---------------+     +-------------------------+     +----------------------+

