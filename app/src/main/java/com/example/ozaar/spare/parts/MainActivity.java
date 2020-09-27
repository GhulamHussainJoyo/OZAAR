package com.example.ozaar.spare.parts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.ozaar.spare.parts.Fragment.Loss;
import com.example.ozaar.spare.parts.Fragment.Profit;
import com.example.ozaar.spare.parts.Fragment.Items;
import com.example.ozaar.spare.parts.Fragment.FragmentE;
import com.example.ozaar.spare.parts.Fragment.Fragment_Dashboard;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import eu.long1.spacetablayout.SpaceTabLayout;

public class MainActivity extends AppCompatActivity {
    SpaceTabLayout tabLayout;


    FirebaseAuth mAuth;
   Fragment_Dashboard dashi;
     List<Fragment> fragmentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dashi = new Fragment_Dashboard();
        mAuth = FirebaseAuth.getInstance();

        //add the fragments you want to display in a List
        fragmentList = new ArrayList<>();
        fragmentList.add(new Profit());
        fragmentList.add(new Loss());
        fragmentList.add(new Fragment_Dashboard());
        fragmentList.add(new Items());
        fragmentList.add(new FragmentE());


        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);


        tabLayout = (SpaceTabLayout) findViewById(R.id.spaceTabLayout);



        tabLayout.initialize(viewPager, getSupportFragmentManager(), fragmentList, savedInstanceState);
        tabLayout.setTabFourIcon(R.drawable.items_for_add_item_for_ozaar);
        tabLayout.setTabFiveIcon(R.drawable.logout_icon);
        int a = tabLayout.FOCUS_BACKWARD;



        tabLayout.setTabOneOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Welcome to SpaceTabLayout", Snackbar.LENGTH_SHORT);

                snackbar.show();
            }
        });




        tabLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                    //Toast.makeText(getApplication(), "" + tabLayout.getCurrentPosition(), Toast.LENGTH_SHORT).show();
                    if (tabLayout.getCurrentPosition() == 4)
                    {
                        SignOutUser();
                    }
                    if (tabLayout.getCurrentPosition() == 2)
                    {


                    }
            }
        });




    }

    private void SignOutUser() {
        if (Jason.DeleteFile(getApplicationContext()) == true)
        {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, RegisterationMethod.class));
            finish();
        }
        else
        {
            Toast.makeText(MainActivity.this, "Sorry we can't delete file due to external reasons", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
       moveTaskToBack(true);
    }
}
