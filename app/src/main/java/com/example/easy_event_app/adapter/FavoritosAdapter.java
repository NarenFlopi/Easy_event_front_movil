package com.example.easy_event_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easy_event_app.InfoProducto;
import com.example.easy_event_app.R;
import com.example.easy_event_app.model.Favorito;
import com.example.easy_event_app.model.Producto;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.ViewHolder>{


    private List<Favorito> favoritos;
    private Context context;

    private NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
    public FavoritosAdapter(List<Favorito> favoritos, Context context) {
        this.favoritos = favoritos;
        this.context = context;
    }

    public void setProductos(List<Favorito> favoritos) {
        this.favoritos = favoritos;
        notifyDataSetChanged(); // Notificar al adaptador sobre los cambios en los datos
    }

    @NonNull
    @Override
    public FavoritosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favoritos, parent, false);
        return new FavoritosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritosAdapter.ViewHolder holder, int position) {
        Favorito favorito = favoritos.get(position);

        holder.txtNombreProducto.setText(favorito.getProducto().getNombre_producto());
        holder.txtCantidad.setText(String.valueOf(favorito.getProducto().getCantidad_disponible()));
        holder.txtPrecio.setText(String.valueOf(favorito.getProducto().getPrecio()));

        String ruta = "http://easyevent.api.adsocidm.com/storage/"+favorito.getProducto().getFoto();
        Picasso.with(context).load(ruta).into(holder.imageView);

        double precioProducto = favorito.getProducto().getPrecio();
        format.setMaximumFractionDigits(0); // Configurar para no mostrar decimale
        String precioFormateado = format.format(precioProducto);

        String precioConMoneda = precioFormateado + " COP";

        holder.txtPrecio.setText(precioConMoneda);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = view.getContext();
                Intent intent = new Intent(context, InfoProducto.class);
                intent.putExtra("PRODUCTO_ID", favorito.getProducto().getId());
                context.startActivity(intent);
            }
        });


        // Añadir más asignaciones según los atributos de tu modelo Producto si es necesario
    }

    @Override
    public int getItemCount() {
        return favoritos != null ? favoritos.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        TextView txtNombreProducto;

        ImageView imageView;
        TextView txtPrecio;

        TextView txtCantidad;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNombreProducto = itemView.findViewById(R.id.txtNombreProducto);
            imageView = itemView.findViewById(R.id.imageView);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            cardView = itemView.findViewById(R.id.cardView2);
        }
    }


}
