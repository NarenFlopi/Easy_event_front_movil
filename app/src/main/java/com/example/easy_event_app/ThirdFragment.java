package com.example.easy_event_app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easy_event_app.adapter.AlquileresAdapter;
import com.example.easy_event_app.adapter.CarritoAdapter;
import com.example.easy_event_app.adapter.ProductoAdapter;
import com.example.easy_event_app.adapter.ProductosAdapter;
import com.example.easy_event_app.model.Alquiler;
import com.example.easy_event_app.model.AlquilerRespuesta;
import com.example.easy_event_app.model.Producto;
import com.example.easy_event_app.model.ProductoRespuesta;
import com.example.easy_event_app.network.AlquilerAPICliente;
import com.example.easy_event_app.network.AlquilerAPIService;
import com.example.easy_event_app.network.ProductoAPICliente;
import com.example.easy_event_app.network.ProductoAPIService;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThirdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThirdFragment extends Fragment implements ProductoAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AlquilerAPIService servicios;
    private CarritoAdapter carritoAdapter;
    private Long alquiler_id;
    private String precio_alquiler;
    private List<Producto> productosLista;
    private RecyclerView recyclerViewProductos;
    private SwipeRefreshLayout refresh;
    TextView nada2;

    public ThirdFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThirdFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThirdFragment newInstance(String param1, String param2) {
        ThirdFragment fragment = new ThirdFragment();
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
        servicios = AlquilerAPICliente.getAlquilerService();
    }

    private void mostrarPedirActivity() {
        Intent intent = new Intent(getActivity(), Pedir.class);
        intent.putExtra("ALQUILER_ID",  alquiler_id);
        long totalAlquiler = Long.parseLong(precio_alquiler);
        intent.putExtra("TOTAL_ALQUILER", totalAlquiler);
        startActivity(intent);
    }

    @Override
    public void onItemClick(com.example.easy_event_app.model.Producto producto) {
        Toast.makeText(getContext(), "Clic en el producto: " + producto.getNombre_producto(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), InfoProducto.class);
        intent.putExtra("PRODUCTO_ID", producto.getId()); // Suponiendo que existe un método getId() en tu modelo Categoria
        startActivity(intent);
    }

    @Override
    public void onStart() {

        cargarProductos();
        super.onStart();

    }

    @Override
    public void onResume() {

        LinearLayout firstLinearLayout = getView().findViewById(R.id.firstLinearLayout);
        LinearLayout secondLinearLayout = getView().findViewById(R.id.secondLinearLayout);

        ObjectAnimator animator = ObjectAnimator.ofFloat(secondLinearLayout, "translationX", 0f);
        animator.setDuration(1000);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                secondLinearLayout.setVisibility(View.VISIBLE);
            }
        });
        cargarProductos();
        animator.start();
        super.onResume();

    }



    public void cargarProductos(){
        String estado = "carrito";
        servicios.carrito(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token(),estado).enqueue(new Callback<ProductoRespuesta>() {
            @Override
            public void onResponse(Call<ProductoRespuesta> call, Response<ProductoRespuesta> response) {
                if (response.isSuccessful()) {

                    List<com.example.easy_event_app.model.Producto> todosLosProductos = response.body().getProducto();
                    alquiler_id = response.body().getAlquiler_id();
                    precio_alquiler = response.body().getPrecio_alquiler();
                    productosLista = todosLosProductos;
                    cargarListaProductos(productosLista);
                }
            }

            @Override
            public void onFailure(Call<ProductoRespuesta> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }





    private void cargarListaProductos(List<Producto> data) {

        CardView pedir = getActivity().findViewById(R.id.pedir);

        if (data.size() <= 0 ){
            nada2 = getActivity().findViewById(R.id.nada2);
            nada2.setVisibility(View.VISIBLE);
            pedir.setVisibility(View.GONE);
        } else {
            nada2 = getActivity().findViewById(R.id.nada2);
            nada2.setVisibility(View.GONE);
            pedir.setVisibility(View.VISIBLE);
            TextView textViewTotal = getActivity().findViewById(R.id.total);
            double precioTotal = Double.parseDouble(precio_alquiler);
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
            format.setMaximumFractionDigits(0);
            String precioTotalFormateado = format.format(precioTotal);
            textViewTotal.setText(precioTotalFormateado);
        }

        Log.i("carrito", data.toString());
        carritoAdapter = new CarritoAdapter(data, getActivity(), this);
        recyclerViewProductos.setAdapter(carritoAdapter);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_third, container, false);

        refresh = view.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(this);

        recyclerViewProductos = view.findViewById(R.id.lista1);
        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        Button pedirButton = view.findViewById(R.id.pedirButton);
        pedirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarPedirActivity();
            }
        });

        return view;
    }

    @Override
    public void onRefresh() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(false);
                cargarProductos();

            }
        }, 100);
    }


}