package com.example.ozaar.spare.parts;

import android.app.Application;
import android.content.Context;
import android.os.FileUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Jason {
    private static final String TAG = "Error";



    public static boolean jasonWriter(String fullNamae, String phoneNumber, String email, String password,Context mContext) {
        boolean check = false;


        try
        {

            JSONObject jsobjct = new JSONObject();
            jsobjct.put("name", fullNamae);
            jsobjct.put("phoneNumber", phoneNumber);
            jsobjct.put("email", email);
            jsobjct.put("password", password);



            String userData = jsobjct.toString();

            File file = new File(mContext.getFilesDir(), "userData.json");


            if (file.exists())
            {
                Toast.makeText(mContext, "File Already Exist", Toast.LENGTH_LONG).show();
                check = false;
            }
            else

             {
                FileWriter fileWriter = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(userData);
                bufferedWriter.close();

                check = true;

            }


        } catch (Exception e) {
            Log.d(TAG, "**********************************************************       "+e.toString());
            check = false;
        }

        finally {
            return check;
        }
    }

    public static JSONObject ReadJasonData(Context mContext) {
        JSONObject jsooo = null;
        try {
            File file = new File(mContext.getFilesDir(), "userData.json");
            if (file.exists()) {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                StringBuilder stringBuilder = new StringBuilder();
                String line = bufferedReader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append("\n");
                    line = bufferedReader.readLine();
                }
                bufferedReader.close();

                String response = stringBuilder.toString();

                JSONObject jsonObject = new JSONObject(response);
               jsooo = jsonObject;

            } else {
                Toast.makeText(mContext, "File Not Exist", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
        }
        

        return jsooo;
    }

    public static boolean DeleteFile(Context mContext)
    {

        File file = new File(mContext.getFilesDir(), "userData.json");
        return delete(file);

    }
    private static boolean delete(File file) {

        boolean check;
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                delete(child);
            }
        }
        return check = file.delete();

    }



}
