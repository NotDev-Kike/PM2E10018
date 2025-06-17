package com.example.pm2e10018;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private SQLiteHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseManager(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertContacto(Contacto contacto) {
        ContentValues values = new ContentValues();
        values.put("nombre", contacto.getNombre());
        values.put("telefono", contacto.getTelefono());
        values.put("foto", contacto.getFoto());
        values.put("nota", contacto.getNota());

        return database.insert("contactos", null, values);
    }

    public List<Contacto> getAllContactos() {
        List<Contacto> contactos = new ArrayList<>();
        Cursor cursor = database.query("contactos", null, null, null, null, null, "nombre ASC");

        if (cursor.moveToFirst()) {
            do {
                Contacto contacto = new Contacto();
                contacto.setId(cursor.getInt(cursor.getColumnIndex("id")));
                contacto.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));
                contacto.setTelefono(cursor.getString(cursor.getColumnIndex("telefono")));
                contacto.setFoto(cursor.getString(cursor.getColumnIndex("foto")));
                contacto.setNota(cursor.getString(cursor.getColumnIndex("nota")));
                contactos.add(contacto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return contactos;
    }

}