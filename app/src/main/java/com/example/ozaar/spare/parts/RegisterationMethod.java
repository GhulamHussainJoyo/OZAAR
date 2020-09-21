package com.example.ozaar.spare.parts;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterationMethod extends AppCompatActivity {


    public static Context context ;
    Button signIn_btn,signUp_btn;
    ImageView logoImage;
    TextView logoText,slogan;


    public static Context getMyContext()
    {
        return context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registeration_method_activity);

        logoImage = findViewById(R.id.logo_Image);
        logoText = findViewById(R.id.logo_name);
        slogan = findViewById(R.id.slogan);


        signIn_btn = (Button) findViewById(R.id.signIn_Btn);
        signUp_btn = (Button) findViewById(R.id.signUp_btn);



        signIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RegisterationMethod.this, SignIn.class);

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(logoImage, "logo_Image");
                pairs[1] = new Pair<View, String>(logoText, "logo_Text");


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterationMethod.this, pairs);
                    startActivity(intent, options.toBundle());


                }

            }
        });

        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RegisterationMethod.this, SignUp.class);

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(logoImage, "logo_Image");
                pairs[1] = new Pair<View, String>(logoText, "logo_Text");


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterationMethod.this, pairs);
                    startActivity(intent, options.toBundle());


                }


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() !=null)
        {
            startActivity(new Intent(this,MainActivity.class));
        }
    }


}