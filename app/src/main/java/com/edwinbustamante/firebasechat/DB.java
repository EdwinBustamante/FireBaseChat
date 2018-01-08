package com.edwinbustamante.firebasechat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by EDWIN on 12/09/2017.
 */

public class DB extends SQLiteOpenHelper {

  String sqlTabla="CREATE TABLE Nombre(nombre TEXT)";

    public DB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlTabla);//creamos la tabla
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String guardar(String cambionombre) {
        String mensaje = "";

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contendor = new ContentValues();
        contendor.put("nombre", cambionombre);
        try {

            database.insertOrThrow("datos", null, contendor);
            mensaje = "nombre cambiado correctamente";
        } catch (SQLException e) {
            mensaje = "erro al cambiar nombre";
        }
        return mensaje;
    }

    public String recuperar() {
        String dato = "";
        SQLiteDatabase database = this.getWritableDatabase();
        String q = "SELECT nombre FROM datos";

        Cursor registros = database.rawQuery(q, null);

        ArrayList<String> lista = new ArrayList<>();
        if (registros.moveToFirst()) {
            do {
                lista.add(registros.getString(0));
            } while (registros.moveToNext());

        }
        return lista.get(lista.size());
    }
}
