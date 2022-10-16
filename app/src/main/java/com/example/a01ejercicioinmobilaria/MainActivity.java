package com.example.a01ejercicioinmobilaria;

import android.content.Intent;
import android.os.Bundle;

import com.example.a01ejercicioinmobilaria.modelos.Piso;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;

import com.example.a01ejercicioinmobilaria.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private ActivityResultLauncher<Intent> launcherAddPiso;

    private ArrayList<Piso> listaPisos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listaPisos = new ArrayList<>();

        inicializaLaunchers();
        mostrarPisos();


        setSupportActionBar(binding.toolbar);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcherAddPiso.launch(new Intent(MainActivity.this, AddPisoActivity.class));
            }
        });
    }

    private void mostrarPisos() {
        binding.content.contenedorMain.removeAllViews();

        for (Piso p : listaPisos) {
            //construyo un layout para cada fila de piso
            View pisoView = LayoutInflater.from(MainActivity.this).inflate(R.layout.piso_model_view, null);
            //ahora los elementos de piso que quiero mostrar.
            //SOLO TIENE QUE APARECER en la fila; direccion, num,ciudad, rating
            TextView lblDireccion = pisoView.findViewById(R.id.lblDireccionPisoModelView);
            TextView lblNumero = findViewById(R.id.lblNumeroPisoModelView);
            TextView lblCiudad = findViewById(R.id.lblCiudadPisoModelView);
            RatingBar ratingBar = findViewById(R.id.ratingBarPisoModelView);

            lblDireccion.setText(p.getDireccion());
            lblNumero.setText(String.valueOf(p.getNumero()));
            lblCiudad.setText(p.getCiudad());
            ratingBar.setRating(p.getValoracion());

            binding.content.contenedorMain.addView(pisoView);


        }
    }

    private void inicializaLaunchers() {

        launcherAddPiso = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            if (result.getData() != null && result.getData().getExtras() != null) {
                                Piso alumno = (Piso) result.getData().getExtras().getSerializable("PISO");
                                listaPisos.add(alumno);
                                mostrarPisos();
                            }
                        }
                    }
                });
    }
}