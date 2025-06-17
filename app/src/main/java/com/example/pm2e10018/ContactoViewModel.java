package com.example.pm2e10018;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class ContactoViewModel extends AndroidViewModel {
    private final LiveData<List<Contacto>> allContactos;
    private final ContactoRepository repository;

    public ContactoViewModel(@NonNull Application application) {
        super(application);
        repository = new ContactoRepository(application);
        allContactos = repository.getAllContactos();
    }

    public LiveData<List<Contacto>> getAllContactos() {
        return allContactos;
    }

    public void insert(Contacto contacto) {
        repository.insert(contacto);
    }

    public void update(Contacto contacto) {
        repository.update(contacto);
    }

    public void delete(Contacto contacto) {
        repository.delete(contacto);
    }
}