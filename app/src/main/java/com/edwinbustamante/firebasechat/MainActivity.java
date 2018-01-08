package com.edwinbustamante.firebasechat;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.concurrent.CopyOnWriteArraySet;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG ="TOKEN" ;
    private CircleImageView fotoperfil;
    private TextView nombre;
    private RecyclerView rvMensajes;
    private EditText txtMensajes;
    private Button enviar;
    private AdapterMensajes adapter;
    private ImageButton btnEnviarFoto;
    private static final int PHOTO_SEND = 1;
    private static final int PHOTO_PERFIL = 2;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String fotoPerfilCadena;
    private ProgressDialog progressDialog;
    private View dialogView;///guarda la variable cuando cambia el nombre
    DB myDb;
    private Typeface lovers;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.vaciar_chat:
                 databaseReference.setValue(null);///estamos borrando de la base de datos

                adapter.removeMensajes();
                adapter.removeMensajes();
                adapter.removeMensajes();
                adapter.removeMensajes();
                adapter.removeMensajes();



                return true;
            case R.id.cerrar:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "token: " + refreshedToken);
        //myDb=new DB(this,"NombreChat",null,1);
        //recuperarDeBaseDeDatosSQLite();
        String fuenteLove = "fuentes/lovers.ttf";
        this.lovers = Typeface.createFromAsset(getAssets(), fuenteLove);

        fotoperfil = (CircleImageView) findViewById(R.id.fotoDePerfil);
        ///Configurando el texto.........
        nombre = (TextView) findViewById(R.id.nombre);
        nombre.setTypeface(lovers);


        rvMensajes = (RecyclerView) findViewById(R.id.rvMensajes);

        txtMensajes = (EditText) findViewById(R.id.editTextMensaje);
        txtMensajes.setSelection(txtMensajes.getText().length());//poner el cursor
        enviar = (Button) findViewById(R.id.btnenviar);
        enviar.setVisibility(View.INVISIBLE);

        btnEnviarFoto = (ImageButton) findViewById(R.id.btnEnviarFoto);


        progressDialog = new ProgressDialog(this);

        final Context context = this;
        SharedPreferences sharprefs = getSharedPreferences("ArchivosSp", context.MODE_PRIVATE);


        database = FirebaseDatabase.getInstance();

        database.setPersistenceEnabled(true);//Habilitamos la persistencia de datos de firebase
        databaseReference = database.getReference("chat");//sala de chat
        databaseReference.keepSynced(true);//mantine los datos sincronizados
        fotoPerfilCadena = "";
        //habilitando persistencia en disco para que cuadno no hjaya conexxion a intyernet
       //  FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        adapter = new AdapterMensajes(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(l);
        rvMensajes.setAdapter(adapter);

        recuperarNombre();
        recuperarFotoperil();
        txtMensajes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    enviar.setVisibility(View.VISIBLE);
                }else{

                    enviar.setVisibility(View.INVISIBLE);
                }

            }
        });


                

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//aqui es donde enviamos el mensaje

                SharedPreferences sharpref = getPreferences(contexto.MODE_PRIVATE);
                String perfil = sharpref.getString("fotoperfilContenedor", "nohayfoto");
                databaseReference.push().setValue(new MensajeEnviar(txtMensajes.getText().toString(), nombre.getText().toString(), perfil, "1", ServerValue.TIMESTAMP));
                txtMensajes.setText("");




                txtMensajes.setText("");
            }
        });


        nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater vista = MainActivity.this.getLayoutInflater();


                dialogView = vista.inflate(R.layout.cambiar_nombre, null);
                alerta.setView(dialogView);

                alerta.setMessage("Deseas cambiar tu nombre..?")

                        .setCancelable(false)
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ////no se esta haciendo nada
                            }
                        })

                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                EditText editText = (EditText) dialogView.findViewById(R.id.cambiarnombre);

                                /**     DB db = new DB(getApplicationContext(), "NombreChat", null, 1);
                                 SQLiteDatabase databse = db.getWritableDatabase();
                                 String cambio = editText.getText().toString();
                                 String estado = "";
                                 if (databse != null) {
                                 ContentValues nuevoregistro = new ContentValues();
                                 nuevoregistro.put("nombre", cambio);
                                 databse.insertOrThrow("Nombre", null, nuevoregistro);
                                 estado = "El nombre se cambio correctamente";
                                 }
                                 databse.close();

                                 //String estado = db.guardar(cambio);

                                 */
                                //Toast.makeText(MainActivity.this, estado, Toast.LENGTH_SHORT).show();

                                //GUARDAMOS LOS DATOS TEMPORALMENTE EN LA APLICACION...ANDROID ESTUDIO SOLO SE PERDERA CUANDO SE DESINSTALA LA APLICACION
                                SharedPreferences sharpref = getPreferences(context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharpref.edit();
                                editor.putString("midatoNombre", editText.getText().toString());
                                editor.commit();

                                nombre.setText(editText.getText().toString());

                            }
                        });
                AlertDialog alertDialog = alerta.create();
                alertDialog.show();


            }
        });

        ///escuchador para enviar foto
        btnEnviarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(i, "Selecciona una foto"), PHOTO_SEND);//PHOTO_SEND ES EL VALOR QUE MANEJAMOS PARA SUBIR UNA IMAGEN
            }
        });


        ////aqui se hara para cambiar el foto de perfil

        fotoperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(i, "Selecciona una foto"), PHOTO_PERFIL);
            }
        });
        //Observa cuando cuando llega un mensaje se debe ir hacia abajo...el scrole
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();//llamaremos a esta funcion para que el scroll se vaya hacia abajo
            }
        });

        //escuchador de los cambios en la base de datos de fire base
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MensajeRecibir m = dataSnapshot.getValue(MensajeRecibir.class);       //AQUI ESTAMOS RECIBIENDO los mensajes desde firebase recuperandoMENSAJE

                if(m.getNombre().equals(nombre.getText())){



                }else{
                    mostrarNotificacion(m.getNombre().toString(),m.getMensaje().toString());
                }
                adapter.addMensaje(m);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //    Toast.makeText(MainActivity.this, "REVISE SU CONEXION A INTERNET", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void recuperarFotoperil() {

        SharedPreferences sharpref = getPreferences(contexto.MODE_PRIVATE);
        String valor = sharpref.getString("fotoperfilContenedor", "nohayfoto");

        if (valor != "nohayfoto") {
            Glide.with(MainActivity.this).load(valor).into(fotoperfil);
        }
    }

    Context contexto = this;

    private void recuperarNombre() {

        SharedPreferences sharpref = getPreferences(contexto.MODE_PRIVATE);
        String valor = sharpref.getString("midatoNombre", "Pon tu nombre para empezar Chat..");
        nombre.setText(valor);


    }


    private void setScrollbar() {
        rvMensajes.scrollToPosition(adapter.getItemCount() - 1);///esta funcion es el que lleva el mensaje hasta abajo
    }


    //metodo que devuelve el resultado de la actividad en este cado el resultado de seleccionar una foto

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_SEND && resultCode == RESULT_OK) {
            //implementamos el dialogo
            progressDialog.setTitle("Enviando foto...");
            progressDialog.setMessage("Espere un momento mientras la foto se envia...");
            progressDialog.setCancelable(false);
            progressDialog.show();


            Uri u = data.getData();
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference("imagenes_chat");//Imagenes de mi Chat
            final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());
            fotoReferencia.putFile(u).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    SharedPreferences sharpref = getPreferences(contexto.MODE_PRIVATE);
                    String perfil = sharpref.getString("fotoperfilContenedor", "nohayfoto");

                    Uri u = taskSnapshot.getDownloadUrl();
                    MensajeEnviar m = new MensajeEnviar(nombre.getText().toString() + " subió una foto", u.toString(), nombre.getText().toString(), perfil, "2", ServerValue.TIMESTAMP);//ENVIAMOS LAS FOTO DESDEM AQUI
                    databaseReference.push().setValue(m);
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Foto enviado exitosamente", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            if (requestCode == PHOTO_PERFIL && resultCode == RESULT_OK) {
                //implementamos el dialogo
                progressDialog.setTitle("Cambiando foto de perfil...");
                progressDialog.setMessage("Espere un momento mientras se cambia la foto...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                Uri u = data.getData();
                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference("imagenes_perfil");//Imagenes de mi Chat
                final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());
                fotoReferencia.putFile(u).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Uri u = taskSnapshot.getDownloadUrl();///la url de la foto esta aqui
                        fotoPerfilCadena = u.toString();

                        ///GUARDAMOS EL LINK
                        SharedPreferences sharpref = getPreferences(MainActivity.this.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharpref.edit();
                        editor.putString("fotoperfilContenedor", fotoPerfilCadena);
                        editor.commit();


                        MensajeEnviar m = new MensajeEnviar(nombre.getText().toString() + " cambio su foto de perfil", u.toString(), nombre.getText().toString(), fotoPerfilCadena, "2", ServerValue.TIMESTAMP);//ENVIAMOS LAS FOTO DESDEM AQUI
                        databaseReference.push().setValue(m);
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Foto de perfil cambiado exitosamente", Toast.LENGTH_SHORT).show();
                        Glide.with(MainActivity.this).load(u.toString()).into(fotoperfil);
                    }
                });
            }

        }
    }

     ////LAS NOTIFICACION QUE LLGAN EL EL MENSAJE AQUI ESTAN

    private void mostrarNotificacion(String title , String body){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri sountUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)

                .setSmallIcon(R.drawable.emoticochat)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(sountUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());


    }
}
