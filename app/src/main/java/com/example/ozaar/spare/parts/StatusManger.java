package com.example.ozaar.spare.parts;

public class StatusManger
{
    public static boolean WANT_UPDATE = false;


    public StatusManger(boolean WANT_UPDATE)
    {
        this.WANT_UPDATE = WANT_UPDATE;
    }

    public static boolean isWANT_UPDATE() {
        return WANT_UPDATE;
    }

    public static void setWANT_UPDATE(boolean WANT_UPDATE) {
        StatusManger.WANT_UPDATE = WANT_UPDATE;
    }
}
