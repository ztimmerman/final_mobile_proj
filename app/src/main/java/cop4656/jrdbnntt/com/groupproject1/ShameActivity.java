package cop4656.jrdbnntt.com.groupproject1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ShameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shame);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView warningText = (TextView) findViewById(R.id.ask_the_truth);
        warningText.setText("Are you really in " + savedInstanceState.getString("class") + " ?");

    }

    protected void shameYesFunct(View v) {
        Toast.makeText(this,"Liar. If you were in class you'd have your cell phone off.",Toast.LENGTH_LONG)
                .show();
    }
}
