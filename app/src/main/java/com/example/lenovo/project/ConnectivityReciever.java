package com.example.lenovo.project;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class ConnectivityReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
        boolean notConnected=intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,false);


            if(notConnected)
                Toast.makeText(context,"Internet Disconnected!",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context,"Internet Connected!",Toast.LENGTH_SHORT).show();

        }

    }
}
