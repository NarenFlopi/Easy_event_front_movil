package com.example.easy_event_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.easy_event_app.network.LoginAPICliente;
import com.example.easy_event_app.network.LoginAPIService;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FourthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FourthFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView nombre;

    private ImageView imageView;

    private SwipeRefreshLayout refresh;

    public FourthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FourthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FourthFragment newInstance(String param1, String param2) {
        FourthFragment fragment = new FourthFragment();
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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fourth, container, false);

        refresh = view.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(this);

        CardView cardView = view.findViewById(R.id.cardView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditarUsuario(v);
            }
        });



        CardView cardView5 = view.findViewById(R.id.cardView5);

        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Favoritos(v); }
        });



        CardView cardViewCerrarSesion = view.findViewById(R.id.cardView4);

        cardViewCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
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
            }
        }, 100);
    }

    private void cerrarSesion() {
        String authToken = Datainfo.resultLogin.getAccess_token();
        Log.i( "msg", authToken);

        if (!TextUtils.isEmpty(authToken)) {

            LoginAPIService loginAPIService = LoginAPICliente.getLoginService();

            Call<Void> call = loginAPIService.logout("Bearer " + authToken);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    int responseCode = response.code(); //Obtener codigo de respuesta
                    Log.i("Logout", "codigo de respuesta" + responseCode);

                    if (response.isSuccessful()) {
                        Log.i("Bearer:", Datainfo.resultLogin.getAccess_token());
                        Datainfo.resultLogin=null;
                        volverLogin();

                    } else {
                        cerrarfail();
                    }
                }

                public void onFailure(Call<Void> call, Throwable t) {
                }
            });
        }
    }

    private void cerrarfail() {
        Toast.makeText(requireContext(), "Error al cerrar sesion.", Toast.LENGTH_SHORT).show();
    }

    private void volverLogin() {
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }


    public void EditarUsuario(View view) {
        startActivity(new Intent(requireActivity(), EditUser.class));
    }

    public void Favoritos(View view) {
        startActivity(new Intent(requireActivity(), Favoritos.class));
    }

    @Override
    public void onStart () {
        nombre = getActivity().findViewById(R.id.nombre);
        nombre.setText(Datainfo.resultLogin.getUser().getNombre());

        imageView = getView().findViewById(R.id.imageView);

        String ruta = "http://easyevent.api.adsocidm.com/storage/" + Datainfo.resultLogin.getUser().getFoto();
        Picasso.with(imageView.getContext()).load(ruta).into(imageView);

        super.onStart();
    }


}