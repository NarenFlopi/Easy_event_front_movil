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

import com.example.easy_event_app.Productos;
import com.example.easy_event_app.R;
import com.example.easy_event_app.model.Categoria;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.ViewHolder> {

    private List<Categoria> categorias;
    private Context context;

    private OnItemClickListener onItemClickListener; // Nueva línea

    public CategoriasAdapter(List<Categoria> categorias, Context context) {
        this.categorias = categorias;
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        // Implementa este método si es necesario para tu caso
        return position;
    }

    @Override
    public CategoriasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_todas_categorias, parent, false);
        return new CategoriasAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CategoriasAdapter.ViewHolder holder, int position) {
        Categoria categoria = categorias.get(position);
        holder.txtNombre.setText(categoria.getNombre());

        String ruta = "http://easyevent.api.adsocidm.com/storage/"+categoria.getFoto();
        Picasso.with(context).load(ruta).into(holder.imageView);

        // Establecer un OnClickListener para el CardView
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener el contexto desde el CardView y abrir la nueva actividad
                Context context = view.getContext();
                Intent intent = new Intent(context, Productos.class);
                intent.putExtra("CATEGORY_ID", categoria.getId());// Reemplaza "NuevaActividad" con el nombre de tu actividad
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return categorias != null ? categorias.size() : 0;
    }

    public void setOnItemClickListener(CategoriasAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    // Interfaz para manejar eventos de clic
    public interface OnItemClickListener {
        void onItemClick(Categoria categoria);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre;

        ImageView imageView;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            imageView = itemView.findViewById(R.id.imageView);
            cardView = itemView.findViewById(R.id.cardView); // Reemplaza con el ID de tu CardView
        }
    }

}