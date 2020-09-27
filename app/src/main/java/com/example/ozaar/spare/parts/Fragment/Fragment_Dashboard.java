package com.example.ozaar.spare.parts.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.ozaar.spare.parts.Jason;
import com.example.ozaar.spare.parts.MainActivity;
import com.example.ozaar.spare.parts.PartsAdapter;
import com.example.ozaar.spare.parts.PartsAdapterForShortItems;
import com.example.ozaar.spare.parts.R;
import com.example.ozaar.spare.parts.RegisterationMethod;
import com.example.ozaar.spare.parts.StatusManger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static com.example.ozaar.spare.parts.Fragment.Items.gridView;
import static com.example.ozaar.spare.parts.Fragment.Items.partsAdapter;

public class Fragment_Dashboard extends Fragment {

    private LinkedList<String[]> shortOzaarLinkedList=new LinkedList<String[]>();

    String userId;
    private static View view;
    private TextView numberOfLossItems, personNameDashbord, personNumberDashbord, lossPriceDashbord, sellProgressText, itemProgressText,
                                                                profitProgressText,shortItemNumberDetails;
    private ProgressBar sellProgressBar, itemProgressBar, profitProgressBar;
    private ImageButton addItemBtn, sellItemBtn, item, loss, profit, sell, addNewItemsBtn;

    private Dialog epicDialog;
    ProgressDialog progressDialog;
    private ImageButton itemImageBtn,lossImageBtn,profitImageBtn,sellImageBtn;

                                                            // TextInputLayout
    private TextInputLayout nameOfOzaar,purchasePrice,sellPrice,totalNumber,ozaarName_FromAddOzarForUpdate,totalNumber_FromAddOzarForUpdate,
                                                                        sellNameOfOzaarItems,sellPriceOfOzaarItems,totalSellItemsOfOzaarItems;



    private FloatingActionButton addOzzar,updateOzaarDataIntoFire,go_btn;

    private Dialog dialogForAddItemsOfOzaar;


    FirebaseAuth mAuth;
    FirebaseDatabase  mDatabase = FirebaseDatabase.getInstance();

    DatabaseReference mReference;

    ListView shortItemsListView;

    /***************** Content Data Initilializing variables *****************************/


    String fullName ;
    String phoneNumber ;
    String email ;
    String password ;

    PartsAdapterForShortItems partsAdapterForShortItems;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (mDatabase == null)
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }


        view = inflater.inflate(R.layout.content_dash_board_activity,container,false);





        /*********************** initializing FireBase Attributes  ***************************/


        mReference = mDatabase.getReference("User");
        mReference.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        epicDialog = new Dialog(view.getContext());


        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("Loading....");



        /*********************** initializing Text View  ***************************/

        numberOfLossItems = view.findViewById(R.id.NumberOfLossItems);
        personNameDashbord = view.findViewById(R.id.Dashboard_PersonName);
        personNumberDashbord = view.findViewById(R.id.Dashboard_PersonPhoneNumber);
        lossPriceDashbord = view.findViewById(R.id.Dashboard_lossPrice);



        sellProgressText = view.findViewById(R.id.sellProgressText);
        itemProgressText = view.findViewById(R.id.itemProgressText);
        profitProgressText = view.findViewById(R.id.profitprogressText);
//        shortItemNumberDetails = findViewById(R.id.shortItemNumberDetails);


        /*********************** initializing ImageButton  ***************************/

//        itemImageBtn = findViewById(R.id.itemImageView);
//        profitImageBtn = findViewById(R.id.profitImageView);
//        lossImageBtn = findViewById(R.id.lossImageView);
//        sellImageBtn = findViewById(R.id.sellImageView);

        /*********************** initializing ProgressBar  ***************************/


        sellProgressBar = view.findViewById(R.id.sell1_progressBar);
        itemProgressBar = view.findViewById(R.id.item_progressBar);
        profitProgressBar = view.findViewById(R.id.profit_progressBar);




        /*********************** initializing Buttons  ***************************/

        addItemBtn = (ImageButton) view.findViewById(R.id.addItemsBtn);
        addNewItemsBtn = (ImageButton)  view.findViewById(R.id.addNewItemsBtn);
        sellItemBtn = (ImageButton)  view.findViewById(R.id.sellItemBtn);



        /************************                 Add update OzaarActivity          **************************************/



        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showAddItemsBoxForUpdate(view);

            }
        });


        /*************************     Add new OzaarActivity     ************************************************/



        addNewItemsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                showAddNewItemBoxActivity(view);

            }
        });


        /*********************** Initializing Short Items ListView  ***************************/

        shortItemsListView = view.findViewById(R.id.shortItems_listView);


        /*********************** Initializing Floating Action Button  ***************************/






        /*********************** Contents  ***************************/


        try {

            JSONObject jsonObject = Jason.ReadJasonData(view.getContext());
            String name = (String) jsonObject.get("name");
            String phoneNumber = jsonObject.get("phoneNumber").toString();
            personNameDashbord.setText(name.toUpperCase());
            personNumberDashbord.setText(phoneNumber);

        }catch (Exception e)
        {
            Toast.makeText(view.getContext(),e.toString(),Toast.LENGTH_LONG).show();
        }

        setContent();



        /***********************************    sell Item Button    ******************** */


        sellItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSellDialogBox();
            }
        });





       // getActivity().onBackPressed();
        return view;


    }


    private void showSellDialogBox()
    {


        final Dialog sell_dialog = new Dialog(view.getContext());

        sell_dialog.setContentView(R.layout.sell_ozaar_popup);

        go_btn = sell_dialog.findViewById(R.id.go_btn);



        sellNameOfOzaarItems = sell_dialog.findViewById(R.id.sellNameOfOzaarItems);
        sellPriceOfOzaarItems = sell_dialog.findViewById(R.id.sellPriceOfOzaarItems);
        totalSellItemsOfOzaarItems = sell_dialog.findViewById(R.id.totalSellItemsOfOzaarItems);

        sell_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        sell_dialog.show();

        sellNameOfOzaarItems.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if (!TextUtils.isEmpty(editable))
                {
                    final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

                    Query query = mRef.child("Parts").orderByChild("name_of_parts").equalTo(String.valueOf(editable).toLowerCase());


                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists())
                            {
                                //Toast.makeText(DashBoard.this, snapshot.toString(), Toast.LENGTH_SHORT).show();
                                Log.i(TAG,snapshot.toString());


                                sellNameOfOzaarItems.setError(null);
                                sellNameOfOzaarItems.setErrorEnabled(false);

                                int purchase_price = 0;
                                int totat_items = 0;

                                for(DataSnapshot fields : snapshot.getChildren())
                                {
                                    purchase_price = Integer.parseInt(fields.child("purchase_price").getValue(String.class));
                                    totat_items = fields.child("Number_of_items").getValue(Integer.class);

                                }
                                sellNameOfOzaarItems.setError("purchase price is : "+String.valueOf(purchase_price));
                                totalSellItemsOfOzaarItems.setError("total: "+String.valueOf(totat_items));
                             

                            }
                            else
                            {
                                sellNameOfOzaarItems.setError(null);
                                sellNameOfOzaarItems.setErrorEnabled(false);

                                totalSellItemsOfOzaarItems.setError(null);
                                totalSellItemsOfOzaarItems.setErrorEnabled(false);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

            }
        });



        go_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                final String name = sellNameOfOzaarItems.getEditText().getText().toString().trim().toLowerCase();
                final String price = sellPriceOfOzaarItems.getEditText().getText().toString().trim().toLowerCase();
                final String total = totalSellItemsOfOzaarItems.getEditText().getText().toString().trim().toLowerCase();



                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(price) && TextUtils.isEmpty(total))
                {
                    sellNameOfOzaarItems.setError("Empty");
                    sellNameOfOzaarItems.requestFocus();
                    sellNameOfOzaarItems.setErrorEnabled(true);

                    sellPriceOfOzaarItems.setError("Empty");
                    sellPriceOfOzaarItems.requestFocus();
                    sellPriceOfOzaarItems.setErrorEnabled(true);

                    totalSellItemsOfOzaarItems.setError("Empty");
                    totalSellItemsOfOzaarItems.requestFocus();
                    totalSellItemsOfOzaarItems.setErrorEnabled(true);


                } else if (TextUtils.isEmpty(name)) {

                    sellNameOfOzaarItems.setError("Empty");
                    sellNameOfOzaarItems.setErrorEnabled(true);


                    if (TextUtils.isEmpty(price))
                    {
                        sellPriceOfOzaarItems.setError("Empty");
                        sellPriceOfOzaarItems.requestFocus();
                        sellPriceOfOzaarItems.setErrorEnabled(true);
                    }
                    else
                    {
                        sellPriceOfOzaarItems.setError(null);
                        sellPriceOfOzaarItems.setErrorEnabled(false);
                    }

                    if (TextUtils.isEmpty(total))
                    {
                        totalSellItemsOfOzaarItems.setError("Empty");
                        totalSellItemsOfOzaarItems.requestFocus();
                        totalSellItemsOfOzaarItems.setErrorEnabled(true);
                    }
                    else
                    {
                        totalSellItemsOfOzaarItems.setError(null);
                        totalSellItemsOfOzaarItems.setErrorEnabled(false);
                    }




                } else if (TextUtils.isEmpty(price)) {

                    sellPriceOfOzaarItems.setError("Empty");
                    sellPriceOfOzaarItems.setErrorEnabled(true);
                    sellPriceOfOzaarItems.requestFocus();


                    if (TextUtils.isEmpty(name))
                    {
                        sellNameOfOzaarItems.setError("Empty");
                        sellNameOfOzaarItems.requestFocus();
                        sellNameOfOzaarItems.setErrorEnabled(true);
                    }
                    else
                    {
                        sellNameOfOzaarItems.setError(null);
                        sellNameOfOzaarItems.setErrorEnabled(false);
                    }

                    if (TextUtils.isEmpty(total))
                    {
                        totalSellItemsOfOzaarItems.setError("Empty");
                        totalSellItemsOfOzaarItems.requestFocus();
                        totalSellItemsOfOzaarItems.setErrorEnabled(true);
                    }
                    else
                    {
                        totalSellItemsOfOzaarItems.setError(null);
                        totalSellItemsOfOzaarItems.setErrorEnabled(false);
                    }
                }
                else if (TextUtils.isEmpty(total)) {

                    sellPriceOfOzaarItems.setError("Empty");
                    sellPriceOfOzaarItems.setErrorEnabled(true);
                    sellPriceOfOzaarItems.requestFocus();


                    if (TextUtils.isEmpty(name))
                    {
                        sellNameOfOzaarItems.setError("Empty");
                        sellNameOfOzaarItems.requestFocus();
                        sellNameOfOzaarItems.setErrorEnabled(true);
                    }
                    else
                    {
                        sellNameOfOzaarItems.setError(null);
                        sellNameOfOzaarItems.setErrorEnabled(false);
                    }

                    if (TextUtils.isEmpty(price))
                    {
                        sellPriceOfOzaarItems.setError("Empty");
                        sellPriceOfOzaarItems.requestFocus();
                        sellPriceOfOzaarItems.setErrorEnabled(true);
                    }
                    else
                    {
                        sellPriceOfOzaarItems.setError(null);
                        sellPriceOfOzaarItems.setErrorEnabled(false);
                    }
                }

                else
                    {

                        sellNameOfOzaarItems.setError(null);
                        sellNameOfOzaarItems.setErrorEnabled(false);

                        sellPriceOfOzaarItems.setError(null);
                        sellPriceOfOzaarItems.setErrorEnabled(false);

                        totalSellItemsOfOzaarItems.setError(null);
                        totalSellItemsOfOzaarItems.setErrorEnabled(false);



                        progressDialog.show();
                        
                        final int total_Int = Integer.parseInt(total);


                    final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

                    //                    final DatabaseReference referenceOfPartChild = mRef.child("Parts");

                    Query query = mRef.child("Parts").orderByChild("name_of_parts").equalTo(name);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists())
                            {
                                //Toast.makeText(DashBoard.this, snapshot.toString(), Toast.LENGTH_SHORT).show();
                                Log.i(TAG,snapshot.toString());

                                String nameOfOzaarFromPart = null;
                                String dateOfOzaarFromPart = null;
                                String sellPriceOfOzaarFromPart = null;


                                int purchase_price = 0;
                                int Number_of_items = 0;
                                int sold = 0;

                                String partKey = null;

                                for(DataSnapshot fields : snapshot.getChildren())
                                {
                                    nameOfOzaarFromPart = fields.child("name_of_parts").getValue().toString();
                                    dateOfOzaarFromPart = fields.child("last_purchase_date").getValue().toString();
                                    sellPriceOfOzaarFromPart = fields.child("sell").getValue().toString();

                                    purchase_price = Integer.parseInt(fields.child("purchase_price").getValue(String.class));
                                    Number_of_items = Integer.parseInt(fields.child("Number_of_items").getValue().toString());
                                    sold = Integer.parseInt(fields.child("sold").getValue().toString());

                                    partKey = fields.getKey();


                                }
                                sold+=total_Int;
                                if (total_Int <= Number_of_items)
                                {
                                    Number_of_items-=total_Int;

                                    final HashMap<String,Object> partsMap =new HashMap<>();
                                    partsMap.put("name_of_parts",nameOfOzaarFromPart);
                                    partsMap.put("last_purchase_date",dateOfOzaarFromPart);
                                    partsMap.put("sell",sellPriceOfOzaarFromPart);
                                    partsMap.put("purchase_price",String.valueOf(purchase_price));
                                    partsMap.put("Number_of_items",Number_of_items);
                                    partsMap.put("sold",String.valueOf(total_Int));




                                    mRef.child("Parts").child(partKey).setValue(partsMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                         Toast.makeText(view.getContext(),"Number_of_items updated",Toast.LENGTH_LONG).show();
                                    }

                                        }
                                    });


//                                    StatusManger.WANT_UPDATE = true;


                                    if (Integer.parseInt(price) >= purchase_price)
                                    {


                                        
                                        
                                        mRef.child("sell").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists())
                                                {
                                                    int sell =Integer.parseInt(snapshot.getValue().toString());
                                                    sell += total_Int;
                                                    mRef.child("sell").setValue(String.valueOf(sell));
                                                }
                                                else
                                                {
                                                    // if set does not Exist

                                                    mRef.child("sell").setValue(total);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        final int finalPurchase_price = purchase_price;
                                        final int finalPurchase_price1 = purchase_price;



                                        mRef.child("Profit").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists())
                                                {

                                                    Query profit_Query = mRef.child("Profit").orderByChild("name_of_parts").equalTo(name);

                                                    profit_Query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists())
                                                            {
                                                                int profit = Integer.parseInt(price) - finalPurchase_price;


                                                                String nameTemp = null;
                                                                String key="";
                                                                String lastDate = null;
                                                                int price_temp=0;
                                                                int numberOfItems=0;



                                                                for (DataSnapshot pri : snapshot.getChildren())
                                                                {
                                                                    nameTemp = pri.child("name_of_parts").getValue().toString();
                                                                    lastDate = pri.child("last_purchase_date").getValue().toString();
                                                                    key = pri.getKey();
                                                                    price_temp = Integer.parseInt(pri.child("price").getValue().toString());
                                                                    numberOfItems = Integer.parseInt(pri.child("Number_of_items").getValue().toString());
                                                                }

                                                                price_temp+=profit;
                                                                numberOfItems+=total_Int;

                                                                //String profiDatabaseRefernce = snapshot.getRef().toString();

                                                                HashMap<String,Object> profitMap = new HashMap<>();
                                                                profitMap.put("name_of_parts",nameTemp);
                                                                profitMap.put("last_purchase_date",getCurrentDate());
                                                                profitMap.put("price",String.valueOf(price_temp));
                                                                profitMap.put("Number_of_items",String.valueOf(numberOfItems));


                                                                mRef.child("Profit").child(key).setValue(profitMap).addOnCompleteListener(new OnCompleteListener<Void>() {

                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful())
                                                                        {
                                                                            setContent();
                                                                            Toast.makeText(view.getContext(),"new Value of number is Added to Profit",Toast.LENGTH_LONG).show();
                                                                            StatusManger.setWANT_UPDATE(true);
                                                                            progressDialog.dismiss();
                                                                            sell_dialog.dismiss();


                                                                            //checkFlagForItems();


                                                                        }
                                                                    }
                                                                });



                                                            }
                                                            else
                                                            {


                                                                int profit = Integer.parseInt(price) - finalPurchase_price;

                                                                HashMap map = new HashMap();
                                                                map.put("name_of_parts",name);
                                                                map.put("price",String.valueOf(profit));
                                                                map.put("Number_of_items",total);
                                                                map.put("last_purchase_date",getCurrentDate());

                                                                mRef.child("Profit").push().setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {

                                                                        Toast.makeText(view.getContext(),"map is added to profit",Toast.LENGTH_LONG).show();
                                                                        setContent();
                                                                        StatusManger.setWANT_UPDATE(true);
                                                                        progressDialog.dismiss();
                                                                        sell_dialog.dismiss();


                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {

                                                                        Toast.makeText(view.getContext(),e.toString(),Toast.LENGTH_LONG).show();
                                                                        progressDialog.dismiss();
                                                                        sell_dialog.dismiss();

                                                                    }
                                                                });

                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                            sell_dialog.dismiss();
                                                        }
                                                    });
                                                }
                                                else
                                                {
                                                    int profit = Integer.parseInt(price) - finalPurchase_price1;

                                                    HashMap map = new HashMap();
                                                    map.put("name_of_parts",name);
                                                    map.put("price",String.valueOf(profit));
                                                    map.put("Number_of_items",total);
                                                    map.put("last_purchase_date",getCurrentDate());

                                                    mRef.child("Profit").push().setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            setContent();
                                                            Toast.makeText(view.getContext(),"map is added to profit",Toast.LENGTH_LONG).show();
                                                            StatusManger.setWANT_UPDATE(true);
                                                            progressDialog.dismiss();
                                                            sell_dialog.dismiss();



                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                            Toast.makeText(view.getContext(),e.toString(),Toast.LENGTH_LONG).show();
                                                            progressDialog.dismiss();
                                                            sell_dialog.dismiss();
                                                        }
                                                    });


                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                                progressDialog.dismiss();
                                                sell_dialog.dismiss();
                                            }
                                        });
                                    }
                                    
                                    
                                    
                                    
                                    else
                                    {

                            /********************************          This is Loss in Items    ************************************/



                                        mRef.child("sell").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists())
                                                {
                                                    int sell =Integer.parseInt(snapshot.getValue().toString());
                                                    sell+=total_Int;
                                                    mRef.child("sell").setValue(String.valueOf(sell));
                                                }
                                                else
                                                {
                                                    mRef.child("sell").setValue(total);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                        final int finalPurchase_price = purchase_price;
                                        final int finalPurchase_price1 = purchase_price;

                                        mRef.child("Loss").addListenerForSingleValueEvent(new ValueEventListener()
                                        {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot)
                                            {
                                                if (snapshot.exists())
                                                {
                                                    Query profit_Query = mRef.child("Loss").orderByChild("name_of_parts").equalTo(name);
                                                    profit_Query.addListenerForSingleValueEvent(new ValueEventListener()
                                                    {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists())
                                                            {
                                                                final int loss = finalPurchase_price - Integer.parseInt(price);

                                                                String nameTemp = null;
                                                                String key="";
                                                                String lastDate = null;
                                                                int price_temp=0;
                                                                int numberOfItems=0;


                                                                for (DataSnapshot pri : snapshot.getChildren())
                                                                {
                                                                    nameTemp = pri.child("name_of_parts").getValue().toString();
                                                                    // last Purchase_date update karni ha profit waale part men
                                                                    lastDate = pri.child("last_purchase_date").getValue().toString();
                                                                    key = pri.getKey();
                                                                    price_temp = Integer.parseInt(pri.child("price").getValue().toString());
                                                                    numberOfItems = Integer.parseInt(pri.child("Number_of_items").getValue().toString());

                                                                }

                                                                price_temp+=loss;
                                                                numberOfItems+=total_Int;

                                                                //String profiDatabaseRefernce = snapshot.getRef().toString();

                                                                HashMap<String,Object> lossMap = new HashMap<>();
                                                                lossMap.put("name_of_parts",nameTemp);
                                                                lossMap.put("last_purchase_date",getCurrentDate());
                                                                lossMap.put("price",String.valueOf(price_temp));

                                                                // ye bhi profit men update karna ha
                                                                lossMap.put("Number_of_items",String.valueOf(numberOfItems));




                                                                mRef.child("Loss").child(key).setValue(lossMap).addOnCompleteListener(new OnCompleteListener<Void>() {

                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful())
                                                                        {
                                                                            StatusManger.setWANT_UPDATE(true);
                                                                            setContent();
                                                                            Toast.makeText(view.getContext(),"new Value of number is Added to Loss",Toast.LENGTH_LONG).show();
                                                                            progressDialog.dismiss();
                                                                            sell_dialog.dismiss();


                                                                        }
                                                                    }
                                                                });




                                                            }
                                                            else
                                                            {
                                                                int loss = finalPurchase_price -  Integer.parseInt(price);



                                                                HashMap<String,Object> lossMap = new HashMap<>();
                                                                lossMap.put("name_of_parts",name);
                                                                lossMap.put("last_purchase_date",getCurrentDate());
                                                                lossMap.put("price",String.valueOf(loss));
                                                                lossMap.put("Number_of_items",total);
                                                                //map.put("date",getDa)

                                                                mRef.child("Loss").push().setValue(lossMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        StatusManger.setWANT_UPDATE(true);
                                                                        progressDialog.dismiss();
                                                                        sell_dialog.dismiss();
                                                                        Toast.makeText(view.getContext(),"new Value of number is Added to Loss",Toast.LENGTH_LONG).show();

                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {

                                                                        Toast.makeText(view.getContext(),e.toString(),Toast.LENGTH_LONG).show();
                                                                        progressDialog.dismiss();
                                                                        sell_dialog.dismiss();


                                                                    }
                                                                });

                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }
                                                else
                                                {
                                                    int Lossi = finalPurchase_price -  Integer.parseInt(price);

                                                    HashMap<String,Object> lossMap = new HashMap<>();
                                                    lossMap.put("name_of_parts",name);
                                                    lossMap.put("last_purchase_date",getCurrentDate());
                                                    lossMap.put("price",String.valueOf(Lossi));
                                                    lossMap.put("Number_of_items",total);
                                                    //map.put("date",getDa)
                                                    mRef.child("Loss").push().setValue(lossMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {


                                                            StatusManger.setWANT_UPDATE(true);
                                                            setContent();
                                                            progressDialog.dismiss();
                                                            sell_dialog.dismiss();
                                                            Toast.makeText(view.getContext(),"new Value of number is Added to Loss",Toast.LENGTH_LONG).show();

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                            Toast.makeText(view.getContext(),e.toString(),Toast.LENGTH_LONG).show();
                                                            progressDialog.dismiss();
                                                            sell_dialog.dismiss();

                                                        }
                                                    });


                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }

                                }
                                else
                                {
                                    totalSellItemsOfOzaarItems.setError("0 Items");
                                    totalSellItemsOfOzaarItems.setErrorEnabled(true);
                                    totalSellItemsOfOzaarItems.requestFocus();
                                }


                            }
                            else
                            {
                                sellNameOfOzaarItems.setError("This Item is not available");
                                sellNameOfOzaarItems.requestFocus();
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }


            }
        });



    }
    
    
    
    
    
    
    private void showAddItemsBoxForUpdate(View view)
    {



        final Dialog addOzarPopup = new Dialog(view.getContext());
        addOzarPopup.setContentView(R.layout.add_ozzar_popup_for_update);
        addOzarPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addOzarPopup.show();


        /********************** initialing Text InputLayout *********************************/

        ozaarName_FromAddOzarForUpdate = addOzarPopup.findViewById(R.id.ozaarName_FromAddOzarForUpdate);
        totalNumber_FromAddOzarForUpdate = addOzarPopup.findViewById(R.id.totalNumber_FromAddOzarForUpdate);


        /********************** initialing Text Floating Action Button *********************************/
        updateOzaarDataIntoFire = addOzarPopup.findViewById(R.id.updateOzaarDataIntoFire);


       ///              Text Watcher Of TextInput Layout

        ozaarName_FromAddOzarForUpdate.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                editable.toString().toLowerCase();

                if (!TextUtils.isEmpty(editable))
                {
                    final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

                    Query query = mRef.child("Parts").orderByChild("name_of_parts").equalTo(String.valueOf(editable));

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists())
                            {

                                ozaarName_FromAddOzarForUpdate.setError(String.valueOf(snapshot.child("Number_of_items").getValue()));
                                ozaarName_FromAddOzarForUpdate.setErrorEnabled(false);


                            }
                            else
                            {

                                ozaarName_FromAddOzarForUpdate.setError("not found");
                                ozaarName_FromAddOzarForUpdate.requestFocus();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

            }
        });



        updateOzaarDataIntoFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view)
            {

                String name = ozaarName_FromAddOzarForUpdate.getEditText().getText().toString().trim().toLowerCase();
                final String total = totalNumber_FromAddOzarForUpdate.getEditText().getText().toString().trim().toLowerCase();


                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(total))
                {

                    ozaarName_FromAddOzarForUpdate.setError("Empty");
                    ozaarName_FromAddOzarForUpdate.setErrorEnabled(true);
                    totalNumber_FromAddOzarForUpdate.setError("Empty");
                    totalNumber_FromAddOzarForUpdate.setErrorEnabled(true);

                }
                else if (TextUtils.isEmpty(name))
                {
                    ozaarName_FromAddOzarForUpdate.setError("Empty");
                    ozaarName_FromAddOzarForUpdate.setErrorEnabled(true);
                    if (TextUtils.isEmpty(total))
                    {
                        totalNumber_FromAddOzarForUpdate.setError("Empty");
                        totalNumber_FromAddOzarForUpdate.setErrorEnabled(true);
                    }
                    else
                    {
                        totalNumber_FromAddOzarForUpdate.setError(null);
                        totalNumber_FromAddOzarForUpdate.setErrorEnabled(false);
                    }
                }
                else if (TextUtils.isEmpty(total))
                {
                    totalNumber_FromAddOzarForUpdate.setError("Empty");
                    totalNumber_FromAddOzarForUpdate.setErrorEnabled(true);


                    if (TextUtils.isEmpty(name))
                    {
                        ozaarName_FromAddOzarForUpdate.setError("Empty");
                        ozaarName_FromAddOzarForUpdate.setErrorEnabled(true);
                    }
                    else
                    {
                        ozaarName_FromAddOzarForUpdate.setError(null);
                        ozaarName_FromAddOzarForUpdate.setErrorEnabled(false);
                    }
                }
                else
                {
                    progressDialog.show();

                    final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

                    Query query = mRef.child("Parts").orderByChild("name_of_parts").equalTo(name);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()) {
                                //Toast.makeText(DashBoard.this, snapshot.toString(), Toast.LENGTH_SHORT).show();
                                Log.i(TAG, snapshot.toString());


                                totalNumber_FromAddOzarForUpdate.setError(null);
                                totalNumber_FromAddOzarForUpdate.setErrorEnabled(false);

                                ozaarName_FromAddOzarForUpdate.setError(null);
                                ozaarName_FromAddOzarForUpdate.setErrorEnabled(false);

                                String nameOfPart = null;
                                String sell = null;
                                String purchasePrice = null;
                                String sold = null;
                                String key = null;
                                int noOfItems  = 0;


                                for (DataSnapshot fields : snapshot.getChildren())

                                {
                                    nameOfPart = fields.child("name_of_parts").getValue().toString();
                                    sell = fields.child("sell").getValue().toString();
                                    sold = fields.child("sold").getValue().toString();
                                    purchasePrice = fields.child("purchase_price").getValue().toString();
                                    noOfItems = Integer.parseInt(fields.child("Number_of_items").getValue().toString());
                                    key = fields.getKey();

                                }
                                noOfItems+=Integer.parseInt(total);


                                HashMap<String,Object> upateMap = new HashMap<>();

                                upateMap.put("name_of_parts",nameOfPart);
                                upateMap.put("Number_of_items",noOfItems);
                                upateMap.put("sell",sell);
                                upateMap.put("sold",sold);
                                upateMap.put("last_purchase_date",getCurrentDate());
                                upateMap.put("purchase_price",String.valueOf(purchasePrice));


                                final int temp[] = {0};

                                mReference.child(userId).child("total").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        int totalItemOfUserID = 0;

                                        if (dataSnapshot.exists())
                                        {
                                            temp[0] = dataSnapshot.getValue(Integer.class);
                                            temp[0]  = Integer.parseInt(total);



                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                        progressDialog.dismiss();
                                    }
                                });



                                mRef.child("Parts").child(key).setValue(upateMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        mReference.child("total").setValue(temp[0]);

                                        StatusManger.setWANT_UPDATE(true);
                                        setContent();
                                        progressDialog.dismiss();
                                        Toast.makeText(view.getContext(),"Updated Number of Parts",Toast.LENGTH_LONG).show();
                                        addOzarPopup.dismiss();

                                    }
                                });





                            }
                            else
                            {
                                progressDialog.dismiss();
                                ozaarName_FromAddOzarForUpdate.setError("not available");
                                ozaarName_FromAddOzarForUpdate.requestFocus();

                                if(!TextUtils.isEmpty(total))
                                {
                                    totalNumber_FromAddOzarForUpdate.setError(null);
                                    totalNumber_FromAddOzarForUpdate.setErrorEnabled(false);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                        }
                    });



                }
            }
        });
    }






    private void showAddNewItemBoxActivity(View view) {

        dialogForAddItemsOfOzaar=new Dialog(view.getContext());
        dialogForAddItemsOfOzaar.setContentView(R.layout.add_items_for_ozaar_activity_);
        dialogForAddItemsOfOzaar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogForAddItemsOfOzaar.show();


        /************************* Initializing TexInputLayout ********************************************/


        nameOfOzaar = (TextInputLayout) dialogForAddItemsOfOzaar.findViewById(R.id.ozaarName);
        purchasePrice = (TextInputLayout) dialogForAddItemsOfOzaar.findViewById(R.id.purchasePrice);
        sellPrice = (TextInputLayout) dialogForAddItemsOfOzaar.findViewById(R.id.sellPrice);
        totalNumber = (TextInputLayout) dialogForAddItemsOfOzaar.findViewById(R.id.totalNumber);

        /************************* Initializing Floating Action Button ********************************************/


        addOzzar = dialogForAddItemsOfOzaar.findViewById(R.id.addOzaar);
        addOzzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOzaartoFireBase();
            }
        });
    }


    private void setSellAndItemsAndexpectedProfit_Max_Progressbar() {


        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(userId).child("total").exists()) {

                    int total = snapshot.child(userId).child("total").getValue(Integer.class);
                    int profit = Integer.parseInt(snapshot.child(userId).child("expectedProfit").getValue().toString());
                    sellProgressBar.setMax(total);
                    itemProgressBar.setMax(total);
                    profitProgressBar.setMax(profit);

                    //Toast.makeText(getApplicationContext(), String.valueOf(total), Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(view.getContext(), "Total not Exist", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    public void setContent()
    {
        setSellAndItemsAndexpectedProfit_Max_Progressbar();
        set_Profit_Max_ProgressBar();
        checkForLoss();
        checkFor_Sell_Item_ProgressBar();
        checkForProfitProgressBar();
        shortItem();

    }

    public void shortItem()
    {
        shortOzaarLinkedList.clear();

        Query q1 =  mReference.child(userId).child("Parts").orderByChild("Number_of_items")
                .endAt(5);

        q1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {


                    for (DataSnapshot items: dataSnapshot.getChildren())
                    {
                        String[]  arr = new String[4];
                        arr[0] = items.child("name_of_parts").getValue().toString();
                        arr[3] = String.valueOf(items.child("Number_of_items").getValue());
                        arr[2] = items.child("sold").getValue().toString();
                        arr[1] = items.child("last_purchase_date").getValue().toString();

                        shortOzaarLinkedList.add(arr);

                    }

                    partsAdapterForShortItems = new PartsAdapterForShortItems(view.getContext(),shortOzaarLinkedList);
                    shortItemsListView.setAdapter(partsAdapterForShortItems);

                }
                else
                {
                    LinkedList<String[]> list =new LinkedList<>();
                    String[] it=new String[4];
                    it[0] = "Head Lights";
                    it[1] = "10/4/2020";
                    it[2] = "12";
                    it[3] = "6";


                    list.add(it);

                    String[] ite=new String[4];
                    ite[0] = "Head Lights";
                    ite[1] = "10/4/2020";
                    ite[2] = "12";
                    ite[3] = "6";

                    list.add(ite);

                    String[] item=new String[4];
                    item[0] = "Head Lights";
                    item[1] = "10/4/2020";
                    item[2] = "12";
                    item[3] = "6";

                    list.add(item);
                    partsAdapterForShortItems = new PartsAdapterForShortItems(view.getContext(),list);
                    shortItemsListView.setAdapter(partsAdapterForShortItems);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean checkForLoss() {

        final boolean[] bol = {false};
        mReference.child(userId).child("Loss").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int total = 0;
                float price = 0;
                if (snapshot.exists()) {
                    for (DataSnapshot i : snapshot.getChildren()) {
                        total++;
                        // this is total price of loss Items
                        price += Float.parseFloat(i.child("price").getValue().toString());

                    }

                    numberOfLossItems.setText(String.valueOf(total));
                    lossPriceDashbord.setText(String.valueOf(price));


                } else {
                    lossPriceDashbord.setText("0.0");
                    numberOfLossItems.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return bol[0];
    }


    private void set_Profit_Max_ProgressBar() {
        mReference.child(userId).child("Profit").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int profit = 0;
                for (DataSnapshot item : snapshot.getChildren()) {
                    {
                        profit+=Integer.parseInt(item.child("price").getValue().toString());
                    }
                }

                profitProgressBar.setProgress(profit);
                profitProgressText.setText(String.valueOf(profit));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void checkForProfitProgressBar()
    {
        // mRef = FirebaseDatabase.getInstance().getReference("User");

        mReference.child(userId).child("Profit").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    int profitTotal = 0;
                    for (DataSnapshot item : snapshot.getChildren())
                    {
                        profitTotal+=Integer.parseInt(item.child("price").getValue().toString());
                    }

                    profitProgressBar.setProgress(profitTotal);
                    profitProgressText.setText(String.valueOf(profitTotal));
                }
                else
                {
                    profitProgressBar.setProgress(0);
                    profitProgressText.setText(String.valueOf(0));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkFor_Sell_Item_ProgressBar() {


        final int[] total = new int[1];


        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int max = 0;

                if (snapshot.child(userId).child("sell").exists()) {
                    int txt = Integer.parseInt(snapshot.child(userId).child("sell").getValue().toString());
                    sellProgressText.setText(String.valueOf(txt));
                    sellProgressBar.setProgress(txt);


                }


                if (snapshot.child(userId).child("Parts").exists()) {

                    int itemAvail = 0;
                    for (DataSnapshot item : snapshot.child(userId).child("Parts").getChildren())
                    {

                        itemAvail+=Integer.parseInt(item.child("Number_of_items").getValue().toString());
                    }

                    // int txt = snapshot.child(userId).child("purchase").getValue(Integer.class);
                    itemProgressText.setText(String.valueOf(itemAvail));
                    itemProgressBar.setProgress(itemAvail);

                }
                else
                {
                    itemProgressText.setText(String.valueOf(0));
                    itemProgressBar.setProgress(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }






    public void addOzaartoFireBase()
    {
        String ozaarName = nameOfOzaar.getEditText().getText().toString().trim().toLowerCase();
        String purchase = purchasePrice.getEditText().getText().toString().trim().toLowerCase();
        String sell = sellPrice.getEditText().getText().toString().trim().toLowerCase();
        String total = totalNumber.getEditText().getText().toString().trim().toLowerCase();




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
            progressDialog.show();
            AddDatatoFireBase(ozaarName,purchase,sell,total);


        }
    }



    public String getCurrentDate()
    {
        String date = null;
        return  date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
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
        mReference.child(mAuth.getUid()).child("Parts").push().updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>()
        {


            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {



                    mReference.child(userId).child("total").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists())
                            {
                                int temp=snapshot.getValue(Integer.class);
                                temp+=Integer.parseInt(mTotal);

                                mReference.child(userId).child("total").setValue(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful())
                                        {
                                            mReference.child(userId).child("expectedProfit").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists())
                                                    {
                                                        int temp=Integer.parseInt(snapshot.getValue().toString());

                                                        temp+=Integer.parseInt(mSell);

                                                        mReference.child(userId).child("expectedProfit").setValue(String.valueOf(temp)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if (task.isSuccessful())
                                                                {
                                                                    StatusManger.setWANT_UPDATE(true);
                                                                    setContent();
                                                                    progressDialog.dismiss();
                                                                    dialogForAddItemsOfOzaar.dismiss();

                                                                }

                                                                else
                                                                {
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(view.getContext(), task.toString(), Toast.LENGTH_LONG).show();
                                                                }

                                                            }
                                                        });
                                                    }
                                                    else
                                                    {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(view.getContext(), "Expected profit Not Exits", Toast.LENGTH_LONG).show();

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                        }
                                        else
                                        {
                                            progressDialog.dismiss();
                                            Toast.makeText(view.getContext(), task.toString(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });




                }
                else
                {
                    Toast.makeText(view.getContext(), task.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });




    }



}
