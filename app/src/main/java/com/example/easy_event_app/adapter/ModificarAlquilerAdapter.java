package com.example.easy_event_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

public class ModificarAlquilerAdapter extends RecyclerView.Adapter<ModificarAlquilerAdapter.ViewHolder> {

    private List<Producto> productos;
    private Context context;

    private OnItemClickListener onItemClickListener; // Nueva línea

    private NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));




    public ModificarAlquilerAdapter(List<Producto> productos, Context context) {
        this.productos = productos;
        this.context = context;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
        notifyDataSetChanged();
    }

    @Override
    public ModificarAlquilerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_modificar_alquiler_adapter, parent, false);
        return new ModificarAlquilerAdapter.ViewHolder(view);
    }

    private void deleteItem(int pos) {
        this.productos.remove(pos);
        this.notifyDataSetChanged();
    }



    @Override
    public void onBindViewHolder(ModificarAlquilerAdapter.ViewHolder holder, int position) {
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



                //Intent intent = new Intent(context, );
                //intent.putExtra("PRODUCTO_ID", producto.getId());
                //context.startActivity(intent);
            }
        });

        holder.botonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                int posicion = position;
                if(posicion == 0) {
                    posicion = 1;
                }else{
                    deleteItem(posicion);
                }


            }
        });

        holder.botonAdd.setOnClickListener(new View.OnClickListener() {
            long cantidad = producto.getCantidad_recibida();
            @Override
            public void onClick(View v) {
                String text = holder.txtCantidad.getText().toString();
                if(!text.isEmpty()){
                    int currentvalue = Integer.parseInt(text);
                    if(currentvalue < cantidad){
                        holder.txtCantidad.setText(String.valueOf(currentvalue + 1));
                        producto.setCantidad_recibida(producto.getCantidad_recibida()+1);
                    } else  {
                        holder.txtCantidad.setText(String.valueOf(producto.getCantidad_recibida()));
                    }


                } else {

                }

                actualizarPrecios( holder, position);


            }
        });


        holder.botonMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = holder.txtCantidad.getText().toString();
                if(!text.isEmpty()){
                    int currentvalue = Integer.parseInt(text);
                    if(currentvalue > 1){
                        holder.txtCantidad.setText(String.valueOf(currentvalue - 1));
                        producto.setCantidad_recibida(producto.getCantidad_recibida()-1);
                    } else  {
                        holder.txtCantidad.setText("1");
                    }

                } else {

                }

                actualizarPrecios( holder, position);


            }
        });

    }

    public void actualizarPrecios(ModificarAlquilerAdapter.ViewHolder holder, int position){

        Producto producto = this.productos.get(position);
        String precio_total = (String.valueOf(producto.getPrecio() * Integer.parseInt(holder.txtCantidad.getText().toString())));

        double precioProducto = Double.parseDouble(precio_total);
        format.setMaximumFractionDigits(0); // Configurar para no mostrar decimale
        String precioFormateado = format.format(precioProducto);
        String precioConMoneda = precioFormateado + " COP";
        holder.txtPrecio.setText(precioConMoneda);
        producto.setPrecio_producto_total(Long.parseLong(precio_total));



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
        ImageButton botonAdd, botonMenos, botonDelete;
        TextView txtCantidad;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNombreProducto = itemView.findViewById(R.id.txtNombreProducto);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
            imageView = itemView.findViewById(R.id.imageView);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            botonAdd = itemView.findViewById(R.id.botonAdd);
            botonMenos = itemView.findViewById(R.id.botonMenos);
            botonDelete = itemView.findViewById(R.id.botonDelete);


            cardView = itemView.findViewById(R.id.cardView2); // Reemplaza con el ID de tu CardView
        }
    }

    public List<Producto> getListaProductos() {
        return this.productos;
    }

}
