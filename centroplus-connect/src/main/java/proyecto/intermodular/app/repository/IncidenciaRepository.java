package main.java.proyecto.intermodular.app.repository;

import main.java.proyecto.intermodular.app.repository.interfaces.IIncidenciaRepository;

import proyecto.intermodular.app.model.Incidencia;
import proyecto.intermodular.app.repository.interfaces.IIncidenciaRepository;
import proyecto.intermodular.database.sqlite.SQLiteConnectionManager;

public class IncidenciaRepository extends SQLiteConnectionManager implements IIncidenciaRepository {

    
}
