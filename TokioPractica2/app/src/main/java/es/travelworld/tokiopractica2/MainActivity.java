package es.travelworld.tokiopractica2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**Creamos un objeto botón y lo relacionamos a traves del id con el botón
         * de nuestra pantalla.*/

        Button boton = findViewById(R.id.botonNext);

        /**Establecemos la función para que el botón ejecute su acción al hacer click.*/

        boton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void  onClick(View v) {
                //Definimos la acción que se ejecutará al pulsar el botón.

                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}