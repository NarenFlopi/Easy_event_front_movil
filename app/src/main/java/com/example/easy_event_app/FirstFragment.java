package com.example.easy_event_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.easy_event_app.adapter.CategoriaAdapter;
import com.example.easy_event_app.adapter.ProductoAdapter;
import com.example.easy_event_app.model.Categoria;
import com.example.easy_event_app.model.CategoriaRespuesta;
import com.example.easy_event_app.model.Producto;
import com.example.easy_event_app.model.ProductoRespuesta;
import com.example.easy_event_app.network.CategoriaAPICliente;
import com.example.easy_event_app.network.CategoriaAPIService;
import com.example.easy_event_app.network.ProductoAPICliente;
import com.example.easy_event_app.network.ProductoAPIService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class FirstFragment extends Fragment implements CategoriaAdapter.OnItemClickListener, ProductoAdapter.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CategoriaAPIService servicio;
    private ProductoAPIService servicios;

    private CategoriaAdapter categoriaAdapter;
    private ProductoAdapter productoAdapter;

    private List<Categoria> primerosTresItems;
    private List<Producto> primerosCincoItems;

    private RecyclerView categoriaRecyclerView;
    private RecyclerView productoRecyclerView;

    public FirstFragment() {

        }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        servicio = CategoriaAPICliente.getCategoriaService();
        servicios = ProductoAPICliente.getProductoService();

    }

    public void cargarproductos () {
        servicios.productos(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token()).enqueue(new Callback<ProductoRespuesta>() {
            @Override
            public void onResponse(Call<ProductoRespuesta> call, Response<ProductoRespuesta> response) {
                if (response.isSuccessful()) {
                    List<Producto> todosLosProductos = response.body().getProducto();

                    Collections.sort(todosLosProductos, new Comparator<Producto>() {
                        @Override
                        public int compare(Producto producto1, Producto producto2) {
                            return Long.compare(producto2.getId(), producto1.getId());
                        }
                    });


                    primerosCincoItems = todosLosProductos.subList(0, Math.min(todosLosProductos.size(), 5));
                    cargarListaProductos(primerosCincoItems);
                }
            }

            @Override
            public void onFailure(Call<ProductoRespuesta> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        cargarproductos();

        servicio.categorias(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token()).enqueue(new Callback<CategoriaRespuesta>() {
            @Override
            public void onResponse(Call<CategoriaRespuesta> call, Response<CategoriaRespuesta> response) {
                if (response.isSuccessful()) {
                    List<Categoria> todasLasCategorias = response.body().getCategoria();
                    primerosTresItems = todasLasCategorias.subList(0, Math.min(todasLasCategorias.size(), 5));
                    cargarListaCategorias(primerosTresItems);
                }
            }

            @Override
            public void onFailure(Call<CategoriaRespuesta> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });



        SearchView searchView = getView().findViewById(R.id.searchView);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                cargarproductos();
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                realizarBusqueda(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }




    private void realizarBusqueda(String query) {

        servicios.searchProductos(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token(), query)
                .enqueue(new Callback<ProductoRespuesta>() {
                    @Override
                    public void onResponse(Call<ProductoRespuesta> call, Response<ProductoRespuesta> response) {
                        if (response.isSuccessful()) {
                            List<Producto> resultadosDeLaBusqueda = response.body().getProducto();
                            // Actualizar el RecyclerView con los resultados de la búsqueda
                            productoAdapter.setProductos(resultadosDeLaBusqueda);
                            productoAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("Error", "Error en la respuesta del servidor");
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductoRespuesta> call, Throwable t) {
                        Log.e("Error", t.getMessage());
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);


        categoriaRecyclerView = view.findViewById(R.id.lista);
        categoriaRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        productoRecyclerView = view.findViewById(R.id.lista1);
        productoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        categoriaAdapter = new CategoriaAdapter(primerosTresItems, getActivity());
        categoriaAdapter.setOnItemClickListener(this);
        categoriaRecyclerView.setAdapter(categoriaAdapter);

        productoAdapter = new ProductoAdapter(primerosCincoItems, getActivity());
        productoAdapter.setOnItemClickListener(this);
        productoRecyclerView.setAdapter(productoAdapter);

        Button button = view.findViewById(R.id.categoria);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Catego(v);
            }
        });

        ImageButton imageButton = view.findViewById(R.id.notificacion);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notifi(v);
            }
        });



        return view;
    }

    public void Catego(View view) {

        Intent intent = new Intent(getActivity(), Categorias.class);
        startActivity(intent);
    }

    public void Notifi(View view) {

        Intent intent = new Intent(getActivity(), Notificacion.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(Categoria categoria) {
        Toast.makeText(getContext(), "Clic en la categoría: " + categoria.getNombre(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), Productos.class);
        intent.putExtra("CATEGORY_ID", categoria.getId()); // Suponiendo que existe un método getId() en tu modelo Categoria
        startActivity(intent);
    }

    @Override
    public void onItemClick(Producto producto) {
        Toast.makeText(getContext(), "Clic en el producto: " + producto.getNombre_producto(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), InfoProducto.class);
        intent.putExtra("PRODUCTO_ID", producto.getId()); // Suponiendo que existe un método getId() en tu modelo Categoria
        startActivity(intent);
    }

    private void cargarListaCategorias(List<Categoria> data) {
        List<Categoria> primerosTresItems = data.subList(0, Math.min(data.size(), 5));
        categoriaAdapter = new CategoriaAdapter(primerosTresItems, getActivity());
        categoriaRecyclerView.setAdapter(categoriaAdapter);
    }

    private void cargarListaProductos(List<Producto> data) {
        List<Producto> primerosCincoItems = data.subList(0, Math.min(data.size(), 5));
        productoAdapter = new ProductoAdapter(primerosCincoItems, getActivity());
        productoRecyclerView.setAdapter(productoAdapter);
    }
}

