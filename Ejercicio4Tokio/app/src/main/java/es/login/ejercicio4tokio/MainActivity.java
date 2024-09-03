package es.login.ejercicio4tokio;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import es.login.ejercicio4tokio.databinding.ActivityMainBinding;
/*
Vamos a crear un programa que usando dataBinding filtre los campos introducidos en los
widgets de nombre, apellidos y edad. Además el botón permanecerá desactivado hasta que los
campos de nombre y apellidos estén cumplimentados. Como funcionalidades extras cuando
pulse la imagen de perfil se accederá a la cámara y la foto realizada se añadirá como
foto de perfil.**/

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Usuario usuario;


    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PERMISSIONS = 2;

    private ImageView imagenPerfil;
    private ImageView botonImagen;//Referencia al botón de la cámara
    private Spinner edadSpinner;
    private TextView condicionesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        usuario = new Usuario("", "", "");


        imagenPerfil = findViewById(R.id.imagenPerfil);
        botonImagen = findViewById(R.id.botonImagen);//Inicializa la referencia.
        edadSpinner = findViewById(R.id.spinner);
        condicionesTextView = findViewById(R.id.condiciones);
        binding.setUsuario(usuario);

        /*Configuramos el spinner para lo cual usaremos un ArrayAdapter.**/

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.edad_options, android.R.layout.simple_spinner_dropdown_item);

        //Especificamos el layout a usar cuando la lista de opciones aparezca.

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Aplicamos el adapter al spinner

        edadSpinner.setAdapter(adapter);

        //Desactivamos el botón inicialmente.

        binding.boton.setEnabled(false);

        /*Creamos el código para los diferentes listener.**/

        //Listener para la validación del nombre.

        binding.nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validarCampos();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().contains("@") || editable.toString().contains("!")) {
                    binding.nombre.setError("Ups, no creo que sea correcto, revisalo");
                }
                validarCampos();

            }
        });


        //Listener para la validación de apellidos

        binding.apellidos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validarCampos();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().contains("@") || editable.toString().contains("!")) {
                    binding.apellidos.setError("Ups, no creo que sea correcto, revisalo");
                }

            }
        });

        //Listener para la selección de edad.

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               validarCampos();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Listener para el botón

        binding.boton.setOnClickListener(view -> {
            Toast.makeText(this, "Datos Guardados", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        });

        //Listener para el TextView condiciones

        condicionesTextView.setOnClickListener(view -> {
            String url = "https://developers.google.com/ml-kit/terms";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        /*Ahora vamos a dar funcionalidad a los permisos para abrir la cámara.
        * Estos permisos serán para el uso de la cámara, para escribir en el
        * almacenaje externo y para leer en el almacenaje externo.**/

        // Solicitar permisos si no se han otorgado
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, REQUEST_PERMISSIONS);
        }

        botonImagen.setOnClickListener(v -> abrirCamara());
    }
    /*Creamos un método para validar los campos como el nombre, apellidos y edad.
    * Si en los campos de nombre y apellidos introducimos @ o ! se mostrará
    * un mensaje de error. Además si mostramos una edad seleccionada inferior a 18
    * años se mostrará otro mensaje de error.**/

    private void validarCampos () {
        String nombre = binding.nombre.getText().toString().trim();
        String apellidos = binding.apellidos.getText().toString().trim();
        int edadSeleccionada = binding.spinner.getSelectedItemPosition();

        boolean nombreValido = !nombre.contains("@") && !nombre.contains("!");
        boolean apellidosValido = !apellidos.contains("@") && !apellidos.contains("!");
        boolean edadValida = true;

        //Validamos el campo de nombre.

        if (!nombreValido) {
            binding.nombre.setError("Ups, no creo que sea correcto, revísalo");
        } else {
            binding.nombre.setError(null);
        }

        //Validamos el campo de apellidos

        if (!apellidosValido) {
            binding.apellidos.setError("Ups, no creo que sea correcto, revísalo");
        } else {
            binding.apellidos.setError(null);
        }

        //Validamos la selección de Edad

        switch (edadSeleccionada) {
            case 1: // 0-5
            case 2: // 6-11
            case 3: // 12-17
                edadValida = false;
                Toast.makeText(this, "Esta app no es para tí", Toast.LENGTH_SHORT).show();
                break;
            default:
                edadValida = true;
                break;

        }

        //Activamos o desactivamos el botón según la validez de los campos.

        boolean habilitarBoton = nombreValido && apellidosValido && !nombre.isEmpty() &&
                !apellidos.isEmpty() && edadValida;
        binding.boton.setEnabled(habilitarBoton);
    }

    /*Método para limpiar los campos cumplimentados una vez pulsado el botón
    * y verificado todos los campos.**/

    private void limpiarCampos () {
        binding.nombre.setText("");
        binding.apellidos.setText("");
        binding.spinner.setSelection(0);
    }

    /*Método para abrir la cámara.**/

    private void abrirCamara() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "No se puede abrir la cámara", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imagenPerfil.setImageBitmap(imageBitmap);

            //Ocultamos el icono de la cámara después de mostrar la imagen capturada

            botonImagen.setVisibility((View.GONE));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permisos concedidos", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permisos denegados", Toast.LENGTH_SHORT).show();
            }
        }
    }
}