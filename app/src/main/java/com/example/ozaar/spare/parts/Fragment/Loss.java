package com.example.ozaar.spare.parts.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ozaar.spare.parts.PartsAdapterForLoss;
import com.example.ozaar.spare.parts.R;
import com.example.ozaar.spare.parts.StatusManger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class Loss extends Fragment {


    private SwipeRefreshLayout mSwipeRefreshLayout;
    private static PartsAdapterForLoss partsAdapterForLoss;

    // This
    private static FirebaseAuth mAuth;
    private static GridView gridView;
    private String userID;
    private String lossupadate;
    private static Context myContext;
    private LinkedList<String[]> parts_list=new LinkedList<String[]>();

    private    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view =  inflater.inflate(R.layout.loss_activity, container, false);

        mSwipeRefreshLayout = view.findViewById(R.id.swipeUpLayout);
        mAuth = FirebaseAuth.getInstance();

        myContext = view.getContext();

        gridView = view.findViewById(R.id.gridview);




        if (StatusManger.WANT_UPDATE)
        {
            Update(parts_list);

        }
        else
        {
            addData(parts_list);
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Update(parts_list);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    return view;
    }


    public static void Update(final LinkedList<String[]> parts_list)
    {
        parts_list.clear();

        DatabaseReference user = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getCurrentUser().getUid());
        user.child("Loss").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot items : snapshot.getChildren())
                {
                    String[]  arr = new String[4];

                    arr[0] = items.child("name_of_parts").getValue(String.class);
                    arr[1] = items.child("last_purchase_date").getValue(String.class);
                    arr[2] = items.child("price").getValue(String.class);
                    arr[3] = items.child("Number_of_items").getValue(String.class);

                    parts_list.add(arr);


                }
                partsAdapterForLoss =new PartsAdapterForLoss(myContext,parts_list);
                gridView.setAdapter(partsAdapterForLoss);
                StatusManger.WANT_UPDATE = false;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void addData(final LinkedList<String[]> parts_list)
    {
        if (parts_list.size() == 0)
        {

            DatabaseReference user = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getCurrentUser().getUid());
            user.child("Loss").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {
                        for (DataSnapshot items : snapshot.getChildren())
                        {
                            String[]  arr = new String[4];

                            arr[0] = items.child("name_of_parts").getValue(String.class);
                            arr[1] = items.child("last_purchase_date").getValue(String.class);
                            arr[2] = items.child("price").getValue(String.class);
                            arr[3] = items.child("Number_of_items").getValue(String.class);

                            parts_list.add(arr);


                        }
                        StatusManger statusManger = new StatusManger(false);

                        partsAdapterForLoss =new PartsAdapterForLoss(view.getContext(),parts_list);
                        gridView.setAdapter(partsAdapterForLoss);
                    }
                    else
                    {
                        Toast.makeText(view.getContext(),"YOu did not sell any thing untill",Toast.LENGTH_LONG).show();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        else
        {
            partsAdapterForLoss =new PartsAdapterForLoss(view.getContext(),parts_list);
            gridView.setAdapter(partsAdapterForLoss);

        }

    }
}
