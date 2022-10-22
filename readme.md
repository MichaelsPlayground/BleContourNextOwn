# BLE Contour next own

This app is usable only with the Glucose Meter "Contour next" from Ascensia Diabetes Care.

Checked with nRF Connect:

Unknown Service:

00000000-0002-11e2-9e96-0800200c9a66:

Unknown characteristics:

00001021-0002-11e2-9e96-0800200c9a66: read

00001022-0002-11e2-9e96-0800200c9a66: indicate, write
see https://github.com/MilosKozak/xDrip-1/blob/master/app/src/main/java/com/eveningoutpost/dexdrip/Services/BluetoothGlucoseMeter.java

```plaintext
    private static final UUID CONTOUR_1022 = UUID.fromString("00001022-0002-11e2-9e96-0800200c9a66");
    private static final UUID CONTOUR_1025 = UUID.fromString("00001025-0002-11e2-9e96-0800200c9a66");
    private static final UUID CONTOUR_1026 = UUID.fromString("00001026-0002-11e2-9e96-0800200c9a66");
```

00001023-0002-11e2-9e96-0800200c9a66: notify, write no response

00001024-0002-11e2-9e96-0800200c9a66: notify

00001025-0002-11e2-9e96-0800200c9a66: indicate, write

00001026-0002-11e2-9e96-0800200c9a66: indicate, write

set the clock, see https://github.com/weliem/blessed-android/blob/master/app/src/main/java/com/welie/blessedexample/BluetoothHandler.java
```plaintext
    // Contour Glucose Service
    public static final UUID CONTOUR_SERVICE_UUID = UUID.fromString("00000000-0002-11E2-9E96-0800200C9A66");
    private static final UUID CONTOUR_CLOCK = UUID.fromString("00001026-0002-11E2-9E96-0800200C9A66");
```


