package com.example.easy_event_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder>{


    private List<Producto> productos;
    private Context context;
    private OnItemClickListener onItemClickListener; // Nueva línea
    private NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));


    public ProductoAdapter(List<Producto> productos, Context context) {
        this.productos = productos;
        this.context = context;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
        notifyDataSetChanged();
    }

    @Override
    public ProductoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false);
        return new ProductoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductoAdapter.ViewHolder holder, int position) {
        Producto producto = this.productos.get(position);
        holder.txtNombreProducto.setText(producto.getNombre_producto());
        holder.txtCantidad.setText(String.valueOf((producto.getCantidad_disponible())));
        holder.txtEmpresa.setText(producto.getNombre_empresa());

        String ruta = "http://easyevent.api.adsocidm.com/storage/"+producto.getFoto();
        Picasso.with(context).load(ruta).into(holder.imageView);

        double precioProducto = producto.getPrecio();
        format.setMaximumFractionDigits(0); // Configurar para no mostrar decimale
        String precioFormateado = format.format(precioProducto);

        String precioConMoneda = precioFormateado + " COP";

        holder.txtPrecio.setText(precioConMoneda);

        // Establecer un OnClickListener para el CardView
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = view.getContext();
                Intent intent = new Intent(context, InfoProducto.class);
                intent.putExtra("PRODUCTO_ID", producto.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productos != null ? productos.size() : 0;
    }

    public void setOnItemClickListener(ProductoAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Producto producto);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombreProducto;
        TextView txtPrecio;
        TextView txtEmpresa;
        TextView txtCantidad;
        ImageView imageView;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNombreProducto = itemView.findViewById(R.id.txtNombreProducto);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
            txtEmpresa = itemView.findViewById(R.id.txtEmpresa);
            imageView = itemView.findViewById(R.id.imageView);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            cardView = itemView.findViewById(R.id.cardView2); // Reemplaza con el ID de tu CardView
        }
    }

}
