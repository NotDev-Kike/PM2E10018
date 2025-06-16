package com.example.pm2e10018;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactoDao {
    // Operación CREATE (Insertar)
    @Insert
    long insert(Contacto contacto);

    // Operación READ (Leer)
    @Query("SELECT * FROM contactos WHERE id = :id")
    Contacto getContactoById(int id);

    @Query("SELECT * FROM contactos ORDER BY nombre ASC")
    LiveData<List<Contacto>> getAllContactos();

    @Query("SELECT * FROM contactos WHERE pais = :pais ORDER BY nombre ASC")
    LiveData<List<Contacto>> getContactosPorPais(String pais);

    // Operación UPDATE (Actualizar)
    @Update
    int update(Contacto contacto);

    // Operación DELETE (Eliminar)
    @Delete
    int delete(Contacto contacto);

    @Query("DELETE FROM contactos WHERE id = :id")
    int deleteById(int id);

    // Búsquedas específicas
    @Query("SELECT * FROM contactos WHERE nombre LIKE :searchQuery OR telefono LIKE :searchQuery")
    LiveData<List<Contacto>> buscarContactos(String searchQuery);

    // Operaciones adicionales
    @Query("SELECT COUNT(*) FROM contactos")
    LiveData<Integer> getTotalContactos();

    @Query("SELECT DISTINCT pais FROM contactos ORDER BY pais ASC")
    LiveData<List<String>> getPaisesUnicos();
}