package box.gift.ytime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import box.gift.ytime.MainActivity;
import box.gift.ytime.NoteUpdateService;
import box.gift.ytime.R;

public class RunOnStartup extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /*if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            //Only start if that is the pref
            SharedPreferences sharedPref = context.getSharedPreferences("SettingsYTime", Context.MODE_PRIVATE);
            if (sharedPref.getBoolean("autostart", false))
            {
                Log.d("n", "AutoStart");
                Intent i = new Intent(context, NoteUpdateService.class);
                context.startService(i);
            }
            else
            {
                Log.d("n", "NoAutoStart");
            }
        }*/

        SharedPreferences sharedPref = context.getSharedPreferences("SettingsYTime", Context.MODE_PRIVATE);
        if (sharedPref.getBoolean("autostart", false))
        {
            Intent i = new Intent(context, NoteUpdateService.class);
            context.startService(i);
        }

        /*Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);*/
    }
}
