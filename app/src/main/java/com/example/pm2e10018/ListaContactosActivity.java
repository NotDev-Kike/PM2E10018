package com.example.pm2e10018;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListaContactosActivity extends AppCompatActivity implements ContactosAdapter.OnItemActionListener {
    private RecyclerView recyclerView;
    private ContactosAdapter adapter;
    private ContactoViewModel contactoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contactos);

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewContactos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar adaptador
        adapter = new ContactosAdapter(this);
        recyclerView.setAdapter(adapter);

        // Configurar ViewModel
        contactoViewModel = new ViewModelProvider(this).get(ContactoViewModel.class);
        contactoViewModel.getAllContactos().observe(this, contactos -> {
            adapter.setContactos(contactos);
        });

        // Configurar FloatingActionButton
        FloatingActionButton fab = findViewById(R.id.fabAgregarContacto);
        fab.setOnClickListener(view -> {
            startActivity(new Intent(this, AgregarContactoActivity.class));
        });
    }

    @Override
    public void onItemClick(Contacto contacto) {
        Intent intent = new Intent(this, EditarContactoActivity.class);
        intent.putExtra("contacto_id", contacto.getId());
        startActivity(intent);
    }

    @Override
    public void onCallClick(Contacto contacto) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + contacto.getTelefono()));
        startActivity(intent);
    }
}