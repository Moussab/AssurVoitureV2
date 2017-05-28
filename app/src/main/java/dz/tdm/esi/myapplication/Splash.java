package dz.tdm.esi.myapplication;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import dz.tdm.esi.myapplication.Activities.FolderList;
import dz.tdm.esi.myapplication.Activities.Login;
import dz.tdm.esi.myapplication.Service.GeofenceTransitionsIntentService;
import dz.tdm.esi.myapplication.Util.FireBaseDB;

public class Splash extends AppCompatActivity implements  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;
    public static final float GEOFENCE_RADIUS_IN_METERS = 1609;
    private static final String TAG ="dz.tdm.esi.myapplication";
    private static int SPLASH_TIME_OUT = 3000;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private PendingIntent mGeofencePendingIntent;

    ArrayList<Geofence> mGeofenceList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        final ConnectivityManager connMgr = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (!mobile.isConnectedOrConnecting ()) {
            /*Activer la synchro*/
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        mGeofenceList = new ArrayList<Geofence>();


        populateGeofenceList();


        // Kick off the request to build GoogleApiClient.


        new Handler().postDelayed(new Runnable() {



            @Override
            public void run() {

                Intent i = new Intent(Splash.this, Login.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    protected void onStart() {
       // buildGoogleApiClient();
        //mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
    private void logSecurityException(SecurityException securityException) {
        Log.e(TAG, "Invalid location permission. " +
                "You need to use ACCESS_FINE_LOCATION with geofences", securityException);
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);// FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Toast.makeText(this, String.valueOf(mLastLocation.getLatitude()+" "+String.valueOf(mLastLocation.getLongitude())), Toast.LENGTH_SHORT).show();
            // mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
            Log.d(TAG, String.valueOf(mLastLocation.getLatitude()+" "+String.valueOf(mLastLocation.getLongitude())) );
            SharedPreferences.Editor editor = getSharedPreferences("position", MODE_PRIVATE).edit();
            editor.putString("latitude", String.valueOf(mLastLocation.getLatitude()));
            editor.putString("longitude", String.valueOf(mLastLocation.getLongitude()));
            editor.commit();
        }
        if (!mGoogleApiClient.isConnected()) {
            Log.e("youcef","error");
        }
        if(mGeofenceList.size()>0){
            try {
                LocationServices.GeofencingApi.addGeofences(
                        mGoogleApiClient,
                        getGeofencingRequest(),
                        getGeofencePendingIntent()
                );
            } catch (SecurityException securityException) {
                logSecurityException(securityException);
            }
        }else {
            try {
                Thread.sleep(1000);
                LocationServices.GeofencingApi.addGeofences(
                        mGoogleApiClient,
                        getGeofencingRequest(),
                        getGeofencePendingIntent()
                );
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (SecurityException securityException) {
                logSecurityException(securityException);
            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    public void populateGeofenceList() {

        DatabaseReference myRef = FireBaseDB.getMyRef().child("Geofence");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                mGeofenceList.clear();
                long cpt = dataSnapshot.getChildrenCount();
                for (int i = 1; i<= cpt; i++){
                    DataSnapshot geofenceSnapshot = dataSnapshot.child(String.valueOf(i));
                    mGeofenceList.add(new Geofence.Builder()
                            .setRequestId(String.valueOf(geofenceSnapshot.child("key").getValue()))
                            .setCircularRegion (Double.valueOf(geofenceSnapshot.child("latitude").getValue().toString()), Double.valueOf(geofenceSnapshot.child("longitude").getValue().toString()), GEOFENCE_RADIUS_IN_METERS)
                            .setExpirationDuration(GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                    Geofence.GEOFENCE_TRANSITION_EXIT)
                            .build());
                }
                buildGoogleApiClient();
                mGoogleApiClient.connect();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }
    private PendingIntent getGeofencePendingIntent() {
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
