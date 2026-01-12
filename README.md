# Warning

This code is not intended for production use as-is.
Each client solution is custom-built based on specific requirements.

# api-gateway

Secure, scalable, and high-performance API Gateway for your application or microservices architecture.

## Security Notice
This repository is a public showcase.
Sensitive implementations (JWT generation, validation logic, secrets)
have been intentionally omitted.

Full implementation is available for clients upon request.

### Features
- JWT authentication & authorization
- Bearer Token support
- Role-based access control
- Centralized security configuration
- API Gateway proxy capabilities
- Custom RestTemplate interceptors
- Global exception handling
- Clean layered architecture

### Architecture
- Controller layer for API exposure
- Service layer for business logic
- Security layer (JWT filters & config)
- Repository abstraction
- DTO-based request/response handling
- Centralized error management

### Tech Stack
- Java 17+
- Spring Boot
- Spring Security
- JWT
- Maven
- REST APIs

This service allows authentication using username and password. Credentials must be stored in a repository, which can be backed by a database, Active Directory, or other storage mechanisms.

`UserRepository` is a generic class that defines the methods to be implemented according to the chosen storage solution.

The class design and system flow when integrating the API Gateway will be attached to the GitHub repository.

## Available Auth Endpoints

The project runs on port **8080**.

| Endpoint                | Parameters        | Description                                                                 |
|-------------------------|-------------------|-----------------------------------------------------------------------------|
| POST /api/auth/login    | Username, password| Validates user existence with the provided password                         |
| POST /api/auth/validate | Token             | Validates token content (user, roles, and token expiration date)            |
| GET /api/auth/users     | -                 | Retrieves all users from the repository                                     |
| GET /api/auth/user      | Username          | Retrieves user details based on the provided username                       |

## Generate a JWT secret key

You can run the following command from the terminal:

````
openssl rand -base64 64
````


You must also configure a token expiration time in milliseconds.

Whenever possible, these values should be set as environment variables:

```
spring.security.jwt.secret-key=${JWT_SECRET_KEY}
spring.security.jwt.expiration=${JWT_EXPIRATION}
```


Alternatively, they can be configured per profile (dev, qa, prod, or any desired profile) in `application.properties`, for example:

```
spring.security.jwt.secret-key=zm1CaJj1ZlFG2SJ5YLaK20b1h/NYQjCD65hkxrHaQmGSmPVVFYya4X8oUzoAHU1uKwAHKc+7/gR6jyYOD3TQBg==
spring.security.jwt.expiration=86400000
```


Note: This step is mandatory, as these values are required to generate JWT tokens during login.

## How to use the service as a proxy between the client and your backend

### How to add new endpoints

Create a new `RestController` (or multiple ones if needed to separate responsibilities) to add new endpoints.

These endpoints must mirror the original backend endpoints.

Example of a new `RestController`:

````
@RestController
@RequestMapping(ApiConstants.URI)
@CrossOrigin(origins = "*", methods= {RequestMethod.POST}) 
public class ExampleRestController implements ExampleApi {

    @Autowired
    private IExampleService exampleService;

    @PostMapping(ApiConstants.EXAMPLE_ENDPOINT)
    public ResponseEntity<?> methodNameInController(@Valid @RequestBody Request request) {
        return ResponseEntity.ok(exampleService.methodNameInService(request));
    }
}
````


Note: API interfaces are used to configure Swagger documentation.

### Classes used to configure the service as a proxy

- `config/security/JwtRequestFilter.java`  
  Extracts the bearer token from each request that is not in the whitelist (endpoints that do not require authentication/authorization) and validates it.  
  Validation includes checking that the user exists, has at least one valid role, and that the token has not expired.

- `config/security/SecurityConfig.java`  
  Configures the response for endpoints that are not in the whitelist and do not include a bearer token.  
  It also defines which endpoints belong to the whitelist.

- `OpenApiConfig.java`  
  Configures Swagger to require token-based authentication for secured endpoints.

- `config/resttemplate/CustomClientHttpRequestInterceptor.java`  
  Intercepts calls to backend services (for which this service acts as a proxy) and injects the bearer token used to access the gateway.

## User Repository

This project provides a demo implementation of a user repository
for authentication and authorization purposes.

The default implementation uses in-memory mock data and is intended
only for development and testing.

You can easily replace it with your own implementation backed by:
- Active Directory / LDAP
- Database (SQL / NoSQL)
- External Identity Provider (IAM)

Simply create a new class implementing `IUserRepository`
and register it as a Spring component.

## Additional Notes

- In `application.properties`, all allowed roles are configured using the `allowed.roles` property.  
  As an example, the roles `ADMIN`, `EDITOR`, and `READ_ONLY` are defined.

- The `ApiConstants` interface also defines role values to be used in the code when needed for testing purposes.  
  If not required, they can be removed.
- 
_____________________________

# api-gateway (ESPAÑOL)

API Gateway seguro, escalable y de alto rendimiento para tu aplicación o arquitectura de microservicios.

## Aviso de Seguridad
Este repositorio es una muestra pública.

Las implementaciones sensibles (generación de JWT, lógica de validación, secrets)
han sido omitidas intencionalmente.

La implementación completa está disponible para clientes bajo solicitud.

### Características
- Autenticación y autorización mediante JWT
- Soporte para Bearer Token
- Control de acceso basado en roles
- Configuración de seguridad centralizada
- Capacidades de proxy como API Gateway
- Interceptores personalizados de `RestTemplate`
- Manejo global de excepciones
- Arquitectura limpia en capas

### Arquitectura
- Capa de controladores para la exposición de APIs
- Capa de servicios para la lógica de negocio
- Capa de seguridad (filtros JWT y configuración)
- Abstracción de repositorios
- Manejo de requests y responses mediante DTOs
- Gestión centralizada de errores

### Tech Stack
- Java 17+
- Spring Boot
- Spring Security
- JWT
- Maven
- REST APIs

El servicio ayuda a loguearse con user y pass, los mismos deben estan almacenados en un repositorio, se puede elegir una
base de datos, un active directory, entre otros.
UserReposotory es una clase genérica con los métodos que se deben implementar de acuerdo al almacenamiento que se elija.
En diseño de clases y el flujo de su sistema cuando se incorpore el api-gateway, estará adjuntado al repositorio de
github.

## Endpoints disponibles para Auth

El proyecto se levanta en el puerto 8080.

| Endpoint                | Parameters      | Función                                                                               |
|-------------------------|-----------------|---------------------------------------------------------------------------------------|
| POST /api/auth/login    | User y password | Validar existencia del usuario con el password indicado                               |
| POST /api/auth/validate | Token           | Validar contenido del token, es decir, usuario, roles y fecha de expiracion del token | 
| GET /api/auth/users     | -               | Obtener todos los usuarios dentro del repositorio                                     |
| GET /api/auth/user      | Username        | Obtener los detalles del usuario ingresado por parámetro                              |

## Generar un jwt secret-key

Se puede correr el siguiente comando desde la terminal:

````
openssl rand -base64 64
````

También hay que setear una expiración en mili segundos.

Los mismos, de ser posible, se deben usar como variables de entornos

```
spring.security.jwt.secret-key=${JWT_SECRET_KEY}
spring.security.jwt.expiration=${JWT_EXPIRATION}
```

o bien como variable en cada perfil (dev, qa, prod, o los perfiles que desee tener) de application.properties, por
ejemplo:

```
spring.security.jwt.secret-key=zm1CaJj1ZlFG2SJ5YLaK20b1h/NYQjCD65hkxrHaQmGSmPVVFYya4X8oUzoAHU1uKwAHKc+7/gR6jyYOD3TQBg==
spring.security.jwt.expiration=86400000
```

Nota: este paso es necesario, ya que se requieren para generar los tokens para el login.

## Como uilizar el servicio como proxy entre el cliente y tu backend

### Cómo agregar los nuevos endpoints:

Crear un nuevo RestController (o más de uno de ser necesario para dividir responsabilidades) para agregar los nuevos
endpoints.
Los mismos deben ser copias de los originales en su backend.
Ejemplo del nuevo RestController:

````
@RestController
@RequestMapping(ApiConstants.URI)
@CrossOrigin(origins = "*", methods= {RequestMethod.POST}) 
public class ExampleRestController implements ExampleApi {

    @Autowired
    private IExampleService exampleService;

    @PostMapping(ApiConstants.EXAMPLE_ENDPOINT)
    public ResponseEntity<?> methodNameInController(@Valid @RequestBody Request request) {
        return ResponseEntity.ok(exampleService.methodNameInService(request));
    }
}
````

Nota: Las interfaces API son para configurar la documentación en swagger.

### Las clases que se usan para configurar el servicio como proxy:

La clase config/security/JwtRequestFilter.java, toma el bearer token en cada request que no este en la white list (
endpoints que no requieren autenticación/autorización),
y lo valida (las validaciones que hacen son que contenga un usuario existente, también alguno de los roles válidos y que
no haya expirado).

La clase config/security/SecurityConfig.java, configura la respuesta que se dará para los endpoints que no estén en la
white list y no tengan bearer token, tmbién especifica
los endpoints que están en la white list.

La clase OpenApiConfig, le indica a swagger que pida el login con token para los endpoints que lo requieren.

La clase config/resttemplate/CustomClientHttpRequestInterceptor.java, intercepta las llamadas a los servicios backend (
de los cuales este servicio hace de proxy), y les
añade el bearer token con que ingresaron al mismo.

## User Repository

Este proyecto provee una implementación de demostración de un repositorio de usuarios para propósitos de autenticación y autorización.

La implementación por defecto utiliza datos simulados en memoria y está destinada únicamente para desarrollo y pruebas.

Podés reemplazarla fácilmente por tu propia implementación basada en:
- Active Directory / LDAP
- Base de datos (SQL / NoSQL)
- Proveedor externo de identidad (IAM)

Simplemente creá una nueva clase que implemente `IUserRepository` y registrala como un componente de Spring.

## Aclaraciones

- En application.properties están configurados, todos los roles permitidos en la variable allowed.roles, a modo de ejemplo están
  seteados los roles ADMIN, EDITOR y READ_ONLY.
- En la interfaz ApiConstants también están seteados los valores de los roles para usarlos en el código de ser necesario para pruebas.
  De no utilizarlos se pueden eliminar.