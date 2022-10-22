# BLE Contour next own

This app is usable only with the Glucose Meter "Contour next" from Ascensia Diabetes Care.

Model: 7901H, S/N 1F64699, 9650E Messung in md/dL

UUIDs checked with nRF Connect:

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

Log from blessed-android app:
```plaintext
I/BluetoothHandler: Found peripheral 'Contour7901H1F64699'
I/BluetoothPeripheral: connected to 'Contour7901H1F64699' (BONDED) in 0,2s
D/BluetoothPeripheral: discovering services of 'Contour7901H1F64699' with delay of 0 ms
D/BluetoothGatt: discoverServices() - device: 28:FF:B2:C1:06:83
D/BluetoothGatt: onSearchComplete() = Device=28:FF:B2:C1:06:83 Status=0
I/BluetoothPeripheral: discovered 5 services for 'Contour7901H1F64699'
I/BluetoothHandler: connected to 'Contour7901H1F64699'
D/BluetoothGatt: configureMTU() - device: 28:FF:B2:C1:06:83 mtu: 185
I/BluetoothPeripheral: requesting MTU of 185
D/BluetoothGatt: onConfigureMTU() - Device=28:FF:B2:C1:06:83 mtu=23 status=0
I/BluetoothHandler: new MTU set: 23
D/BluetoothPeripheral: reading characteristic <00002a29-0000-1000-8000-00805f9b34fb>
I/BluetoothHandler: Received manufacturer: AscensiaDiabetesCare
D/BluetoothPeripheral: reading characteristic <00002a24-0000-1000-8000-00805f9b34fb>
I/BluetoothHandler: Received modelnumber: 7901H
D/BluetoothGatt: setCharacteristicNotification() - uuid: 00002a2b-0000-1000-8000-00805f9b34fb enable: true
I/BluetoothHandler: SUCCESS: Notify set to 'true' for 00002a2b-0000-1000-8000-00805f9b34fb
D/BluetoothGatt: setCharacteristicNotification() - uuid: 00002a18-0000-1000-8000-00805f9b34fb enable: true
I/BluetoothHandler: SUCCESS: Notify set to 'true' for 00002a18-0000-1000-8000-00805f9b34fb
D/BluetoothGatt: setCharacteristicNotification() - uuid: 00002a34-0000-1000-8000-00805f9b34fb enable: true
I/BluetoothHandler: SUCCESS: Notify set to 'true' for 00002a34-0000-1000-8000-00805f9b34fb
D/BluetoothGatt: setCharacteristicNotification() - uuid: 00002a52-0000-1000-8000-00805f9b34fb enable: true
I/BluetoothHandler: SUCCESS: Notify set to 'true' for 00002a52-0000-1000-8000-00805f9b34fb
D/BluetoothGatt: setCharacteristicNotification() - uuid: 00001026-0002-11e2-9e96-0800200c9a66 enable: true
I/BluetoothHandler: SUCCESS: Notify set to 'true' for 00001026-0002-11e2-9e96-0800200c9a66
D/BluetoothPeripheral: writing <0101> to characteristic <00002a52-0000-1000-8000-00805f9b34fb>
I/BluetoothHandler: SUCCESS: Writing <0101> to <00002a52-0000-1000-8000-00805f9b34fb>
D/BluetoothPeripheral: writing <01e6070a160c26137800> to characteristic <00001026-0002-11e2-9e96-0800200c9a66>
I/BluetoothHandler: SUCCESS: Writing <01e6070a160c26137800> to <00001026-0002-11e2-9e96-0800200c9a66>
```

