package cop4656.jrdbnntt.com.groupproject1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button bAddCourse;
    Button bViewSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bAddCourse = (Button) findViewById(R.id.bAddCourse);
        bViewSchedule = (Button) findViewById(R.id.bViewSchedule);

        bAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddNewCourseActivity.class);
                startActivity(intent);
            }
        });
        bViewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CoursesListActivity.class);
                startActivity(intent);
            }
        });
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
            case R.id.iPreferences:
                i = new Intent(this, PreferencesActivity.class);
                startActivity(i);
                break;
        }

        return true;
    }
}
