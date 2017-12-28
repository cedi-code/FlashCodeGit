package ch.appquest.boredboizz.flashcode;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    // in diesem Attribut wird die Nachricht gespeichert die man eingetipt hat
    private String message;

    // für auf die Kamera zu zugreifen
    private String mCameraId;
    private CameraManager mCameraManager;

    private Fragment receive;
    private Fragment transmit;

    // die Android Main methode wo alles initzialisiert wird
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // seztz das activity_main layout als main an
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // erstellt die beiden Fragments und Plaziert sie auf den Content tag
        transmit = new senderFragment();
        receive = new receiverFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content, transmit).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.content, receive).commit();

        // initzialisiert die BottomNavigation inklusive den Click event.
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Diese Methode überpüft ob das Gerät eine Kamera berechtigung hat
        checkValidety();

        // holt sich die Kamera vom System
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    // Setzt ein Onklick even für die Bottom Navigation
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager manager = getSupportFragmentManager();
            // switch für welches icon ausgewählt wurde
            switch (item.getItemId()) {
                case R.id.navigation_transmit:
                    // das erste Fragment wird versteckt und das andere angezeigt
                    manager.beginTransaction().hide(receive).commit();
                    manager.beginTransaction().show(transmit).commit();
                    return true;
                case R.id.navigation_receive:
                    // das zweite Fragment wird versteckt und das andere angezeigt
                    manager.beginTransaction().hide(transmit).commit();
                    manager.beginTransaction().show(receive).commit();
                    return true;
            }
            return false;

        }

    };
    // TODO ihr bekommst du die eingetipte nachricht Default: null
    public String getMessage() {
        return this.message;
    }
    // setzt die Nachricht für bearbetungs zwecke
    public void setMessage(String message) {
        this.message = message;
    }

    // TODO wenn du die Methode aufrufst geht das Flash licht an (es geht erst wieder aus wenn die turnOffFlashLight() Methode aufgerufen wird)
    public void turnOnFlashLight() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO wenn du die Methode aufrufst geht das Flash licht aus (es geht erst wieder an wenn die turnOnFlashLight() Methode aufgerufen wird)
    public void turnOffFlashLight() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // überpüft ob das Handy eine Kamera hat falls nicht kommt ein Popup mit einer nachricht und das Programm wird beendet.
    private void checkValidety() {
        boolean hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!hasFlash) {
            AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                    .create();
            alert.setTitle("Error");
            alert.setMessage("Sorry, your device doesn't support flash light!");
            alert.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alert.show();
        }
    }
}