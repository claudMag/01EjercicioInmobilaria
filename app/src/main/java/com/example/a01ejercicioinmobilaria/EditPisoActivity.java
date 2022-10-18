package com.example.a01ejercicioinmobilaria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.a01ejercicioinmobilaria.configuraciones.Constantes;
import com.example.a01ejercicioinmobilaria.databinding.ActivityEditPisoBinding;
import com.example.a01ejercicioinmobilaria.modelos.Piso;

public class EditPisoActivity extends AppCompatActivity {

    private ActivityEditPisoBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditPisoBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        //FASE 1 --> Obtener el objeto a editar
        Intent intentMain = getIntent();
        Bundle bundleMain = intentMain.getExtras();
        Piso pisoEdit = (Piso) bundleMain.getSerializable(Constantes.PISO);
        Log.d("OBJETO", pisoEdit.toString());

        //FASE 2 --> mostrar los elementos en la interfaz
        binding.txtDireccionEditPiso.setText(pisoEdit.getDireccion());
        binding.txtNumeroEditPiso.setText(String.valueOf(pisoEdit.getNumero()));
        binding.txtCiudadEditPiso.setText(pisoEdit.getCiudad());
        binding.txtCpEditPiso.setText(pisoEdit.getCp());
        binding.rbEditPiso.setRating(pisoEdit.getValoracion());


        binding.btnDeleteEditPiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int posicion = bundleMain.getInt(Constantes.POSICION);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(Constantes.POSICION, posicion);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();

            }
        });

        binding.btnUpdateEditPiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Piso pisoUpdated = crearPiso();
                if (pisoUpdated != null){
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    int posicion = bundleMain.getInt(Constantes.POSICION);
                    bundle.putInt(Constantes.POSICION, posicion);
                    bundle.putSerializable(Constantes.PISO, pisoUpdated);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });


    }

    private Piso crearPiso() {

        //crear un nuevo objeto y mandar eso.

        if (
                binding.txtDireccionEditPiso.getText().toString().isEmpty()
                        || binding.txtNumeroEditPiso.getText().toString().isEmpty()
                        || binding.txtCiudadEditPiso.getText().toString().isEmpty()
                        || binding.txtProvinciaEditPiso.getText().toString().isEmpty()
                        || binding.txtCpEditPiso.getText().toString().isEmpty()

        )
            return null;

        float valoracion = binding.rbEditPiso.getRating();
        if (valoracion == 0){
            return null;
        }

        return new Piso(binding.txtDireccionEditPiso.getText().toString(),
                Integer.parseInt(binding.txtNumeroEditPiso.getText().toString()),
                binding.txtCiudadEditPiso.getText().toString(),
                binding.txtProvinciaEditPiso.getText().toString(),
                binding.txtCpEditPiso.getText().toString(), valoracion);
    }
}