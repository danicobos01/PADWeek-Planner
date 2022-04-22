package es.ucm.fdi.pdm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String NOMBRE_DB = "aplicacion.db";

    public DbHelper(@Nullable Context context) {
        super(context, NOMBRE_DB, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Utilidades.CREAR_TABLA_ASIGNATURAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(Utilidades.DROP_TABLA_ASIGNATURAS);
        onCreate(db);

        // TODO NI IDEA DE QUÉ HACE ESTO
    }
}
