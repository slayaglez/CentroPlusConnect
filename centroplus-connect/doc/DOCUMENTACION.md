# CentroPlus Connect — Documentación del Proyecto

**Autores:** Atteneri (Atthemyg) & Sebastián (slayaglez)  
**Versión:** 1.0-SNAPSHOT  
**Fecha:** Junio 2026

---

## Índice

1. [Descripción general del proyecto](#1-descripción-general-del-proyecto)
2. [Arquitectura del sistema](#2-arquitectura-del-sistema)
3. [Tecnologías y versiones](#3-tecnologías-y-versiones)
4. [Estructura del proyecto](#4-estructura-del-proyecto)
5. [Instrucciones de instalación y ejecución](#5-instrucciones-de-instalación-y-ejecución)
6. [Endpoints de la API (módulo de datos)](#6-endpoints-de-la-api-módulo-de-datos)
7. [Base de datos](#7-base-de-datos)
8. [Decisiones de diseño y retos encontrados](#8-decisiones-de-diseño-y-retos-encontrados)
9. [Manual de usuario](#9-manual-de-usuario)

---

## 1. Descripción general del proyecto

**CentroPlus Connect** es una aplicación de escritorio orientada a la **gestión integral de un centro deportivo/educativo**. Permite a los administradores centralizar en una sola herramienta el control de usuarios, actividades, reservas e incidencias.

### Funcionalidades principales

- **Autenticación** — Login con email y contraseña hasheada mediante BCrypt.
- **Dashboard** — Panel de resumen con contadores globales, barras de ocupación por actividad, ingresos totales e historial de las últimas reservas.
- **Usuarios** — Registro, edición, borrado y búsqueda por nombre, DNI o email. Soporte para dos tipos de usuario: `ALUMNO` y `SOCIO`.
- **Actividades** — Gestión del catálogo de actividades (`ACADEMICA` / `DEPORTIVA`) con control de plazas y ocupación en tiempo real.
- **Reservas** — Creación y seguimiento de reservas vinculadas a usuario y actividad, con estados `ACTIVA` y `CANCELADA`.
- **Incidencias** — Registro, seguimiento y cambio de estado a través del ciclo completo: `ABIERTO → EN_PROCESO → CERRADA`.

---

## 2. Arquitectura del sistema

El proyecto sigue una **arquitectura en tres capas** con separación clara de responsabilidades:

```
┌──────────────────────────────────────────┐
│         Capa de Presentación             │
│   Controllers (JavaFX / FXML)            │
│  LoginController, DashboardController    │
│  UsuarioController, ActividadController  │
│  ReservaController, IncidenciaController │
└───────────────┬──────────────────────────┘
                │ delega en
┌───────────────▼─────────────────────┐
│         Capa de Servicios           │
│   UsuarioService, ActividadService  │
│   ReservaService, IncidenciaService │
│   PasswordService                   │
└───────────────┬─────────────────────┘
                │ accede a
┌───────────────▼──────────────────────────┐
│        Capa de Repositorios              │
│  UsuarioRepository, ActividadRepository  │
│  ReservaRepository, IncidenciaRepository │
└───────────────┬──────────────────────────┘
                │ JDBC
┌───────────────▼─────────────────────┐
│         Base de Datos               │
│         SQLite (centroplus.db)      │
└─────────────────────────────────────┘
```

Cada capa expone su propia **interfaz** (`IUsuarioService`, `IUsuarioRepository`, etc.) para facilitar el desacoplamiento y la escritura de tests.

Las **validaciones de datos** se centralizan en la clase utilitaria `Validations`, reutilizada desde la capa de servicio.

---

## 3. Tecnologías y versiones

| Tecnología | Versión | Uso |
|---|---|---|
| Java JDK | 17 | Lenguaje principal |
| JavaFX | 21 | Interfaz gráfica de escritorio |
| SQLite (xerial/sqlite-jdbc) | 3.45.3.0 | Base de datos local embebida |
| Maven | 3.8+ | Gestión de dependencias y ciclo de build |
| JUnit Jupiter | 5.10.2 | Tests unitarios |
| JaCoCo | 0.8.12 | Cobertura de código |
| jBCrypt | 0.4 | Hash seguro de contraseñas |
| Ikonli + Material Design 2 | 12.3.1 | Iconografía en la interfaz |
| Maven Surefire Plugin | 3.2.5 | Ejecución de tests en CI |
| JavaFX Maven Plugin | 0.0.8 | Lanzamiento de la app con Maven |

---

## 4. Estructura del proyecto

```
centroPlus_connect_atteneri_sebastian/
├── README.md
├── images/
│   ├── logo.png
│   ├── header.png
│   ├── usuario.png
│   └── diagrama-bd.png
└── centroplus-connect/
    ├── docker-compose.yml
    ├── doc/
    │   └── DOCUMENTACION.md
    │
    ├── backend-api/
    │   ├── pom.xml
    │   ├── src/
    │   │   ├── main/
    │   │   │   ├── java/com/centroplus/api/
    │   │   │   │   ├── CentroPlusApplication.java
    │   │   │   │   │
    │   │   │   │   ├── domain/
    │   │   │   │   │   └── model/
    │   │   │   │   │       ├── Usuario.java
    │   │   │   │   │       ├── Actividad.java
    │   │   │   │   │       ├── Reserva.java
    │   │   │   │   │       └── Incidencia.java
    │   │   │   │   │
    │   │   │   │   ├── business/
    │   │   │   │   │   ├── UsuarioService.java
    │   │   │   │   │   ├── ActividadService.java
    │   │   │   │   │   ├── ReservaService.java
    │   │   │   │   │   ├── IncidenciaService.java
    │   │   │   │   │   └── interfaces/
    │   │   │   │   │       ├── IUsuarioService.java
    │   │   │   │   │       ├── IActividadService.java
    │   │   │   │   │       ├── IReservaService.java
    │   │   │   │   │       └── IIncidenciaService.java
    │   │   │   │   │
    │   │   │   │   ├── adapters/
    │   │   │   │   │   ├── in/
    │   │   │   │   │   │   ├── controller/
    │   │   │   │   │   │   │   ├── AuthController.java
    │   │   │   │   │   │   │   ├── UsuarioController.java
    │   │   │   │   │   │   │   ├── ActividadController.java
    │   │   │   │   │   │   │   ├── ReservaController.java
    │   │   │   │   │   │   │   └── IncidenciaController.java
    │   │   │   │   │   │   │
    │   │   │   │   │   │   └── api/
    │   │   │   │   │   │       ├── AuthRequest.java
    │   │   │   │   │   │       ├── AuthResponse.java
    │   │   │   │   │   │       ├── UsuarioRequest.java
    │   │   │   │   │   │       ├── UsuarioResponse.java
    │   │   │   │   │   │       ├── ActividadRequest.java
    │   │   │   │   │   │       ├── ActividadResponse.java
    │   │   │   │   │   │       ├── ReservaRequest.java
    │   │   │   │   │   │       ├── ReservaResponse.java
    │   │   │   │   │   │       ├── IncidenciaRequest.java
    │   │   │   │   │   │       └── IncidenciaResponse.java
    │   │   │   │   │   │
    │   │   │   │   │   ├── mapper/
    │   │   │   │   │   │   └── CentroPlusMapper.java
    │   │   │   │   │   │
    │   │   │   │   │   └── out/
    │   │   │   │   │       └── persistence/
    │   │   │   │   │           ├── UsuarioPersistenceAdapter.java
    │   │   │   │   │           ├── ActividadPersistenceAdapter.java
    │   │   │   │   │           ├── ReservaPersistenceAdapter.java
    │   │   │   │   │           ├── IncidenciaPersistenceAdapter.java
    │   │   │   │   │           │
    │   │   │   │   │           ├── interfaces/
    │   │   │   │   │           │   ├── IUsuarioPersistenceAdapter.java
    │   │   │   │   │           │   ├── IActividadPersistenceAdapter.java
    │   │   │   │   │           │   ├── IReservaPersistenceAdapter.java
    │   │   │   │   │           │   └── IIncidenciaPersistenceAdapter.java
    │   │   │   │   │           │
    │   │   │   │   │           └── jpa/
    │   │   │   │   │               ├── UsuarioJpaEntity.java
    │   │   │   │   │               ├── UsuarioJpaRepository.java
    │   │   │   │   │               ├── ActividadJpaEntity.java
    │   │   │   │   │               ├── ActividadJpaRepository.java
    │   │   │   │   │               ├── ReservaJpaEntity.java
    │   │   │   │   │               ├── ReservaJpaRepository.java
    │   │   │   │   │               ├── IncidenciaJpaEntity.java
    │   │   │   │   │               └── IncidenciaJpaRepository.java
    │   │   │   │   │
    │   │   │   │   └── infrastructure/
    │   │   │   │       ├── security/
    │   │   │   │       │   ├── SecurityConfig.java
    │   │   │   │       │   ├── JwtService.java
    │   │   │   │       │   └── JwtAuthenticationFilter.java
    │   │   │   │       │
    │   │   │   │       └── openapi/
    │   │   │   │           └── OpenApiConfig.java
    │   │   │   │
    │   │   │   └── resources/
    │   │   │       └── application.properties
    │   │   │
    │   │   └── test/
    │   │       └── java/
    │   │           ├── controller/
    │   │           ├── business/
    │   │           └── persistence/
    │   │
    │   └── target/
    │
    ├── database/
    │   ├── centroplus.db
    │   ├── centroplus-backup.db
    │   ├── schema.sql
    │   ├── seed.sql
    │   ├── Diagrama.drawio
    │   └── Diagrama.png
    │         
    └── mobile-app/
        ├── pom.xml
        ├── database/
        │   ├── centroplus.db
        │   ├── schema.sql
        │   └── seed.sql
        └── src/
            ├── main/
            │   ├── java/proyecto/intermodular/
            │   │   ├── app/
            │   │   │   ├── Main.java
            │   │   │   ├── PrincipalApplication.java
            │   │   │   ├── controllers/       
            │   │   │   │   ├── LoginController.java
            │   │   │   │   ├── DashboardController.java
            │   │   │   │   ├── UsuarioController.java
            │   │   │   │   ├── CrearUsuarioController.java
            │   │   │   │   ├── EditarUsuarioController.java
            │   │   │   │   ├── ActividadController.java
            │   │   │   │   ├── CrearActividadController.java
            │   │   │   │   ├── EditarActividadController.java
            │   │   │   │   ├── ReservaController.java
            │   │   │   │   ├── CrearReservaController.java
            │   │   │   │   ├── EditarReservaController.java
            │   │   │   │   ├── IncidenciaController.java
            │   │   │   │   ├── CrearIncidenciaController.java
            │   │   │   │   ├── EditarIncidenciaController.java
            │   │   │   │   └── MainController.java
            │   │   │   ├── model/             
            │   │   │   │   ├── Usuario.java
            │   │   │   │   ├── Actividad.java
            │   │   │   │   ├── Reserva.java
            │   │   │   │   ├── ReservaDetalle.java
            │   │   │   │   └── Incidencia.java
            │   │   │   ├── repository/        
            │   │   │   │   ├── UsuarioRepository.java
            │   │   │   │   ├── ActividadRepository.java
            │   │   │   │   ├── ReservaRepository.java
            │   │   │   │   ├── IncidenciaRepository.java
            │   │   │   │   └── interfaces/
            │   │   │   │       ├── IUsuarioRepository.java
            │   │   │   │       ├── IActividadRepository.java
            │   │   │   │       ├── IReservaRepository.java
            │   │   │   │       └── IIncidenciaRepository.java
            │   │   │   └── service/           
            │   │   │       ├── UsuarioService.java
            │   │   │       ├── ActividadService.java
            │   │   │       ├── ReservaService.java
            │   │   │       ├── IncidenciaService.java
            │   │   │       ├── PasswordService.java
            │   │   │       └── interfaces/
            │   │   │           ├── IUsuarioService.java
            │   │   │           ├── IActividadService.java
            │   │   │           ├── IReservaService.java
            │   │   │           └── IIncidenciaService.java
            │   │   ├── database/sqlite/
            │   │   │   └── SQLiteConnectionManager.java
            │   │   └── validations/
            │   │       └── Validations.java
            │   └── resources/
            │       ├── css/estilos.css
            │       └── proyecto/intermodular/app/views/
            │           ├── login.fxml
            │           ├── dashboard.fxml
            │           ├── usuarios.fxml
            │           ├── crear_usuario.fxml
            │           ├── editar_usuario.fxml
            │           ├── actividades.fxml
            │           ├── crear_actividad.fxml
            │           ├── editar_actividad.fxml
            │           ├── reservas.fxml
            │           ├── crear_reserva.fxml
            │           ├── editar_reserva.fxml
            │           ├── incidencias.fxml
            │           ├── crear_incidencia.fxml
            │           └── editar_incidencia.fxml
            └── test/
                └── java/proyecto/intermodular/
                    ├── model/         
                    ├── repository/    
                    └── service/       
```

---

## 5. Instrucciones de instalación y ejecución

### Requisitos previos

| Herramienta | Versión mínima |
|---|---|
| Java JDK | 17 |
| Maven | 3.8+ |
| SQLite3 (CLI) | cualquiera |

Verifica tu entorno antes de continuar:

```bash
java -version
mvn -version
sqlite3 --version
```

### 1. Clonar el repositorio

```bash
git clone git@github.com:slayaglez/centroPlus_connect_atteneri_sebastian.git
cd centroPlus_connect_atteneri_sebastian/centroplus-connect/mobile-app
```

### 2. Inicializar la base de datos

```bash
# Crear las tablas
sqlite3 database/centroplus.db < database/schema.sql

# (Opcional) Cargar datos de ejemplo
sqlite3 database/centroplus.db < database/seed.sql

# Verificar que las tablas se han creado
sqlite3 database/centroplus.db ".tables"
# Resultado esperado: actividades  incidencias  reservas  usuarios
```

### 3. Ejecutar la aplicación

Siempre desde el directorio `mobile-app/` para que la ruta relativa `./database/centroplus.db` se resuelva correctamente:


### 4. Ejecutar los tests

```bash
mvn test
```

Los informes se generan en `target/surefire-reports/`.

### 5. Generar informe de cobertura (JaCoCo)

```bash
mvn clean test
```

El informe HTML interactivo estará en `target/site/jacoco/index.html`.

---

## 6. Endpoints de la API (módulo de datos)

La capa de repositorio actúa como API de datos interna. A continuación se detallan las operaciones disponibles por entidad.

### Usuarios

| Operación | Método en Repository | Descripción |
|---|---|---|
| Crear usuario | `create(usuario, password)` / `createAutoId(usuario, password)` | Inserta usuario con hash BCrypt de la contraseña |
| Obtener por ID | `findById(id)` | Retorna el `Usuario` con ese ID o `null` |
| Obtener todos | `findAll()` | Lista completa de usuarios |
| Actualizar | `update(usuario)` | Modifica nombre, DNI, email, teléfono y tipo |
| Eliminar | `deleteById(id)` | Borrado por ID |
| Autenticación | `findHashByEmail(email)` | Devuelve el hash de contraseña para validar login |

### Actividades

| Operación | Método en Repository | Descripción |
|---|---|---|
| Crear | `create(actividad)` / `createAutoId(actividad)` | Alta de actividad |
| Obtener por ID | `findById(id)` | Actividad por ID |
| Obtener todas | `findAll()` | Catálogo completo |
| Actualizar | `update(actividad)` | Modifica todos los campos |
| Eliminar | `deleteById(id)` | Borrado por ID |
| Reservar plaza | `reservarPlaza(idActividad)` | Incrementa `plazas_ocupadas` en 1 |
| Cancelar plaza | `cancelarPlaza(idActividad)` | Decrementa `plazas_ocupadas` en 1 |
| Actividades completas | `findCompletas()` | Actividades con plazas agotadas |
| Ingresos totales | `calcularIngresosTotales()` | Suma de `precio × plazas_ocupadas` de todas las actividades |

### Reservas

| Operación | Método en Repository | Descripción |
|---|---|---|
| Crear | `create(reserva)` / `createAutoId(reserva)` | Alta de reserva |
| Obtener por ID | `findById(id)` | Reserva por ID |
| Obtener todas | `findAll()` | Lista completa de reservas |
| Actualizar | `update(reserva)` | Modifica estado y otros campos |
| Eliminar | `deleteById(id)` | Borrado por ID |

### Incidencias

| Operación | Método en Repository | Descripción |
|---|---|---|
| Crear | `create(incidencia)` / `createAutoId(incidencia)` | Registro de nueva incidencia |
| Obtener por ID | `findById(id)` | Incidencia por ID |
| Obtener todas | `findAll()` | Lista completa de incidencias |
| Actualizar | `update(incidencia)` | Modifica asunto, descripción o estado |
| Eliminar | `deleteById(id)` | Borrado por ID |

---

## 7. Base de datos

### Esquema de tablas

```sql
PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS incidencias;
DROP TABLE IF EXISTS reservas;
DROP TABLE IF EXISTS actividades;
DROP TABLE IF EXISTS usuarios;

CREATE TABLE usuarios (
    id INTEGER PRIMARY KEY,
    nombre TEXT NOT NULL,
    dni TEXT NOT NULL UNIQUE,
    email TEXT NOT NULL,
    telefono TEXT,
    tipo_usuario TEXT NOT NULL,
    hashed_password TEXT NOT NULL
);

CREATE TABLE actividades (
    id INTEGER PRIMARY KEY,
    nombre TEXT NOT NULL,
    tipo_actividad TEXT NOT NULL,
    duracion INTEGER NOT NULL,
    precio REAL NOT NULL,
    plazas_maximas INTEGER NOT NULL,
    plazas_ocupadas INTEGER NOT NULL
);

CREATE TABLE reservas (
    id INTEGER PRIMARY KEY,
    id_usuario INTEGER NOT NULL,
    id_actividad INTEGER NOT NULL,
    fecha TEXT NOT NULL,
    estado TEXT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
    FOREIGN KEY (id_actividad) REFERENCES actividades(id)
);

CREATE TABLE incidencias (
    id INTEGER PRIMARY KEY,
    id_usuario INTEGER NOT NULL,
    asunto TEXT NOT NULL,
    descripcion TEXT NOT NULL,
    fecha TEXT NOT NULL,
    estado TEXT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);
```

### Gestor de conexión

La clase `SQLiteConnectionManager` gestiona el ciclo de vida de las conexiones JDBC. La ruta por defecto es `./database/centroplus.db` (relativa al directorio de trabajo). Se activan las claves foráneas mediante `PRAGMA foreign_keys = ON` en cada conexión nueva.

Para los tests de repositorio se utiliza una base de datos de prueba en archivo físico (no `:memory:`) y se limpia con `DELETE FROM` en el método `@BeforeEach`, evitando así el problema de que cada conexión en modo `:memory:` genera una instancia de BD independiente.

### Inspección directa de la BD

```bash
sqlite3 database/centroplus.db
sqlite> SELECT * FROM usuarios;
sqlite> SELECT * FROM actividades;
sqlite> .quit
```

---

## 8. Decisiones de diseño y retos encontrados

### Arquitectura en capas con interfaces

Se optó por separar cada capa con su propia interfaz. Esto permite sustituir implementaciones fácilmente y escribir tests unitarios de la lógica de negocio sin necesidad de una BD real.

### Hash de contraseñas con BCrypt

Las contraseñas nunca se almacenan en texto plano. Se utiliza `PasswordService` (basado en jBCrypt) para el hash en el momento del alta y para la verificación en el login. El hash se guarda en la tabla `usuarios` junto con los datos del registro.

### Validaciones centralizadas

Toda la validación de formato y coherencia de datos se concentra en la clase utilitaria `Validations`. Esto evita duplicar lógica entre controladores y servicios, y facilita testear las reglas de negocio de forma independiente. Las reglas incluyen: formato de DNI, email, teléfono, precios, plazas, y enumerados para tipo de usuario/actividad/estado.

### Control de plazas con operaciones atómicas

La reserva y cancelación de plazas se implementan como operaciones `UPDATE` directas en el repositorio (`reservarPlaza` / `cancelarPlaza`) en lugar de leer el objeto y escribirlo de vuelta, reduciendo el riesgo de condiciones de carrera.

### Base de datos de tests en archivo físico

El modo `:memory:` de SQLite crea una nueva instancia de BD por cada conexión JDBC, lo que hacía que los datos insertados en `@BeforeEach` no fueran visibles en la consulta posterior. La solución adoptada fue apuntar los tests a un archivo físico de BD de prueba y vaciar las tablas con `DELETE FROM` antes de cada test.

### Navegación entre vistas con FXMLLoader

Cada pantalla se carga dinámicamente con `FXMLLoader` cuando el usuario navega. Se pasa el `Stage` actual a la nueva escena para no abrir ventanas adicionales. Los controladores de edición reciben el objeto a modificar mediante métodos `set` antes de que se muestre la vista.

### Cambio de idioma en el Login

La pantalla de login incluye un botón que alterna entre español e inglés. La implementación actual conmuta el texto del botón como demostración del mecanismo; la internacionalización completa de todos los textos está prevista como mejora futura.

---

## 9. Manual de usuario

### Inicio de sesión

Al lanzar la aplicación se muestra la pantalla de **Login**. Introduce tu email y contraseña registrados y pulsa **Entrar** (o presiona Enter desde el campo contraseña). Si las credenciales son incorrectas se mostrará un mensaje de error y el campo de contraseña se vaciará automáticamente.

### Dashboard

Tras el login se accede al **Dashboard**, que muestra:

- Número total de usuarios, actividades, reservas e incidencias activas.
- Barras de ocupación de plazas por actividad (en porcentaje).
- Ingresos totales calculados a partir del precio y las plazas ocupadas.
- Historial de las 3 últimas reservas registradas.

Desde la barra de navegación lateral se puede acceder a cualquier módulo.

### Módulo de Usuarios

- **Listado:** tabla con búsqueda en tiempo real por nombre, DNI o email.
- **Crear:** botón "Crear" abre el formulario de alta. Campos obligatorios: nombre, DNI (formato `12345678A`), email, teléfono (`+34XXXXXXXXX`) y tipo (`ALUMNO` / `SOCIO`).
- **Editar:** seleccionar un usuario en la tabla y pulsar "Editar". Los campos se pre-rellenan con los datos actuales.
- **Eliminar:** seleccionar un usuario y pulsar "Eliminar". Se pedirá confirmación.

### Módulo de Actividades

- **Listado:** tabla con nombre, tipo, duración, precio, plazas máximas y plazas ocupadas.
- **Crear:** campos obligatorios: nombre, tipo (`ACADEMICA` / `DEPORTIVA`), duración (minutos), precio (≥ 0) y plazas máximas (> 0).
- **Editar / Eliminar:** misma operativa que en Usuarios.

### Módulo de Reservas

- **Listado:** tabla con ID de reserva, usuario asociado, actividad, fecha y estado.
- **Crear:** se selecciona el usuario y la actividad. El estado inicial es `ACTIVA`. Al crear, se incrementa automáticamente `plazas_ocupadas` en la actividad.
- **Cancelar:** cambiar el estado a `CANCELADA` decrementa las plazas ocupadas.

### Módulo de Incidencias

- **Listado:** tabla con asunto, usuario, fecha y estado, con filtro por estado mediante ComboBox.
- **Crear:** campos obligatorios: usuario, asunto, descripción y estado inicial (`ABIERTO`).
- **Cambiar estado:** el botón de estado en la vista de listado cicla automáticamente el estado y cambia de color: `ABIERTO` (rojo) → `EN_PROCESO` (amarillo) → `CERRADA` (verde).


## 10. Backend REST API

### 10.1 Descripción general

El módulo `backend-api` expone una API REST construida con Spring Boot 3 y seguridad basada en JWT (JSON Web Tokens). Todas las rutas, salvo el login y la consola Swagger, requieren un token Bearer válido en la cabecera `Authorization`.

- **URL base:** `http://localhost:8080/api/v1`
- **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`
- **Consola H2:** `http://localhost:8080/h2-console`

---

### 10.2 Tecnologías del backend

| Tecnología | Versión | Uso |
|---|---|---|
| Spring Boot | 3.x | Framework principal REST |
| Spring Security | 6.x | Autenticación y autorización (JWT + BCrypt) |
| Spring Data JPA / H2 | — | Persistencia en memoria (modo demo) |
| JJWT (io.jsonwebtoken) | — | Generación y validación de tokens JWT |
| SpringDoc OpenAPI 3 | — | Generación automática de Swagger UI |
| MapStruct / Mapper manual | — | Conversión entre domain ↔ DTOs |

---

### 10.3 Seguridad y autenticación

La seguridad es **stateless**: no hay sesión HTTP. Cada petición debe incluir el token JWT obtenido en el login.

**Roles disponibles:**

| Rol | Usuario de demo | Contraseña | Permisos |
|---|---|---|---|
| `ROLE_ADMIN` | `admin` | `admin123` | GET, POST, PATCH, DELETE en todos los recursos |
| `ROLE_USER` | `user` | `user123` | Solo GET en todos los recursos |

El token expira tras **3600 segundos** (1 hora). Para renovarlo, vuelve a llamar al endpoint de login.

**Cabecera obligatoria en todas las peticiones protegidas:**

```
Authorization: Bearer <token>
```

---

### 10.4 Autenticación — `/api/v1/auth`

#### `POST /api/v1/auth/login`

Obtiene un token JWT. Único endpoint **público** (no requiere autenticación previa).

**Body:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Respuesta `200 OK`:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIs...",
  "tokenType": "Bearer",
  "roles": ["ROLE_ADMIN"]
}
```

| Código | Descripción |
|---|---|
| `200 OK` | Login correcto — devuelve token y roles |
| `401 Unauthorized` | Credenciales incorrectas |

---

### 10.5 Usuarios — `/api/v1/usuarios`

#### `GET /api/v1/usuarios`
> Roles: `USER`, `ADMIN`

Lista todos los usuarios registrados.

**Respuesta `200 OK`:**
```json
[
  {
    "id": 1,
    "nombre": "Ana García",
    "dni": "12345678A",
    "email": "ana@example.com",
    "telefono": "+34612345678",
    "tipoUsuario": "SOCIO"
  }
]
```

---

#### `GET /api/v1/usuarios/{id}`
> Roles: `USER`, `ADMIN`

Devuelve el usuario con el ID indicado.

**Respuesta `200 OK`:**
```json
{
  "id": 1,
  "nombre": "Ana García",
  "dni": "12345678A",
  "email": "ana@example.com",
  "telefono": "+34612345678",
  "tipoUsuario": "SOCIO"
}
```

| Código | Descripción |
|---|---|
| `200 OK` | Usuario encontrado |
| `404 Not Found` | No existe ningún usuario con ese ID |

---

#### `POST /api/v1/usuarios`
> Roles: `ADMIN`

Crea un nuevo usuario.

**Body:**
```json
{
  "nombre": "Carlos Pérez",
  "dni": "87654321B",
  "email": "carlos@example.com",
  "telefono": "+34699887766",
  "tipoUsuario": "ALUMNO"
}
```

**Respuesta `201 Created`:**
```json
{
  "id": 5,
  "nombre": "Carlos Pérez",
  "dni": "87654321B",
  "email": "carlos@example.com",
  "telefono": "+34699887766",
  "tipoUsuario": "ALUMNO"
}
```

---

#### `PATCH /api/v1/usuarios/{id}`
> Roles: `ADMIN`

Actualiza los datos de un usuario existente.

**Body:**
```json
{
  "nombre": "Carlos Pérez López",
  "dni": "87654321B",
  "email": "carlos.new@example.com",
  "telefono": "+34699887766",
  "tipoUsuario": "SOCIO"
}
```

| Código | Descripción |
|---|---|
| `200 OK` | Usuario actualizado |
| `404 Not Found` | No existe ningún usuario con ese ID |

---

#### `DELETE /api/v1/usuarios/{id}`
> Roles: `ADMIN`

Elimina un usuario por su ID.

| Código | Descripción |
|---|---|
| `204 No Content` | Eliminado correctamente |
| `404 Not Found` | No existe ningún usuario con ese ID |

---

### 10.6 Actividades — `/api/v1/actividades`

#### `GET /api/v1/actividades`
> Roles: `USER`, `ADMIN`

Lista todas las actividades.

**Respuesta `200 OK`:**
```json
[
  {
    "id": 1,
    "nombre": "Yoga",
    "tipoActividad": "DEPORTIVA",
    "duracion": 60,
    "precio": 25.0,
    "plazasMaximas": 20,
    "plazasOcupadas": 14
  }
]
```

---

#### `GET /api/v1/actividades/{id}`
> Roles: `USER`, `ADMIN`

Devuelve la actividad con el ID indicado.

**Respuesta `200 OK`:**
```json
{
  "id": 1,
  "nombre": "Yoga",
  "tipoActividad": "DEPORTIVA",
  "duracion": 60,
  "precio": 25.0,
  "plazasMaximas": 20,
  "plazasOcupadas": 14
}
```

---

#### `POST /api/v1/actividades`
> Roles: `ADMIN`

Crea una nueva actividad.

**Body:**
```json
{
  "nombre": "Pilates",
  "tipoActividad": "DEPORTIVA",
  "duracion": 45,
  "precio": 20.0,
  "plazasMaximas": 15,
  "plazasOcupadas": 0
}
```

**Respuesta `201 Created`:**
```json
{
  "id": 3,
  "nombre": "Pilates",
  "tipoActividad": "DEPORTIVA",
  "duracion": 45,
  "precio": 20.0,
  "plazasMaximas": 15,
  "plazasOcupadas": 0
}
```

---

#### `PATCH /api/v1/actividades/{id}`
> Roles: `ADMIN`

Actualiza los datos de una actividad.

**Body:**
```json
{
  "nombre": "Pilates Avanzado",
  "tipoActividad": "DEPORTIVA",
  "duracion": 60,
  "precio": 22.5,
  "plazasMaximas": 15,
  "plazasOcupadas": 0
}
```

| Código | Descripción |
|---|---|
| `200 OK` | Actividad actualizada |
| `404 Not Found` | No existe ninguna actividad con ese ID |

---

#### `DELETE /api/v1/actividades/{id}`
> Roles: `ADMIN`

Elimina una actividad por su ID.

| Código | Descripción |
|---|---|
| `204 No Content` | Eliminada correctamente |
| `404 Not Found` | No existe ninguna actividad con ese ID |

---

#### `POST /api/v1/actividades/{id}/reservar-plaza`
> Roles: `ADMIN`

Incrementa en 1 el contador `plazasOcupadas` de la actividad. Se invoca automáticamente al crear una reserva con estado `ACTIVA`.

| Código | Descripción |
|---|---|
| `200 OK` | Plaza reservada |
| `400 Bad Request` | Actividad completa o ID inexistente |

---

#### `POST /api/v1/actividades/{id}/cancelar-plaza`
> Roles: `ADMIN`

Decrementa en 1 el contador `plazasOcupadas`. Se invoca automáticamente al cancelar una reserva.

| Código | Descripción |
|---|---|
| `200 OK` | Plaza liberada |
| `400 Bad Request` | No hay plazas ocupadas o ID inexistente |

---

#### `GET /api/v1/actividades/completas`
> Roles: `USER`, `ADMIN`

Lista las actividades con `plazasOcupadas >= plazasMaximas`.

**Respuesta `200 OK`:**
```json
[
  {
    "id": 2,
    "nombre": "Natación",
    "tipoActividad": "DEPORTIVA",
    "duracion": 45,
    "precio": 30.0,
    "plazasMaximas": 10,
    "plazasOcupadas": 10
  }
]
```

---

#### `GET /api/v1/actividades/ingresos`
> Roles: `USER`, `ADMIN`

Calcula los ingresos totales del centro (suma de `precio × plazasOcupadas` de todas las actividades).

**Respuesta `200 OK`:**
```json
{
  "total": 2450.0
}
```

---

### 10.7 Reservas — `/api/v1/reservas`

#### `GET /api/v1/reservas`
> Roles: `USER`, `ADMIN`

Lista todas las reservas.

**Respuesta `200 OK`:**
```json
[
  {
    "id": 1,
    "idUsuario": 2,
    "idActividad": 1,
    "fecha": "2026-06-10",
    "estado": "ACTIVA"
  }
]
```

---

#### `GET /api/v1/reservas/{id}`
> Roles: `USER`, `ADMIN`

Devuelve la reserva con el ID indicado.

**Respuesta `200 OK`:**
```json
{
  "id": 1,
  "idUsuario": 2,
  "idActividad": 1,
  "fecha": "2026-06-10",
  "estado": "ACTIVA"
}
```

---

#### `POST /api/v1/reservas`
> Roles: `ADMIN`

Crea una nueva reserva. El campo `estado` acepta `ACTIVA` o `CANCELADA`.

**Body:**
```json
{
  "idUsuario": 3,
  "idActividad": 2,
  "fecha": "2026-06-15",
  "estado": "ACTIVA"
}
```

**Respuesta `201 Created`:**
```json
{
  "id": 7,
  "idUsuario": 3,
  "idActividad": 2,
  "fecha": "2026-06-15",
  "estado": "ACTIVA"
}
```

---

#### `PATCH /api/v1/reservas/{id}`
> Roles: `ADMIN`

Actualiza los campos de una reserva.

**Body:**
```json
{
  "idUsuario": 3,
  "idActividad": 2,
  "fecha": "2026-06-16",
  "estado": "ACTIVA"
}
```

| Código | Descripción |
|---|---|
| `200 OK` | Reserva actualizada |
| `404 Not Found` | No existe ninguna reserva con ese ID |

---

#### `DELETE /api/v1/reservas/{id}`
> Roles: `ADMIN`

Elimina una reserva.

| Código | Descripción |
|---|---|
| `204 No Content` | Eliminada correctamente |
| `404 Not Found` | No existe ninguna reserva con ese ID |

---

#### `POST /api/v1/reservas/{id}/cancelar`
> Roles: `ADMIN`

Cambia el estado de la reserva a `CANCELADA` y decrementa automáticamente `plazasOcupadas` en la actividad asociada.

| Código | Descripción |
|---|---|
| `200 OK` | Reserva cancelada |
| `404 Not Found` | No existe ninguna reserva con ese ID |

---

#### `GET /api/v1/reservas/disponibles`
> Roles: `USER`, `ADMIN`

Lista únicamente las reservas con estado `ACTIVA`.

**Respuesta `200 OK`:**
```json
[
  {
    "id": 1,
    "idUsuario": 2,
    "idActividad": 1,
    "fecha": "2026-06-10",
    "estado": "ACTIVA"
  }
]
```

---

### 10.8 Incidencias — `/api/v1/incidencias`

#### `GET /api/v1/incidencias`
> Roles: `USER`, `ADMIN`

Lista todas las incidencias.

**Respuesta `200 OK`:**
```json
[
  {
    "id": 1,
    "idUsuario": 2,
    "asunto": "Vestuario inundado",
    "descripcion": "El grifo del vestuario masculino no cierra.",
    "fecha": "2026-06-01",
    "estado": "ABIERTO"
  }
]
```

---

#### `GET /api/v1/incidencias/{id}`
> Roles: `USER`, `ADMIN`

Devuelve la incidencia con el ID indicado.

**Respuesta `200 OK`:**
```json
{
  "id": 1,
  "idUsuario": 2,
  "asunto": "Vestuario inundado",
  "descripcion": "El grifo del vestuario masculino no cierra.",
  "fecha": "2026-06-01",
  "estado": "ABIERTO"
}
```

---

#### `POST /api/v1/incidencias`
> Roles: `ADMIN`

Registra una nueva incidencia. El estado inicial recomendado es `ABIERTO`.

**Body:**
```json
{
  "idUsuario": 3,
  "asunto": "Proyector averiado",
  "descripcion": "El proyector del aula B no enciende.",
  "fecha": "2026-06-11",
  "estado": "ABIERTO"
}
```

**Respuesta `201 Created`:**
```json
{
  "id": 4,
  "idUsuario": 3,
  "asunto": "Proyector averiado",
  "descripcion": "El proyector del aula B no enciende.",
  "fecha": "2026-06-11",
  "estado": "ABIERTO"
}
```

---

#### `PATCH /api/v1/incidencias/{id}`
> Roles: `ADMIN`

Actualiza todos los campos de una incidencia.

**Body:**
```json
{
  "idUsuario": 3,
  "asunto": "Proyector averiado — Aula B",
  "descripcion": "El proyector del aula B no enciende desde el 10/06.",
  "fecha": "2026-06-11",
  "estado": "EN_PROCESO"
}
```

| Código | Descripción |
|---|---|
| `200 OK` | Incidencia actualizada |
| `404 Not Found` | No existe ninguna incidencia con ese ID |

---

#### `PATCH /api/v1/incidencias/{id}/estado`
> Roles: `ADMIN`

Cambia únicamente el estado de la incidencia sin tocar el resto de campos. Valores válidos: `ABIERTO`, `EN_PROCESO`, `CERRADA`.

**Body:**
```json
{
  "estado": "CERRADA"
}
```

| Código | Descripción |
|---|---|
| `200 OK` | Estado actualizado |
| `400 Bad Request` | Campo `estado` ausente o valor inválido |
| `404 Not Found` | No existe ninguna incidencia con ese ID |

---

#### `DELETE /api/v1/incidencias/{id}`
> Roles: `ADMIN`

Elimina una incidencia.

| Código | Descripción |
|---|---|
| `204 No Content` | Eliminada correctamente |
| `404 Not Found` | No existe ninguna incidencia con ese ID |

---

#### `GET /api/v1/incidencias/usuario/{idUsuario}`
> Roles: `USER`, `ADMIN`

Lista todas las incidencias asociadas a un usuario concreto.

**Respuesta `200 OK`:**
```json
[
  {
    "id": 1,
    "idUsuario": 2,
    "asunto": "Vestuario inundado",
    "descripcion": "El grifo del vestuario masculino no cierra.",
    "fecha": "2026-06-01",
    "estado": "CERRADA"
  }
]
```

---

### 10.9 Resumen de todos los endpoints

| Método | Ruta | Descripción | Roles |
|---|---|---|---|
| `POST` | `/api/v1/auth/login` | Obtener token JWT | Público |
| `GET` | `/api/v1/usuarios` | Listar usuarios | USER, ADMIN |
| `GET` | `/api/v1/usuarios/{id}` | Obtener usuario por ID | USER, ADMIN |
| `POST` | `/api/v1/usuarios` | Crear usuario | ADMIN |
| `PATCH` | `/api/v1/usuarios/{id}` | Actualizar usuario | ADMIN |
| `DELETE` | `/api/v1/usuarios/{id}` | Eliminar usuario | ADMIN |
| `GET` | `/api/v1/actividades` | Listar actividades | USER, ADMIN |
| `GET` | `/api/v1/actividades/{id}` | Obtener actividad por ID | USER, ADMIN |
| `POST` | `/api/v1/actividades` | Crear actividad | ADMIN |
| `PATCH` | `/api/v1/actividades/{id}` | Actualizar actividad | ADMIN |
| `DELETE` | `/api/v1/actividades/{id}` | Eliminar actividad | ADMIN |
| `POST` | `/api/v1/actividades/{id}/reservar-plaza` | Reservar plaza | ADMIN |
| `POST` | `/api/v1/actividades/{id}/cancelar-plaza` | Cancelar plaza | ADMIN |
| `GET` | `/api/v1/actividades/completas` | Actividades sin plazas | USER, ADMIN |
| `GET` | `/api/v1/actividades/ingresos` | Ingresos totales | USER, ADMIN |
| `GET` | `/api/v1/reservas` | Listar reservas | USER, ADMIN |
| `GET` | `/api/v1/reservas/{id}` | Obtener reserva por ID | USER, ADMIN |
| `POST` | `/api/v1/reservas` | Crear reserva | ADMIN |
| `PATCH` | `/api/v1/reservas/{id}` | Actualizar reserva | ADMIN |
| `DELETE` | `/api/v1/reservas/{id}` | Eliminar reserva | ADMIN |
| `POST` | `/api/v1/reservas/{id}/cancelar` | Cancelar reserva | ADMIN |
| `GET` | `/api/v1/reservas/disponibles` | Reservas activas | USER, ADMIN |
| `GET` | `/api/v1/incidencias` | Listar incidencias | USER, ADMIN |
| `GET` | `/api/v1/incidencias/{id}` | Obtener incidencia por ID | USER, ADMIN |
| `POST` | `/api/v1/incidencias` | Crear incidencia | ADMIN |
| `PATCH` | `/api/v1/incidencias/{id}` | Actualizar incidencia | ADMIN |
| `PATCH` | `/api/v1/incidencias/{id}/estado` | Cambiar estado incidencia | ADMIN |
| `DELETE` | `/api/v1/incidencias/{id}` | Eliminar incidencia | ADMIN |
| `GET` | `/api/v1/incidencias/usuario/{idUsuario}` | Incidencias por usuario | USER, ADMIN |

---

### 10.10 Códigos de error comunes

| Código HTTP | Causa |
|---|---|
| `400 Bad Request` | Cuerpo de petición mal formado o campo obligatorio ausente |
| `401 Unauthorized` | Token JWT ausente, expirado o inválido |
| `403 Forbidden` | El usuario autenticado no tiene el rol necesario |
| `404 Not Found` | El recurso con el ID indicado no existe |
| `204 No Content` | Operación DELETE completada correctamente (sin cuerpo) |
| `500 Internal Server Error` | Error inesperado en el servidor |

---
