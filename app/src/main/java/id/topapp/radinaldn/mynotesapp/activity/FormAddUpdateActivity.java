package id.topapp.radinaldn.mynotesapp.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import id.topapp.radinaldn.mynotesapp.R;
import id.topapp.radinaldn.mynotesapp.db.NoteHelper;
import id.topapp.radinaldn.mynotesapp.model.Note;

import static id.topapp.radinaldn.mynotesapp.db.DatabaseContract.CONTENT_URI;
import static id.topapp.radinaldn.mynotesapp.db.DatabaseContract.NoteColumn.DATE;
import static id.topapp.radinaldn.mynotesapp.db.DatabaseContract.NoteColumn.DESCRIPTION;
import static id.topapp.radinaldn.mynotesapp.db.DatabaseContract.NoteColumn.TITLE;

public class FormAddUpdateActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etTitle, etDescription;
    Button btSubmit;

    public static String EXTRA_NOTE = "extra_note";
    public static String EXTRA_POSITION = "extra_position";

    private boolean isEdit = false;
    public static int REQUEST_ADD = 100;
    public static int RESULT_ADD = 101;
    public static int REQUEST_UPDATE = 200;
    public static int RESULT_UPDATE = 201;
    public static int RESULT_DELETE = 301;

    private Note note;
    private int position;
    private NoteHelper noteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_add_update);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        btSubmit = findViewById(R.id.btSubmit);
        btSubmit.setOnClickListener(this);

        noteHelper = new NoteHelper(this);
        noteHelper.open();

        Uri uri = getIntent().getData();

        if (uri != null){
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            if (cursor != null){
                if (cursor.moveToFirst()) note = new Note(cursor);
                cursor.close();
            }
        }

        String actionBarTitle = null;
        String btTitle = null;
        if (isEdit){
            actionBarTitle = "Edit";
            btTitle = "Update";
            etTitle.setText(note.getTitle());
            etDescription.setText(note.getDescription());
        } else {
            actionBarTitle = "Add";
            btTitle = "Save";
        }

        getSupportActionBar().setTitle(actionBarTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         btSubmit.setText(btTitle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (noteHelper != null){
            noteHelper.close();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btSubmit){
            String title = etTitle.getText().toString().trim();
            String description = etDescription.getText().toString().trim();

            boolean isEmpty = false;

            /*
            Jika field masih kosong tampilkan error
             */

            if (TextUtils.isEmpty(title)){
                isEmpty = true;
                etTitle.setError("Field can not be blank");
            }

            if (!isEmpty){
                // Gunakan contenvalues untuk menampung data
                ContentValues values = new ContentValues();
                values.put(TITLE, title);
                values.put(DESCRIPTION, description);

                /*
                Jika merupakan edit setResultnya UPDATE, dan jika bukan maka setResultnya ADD
                 */

                if (isEdit){
                    getContentResolver().update(getIntent().getData(), values,
                            null, null);
                    setResult(RESULT_UPDATE);
                    finish();
                } else {
                    values.put(DATE, getCurrentDate());
                    getContentResolver().insert(CONTENT_URI, values);
                    setResult(RESULT_ADD);
                    finish();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit){
            getMenuInflater().inflate(R.menu.menu_form, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete:
                showAlertDialog(ALERT_DIALOG_DELETE);
                break;

            case android.R.id.home:
                showAlertDialog(ALERT_DIALOG_CLOSE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlertDialog(int type) {
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle = null, dialogMessage = null;

        if (isDialogClose){
            dialogTitle = "Cancel";
            dialogMessage = "Do you want to cancel this changes?";
        } else {
            dialogMessage = "Are you sure delete this item?";
            dialogTitle = "Delete Note";
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isDialogClose){
                            finish();
                        } else {
                            getContentResolver().delete(getIntent().getData(), null, null);
                            setResult(RESULT_DELETE, null);
                            finish();
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }

    final int ALERT_DIALOG_CLOSE = 10;
    final int ALERT_DIALOG_DELETE = 20;

    /*
    konfirmasi dialog sebelum batal/hapus
    close 10
    delete 20
     */

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM//dd HH:mm:ss");
        Date date = new Date();

        return dateFormat.format(date);
    }


    // INI BELUM SELESAI
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FormAddUpdateActivity.REQUEST_ADD){
            if (resultCode == FormAddUpdateActivity.RESULT_ADD){

            }
        }
    }
}
