package com.example.ozaar.spare.parts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.ozaar.spare.parts.Fragment.Fragment_Dashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import kotlin.Function;

public class AddItemsForOzaar implements onGetDataListener {

    TextInputLayout nameOfOzaar,purchasePrice,sellPrice,totalNumber;
    private FloatingActionButton addOzzar;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth mAuth;
    DatabaseReference mReference;
    Context mContext;

    private Dialog dialog;


    public AddItemsForOzaar(Dialog dialog, Context context) {


        this.mContext = context;
        this.dialog = dialog;
        dialog.show();

        /************************* Initializing FireBase ********************************************/
        if (database == null)
        {
            database.setPersistenceEnabled(true);
        }

        mAuth = FirebaseAuth.getInstance();
        mReference = database.getReference("User");




        /************************* Initializing TexInputLayout ********************************************/


        nameOfOzaar = (TextInputLayout) dialog.findViewById(R.id.ozaarName);
        purchasePrice = (TextInputLayout) dialog.findViewById(R.id.purchasePrice);
        sellPrice = (TextInputLayout) dialog.findViewById(R.id.sellPrice);
        totalNumber = (TextInputLayout) dialog.findViewById(R.id.totalNumber);

        /************************* Initializing Floating Action Button ********************************************/


        addOzzar = dialog.findViewById(R.id.addOzaar);
        addOzzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOzaartoFireBase();
            }
        });
    }


    public void addOzaartoFireBase()
    {
        String ozaarName = nameOfOzaar.getEditText().getText().toString().trim();
            String purchase = purchasePrice.getEditText().getText().toString().trim();
            String sell = sellPrice.getEditText().getText().toString().trim();
            String total = totalNumber.getEditText().getText().toString().trim();




            if (TextUtils.isEmpty(ozaarName) || TextUtils.isEmpty(purchase) ||TextUtils.isEmpty(sell) || TextUtils.isEmpty(total) )
            {
                nameOfOzaar.setError("Empty");
                nameOfOzaar.setErrorEnabled(true);
                purchasePrice.setError("Empty");
                purchasePrice.setErrorEnabled(true);
                sellPrice.setError("Empty");
                sellPrice.setErrorEnabled(true);
                totalNumber.setError("Empty");
                totalNumber.setErrorEnabled(true);
            }
            else if ( Integer.parseInt(purchase) <=0 )
            {
                nameOfOzaar.setError(null);
                nameOfOzaar.setErrorEnabled(false);
                sellPrice.setError(null);
                sellPrice.setErrorEnabled(false);


                purchasePrice.setError("Empty");
                purchasePrice.setErrorEnabled(true);


                totalNumber.setError(null);
                totalNumber.setErrorEnabled(false);

            }

            else if ( Integer.parseInt(sell) <=0 )
            {
                nameOfOzaar.setError(null);
                nameOfOzaar.setErrorEnabled(false);
                purchasePrice.setError(null);
                purchasePrice.setErrorEnabled(false);


                sellPrice.setError("Empty");
                sellPrice.setErrorEnabled(true);


                totalNumber.setError(null);
                totalNumber.setErrorEnabled(false);

            }
            else if ((Integer.parseInt(total) <= 0) )
            {
                nameOfOzaar.setError(null);
                nameOfOzaar.setErrorEnabled(false);
                purchasePrice.setError(null);
                purchasePrice.setErrorEnabled(false);


                totalNumber.setError("Empty");
                totalNumber.setErrorEnabled(true);


                sellPrice.setError(null);
                sellPrice.setErrorEnabled(false);

            }
            else
            {
                AddDatatoFireBase(ozaarName,purchase,sell,total);

            }
        }


    private void AddDatatoFireBase(String mOzaarName, String mPurchase, final String mSell, final String mTotal)
    {
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        final HashMap<String,Object> map=new HashMap<>();
        map.put("name_of_parts",mOzaarName);
        map.put("purchase_price",mPurchase);
        map.put("sell",mSell);
        map.put("Number_of_items",Integer.parseInt(mTotal));
        map.put("last_purchase_date",date);
        map.put("sold","0");


                    //
                    mReference.child(mAuth.getUid()).child("Parts").push().updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {

                                mReference.child(mAuth.getUid()).child("total").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists())
                                        {
                                            int temp=snapshot.getValue(Integer.class);
                                            temp+=Integer.parseInt(mTotal);
                                            mReference.child("total").setValue(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful())
                                                    {

                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(mContext, task.toString(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                mReference.child(mAuth.getUid()).child("expectedProfit").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists())
                                        {
                                            int temp=Integer.parseInt(snapshot.getValue().toString());

                                            temp+=Integer.parseInt(mSell);

                                            mReference.child("expectedProfit").setValue(String.valueOf(temp)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful())
                                                    {
                                                        dialog.dismiss();
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(mContext, task.toString(), Toast.LENGTH_LONG).show();

                                                    }
                                                }
                                            });
                                        }
                                        else
                                        {
                                            Toast.makeText(mContext, "Expected profit Not Exits", Toast.LENGTH_LONG).show();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                            else
                            {
                                Toast.makeText(mContext, task.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });




    }


    @Override
    public void onBegin() {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(String task) {

    }
}