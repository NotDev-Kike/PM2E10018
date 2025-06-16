package com.example.pm2e10018;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PERMISSION_CAMERA = 100;
    private static final int REQUEST_PERMISSION_WRITE_STORAGE = 101;

    private AutoCompleteTextView actvPais;
    private EditText etNombre, etTelefono, etNota;
    private Button btnGuardar, btnVerContactos, btnTomarFoto;
    private ImageView ivFoto;
    private AppDatabase db;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getDatabase(this);
        initViews();
        setupSpinner();
        setupButtons();
    }

    private void initViews() {
        actvPais = findViewById(R.id.actvPais);
        etNombre = findViewById(R.id.etNombre);
        etTelefono = findViewById(R.id.etTelefono);
        etNota = findViewById(R.id.etNota);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnVerContactos = findViewById(R.id.btnVerContactos);
        btnTomarFoto = findViewById(R.id.btnTomarFoto);
        ivFoto = findViewById(R.id.ivFoto);
    }

    private void setupSpinner() {
        String[] paises = {"Honduras (504)", "Costa Rica", "Guatemala (502)", "El Salvador", "Polses"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, paises);
        actvPais.setAdapter(adapter);
    }

    private void setupButtons() {
        btnGuardar.setOnClickListener(v -> {
            if (validarCampos()) {
                guardarContacto();
            }
        });

        btnVerContactos.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ListaContactosActivity.class));
        });

        btnTomarFoto.setOnClickListener(v -> verificarPermisosCamara());
    }

    private boolean validarCampos() {
        if (actvPais.getText().toString().trim().isEmpty()) {
            mostrarAlerta("Debe seleccionar un país");
            return false;
        }
        if (etNombre.getText().toString().trim().isEmpty()) {
            mostrarAlerta("Debe escribir un nombre");
            return false;
        }
        if (etTelefono.getText().toString().trim().isEmpty()) {
            mostrarAlerta("Debe escribir un teléfono");
            return false;
        }
        return true;
    }

    private void mostrarAlerta(String mensaje) {
        new AlertDialog.Builder(this)
                .setTitle("Alerta")
                .setMessage(mensaje)
                .setPositiveButton("OK", null)
                .show();
    }

    private void verificarPermisosCamara() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_CAMERA);
        } else {
            dispatchTakePictureIntent();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Error al crear el archivo de imagen", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.pm2e10018.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File imgFile = new File(currentPhotoPath);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ivFoto.setImageBitmap(myBitmap);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Se necesitan permisos de cámara para tomar fotos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void guardarContacto() {
        new Thread(() -> {
            Contacto contacto = new Contacto(
                    actvPais.getText().toString(),
                    etNombre.getText().toString(),
                    etTelefono.getText().toString(),
                    etNota.getText().toString(),
                    currentPhotoPath // Ahora incluye la ruta de la foto
            );
            db.contactoDao().insert(contacto);
            runOnUiThread(() -> {
                Toast.makeText(this, "Contacto guardado", Toast.LENGTH_SHORT).show();
                limpiarCampos();
            });
        }).start();
    }

    private void limpiarCampos() {
        actvPais.setText("");
        etNombre.setText("");
        etTelefono.setText("");
        etNota.setText("");
        ivFoto.setImageResource(R.drawable.ic_person);
        currentPhotoPath = null;
    }
}