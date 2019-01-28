package id.topapp.radinaldn.mynotesapp.db;

import android.provider.BaseColumns;

/**
 * Created by radinaldn on 26/01/19.
 */

public class DatabaseContract {

    public static String TABLE_NOTE = "note";

    public static final class NoteColumn implements BaseColumns{

        // Note tilte
        public static String TITLE = "title";
        // Note description
        public static String DESCRIPTION = "description";
        // Note date
        public static String DATE = "date";


    }
}
