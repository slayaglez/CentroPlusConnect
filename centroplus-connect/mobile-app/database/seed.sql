PRAGMA foreign_keys = ON;

INSERT INTO usuarios (id, nombre, dni, email, telefono, tipo_usuario, hashed_password)
VALUES
(1, 'admin', '00000000A', 'admin@admin.com', '600000000', 'Socio', '$2a$12$6Gcyuy51PHR5obCuMf6wYuuqfNLGrRufBQDFAHwRLiGye6s/wvayG'),
(2, 'Sebastián', '00000000B', 'slayaglez@gmail.com', '600000000', 'Alumno', '$2a$12$UlaJ3RLvlOr1OcYn0lCjcOFiHjebkqhEpAay7Ah0Nj6sLtw8wAJo6'),
(3, 'Atteneri', '00000000C', 'atthemyg@gmail.com', '600000000', 'Alumno', '$2a$12$zoeErm8m.87HJAXagLmkyeBJKTflF9HZcdX9RHA1w4E1AHgNM6RwC');


INSERT INTO actividades (
    id, nombre, tipo_actividad, duracion, precio, plazas_maximas, plazas_ocupadas
)
VALUES
(1, 'YogaPlus', 'Deportiva', 60, 25.50, 15, 8),
(2, 'Programación Java', 'Academica', 90, 40.00, 20, 12),
(3, 'SpinUltra', 'Deportiva', 45, 18.00, 12, 12),
(4, 'Inglés técnico', 'Academica', 60, 30.00, 18, 6),
(5, 'Sistemas Linux', 'Academica', 120, 45.00, 16, 10);

INSERT INTO reservas (id, id_usuario, id_actividad, fecha, estado)
VALUES
(1, 1, 1, '2025-01-10', 'Activa'),
(2, 2, 2, '2025-01-11', 'Activa');

INSERT INTO incidencias (id, id_usuario, asunto, descripcion, fecha, estado)
VALUES
(1, 1, 'Problema con reserva', 'No puedo reservar una plaza', '2025-01-12', 'Abierta'),
(2, 2, 'Cambio de horario', 'El horario de la actividad no coincide', '2025-01-13', 'Procesando');