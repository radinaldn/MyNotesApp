package id.topapp.radinaldn.mynotesapp.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import id.topapp.radinaldn.mynotesapp.db.DatabaseContract;

import static android.provider.BaseColumns._ID;
import static id.topapp.radinaldn.mynotesapp.db.DatabaseContract.getColumnString;
import static id.topapp.radinaldn.mynotesapp.db.DatabaseContract.getColumnInt;

/**
 * Created by radinaldn on 26/01/19.
 */

public class Note implements Parcelable {

    private int id;
    private String title;
    private String description;
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public Note() {
    }

    // Untuk content provider
    public Note(Cursor cursor) {
        this.id = getColumnInt(cursor, _ID);
        this.title = getColumnString(cursor, DatabaseContract.NoteColumn.TITLE);
        this.description = getColumnString(cursor, DatabaseContract.NoteColumn.DESCRIPTION);
        this.date = getColumnString(cursor, DatabaseContract.NoteColumn.DATE);
    }
    // End Untuk content provider

    protected Note(Parcel in) {
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
