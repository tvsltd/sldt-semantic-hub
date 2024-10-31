# Semantic Hub - Docker Setup Guide

This guide provides comprehensive instructions for setting up and running the Semantic Hub using Docker.

## Prerequisites

- Docker and Docker Compose installed on your system
- Postman (for API testing)
- Basic understanding of Keycloak authentication

## Quick Start

1. Launch Services
    ```bash
    $ docker compose -f docker-compose-tvs.yml up
    ```
This command starts all required services:
- Semantic Hub (Port 4242)
- Keycloak (Port 8080)
- PostgreSQL Database

## Service Access

### Semantic Hub
- URL: [http://localhost:4242](http://localhost:4242)
- API Documentation: Available through Swagger UI at the same URL

### Keycloak
- URL: [http://localhost:8080](http://localhost:8080)
- Credentials:
  - Username: `keycloak`
  - Password: `keycloak`
- Configuration: Using customized realm configuration `tvs-demo-realm-import.json` (modified from `default-realm-import.json`)

## API Testing Guide

### Postman Setup
1. Import Collection
   - Locate `semantic-hub.postman_collection.json` in the project repository
   - Import into Postman

### Authentication
1. Navigate to the "Authentication" tab in the `Get Models` request
2. Click "Get access token" to obtain and automatically set the authorization token
3. The authentication process is simplified using hardcoded credentials for development purposes

### Known Behaviors
- Initial API requests may return a 403 (Permission Denied) error
- This is expected behavior during the initial setup phase

## Development

### Making Changes
1. Modify the codebase as needed
2. Rebuild and restart the services:
    ```bash
    $ docker compose -f docker-compose-tvs.yml up --build
    ```

## Troubleshooting

Common Issues:
1. Port Conflicts
   - Ensure ports 4242 and 8080 are available
   - Check for other services using these ports

2. Authentication Issues
   - Verify Keycloak is running
   - Confirm realm configuration is properly loaded
   - Check token expiration

3. Service Dependencies
   - Ensure all services are running (`docker ps`)
   - Check service logs for errors

## Security Notes

- Current authentication setup is for development purposes only
- Modify credentials and security settings before deploying to production
- Review and update the Keycloak configuration for production use

## Support

For additional support:
- Check the service logs using `docker logs [container-name]`
- Review Keycloak documentation for authentication issues
- Create an issue in the repository for bug reports

## Additional Resources

- Keycloak Documentation: [Official Keycloak Docs](https://www.keycloak.org/documentation)
- Semantic Hub API Documentation: Available through Swagger UI