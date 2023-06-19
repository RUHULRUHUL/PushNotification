package com.ruhul.pushnotification;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RemoteViews;

public class DisplayNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notification);


        RemoteViews expandedView = new RemoteViews(getPackageName(),
                R.layout.notification_expanded);


    }
}