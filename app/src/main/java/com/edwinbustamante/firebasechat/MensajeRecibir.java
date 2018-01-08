package com.edwinbustamante.firebasechat;

/**
 * Created by EDWIN on 10/09/2017.
 */

public class MensajeRecibir extends Mensaje {
private Long hora;

    public MensajeRecibir(Long hora) {
        this.hora = hora;
    }

    public MensajeRecibir() {
    }

    public MensajeRecibir(String mensaje, String urlFoto, String nombre, String fotoPerfil, String type_mensaje, Long hora) {
        super(mensaje, urlFoto, nombre, fotoPerfil, type_mensaje);
        this.hora = hora;
    }

    public Long getHora() {
        return hora;
    }

    public void setHora(Long hora) {
        this.hora = hora;
    }
}
