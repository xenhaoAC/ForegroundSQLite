package com.example.xenhao.foregroundsqlite;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    private final String TAG = "MainActivity";
    SQLiteDatabase db;

    Button startBtn, stopBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //  temporary allowing network to run in main thread
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  define buttons & their behavior
        startBtn = (Button)findViewById(R.id.startButton);
        stopBtn = (Button)findViewById(R.id.stopButton);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  starts Service
                Intent startIntent = new Intent(MainActivity.this, ForegroundService.class);
                startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                startService(startIntent);
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  stops Service
                Intent stopIntent = new Intent(MainActivity.this, ForegroundService.class);
                stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                startService(stopIntent);
            }
        });



        //  creates SQLite database
//        db = openOrCreateDatabase("RecordsDB", Context.MODE_PRIVATE, null);
//        db.execSQL("CREATE TABLE IF NOT EXISTS records("+"click_id INTEGER PRIMARY KEY AUTOINCREMENT,"+"clickno INTEGER)");
//        db.execSQL("INSERT INTO records(clickno)VALUES(0)");
    }

//    @Override
//    protected void onResume(){
//        super.onResume();
//        Cursor c = db.rawQuery("SELECT * FROM records", null);
//        while(c.moveToNext()){
//            Log.i(TAG, "How many times button pushed: " + c.getInt(c.getColumnIndexOrThrow("clickno")));
//        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
