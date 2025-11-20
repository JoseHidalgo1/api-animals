# Animals & Habitats Management API

## Descripción del Proyecto

**Animals & Habitats Management API** es una aplicación REST desarrollada en **Spring Boot** que gestiona la información de animales y sus hábitats en una base de datos relacional. El sistema permite realizar operaciones CRUD (crear, leer, actualizar, eliminar) sobre dos entidades relacionadas: **Animal** (detalle) y **Habitat** (maestro).

La aplicación implementa una arquitectura en capas siguiendo las mejores prácticas de desarrollo empresarial, utilizando **JPA/Hibernate** para la persistencia de datos y **REST** para la exposición de servicios web.

---

## Características Principales

✅ **CRUD Completo**
- Crear, consultar, actualizar y eliminar animales y hábitats
- Búsqueda individual mostrando todos los atributos

✅ **Consultas Personalizadas**
- Filtrar animales por estado salvaje/domesticado
- Buscar animales por nombre (insensible a mayúsculas/minúsculas)
- Filtrar hábitats por cobertura (cubiertos/descubiertos)

✅ **Validaciones y Reglas de Negocio**
- No se permite eliminar un hábitat si tiene animales asociados
- No se puede crear duplicados con el mismo ID
- Validación de datos al crear y actualizar registros
- Mensajes de error claros en español

✅ **Persistencia en Base de Datos**
- Almacenamiento relacional con JPA/Hibernate
- Relación Many-to-One entre Animal y Habitat
- Transacciones automáticas gestionadas por Spring

✅ **Manejo de Excepciones**
- GlobalExceptionHandler centralizado
- Respuestas HTTP coherentes con códigos apropiados (404, 409, 500, etc.)
- Mensajes de error descriptivos en español

---

## Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|------------|---------|----------|
| **Java** | 11+ | Lenguaje de programación |
| **Spring Boot** | 2.7.x | Framework principal |
| **Spring Data JPA** | 2.7.x | Acceso a datos |
| **Hibernate** | 5.6.x | ORM (Object-Relational Mapping) |
| **MySQL/H2** | Última | Base de datos relacional |
| **Maven** | 3.6+ | Gestor de dependencias |
| **Postman** | - | Prueba de endpoints |

---

## Estructura del Proyecto

```
com.example.animals/
├── AnimalsApiApplication.java          # Clase principal de arranque
│
├── controller/
│   ├── AnimalController.java            # Endpoints para animales
│   └── HabitatController.java           # Endpoints para hábitats
│
├── service/
│   ├── AnimalService.java               # Lógica de negocio de animales
│   └── HabitatService.java              # Lógica de negocio de hábitats
│
├── repository/
│   ├── AnimalRepository.java            # Acceso a datos de animales
│   └── HabitatRepository.java           # Acceso a datos de hábitats
│
├── model/
│   ├── Animal.java                      # Entidad Animal
│   └── Habitat.java                     # Entidad Habitat
│
└── exception/
    ├── GlobalExceptionHandler.java      # Manejador centralizado de errores
    ├── AnimalNotFoundException.java      # Excepción personalizada
    ├── HabitatNotFoundException.java     # Excepción personalizada
    ├── AnimalIdAlreadyExistsException.java
    └── HabitatIdAlreadyExistsException.java
```

---

## Modelo de Datos

### Relación entre Entidades

```
Habitat (Maestro)
  ├── id (Integer) - PK
  ├── name (String)
  ├── area (Double)
  ├── establishedDate (LocalDateTime)
  └── isCovered (Boolean)
       ↓ 1:N
    Animal (Detalle)
      ├── id (Long) - PK
      ├── name (String)
      ├── weight (Double)
      ├── birthDateTime (LocalDateTime)
      ├── isWild (Boolean)
      └── habitat_id (Integer) - FK
```

**Descripción:**
- Un **Habitat** puede tener múltiples **Animales** asociados.
- Un **Animal** pertenece a exactamente un **Habitat**.
- La relación es **Many-to-One** desde Animal hacia Habitat.

---

## Endpoints REST

### Animales

| Método | Ruta | Descripción | Parámetros |
|--------|------|-------------|-----------|
| **POST** | `/animals` | Crear nuevo animal | Body: JSON Animal |
| **GET** | `/animals` | Listar todos los animales | `?isWild=true/false` (opcional) |
| **GET** | `/animals/{id}` | Obtener animal por ID | Path: id |
| **GET** | `/animals/search/by-name` | Buscar por nombre | `?name=Leo` |
| **PUT** | `/animals/{id}` | Actualizar animal | Path: id, Body: JSON |
| **DELETE** | `/animals/{id}` | Eliminar animal | Path: id |

### Hábitats

| Método | Ruta | Descripción | Parámetros |
|--------|------|-------------|-----------|
| **POST** | `/habitats` | Crear nuevo hábitat | Body: JSON Habitat |
| **GET** | `/habitats` | Listar todos los hábitats | `?is_covered=true/false` (opcional) |
| **GET** | `/habitats/{id}` | Obtener hábitat por ID | Path: id |
| **PUT** | `/habitats/{id}` | Actualizar hábitat | Path: id, Body: JSON |
| **DELETE** | `/habitats/{id}` | Eliminar hábitat | Path: id |

---

## Ejemplos de Uso (Postman)

### 1. Crear un Hábitat
```http
POST http://localhost:8080/habitats
Content-Type: application/json

{
  "id": 1,
  "name": "Selva Tropical",
  "area": 500.0,
  "establishedDate": "2020-01-15T10:30:00",
  "isCovered": true
}
```

**Respuesta (201 Created):**
```json
{
  "id": 1,
  "name": "Selva Tropical",
  "area": 500.0,
  "establishedDate": "2020-01-15T10:30:00",
  "isCovered": true
}
```

---

### 2. Crear un Animal
```http
POST http://localhost:8080/animals
Content-Type: application/json

{
  "id": 1,
  "name": "León",
  "weight": 190.5,
  "birthDateTime": "2015-06-20T08:00:00",
  "isWild": true,
  "habitat": {
    "id": 1
  }
}
```

**Respuesta (201 Created):**
```json
{
  "id": 1,
  "name": "León",
  "weight": 190.5,
  "birthDateTime": "2015-06-20T08:00:00",
  "isWild": true,
  "habitat": {
    "id": 1,
    "name": "Selva Tropical",
    ...
  }
}
```

---

### 3. Listar Animales Salvajes
```http
GET http://localhost:8080/animals?isWild=true
```

**Respuesta (200 OK):**
```json
[
  {
    "id": 1,
    "name": "León",
    "weight": 190.5,
    "birthDateTime": "2015-06-20T08:00:00",
    "isWild": true,
    "habitat": { ... }
  }
]
```

---

### 4. Buscar Animal por Nombre
```http
GET http://localhost:8080/animals/search/by-name?name=león
```

---

### 5. Obtener un Hábitat Específico
```http
GET http://localhost:8080/habitats/1
```

**Respuesta (200 OK):**
```json
{
  "id": 1,
  "name": "Selva Tropical",
  "area": 500.0,
  "establishedDate": "2020-01-15T10:30:00",
  "isCovered": true
}
```

---

### 6. Actualizar un Animal
```http
PUT http://localhost:8080/animals/1
Content-Type: application/json

{
  "name": "León Mayor",
  "weight": 195.0,
  "birthDateTime": "2015-06-20T08:00:00",
  "isWild": true,
  "habitat": { "id": 1 }
}
```

---

### 7. Eliminar Hábitat (Con Validación)
```http
DELETE http://localhost:8080/habitats/1
```

**Si hay animales asociados (409 Conflict):**
```json
{
  "status": 409,
  "message": "No se puede eliminar el hábitat porque está asociado a uno o más animales.",
  "timestamp": "2025-11-20T10:30:00"
}
```

---

## Instalación y Configuración

### Requisitos Previos
- **JDK 11** o superior
- **Maven 3.6** o superior
- **MySQL 8.0** o **H2** (incluido en el proyecto para pruebas)

### Pasos de Instalación

1. **Clonar el repositorio**
```bash
git clone https://github.com/tuusuario/animals-api.git
cd animals-api
```

2. **Configurar la base de datos**

Edita el archivo `application.properties` en `src/main/resources/`:

```properties
# Para MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/animals_db
spring.datasource.username=root
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update

# Para H2 (desarrollo)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

3. **Compilar el proyecto**
```bash
mvn clean install
```

4. **Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

O ejecutar directamente:
```bash
java -jar target/animals-api-1.0.0.jar
```

5. **Verificar que funciona**

Accede a:
```
http://localhost:8080/habitats
```

Deberías recibir una respuesta JSON (vacía si es la primera vez).

---

## Pruebas

### Con Postman

1. Importa las colecciones de ejemplo incluidas en `postman/`
2. O crea manualmente las solicitudes siguiendo los ejemplos de arriba

### Con cURL

```bash
# Crear hábitat
curl -X POST http://localhost:8080/habitats \
  -H "Content-Type: application/json" \
  -d '{"id":1,"name":"Selva","area":500,"establishedDate":"2020-01-15T10:30:00","isCovered":true}'

# Listar todos
curl -X GET http://localhost:8080/habitats

# Buscar por ID
curl -X GET http://localhost:8080/habitats/1
```

---

## Consultas Personalizadas JPA

El proyecto incluye dos consultas personalizadas para cada entidad:

### AnimalRepository
```java
List<Animal> findByIsWild(Boolean isWild);
List<Animal> findByNameContainingIgnoreCase(String name);
```

### HabitatRepository
```java
List<Habitat> findByIsCovered(boolean isCovered);
List<Habitat> findByAreaGreaterThan(double area);
```

---

## Manejo de Errores

### Códigos HTTP Utilizados

| Código | Significado | Ejemplo |
|--------|-------------|---------|
| **200** | OK - Solicitud exitosa | GET, PUT correctos |
| **201** | Created - Recurso creado | POST exitoso |
| **400** | Bad Request - Datos inválidos | Validación fallida |
| **404** | Not Found - Recurso no existe | Animal/Habitat no encontrado |
| **409** | Conflict - Violación de regla | Eliminar hábitat con animales |
| **500** | Internal Server Error | Error del servidor |

### Estructura de Respuesta de Error

```json
{
  "status": 404,
  "message": "No se encontró un animal con el ID 999",
  "timestamp": "2025-11-20T10:30:00"
}
```

---

## Arquitectura y Diseño

### Patrón de Capas

```
┌─────────────────────────────────┐
│   REST Controller (API REST)    │  ← Recibe peticiones HTTP
├─────────────────────────────────┤
│   Service (Lógica de Negocio)   │  ← Valida y procesa
├─────────────────────────────────┤
│   Repository (Acceso a Datos)   │  ← Consulta base de datos
├─────────────────────────────────┤
│   Base de Datos Relacional      │  ← Almacena información
└─────────────────────────────────┘
```

### Flujo de una Petición

```
1. Cliente (Postman) envía: POST /animals
2. Controller recibe y valida el JSON
3. Service verifica que el Habitat existe
4. Repository guarda en la BD
5. GlobalExceptionHandler captura errores si existen
6. Servidor devuelve respuesta JSON con código HTTP
```
---

## Autores
Desarrollo de Aplicaciones Empresariales  
---



