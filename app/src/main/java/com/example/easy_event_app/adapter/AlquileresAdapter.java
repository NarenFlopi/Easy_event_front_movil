package com.example.easy_event_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easy_event_app.AceptarAlquiler;
import com.example.easy_event_app.EntregaAlquiler;
import com.example.easy_event_app.EntregarAlquilerUser;
import com.example.easy_event_app.InfoAlquilerSolicitado;
import com.example.easy_event_app.Productos;
import com.example.easy_event_app.R;
import com.example.easy_event_app.ThirdFragment;
import com.example.easy_event_app.model.Alquiler;
import com.example.easy_event_app.model.Categoria;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AlquileresAdapter extends RecyclerView.Adapter<AlquileresAdapter.ViewHolder> {

    private List<Alquiler> alquilers;
    private Context context;
    private OnItemClickListener onItemClickListener;

    private NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

    public AlquileresAdapter(List<Alquiler> alquilers, Context context) {
        this.alquilers = alquilers;
        this.context = context;
    }

    @Override
    public AlquileresAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_alquiler, parent, false);
        return new AlquileresAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlquileresAdapter.ViewHolder holder, int position) {

        Alquiler alquiler = alquilers.get(position);


        holder.txtFechaAlquiler.setText(alquiler.getFecha_alquiler());
        holder.txtFechaDevolucion.setText(alquiler.getFecha_devolucion());
        holder.txtEstadoPedido.setText(alquiler.getEstado_pedido());
        holder.txtPrecioAlquiler.setText(String.valueOf(alquiler.getPrecio_alquiler()));

        double precioProducto = alquiler.getPrecio_alquiler();
        format.setMaximumFractionDigits(0); // Configurar para no mostrar decimale
        String precioFormateado = format.format(precioProducto);

        String precioConMoneda = precioFormateado + " COP";

        holder.txtPrecioAlquiler.setText(precioConMoneda);

        int colorResId;
        switch (alquiler.getEstado_pedido()) {
            case "Modificado":
                colorResId = R.color.colorActivo;
                break;
            case "Rechazado":
                colorResId = R.color.colorInactivo;
                break;
            case "Aceptado":
                colorResId = R.color.colorFinalizado;
                break;
            case "Aceptado_empresario":
                colorResId = R.color.colorActivo;
                break;
            case "Entregado":
                colorResId = R.color.colorActivo;
                break;
            case "No_recibido":
                colorResId = R.color.colorInactivo;
                break;
            case "Recibido":
                colorResId = R.color.colorFinalizado;
                break;
            case "Finalizado":
                colorResId = R.color.colorInactivo;
                break;
            case "Solicitud":
                colorResId = R.color.colorSolicitud;
                break;
            case "Entregado_empresario":
                colorResId = R.color.colorFinalizado;
                break;
            case "Entregado_usuario":
                colorResId = R.color.colorFinalizado;
                break;
            default:
                colorResId = android.R.color.white; // Color transparente por defecto
                break;
        }
        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, colorResId));

        if (alquiler.getEstado_pedido().equals("Aceptado") || alquiler.getEstado_pedido().equals("Modificado") || alquiler.getEstado_pedido().equals("Solicitud") || alquiler.getEstado_pedido().equals("Aceptado_empresario") || alquiler.getEstado_pedido().equals("Rechazado") || alquiler.getEstado_pedido().equals("Finalizado") || alquiler.getEstado_pedido().equals("Entregado_usuario")) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Context context = view.getContext();
                    Intent intent = new Intent(context, InfoAlquilerSolicitado.class);
                    intent.putExtra("ALQUILER_ID", alquiler.getId());
                    intent.putExtra("ESTADO_PEDIDO", alquiler.getEstado_pedido());
                    context.startActivity(intent);
                }
            });



        } else {
            if(alquiler.getEstado_pedido().equals("Entregado_empresario")){
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Context context = view.getContext();
                        Intent intent = new Intent(context, AceptarAlquiler.class);
                        intent.putExtra("ALQUILER_ID", alquiler.getId());
                        intent.putExtra("ESTADO_PEDIDO", alquiler.getEstado_pedido());
                        context.startActivity(intent);
                    }
                });
            } else if (alquiler.getEstado_pedido().equals("Recibido")) {
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Context context = view.getContext();
                        Intent intent = new Intent(context, EntregarAlquilerUser.class);
                        intent.putExtra("ALQUILER_ID", alquiler.getId());
                        intent.putExtra("ESTADO_PEDIDO", alquiler.getEstado_pedido());
                        context.startActivity(intent);
                    }
                });

            }else
            {

                holder.cardView.setOnClickListener(null);

            }

        }



    }

    @Override
    public int getItemCount() {
        return alquilers != null ? alquilers.size() : 0;
    }

    public void setOnItemClickListener(AlquileresAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Alquiler alquiler);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtFechaAlquiler;

        TextView txtFechaDevolucion;
        TextView txtEstadoPedido;
        TextView txtPrecioAlquiler;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            txtFechaAlquiler = itemView.findViewById(R.id.txtFechaAlquiler);
            txtFechaDevolucion = itemView.findViewById(R.id.txtFechaDevolucion);
            txtEstadoPedido = itemView.findViewById(R.id.txtEstadoPedido);
            txtPrecioAlquiler = itemView.findViewById(R.id.txtPrecioAlquiler);
            cardView = itemView.findViewById(R.id.cardView); // Reemplaza con el ID de tu CardView

        }
    }

}
