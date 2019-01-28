package id.topapp.radinaldn.dicodingnotesapp.db;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static id.topapp.radinaldn.dicodingnotesapp.db.DatabaseContract.getColumnInt;
import static id.topapp.radinaldn.dicodingnotesapp.db.DatabaseContract.getColumnString;

/**
 * Created by radinaldn on 28/01/19.
 */

public class NoteItem implements Parcelable {

    private int id;
    private String title, description, date;

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
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(date);
    }

    public NoteItem() {
    }

    public NoteItem(Cursor cursor) {
        this.id = getColumnInt(cursor, DatabaseContract.NoteColumn._ID);
        this.title = getColumnString(cursor, DatabaseContract.NoteColumn.TITLE);
        this.description = getColumnString(cursor, DatabaseContract.NoteColumn.DESCRIPTION);
        this.date = getColumnString(cursor, DatabaseContract.NoteColumn.DATE);
    }

    protected NoteItem(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        date = in.readString();
    }

    public static final Parcelable.Creator<NoteItem> CREATOR = new
            Creator<NoteItem>() {
        @Override
        public NoteItem createFromParcel(Parcel in) {
            return new NoteItem(in);
        }

        @Override
        public NoteItem[] newArray(int size) {
            return new NoteItem[size];
        }
    };
}
