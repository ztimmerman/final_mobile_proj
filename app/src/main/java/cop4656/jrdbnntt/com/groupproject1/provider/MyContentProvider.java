package cop4656.jrdbnntt.com.groupproject1.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import cop4656.jrdbnntt.com.groupproject1.provider.table.Course;
import cop4656.jrdbnntt.com.groupproject1.provider.table.DatabaseTable;
import cop4656.jrdbnntt.com.groupproject1.provider.table.ParkingLocation;

public class MyContentProvider extends ContentProvider {

    MainDatabaseHelper helper1;
    private static final ArrayList<DatabaseTable> tables;

    static {
        tables = new ArrayList<>();
        tables.add(new Course());
        tables.add(new ParkingLocation());
    }

    public static final String AUTHORITY = "cop4656.jrdbnntt.com.groupproject1.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/");

    @Override
    public boolean onCreate() {
        helper1 = new MainDatabaseHelper(getContext(), tables);
        return true;
    }

    private static boolean isValidTableName(String tableName) {
        for (DatabaseTable table : tables) {
            if (table.getTableName().equals(tableName)) {
                return true;
            }
        }
        return false;
    }

    public static Uri getUriForTable(String tableName) {
        // Check table
        if (!isValidTableName(tableName)) {
            throw new IllegalArgumentException("Invalid table name");
        }

        return Uri.withAppendedPath(BASE_CONTENT_URI, tableName);
    }

    private String getTableFromUri(Uri uri) {
        String tableName = uri.getPathSegments().get(0);
        if (!isValidTableName(tableName)) {
            throw new IllegalArgumentException("Invalid table name");
        }
        return tableName;
    }

    @Override
    public int delete(@NonNull Uri uri, String whereClause, String[] whereArgs) {
        return helper1.getWritableDatabase().delete(getTableFromUri(uri), whereClause, whereArgs);
    }

    @Override
    public String getType(@NonNull Uri uri)
    {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        String tableName = getTableFromUri(uri);
        long id = helper1.getWritableDatabase().insert(tableName, null, values);

        return Uri.withAppendedPath(uri, "" + id);
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] columns, String selection, String[] args, String orderBy) {
        return helper1.getReadableDatabase().query(
                getTableFromUri(uri), columns, selection, args, null, null, orderBy);
    }


    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return helper1.getWritableDatabase().update(getTableFromUri(uri), values, selection, selectionArgs);
    }


    private static final class MainDatabaseHelper extends SQLiteOpenHelper {
        ArrayList<DatabaseTable> tables;
        private static final String DB_NAME = "ScheduleDB1";

        MainDatabaseHelper(Context context, ArrayList<DatabaseTable> tables) {
            super(context, DB_NAME, null, 1);
            this.tables = tables;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "";
            for (DatabaseTable table : tables) {
                sql += table.getCreateSql();
            }
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {}
    }
}
