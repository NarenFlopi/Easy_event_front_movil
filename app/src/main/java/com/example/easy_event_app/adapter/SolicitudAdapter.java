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

import com.example.easy_event_app.R;
import com.example.easy_event_app.model.Producto;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class SolicitudAdapter extends RecyclerView.Adapter<SolicitudAdapter.ViewHolder>{

    private List<Producto> productos;
    private Context context;

    private OnItemClickListener onItemClickListener; // Nueva línea

    private NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));




    public SolicitudAdapter(List<Producto> productos, Context context) {
        this.productos = productos;
        this.context = context;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
        notifyDataSetChanged();
    }

    @Override
    public SolicitudAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_solicitud, parent, false);
        return new SolicitudAdapter.ViewHolder(view);
    }

    private void deleteItem(int pos) {
        this.productos.remove(pos);
        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(SolicitudAdapter.ViewHolder holder, int position) {
        Producto producto = this.productos.get(position);
        holder.txtNombreProducto.setText(producto.getNombre_producto());
        holder.txtPrecio.setText(String.valueOf(producto.getPrecio_producto_total()));
        holder.txtCantidad.setText(String.valueOf(producto.getCantidad_recibida()));


        String ruta = "http://easyevent.api.adsocidm.com/storage/"+producto.getFoto();
        Picasso.with(context).load(ruta).into(holder.imageView);

        double precioProducto = producto.getPrecio_producto_total();
        format.setMaximumFractionDigits(0); // Configurar para no mostrar decimale
        String precioFormateado = format.format(precioProducto);

        String precioConMoneda = precioFormateado + " COP";

        holder.txtPrecio.setText(precioConMoneda);

        // Establecer un OnClickListener para el CardView
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = view.getContext();
                Producto p = productos.get(position);
                p.setCantidad_recibida(Integer.parseInt(holder.txtCantidad.getText().toString()));

                //Intent intent = new Intent(context, );
                //intent.putExtra("PRODUCTO_ID", producto.getId());
                //context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productos != null ? productos.size() : 0;
    }


    public interface OnItemClickListener {
        void onItemClick(Producto producto);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombreProducto;
        TextView txtPrecio;
        ImageView imageView;
        CardView cardView;
        TextView txtCantidad;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNombreProducto = itemView.findViewById(R.id.txtNombreProducto);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
            imageView = itemView.findViewById(R.id.imageView);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);

            cardView = itemView.findViewById(R.id.cardView2); // Reemplaza con el ID de tu CardView
        }
    }

    public List<Producto> getListaProductos() {
        return this.productos;
    }

}
