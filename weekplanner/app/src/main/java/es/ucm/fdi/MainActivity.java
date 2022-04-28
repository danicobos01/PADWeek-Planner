package es.ucm.fdi;


import static es.ucm.fdi.Calendario.CalendarUtils.daysInWeekArray;
import static es.ucm.fdi.Calendario.CalendarUtils.monthYearFromDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import es.ucm.fdi.R;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import es.ucm.fdi.Calendario.*;
import es.ucm.fdi.TempEstad.DbHelper;
import es.ucm.fdi.TempEstad.Estadisticas;
import es.ucm.fdi.TempEstad.TempActivity;
import es.ucm.fdi.TempEstad.Utilidades;
import es.ucm.fdi.tienda.ThemeUtils;
import es.ucm.fdi.tienda.TiendaMain;

public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener{
    Button dia;
    Button semana;
    Button mes;
    Button tienda;
    Button temp;
    Button estad;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);

        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db == null) dbHelper.onCreate(db);

        initWidgets();
        CalendarUtils.selectedDate = LocalDate.now();
        setWeekView();

        if (Event.eventsList.size() == 0) {
            getEventsFromBBDD();
        }

        tienda = (Button) findViewById(R.id.Tienda);
        tienda.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, TiendaMain.class));
            }
        });
        dia = (Button) findViewById(R.id.Dia);
        dia.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, DailyCalendarActivity.class));
            }
        });
        semana = (Button) findViewById(R.id.Semana);
        semana.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });
        mes = (Button) findViewById(R.id.Mes);
        mes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, MonthCalendarActivity.class));
            }
        });
        temp = (Button) findViewById(R.id.Temporizador);
        temp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, TempActivity.class));
            }
        });
        estad = (Button) findViewById(R.id.Estadisticas);
        estad.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, Estadisticas.class));
            }
        });
    }
    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdapter();

    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);
    }

    public void previousWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setEventAdapter();
    }

    private void setEventAdapter() {
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }

    public void newEventAction(View view) {
        startActivity(new Intent(this, EventEditActivity.class));
    }

    private void getEventsFromBBDD() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db == null) {
            Toast.makeText(this, "Error al conectar con la BBDD", Toast.LENGTH_SHORT).show();
        }
        else {
            String query = "SELECT * FROM " + Utilidades.TABLA_EVENTOS;
            Cursor c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                do {
                    String nombre = c.getString(0);
                    String fecha = c.getString(1);
                    String hora = c.getString(2);
                    String[] anoYmesYdia = fecha.split(":");
                    String[] horaYmin = hora.split(":");
                    LocalTime time = LocalTime.of(Integer.parseInt(horaYmin[0]), Integer.parseInt(horaYmin[1]));
                    LocalDate date = LocalDate.of(Integer.parseInt(anoYmesYdia[0]), Integer.parseInt(anoYmesYdia[1]), Integer.parseInt(anoYmesYdia[2]));
                    Event event = new Event(nombre, date, time);
                    Event.eventsList.add(event);
                } while (c.moveToNext());
            }
            db.close();
        }
    }



    public void BotonDia(View view) {

    }

    public void BotonSemana(View view) {
    }

    public void BotonMes(View view) {
    }

    public void BotonTemporizador(View view) {
    }

    public void BotonEstad(View view) {
    }

  /*  public void BotonTienda(View view) {
        try{
            Intent i = new Intent(this, TiendaMain.class);

            startActivity(i);

        } catch(Exception e1) {
            Toast.makeText(this,"No ha funcionado xd", Toast.LENGTH_LONG).show();
        }
    }

   */
}