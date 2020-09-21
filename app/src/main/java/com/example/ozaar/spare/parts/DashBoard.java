package com.example.ozaar.spare.parts;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

public class DashBoard extends AppCompatActivity {


    private TextView numberOfLossItems, personNameDashbord, personNumberDashbord, lossPriceDashbord, sellProgressText, itemProgressText, profitProgressText,shortItemNumberDetails;

    private ProgressBar sellProgressBar, itemProgressBar, profitProgressBar;
    private ImageButton addItemBtn, sellItemBtn, item, loss, profit, sell, addNewItemsBtn, go_btn;

   // private HorizontalListView shortItemsListView;

    private Dialog epicDialog;
    ProgressDialog progressDialog;


    private ImageButton itemImageBtn,lossImageBtn,profitImageBtn,sellImageBtn;

    private TextInputLayout nameOfSellingItem, priceOfSellingItem;



    /***************** Content Data Initilializing variables *****************************/


    String fullName ;
    String phoneNumber ;
    String email ;
    String password ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_dash_board_activity);




        /*********************** initializing Text View  ***************************/

        numberOfLossItems = findViewById(R.id.NumberOfLossItems);
        personNameDashbord = findViewById(R.id.Dashboard_PersonName);
        personNumberDashbord = findViewById(R.id.Dashboard_PersonPhoneNumber);
        lossPriceDashbord = findViewById(R.id.Dashboard_lossPrice);



        sellProgressText = findViewById(R.id.sellProgressText);
        itemProgressText = findViewById(R.id.itemProgressText);
        profitProgressText = findViewById(R.id.profitprogressText);
//        shortItemNumberDetails = findViewById(R.id.shortItemNumberDetails);


        /*********************** initializing ImageButton  ***************************/

//        itemImageBtn = findViewById(R.id.itemImageView);
//        profitImageBtn = findViewById(R.id.profitImageView);
//        lossImageBtn = findViewById(R.id.lossImageView);
//        sellImageBtn = findViewById(R.id.sellImageView);

        /*********************** initializing ProgressBar  ***************************/


        sellProgressBar = findViewById(R.id.sell1_progressBar);
        itemProgressBar = findViewById(R.id.item_progressBar);
        profitProgressBar = findViewById(R.id.profit_progressBar);




        /*********************** initializing ImageButtons  ***************************/

        addItemBtn = (ImageButton) findViewById(R.id.addItemsBtn);
        addNewItemsBtn = (ImageButton)  findViewById(R.id.addNewItemsBtn);
        sellItemBtn = (ImageButton)  findViewById(R.id.sellItemBtn);



        /*********************** initializing Dialog Box  ***************************/

        epicDialog = new Dialog(this);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");


        Intent in=getIntent();
        fullName = in.getStringExtra("fullName");
        phoneNumber = in.getStringExtra("phoneNumber");
        email = in.getStringExtra("email");
        password = in.getStringExtra("password");



        setContent();



    }

    public void setContent()
    {
       try {

          JSONObject jsonObject = Jason.ReadJasonData(getApplicationContext());
          String name = (String) jsonObject.get("name");
          String phoneNumber = jsonObject.get("phoneNumber").toString();
           personNameDashbord.setText(name);
           personNumberDashbord.setText(phoneNumber);

       }catch (Exception e)
       {
           Toast.makeText(getMyContext(),e.toString(),Toast.LENGTH_LONG).show();
       }

    }
    public static Context context;
    public static Context getMyContext()
    {
        return context;
    }
}