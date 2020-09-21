package com.example.ozaar.spare.parts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignIn extends AppCompatActivity implements View.OnClickListener {

    private Button signUp_btn1,signUp_btn2;
    private ImageView logoImage;
    private TextView logoText,slogan;

    private TextInputLayout email,password;

    private FloatingActionButton signInBtnFab;

    ProgressDialog pd;


    FirebaseAuth mAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("User");


        /***********        initializing SignIn Button     *****************/

        signInBtnFab = findViewById(R.id.signIn_Btn);

        /***********        initializing TextInputLayout     *****************/


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);


        pd =new ProgressDialog(this);
        pd.setMessage("Loading...");
        /***********        initializing Animation Contents     *****************/

        logoImage = findViewById(R.id.logo_Image);
        logoText = findViewById(R.id.logo_name);
        slogan = findViewById(R.id.slogan);


        signUp_btn1 = findViewById(R.id.signUp_Btn1);
        signUp_btn2 = findViewById(R.id.signUp_Btn2);

        signUp_btn1.setOnClickListener(this);
        signUp_btn2.setOnClickListener(this);


        signInBtnFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mEmail = email.getEditText().getText().toString().trim();
                String mPassword = password.getEditText().getText().toString().trim();
                LoginUser(mEmail,mPassword);
            }
        });

    }

    public static boolean isNetworkAvaliable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                || (connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState() == NetworkInfo.State.CONNECTED)) {
            return true;
        } else {
            return false;
        }
    }


    private void LoginUser(final String mEmail, final String mPassword)
    {
        if (TextUtils.isEmpty(mEmail) || TextUtils.isEmpty(mPassword))
        {
            email.setError("Empty");
            password.setError("Empty");
            email.setErrorEnabled(true);
            password.setErrorEnabled(true);


        }
        else if (!EmailValidation(mEmail))
        {
            Toast.makeText(this, "Please put Valid Email", Toast.LENGTH_SHORT).show();
        }
        else if (!isNetworkAvaliable(getApplicationContext()))
        {
            Toast.makeText(this, "Please check Internet connection", Toast.LENGTH_SHORT).show();

        }
        else
        {
            email.setError(null);
            password.setError(null);
            email.setErrorEnabled(false);
            password.setErrorEnabled(false);

            pd.show();
            mAuth.signInWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        reference.child(mAuth.getUid().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists())
                                {
                                    String name = dataSnapshot.child("user_data").child("name").getValue(String.class);
                                    String phoneNumber = dataSnapshot.child("user_data").child("phoneNumber").getValue(String.class);
                                    if (Jason.jasonWriter(name,phoneNumber,mEmail,mPassword,getApplicationContext()) == true)
                                    {
                                        pd.dismiss();
                                        startActivity(new Intent(SignIn.this,MainActivity.class));
                                        finish();
                                    }
                                    else
                                    {
                                        pd.dismiss();
                                        Toast.makeText(SignIn.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    pd.dismiss();
                                    Toast.makeText(SignIn.this, "DataSnapShot Not Exist", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    else
                    {
                        pd.dismiss();
                        Toast.makeText(SignIn.this, task.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }


    public boolean EmailValidation(String mail)
    {

        try
        {

            Pattern pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher matcher = pattern.matcher(mail);
            return matcher.matches();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

    }


    public boolean checkTextInputBox(String mEmail, String mPassword)
    {
        if (TextUtils.isEmpty(mEmail) && TextUtils.isEmpty(mPassword))
        {
            email.setError("Empty");
            password.setError("Empty");
            email.setErrorEnabled(true);
            password.setErrorEnabled(true);
            return true;
        }
        else if (TextUtils.isEmpty(mEmail))
        {
            email.setError("Empty");
            email.setErrorEnabled(true);

            if (TextUtils.isEmpty(mPassword))
            {
                password.setError("Empty");
                password.setErrorEnabled(true);
            }
            else
            {
                password.setError(null);
                password.setErrorEnabled(false);

            }
            return true;

        }
        else if (TextUtils.isEmpty(mPassword))
        {
            password.setError("Empty");
            password.setErrorEnabled(true);

            if (TextUtils.isEmpty(mEmail))
            {
                email.setError("Empty");
                email.setErrorEnabled(true);
            }
            else
            {
                email.setError(null);
                email.setErrorEnabled(false);

            }
            return true;

        }
        else
        {
            email.setError(null);
            password.setError(null);
            email.setErrorEnabled(false);
            password.setErrorEnabled(false);
            return false;
        }

    }

    @Override
    public void onClick(View view) {
        if ((view.getId() == R.id.signUp_Btn1) || ((view.getId() == R.id.signUp_Btn2)))
        {
            Intent intent = new Intent(SignIn.this, SignUp.class);

            Pair[] pairs = new Pair[2];
            pairs[0] = new Pair<View, String>(logoImage, "logo_Image");
            pairs[1] = new Pair<View, String>(logoText, "logo_Text");


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignIn.this, pairs);
                startActivity(intent, options.toBundle());
                finish();

            }

        }
    }


}