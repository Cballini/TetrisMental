package com.project.matam.tetris.gamecore;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.emotiv.insight.IEdk;
import com.project.matam.tetris.R;

/**
 * Created by cecib on 03/07/2017.
 */

public class StartActivity extends AppCompatActivity implements EngineInterface {
    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int MY_PERMISSIONS_REQUEST_BLUETOOTH = 0;
    EngineConnector engineConnector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        EngineConnector.setContext(this);
        engineConnector =EngineConnector.shareInstance();
        engineConnector.delegate = this;

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /***Android 6.0 and higher need to request permission*****/
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_BLUETOOTH);
            }
            else{
                checkConnect();
            }
        }
        else {
            checkConnect();
        }


        final Button playButton = (Button) findViewById(R.id.play);
        playButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final Button settingsButton = (Button) findViewById(R.id.settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, CalibrationLeftActivity.class);
                startActivity(intent);
            }
        });

        final Button bluetoothButton = (Button) findViewById(R.id.bluetooth);
        bluetoothButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkConnect();
                if (!engineConnector.isConnected){
                    Toast.makeText(StartActivity.this, "You need to connect to your headset.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(StartActivity.this, "Connexion OK.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_BLUETOOTH: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    checkConnect();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "App can't run without this permission", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK){
                System.out.println("TEST connexionDone");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "You must be turn on bluetooth to connect with Emotiv devices"
                        , Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkConnect(){
        if (!mBluetoothAdapter.isEnabled()) {
            /****Request turn on Bluetooth***************/
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        else
        {
            //Connect to emoEngine
            IEdk.IEE_EngineConnect(this,"");
        }
    }

    @Override
    public void trainStarted() {

    }

    @Override
    public void trainSucceed() {

    }

    @Override
    public void trainCompleted() {

    }

    @Override
    public void trainRejected() {

    }

    @Override
    public void trainErased() {

    }

    @Override
    public void trainReset() {

    }

    @Override
    public void userAdded(int userId) {

    }

    @Override
    public void userRemove() {

    }

    @Override
    public void detectedActionLowerFace(String typeAction) {

    }
}
