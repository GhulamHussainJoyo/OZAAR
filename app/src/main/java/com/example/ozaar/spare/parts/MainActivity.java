package com.example.ozaar.spare.parts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.ozaar.spare.parts.Fragment.FragmentB;
import com.example.ozaar.spare.parts.Fragment.FragmentC;
import com.example.ozaar.spare.parts.Fragment.FragmentD;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        //add the fragments you want to display in a List
        final List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new FragmentC());
        fragmentList.add(new FragmentB());
        fragmentList.add(new Fragment_Dashboard());
        fragmentList.add(new FragmentD());
        fragmentList.add(new FragmentE());




        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (SpaceTabLayout) findViewById(R.id.spaceTabLayout);

        tabLayout.initialize(viewPager, getSupportFragmentManager(), fragmentList, savedInstanceState);

        tabLayout.setTabFourIcon(R.drawable.sold);
        tabLayout.setTabFiveIcon(R.drawable.logout_icon);
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
            public void onClick(View v) {
                //Toast.makeText(getApplication(), "" + tabLayout.getCurrentPosition(), Toast.LENGTH_SHORT).show();
                if (tabLayout.getCurrentPosition() == 4)
                {
                    if (Jason.DeleteFile(getApplicationContext()) == true)
                    {
                        mAuth.signOut();
                        startActivity(new Intent(MainActivity.this,RegisterationMethod.class));
                        finish();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Sorry we can't delete file due to external reasons", Toast.LENGTH_SHORT).show();
                    }
                }
                if (tabLayout.getCurrentPosition() == 2)
                {


                }
                }
        });


    }





    }
