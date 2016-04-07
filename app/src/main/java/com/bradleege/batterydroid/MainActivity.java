package com.bradleege.batterydroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.bradleege.batterydroid.data.CardData;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView = null;
    private CardRecyclerViewAdapter cardRecyclerViewAdapter = null;

    private BroadcastReceiver batteryInfoReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cardRecyclerViewAdapter = new CardRecyclerViewAdapter(new ArrayList<CardData>());
        recyclerView.setAdapter(cardRecyclerViewAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        batteryInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "onReceive() called with intent " + intent);
                updateBatteryStatus(intent);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);

        registerReceiver(batteryInfoReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (batteryInfoReceiver != null) {
            unregisterReceiver(batteryInfoReceiver);
        }
    }

    private void updateBatteryStatus(Intent intent) {

        ArrayList<CardData> cardData = new ArrayList<>();

        boolean present = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);

        if (present) {
            int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
            int healthLbl = -1;
            CardData cd = new CardData();
            cd.setFeature("Battery Health");

            switch (health) {
                case BatteryManager.BATTERY_HEALTH_COLD:
                    cd.setStatus("Cold");
                    break;
                case BatteryManager.BATTERY_HEALTH_DEAD:
                    cd.setStatus("Dead");
                    break;
                case BatteryManager.BATTERY_HEALTH_GOOD:
                    cd.setStatus("Good");
                    break;
               case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                   cd.setStatus("Over Voltage");
                    break;
                case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                    cd.setStatus("Over Heat");
                    break;
                case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                    cd.setStatus("Unspecified Failure");
                    break;
                case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                default:
                    cd.setStatus("Unkown");
                    break;
            }
            cardData.add(cd);

            // Calculate Battery Pourcentage ...
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            if (level != -1 && scale != -1) {
                int batteryPct = (int) ((level / (float) scale) * 100f);
                cd = new CardData();
                cd.setFeature("Battery Percentage");
                cd.setStatus(batteryPct + " %");
                cardData.add(cd);
            }

            int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);

            cd = new CardData();
            cd.setFeature("Plugged Status");

            switch (plugged) {
                case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                    cd.setStatus("Wireless");
                    break;
                case BatteryManager.BATTERY_PLUGGED_USB:
                    cd.setStatus("USB");
                    break;
                case BatteryManager.BATTERY_PLUGGED_AC:
                    cd.setStatus("AC");
                    break;
                default:
                    cd.setStatus("Default (None)");
                    break;
            }
            cardData.add(cd);

            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            cd = new CardData();
            cd.setFeature("Status");

            switch (status) {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    cd.setStatus("Charging");
                    break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    cd.setStatus("Discharging");
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:
                    cd.setStatus("Full");
                    break;
                case BatteryManager.BATTERY_STATUS_UNKNOWN:
                    cd.setStatus("Unknown");
                    break;
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                default:
                    cd.setStatus("Not Charging");
                    break;
            }
            cardData.add(cd);

            if (intent.getExtras() != null) {
                String technology = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);

                if (!TextUtils.isEmpty(technology)) {
                    cd = new CardData();
                    cd.setFeature("Technology");
                    cd.setStatus(technology);
                    cardData.add(cd);
                }
            }

            int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);

            if (temperature > 0) {
                float temp = ((float) temperature) / 10f;
                cd = new CardData();
                cd.setFeature("Temperature");
                cd.setStatus(temp + "°C");
                cardData.add(cd);
            }

            int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);

            if (voltage > 0) {
                cd = new CardData();
                cd.setFeature("Voltage");
                cd.setStatus(voltage + "mV");
                cardData.add(cd);
            }

            long capacity = getBatteryCapacity(this);

            if (capacity > 0) {
                cd = new CardData();
                cd.setFeature("Capacity");
                cd.setStatus(capacity + "mAh");
                cardData.add(cd);
            }

            cardRecyclerViewAdapter.updateData(cardData);
        } else {
            Toast.makeText(this, "No Battery present", Toast.LENGTH_SHORT).show();
        }

    }

    public long getBatteryCapacity(Context ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager mBatteryManager = (BatteryManager) ctx.getSystemService(Context.BATTERY_SERVICE);
            Long chargeCounter = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
            Long capacity = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

            if (chargeCounter != null && capacity != null) {
                long value = (long) (((float) chargeCounter / (float) capacity) * 100f);
                return value;
            }
        }

        return 0;
    }
}
