package id.topapp.radinaldn.dicodingnotesapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;

import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import id.topapp.radinaldn.dicodingnotesapp.R;
import id.topapp.radinaldn.dicodingnotesapp.adapter.DicodingNotesAdapter;
import id.topapp.radinaldn.dicodingnotesapp.db.DatabaseContract;

import static id.topapp.radinaldn.dicodingnotesapp.db.DatabaseContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener{

    private DicodingNotesAdapter dicodingNotesAdapter;
    ListView lvNotes;

    private final int LOAD_NOTES_ID = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Dicoding Notes");

        lvNotes = (ListView)findViewById(R.id.lvNotes);
        dicodingNotesAdapter = new DicodingNotesAdapter(this, null, true);
        lvNotes.setAdapter(dicodingNotesAdapter);
        lvNotes.setOnItemClickListener(this);

        getSupportLoaderManager().initLoader(LOAD_NOTES_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        dicodingNotesAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        dicodingNotesAdapter.swapCursor(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(LOAD_NOTES_ID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionAdd){
            Intent intent = new Intent(MainActivity.this, FormActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        Cursor cursor = (Cursor) dicodingNotesAdapter.getItem(position);

        int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumn._ID));
        Intent intent = new Intent(MainActivity.this, FormActivity.class);
        intent.setData(Uri.parse(CONTENT_URI+"/"+id));
        startActivity(intent);
    }
}
