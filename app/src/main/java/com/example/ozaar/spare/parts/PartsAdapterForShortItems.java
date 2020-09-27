package com.example.ozaar.spare.parts;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.LinkedList;

import static android.icu.lang.UCharacter.toUpperCase;

public class PartsAdapterForShortItems extends BaseAdapter {

    private final Context mContext;
    // private final int[] books;

    private final LinkedList<String[]> parts;

    // 1
    public PartsAdapterForShortItems(Context context, LinkedList<String[]> parts) {
        this.mContext = context;
        this.parts=parts;

    }

    // 2
    @Override
    public int getCount() {
        return parts.size();
    }

    // 3
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 4
    @Override
    public Object getItem(int position) {
        return null;
    }

    // 5
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {



        String[] parts_string_name;
        parts_string_name=parts.get(position);

        if(convertView==null)
        {
            final LayoutInflater layoutInflater=LayoutInflater.from(mContext);
            convertView=layoutInflater.inflate(R.layout.short_items,null);


        }


        final TextView name_Of_Part=(TextView) convertView.findViewById(R.id.name_of_part);

        final TextView dateMonthYear=(TextView) convertView.findViewById(R.id.date_month_year);


        final TextView mSold=(TextView) convertView.findViewById(R.id.sold);


        final TextView mStock=(TextView) convertView.findViewById(R.id.stock);


        //imageView.setImageResource(R.drawable.hand_tools);

        name_Of_Part.setText(parts_string_name[0]);
        dateMonthYear.setText(parts_string_name[1]);

        mSold.setText(parts_string_name[2].toString());
        mStock.setText(parts_string_name[3].toString());

        return convertView;

    }

}