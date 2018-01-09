package io.execube.monotype.walldo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    private TextView mStatusTextView;
    private FrameLayout frameLayout;

    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private Typeface SFUI;
    private Toolbar toolbar;
    private Typeface GothamRounded;

    private Button mBrowseButton,mFavouritesButton;
    private AnimationDrawable animationDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mStatusTextView=(TextView)findViewById(R.id.status_textview);
        toolbar=(Toolbar)findViewById(R.id.toolBar);
        frameLayout=(FrameLayout)findViewById(R.id.home_frameLayout);


        animationDrawable = (AnimationDrawable)frameLayout.getBackground();

        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        mBrowseButton=(Button)findViewById(R.id.browse_btn);
        mFavouritesButton=(Button)findViewById(R.id.favourites_btn);

        mFirebaseAnalytics= FirebaseAnalytics.getInstance(this);


        setSupportActionBar(toolbar);

        SFUI=Typeface.createFromAsset(getAssets(),"fonts/sftext.otf");
        GothamRounded=Typeface.createFromAsset(getAssets(),"fonts/Gotham-Rounded-Medium.ttf");

        mBrowseButton.setTypeface(GothamRounded);
        mFavouritesButton.setTypeface(GothamRounded);
        if(Build.VERSION.SDK_INT>=21) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }

        mStatusTextView.setTypeface(SFUI);

        mAuth=FirebaseAuth.getInstance();


        if (mAuth.getCurrentUser()==null)
        {

            startActivity(new Intent(Home.this,MainActivity.class));
            finish();
        }


        mAuthStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null)
                {

                    startActivity(new Intent(Home.this,MainActivity.class));
                    finish();
                }

            }
        };


        handleActionBar();
;

        String fullName=mAuth.getCurrentUser().getDisplayName();
        String[] name = fullName.split(" ");

        mStatusTextView.setText(getString(R.string.hey_there) +" "+ name[0] + ".");

        mBrowseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle= new Bundle();
                bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM,"Browsed Wallpapers");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH,bundle);

                startActivity(new Intent(Home.this,Browse.class));


            }
        });


        mFavouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,Favorites.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id== R.id.logout)
        {
            mAuth.signOut();
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    private void handleActionBar() {
        ActionBar actionBar= getSupportActionBar();


        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
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

//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
}
