package com.example.nishanth.mymessengerapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

public class UserAuthentication extends AsyncTask<String, Void, String> {

    private static Socket clientSocket = null;
    private static PrintWriter pr;
    private static PrintStream os = null;
    private static DataInputStream is = null;
    private static String ip = "10.0.2.2";
    AlertDialog alertDialog;

    public static DataHolder holder  = DataHolder.getInstance();
    Context context;
    UserAuthentication (Context ctx){context= ctx;}

    public void getConnection(){
        clientSocket = holder.getClientSocket();
        os = holder.getOs();
        is = holder.getIs();
    }
    @Override
    protected String doInBackground(String... params) {
        clientSocket = DataHolder.getClientSocket();
        if(clientSocket == null){
            holder.createConnection(ip);
        }
        getConnection();

        String type = params[0];
        String username = params[1];
        String password = params[2];
        String result = "";
        if(type.equals("login")) {
            try {
                os.println("Login "+username+" "+password);
                result = is.readLine();
                if(result.equals("false")){
                    System.out.println("Invalid user");
                    return result;
                }
            }catch (Exception e){

            }
        }
        return username;
    }

    @Override
    protected void onPostExecute(String result) {
       // alertDialog.setMessage(result);
        //alertDialog.show();

        if(result.equals("false")) {
            Toast.makeText(context,"Please enter correct details...",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Welcome "+result, Toast.LENGTH_SHORT).show();
            Intent I = new Intent(context, UserView.class);
            I.putExtra("username", result);
            context.startActivity(I);
        }
    }
}