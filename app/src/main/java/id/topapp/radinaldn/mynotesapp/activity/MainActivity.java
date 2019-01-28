package id.topapp.radinaldn.mynotesapp.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.LinkedList;

import id.topapp.radinaldn.mynotesapp.R;
import id.topapp.radinaldn.mynotesapp.adapter.NoteAdapter;
import id.topapp.radinaldn.mynotesapp.db.NoteHelper;
import id.topapp.radinaldn.mynotesapp.model.Note;

import static id.topapp.radinaldn.mynotesapp.db.DatabaseContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity
implements View.OnClickListener{

    RecyclerView rvNotes;
    ProgressBar progressBar;
    FloatingActionButton fabAdd;


    private NoteAdapter adapter;
    private NoteHelper noteHelper;

    private Cursor list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Notes");

        rvNotes = findViewById(R.id.rvNotes);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.setHasFixedSize(true);

        progressBar = findViewById(R.id.progressbar);
        fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(this);

        noteHelper = new NoteHelper(this);
        noteHelper.open();



        adapter = new NoteAdapter(this);
        adapter.setListNotes(list);
        rvNotes.setAdapter(adapter);

        new LoadNoteAsync().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fabAdd){
            Intent intent = new Intent(MainActivity.this,
                    FormAddUpdateActivity.class);
            startActivityForResult(intent, FormAddUpdateActivity.REQUEST_ADD);
        }
    }

    private class LoadNoteAsync extends AsyncTask<Void, Void, Cursor>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor notes) {
            super.onPostExecute(notes);
            progressBar.setVisibility(View.GONE);

            list = notes;
            adapter.setListNotes(list);
            adapter.notifyDataSetChanged();

            if (list.getCount() == 0){
                showSnackbarMessage("No data");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FormAddUpdateActivity.REQUEST_ADD){
            if (resultCode == FormAddUpdateActivity.RESULT_ADD) {
                new LoadNoteAsync().execute();
                showSnackbarMessage("One item has added successfuly");

//                int position = data.getIntExtra(FormAddUpdateActivity.EXTRA_POSITION, 0);
//                rvNotes.getLayoutManager().smoothScrollToPosition(rvNotes, new RecyclerView.State(), position);
            } else if (requestCode == FormAddUpdateActivity.REQUEST_UPDATE){

                if (resultCode == FormAddUpdateActivity.RESULT_UPDATE){
                    new LoadNoteAsync().execute();
                    showSnackbarMessage("One item has updated succesfuly");
                }

                else if (resultCode == FormAddUpdateActivity.RESULT_DELETE){
                    new LoadNoteAsync().execute();
                    showSnackbarMessage("One item has deleted succesfuly");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(rvNotes, message, Snackbar.LENGTH_SHORT).show();
    }
}
