package com.example.nishanth.mymessengerapplication;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    EditText e1;
    ListView listView;
    Button button;
    private static Socket clientSocket = null;
    private static PrintWriter pr;
    private static PrintStream os = null;
    private static DataInputStream is = null;
    private static BufferedReader inputline = null;
    private static boolean closed = false;
    private static final String TAG = "DoSomethingTask";
    private Context context;
    private String user;

    String message = "";
    private static String ip = "10.0.2.2";
    //public static DataHolder holder  = DataHolder.getInstance();
    ChatArrayAdapter chatArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1 = (EditText) findViewById(R.id.msg1);
        listView = (ListView) findViewById(R.id.msgview);


        chatArrayAdapter = new ChatArrayAdapter( getApplicationContext(), R.layout.right );
        listView = (ListView) findViewById( R.id.msgview );
        listView.setAdapter( chatArrayAdapter );
        listView.setTranscriptMode( AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL );
        listView.setAdapter( chatArrayAdapter );
        chatArrayAdapter.registerDataSetObserver( new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection( chatArrayAdapter.getCount() - 1 );
            }
        } );

        String toUser = getIntent().getStringExtra("EXTRA_SESSION_ID");
        String userName = getIntent().getStringExtra("username");
        user = toUser;
        context = getApplicationContext();
        Toast.makeText(context, toUser, Toast.LENGTH_SHORT).show();
        myTask mytask = new myTask();
        mytask.execute();
    }

    public void sendText(View view) {
        message = e1.getText().toString();
        //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        e1.setText( "" );
        chatArrayAdapter.add(new ChatMessage(true,"@"+user+" "+message));
        os = DataHolder.getOs();
        System.out.println(message);
        os.println("@"+user+" "+message);
    }

    private boolean receiveMessage(String msg) {
        chatArrayAdapter.add(new ChatMessage(false, msg));
        return true;
    }


    private boolean sendMessage(String msg) {
        chatArrayAdapter.add(new ChatMessage(true, msg));
        return true;
    }


    public void getConnection(){
        clientSocket = DataHolder.getClientSocket();
        inputline = new BufferedReader(new InputStreamReader(System.in));
        os = DataHolder.getOs();
        is = DataHolder.getIs();
    }



    class myTask extends AsyncTask<Void, String, Void> {

      /*  @Override
        protected void onPreExecute() {
            Log.v(TAG, "starting the Random Number Task");
            if(clientSocket == null){
                holder.createConnection(ip);
            }
            getConnection();
            super.onPreExecute();
        }*/

        @Override
        protected void onProgressUpdate(String... values) {
            Log.v(TAG, "reporting back from the Random Number Task");
            receiveMessage(values[0].toString());
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... params) {
       /*     if(clientSocket == null){
                DataHolder.createConnection(ip);
            }*/
            getConnection();

            String responseLine;
            try {
                while ((responseLine = is.readLine()) != null) {
                    System.out.println(responseLine);
                    publishProgress(responseLine);
                    if (responseLine.indexOf("* Bye") != -1)
                        break;
                }
                closed = true;
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
            return null;
        }
    }

    public class ChatMessage {
        public boolean left;
        public String message;

        public ChatMessage(boolean left, String message) {
            super();
            this.left = left;
            this.message = message;
        }
    }
}