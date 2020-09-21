package com.example.ozaar.spare.parts;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Splash_Screen extends AppCompatActivity {


    public static int FULL_SCREEN = 7000;

    Animation topAnimFadeIn,bottomAnimFadeIn,topAnimFadeOut,bottomAnimFadeOut;
    ImageView logoImage;
    TextView logoText,slogan;

    LinearLayout lLayout;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database.setPersistenceEnabled(true);


        setContentView(R.layout.splash__screen_activity);




        topAnimFadeIn = AnimationUtils.loadAnimation(this,R.anim.top_animation_fade_in);
        bottomAnimFadeIn = AnimationUtils.loadAnimation(this,R.anim.bottom_animation_fade_in);

        topAnimFadeOut = AnimationUtils.loadAnimation(this,R.anim.top_anim_fade_out);
        bottomAnimFadeOut = AnimationUtils.loadAnimation(this,R.anim.bottom_animation_fade_out);

        logoImage = findViewById(R.id.logo_Image);
        logoText = findViewById(R.id.logo_name);
        slogan = findViewById(R.id.slogan);



      logoImage.startAnimation(topAnimFadeIn);
        logoText.setAnimation(bottomAnimFadeIn);
        slogan.setAnimation(bottomAnimFadeIn);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                logoImage.startAnimation(topAnimFadeOut);
                logoText.startAnimation(bottomAnimFadeOut);
                slogan.startAnimation(bottomAnimFadeOut);


                logoImage.setVisibility(View.INVISIBLE);
                logoText.setVisibility(View.INVISIBLE);
                slogan.setVisibility(View.INVISIBLE);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        startActivity(new Intent(Splash_Screen.this,RegisterationMethod.class));
                        finish();
                    }
                },1000);






            }
        },FULL_SCREEN);


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(Splash_Screen.this,RegisterationMethod.class);
//
//                Pair[] pairs=new Pair[2];
//                pairs[0] = new Pair<View,String>(logoImage,"logo_Image");
//                pairs[1] = new Pair<View,String>(logoText,"logo_Text");
//
//
//                logoImage.setAnimation(topAnimFadeOut);
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                    ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation(Splash_Screen.this,pairs);
//
//                    startActivity(intent,options.toBundle());
//
//
//                }
//            }
//        },FULL_SCREEN);





    }


    public void Handler() {

        Intent intent = new Intent(Splash_Screen.this, RegisterationMethod.class);

        Pair[] pairs = new Pair[2];
        pairs[0] = new Pair<View, String>(logoImage, "logo_Image");
        pairs[1] = new Pair<View, String>(logoText, "logo_Text");


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Splash_Screen.this, pairs);
            startActivity(intent, options.toBundle());


        }
    }


}