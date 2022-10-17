package com.example.a01ejercicioinmobilaria;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.a01ejercicioinmobilaria.configuraciones.Constantes;
import com.example.a01ejercicioinmobilaria.databinding.ActivityAddPisoBinding;
import com.example.a01ejercicioinmobilaria.modelos.Piso;

public class AddPisoActivity extends AppCompatActivity {


    private ActivityAddPisoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddPisoBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        binding.btnCancelarAddPiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        binding.btnAnyadirAddPiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Piso piso;

                if ((piso = crearPiso()) != null){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constantes.PISO, piso);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();

                }
                else{
                    Toast.makeText(AddPisoActivity.this, "FALTAN DATOS", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Piso crearPiso() {

        if (
                binding.txtDireccionAddPiso.getText().toString().isEmpty()
        || binding.txtNumeroAddPiso.getText().toString().isEmpty()
        || binding.txtCiudadAddPiso.getText().toString().isEmpty()
        || binding.txtProvinciaAddPiso.getText().toString().isEmpty()
        || binding.txtCpAddPiso.getText().toString().isEmpty()

        )
            return null;

        float valoracion = binding.ratingBarAddPiso.getRating();
        if (valoracion == 0){
            return null;
        }

        return new Piso(binding.txtDireccionAddPiso.getText().toString(),
                Integer.parseInt(binding.txtNumeroAddPiso.getText().toString()),
                binding.txtCiudadAddPiso.getText().toString(),
                binding.txtProvinciaAddPiso.getText().toString(),
                binding.txtCpAddPiso.getText().toString(), valoracion);
    }
}