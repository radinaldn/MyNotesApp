package id.topapp.radinaldn.mynotesapp.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import id.topapp.radinaldn.mynotesapp.db.DatabaseContract;
import id.topapp.radinaldn.mynotesapp.db.NoteHelper;

import static id.topapp.radinaldn.mynotesapp.db.DatabaseContract.AUTHORITY;
import static id.topapp.radinaldn.mynotesapp.db.DatabaseContract.CONTENT_URI;

/**
 * Created by radinaldn on 28/01/19.
 */

public class NoteProvider extends ContentProvider {

    private static final int NOTE = 1;
    private static final int NOTE_ID = 2;

    private static final UriMatcher sUriMatcher = new
            UriMatcher(UriMatcher.NO_MATCH);

    static {

        // content://id.topapp.radinaldn/note
        sUriMatcher.addURI(AUTHORITY, DatabaseContract.TABLE_NOTE, NOTE);

        // content://id.topapp.radinaldn/note/id
        sUriMatcher.addURI(AUTHORITY,
                DatabaseContract.TABLE_NOTE+ "/#",
                NOTE_ID);
    }

    private NoteHelper noteHelper;


    @Override
    public boolean onCreate() {
        noteHelper = new NoteHelper(getContext());
        noteHelper.open();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)){
            case NOTE:
                cursor = noteHelper.queryProvider();
                break;

            case NOTE_ID:
                cursor = noteHelper.queryIdProvider(uri.getLastPathSegment());
                break;

            default:
                cursor = null;
                break;
        }

        if (cursor!=null){
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long added;

        switch (sUriMatcher.match(uri)){
            case NOTE:
                added = noteHelper.insertProvider(contentValues);
                break;

            default:
                added = 0;
                break;
        }

        if (added > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return Uri.parse(CONTENT_URI + "/" +added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deleted;
        switch (sUriMatcher.match(uri)){
            case NOTE_ID:
                deleted = noteHelper.deleteProvider(uri.getLastPathSegment());
                break;

            default:
                deleted = 0;
                break;
        }

        if (deleted > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int updated;
        switch (sUriMatcher.match(uri)){
            case NOTE_ID:
                updated = noteHelper.updateProvider(uri.getLastPathSegment(), values);
                break;

            default:
                updated = 0;
                break;
        }

        if (updated > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updated;
    }
}
