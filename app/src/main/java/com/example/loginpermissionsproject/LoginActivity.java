package com.example.loginpermissionsproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class LoginActivity extends AppCompatActivity {
    private MaterialTextView login_LBL_header;
    private MaterialButton login_BTN_RECORD_AUDIO_permissions;
    private MaterialButton login_BTN_CALENDAR_permissions;
    private MaterialButton login_BTN_CAMERA_permissions;
    private MaterialButton login_BTN_signIn;
    private static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    private static final int PERMISSION_REQ_CODE_RECORD_AUDIO = 100;
    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private static final int PERMISSION_REQ_CODE_CAMERA = 101;
    private static final String PERMISSION_READ_CALENDAR = Manifest.permission.READ_CALENDAR;
    private static final String PERMISSION_WRITE_CALENDAR = Manifest.permission.WRITE_CALENDAR;
    private static final int PERMISSION_REQ_CODE_CALENDAR = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
        initViews();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQ_CODE_RECORD_AUDIO:
                handleRecordAudioPermissionResult(grantResults);
                break;
            case PERMISSION_REQ_CODE_CAMERA:
                handleCameraPermissionResult(grantResults);
                break;
            case PERMISSION_REQ_CODE_CALENDAR:
                handleCalendarPermissionResult(grantResults);
                break;
        }
    }

    private void handleRecordAudioPermissionResult(int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Granted. You can use the API which requires the permission.", Toast.LENGTH_LONG).show();
        } else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_RECORD_AUDIO)) {
            showPermissionDeniedDialog("Microphone (Record Audio) permission", this::requestRuntimePermissionRecordAudio);
        } else {
            requestRuntimePermissionRecordAudio();
        }
    }

    private void handleCameraPermissionResult(int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Granted. You can use the API which requires the permission.", Toast.LENGTH_LONG).show();
        } else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_CAMERA)) {
            showPermissionDeniedDialog("Camera permission", this::requestRuntimePermissionCamera);
        } else {
            requestRuntimePermissionCamera();
        }
    }

    private void handleCalendarPermissionResult(int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissions Granted. You can use the API which requires the permissions.", Toast.LENGTH_LONG).show();
        } else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_READ_CALENDAR) && !ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_WRITE_CALENDAR)) {
            showPermissionDeniedDialog("Calendar permissions", this::requestRuntimePermissionCalender);
        } else {
            requestRuntimePermissionCalender();
        }
    }

    private void showPermissionDeniedDialog(String permissionName, Runnable retryAction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialogTheme);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);
        builder.setView(dialogView);

        // Set the custom message text
        TextView dialogMessage = dialogView.findViewById(R.id.dialog_message);
        dialogMessage.setText("This feature is unavailable because this feature requires " + permissionName + " that you have denied.\n" +
                "Please allow " + permissionName + " from settings to proceed further.");

        builder.setTitle("Permission Required")
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Settings", (dialog, which) -> {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                    dialog.dismiss();
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // Apply the custom background color to buttons
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(ContextCompat.getColor(this, R.color.cream));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(ContextCompat.getColor(this, R.color.cream));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.darkGreen));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.darkGreen));
    }



    private void requestRuntimePermission(String[] permissions, int requestCode, String rationaleMessage) {
        boolean allPermissionsGranted = true;
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }

        if (allPermissionsGranted) {
            Toast.makeText(this, "Permissions Granted. You can use the API which requires the permissions.", Toast.LENGTH_LONG).show();
        } else {
            boolean shouldShowRationale = false;
            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    shouldShowRationale = true;
                    break;
                }
            }

            if (shouldShowRationale) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialogTheme);
                LayoutInflater inflater = this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);
                builder.setView(dialogView);
                TextView dialogMessage = dialogView.findViewById(R.id.dialog_message);
                dialogMessage.setText(rationaleMessage);

                builder.setTitle("Permissions Required")
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, which) -> {
                            ActivityCompat.requestPermissions(LoginActivity.this, permissions, requestCode);
                            dialog.dismiss();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                // Apply the custom background color to buttons
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.darkGreen));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(ContextCompat.getColor(this, R.color.cream));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.darkGreen));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(ContextCompat.getColor(this, R.color.cream));
            } else {
                ActivityCompat.requestPermissions(this, permissions, requestCode);
            }
        }
    }


    private void requestRuntimePermissionRecordAudio() {
        requestRuntimePermission(
                new String[]{PERMISSION_RECORD_AUDIO},
                PERMISSION_REQ_CODE_RECORD_AUDIO,
                "This app requires RECORD_AUDIO permission for particular feature to work as expected."
        );
    }

    private void requestRuntimePermissionCamera() {
        requestRuntimePermission(
                new String[]{PERMISSION_CAMERA},
                PERMISSION_REQ_CODE_CAMERA,
                "This app requires CAMERA permission for particular feature to work as expected."
        );
    }

    private void requestRuntimePermissionCalender() {
        requestRuntimePermission(
                new String[]{PERMISSION_READ_CALENDAR, PERMISSION_WRITE_CALENDAR},
                PERMISSION_REQ_CODE_CALENDAR,
                "This app requires Calendar permissions for particular features to work as expected."
        );
    }

    private boolean areAllPermissionsGranted() {
        return ActivityCompat.checkSelfPermission(this, PERMISSION_RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, PERMISSION_READ_CALENDAR) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, PERMISSION_WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED;
    }


    private void showMissingPermissions() {
        StringBuilder missingPermissions = new StringBuilder("The following permissions are missing:\n\n");

        if (ActivityCompat.checkSelfPermission(this, PERMISSION_RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            missingPermissions.append("Microphone (Record Audio)\n");
        }
        if (ActivityCompat.checkSelfPermission(this, PERMISSION_CAMERA) != PackageManager.PERMISSION_GRANTED) {
            missingPermissions.append("Camera\n");
        }
        if (ActivityCompat.checkSelfPermission(this, PERMISSION_READ_CALENDAR) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, PERMISSION_WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            missingPermissions.append("Calendar (Read and Write)\n");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialogTheme);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);
        builder.setView(dialogView);
        TextView dialogMessage = dialogView.findViewById(R.id.dialog_message);
        dialogMessage.setText(missingPermissions.toString());

        builder.setTitle("Missing Permissions")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // Apply the custom background color to buttons
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.darkGreen));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(ContextCompat.getColor(this, R.color.cream));

    }


    private void initViews() {
        login_BTN_RECORD_AUDIO_permissions.setOnClickListener(v -> requestRuntimePermissionRecordAudio());
        login_BTN_CAMERA_permissions.setOnClickListener(v -> requestRuntimePermissionCamera());
        login_BTN_CALENDAR_permissions.setOnClickListener(v -> requestRuntimePermissionCalender());
        login_BTN_signIn.setOnClickListener(v -> {
            if (areAllPermissionsGranted()) {
                // Move to MainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                showMissingPermissions();
            }
        });
    }

    private void findViews() {
        login_BTN_RECORD_AUDIO_permissions = findViewById(R.id.login_BTN_RECORD_AUDIO_permissions);
        login_BTN_CAMERA_permissions = findViewById(R.id.login_BTN_CAMERA_permissions);
        login_BTN_CALENDAR_permissions = findViewById(R.id.login_BTN_CALENDAR_permissions);
        login_BTN_signIn = findViewById(R.id.login_BTN_signIn);
        login_LBL_header = findViewById(R.id.login_LBL_header);
    }
}
