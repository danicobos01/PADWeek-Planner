package es.ucm.fdi.Calendario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import es.ucm.fdi.Calendario.*;
import es.ucm.fdi.MainActivity;
import es.ucm.fdi.R;
import es.ucm.fdi.tienda.TiendaMain;

public class MonthCalendarActivity extends AppCompatActivity {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    Button dia;
    Button semana;
    Button mes;
    Button tienda;
    Button temp;
    Button estad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        CalendarUtils.selectedDate = LocalDate.now();
        setMonthView();

        tienda = (Button) findViewById(R.id.Tienda);
        tienda.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MonthCalendarActivity.this, TiendaMain.class));
            }
        });
        dia = (Button) findViewById(R.id.Dia);
        dia.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MonthCalendarActivity.this, DailyCalendarActivity.class));
            }
        });
        semana = (Button) findViewById(R.id.Semana);
        semana.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MonthCalendarActivity.this, MainActivity.class));
            }
        });
        mes = (Button) findViewById(R.id.Mes);
        mes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MonthCalendarActivity.this, MonthCalendarActivity.class));
            }
        });
        temp = (Button) findViewById(R.id.Temporizador);
        temp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MonthCalendarActivity.this, TiendaMain.class));
            }
        });
        estad = (Button) findViewById(R.id.Estadisticas);
        estad.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MonthCalendarActivity.this, TiendaMain.class));
            }
        });

    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    public void previousMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        if(date != null) {
            CalendarUtils.selectedDate = date;
            setMonthView();
        }
    }
}
