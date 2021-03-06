# Spring Security Keycloak

A simple Spring Security implementation for Keycloak JWT tokens including:

- Controller tests with JWT mock
- Swagger page with OAUTH2 flow
- Docker compose for Keycloak server
- A generic Spring adapter, non-Keycloak specific

### Keycloak config
For this example to work, make sure you have configured your Keycloak server. A user `user` is already inserted as 
specified in the docker compose file.

1. Create a public client `public`
   - Valid Redirect URIs `*`
   - Web Origins `*`
2. Create a role `test`
3. Assign role `test` to `user`

You can now follow the login flow on `http://localhost:8080/swagger-ui/index.html#/` using credentials `user`/`password`

<sub>*Note that this Keycloak configuration is only suitable for development environments, not for production.<sub>

### Usage example
With this project we can easily secure our endpoints and extract the `sub` of the JWT token, all while Swagger is
facilitating the PKCE flow for us:

https://github.com/sanderk92/spring-security-keycloak/blob/d3e047cdb897f1800a9ab4e32f29d64540d354d7/src/main/kotlin/com/example/controller/TestController.kt#L16-L36
