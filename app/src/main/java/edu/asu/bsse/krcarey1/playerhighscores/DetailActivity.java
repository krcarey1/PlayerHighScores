package edu.asu.bsse.krcarey1.playerhighscores;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Copyright 2017 Kyle Carey
 *
 * Rights granted to Timothy Lindquist and any instructor for SER423 and the University of
 * Arizona State University to build and evaluate the software package for the purpose of
 * determining grade and program assessment.
 *
 * Purpose - This page is reached by either pressing the + fab button or by selecting a list
 * item on MainActivity. If the + button is pressed then the user may add a record to the table
 * by entering all the fields (required), or delete a record by entering name, avatar, and game that
 * matches the record. If an item is selected from the list on MainActivity, the text fields are
 * filled with the same information. From here the user can update the record by changing any of
 * the fields and selecting update, or delete the record entirely.
 *
 * @author  Kyle Carey    mailto:krcarey1@asu.edu
 *
 * @version April 22, 2017
 */


public class DetailActivity extends AppCompatActivity {
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        db= openOrCreateDatabase("ScoresDB", Context.MODE_PRIVATE, null);
        Button addButton = (Button) findViewById(R.id.buttonAdd);
        Button deleteButton = (Button) findViewById(R.id.buttonDelete);
        final EditText nameET = (EditText) findViewById(R.id.editName);
        final EditText avatarET = (EditText) findViewById(R.id.editAvatar);
        final EditText gameET = (EditText) findViewById(R.id.editGame);
        final EditText scoreET = (EditText) findViewById(R.id.editScore);

        // Passes info from List in MainActivity, otherwise sets to null
        Intent p = getIntent();
        final String name = p.getStringExtra("name");
        final String avatar = p.getStringExtra("avatar");
        final String game = p.getStringExtra("game");
        final int score = p.getIntExtra("score", 0);

        // If clicked on list item from MainActivity, set the text fields with the values
        if (name != null) {
            nameET.setText(name);
            avatarET.setText(avatar);
            gameET.setText(game);
            scoreET.setText(String.valueOf(score));
            addButton.setText("Update"); // changes text of add button since record can only be updated

            // Update action
            addButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Cursor cur = db.rawQuery("SELECT id from scores where name = " + "\"" + name + "\""
                            + "and avatar =" + "\"" + avatar + "\"" + "and game =" + "\"" + game + "\"", null);
                    cur.moveToFirst();
                    if (cur.getCount() == 0) {
                        dlgAlert.setMessage("Record does not exist.");
                        dlgAlert.setTitle("Error");
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                        dlgAlert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //dismiss the dialog
                                    }
                                });

                    } else {
                        String name = nameET.getText().toString();
                        String avatar = avatarET.getText().toString();
                        String game = gameET.getText().toString();
                        String score = scoreET.getText().toString();
                        int finalScore = intParse(score);
                        ContentValues values = new ContentValues();
                        values.put("name", name);
                        values.put("avatar", avatar);
                        values.put("game", game);
                        values.put("score", finalScore);
                        int id = cur.getInt(cur.getColumnIndex("id"));
                        String finalId = Integer.toString(id);
                        cur.close();
                        // Updates record in the database
                        if (finalId != null) {
                            db.update("scores", values, "id=?", new String[]{finalId});
                        }
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);

                    }

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);

                }
            });

        } else {

            addButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    String name = nameET.getText().toString();
                    String avatar = avatarET.getText().toString();
                    String game = gameET.getText().toString();
                    String score = scoreET.getText().toString();
                    if (name.equals("")) {
                        dlgAlert.setMessage("Please enter a name.");
                        dlgAlert.setTitle("Error");
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                        dlgAlert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //dismiss the dialog
                                    }
                                });
                    } else if (avatar.equals("")) {
                        dlgAlert.setMessage("Please enter an avatar.");
                        dlgAlert.setTitle("Error");
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                        dlgAlert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //dismiss the dialog
                                    }
                                });

                    } else if (game.equals("")) {
                        dlgAlert.setMessage("Please enter a game title.");
                        dlgAlert.setTitle("Error");
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                        dlgAlert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //dismiss the dialog
                                    }
                                });

                    } else if (score.equals("")) {
                        dlgAlert.setMessage("Please enter a score.");
                        dlgAlert.setTitle("Error");
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                        dlgAlert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //dismiss the dialog
                                    }
                                });

                    } else {
                        int finalScore = intParse(score);
                        ContentValues values = new ContentValues();
                        values.put("name", name);
                        values.put("avatar", avatar);
                        values.put("game", game);
                        values.put("score", finalScore);
                        // Adds new record to the database
                        db.insert("scores", null, values);
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    }

                }
            });

        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = nameET.getText().toString();
                String avatar = avatarET.getText().toString();
                String game = gameET.getText().toString();
                // looks for id of record in database to use for deletion
                Cursor cur = db.rawQuery("SELECT id from scores where name = " + "\"" + name + "\""
                        + "and avatar =" + "\"" + avatar + "\"" + "and game =" + "\"" + game + "\"", null);
                cur.moveToFirst();
                if (cur.getCount() == 0) {
                    dlgAlert.setMessage("Record does not exist.");
                    dlgAlert.setTitle("Error");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                    dlgAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //dismiss the dialog
                                }
                            });

                } else {
                    int id = cur.getInt(cur.getColumnIndex("id"));
                    String finalId = Integer.toString(id);
                    cur.close();
                    // Deletes record from the database
                    if (finalId != null) {
                        db.delete("scores", "id=?", new String[]{finalId});
                    }
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);

                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public static Integer intParse(String score){
        try {
            return Integer.parseInt(score);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
