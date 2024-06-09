package com.example.easy_event_app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.easy_event_app.Datainfo;
import com.example.easy_event_app.HomeEmpresario;
import com.example.easy_event_app.InfoProducto;
import com.example.easy_event_app.R;
import com.example.easy_event_app.ThirdFragment;
import com.example.easy_event_app.model.Producto;
import com.example.easy_event_app.network.AlquilerAPICliente;
import com.example.easy_event_app.network.AlquilerAPIService;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolder>{

    private List<Producto> productos;
    private Context context;

    private AlquilerAPIService servicios;

    private NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

    private ThirdFragment fragment; // Reference to the Fragment

    public CarritoAdapter(List<Producto> productos, Context context, ThirdFragment fragment) {
        this.productos = productos;
        this.context = context;
        this.fragment = fragment;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
        notifyDataSetChanged();
    }

    private void callFragmentMethod() {
        if (fragment != null) {
            fragment.cargarProductos(); // Call the Fragment method
        }
    }

    @Override
    public CarritoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_carrito, parent, false);
        servicios = AlquilerAPICliente.getAlquilerService();
        return new CarritoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CarritoAdapter.ViewHolder holder, int position) {
        Producto producto = this.productos.get(position);
        holder.txtNombreProducto.setText(producto.getNombre_producto());
        holder.txtPrecio.setText(String.valueOf(producto.getPrecio_producto_total()));
        holder.txtCantidad.setText(String.valueOf(producto.getCantidad_recibida()));

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = producto.getAlquiler_id()+"";

                String idProducto = producto.getId()+"";

                    servicios.delete(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token(), id, idProducto)
                            .enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        deleteItem(position);
                                        callFragmentMethod();
                                        /*SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
                                        mSwipeRefreshLayout.setRefreshing(true);*/


                                    } else {

                                        Log.e("Error", "Error al eliminar el producto: " + response.message());
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Log.e("Error", "Error al conectar con el servidor: " + t.getMessage());
                                }
                            });

            }
        });


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
                Intent intent = new Intent(context, InfoProducto.class);
                intent.putExtra("PRODUCTO_ID", producto.getId());
                intent.putExtra("CANTIDAD", producto.getCantidad_recibida());
                context.startActivity(intent);
            }
        });
    }

    private void deleteItem(int pos) {
        this.productos.remove(pos);
        this.notifyDataSetChanged();
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
        ImageButton delete;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNombreProducto = itemView.findViewById(R.id.txtNombreProducto);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
            imageView = itemView.findViewById(R.id.imageView);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            delete = itemView.findViewById(R.id.delete);

            cardView = itemView.findViewById(R.id.cardView2); // Reemplaza con el ID de tu CardView
        }
    }



}
