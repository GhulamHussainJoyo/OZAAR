package com.example.ozaar.spare.parts;

import android.os.AsyncTask;

public interface onGetDataListener  {

    public void onBegin();
    public void onSuccess();
    public void onFailure(String task);

}
