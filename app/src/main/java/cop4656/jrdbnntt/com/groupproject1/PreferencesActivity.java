package cop4656.jrdbnntt.com.groupproject1;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

/**
 * TODO
 */

public class PreferencesActivity extends PreferenceActivity {

    public static final String APP_PREFERENCES_NAME = "app_preferences";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(
                android.R.id.content,
                new PreferencesFragment()
        ).commit();


    }

    public static class PreferencesFragment extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
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
            case R.id.iViewSchedule:
                i = new Intent(this, CoursesListActivity.class);
                startActivity(i);
                break;
        }

        return true;
    }
}
