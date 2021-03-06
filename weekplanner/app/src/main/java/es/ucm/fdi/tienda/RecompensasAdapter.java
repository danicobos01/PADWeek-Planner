package es.ucm.fdi.tienda;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import es.ucm.fdi.R;


public class RecompensasAdapter extends RecyclerView.Adapter<RecompensasAdapter.ViewHolderRecompensas>{

    public ArrayList<RecompensaInfo> recData;
    public Context context;
    private DatabaseOrg dbAdapter;
    private int monedas;

    public RecompensasAdapter(ArrayList<RecompensaInfo> recData, Context context, DatabaseOrg dbAdapter, int curMonedas) {
        this.recData = recData;
        this.context = context;
        this.dbAdapter = dbAdapter;
        this.monedas = curMonedas;
    }


    public ViewHolderRecompensas onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new ViewHolderRecompensas(view);
    }

    @Override
    public void onBindViewHolder(RecompensasAdapter.ViewHolderRecompensas holder, int position) {
        Log.d("onBindViewHolder", "...");
        holder.asignarDatos(recData.get(position).getNombre(), recData.get(position).getDescripcion(),
                recData.get(position).getPlannerCoins(), recData.get(position).getImagen(), recData.get(position).isAdquirida());
    }

    @Override
    public int getItemCount() {
        return recData.size();
    }

    public void setRecData(ArrayList<RecompensaInfo> recData) {
        this.recData = recData;
    }


    public class ViewHolderRecompensas extends RecyclerView.ViewHolder {

        TextView textNombre;
        TextView textDescrip;
        TextView textPlannerCoins;
        TextView textAdquirida;
        ImageView imagen;
        CardView cv;

        public ViewHolderRecompensas(View itemView) {
            super(itemView);
            textNombre = itemView.findViewById(R.id.nombrerecompensa);
            textDescrip = itemView.findViewById(R.id.descripcion);
            textPlannerCoins = itemView.findViewById(R.id.plannercoins);
            textAdquirida = itemView.findViewById(R.id.textadquirida);
            imagen = itemView.findViewById(R.id.iconorecompensa);
            cv = itemView.findViewById(R.id.cardview);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Has pulsado un cardView", "en la pos: " + getAdapterPosition());

                    int pos = getAdapterPosition();
                    RecompensaInfo rec = dbAdapter.getSingleEntry(pos);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    if(rec.isAdquirida()){ // Si la cardview presionada pertenece a una recompensa adquirida
                        builder.setTitle("Aplicar");
                        builder.setMessage("??Desea aplicar la siguiente recompensa?");
                        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch(pos){
                                    case 0: // Color Azul
                                        ThemeUtils.changeToTheme((Activity) context, 0);
                                        break;
                                    case 1: // Color Verde
                                        ThemeUtils.changeToTheme((Activity) context, 2);
                                        break;
                                    case 2: // Color Amarillo
                                        ThemeUtils.changeToTheme((Activity) context, 1);
                                        break;
                                    case 3: // Color Morado
                                        ThemeUtils.changeToTheme((Activity) context, 4);
                                        break;
                                    case 4: // Color Rojo
                                        ThemeUtils.changeToTheme((Activity) context, 3);
                                        break;
                                    default:
                                        ThemeUtils.changeToTheme((Activity) context, 6);
                                }
                            }
                        });
                    }
                    else{
                        builder.setTitle("Confirmaci??n");
                        builder.setMessage("??Desea comprar esta recompensa?");
                        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                // PONER CONDICION DE QUE TIENE SUFICIENTES MONEDAS (mas adelante)
                                /*
                                int monedasAux = dbAdapter.getSingleEntry(pos).getPlannerCoins();
                                if(monedas > monedasAux){

                                    monedas -= monedasAux;
                                }
                                else{ // Mostramos un toast que indique que no hay monedas sufientes

                                }
                                */
                                if (monedas > recData.get(pos).getPlannerCoins()) {
                                    // Modificamos la tabla
                                    dbAdapter.modificarTabla(pos);
                                    int d = Log.d("onClick confirmaci??n", "Queremos modificar la card view n??" + String.valueOf(pos) + rec.getNombre());

                                    // Actualizamos el array
                                    recData = dbAdapter.leerRecompensasFromDB();
                                    textAdquirida.setText("Comprada");

                                    // Actualizamos el recycler
                                    notifyDataSetChanged();
                                    TiendaMain.cambiarMonedasToBD(monedas - recData.get(pos).getPlannerCoins(), context);
                                }
                                else {
                                    Toast.makeText(context, "No tienes suficientes PCs", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                    builder.setNegativeButton("Cancelar", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }

        public void asignarDatos(String nombre, String desc, int pc, int imagen, Boolean adquirida){
            this.textNombre.setText(nombre);
            this.textDescrip.setText(desc);
            if(adquirida){
                this.textAdquirida.setText("Comprada");
                this.textPlannerCoins.setText("");
            }
            else {
                this.textAdquirida.setText("");
                this.textPlannerCoins.setText("PC coins: " + pc);
            }
            this.imagen.setImageResource(imagen);

        }
    }
}
