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
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.JsonReader;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity implements onGetDataListener,View.OnClickListener {

    public boolean flag = false;

    ProgressDialog pd;
    Button signIn_btn1,signIn_btn2;

    FloatingActionButton login_SIGNUP;

    ImageView logoImage;
    TextView logoText,slogan;
    TextInputLayout fulName,phoneNumber,email,password;


    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);


        /***********    initializing Animation Contents      *****************/

        logoImage = findViewById(R.id.logo_Image);
        logoText = findViewById(R.id.logo_name);
        slogan = findViewById(R.id.slogan);

        /**************         initializing FireBase                 ***********************************  */

        mAuth = FirebaseAuth.getInstance();


        reference = database.getReference("User");
        reference.keepSynced(true);

        pd = new ProgressDialog(this);
        pd.setMessage("Loading....");
        pd = new ProgressDialog(this);



        /**************         initializing floatingAction button  */

        login_SIGNUP = (FloatingActionButton) findViewById(R.id.login_SIGNUP);

        login_SIGNUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String FullName = fulName.getEditText().getText().toString().trim();
                final String phone = phoneNumber.getEditText().getText().toString().trim();
                final String mEmail = email.getEditText().getText().toString();
                final String pass = password.getEditText().getText().toString();


                if (TextUtils.isEmpty(FullName) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(mEmail) || TextUtils.isEmpty(pass))
                {
                    fulName.setError("Empty");
                    email.setError("Empty");
                    phoneNumber.setError("Empty");
                    password.setError("Empty");

                }
//                if (checkTextInputEditText(FullName,phone,emaiL,pass))
//                {
//
//                }
                else if (!checkEmailValidation(mEmail))
                {
                    email.setError("Email is not valid");
                }
                else if (pass.length() < 3)
                {
                    password.setError("Password is too short");
                    password.requestFocus();
                }
                else if (!isNetworkAvaliable(getApplicationContext()))
                {
                    Toast.makeText(getApplicationContext(),"Please check Internet connection",Toast.LENGTH_LONG).show();

                }
                else
                {
                    fulName.setError(null);
                    fulName.setErrorEnabled(false);

                    phoneNumber.setError(null);
                    phoneNumber.setErrorEnabled(false);

                    email.setError(null);
                    email.setErrorEnabled(false);

                    password.setError(null);
                    password.setErrorEnabled(false);

                    final boolean[] check = new boolean[2];

                    final boolean check3;


                     if (Jason.jasonWriter(FullName,phone,mEmail,pass,getApplicationContext()) == true)
                     {
                         RegisterUser(FullName,phone,mEmail,pass);
                     }
                     else
                     {
                         Jason.DeleteFile(getApplicationContext());
                         Jason.jasonWriter(FullName,phone,mEmail,pass,getApplicationContext());
                         RegisterUser(FullName,phone,mEmail,pass);
                     }





                }



            }


        });





        /**************         initializing TextInputLayout               ***********************************  */

        fulName = findViewById(R.id.fullName);
        phoneNumber = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);



        /**************        Content                ***********************************  */

        signIn_btn1 = findViewById(R.id.signIn_btn1_SIGNUP);
       signIn_btn2 = findViewById(R.id.signIn_btn2_SIGNUP);

       signIn_btn1.setOnClickListener(this);
       signIn_btn2.setOnClickListener(this);




    }



    private void RegisterUser(final String name, final String mPhoneNumber, final String emaiL, final String pass) {

        onBegin();

        DatabaseReference mRef = database.getReference("User");

        Query query = mRef.orderByChild("email").equalTo(emaiL);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    Toast.makeText(SignUp.this, "Already have this email", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    mAuth.createUserWithEmailAndPassword(emaiL,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                HashMap<String,Object> map=new HashMap<>();
                                map.put("id",mAuth.getCurrentUser().getUid());
                                map.put("name",name);
                                map.put("phoneNumber",mPhoneNumber);
                                map.put("email",emaiL);
                                map.put("password",pass);

                                Parts parts=new Parts("0","0",0,0,map);

                                reference.child(mAuth.getCurrentUser().getUid()).setValue(parts).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            onSuccess();
                                        }
                                        else
                                        {
                                            onFailure(task.toString());

                                        }
                                    }
                                });
                            }
                            else
                            {


                                onFailure("This email is under use of an other account ");



                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    // check Internet

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

    public boolean checkEmailValidation(String mail)
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


    @Override
    public void onClick(View view) {
       if ((view.getId() == R.id.signIn_btn1_SIGNUP) || ((view.getId() == R.id.signIn_btn2_SIGNUP)))
        {
            Intent intent = new Intent(SignUp.this, SignIn.class);

            Pair[] pairs = new Pair[2];
            pairs[0] = new Pair<View, String>(logoImage, "logo_Image");
            pairs[1] = new Pair<View, String>(logoText, "logo_Text");


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this, pairs);
                startActivity(intent, options.toBundle());
                finish();


            }

        }

    }

    private  boolean checkTextInputEditText(String name,String mPhoneNumer,String mEmail,String mPassword)
    {

        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(mPhoneNumer) && TextUtils.isEmpty(mEmail) && TextUtils.isEmpty(mPassword))
        {
            fulName.setError("Empty");
            email.setError("Empty");
            phoneNumber.setError("Empty");
            password.setError("Empty");
           return true;
        }

        else if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(mPhoneNumer) && !TextUtils.isEmpty(mEmail) && !TextUtils.isEmpty(mPassword))
        {
           return  false;
        }

        else
        {
            boolean check = false;

            if (TextUtils.isEmpty(name))
            {
                fulName.setError("Empty");
                check = true;

                if (TextUtils.isEmpty(mPhoneNumer))
                {
                    phoneNumber.setError("Empty");
                }
                else
                {
                    phoneNumber.setError(null);
                    phoneNumber.setErrorEnabled(false);
                }

                if (TextUtils.isEmpty(mEmail))
                {
                    email.setError("Empty");

                }
                else
                {
                    email.setError(null);
                    email.setErrorEnabled(false);
                }

                if (TextUtils.isEmpty(mPassword))
                {
                    password.setError("Empty");


                }
                else
                {
                    password.setError(null);
                    password.setErrorEnabled(false);
                }

            }
            else
            {
                fulName.setError(null);
                fulName.setErrorEnabled(false);
            }

            if (TextUtils.isEmpty(mPhoneNumer))
            {
                phoneNumber.setError("Empty");
                check = true;

                if (TextUtils.isEmpty(name)) {
                    fulName.setError("Empty");
                }
                else
                {
                    fulName.setError(null);
                    fulName.setErrorEnabled(false);
                }

                if (TextUtils.isEmpty(mEmail))
                {
                    email.setError("Empty");

                }
                else
                {
                    email.setError(null);
                    email.setErrorEnabled(false);
                }

                if (TextUtils.isEmpty(mPassword))
                {
                    password.setError("Empty");


                }
                else
                {
                    password.setError(null);
                    password.setErrorEnabled(false);
                }


            }

            if (TextUtils.isEmpty(mEmail))
            {
                email.setError("Empty");
                check = true;

                if (TextUtils.isEmpty(name))
                {
                    fulName.setError("Empty");
                }
                else
                {
                    fulName.setError(null);
                    fulName.setErrorEnabled(false);
                }

                if (TextUtils.isEmpty(mPhoneNumer))
                {
                    phoneNumber.setError("Empty");}
                else
                {
                    phoneNumber.setError(null);
                    phoneNumber.setErrorEnabled(false);
                }

                if (TextUtils.isEmpty(mPassword))
                {
                    password.setError("Empty");

                }
                else
                {
                    password.setError(null);
                    password.setErrorEnabled(false);
                }


            }
            else
            {
                email.setError(null);
                email.setErrorEnabled(false);
            }

            if (TextUtils.isEmpty(mPassword))
            {
                password.setError("Empty");
                check = true;

                if (TextUtils.isEmpty(name)) {
                    fulName.setError("Empty");
                }
                else
                {
                    fulName.setError(null);
                    fulName.setErrorEnabled(false);
                }

                if (TextUtils.isEmpty(mPhoneNumer))
                {
                    phoneNumber.setError("Empty");}
                else
                {
                    phoneNumber.setError(null);
                    phoneNumber.setErrorEnabled(false);
                }

                if (TextUtils.isEmpty(mEmail)) {
                    email.setError("Empty");
                }
                else
                {
                    email.setError(null);
                    email.setErrorEnabled(false);
                }


            }
            else
            {
                password.setError(null);
                password.setErrorEnabled(false);
            }
            return check;
        }




    }





    @Override
    public void onBegin() {

        pd.show();
        pd.setMessage("Loading....");
    }

    @Override
    public void onSuccess() {
        pd.dismiss();
        Intent intent=new Intent(SignUp.this,MainActivity.class);
        startActivity(intent);
        finish();


    }

    @Override
    public void onFailure(String task) {

        pd.dismiss();
        Toast.makeText(this, task, Toast.LENGTH_SHORT).show();
    }


}