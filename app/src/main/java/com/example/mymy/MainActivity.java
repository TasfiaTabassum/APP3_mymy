package com.example.mymy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity<noHistory, name, label, excludeFromRecents, activity> extends AppCompatActivity {

    private CardView AppUsagePermission;
    private CardView SetUpPrivacy;
    private CardView TimeSlotSetting;
    Button btnapplocker , btnapppermission , buttonprofile , btntimeslotsetting;
    Button signOut;
    ImageView imageView;
    SwitchCompat switchCompat;
    SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView appSetting = findViewById(R.id.setting_cardview);
        AppUsagePermission = findViewById(R.id.App_usage_permission);
        SetUpPrivacy = findViewById(R.id.set_up_privacy);
        TimeSlotSetting = findViewById(R.id.time_slot_setting);
        signOut = findViewById(R.id.sign_out_btn);
        imageView = findViewById(R.id.imageView);
        switchCompat = findViewById(R.id.switchCompat);
        backupApps();



        appSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Day_night.class);
                startActivity(intent);
            }
        });

        AppUsagePermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(intent);
            }
        });

        SetUpPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, app_privacy_setting.class);
                startActivity(intent);
            }
        });

        TimeSlotSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, timeSlotSettingActivity.class);
                startActivity(intent);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, logIn.class));

            }
        });
    }

    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            }
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void backupApps(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> app = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        List<String> packageList = new ArrayList<>();
        for (ApplicationInfo a : app)
            packageList.add(a.packageName);
        db.collection("backup").whereNotIn("package",packageList).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot d: task.getResult()){
                            db.collection("backup").document(d.getId()).delete();
                        }
                    }
                });

        for (ApplicationInfo a: app)
        {
            db.collection("backup").whereEqualTo("package",a.packageName).get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful())
                        {
                           if(task.getResult().size() == 0)
                           {
                               Map<String,String> mp = new HashMap<>();
                               mp.put("package",a.packageName);
                               db.collection("backup").add(mp);
                           }
                        }
                    });

        }
    }
}