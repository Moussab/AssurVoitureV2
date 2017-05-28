package dz.tdm.esi.myapplication.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import dz.tdm.esi.myapplication.R;
import dz.tdm.esi.myapplication.Util.FireBaseDB;
import dz.tdm.esi.myapplication.Util.Util;
import dz.tdm.esi.myapplication.models.User;

public class Login extends AppCompatActivity  implements
        GoogleApiClient.OnConnectionFailedListener {

    User user = null;

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {


        mAuth = FirebaseAuth.getInstance();
        getUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();



        /*** Google Auth ***/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });



        System.out.println(this.user);



    }

    private void getUser(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            DatabaseReference myRef = FireBaseDB.getMyRef().child("AssurVoiture").child(currentUser.getUid()).child("client");

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Login.this.user = dataSnapshot.getValue(User.class);
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if (currentUser != null && Login.this.user != null ) {
                        if (Login.this.user.getEmail().toString().compareTo(currentUser.getEmail().toString()) == 0 ||
                                Login.this.user.getUserName().toString().compareTo(currentUser.getDisplayName().toString()) == 0) {

                            Intent it = new Intent(Login.this, FolderList.class);
                            startActivity(it);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });


        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onStart() {
        super.onStart();
        getUser();
    }

    private void login(User user){
        if (user != null ){
            Gson gson = new Gson();
            String jsonInString = gson.toJson(user);
            SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
            editor.putString("user", jsonInString);
            editor.commit();

            DatabaseReference myRef = FireBaseDB.getMyRef().child("AssurVoiture").child(user.getToken()).child("client");
            myRef.keepSynced(true);
            myRef.setValue(user);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String notification = FirebaseInstanceId.getInstance().getToken();
                            User user_ = new User(user.getDisplayName(),user.getEmail(),user.getUid(),notification );
                            login(user_);
                            Intent it = new Intent(Login.this, FolderList.class);
                            startActivity(it);
                           // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if (currentUser != null){
                                if (user.getEmail().toString().compareTo(currentUser.getEmail().toString()) == 0 ||
                                        user.getUserName().toString().compareTo(currentUser.getDisplayName().toString()) == 0 ){
                                    Intent it = new Intent(Login.this, FolderList.class);
                                    startActivity(it);
                                }
                            }else {
                                Util.alert(Login.this, "Veuillez connecter pour retrouver votre compte ou pour le creer.").show();
                            }
                        }
                    }
                });
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        //updateUI(null);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        //updateUI(null);
                    }
                });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        //Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

}
