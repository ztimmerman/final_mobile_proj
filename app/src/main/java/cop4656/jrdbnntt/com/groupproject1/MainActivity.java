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
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "IPw0iAHmSn3xc04uljcow3u5J";
    private static final String TWITTER_SECRET = "lVz9bNWnW2z4LZUfrppl2W7QaMVHSwdjAOlw31zNs7dnGgNgPt";
    public static final String TAG = "MainActivity";


    private TwitterLoginButton loginButton;


    Button bAddCourse;
    Button bViewSchedule;
    Button postTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new TweetComposer());
        setContentView(R.layout.activity_main);

        bAddCourse = (Button) findViewById(R.id.bAddCourse);
        bViewSchedule = (Button) findViewById(R.id.bViewSchedule);
        postTweet = (Button) findViewById(R.id.postTweet);

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

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

    }
    protected void postTweetListener(View v) {
        TweetComposer.Builder builder = new TweetComposer.Builder(this)
                .text("just setting up my Fabric.");
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
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
