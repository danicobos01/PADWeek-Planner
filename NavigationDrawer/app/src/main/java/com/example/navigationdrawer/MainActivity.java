package com.example.navigationdrawer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.navigationdrawer.databinding.ActivityMainBinding;
import com.example.navigationdrawer.ui.CalendarioSemanal.SemanalViewModel;
import com.example.navigationdrawer.ui.Temporizador.TempViewModel;
import com.example.navigationdrawer.ui.calendarioMensual.CalendarViewModel;
import com.example.navigationdrawer.ui.estadisticas.EstadViewModel;
import com.example.navigationdrawer.ui.tienda.TiendaViewModel;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity  extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_semanal, R.id.nav_calendario, R.id.nav_Temporizador, R.id.nav_estadisticas, R.id.nav_tienda)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
   /* public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_tienda) {
            Intent tiendaIntent = new Intent(this, TiendaViewModel.class);
            startActivity(tiendaIntent);
        } else if (id == R.id.nav_calendario) {
            Intent calendarioIntent = new Intent(this, CalendarViewModel.class);
            startActivity(calendarioIntent);
        } else if (id == R.id.nav_semanal) {
            Intent semanalIntent = new Intent(this, SemanalViewModel.class);
            startActivity(semanalIntent);
        } else if (id == R.id.nav_Temporizador) {
            Intent tempIntent = new Intent(this, TempViewModel.class);
            startActivity(tempIntent);
        } else if (id == R.id.nav_estadisticas) {
            Intent estadIntent = new Intent(this, EstadViewModel.class);
            startActivity(estadIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    */
}
