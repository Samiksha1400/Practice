package com.example.practice;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;

public class Utility
{
    static String timestampToString(Timestamp timestamp)
    {
        return new SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate());
    }
}
