package com.example.pm2e10018;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "contactos")
public class Contacto {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String pais;

    @NonNull
    private String nombre;

    @NonNull
    private String telefono;

    private String nota;
    private String foto;

    public Contacto() {
        this.pais = "";
        this.nombre = "";
        this.telefono = "";
        this.nota = "";
        this.foto = "";
    }

    public Contacto(@NonNull String pais, @NonNull String nombre,
                    @NonNull String telefono, String nota, String foto) {
        this.pais = pais;
        this.nombre = nombre;
        this.telefono = telefono;
        this.nota = nota != null ? nota : "";
        this.foto = foto != null ? foto : "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getPais() {
        return pais;
    }

    public void setPais(@NonNull String pais) {
        this.pais = pais;
    }

    @NonNull
    public String getNombre() {
        return nombre;
    }

    public void setNombre(@NonNull String nombre) {
        this.nombre = nombre;
    }

    @NonNull
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(@NonNull String telefono) {
        this.telefono = telefono;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota != null ? nota : "";
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto != null ? foto : "";
    }

    @Override
    public String toString() {
        return "Contacto{" +
                "id=" + id +
                ", pais='" + pais + '\'' +
                ", nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                ", nota='" + nota + '\'' +
                ", foto='" + foto + '\'' +
                '}';
    }

    public boolean isValid() {
        return !nombre.isEmpty() && !telefono.isEmpty() && !pais.isEmpty();
    }
}