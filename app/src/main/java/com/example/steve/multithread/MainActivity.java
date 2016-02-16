package com.example.steve.multithread;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This app has buttons that create a file with the numbers 1-10 on separate lines,
 * displays those numbers, and clears those numbers.
 *
 * @author Steve
 */
public class MainActivity extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    List<String> inputList = new ArrayList<String>();
    private ProgressBar progress;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set progress bar to the GUI progress bar
        progress = (ProgressBar) findViewById(R.id.progressBar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This class handles creating the
     * file and setting the progress.
     *
     */
    class CreateAsync extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            progress.setProgress(0);
        }

        @Override
        protected Void doInBackground(Void... params) {
            String fileName = "numbers.txt";
            String tempNum;

            FileOutputStream outputStream;

            try {
                outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                for (int i = 1; i < 11; i++) {
                    tempNum = String.valueOf(i) + "\n";
                    outputStream.write(tempNum.getBytes());
                    publishProgress(i * 10);
                    Thread.sleep(250);
                }
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progress.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {}
    }

    /**
     * This function calls the AsyncTask
     * that creates a number file.
     *
     */
    public void create(View v) {
        new CreateAsync().execute();
    }

    /**
     * This class handles displaying the
     * file and setting the progress.
     *
     */
    class LoadAsync extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            progress.setProgress(0);
        }

        @Override
        protected Void doInBackground(Void... params) {
            String fileName = "numbers.txt";

            FileInputStream inputStream;
            try {
                inputStream = openFileInput(fileName);
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(isr);
                String line;
                int i = 1;
                while ((line = bufferedReader.readLine()) != null) {
                    inputList.add(line);
                    publishProgress(i * 10);
                    i++;
                    Thread.sleep(250);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progress.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ListView myListView = (ListView) findViewById(R.id.listView);

            adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,
                    inputList);
            myListView.setAdapter(adapter);
        }
    }

    /**
     * This function calls the AsyncTask
     * that displays a number file.
     *
     */
    public void load(View v) {
        context = this;

        new LoadAsync().execute();
    }

    /**
     * This function calls clears
     * the ListView.
     *
     */
    public void clear(View v) {
        adapter.clear();
        ListView myListView = (ListView) findViewById(R.id.listView);
        myListView.setAdapter(adapter);
    }
}
