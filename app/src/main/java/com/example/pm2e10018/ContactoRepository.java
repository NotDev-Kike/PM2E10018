package com.example.pm2e10018;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContactoRepository {
    private final ContactoDao contactoDao;
    private final LiveData<List<Contacto>> allContactos;
    private final ExecutorService executorService;

    public ContactoRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        contactoDao = db.contactoDao();
        allContactos = contactoDao.getAllContactos();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Contacto>> getAllContactos() {
        return allContactos;
    }

    public void insert(Contacto contacto) {
        executorService.execute(() -> contactoDao.insert(contacto));
    }

    public void update(Contacto contacto) {
        executorService.execute(() -> contactoDao.update(contacto));
    }

    public void delete(Contacto contacto) {
        executorService.execute(() -> contactoDao.delete(contacto));
    }
}