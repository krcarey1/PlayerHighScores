package edu.asu.bsse.krcarey1.playerhighscores;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright 2017 Kyle Carey
 *
 * Rights granted to Timothy Lindquist and any instructor for SER423 and the University of
 * Arizona State University to build and evaluate the software package for the purpose of
 * determining grade and program assessment.
 *
 * Purpose - This android application creates a High Score database, and displays each row of
 * information as a list item on the MainActivity. Users can press the + fab button to add more
 * information to the table. If no database or table are present on the device one is created,
 * with 2 default entries. Note, if all rows from the table are at anytime completely deleted
 * resulting in an empty table, the default values will be written again.
 *
 * @author  Kyle Carey    mailto:krcarey1@asu.edu
 *
 * @version April 22, 2017
 */

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // opens and checks to see if database and table are created, creates both if not
        db = openOrCreateDatabase("ScoresDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS scores" +
                "(id integer primary key, " +
                "name TEXT NOT NULL," +
                "avatar TEXT NOT NULL," +
                "game TEXT NOT NULL, " +
                "score INTEGER NOT NULL);"
        );

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                startActivity(i);
            }
        });

        List<Map<String,String>> scoreMapList = new ArrayList<>();
        Cursor res =  db.rawQuery( "select * from scores", null ); // gets all data from the table
        res.moveToFirst();
        // create initial data if table is empty
        if (res.getCount() == 0) {
            db.execSQL("INSERT INTO scores VALUES(\"0\",\"Kyle\",\"MonsterX\",\"COD\",2500);");
            db.execSQL("INSERT INTO scores VALUES(\"1\",\"Casey\",\"BlasterMaster\",\"Blood Borne\",500);");
            res =  db.rawQuery( "select * from scores", null ); // gets all data from the table
            res.moveToFirst();
        }

        // fills the list with each rows information (except id) from the scores table
        while(res.isAfterLast() == false){
            Map<String, String> scoreData = new HashMap<>(2);
            scoreData.put("name", res.getString(1));
            scoreData.put("avatar", res.getString(2));
            scoreData.put("game", res.getString(3));
            scoreData.put("score", String.valueOf(res.getInt(4)));
            scoreMapList.add(scoreData);
            res.moveToNext();
        }
        SimpleAdapter adapter = new SimpleAdapter(this, scoreMapList,
                R.layout.list_layout, new String[] {"name","avatar","game","score"},
                new int[] {R.id.name, R.id.avatar, R.id.game, R.id.score});
        ListView list = (ListView) findViewById(R.id.scoreList);
        list.setAdapter(adapter);

        // prepares and passes the information from the list to the DetailActivity page
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                TextView selectedName = (TextView) v.findViewById(R.id.name);
                TextView selectedAvatar = (TextView) v.findViewById(R.id.avatar);
                TextView selectedGame = (TextView) v.findViewById(R.id.game);
                TextView selectedScore = (TextView) v.findViewById(R.id.score);
                String name = selectedName.getText().toString();
                String avatar = selectedAvatar.getText().toString();
                String game = selectedGame.getText().toString();
                String score = selectedScore.getText().toString();
                int finalScore = Integer.parseInt(score);
                i.putExtra("name", name);
                i.putExtra("avatar", avatar);
                i.putExtra("game", game);
                i.putExtra("score", finalScore);
                startActivity(i);

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
