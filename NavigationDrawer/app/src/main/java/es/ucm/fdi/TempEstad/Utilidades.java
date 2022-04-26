package es.ucm.fdi.TempEstad;

public class Utilidades {

    //TABLA ASIGNATURAS
    public static final String CREAR_TABLA_ASIGNATURAS = "CREATE TABLE " + Utilidades.TABLA_ASIGNATURAS + "(" +
            Utilidades.ASIGNATURA_NOMBRE + " TEXT NOT NULL, " +
            Utilidades.ASIGNATURA_TIEMPO + " TEXT NOT NULL)";

    public static final String DROP_TABLA_ASIGNATURAS = "DROP TABLE IF EXISTS " + Utilidades.TABLA_ASIGNATURAS;

    public static final String TABLA_ASIGNATURAS = "asignaturas";
    public static final String ASIGNATURA_NOMBRE = "nombre";
    public static final String ASIGNATURA_TIEMPO = "tiempo";

}
