package com.javeriana.bicisupport.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.javeriana.bicisupport.R;
import com.javeriana.bicisupport.models.Mensaje;

import java.util.List;

public class MensajeAdapter extends RecyclerView.Adapter<MensajeAdapter.MyViewHolder> {
    private List<Mensaje> mensajes;
    private Context context;
    private LayoutInflater linflate;

    public MensajeAdapter(List<Mensaje> mensajes, Context context){
        this.linflate = LayoutInflater.from(context);
        this.context = context;
        this.mensajes = mensajes;
    }

    public class  MyViewHolder extends RecyclerView.ViewHolder{
        TextView msg, aut;

        MyViewHolder(View itemView){
            super(itemView);
            msg = itemView.findViewById(R.id.cmensaje);
            aut = itemView.findViewById(R.id.cusuario);
        }

        void bindData(@NonNull Mensaje item){
            msg.setText(item.getMensaje());
            aut.setText(item.getEmisor());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = linflate.inflate(R.layout.mensajelayout,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       holder.bindData(mensajes.get(position));
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    public void agregarMensaje(Mensaje mensaje){
        this.mensajes.add(mensaje);
    }

   
}
