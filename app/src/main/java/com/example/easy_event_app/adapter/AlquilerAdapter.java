package com.example.easy_event_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easy_event_app.AceptarEntregaEmpre;
import com.example.easy_event_app.EntregaAlquiler;
import com.example.easy_event_app.R;
import com.example.easy_event_app.model.Alquiler;
import com.example.easy_event_app.InfoAlquiler;
import com.example.easy_event_app.model.Producto;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AlquilerAdapter extends RecyclerView.Adapter<AlquilerAdapter.ViewHolder>{



    private List<Alquiler> alquileres;
    private Context context;
    private NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));


    private AlquilerAdapter.OnItemClickListener onItemClickListener; // Nueva línea

    public AlquilerAdapter(List<Alquiler> alquileres, Context context) {
        this.alquileres = alquileres;
        this.context = context;
        Log.i("alquileres", alquileres.toString());
    }

    @Override
    public AlquilerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_alquiler_empre, parent, false);
        return new AlquilerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlquilerAdapter.ViewHolder holder, int position) {
        Alquiler alquiler = this.alquileres.get(position);
        holder.txtNombreUser.setText(alquiler.getNombre());
        holder.txtFechaEntrega.setText(alquiler.getFecha_devolucion());
        holder.txtFecha.setText(String.valueOf(alquiler.getFecha_alquiler()));
        holder.txtEstado.setText(alquiler.getEstado_pedido());
        double precioProducto = alquiler.getPrecio_alquiler();
        format.setMaximumFractionDigits(0); // Configurar para no mostrar decimale
        String precioFormateado = format.format(precioProducto);
        String precioConMoneda = precioFormateado + " COP";
        holder.txtPrecio.setText(precioConMoneda);


        // Establecer un OnClickListener para el CardView
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener el contexto desde el CardView y abrir la nueva actividad
                Context context = view.getContext();

                if(alquiler.getEstado_pedido().equals("Aceptado")){

                    Intent intent = new Intent(context, EntregaAlquiler.class); // Reemplaza "NuevaActividad" con el nombre de tu actividad
                    intent.putExtra("alquiler_id", alquiler.getId());
                    intent.putExtra("ESTADO_PEDIDO", alquiler.getEstado_pedido());
                    context.startActivity(intent);

                }else if (alquiler.getEstado_pedido().equals("Entregado_usuario")){

                    Intent intent = new Intent(context, AceptarEntregaEmpre.class); // Reemplaza "NuevaActividad" con el nombre de tu actividad
                    intent.putExtra("alquiler_id", alquiler.getId());
                    intent.putExtra("ESTADO_PEDIDO", alquiler.getEstado_pedido());
                    context.startActivity(intent);

                } else {
                    Intent intent = new Intent(context, InfoAlquiler.class); // Reemplaza "NuevaActividad" con el nombre de tu actividad
                    intent.putExtra("alquiler_id", alquiler.getId());
                    intent.putExtra("ESTADO_PEDIDO", alquiler.getEstado_pedido());
                    context.startActivity(intent);
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return alquileres != null ? alquileres.size() : 0;
    }

    public void setOnItemClickListener(AlquilerAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Alquiler alquiler);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombreUser;
        TextView txtFecha;
        TextView txtEstado;
        TextView txtPrecio;
        TextView txtFechaEntrega;
        TextView nada;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNombreUser = itemView.findViewById(R.id.txtNombreUser);
            txtFechaEntrega = itemView.findViewById(R.id.txtFechaEntrega);
            txtFecha = itemView.findViewById(R.id.txtFecha);
            txtEstado = itemView.findViewById(R.id.txtEstado);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
            cardView = itemView.findViewById(R.id.cardView2); // Reemplaza con el ID de tu CardView
        }
    }




}
