package com.example.navigationdrawer.ui.tienda;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.util.Log;

import com.example.navigationdrawer.R;

import java.util.ArrayList;

public class DatabaseOrg {

    private Context context;

    private static SQLiteDatabase db;
    private static MyOpenHelper dbHelper;

    /* Recompensas iniciales */
    private final ArrayList<RecompensaInfo> recompensasA;

    public DatabaseOrg(Context context){

        this.context = context;
        dbHelper = new MyOpenHelper(this.context);
        recompensasA = new ArrayList<>(5);
        db = dbHelper.getReadableDatabase();
        if (db == null) { // Si la base de datos no ha sido creada
            dbHelper.onUpgrade(db, 0, 0);
            db = dbHelper.getReadableDatabase();
        }
        if(firstInitialize()){ // Si la base de datos no ha sido inicializada
            initializeRecompensas();
            this.initialize();
        }
    }

    // Devuelve un booleano que indica si ha sido inicializada la base de datos
    public boolean firstInitialize(){
        db = dbHelper.getReadableDatabase();
        Cursor recCursor = db.rawQuery("SELECT recompensa, descr, coins, imagen, adquirida FROM recompensasBD", null);
        if(recCursor.moveToFirst()) return false;
        else return true;
    }

    // Devuelve una instancia del adapter (esta clase) de la base de datos
    public DatabaseOrg open() {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    // Cierra la base de datos
    public void close(){
        db.close();
    }

    // Devuelve una instancia de la base de datos
    public SQLiteDatabase getDB(){
        return db;
    }

    /*
    public ArrayList<RecompensaInfo> getRecompensas(){
        return recompensasA; // Debería retornar un array obtenido desde la tabla, no desde el array inicial
    }
    */

    // Inicializa la tabla metiendo las recompensas iniciales
    public void initialize(){
        db = dbHelper.getWritableDatabase();
        for (int i = 0; i < recompensasA.size(); i++){
            ContentValues cv = new ContentValues();
            cv.put("recompensa", recompensasA.get(i).getNombre());
            cv.put("descr", recompensasA.get(i).getDescripcion());
            cv.put("coins", recompensasA.get(i).getPlannerCoins());
            // Bitmap aux = recompensasA.get(i).getImagen();
            // byte[] image = DBBitmapUtility.getBytes(aux);
            cv.put("imagen", recompensasA.get(i).getImagen()); // byte[] image
            cv.put("adquirida", recompensasA.get(i).isAdquirida());
            db.insert("recompensasBD", null, cv);
        }
        db.close();
    }

    // Método para obtener las recompensas desde la tabla sql
    public ArrayList<RecompensaInfo> leerRecompensasFromDB(){
        db = dbHelper.getReadableDatabase();
        Cursor recCursor = db.rawQuery("SELECT recompensa, descr, coins, imagen, adquirida FROM recompensasBD", null);
        ArrayList<RecompensaInfo> recArr = new ArrayList<RecompensaInfo>(recompensasA.size());
        if (recCursor.moveToFirst()) {
            do {
                recArr.add(new RecompensaInfo(recCursor.getString(0),
                        recCursor.getString(1),
                        recCursor.getInt(2),
                        recCursor.getInt(3),
                        recCursor.getInt(4) > 0));
            } while (recCursor.moveToNext());
        }
        recCursor.close();
        return recArr;
    }

    // Se llamará a este método cuando el usuario compre una recompensa y "adquirida" pase a ser true
    public static void modificarTabla(int pos){
        db = dbHelper.getWritableDatabase();
        Cursor recCursor = db.rawQuery("SELECT recompensa, descr, coins, imagen, adquirida FROM recompensasBD", null);
        recCursor.moveToPosition(pos);
        String nombreRec = recCursor.getString(0);
        String rec = "\"" + nombreRec + "\"";
        String modifyQuery = "UPDATE recompensasBD" +
                " SET adquirida = 1 " +
                "WHERE recompensa = " +rec+";";

        Log.d("Modificacion BD", modifyQuery);
        try{
            db.execSQL(modifyQuery);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    // Devuelve la recompensa de una única fila
    public RecompensaInfo getSingleEntry(int pos){
        db = dbHelper.getReadableDatabase();
        Cursor recCursor = db.rawQuery("SELECT recompensa, descr, coins, imagen, adquirida FROM recompensasBD", null);
        recCursor.moveToPosition(pos);
        RecompensaInfo rec = new RecompensaInfo(recCursor.getString(0),
                recCursor.getString(1),
                recCursor.getInt(2),
                // DBBitmapUtility.getImage(image),
                recCursor.getInt(3),
                recCursor.getInt(4) > 0);
        return rec;
    }

    // Funcion en la que estableceremos todas las recompensas iniciales
    public void initializeRecompensas(){

        recompensasA.add(new RecompensaInfo("Color Azul", "Cambiar el color de la app al de la imagen",
                1000, R.drawable.azul, false));
        recompensasA.add(new RecompensaInfo("Color Verde", "Cambiar el color de la app al de la imagen",
                1000, R.drawable.verde, false));
        recompensasA.add(new RecompensaInfo("Color Amarillo", "Cambiar el color de la app al de la imagen",
                1000, R.drawable.amarillo, false));
        recompensasA.add(new RecompensaInfo("Color Morado", "Cambiar el color de la app al de la imagen",
                1000, R.drawable.morado, false));
        recompensasA.add(new RecompensaInfo("Color Rojo", "Cambiar el color de la app al de la imagen",
                1000, R.drawable.rojo, false));
        recompensasA.add(new RecompensaInfo("No ads", "Elimina la publicidad de la aplicación",
                9999999, R.drawable.noads, false));
        recompensasA.add(new RecompensaInfo("Modo nocturno", "Desbloquea el modo nocturno de la aplicación",
                50000, R.drawable.modonocturno, false));

    }
}
