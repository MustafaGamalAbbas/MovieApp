package com.example.movieapp.Screens.Home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.movieapp.EventBus.AddButtonClicked;
import com.example.movieapp.R;

import org.greenrobot.eventbus.EventBus;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences.Editor editor = getSharedPreferences("Tablet_UI", MODE_PRIVATE).edit();
        if (findViewById(R.id.fr_detailFragment) != null) {
            //that's two fragment :DD
            editor.putBoolean("tablet", true);
        } else {
            editor.putBoolean("tablet", false);
        }
        editor.apply();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            EventBus.getDefault().post(new AddButtonClicked());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
