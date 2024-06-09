package com.example.easy_event_app;

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
import android.widget.TextView;

import com.example.easy_event_app.adapter.AlquilerAdapter;
import com.example.easy_event_app.adapter.ProductoAdapter;
import com.example.easy_event_app.model.Alquiler;
import com.example.easy_event_app.model.AlquilerRespuesta;
import com.example.easy_event_app.model.Producto;
import com.example.easy_event_app.network.AlquilerAPICliente;
import com.example.easy_event_app.network.AlquilerAPIService;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlquilerEmpresarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlquilerEmpresarioFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SwipeRefreshLayout refresh;
    private AlquilerAPIService servicio;
    private AlquilerAdapter alquilerAdapter;
    private List<Alquiler> alquilereslista;
    private RecyclerView alquilerRecyclerView;
    private TextView nada;




    public AlquilerEmpresarioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlquilerEmpresarioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlquilerEmpresarioFragment newInstance(String param1, String param2) {
        AlquilerEmpresarioFragment fragment = new AlquilerEmpresarioFragment();
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
    public void onStart() {
        super.onStart();
        cargardatos();

    }

    public void cargardatos(){
        servicio.alquileres(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token()).enqueue(new Callback<AlquilerRespuesta>() {
            @Override
            public void onResponse(Call<AlquilerRespuesta> call, Response<AlquilerRespuesta> response) {
                if (response.isSuccessful()) {
                    List<Alquiler> todosLosAlquileres = response.body().getAlquileres();
                    Log.i("alquilerestodos", response.body().toString());

                    Collections.sort(todosLosAlquileres, new Comparator<Alquiler>() {
                        @Override
                        public int compare(Alquiler alquiler1, Alquiler alquiler2) {
                            return Long.compare(alquiler2.getId(), alquiler1.getId());
                        }
                    });

                    alquilereslista = todosLosAlquileres;
                    cargarListaAlquileres(alquilereslista);
                }
            }

            @Override
            public void onFailure(Call<AlquilerRespuesta> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

    private void cargarListaAlquileres(List<Alquiler> data) {

        Log.i("alquileres0", data.toString());

        if (data.size() <= 0 ){
            nada = getActivity().findViewById(R.id.nada);
            nada.setVisibility(View.VISIBLE);
        } else {
            nada = getActivity().findViewById(R.id.nada);
            nada.setVisibility(View.GONE);
        }

        alquilerAdapter = new AlquilerAdapter(data, getActivity());
        alquilerRecyclerView.setAdapter(alquilerAdapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alquiler_empresario, container, false);
        refresh = view.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(this);
        alquilerRecyclerView = view.findViewById(R.id.listaAlquiler);
        alquilerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        //alquilerAdapter = new AlquilerAdapter(alquilereslista, getActivity());
        //alquilerRecyclerView.setAdapter(alquilerAdapter);


        return view;
    }


    @Override
    public void onRefresh() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(false);
                cargardatos();

            }
        }, 100);

    }
}