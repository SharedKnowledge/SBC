package net.sharksystem.sbc.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import net.sharksystem.sbc.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SetupActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText nameEdit;
    private EditText interestEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        findViewById(R.id.setup_button).setOnClickListener(this);

        interestEdit = (EditText) findViewById(R.id.edittextSetupInterest);
        nameEdit = (EditText) findViewById(R.id.edittextSetupName);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        String name = nameEdit.getText().toString();
        String interest = interestEdit.getText().toString();
        intent.putExtra("name", name);
        intent.putExtra("interest", interest);
        startActivity(intent);
    }

}
