package com.edwinbustamante.firebasechat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by EDWIN on 09/09/2017.
 */

public class HolderMensaje extends RecyclerView.ViewHolder {
    private CircleImageView fotoMensajePerfil;
    TextView nombreMensaje;
    TextView horaMensaje;
    TextView mensajeMensaje;
    LinearLayout linearLayout;
    private ImageView fotoMensaje;
    public HolderMensaje(View itemView) {
        super(itemView);
        fotoMensajePerfil = (CircleImageView) itemView.findViewById(R.id.fotoPerfilMensaje);
        nombreMensaje = (TextView) itemView.findViewById(R.id.nombreMensaje);
        horaMensaje = (TextView) itemView.findViewById(R.id.horaMensaje);
        mensajeMensaje = (TextView) itemView.findViewById(R.id.mensajeMensaje);
       fotoMensaje=(ImageView)itemView.findViewById(R.id.mensajeFoto);
        linearLayout=(LinearLayout)itemView.findViewById(R.id.LinearLayoutColor);
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }

    public ImageView getFotoMensaje() {
        return fotoMensaje;
    }

    public void setFotoMensaje(ImageView fotoMensaje) {
        this.fotoMensaje = fotoMensaje;
    }

    public CircleImageView getFotoMensajePerfil() {
        return fotoMensajePerfil;
    }

    public void setFotoMensajePerfil(CircleImageView fotoMensajePerfil) {
        this.fotoMensajePerfil = fotoMensajePerfil;
    }

    public TextView getNombreMensaje() {
        return nombreMensaje;
    }

    public void setNombreMensaje(TextView nombreMensaje) {
        this.nombreMensaje = nombreMensaje;
    }

    public TextView getHoraMensaje() {
        return horaMensaje;
    }

    public void setHoraMensaje(TextView horaMensaje) {
        this.horaMensaje = horaMensaje;
    }

    public TextView getMensajeMensaje() {
        return mensajeMensaje;
    }

    public void setMensajeMensaje(TextView mensajeMensaje) {
        this.mensajeMensaje = mensajeMensaje;
    }
}
