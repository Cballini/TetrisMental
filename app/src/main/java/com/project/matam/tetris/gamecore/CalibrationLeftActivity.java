package com.project.matam.tetris.gamecore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.project.matam.tetris.R;

/**
 * Created by cecib on 03/07/2017.
 */

public class CalibrationLeftActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration_left);

        final Button playButton = (Button) findViewById(R.id.calibrationButtonOKLeft);
        playButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalibrationLeftActivity.this, CalibrationRightActivity.class);
                startActivity(intent);
            }
        });

    }
}
