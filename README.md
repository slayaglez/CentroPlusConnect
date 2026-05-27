# CentroPlus Connect
El desarrollo de una solución tecnológica completa e intermodular con Java17 y Maven

## Estructura del proyecto
```
.
├── centroplus-connect
│   ├── database
│   │   ├── centroplus-backup.db
│   │   ├── centroplus.db
│   │   ├── README.md
│   │   ├── schema.sql
│   │   └── seed.sql
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── proyecto
│       │   │       └── intermodular
│       │   │           ├── app
│       │   │           │   ├── controllers
│       │   │           │   │   ├── ActividadController.java
│       │   │           │   │   ├── DashboardController.java
│       │   │           │   │   ├── IncidenciaController.java
│       │   │           │   │   ├── LoginController.java
│       │   │           │   │   ├── MainController.java
│       │   │           │   │   ├── ReservaController.java
│       │   │           │   │   └── UsuarioController.java
│       │   │           │   ├── Main.java
│       │   │           │   ├── model
│       │   │           │   │   ├── Actividad.java
│       │   │           │   │   ├── Incidencia.java
│       │   │           │   │   ├── Reserva.java
│       │   │           │   │   └── Usuario.java
│       │   │           │   ├── PrincipalApplication.java
│       │   │           │   ├── repository
│       │   │           │   │   ├── ActividadRepository.java
│       │   │           │   │   ├── IncidenciaRepository.java
│       │   │           │   │   ├── interfaces
│       │   │           │   │   │   ├── IActividadRepository.java
│       │   │           │   │   │   ├── IIncidenciaRepository.java
│       │   │           │   │   │   ├── IReservaRepository.java
│       │   │           │   │   │   └── IUsuarioRepository.java
│       │   │           │   │   ├── ReservaRepository.java
│       │   │           │   │   └── UsuarioRepository.java
│       │   │           │   └── service
│       │   │           │       ├── ActividadService.java
│       │   │           │       ├── IncidenciaService.java
│       │   │           │       ├── interfaces
│       │   │           │       │   ├── IActividadService.java
│       │   │           │       │   ├── IIncidenciaService.java
│       │   │           │       │   ├── IReservaService.java
│       │   │           │       │   └── IUsuarioService.java
│       │   │           │       ├── ReservaService.java
│       │   │           │       └── UsuarioService.java
│       │   │           ├── database
│       │   │           │   └── sqlite
│       │   │           │       └── SQLiteConnectionManager.java
│       │   │           └── validations
│       │   │               └── Validations.java
│       │   └── resources
│       │       ├── css
│       │       │   └── estilos.css
│       │       └── views
│       │           ├── actividades.fxml
│       │           ├── dashboard.fxml
│       │           ├── incidencias.fxml
│       │           ├── login.fxml
│       │           ├── reservas.fxml
│       │           └── usuarios.fxml
│       └── test
│           └── java
│               └── proyecto
│                   └── intermodular
│                       ├── model
│                       │   ├── ActividadTest.java
│                       │   ├── IncidenciaTest.java
│                       │   ├── ReservaTest.java
│                       │   └── UsuarioTest.java
│                       ├── repository
│                       │   ├── ActividadRepositoryTest.java
│                       │   ├── IncidenciaRepositoryTest.java
│                       │   ├── ReservaRepositoryTest.java
│                       │   └── UsuarioRepositoryTest.java
│                       └── service
│                           ├── ActividadServiceTest.java
│                           ├── IncidenciaServiceTest.java
│                           ├── ReservaServiceTest.java
│                           └── UsuarioServiceTest.java
├── images
│   ├── diagrama-bd.png
│   └── usuario.png
└── README.md

29 directories, 59 files

```

## Ejecución

Ejecuta `mvn run:javafx` en el directorio raíz
