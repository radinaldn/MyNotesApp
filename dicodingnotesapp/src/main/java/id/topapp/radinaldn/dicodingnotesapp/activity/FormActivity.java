package id.topapp.radinaldn.dicodingnotesapp.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import id.topapp.radinaldn.dicodingnotesapp.R;
import id.topapp.radinaldn.dicodingnotesapp.db.NoteItem;

import static id.topapp.radinaldn.dicodingnotesapp.db.DatabaseContract.CONTENT_URI;
import static id.topapp.radinaldn.dicodingnotesapp.db.DatabaseContract.NoteColumn.DATE;
import static id.topapp.radinaldn.dicodingnotesapp.db.DatabaseContract.NoteColumn.DESCRIPTION;
import static id.topapp.radinaldn.dicodingnotesapp.db.DatabaseContract.NoteColumn.TITLE;

public class FormActivity extends AppCompatActivity implements
        View.OnClickListener{

    EditText etTitle, etDescription;
    Button btSubmit;

    public static String EXTRA_NOTE_ITEM = "extra_note_item";
    private NoteItem noteItem = null;
    private boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        btSubmit = findViewById(R.id.btSubmit);
        btSubmit.setOnClickListener(this);

        Uri uri = getIntent().getData();

        if (uri != null){
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            if (cursor != null){
                if (cursor.moveToFirst()) noteItem = new NoteItem(cursor);
                cursor.close();
            }
        }

        String actionBarTitle = null;
        String btActionTitle = null;
        if (noteItem != null){
            isUpdate = true;
            actionBarTitle = "Update";
            btActionTitle = "Save";

            etTitle.setText(noteItem.getTitle());
            etDescription.setText(noteItem.getDescription());

        } else {
            actionBarTitle = "Create New";
            btActionTitle = "Submit";
        }

        btSubmit.setText(btActionTitle);
        getSupportActionBar().setTitle(actionBarTitle);;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btSubmit){
            String title = etTitle.getText().toString().trim();
            String description = etDescription.getText().toString().trim();

            boolean isEmptyField = false;
            if (TextUtils.isEmpty(title)){
                isEmptyField = true;
                etTitle.setError("Field must not empty");
            }

            if (!isEmptyField){
                ContentValues mContentValues = new ContentValues();
                mContentValues.put(TITLE, title);
                mContentValues.put(DESCRIPTION, description);
                mContentValues.put(DATE, getCurrentDate());

                if (isUpdate){
                    Uri uri = getIntent().getData();
                    getContentResolver().update(uri, mContentValues, null,null);

                    Toast.makeText(this, "One note updated successfuly", Toast.LENGTH_SHORT).show();

                } else {
                    getContentResolver().insert(CONTENT_URI, mContentValues);
                    Toast.makeText(this, "One note added successfuly", Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        }
    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isUpdate){
            getMenuInflater().inflate(R.menu.menu_form, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() ==  android.R.id.home){
            finish();
        }

        if (item.getItemId() == R.id.action_delete){
            showDeleteAlertDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteAlertDialog() {
        String dialogMessage = "Do you want to delete this item?";
        String dialogTitle = "Delete Note";

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(dialogTitle)
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = getIntent().getData();

                        getContentResolver().delete(uri, null, null);
                        Toast.makeText(FormActivity.this, "One item deleted successfuly", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

