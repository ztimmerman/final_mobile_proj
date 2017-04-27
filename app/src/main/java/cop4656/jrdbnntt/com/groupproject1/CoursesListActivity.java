package cop4656.jrdbnntt.com.groupproject1;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import cop4656.jrdbnntt.com.groupproject1.provider.MyContentProvider;
import cop4656.jrdbnntt.com.groupproject1.provider.table.Course;

/**
 * Lists courses in database
 * - Click for nav/edit dialog
 * - Long click for delete dialog
 */

public class CoursesListActivity extends ListActivity {

    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] listColumns = new String[] {
                Course.COLUMN_ID,
                Course.COLUMN_NAME,
                Course.COLUMN_ROOM,
                Course.COLUMN_START_TIME,
                Course.COLUMN_DAYS
        };
        int[] listItems = new int[] {
                R.id.tvCourseId,
                R.id.tvCourseName,
                R.id.tvRoom,
                R.id.tvStartTime,
                R.id.tvDays
        };

        adapter = new SimpleCursorAdapter(
                this, R.layout.course_list_item, createTableCursor(), listColumns, listItems
        );
        setListAdapter(adapter);

        // Delete Dialog on click
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvCourseId = (TextView) view.findViewById(R.id.tvCourseId);
                TextView tvCourseName = (TextView) view.findViewById(R.id.tvCourseName);

                Course course = new Course();
                course.setId(Long.parseLong(tvCourseId.getText().toString()));
                course.name = tvCourseName.getText().toString();

                promptAction(course);
            }
        });

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvCourseId = (TextView) view.findViewById(R.id.tvCourseId);
                TextView tvCourseName = (TextView) view.findViewById(R.id.tvCourseName);

                Course course = new Course();
                course.setId(Long.parseLong(tvCourseId.getText().toString()));
                course.name = tvCourseName.getText().toString();

                promptDelete(course);
                return true;
            }
        });
    }


    private Cursor createTableCursor() {
        return getContentResolver().query(
                MyContentProvider.getUriForTable(Course.TABLE_NAME), null, null, null, null
        );
    }


    public void promptAction(final Course course) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // TODO Edit
                        Toast.makeText(
                                getApplicationContext(),
                                "Sorry, not implemented :/",
                                Toast.LENGTH_SHORT
                        ).show();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // Navigate
                        Intent intent = NavigateToCourseActivity.newNavigateIntent(CoursesListActivity.this, course.id);
                        startActivity(intent);
                        break;
                }

                dialog.dismiss();
            }
        };


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(course.name)
                .setPositiveButton("Edit", dialogClickListener)
                .setNegativeButton("Navigate", dialogClickListener)
                .show();
    }

    public void promptDelete(final Course course) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Delete it!
                        String selectionClause = Course.COLUMN_ID + " = ?";
                        String[] selectionArgs = new String[] { course.id.toString() };
                        int rowsDeleted = getContentResolver().delete(
                                MyContentProvider.getUriForTable(Course.TABLE_NAME),
                                selectionClause,
                                selectionArgs
                        );

                        if (rowsDeleted != 1) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Error: Unable to delete",
                                    Toast.LENGTH_SHORT
                            ).show();
                        } else {
                            course.disable(getApplicationContext());
                        }

                        // Refresh list view
                        adapter.swapCursor(createTableCursor());

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Remove course '"+course.name+"'?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.iAddCourse:
                i = new Intent(this, AddNewCourseActivity.class);
                startActivity(i);
                break;
            case R.id.iPreferences:
                i = new Intent(this, PreferencesActivity.class);
                startActivity(i);
                break;
        }

        return true;
    }
}
