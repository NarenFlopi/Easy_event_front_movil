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

import com.example.easy_event_app.EditUser;
import com.example.easy_event_app.InfoProducto;
import com.example.easy_event_app.Productos;
import com.example.easy_event_app.R;
import com.example.easy_event_app.model.Producto;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ViewHolder> {

    private List<Producto> productos;
    private Context context;

    private ProductoAdapter.OnItemClickListener onItemClickListener;

    private NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
    public ProductosAdapter(List<Producto> productos, Context context) {
        this.productos = productos;
        this.context = context;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
        notifyDataSetChanged(); // Notificar al adaptador sobre los cambios en los datos
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_productos_id, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.txtNombreProducto.setText(producto.getNombre_producto());
        holder.txtCantidad.setText(String.valueOf(producto.getCantidad_disponible()));
        holder.txtPrecio.setText(String.valueOf(producto.getPrecio()));
        holder.txtEmpresa.setText(producto.getNombre_empresa());

        String ruta = "http://easyevent.api.adsocidm.com/storage/"+producto.getFoto();
        Picasso.with(context).load(ruta).into(holder.imageView);

        double precioProducto = producto.getPrecio();
        format.setMaximumFractionDigits(0); // Configurar para no mostrar decimale
        String precioFormateado = format.format(precioProducto);

        String precioConMoneda = precioFormateado + " COP";

        holder.txtPrecio.setText(precioConMoneda);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = view.getContext();
                Intent intent = new Intent(context, InfoProducto.class);
                intent.putExtra("PRODUCTO_ID", producto.getId());
                context.startActivity(intent);
            }
        });


        // Añadir más asignaciones según los atributos de tu modelo Producto si es necesario
    }

    @Override
    public int getItemCount() {
        return productos != null ? productos.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombreProducto;

        ImageView imageView;
        TextView txtPrecio;

        TextView txtEmpresa;

        TextView txtCantidad;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombreProducto = itemView.findViewById(R.id.txtNombreProducto);
            imageView = itemView.findViewById(R.id.imageView);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
            txtEmpresa = itemView.findViewById(R.id.txtEmpresa);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            cardView = itemView.findViewById(R.id.cardView2);// Reemplaza con el ID de tu TextView
            // Añadir más inicializaciones según los atributos de tu modelo Producto si es necesario
        }
    }


}
