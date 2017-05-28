package dz.tdm.esi.myapplication.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Calendar;

import dz.tdm.esi.myapplication.DAO.DossierDAO;
import dz.tdm.esi.myapplication.R;
import dz.tdm.esi.myapplication.Util.Dialog.SweetAlertDialog;
import dz.tdm.esi.myapplication.Util.FireBaseDB;
import dz.tdm.esi.myapplication.Util.ImageTaker;
import dz.tdm.esi.myapplication.Util.Util;
import dz.tdm.esi.myapplication.Util.VideoTaker;
import dz.tdm.esi.myapplication.models.Dossier;
import dz.tdm.esi.myapplication.models.EtatDossier;

import static dz.tdm.esi.myapplication.Util.VideoTaker.REQUEST_VIDEO_CAPTURE;

public class FolderUpdate extends AppCompatActivity {

    DossierDAO dossierDAO;
    Dossier dossier;

    CheckBox adversaire;
    CardView adversaire1, adversaire2, adversaire3, adversaire4;
    TextView date;
    ImageView photo, video,refresh;
    VideoView videoPlay;
    EditText montant, nomAdver, nPermisAdvers, vehiculeAdvers, matriculeAdvers;
    Button addFolder;
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_update);

        dossierDAO = new DossierDAO(this);

        Long id = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id  = extras.getLong("id_folder");
        }

        dossier = dossierDAO.selectionner(id);


        adversaire = (CheckBox) findViewById(R.id.adversaire);
        adversaire1 = (CardView) findViewById(R.id.adversaire1);
        adversaire1.setVisibility(View.GONE);
        adversaire2 = (CardView) findViewById(R.id.adversaire2);
        adversaire2.setVisibility(View.GONE);
        adversaire3 = (CardView) findViewById(R.id.adversaire3);
        adversaire3.setVisibility(View.GONE);
        adversaire4 = (CardView) findViewById(R.id.adversaire4);
        adversaire4.setVisibility(View.GONE);

        date = (TextView) findViewById(R.id.date);

        photo = (ImageView)findViewById(R.id.photo);
        refresh = (ImageView)findViewById(R.id.refresh);
        video = (ImageView)findViewById(R.id.video);
        video.setVisibility(View.GONE);
        refresh.setVisibility(View.VISIBLE);

        videoPlay = (VideoView)findViewById(R.id.videoPlay);
        videoPlay.setVisibility(View.VISIBLE);

        montant = (EditText) findViewById(R.id.montant);
        nomAdver = (EditText) findViewById(R.id.nomAdver);
        nPermisAdvers = (EditText) findViewById(R.id.nPermisAdvers);
        vehiculeAdvers = (EditText) findViewById(R.id.vehiculeAdvers);
        matriculeAdvers = (EditText) findViewById(R.id.matriculeAdvers);

        addFolder = (Button) findViewById(R.id.addFolder);
        addFolder.setText("Enregistrer");

        if (dossier.getNomAdversaire().length()>1){
            adversaire1.setVisibility(View.VISIBLE);
            adversaire2.setVisibility(View.VISIBLE);
            adversaire3.setVisibility(View.VISIBLE);
            adversaire4.setVisibility(View.VISIBLE);
        }

        adversaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adversaire.isChecked()){
                    adversaire1.setVisibility(View.VISIBLE);
                    adversaire2.setVisibility(View.VISIBLE);
                    adversaire3.setVisibility(View.VISIBLE);
                    adversaire4.setVisibility(View.VISIBLE);
                }else {
                    adversaire1.setVisibility(View.GONE);
                    adversaire2.setVisibility(View.GONE);
                    adversaire3.setVisibility(View.GONE);
                    adversaire4.setVisibility(View.GONE);
                }
            }
        });


        date.setText(dossier.getDate());
        montant.setText(dossier.getMontant());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bitmap = BitmapFactory.decodeFile(dossier.getImageURL(), options);
        photo.setImageBitmap(bitmap);

        nomAdver.setText(dossier.getNomAdversaire().isEmpty()?" ":dossier.getNomAdversaire());
        nPermisAdvers.setText(dossier.getNumPermisAdversaire().isEmpty()?" ":dossier.getNumPermisAdversaire());
        vehiculeAdvers.setText(dossier.getVehiculeAdversaire().isEmpty()?" ":dossier.getVehiculeAdversaire());
        matriculeAdvers.setText(dossier.getMatriculeAdversaire().isEmpty()?" ":dossier.getMatriculeAdversaire());


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(FolderUpdate.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dossier.setDate("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year + "");
                                date.setText(dossier.getDate());
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoTaker.dispatchTakeVideoIntent(FolderUpdate.this,videoPlay);
            }
        });

        videoPlay.setVideoPath(dossier.getVideoURL());
        MediaController videoMediaController = new MediaController(this);
        videoMediaController.setMediaPlayer(videoPlay);
        videoPlay.setMediaController(videoMediaController);
        videoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPlay.start();
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageTaker.captureImage(FolderUpdate.this,photo);
            }
        });

        addFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dossier.getDate().isEmpty() || dossier.getImageURL().isEmpty()
                        || dossier.getVideoURL().isEmpty() || montant.getText().toString().isEmpty()){
                    Util.alert(FolderUpdate.this,"Veuillez remplir les champs qui manque").show();
                }else {
                    dossier.setEtat(EtatDossier.ouvert);
                    dossier.setMontant(montant.getText().toString());
                    if(adversaire.isChecked()){
                        if (nomAdver.getText().toString().isEmpty() || nPermisAdvers.getText().toString().isEmpty() ||
                                vehiculeAdvers.getText().toString().isEmpty() || matriculeAdvers.getText().toString().isEmpty()){
                            Util.alert(FolderUpdate.this,"Veuillez remplir les champs concernant l'adversaire").show();
                        }else {
                            dossier.setNomAdversaire(nomAdver.getText().toString());
                            dossier.setMatriculeAdversaire(matriculeAdvers.getText().toString());
                            dossier.setNumPermisAdversaire(nPermisAdvers.getText().toString());
                            dossier.setVehiculeAdversaire(vehiculeAdvers.getText().toString());



                            dossierDAO.modifier(dossier);
                            onBackPressed();
                        }
                    }else {
                        dossier.setNomAdversaire(nomAdver.getText().toString());
                        dossier.setMatriculeAdversaire(matriculeAdvers.getText().toString());
                        dossier.setNumPermisAdversaire(nPermisAdvers.getText().toString());
                        dossier.setVehiculeAdversaire(vehiculeAdvers.getText().toString());

                        dossierDAO.modifier(dossier);
                        onBackPressed();
                    }
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            dossier.setVideoURL(VideoTaker.onActivityResult(requestCode,resultCode,data));
            videoPlay.setVideoPath(dossier.getVideoURL());
            MediaController videoMediaController = new MediaController(this);
            videoMediaController.setMediaPlayer(videoPlay);
            videoPlay.setMediaController(videoMediaController);
            videoPlay.requestFocus();
        }else {
            dossier.setImageURL(ImageTaker.onActivityResult(requestCode,resultCode,data));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        addFolder.setText("Enregistrer");

        if (dossier.getNomAdversaire().length()>1){
            adversaire1.setVisibility(View.VISIBLE);
            adversaire2.setVisibility(View.VISIBLE);
            adversaire3.setVisibility(View.VISIBLE);
            adversaire4.setVisibility(View.VISIBLE);
        }

        adversaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adversaire.isChecked()){
                    adversaire1.setVisibility(View.VISIBLE);
                    adversaire2.setVisibility(View.VISIBLE);
                    adversaire3.setVisibility(View.VISIBLE);
                    adversaire4.setVisibility(View.VISIBLE);
                }else {
                    adversaire1.setVisibility(View.GONE);
                    adversaire2.setVisibility(View.GONE);
                    adversaire3.setVisibility(View.GONE);
                    adversaire4.setVisibility(View.GONE);
                }
            }
        });

        date.setText(dossier.getDate());
        montant.setText(dossier.getMontant());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bitmap = BitmapFactory.decodeFile(dossier.getImageURL(), options);
        photo.setImageBitmap(bitmap);

        nomAdver.setText(dossier.getNomAdversaire().isEmpty()?" ":dossier.getNomAdversaire());
        nPermisAdvers.setText(dossier.getNumPermisAdversaire().isEmpty()?" ":dossier.getNumPermisAdversaire());
        vehiculeAdvers.setText(dossier.getVehiculeAdversaire().isEmpty()?" ":dossier.getVehiculeAdversaire());
        matriculeAdvers.setText(dossier.getMatriculeAdversaire().isEmpty()?" ":dossier.getMatriculeAdversaire());

        videoPlay.setVideoPath(dossier.getVideoURL());
        MediaController videoMediaController = new MediaController(this);
        videoMediaController.setMediaPlayer(videoPlay);
        videoPlay.setMediaController(videoMediaController);

    }
}
