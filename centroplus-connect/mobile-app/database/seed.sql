PRAGMA foreign_keys = ON;

INSERT INTO usuarios (id, nombre, dni, email, telefono, tipo_usuario, hashed_password)
VALUES
(1, 'example', '11111111A', 'example@example.com', '600111111', 'ALUMNO', '123456');


INSERT INTO actividades (
    id, nombre, tipo_actividad, duracion, precio, plazas_maximas, plazas_ocupadas
)
VALUES
(1, 'Yoga +45', 'Yoga', 60, 25.50, 15, 8),
(2, 'Programación Java', 'Otro', 90, 40.00, 20, 12),
(3, 'SpinUltra', 'Spinning', 45, 18.00, 12, 12),
(4, 'Inglés técnico', 'Otro', 60, 30.00, 18, 6),
(5, 'Sistemas Linux', 'Otro', 120, 45.00, 16, 10);

INSERT INTO reservas (id, id_usuario, id_actividad, fecha, estado)
VALUES
(1, 1, 1, '2025-01-10', 'Pendiente'),
(2, 2, 2, '2025-01-11', 'Pendiente');

INSERT INTO incidencias (id, id_usuario, asunto, descripcion, fecha, estado)
VALUES
(1, 1, 'Problema con reserva', 'No puedo reservar una plaza', '2025-01-12', 'ABIERTA'),
(2, 2, 'Cambio de horario', 'El horario de la actividad no coincide', '2025-01-13', 'EN_PROCESO');