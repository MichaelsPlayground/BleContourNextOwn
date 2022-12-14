package de.androidcrypto.blecontournextown;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    //Button scanForDevices, getCurrentTime, setCurrentTimeNotification, unsetCurrentTimeNotification, setTime;
    Button connectDevice;

    String heartRateMeasurementString;
    com.google.android.material.textfield.TextInputLayout sendData;
    com.google.android.material.textfield.TextInputEditText connectedDevice;
    com.google.android.material.textfield.TextInputEditText heartRateMeasurement;
    com.google.android.material.textfield.TextInputEditText currentTime;
    com.google.android.material.textfield.TextInputEditText manufacturerName;
    com.google.android.material.textfield.TextInputEditText requestedModelNumber; // new for write
    Button getModelNumber, setModelNumber; // new for write and read

    int mYear, mMonth, mDay, mHour, mMinute, mSecond = 0; // filled by date and time pickers

    BluetoothHandler bluetoothHandler;
    String connectedDeviceFromBluetoothHandler;

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int ACCESS_LOCATION_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectedDevice = findViewById(R.id.etMainConnectedDevice);
        heartRateMeasurement = findViewById(R.id.etMainHeartRateMeasurement);
        currentTime = findViewById(R.id.etMainCurrentTime);
        manufacturerName = findViewById(R.id.etMainManufacturerNameMeasurement);
        requestedModelNumber = findViewById(R.id.etMainModelNumberMeasurement);

        sendData = findViewById(R.id.etMainInputWithEnterIconLayout);
        sendData.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeToUiToastLong("send icon pressed");
            }
        });

        // get the connectedDevice in case of "back key" pushed
        if (connectedDeviceFromBluetoothHandler != null) {
            connectedDevice.setText(connectedDeviceFromBluetoothHandler);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        registerReceiver(connectedDeviceDataReceiver, new IntentFilter((BluetoothHandler.CONNECTED_DEVICE_ACTION)));
        registerReceiver(manufacturerNameDataReceiver, new IntentFilter(BluetoothHandler.MEASUREMENT_MANUFACTURER_NAME));
        registerReceiver(modelNumberDataReceiver, new IntentFilter(BluetoothHandler.MEASUREMENT_MODEL_NUMBER));
        registerReceiver(currentTimeDataReceiver, new IntentFilter(BluetoothHandler.MEASUREMENT_CURRENT_TIME));

        // bluetoothHandler = BluetoothHandler.getInstance(getApplicationContext(), macAddress);

        connectDevice = findViewById(R.id.btnMainConnect);
        connectDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Main connectDevice");
                //bluetoothHandler = BluetoothHandler.getInstance(getApplicationContext(), macAddress);
                bluetoothHandler = BluetoothHandler.getInstance2(getApplicationContext());
            }
        });

        Button getCurrentTime = findViewById(R.id.btnMainGetCurrentTime);
        getCurrentTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bluetoothHandler != null) {
                    Log.i("Main", "readCurrentTime started");
                    // todo get the address from connectedDevice
                    bluetoothHandler.readCurrentTime(connectedDeviceFromBluetoothHandler);
                }

            }
        });

        Button setCurrentTimeNotification = findViewById(R.id.btnMainSetCurrentTimeNotification);
        setCurrentTimeNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bluetoothHandler != null) {
                    Log.i("Main", "readCurrentTime started");
                    bluetoothHandler.setCurrentTimeNotification(connectedDeviceFromBluetoothHandler, true);
                }

            }
        });

        Button unsetCurrentTimeNotification = findViewById(R.id.btnMainUnsetCurrentTimeNotification);
        unsetCurrentTimeNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bluetoothHandler != null) {
                    Log.i("Main", "readCurrentTime started");
                    bluetoothHandler.setCurrentTimeNotification(connectedDeviceFromBluetoothHandler, false);
                }

            }
        });

        TextView selectedDate = findViewById(R.id.tvMainSelectedDate);

        Button selectDate = findViewById(R.id.btnMainSelectDate);
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //selectedDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                selectedDate.setText(year + "-" + (monthOfYear + 1) + "-" +  dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        TextView selectedTime = findViewById(R.id.tvMainSelectedTime);

        Button selectTime = findViewById(R.id.btnMainSelectTime);
        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                selectedTime.setText(hourOfDay + ":" + minute + ":00");
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        Button setTime = findViewById(R.id.btnMainSetDeviceTime);
        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bluetoothHandler != null) {
                    Log.i("Main", "set time");

/*
int componentTimeToTimestamp(int year, int month, int day, int hour, int minute) {

    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.MONTH, month);
    c.set(Calendar.DAY_OF_MONTH, day);
    c.set(Calendar.HOUR, hour);
    c.set(Calendar.MINUTE, minute);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);

    return (int) (c.getTimeInMillis() / 1000L);
}
 */



                    //try {
                        String date = "2014-08-03 15:20:10"; //Replace with your value
                        //String date = selectedDate.getText().toString() + " " + selectedTime.getText().toString();
                        System.out.println("string: " + date);
                        Timestamp timestamp = Timestamp.valueOf(date);
                        // Convert timestamp to long for use
                        long timeParameter = timestamp.getTime();
                        System.out.println("timeParameter: " + timeParameter);
                        bluetoothHandler.setTimeOnDevice(connectedDeviceFromBluetoothHandler, timeParameter);
                    /*} catch (IllegalArgumentException e) {
                        // so date and time was given, abort
                        System.out.println("to set the time on the device you need to select the date and the time first");
                        writeToUiToastLong("to set the time on the device you need to select the date and the time first");

                    }*/
                }
            }
        });

        Button writeChar1025 = findViewById(R.id.btnMainSetChar1025);
        writeChar1025.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bluetoothHandler != null) {
                    Log.i("Main", "writeChar1025 started");
                    bluetoothHandler.writeChar1025(connectedDeviceFromBluetoothHandler);
                }
            }
        });

    }

    private void writeToUiToastLong(String message) {
        runOnUiThread(() -> {
            Toast.makeText(getApplicationContext(),
                    message,
                    Toast.LENGTH_LONG).show();
        });
    }

    /**
     * section for (runtime) permissions
     */

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] missingPermissions = getMissingPermissions(getRequiredPermissions());
            if (missingPermissions.length > 0) {
                requestPermissions(missingPermissions, ACCESS_LOCATION_REQUEST);
            } else {
                permissionsGranted();
            }
        }
    }

    private String[] getMissingPermissions(String[] requiredPermissions) {
        List<String> missingPermissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String requiredPermission : requiredPermissions) {
                if (getApplicationContext().checkSelfPermission(requiredPermission) != PackageManager.PERMISSION_GRANTED) {
                    missingPermissions.add(requiredPermission);
                }
            }
        }
        return missingPermissions.toArray(new String[0]);
    }

    private String[] getRequiredPermissions() {
        int targetSdkVersion = getApplicationInfo().targetSdkVersion;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && targetSdkVersion >= Build.VERSION_CODES.S) {
            return new String[]{Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT};
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && targetSdkVersion >= Build.VERSION_CODES.Q) {
            return new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        } else return new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};
    }

    private void permissionsGranted() {
        // Check if Location services are on because they are required to make scanning work for SDK < 31
        int targetSdkVersion = getApplicationInfo().targetSdkVersion;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S && targetSdkVersion < Build.VERSION_CODES.S) {
            if (checkLocationServices()) {
                //initBluetoothHandler(); // don't start here, use the initBluetoothHandler from selectedDevice
                //printToast("start scan to proceed");
            }
        } else {
            //initBluetoothHandler(); // don't start here, use the initBluetoothHandler from selectedDevice
            //printToast("start scan to proceed");
        }
    }

    private boolean areLocationServicesEnabled() {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            Timber.e("could not get location manager");
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return locationManager.isLocationEnabled();
        } else {
            boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            return isGpsEnabled || isNetworkEnabled;
        }
    }

    private boolean checkLocationServices() {
        if (!areLocationServicesEnabled()) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Location services are not enabled")
                    .setMessage("Scanning for Bluetooth peripherals requires locations services to be enabled.") // Want to enable?
                    .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    })
                    .create()
                    .show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Check if all permission were granted
        boolean allGranted = true;
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }

        if (allGranted) {
            permissionsGranted();
        } else {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Permission is required for scanning Bluetooth peripherals")
                    .setMessage("Please grant permissions")
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            checkPermissions();
                        }
                    })
                    .create()
                    .show();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();

        if (getBluetoothManager().getAdapter() != null) {
            if (!isBluetoothEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                checkPermissions();
            }
        } else {
            Timber.e("This device has no Bluetooth hardware");
        }
    }

    private boolean isBluetoothEnabled() {
        BluetoothAdapter bluetoothAdapter = getBluetoothManager().getAdapter();
        if(bluetoothAdapter == null) return false;

        return bluetoothAdapter.isEnabled();
    }

    @NotNull
    private BluetoothManager getBluetoothManager() {
        return Objects.requireNonNull((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE),"cannot get BluetoothManager");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*
        unregisterReceiver(heartRateDataReceiver);
        unregisterReceiver(locationServiceStateReceiver);
        unregisterReceiver(currentTimeDataReceiver);
         */
        unregisterReceiver(connectedDeviceDataReceiver);
        unregisterReceiver(manufacturerNameDataReceiver);
        unregisterReceiver(modelNumberDataReceiver);
        unregisterReceiver(currentTimeDataReceiver);
    }

    /**
     * section for broadcast receiver = all included ble services need one
     */

    private final BroadcastReceiver connectedDeviceDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String dataString = intent.getStringExtra(BluetoothHandler.CONNECTED_DEVICE_EXTRA);
            if (dataString == null) return;
            connectedDevice.setText(dataString);
            connectedDeviceFromBluetoothHandler = dataString.substring(dataString.length()-17);
            System.out.println("* connectedDeviceFromBluetoothHandler : " + connectedDeviceFromBluetoothHandler);
        }
    };

    private final BroadcastReceiver manufacturerNameDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String dataString = intent.getStringExtra(BluetoothHandler.MEASUREMENT_MANUFACTURER_NAME_EXTRA);
            if (dataString == null) return;
            manufacturerName.setText(dataString);
        }
    };

    private final BroadcastReceiver modelNumberDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String dataString = intent.getStringExtra(BluetoothHandler.MEASUREMENT_MODEL_NUMBER_EXTRA);
            if (dataString == null) return;
            requestedModelNumber.setText(dataString);
        }
    };

    private final BroadcastReceiver currentTimeDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String dataString = intent.getStringExtra(BluetoothHandler.MEASUREMENT_CURRENT_TIME_EXTRA);
            if (dataString == null) return;
            currentTime.setText(dataString);
        }
    };
}