package dz.tdm.esi.myapplication.Activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import dz.tdm.esi.myapplication.DAO.DossierDAO;
import dz.tdm.esi.myapplication.R;
import dz.tdm.esi.myapplication.Util.Dialog.SweetAlertDialog;
import dz.tdm.esi.myapplication.Util.FireBaseDB;
import dz.tdm.esi.myapplication.Util.ImageCache.ImageLoader;
import dz.tdm.esi.myapplication.models.Dossier;
import dz.tdm.esi.myapplication.models.EtatDossier;
import dz.tdm.esi.myapplication.models.User;

import static java.lang.Math.round;

public class FolderDetail extends AppCompatActivity {

    TextView date, montant, nomAdver, nPermisAdvers, vehiculeAdvers, matriculeAdvers;
    ImageView photo;
    VideoView videoPlay;
    DossierDAO dossierDAO;
    Dossier dossier;
    private FirebaseAuth mAuth;
    Button updateFolder, sendFolder;
    FirebaseUser currentUser;
    SweetAlertDialog pDialog;
    private ImageLoader imgLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_detail);

        dossierDAO = new DossierDAO(this);
        imgLoader = new ImageLoader(this);
        String notification = FirebaseInstanceId.getInstance().getToken();
        System.out.println(notification);




        date = (TextView)findViewById(R.id.date);
        montant = (TextView)findViewById(R.id.montant);
        nomAdver = (TextView)findViewById(R.id.nomAdver);
        nPermisAdvers = (TextView)findViewById(R.id.nPermisAdvers);
        vehiculeAdvers = (TextView)findViewById(R.id.vehiculeAdvers);
        matriculeAdvers = (TextView)findViewById(R.id.matriculeAdvers);

        updateFolder = (Button)findViewById(R.id.updateFolder);
        sendFolder = (Button)findViewById(R.id.sendFolder);

        photo = (ImageView)findViewById(R.id.photo);
        videoPlay = (VideoView)findViewById(R.id.videoPlay);

        Long id = null;
        Boolean aBoolean = null;
        String folder = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id  = extras.getLong("id_folder",0);
            aBoolean  = extras.getBoolean("btn");
            folder  = extras.getString("folder",null);
        }

        if (aBoolean){
            Gson gson = new Gson();
            updateFolder.setVisibility(View.GONE);
            sendFolder.setVisibility(View.GONE);
            dossier = gson.fromJson(folder, Dossier.class);
        }else{
            dossier = dossierDAO.selectionner(id);
        }



        date.setText(dossier.getDate());
        montant.setText(dossier.getMontant());
        if (dossier.getImageURL().contains("https")){
            Picasso.with(this).load(dossier.getImageURL()).into(photo);
            //imgLoader.DisplayImage(dossier.getImageURL(), photo);

        }else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeFile(dossier.getImageURL(), options);
            photo.setImageBitmap(bitmap);
        }
        videoPlay.setVideoPath(dossier.getVideoURL());
        MediaController videoMediaController = new MediaController(this);
        videoMediaController.setMediaPlayer(videoPlay);
        videoPlay.setMediaController(videoMediaController);
        videoPlay.requestFocus();
        nomAdver.setText(dossier.getNomAdversaire().isEmpty()?" ":dossier.getNomAdversaire());
        nPermisAdvers.setText(dossier.getNumPermisAdversaire().isEmpty()?" ":dossier.getNumPermisAdversaire());
        vehiculeAdvers.setText(dossier.getVehiculeAdversaire().isEmpty()?" ":dossier.getVehiculeAdversaire());
        matriculeAdvers.setText(dossier.getMatriculeAdversaire().isEmpty()?" ":dossier.getMatriculeAdversaire());

        updateFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FolderDetail.this, FolderUpdate.class);
                Long cpt = Long.parseLong(String.valueOf(dossier.getId()));
                intent.putExtra("id_folder", cpt);
                startActivity(intent);
            }
        });

        sendFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   pDialog = new SweetAlertDialog(FolderDetail.this, SweetAlertDialog.PROGRESS_TYPE)
                        .setTitleText(getString(R.string.LOADING));*/
                //pDialog.show();
                dossier.setEtat(EtatDossier.envoye);
                mAuth = FirebaseAuth.getInstance();
                currentUser = mAuth.getCurrentUser();
                if (currentUser != null){

                    Uri image = Uri.fromFile(new File(dossier.getImageURL()));
                    String[] strings = image.getPath().split("/");
                    StorageReference riversRef = FireBaseDB.getMyStorageRef().child("AssurVoiture").child(currentUser.getUid()).child("folders").child(strings[strings.length-1]);

                    final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
                    mBuilder.setSmallIcon(R.drawable.ic_email_black_24dp)
                            .setContentTitle("Envoi de l'image")
                            .setOngoing(true)
                            .setAutoCancel(true)
                            .setProgress(100,0,false);
                    final NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                    riversRef.putFile(image)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @SuppressWarnings("VisibleForTests")
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Get a URL to the uploaded content

                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }



                                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                    dossier.setImageURL(downloadUrl.getScheme()+":"+downloadUrl.getEncodedSchemeSpecificPart());
                                    System.out.println(dossier.getImageURL());
                                    mBuilder.setProgress(100,100,false).setContentText("Upload de l'image terminé !")
                                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                            .setAutoCancel(true);
                                    notificationManager.notify(1010, mBuilder.build());
                                    new sendToFireBase().execute();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    // ...
                                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
                                    mBuilder.setSmallIcon(R.drawable.add)
                                            .setContentTitle("Erreur dans l'envoi de l'image")
                                            .setContentText("L' image  n'a été envoyée sur Firebase")
                                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                            .setAutoCancel(true);
                                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.notify(1010, mBuilder.build());
                                }
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            @SuppressWarnings("VisibleForTests") float progression = ((float)taskSnapshot.getBytesTransferred()/(float)taskSnapshot.getTotalByteCount())*100;
                            int val=round(progression);
                            //noinspection VisibleForTests
                            Log.d("**Valeurs**", String.valueOf(taskSnapshot.getBytesTransferred())+ " " + String.valueOf(taskSnapshot.getTotalByteCount())+ "= "+val);
                            mBuilder.setProgress(100,val,false);
                            notificationManager.notify(1010, mBuilder.build());

                        }
                    });;

                    Uri video = Uri.fromFile(new File(dossier.getVideoURL()));
                    strings = video.getPath().split("/");
                    riversRef = FireBaseDB.getMyStorageRef().child("AssurVoiture").child(currentUser.getUid()).child("folders").child(strings[strings.length-1]);

                    final NotificationCompat.Builder mBuilderBuilder = new NotificationCompat.Builder(getApplicationContext());
                    mBuilder.setSmallIcon(R.drawable.ic_email_black_24dp)
                            .setContentTitle("Envoi de la video")
                            .setOngoing(true)
                            .setAutoCancel(true)
                            .setProgress(100,0,false);
                    final NotificationManager notificationManager1 = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);


                    riversRef.putFile(video)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @SuppressWarnings("VisibleForTests")
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }



                                    // Get a URL to the uploaded content
                                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                    dossier.setVideoURL(downloadUrl.getScheme()+":"+downloadUrl.getEncodedSchemeSpecificPart());
                                    System.out.println(dossier.getVideoURL());

                                    mBuilderBuilder.setProgress(100,100,false).setContentText("Upload de la video terminé !")
                                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                            .setAutoCancel(true);
                                    notificationManager1.notify(1011, mBuilderBuilder.build());

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
                                    mBuilder.setSmallIcon(R.drawable.add)
                                            .setContentTitle("Erreur dans l'envoi de la video")
                                            .setContentText("L' image  n'a été envoyée sur Firebase")
                                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                            .setAutoCancel(true);
                                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.notify(1011, mBuilder.build());
                                }
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            @SuppressWarnings("VisibleForTests") float progression = ((float)taskSnapshot.getBytesTransferred()/(float)taskSnapshot.getTotalByteCount())*100;
                            int val=round(progression);
                            //noinspection VisibleForTests
                            Log.d("**Valeurs**", String.valueOf(taskSnapshot.getBytesTransferred())+ " " + String.valueOf(taskSnapshot.getTotalByteCount())+ "= "+val);
                            mBuilderBuilder.setProgress(100,val,false);
                            notificationManager1.notify(1011, mBuilderBuilder.build());

                        }
                    });;
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


        Long id = null;
        Boolean aBoolean = null;
        String folder = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id  = extras.getLong("id_folder",0);
            aBoolean  = extras.getBoolean("btn");
            folder  = extras.getString("folder",null);
        }

        if (aBoolean){
            Gson gson = new Gson();
            updateFolder.setVisibility(View.GONE);
            sendFolder.setVisibility(View.GONE);
            dossier = gson.fromJson(folder, Dossier.class);
        }else{
            dossier = dossierDAO.selectionner(id);
        }

        date.setText(dossier.getDate());
        montant.setText(dossier.getMontant());
        if (dossier.getImageURL().contains("https")){
            Picasso.with(this).load(dossier.getImageURL()).into(photo);
          //  imgLoader.DisplayImage(dossier.getImageURL(), photo);
        }else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeFile(dossier.getImageURL(), options);
            photo.setImageBitmap(bitmap);
        }

        videoPlay.setVideoPath(dossier.getVideoURL());
        MediaController videoMediaController = new MediaController(this);
        videoMediaController.setMediaPlayer(videoPlay);
        videoPlay.setMediaController(videoMediaController);
        videoPlay.requestFocus();
        nomAdver.setText(dossier.getNomAdversaire().isEmpty()?" ":dossier.getNomAdversaire());
        nPermisAdvers.setText(dossier.getNumPermisAdversaire().isEmpty()?" ":dossier.getNumPermisAdversaire());
        vehiculeAdvers.setText(dossier.getVehiculeAdversaire().isEmpty()?" ":dossier.getVehiculeAdversaire());
        matriculeAdvers.setText(dossier.getMatriculeAdversaire().isEmpty()?" ":dossier.getMatriculeAdversaire());

    }

    private class sendToFireBase extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected String doInBackground(String... params) {
            //Create data to send to server

            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            DatabaseReference myRef = FireBaseDB.getMyRef().child("AssurVoiture").child(currentUser.getUid()).child("folders").child(String.valueOf(dossier.getId()));
            myRef.keepSynced(true);
            myRef.setValue(dossier);
            dossierDAO.supprimer(dossier.getId());
           /* pDialog.setTitleText("Success")
                    .setConfirmText("OK")
                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);*/
            onBackPressed();
        }
    }
}
