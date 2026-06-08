# CentroPlus Connect

Aplicación de escritorio para la **gestión integral de un centro**: usuarios, actividades, reservas e incidencias. Desarrollada en Java con JavaFX y SQLite como base de datos local.

**Autores:** Atteneri · Sebastián  
**Versión:** 1.0-SNAPSHOT  
**Java:** 17 · **JavaFX:** 21 · **SQLite:** 3.45.3

---

## Índice

1. [Descripción](#descripción)
2. [Estructura del proyecto](#estructura-del-proyecto)
3. [Requisitos previos](#requisitos-previos)
4. [Instalación y primera ejecución](#instalación-y-primera-ejecución)
5. [Ejecución](#ejecución)
6. [Tests](#tests)
7. [Cobertura de código](#cobertura-de-código)
8. [Arquitectura](#arquitectura)
9. [Base de datos](#base-de-datos)

---

## Descripción

CentroPlus Connect permite a los administradores de un centro gestionar de forma centralizada:

- **Usuarios** — alta, edición, baja y búsqueda por nombre, DNI o email.
- **Actividades** — gestión del catálogo con control de plazas y ocupación en tiempo real.
- **Reservas** — creación y seguimiento de reservas con visualización de nombre de usuario y actividad.
- **Incidencias** — registro, seguimiento y cambio de estado del ciclo de vida completo (Abierta → En proceso → Resuelta → Cerrada).

---

## Estructura del proyecto

```
centroplus-connect/
└── mobile-app/
    ├── pom.xml
    ├── database/
    │   ├── centroplus.db          # Base de datos SQLite
    │   ├── schema.sql             # Definición de tablas
    │   └── seed.sql               # Datos de ejemplo
    └── src/
        ├── main/
        │   ├── java/proyecto/intermodular/
        │   │   ├── app/
        │   │   │   ├── controllers/   # Controladores JavaFX (FXML)
        │   │   │   ├── model/         # Entidades: Usuario, Actividad, Reserva, Incidencia
        │   │   │   ├── repository/    # Acceso a datos (SQLite)
        │   │   │   │   └── interfaces/
        │   │   │   └── service/       # Lógica de negocio
        │   │   │       └── interfaces/
        │   │   ├── database/sqlite/   # Gestor de conexión SQLite
        │   │   └── validations/       # Validaciones reutilizables
        │   └── resources/
        │       ├── css/estilos.css
        │       └── proyecto/intermodular/app/views/   # Archivos FXML
        └── test/
            └── java/proyecto/intermodular/
                ├── model/
                ├── repository/
                └── service/
```

---

## Requisitos previos

| Herramienta | Versión mínima |
|-------------|---------------|
| Java JDK    | 17            |
| Maven       | 3.8+          |
| SQLite3     | cualquiera    |

Verifica tu entorno:

```bash
java -version
mvn -version
sqlite3 --version
```

---

## Instalación y primera ejecución

### 1. Clonar el repositorio

```bash
git clone git@github.com:slayaglez/centroPlus_connect_atteneri_sebastian.git
cd centroplus-connect/mobile-app
```

### 2. Crear la base de datos

La base de datos debe inicializarse antes de ejecutar la aplicación por primera vez:

```bash
sqlite3 database/centroplus.db < database/schema.sql
```

Opcionalmente, carga datos de ejemplo:

```bash
sqlite3 database/centroplus.db < database/seed.sql
```

Verifica que las tablas se han creado correctamente:

```bash
sqlite3 database/centroplus.db ".tables"
# Debe mostrar: actividades  incidencias  reservas  usuarios
```

---

## Ejecución

Desde el directorio `mobile-app/`:

```bash
mvn org.openjfx:javafx-maven-plugin:0.0.8:run
```

> **Nota:** Es importante ejecutar el comando desde `mobile-app/` para que la ruta relativa `./database/centroplus.db` se resuelva correctamente.

---

## Tests

Ejecutar todos los tests:

```bash
mvn test
```

Los resultados se generan en `target/surefire-reports/`.

---

## Cobertura de código

Generar el informe de cobertura con JaCoCo:

```bash
mvn test jacoco:report
```

El informe HTML se genera en `target/site/jacoco/index.html`. Ábrelo en el navegador para ver la cobertura por clase, método y línea.

---

## Arquitectura

El proyecto sigue una arquitectura en **tres capas**:

```
Controladores (JavaFX)
        │
        ▼
   Servicios (Service)
        │
        ▼
  Repositorios (Repository) ──► SQLite
```

- **Controllers** — gestionan los eventos de la interfaz y delegan en los servicios.
- **Services** — contienen la lógica de negocio y validaciones.
- **Repositories** — ejecutan las consultas SQL contra la base de datos.

Cada capa tiene su interfaz correspondiente (`IUsuarioService`, `IUsuarioRepository`, etc.) para facilitar el testing con mocks.

---

## Base de datos

Esquema de tablas:

```
usuarios       id, nombre, dni (UNIQUE), email, telefono, tipo_usuario
actividades    id, nombre, tipo_actividad, duracion, precio, plazas_maximas, plazas_ocupadas
reservas       id, id_usuario (FK), id_actividad (FK), fecha, estado
incidencias    id, id_usuario (FK), asunto, descripcion, fecha, estado
```

Las claves foráneas están activadas mediante `PRAGMA foreign_keys = ON` en cada conexión.

Para inspeccionar la base de datos directamente:

```bash
sqlite3 database/centroplus.db
sqlite> SELECT * FROM usuarios;
sqlite> .quit
```
