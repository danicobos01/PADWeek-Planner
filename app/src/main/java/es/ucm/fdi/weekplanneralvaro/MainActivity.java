package es.ucm.fdi.weekplanneralvaro;

package es.ucm.fdi.pdm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import es.ucm.fdi.pdm.db.DbHelper;
import es.ucm.fdi.pdm.db.Utilidades;

public class MainActivity extends AppCompatActivity {

    private ProgressBar bar;
    private Button btn_activar;

    private CountDownTimer timer;
    private boolean timerRunning;
    private TextView texto_bar;

    private Chronometer crono;

    private Dialog dialog;
    private EditText asignatura;

    private long elapsedSeconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bar = findViewById(R.id.temporizador);
        btn_activar = findViewById(R.id.comenzar);

        crono = findViewById(R.id.cronometro);

        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_asignatura);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

    }

    public void aEstadisticas(View view) {
        if (comprobarAsignatura()) {
            Intent i = new Intent(this, Estadísticas.class);
            startActivity(i);
        }
    }

    private boolean comprobarAsignatura() {
        EditText asignatura = dialog.findViewById(R.id.et_asignatura);
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
                    if (registrarEstudio()) {
                        db.close();
                        return true;
                    }
                    else {
                        db.close();
                        return false;
                    }
                } else {
                    if (updateEstudio(c.getInt(c.getColumnIndex(Utilidades.ASIGNATURA_TIEMPO)))) {
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

    private boolean registrarEstudio() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db == null) {
            Toast.makeText(this, "Error al conectar con la bbdd", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            ContentValues cv = new ContentValues();
            cv.put(Utilidades.ASIGNATURA_NOMBRE, asignatura.getText().toString());
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

    private boolean updateEstudio(long tiempo) {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db == null) {
            Toast.makeText(this, "Error al conectar con la bbdd", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            ContentValues cv = new ContentValues();
            cv.put(Utilidades.ASIGNATURA_NOMBRE, asignatura.getText().toString());
            cv.put(Utilidades.ASIGNATURA_TIEMPO, elapsedSeconds + tiempo);
            String[] args = new String[]{asignatura.getText().toString()};
            int idResultante = db.update(Utilidades.TABLA_ASIGNATURAS, cv, Utilidades.ASIGNATURA_NOMBRE + "=?", args);
            Toast.makeText(this, "Se han actualizado " + idResultante + " asignaturas", Toast.LENGTH_LONG).show();
            db.close();
            if (idResultante == 0) return false;
            else return true;
        }
    }

    public void activarDesactivarTemporizador(View view) {
        if (timerRunning) {
            btn_activar.setText("Comenzar");
            timer.cancel();
            timerRunning = false;
            crono.stop();
            long elapsedMillis = SystemClock.elapsedRealtime() - crono.getBase();
            elapsedSeconds = elapsedMillis / 1000;
            dialog.show();
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