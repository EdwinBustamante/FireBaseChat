package com.edwinbustamante.firebasechat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by EDWIN on 09/09/2017.
 */

public class AdapterMensajes extends RecyclerView.Adapter<HolderMensaje> {
    private List<MensajeRecibir> listMensaje = new ArrayList<>();
    private Context c;

    public AdapterMensajes(Context c) {

        this.c = c;
    }

    public void addMensaje(MensajeRecibir m) {//recibe el mensaje
        listMensaje.add(m);
        notifyItemInserted(listMensaje.size());


    }

    public void removeMensajes() {//recibe el mensaje


        for (int i = 0; i <listMensaje.size(); i=i+1) {

            listMensaje.remove(i);
            notifyItemRemoved(i);
        }
    }

    @Override
    public HolderMensaje onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.card_view_mensajes, parent, false);
        return new HolderMensaje(v);
    }

    @Override
    public void onBindViewHolder(HolderMensaje holder, int position) {

        holder.getNombreMensaje().setText(listMensaje.get(position).getNombre());
        holder.getMensajeMensaje().setText(listMensaje.get(position).getMensaje());

        if (listMensaje.get(position).getType_mensaje().equals("2")) {
            holder.getFotoMensaje().setVisibility(View.VISIBLE);
            holder.getMensajeMensaje().setVisibility(View.VISIBLE);
            Glide.with(c).load(listMensaje.get(position).getUrlFoto()).into(holder.getFotoMensaje());

        } else {
            if (listMensaje.get(position).getType_mensaje().equals("1")) {
                holder.getFotoMensaje().setVisibility(View.GONE);
                holder.getMensajeMensaje().setVisibility(View.VISIBLE);
            }
        }

        ///el holder es un mensaje un card
        if (listMensaje.get(position).getFotoPerfil().isEmpty()) {
            holder.getFotoMensaje().setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(c).load(listMensaje.get(position).getFotoPerfil()).into(holder.getFotoMensajePerfil());
        }


        Long codigoHora = listMensaje.get(position).getHora();
        Date d = new Date(codigoHora);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("hh:mm:ss a");//se da el formato en este caso la hora los minutos y los segundos
        holder.getHoraMensaje().setText(sdf.format(d));


        holder.getNombreMensaje().setText(listMensaje.get(position).getNombre());


    }

    @Override
    public int getItemCount() {
        return listMensaje.size();
    }
}
