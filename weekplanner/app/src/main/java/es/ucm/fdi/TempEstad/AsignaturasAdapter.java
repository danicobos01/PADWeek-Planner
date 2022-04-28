package es.ucm.fdi.TempEstad;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;

import android.util.Log;
import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import es.ucm.fdi.R;


public class AsignaturasAdapter extends RecyclerView.Adapter<AsignaturasAdapter.ViewHolderAsignaturas>{

    private ArrayList<Asignatura> asignaturasLista;
    private Context context;
    private int maxTiempo;

    public AsignaturasAdapter(ArrayList<Asignatura> recData, Context context, int maxTiempo) {
        this.asignaturasLista = recData;
        this.context = context;
        this.maxTiempo = maxTiempo;
    }


    public ViewHolderAsignaturas onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_asignatura, parent, false);
        return new ViewHolderAsignaturas(view);
    }

    @Override
    public void onBindViewHolder(AsignaturasAdapter.ViewHolderAsignaturas holder, int position) {
        Log.d("onBindViewHolder", "...");
        holder.asignarDatos(asignaturasLista.get(position));
    }

    @Override
    public int getItemCount() {
        return asignaturasLista.size();
    }


    public class ViewHolderAsignaturas extends RecyclerView.ViewHolder {

        TextView textNombre;
        TextView textTiempo;
        ProgressBar pb;

        public ViewHolderAsignaturas(View itemView) {
            super(itemView);
            textNombre = itemView.findViewById(R.id.tv_nombre_asignatura);
            textTiempo = itemView.findViewById(R.id.tv_tiempo_asignatura);
            pb = itemView.findViewById(R.id.pb_asignatura);
        }

        public void asignarDatos(Asignatura asignatura){
            this.textNombre.setText(asignatura.getNombre());
            int hour, min, sec;
            int tiempo = asignatura.getTiempo();
            if (tiempo > 3600) {
                hour = tiempo / 3600;
                min = tiempo % 3600;
                if (min == 0) {
                    textTiempo.setText("" + hour + " h");
                }
                else {
                    textTiempo.setText("" + hour + " h" + min + " min");
                }
            }
            else {
                min = tiempo / 60;
                sec = tiempo % 60;
                if (min == 0) {
                    textTiempo.setText("" + sec + " seg");
                }
                else if (sec == 0) {
                    textTiempo.setText("" + min + " min");
                }
                else {
                    textTiempo.setText("" + min + " min " + sec + " seg");
                }
            }
            pb.setMax(maxTiempo);
            pb.setProgress(tiempo);
        }
    }
}
