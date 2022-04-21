package es.ucm.fdi.weekplanner2;


import android.content.Context;

import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;



public class MyOpenHelper extends SQLiteOpenHelper {

    private static final String COMMENTS_TABLE_CREATE =
            "CREATE TABLE recompensasBD(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "recompensa TEXT, descr TEXT, coins INTEGER, imagen INTEGER, adquirida INTEGER)"; // VARBINARY funciona ??
    /*

    CAMPOS:
    - recompensa: nombre de la recompensa
    - descripcion: descripcion de la recompensa
    - coins: coste de la recompensa
    - imagen: imagen de la recompensa
    - adquirida: 1 si el usuario la ha comprado / 0 si el usuario no la ha comprado
     */

    public static final String TABLE_NAME = "recompensasBD";
    private static final String DB_NAME = "recompensas.sqlite";
    private static final int DB_VERSION = 1;


    public MyOpenHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqDB) {
        sqDB.execSQL(COMMENTS_TABLE_CREATE);
        long count = DatabaseUtils.queryNumEntries(sqDB, TABLE_NAME);
        Log.d("MYOPENHELPER", "Numero filas = " + count);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqDB, int i, int i1) {
        sqDB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqDB);
    }
}
