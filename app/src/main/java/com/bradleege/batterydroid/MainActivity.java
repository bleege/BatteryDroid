package com.bradleege.batterydroid;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.bradleege.batterydroid.data.CardData;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<CardData> cardData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CardData cd = new CardData();
        cd.setFeature("Feature 1");
        cd.setStatus("Status 1");
        cardData.add(cd);
        cd = new CardData();
        cd.setFeature("Feature 2");
        cd.setStatus("Status 2");
        cardData.add(cd);
        cd = new CardData();
        cd.setFeature("Feature 3");
        cd.setStatus("Status 3");
        cardData.add(cd);
        cd = new CardData();
        cd.setFeature("Feature 4");
        cd.setStatus("Status 4");
        cardData.add(cd);
        cd = new CardData();
        cd.setFeature("Feature 5");
        cd.setStatus("Status 5");
        cardData.add(cd);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recylerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new CardRecylerViewAdapter(cardData));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
