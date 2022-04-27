package es.ucm.fdi.Calendario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalTime;

import es.ucm.fdi.R;
import es.ucm.fdi.TempEstad.DbHelper;
import es.ucm.fdi.TempEstad.Utilidades;

public class EventEditActivity extends AppCompatActivity {
    private EditText eventNameET;
    private TextView eventDateTV, eventTimeTV;
    private NumberPicker numPickerH, numPickerM;

    private LocalTime time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();


        numPickerM.setMinValue(0);
        numPickerM.setMaxValue(59);
        numPickerM.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });
        numPickerH.setMinValue(0);
        numPickerH.setMaxValue(23);
        numPickerH.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        eventDateTV.setText("Fecha: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        eventTimeTV.setText("Hora: ");
    }

    private void initWidgets() {
        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventTimeTV = findViewById(R.id.eventTimeTV);
        numPickerH = findViewById(R.id.numPickerH);
        numPickerM = findViewById(R.id.numPickerM);
    }

    public void saveEventAction(View view) {
        time = LocalTime.of(numPickerH.getValue(), numPickerM.getValue());
        String eventName = eventNameET.getText().toString();
        Event newEvent = new Event(eventName, CalendarUtils.selectedDate, time);
        Event.eventsList.add(newEvent);
        int month = CalendarUtils.selectedDate.getMonthValue();
        int day = CalendarUtils.selectedDate.getDayOfMonth();
        int year = CalendarUtils.selectedDate.getYear();
        String fecha = "" + year + ":" + month + ":" + day;
        String hora = "" + numPickerH.getValue() + ":" + numPickerM.getValue();
        guardarEventoBBDD(eventName, fecha, hora);
        finish();

    }

    private void guardarEventoBBDD(String nombre, String fecha, String hora) {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db == null) {
            Toast.makeText(this, "Error al conectarse a la BBDD", Toast.LENGTH_SHORT).show();
        }
        else {
            ContentValues cv = new ContentValues();
            cv.put(Utilidades.EVENTOS_NOMBRE, nombre);
            cv.put(Utilidades.EVENTOS_FECHA, fecha);
            cv.put(Utilidades.EVENTOS_HORA, hora);
            Long idResultante = db.insert(Utilidades.TABLA_EVENTOS, Utilidades.EVENTOS_NOMBRE, cv);
            if (idResultante == -1) {
                Toast.makeText(this, "Error al insertar evento en la BBDD", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Se ha insertado el evento en la BBDD", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }
    }
}