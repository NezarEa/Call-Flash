## Call Flash Android App

This Android app uses the camera flash to create a visual alert during incoming calls. When a call is ringing, the app detects the phone’s state and toggles the camera flash on and off at set intervals, simulating a flashing effect. The flash automatically turns off once the call is answered or ends.

### Key Components

- **`Service`**: The app runs as a background service (`FlashOnCallService`), allowing it to monitor incoming calls and control the camera flash while the phone is ringing.
  
- **`BroadcastReceiver`**: A broadcast receiver listens for phone state changes using `TelephonyManager.ACTION_PHONE_STATE_CHANGED`. It determines when the phone is ringing, off-hook (answered), or idle (call ended).

- **Flash Toggle Using `CameraManager`**: The app uses `CameraManager` to access and control the camera flash. When the phone is in the ringing state, the app triggers the flash to blink at regular intervals using a `Handler`.

- **Flashing Logic**:
  - A `Handler` with `Runnable` is used to toggle the flash on and off at a specified interval (e.g., 500 milliseconds).
  - The `flashOnOffRunnable` keeps the flash in a blinking state during an incoming call and stops it when the call state changes to off-hook or idle.

### Code Highlights

- **Efficient Flash Control**: The app identifies the device's camera ID, ensuring the flash is only controlled on supported devices.
- **Runnable Timing**: A `Handler` running on the main looper keeps the flashing interval precise and reduces lag.

### Here’s what it looks like

https://github.com/user-attachments/assets/c82bfb27-c437-4a8c-a360-d9ee64f2eba4

