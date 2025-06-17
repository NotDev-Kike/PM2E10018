package com.example.pm2e10018;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditarContactoActivity extends AppCompatActivity {
    private EditText etNombre, etTelefono, etPais, etNota;
    private AppDatabase db;
    private Contacto contacto;
    private int contactoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_contacto);

        db = AppDatabase.getDatabase(this);

        contactoId = getIntent().getIntExtra("contacto_id", -1);

        initViews();

        if (contactoId != -1) {
            cargarContacto(contactoId);
        } else {
            contacto = new Contacto();
        }
    }

    private void initViews() {
        etNombre = findViewById(R.id.etNombre);
        etTelefono = findViewById(R.id.etTelefono);
        etPais = findViewById(R.id.etPais);
        etNota = findViewById(R.id.etNota);
    }

    private void cargarContacto(int id) {
        new Thread(() -> {
            contacto = db.contactoDao().getContactoById(id);

            runOnUiThread(() -> {
                if (contacto != null) {
                    mostrarDatosContacto();
                } else {
                    Toast.makeText(EditarContactoActivity.this,
                            "Error al cargar el contacto",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }).start();
    }

    private void mostrarDatosContacto() {
        etNombre.setText(contacto.getNombre());
        etTelefono.setText(contacto.getTelefono());
        etPais.setText(contacto.getPais());
        etNota.setText(contacto.getNota());
    }

    public void guardarCambios(View view) {
        if (etNombre.getText().toString().trim().isEmpty() ||
                etTelefono.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Nombre y teléfono son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (contacto == null) {
            contacto = new Contacto();
        }

        contacto.setNombre(etNombre.getText().toString().trim());
        contacto.setTelefono(etTelefono.getText().toString().trim());
        contacto.setPais(etPais.getText().toString().trim());
        contacto.setNota(etNota.getText().toString().trim());

        new Thread(() -> {
            try {
                if (contactoId == -1) {
                    long newId = db.contactoDao().insert(contacto);
                    runOnUiThread(() -> {
                        Toast.makeText(EditarContactoActivity.this,
                                "Contacto creado con éxito",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    });
                } else {
                    db.contactoDao().update(contacto);
                    runOnUiThread(() -> {
                        Toast.makeText(EditarContactoActivity.this,
                                "Contacto actualizado con éxito",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(EditarContactoActivity.this,
                            "Error al guardar: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    public void cancelarEdicion(View view) {
        finish();
    }
}