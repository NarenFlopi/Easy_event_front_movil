package com.example.easy_event_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {
    //declaramos las 4 vistas
    FirstFragment firstFragment = new FirstFragment();
    SecondFragment secondFragment = new SecondFragment();
    ThirdFragment thirdFragment = new ThirdFragment();
    FourthFragment fourthFragment = new FourthFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//referencie el bottomnavigation y le pusimos un listener

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation_usuario);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(firstFragment);
    }

    /*
        private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    //aca se supone que cuando presione el primero, sera el primero, asi sucesivamente.
                    case R.id.firstFragment:
                        loadFragment(firstFragment);
                        return true;
                    case R.id.secondFragment:
                        loadFragment(secondFragment);
                        return true;
                    case R.id.thirdFragment:
                        loadFragment(thirdFragment);
                        return true;
                    case R.id.fourthFragment:
                        loadFragment(fourthFragment);
                        returtrue;

                }
                return false;
            }
     */

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.firstFragament) {
                loadFragment(firstFragment);
                return true;
            } else if (item.getItemId() == R.id.secondFragament) {
                loadFragment(secondFragment);
                return true;
            } else if (item.getItemId() == R.id.thirdFragament) {
                loadFragment(thirdFragment);
                return true;
            } else if (item.getItemId() == R.id.fourthFragament) {
                loadFragment(fourthFragment);
                return true;
            }

            return true;
        }
    };

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount()>=0){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

        //super.onBackPressed();

        // your code.
    }


    //aca llega el fragmento y reemplaza lo que se tenia por el nuevo fragmento para visualizarlo
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Oculta todos los fragmentos antes de mostrar el nuevo
        for (Fragment frag : getSupportFragmentManager().getFragments()) {
            transaction.hide(frag);
        }

        // Muestra el nuevo fragmento o lo agrega si aún no está presente
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.frame_container, fragment);
        }
        transaction.addToBackStack("actual");
        transaction.commit();
    }
}