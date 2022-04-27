package es.ucm.fdi.TempEstad;

public class Utilidades {

    //TABLA ASIGNATURAS
    public static final String CREAR_TABLA_ASIGNATURAS = "CREATE TABLE " + Utilidades.TABLA_ASIGNATURAS + "(" +
            Utilidades.ASIGNATURA_NOMBRE + " TEXT NOT NULL PRIMARY KEY, " +
            Utilidades.ASIGNATURA_TIEMPO + " TEXT NOT NULL)";

    public static final String DROP_TABLA_ASIGNATURAS = "DROP TABLE IF EXISTS " + Utilidades.TABLA_ASIGNATURAS;

    public static final String TABLA_ASIGNATURAS = "asignaturas";
    public static final String ASIGNATURA_NOMBRE = "nombre";
    public static final String ASIGNATURA_TIEMPO = "tiempo";


    //TABLA MONEDAS
    public static final String CREAR_TABLA_MONEDAS = "CREATE TABLE " + Utilidades.TABLA_MONEDAS + "(" +
            Utilidades.MONEDAS_CANTIDAD + " INTEGER PRIMARY KEY)";

    public static final String DROP_TABLA_MONEDAS = "DROP TABLE IF EXISTS " + Utilidades.TABLA_MONEDAS;

    public static final String TABLA_MONEDAS = "monedas";
    public static final String MONEDAS_CANTIDAD = "n_monedas";

    //TABLA EVENTOS
    public static final String CREAR_TABLA_EVENTOS = "CREATE TABLE " + Utilidades.TABLA_EVENTOS + "(" +
            Utilidades.EVENTOS_NOMBRE + " TEXT NOT NULL, " +
            Utilidades.EVENTOS_FECHA + " TEXT NOT NULL, " +
            Utilidades.EVENTOS_HORA + " TEXT NOT NULL)";

    public static final String DROP_TABLA_EVENTOS = "DROP TABLE IF EXISTS " + Utilidades.TABLA_EVENTOS;

    public static final String TABLA_EVENTOS = "eventos";
    public static final String EVENTOS_NOMBRE = "n_monedas";
    public static final String EVENTOS_FECHA = "fecha";
    public static final String EVENTOS_HORA = "hora";


}
