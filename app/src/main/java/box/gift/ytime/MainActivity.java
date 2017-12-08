package box.gift.ytime;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.Pair;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    Intent startNotes;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        //Switch events
        Switch notify = (Switch) findViewById(R.id.notify);
        Switch autostart = (Switch) findViewById(R.id.autostart);

        SharedPreferences sharedPref = getSharedPreferences("SettingsYTime", Context.MODE_PRIVATE);
        //Setup listeners and choose default state based on settings
        if (notify != null) {
            notify.setOnCheckedChangeListener(this);
            notify.setChecked(sharedPref.getBoolean("notify", true));
        }
        if (autostart != null) {
            autostart.setOnCheckedChangeListener(this);
            autostart.setChecked(sharedPref.getBoolean("autostart", false));
        }

        //Start notification service
        startNotes = new Intent(context, NoteUpdateService.class);
        context.startService(startNotes);
    }

    public void close(View view) {
        //When close button is pressed
        context.stopService(startNotes);
        //Log.d("And: ", "Close pressed");
        if (android.os.Build.VERSION.SDK_INT >= 21) //If we can, totally close the app and remove from recently opened apps list
            finishAndRemoveTask();
        finish(); //On older devices, just close
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //context.stopService(startNotes);
    }


    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences sharedPref = getSharedPreferences("SettingsYTime", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        int id = buttonView.getId();
        if (id == R.id.notify)
        {
            editor.putBoolean("notify", isChecked);
        }
        if (id == R.id.autostart)
        {
            editor.putBoolean("autostart", isChecked);
        }

        editor.commit();
    }

}
