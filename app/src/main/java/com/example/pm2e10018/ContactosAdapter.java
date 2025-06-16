package com.example.pm2e10018;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ContactosAdapter extends RecyclerView.Adapter<ContactosAdapter.ContactoViewHolder> {
    private List<Contacto> contactos;
    private final OnItemActionListener listener;

    public interface OnItemActionListener {
        void onItemClick(Contacto contacto);
        void onCallClick(Contacto contacto);
    }

    public ContactosAdapter(OnItemActionListener listener) {
        this.contactos = new ArrayList<>();
        this.listener = listener;
    }

    // Método para actualizar la lista con DiffUtil para mejor rendimiento
    public void setContactos(List<Contacto> nuevosContactos) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new ContactoDiffCallback(this.contactos, nuevosContactos));
        this.contactos = new ArrayList<>(nuevosContactos);
        result.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ContactoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contacto, parent, false);
        return new ContactoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactoViewHolder holder, int position) {
        holder.bind(contactos.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return contactos.size();
    }

    // ViewHolder optimizado
    static class ContactoViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivFoto;
        private final TextView tvNombre;
        private final TextView tvTelefono;
        private final TextView tvPais;
        private final ImageView btnLlamar;

        public ContactoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFoto = itemView.findViewById(R.id.ivFoto);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvTelefono = itemView.findViewById(R.id.tvTelefono);
            tvPais = itemView.findViewById(R.id.tvPais);
            btnLlamar = itemView.findViewById(R.id.btnLlamar);
        }

        public void bind(Contacto contacto, OnItemActionListener listener) {
            tvNombre.setText(contacto.getNombre());
            tvTelefono.setText(contacto.getTelefono());
            tvPais.setText(contacto.getPais());

            // Cargar foto con manejo de errores
            loadContactPhoto(contacto.getFoto());

            // Configurar listeners
            itemView.setOnClickListener(v -> listener.onItemClick(contacto));
            btnLlamar.setOnClickListener(v -> listener.onCallClick(contacto));
        }

        private void loadContactPhoto(String photoPath) {
            if (photoPath != null && !photoPath.isEmpty()) {
                try {
                    File imgFile = new File(photoPath);
                    if (imgFile.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        ivFoto.setImageBitmap(bitmap);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ivFoto.setImageResource(R.drawable.ic_person);
        }
    }

    // Clase para comparar listas y optimizar actualizaciones
    private static class ContactoDiffCallback extends DiffUtil.Callback {
        private final List<Contacto> oldList, newList;

        public ContactoDiffCallback(List<Contacto> oldList, List<Contacto> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            Contacto oldContacto = oldList.get(oldItemPosition);
            Contacto newContacto = newList.get(newItemPosition);

            return oldContacto.getNombre().equals(newContacto.getNombre()) &&
                    oldContacto.getTelefono().equals(newContacto.getTelefono()) &&
                    oldContacto.getPais().equals(newContacto.getPais()) &&
                    oldContacto.getFoto().equals(newContacto.getFoto());
        }
    }
}