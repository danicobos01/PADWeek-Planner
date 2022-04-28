package es.ucm.fdi.TempEstad;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import es.ucm.fdi.Calendario.DailyCalendarActivity;
import es.ucm.fdi.Calendario.MonthCalendarActivity;
import es.ucm.fdi.MainActivity;
import es.ucm.fdi.R;
import es.ucm.fdi.tienda.ThemeUtils;
import es.ucm.fdi.tienda.TiendaMain;

public class TempActivity extends AppCompatActivity {

    private ProgressBar bar;
    private Button btn_activar;

    private CountDownTimer timer;
    private boolean timerRunning;

    private Chronometer crono;

    private Dialog dialog;

    private long elapsedSeconds;

    Button dia;
    Button semana;
    Button mes;
    Button tienda;
    Button temp;
    Button estad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ThemeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.temp_activity);

        bar = findViewById(R.id.temporizador);
        btn_activar = findViewById(R.id.comenzar);

        crono = findViewById(R.id.cronometro);

        dialog = new Dialog(TempActivity.this);
        dialog.setContentView(R.layout.dialog_asignatura);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        //Creo la BBDD si no se ha creado
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db == null) dbHelper.onCreate(db);


        tienda = (Button) findViewById(R.id.Tienda);
        tienda.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(TempActivity.this, TiendaMain.class));
            }
        });
        dia = (Button) findViewById(R.id.Dia);
        dia.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(TempActivity.this, DailyCalendarActivity.class));
            }
        });
        semana = (Button) findViewById(R.id.Semana);
        semana.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(TempActivity.this, MainActivity.class));
            }
        });
        mes = (Button) findViewById(R.id.Mes);
        mes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(TempActivity.this, MonthCalendarActivity.class));
            }
        });
        temp = (Button) findViewById(R.id.Temporizador);
        temp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(TempActivity.this, TempActivity.class));
            }
        });
        estad = (Button) findViewById(R.id.Estadisticas);
        estad.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(TempActivity.this, Estadisticas.class));
            }
        });

    }

    public void aEstadisticas(View view) {
        if (comprobarAsignatura()) {
            contabilizarMonedas();
            //Intent i = new Intent(this, Estadisticas.class);
            //startActivity(i);
        }
    }

    private void contabilizarMonedas() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db == null) {
            Toast.makeText(this, "Error al conectar con la BBDD", Toast.LENGTH_SHORT).show();
        }
        else {
            String query = "SELECT * FROM " + Utilidades.TABLA_MONEDAS;
            Cursor c = db.rawQuery(query, null);
            if (!c.moveToFirst()) {
                registrarMonedas();
            }
            else {
                updateMonedas(c.getInt(0));
            }
            db.close();
        }
    }

    private void registrarMonedas() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db == null) {
            Toast.makeText(this, "Error al conectar con la BBDD", Toast.LENGTH_SHORT).show();
        }
        else {
            ContentValues cv = new ContentValues();
            cv.put(Utilidades.MONEDAS_CANTIDAD, elapsedSeconds);
            Long idResultante = db.insert(Utilidades.TABLA_MONEDAS, Utilidades.MONEDAS_CANTIDAD, cv);
            if (idResultante == -1) {
                Toast.makeText(this, "Error al insertar las monedas", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Has conseguido " + elapsedSeconds + " monedas", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }
    }

    private void updateMonedas(int curMonedas) {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db == null) {
            Toast.makeText(this, "Error al conectar con la BBDD", Toast.LENGTH_SHORT).show();
        }
        else {
            ContentValues cv = new ContentValues();
            cv.put(Utilidades.MONEDAS_CANTIDAD, elapsedSeconds + curMonedas);
            int rows = db.update(Utilidades.TABLA_MONEDAS, cv, null, null);
            if (rows < 1) {
                Toast.makeText(this, "No se ha actualizado ninguna tabla", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Ahora tienes " + elapsedSeconds + " monedas más", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }
    }

    @SuppressLint("Range")
    private boolean comprobarAsignatura() {
        EditText asignatura = dialog.findViewById(R.id.et_asignatura);
        String asignatura_str = asignatura.getText().toString();
        if (!asignatura.getText().toString().trim().equalsIgnoreCase("")) {
            DbHelper dbHelper = new DbHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            if (db == null) {
                Toast.makeText(this, "Error al conectar con la bbdd", Toast.LENGTH_LONG).show();
                return false;
            }
            else {
                String[] camposDevueltos = new String[]{Utilidades.ASIGNATURA_NOMBRE, Utilidades.ASIGNATURA_TIEMPO};
                String select = Utilidades.ASIGNATURA_NOMBRE + " = ?";
                String[] selectionArgs = new String[]{asignatura.getText().toString()};
                Cursor c = db.query(
                        Utilidades.TABLA_ASIGNATURAS,
                        camposDevueltos,
                        select,
                        selectionArgs,
                        null,
                        null,
                        null
                );
                if (!c.moveToFirst()) {
                    Toast.makeText(this, "No se han encontrado asignaturas con ese nombre", Toast.LENGTH_SHORT).show();
                    if (registrarEstudio(asignatura_str)) {
                        db.close();
                        return true;
                    }
                    else {
                        db.close();
                        return false;
                    }
                } else {
                    if (updateEstudio(c.getInt(c.getColumnIndex(Utilidades.ASIGNATURA_TIEMPO)), asignatura_str)) {
                        db.close();
                        return true;
                    }
                    else {
                        db.close();
                        return false;
                    }
                }
            }
        }
        else {
            Toast.makeText(this, "Por favor, introduzca el nombre de la asignatura", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean registrarEstudio(String asignatura) {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db == null) {
            Toast.makeText(this, "Error al conectar con la bbdd", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            ContentValues cv = new ContentValues();
            cv.put(Utilidades.ASIGNATURA_NOMBRE, asignatura);
            cv.put(Utilidades.ASIGNATURA_TIEMPO, elapsedSeconds);
            Long idResultante = db.insert(Utilidades.TABLA_ASIGNATURAS, Utilidades.ASIGNATURA_NOMBRE, cv);
            if (idResultante == -1) {
                Toast.makeText(this, "Error al insertar nueva coleccion", Toast.LENGTH_LONG).show();
                db.close();
                return false;
            }
            else {
                Toast.makeText(this, "Se ha insertado la asignatura", Toast.LENGTH_SHORT).show();
                dialog.cancel();
                db.close();
                return true;
            }
        }
    }

    private boolean updateEstudio(long tiempo, String asignatura) {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db == null) {
            Toast.makeText(this, "Error al conectar con la bbdd", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            ContentValues cv = new ContentValues();
            cv.put(Utilidades.ASIGNATURA_NOMBRE, asignatura);
            cv.put(Utilidades.ASIGNATURA_TIEMPO, elapsedSeconds + tiempo);
            String[] args = new String[]{asignatura};
            int idResultante = db.update(Utilidades.TABLA_ASIGNATURAS, cv, Utilidades.ASIGNATURA_NOMBRE + "=?", args);
            Toast.makeText(this, "Se han actualizado " + idResultante + " asignaturas", Toast.LENGTH_LONG).show();
            db.close();
            if (idResultante == 0) return false;
            else return true;
        }
    }

    public void activarDesactivarTemporizador(View view) {
        if (timerRunning) {
            dialog.show();
            btn_activar.setText("Comenzar");
            timer.cancel();
            timerRunning = false;
            crono.stop();
            long elapsedMillis = SystemClock.elapsedRealtime() - crono.getBase();
            elapsedSeconds = elapsedMillis / 1000;
        }
        else {
            final int tiempo = 1; // minutos
            bar.setMax(tiempo * 60);
            int intervalo = 1000;
            btn_activar.setText("Detener");
            crono.setBase(SystemClock.elapsedRealtime());
            crono.start();
            timer = new CountDownTimer(tiempo * 60 * intervalo, intervalo) {

                private int segundos = 59;
                private int minutos = tiempo - 1;

                @Override
                public void onTick(long l) {
                    bar.setProgress((tiempo * 60) - (minutos * 60 + segundos));
                    segundos--;
                    if (segundos == 0) {
                        segundos = 60;
                        bar.setProgress(0);
                    }
                    timerRunning = true;
                }

                @Override
                public void onFinish() {
                    //vuelve a empezar
                    timer.start();
                }
            };
            timer.start();
        }
    }



}