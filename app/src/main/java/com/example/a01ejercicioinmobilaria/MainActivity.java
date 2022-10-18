package com.example.a01ejercicioinmobilaria;

import android.content.Intent;
import android.os.Bundle;

import com.example.a01ejercicioinmobilaria.configuraciones.Constantes;
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
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private ActivityResultLauncher<Intent> launcherAddPiso;

    private ActivityResultLauncher<Intent> launcherEditPiso;

    private ArrayList<Piso> listaPisos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listaPisos = new ArrayList<>();

        inicializaLaunchers();

        setSupportActionBar(binding.toolbar);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //lanzar la creación de un nuevo objeto
                launcherAddPiso.launch(new Intent(MainActivity.this, AddPisoActivity.class));
            }
        });

    }

    private void mostrarPisos() {
        binding.contentMain.contenedorMain.removeAllViews();

        for (int i = 0; i < listaPisos.size(); i++) {
            Piso p = listaPisos.get(i);
            //construyo un layout para cada fila de piso
            View pisoView = LayoutInflater.from(MainActivity.this).inflate(R.layout.piso_model_view, null);
            //ahora los elementos de piso que quiero mostrar.
            //SOLO TIENE QUE APARECER en la fila; direccion, num,ciudad, rating
            TextView lblDireccion = pisoView.findViewById(R.id.lblDireccionPisoModelView);
            TextView lblNumero = pisoView.findViewById(R.id.lblNumeroPisoModelView);
            TextView lblCiudad = pisoView.findViewById(R.id.lblCiudadPisoModelView);
            RatingBar ratingBar = pisoView.findViewById(R.id.ratingBarPisoModelView);

            int finalI = i;
            pisoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, EditPisoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constantes.PISO, p);
                    bundle.putInt(Constantes.POSICION, finalI);
                    intent.putExtras(bundle);
                    launcherEditPiso.launch(intent);
                }
            });

            lblDireccion.setText(p.getDireccion());
            lblNumero.setText(String.valueOf(p.getNumero()));
            lblCiudad.setText(p.getCiudad());
            ratingBar.setRating(p.getValoracion());

            binding.contentMain.contenedorMain.addView(pisoView);
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
                                if (result.getData().getExtras().getSerializable(Constantes.PISO) != null) {
                                    Piso piso = (Piso) result.getData().getExtras().getSerializable(Constantes.PISO);
                                    listaPisos.add(piso);
                                    mostrarPisos();
                                } else {
                                    Toast.makeText(MainActivity.this, "No hay datos", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "NO HAY INTENT O BUNDLE", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Ventana cancelada", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //en el launcher edit para saber si es un delete o un update habria podido tambien:
        //un bundle con un tag, el numero id del boton al que le han hecho click
        //pero como con el delete no hemos creado ningun objeto, solo recogido la accion, lo resuelvo asi:

        launcherEditPiso = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            if (result.getData() != null && result.getData().getExtras() != null) {
                                if (result.getData().getExtras().getSerializable(Constantes.PISO) != null) { //en este caso es un UPDATE!
                                    Piso piso = (Piso) result.getData().getExtras().getSerializable(Constantes.PISO);
                                    int posicion = result.getData().getExtras().getInt(Constantes.POSICION);
                                    listaPisos.set(posicion, piso);
                                    mostrarPisos();
                                } else {
                                    //si no existe el piso, delete.
                                    if (result.getData().getExtras() != null){
                                        int posicion = result.getData().getExtras().getInt(Constantes.POSICION);
                                        listaPisos.remove(posicion);
                                        mostrarPisos();
                                    }
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "NO HAY INTENT O BUNDLE", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Ventana cancelada", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}