package com.toni.juegomemoria;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class JuegoActivity extends AppCompatActivity {

    private static final int NUM_CASILLAS = 16;

    private ImageButton btn00;
    private ImageButton btn01;
    private ImageButton btn02;
    private ImageButton btn03;
    private ImageButton btn10;
    private ImageButton btn11;
    private ImageButton btn12;
    private ImageButton btn13;
    private ImageButton btn20;
    private ImageButton btn21;
    private ImageButton btn22;
    private ImageButton btn23;
    private ImageButton btn30;
    private ImageButton btn31;
    private ImageButton btn32;
    private ImageButton btn33;

    private TextView txtPuntuacion;
    private Button btnReiniciar;

    //variables para el juego
    private ImageButton[] tablero;
    private int[] imagenes;
    private int imagenFondo;
    private int puntuacion;
    private int aciertos;

    private ArrayList<Integer> arrayBarajado;
    private ImageButton imagenVista;
    private int seleccion1;
    private int seleccion2;
    private boolean tableroBloqueado;
    private final Handler handler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego2);

        btn00 = findViewById(R.id.img00);
        btn01 = findViewById(R.id.img01);
        btn02 = findViewById(R.id.img02);
        btn03 = findViewById(R.id.img03);
        btn10 = findViewById(R.id.img10);
        btn11 = findViewById(R.id.img11);
        btn12 = findViewById(R.id.img12);
        btn13 = findViewById(R.id.img13);
        btn20 = findViewById(R.id.img20);
        btn21 = findViewById(R.id.img21);
        btn22 = findViewById(R.id.img22);
        btn23 = findViewById(R.id.img23);
        btn30 = findViewById(R.id.img30);
        btn31 = findViewById(R.id.img31);
        btn32 = findViewById(R.id.img32);
        btn33 = findViewById(R.id.img33);
        txtPuntuacion = findViewById(R.id.txtPuntuacion);
        btnReiniciar = findViewById(R.id.btnReiniciar);

        //Meter las imagenes en el array tablero
        tablero = new ImageButton[]{
                btn00,
                btn01,
                btn02,
                btn03,
                btn10,
                btn11,
                btn12,
                btn13,
                btn20,
                btn21,
                btn22,
                btn23,
                btn30,
                btn31,
                btn32,
                btn33,
        };

        //Referencia a la imagen de fondo
        imagenFondo = R.drawable.fondo;

        //Referencias a las imagenes
        imagenes = new int[]{
                R.drawable.la0,
                R.drawable.la1,
                R.drawable.la2,
                R.drawable.la3,
                R.drawable.la4,
                R.drawable.la5,
                R.drawable.la6,
                R.drawable.la7
        };

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.sonido_imagen);
        iniciandoJuego(mp);

        btnReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciandoJuego(mp);
            }
        });

    }

    public void iniciandoJuego(MediaPlayer mp){
        puntuacion = 0;
        aciertos = 0;

        txtPuntuacion.setText(getString(R.string.txt_puntuacion) + Integer.toString(puntuacion));
        tableroBloqueado = false;
        imagenVista = null;
        seleccion1 = -1;
        seleccion2 = -1;

        arrayBarajado = barajarImagenes();
        //Muestra las imagenes en el tablero
        for (int i = 0 ; i < NUM_CASILLAS ; i++){
            tablero[i].setImageResource(imagenes[arrayBarajado.get(i)]);
            //animacionIn(tablero[i]);
        }

        //oculta las imagenes del tablero
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0 ; i < NUM_CASILLAS ; i++){
                    tablero[i].setImageResource(imagenFondo);
                    //animacionOut(tablero[i]);
                    animacionInOut(tablero[i]);
                }
            }
        }, 500);

        //Añadir listeners a las ImageView
        for (int i = 0 ; i < NUM_CASILLAS ; i++){

            tablero[i].setEnabled(true);
            final int j = i;
            tablero[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mp.start();
                    if(tableroBloqueado == false){

                        //animacionIn(v);
                        comprobar(j, tablero[j]);
                    }
                }
            });
        }

    }


    public void comprobar(int i, final ImageButton imageButton){

        //ninguna imagen seleccionada
        if(imagenVista == null){
            imagenVista = imageButton;
            imagenVista.setImageResource(imagenes[arrayBarajado.get(i)]);
            animacionOut(imagenVista);
            imagenVista.setEnabled(false);
            seleccion1 = arrayBarajado.get(i);
        }else{//dos imagenes seleccionadas
            tableroBloqueado = true;
            imageButton.setImageResource(imagenes[arrayBarajado.get(i)]);
            imageButton.setEnabled(false);
            seleccion2 = arrayBarajado.get(i);
            //compruebo si son la misma imagen
            if(seleccion1 == seleccion2){
                imagenVista = null;
                tableroBloqueado = false;
                aciertos++;
                puntuacion++;
                txtPuntuacion.setText(getString(R.string.txt_puntuacion) + Integer.toString(puntuacion));
                //compruebo si ya están todas las imagenes destapadas
                if(aciertos == NUM_CASILLAS/2){
                    Toast.makeText(this, "Has ganado", Toast.LENGTH_LONG).show();
                    comprobarRecord();
                }
            }else{
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //no son la misma imagen
                        imagenVista.setImageResource(imagenFondo);
                        imagenVista.setEnabled(true);
                        imageButton.setImageResource(imagenFondo);
                        imageButton.setEnabled(true);
                        tableroBloqueado = false;
                        imagenVista = null;
                    }
                }, 1000);

            }
        }
    }
    private ArrayList<Integer> barajarImagenes(){
        ArrayList<Integer> listaBarajada = new ArrayList<Integer>();
        for (int i = 0 ; i < NUM_CASILLAS ; i++){
            listaBarajada.add(i%(NUM_CASILLAS / 2));
        }
        Log.d("Lista ordenada", Arrays.toString(listaBarajada.toArray()));
        Collections.shuffle(listaBarajada);
        Log.d("Lista barajada", Arrays.toString(listaBarajada.toArray()));
        return listaBarajada;
    }

    private void comprobarRecord(){
        String nombreJugador = "";
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        int record = sharedPref.getInt("record", 0);
        nombreJugador = sharedPref.getString("nombre", "");
        if(record < puntuacion){
            //nuevo record
            Toast.makeText(this, "Nuevo record", Toast.LENGTH_SHORT).show();
            //guardo el nuevo record

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("record", puntuacion);
            editor.putString("nombre", nombreJugador);
            editor.commit();
        }else{
            String mensajeRecord = "El record anterior es: " + Integer.toString(record);
            Toast.makeText(this, mensajeRecord, Toast.LENGTH_SHORT).show();
        }
    }

    //animación para la imagen
    public void animacionInOut(View view){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            int cx = view.getWidth() / 2;
            int cy = view.getHeight() / 2;
            float radius = view.getWidth();
            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, radius, 0);
            anim.setDuration(1000);
            Animator revealAnim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, radius);
            revealAnim.setDuration(1000);

            AnimatorSet set = new AnimatorSet();
            set.playSequentially(anim, revealAnim);
            set.start();
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }
            });
        }
    }

    public void animacionIn(View view){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            int cx = view.getWidth() / 2;
            int cy = view.getHeight() / 2;
            float radius = view.getWidth();
            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, radius, 0);
            anim.setDuration(1000);
            anim.start();
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }
            });
        }
    }

    public void animacionOut(View view){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            int cx = view.getWidth() / 2;
            int cy = view.getHeight() / 2;
            float radius = view.getWidth();
            Animator revealAnim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, radius);
            revealAnim.setDuration(1000);
            revealAnim.start();
        }
    }

    public void salirJuego(View view) {
        finish();
    }

}