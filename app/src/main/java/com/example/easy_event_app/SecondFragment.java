package com.example.easy_event_app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easy_event_app.adapter.AlquileresAdapter;
import com.example.easy_event_app.adapter.CategoriaAdapter;
import com.example.easy_event_app.adapter.ProductosAdapter;
import com.example.easy_event_app.model.Alquiler;
import com.example.easy_event_app.model.AlquilerRespuesta;
import com.example.easy_event_app.model.Categoria;
import com.example.easy_event_app.model.CategoriaRespuesta;
import com.example.easy_event_app.model.FavoritoRespuesta;
import com.example.easy_event_app.model.Producto;
import com.example.easy_event_app.network.AlquilerAPICliente;
import com.example.easy_event_app.network.AlquilerAPIService;
import com.example.easy_event_app.network.CategoriaAPICliente;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment implements AlquileresAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AlquilerAPIService servicio;
    private AlquileresAdapter alquileresAdapter;
    private List<Alquiler> listaAlquiler;
    private RecyclerView alquilerRecyclerView;
    private SwipeRefreshLayout refresh;


    public SecondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondFragment newInstance(String param1, String param2) {
        SecondFragment fragment = new SecondFragment();
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
        servicio = AlquilerAPICliente.getAlquilerService();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        refresh = view.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(this);

        alquilerRecyclerView = view.findViewById(R.id.lista1);
        alquilerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        alquileresAdapter = new AlquileresAdapter(listaAlquiler, getActivity());
        alquileresAdapter.setOnItemClickListener(this);
        alquilerRecyclerView.setAdapter(alquileresAdapter);

        return view;
    }

    @Override
    public void onRefresh() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(false);
                cargarAlquileres();

            }
        }, 100);
    }

    @Override
    public void onItemClick(Alquiler alquiler) {
        Toast.makeText(getContext(), "Clic en la categoría: " + alquiler.getId(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), InfoAlquilerSolicitado.class);
        intent.putExtra("ALQUILER_ID", alquiler.getId());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        LinearLayout firstLinearLayout = getView().findViewById(R.id.firstLinearLayout);
        LinearLayout secondLinearLayout = getView().findViewById(R.id.secondLinearLayout);
        //TextView textViewEmpty = getView().findViewById(R.id.textViewEmpty);
        //RecyclerView recyclerView = getView().findViewById(R.id.lista1);

        // Configura la animación de desplazamiento desde la izquierda hacia la derecha
        ObjectAnimator animator = ObjectAnimator.ofFloat(secondLinearLayout, "translationX", 0f);
        animator.setDuration(1000);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                secondLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        animator.start();

        /*if (recyclerView.getAdapter() != null && recyclerView.getAdapter().getItemCount() == 0) {
            textViewEmpty.setVisibility(View.VISIBLE);
        } else {
            textViewEmpty.setVisibility(View.GONE);
        }*/
    }


    @Override
    public void onStart() {
        super.onStart();

        cargarAlquileres();

    }

    private void cargarAlquileres(){
        servicio.alquileres(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token()).enqueue(new Callback<AlquilerRespuesta>() {
            @Override
            public void onResponse(Call<AlquilerRespuesta> call, Response<AlquilerRespuesta> response) {
                if (response.isSuccessful()) {
                    List<Alquiler> todosLosAlquilers = response.body().getAlquileres();
                    // Sort the list in descending order based on the 'fecha_alquiler' field
                    Collections.sort(todosLosAlquilers, new Comparator<Alquiler>() {
                        @Override
                        public int compare(Alquiler alquiler1, Alquiler alquiler2) {
                            return Long.compare(alquiler2.getId(), alquiler1.getId());
                        }
                    });
                    listaAlquiler = todosLosAlquilers;
                    cargarListaAlquilers(listaAlquiler);
                } else {
                    Log.e("Error", "Respuesta no exitosa. Código: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<AlquilerRespuesta> call, Throwable t) {
                Log.e("Error", "Mensaje de error: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }


    private void cargarListaAlquilers(List<Alquiler> data) {

        TextView textViewEmpty = getActivity().findViewById(R.id.textViewEmpty);

        if (data.isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
            alquilerRecyclerView.setVisibility(View.GONE);
        } else {
            textViewEmpty.setVisibility(View.GONE);
            alquilerRecyclerView.setVisibility(View.VISIBLE);
        }
            Log.i("alquileres", data.toString());
            alquileresAdapter = new AlquileresAdapter(data, getActivity());
            alquilerRecyclerView.setAdapter(alquileresAdapter);


    }
}