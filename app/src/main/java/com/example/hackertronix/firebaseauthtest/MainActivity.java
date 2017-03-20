package com.example.hackertronix.firebaseauthtest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private Button mSigninButton;
    private ProgressDialog progressDialog;


    private GoogleApiClient mGoogleApiClient;

    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private static final int RC_SIGN_IN=9001;
    public static final String TAG="SigninActivity";

    private TextView titleTextView;
    private Typeface Signalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleTextView=(TextView)findViewById(R.id.title_tv);

        Fabric.with(this, new Crashlytics());
        Fabric.with(this, new Answers());

        progressDialog= new ProgressDialog(this);

        mAuth=FirebaseAuth.getInstance();

        Signalist=Typeface.createFromAsset(getAssets(),"fonts/Signalist.otf");
        titleTextView.setTypeface(Signalist);

        titleTextView.setContentDescription("Hey There, Welcome to Walldo");



        mFirebaseAnalytics= FirebaseAnalytics.getInstance(this);

        if(mAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(),Home.class));
            finish();
        }


        mAuthStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser()!=null)
                {

                    startActivity(new Intent(MainActivity.this,Home.class));
                    finish();
                }

            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient= new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();


        mSigninButton=(Button)findViewById(R.id.signin_button);



        mSigninButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isNetworkAvaialable()){

                signIn();
                }
                else if(!isNetworkAvaialable())
                {
                    showAlertDialog();
                }
            }
        });
    }

    private boolean isNetworkAvaialable() {

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showAlertDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Whoah there chief!")
                .setMessage("Walldo needs internet connectivity. Please retry after connecting.")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(isNetworkAvaialable())
                {
                    signIn();
                }

                else{
                    showAlertDialog();
                }
            }
        }).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).create().show();
    }


    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }






    private void signIn() {

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SIGN_UP_METHOD,"Google Sign in");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP,bundle);

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);

        progressDialog.setMessage("Signing in....");
        progressDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        
        if(requestCode== RC_SIGN_IN)
        {
            GoogleSignInResult result= Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess())
            {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
            else{
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());

                alertDialog.setTitle("Google Sign in failed")
                        .setMessage(R.string.google_signin_failed)
                        .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).show();
            }
        }
        }
        
        


    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {



        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressDialog.dismiss();
                        if(task.isSuccessful())
                        {
                            startActivity(new Intent(getApplicationContext(),Home.class));
                            finish();
                        }




                    }
                });

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


        Log.d(TAG,"onConnectionFailed: "+connectionResult);
    }

}













