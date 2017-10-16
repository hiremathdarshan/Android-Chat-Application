package com.example.nishanth.mymessengerapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class UserView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_userview );
        String[] users = {"nish","shri"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,users);
        ListView listView = (ListView)findViewById( R.id.listView );
        listView.setAdapter(adapter);
        final String username = getIntent().getStringExtra("username");
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = (String)parent.getItemAtPosition( position );

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("EXTRA_SESSION_ID", data);
                intent.putExtra("username", username);
                startActivity(intent);

            }
        } );
    }

}
