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

public class PartsAdapterForLoss extends BaseAdapter {

    private final Context mContext;
    // private final int[] books;

    private final LinkedList<String[]> parts;

    // 1
    public PartsAdapterForLoss(Context context, LinkedList<String[]> parts) {
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


        String head = "";
        String tail = "";


        String[] parts_string_name;
        parts_string_name=parts.get(position);

        boolean check=true;
        for(int i=0;i<parts_string_name[0].length();i++)
        {
            if (parts_string_name[0].charAt(i)!=' ')
            {
                if (check==true)
                {
                    head+=parts_string_name[0].charAt(i);
                }
                else
                {
                    tail+=parts_string_name[0].charAt(i);
                }
            }
            else if (parts_string_name[0].charAt(i)==' ')
            {
                check=false;
                tail+=parts_string_name[0].charAt(i);
            }

        }


        if(convertView==null)
        {
            final LayoutInflater layoutInflater=LayoutInflater.from(mContext);
            convertView=layoutInflater.inflate(R.layout.loss_card_view,null);
        }

        final TextView name_Of_Part1=(TextView) convertView.findViewById(R.id.name_Of_parts_1);

        final TextView name_Of_Part2=(TextView) convertView.findViewById(R.id.name_Of_parts_2);


        final TextView sell_number=(TextView) convertView.findViewById(R.id.sell_number);

        final TextView date = (TextView) convertView.findViewById(R.id.date_month_year);

        final TextView total_number=(TextView) convertView.findViewById(R.id.total_number);

        final TextView loss_profit=(TextView) convertView.findViewById(R.id.loss_profit);

        //imageView.setImageResource(R.drawable.hand_tools);

        name_Of_Part1.setText(toUpperCase(head));
        name_Of_Part2.setText(toUpperCase(tail));

        date.setText(parts_string_name[1].toString());
        sell_number.setText(parts_string_name[2].toString());
        total_number.setText(parts_string_name[3].toString());

        return convertView;

    }

}