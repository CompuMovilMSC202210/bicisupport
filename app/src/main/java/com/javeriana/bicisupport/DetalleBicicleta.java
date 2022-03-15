package com.javeriana.bicisupport;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class DetalleBicicleta extends AppCompatActivity {
    Spinner tipo, color, marca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_bicicleta);
        tipo = findViewById(R.id.bstipo);
        color = findViewById(R.id.bscolor);
        marca = findViewById(R.id.bsmarca);

        ArrayAdapter<CharSequence> adaptertipo = ArrayAdapter.createFromResource(this,
                R.array.tipo_bibicleta, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> adaptermarca = ArrayAdapter.createFromResource(this,
                R.array.bicicletas_marcas, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> adaptercolor = ArrayAdapter.createFromResource(this,
                R.array.color_bicicletas, android.R.layout.simple_spinner_item);


        tipo.setAdapter(adaptertipo);
        color.setAdapter(adaptercolor);
        marca.setAdapter(adaptermarca);
    }
}