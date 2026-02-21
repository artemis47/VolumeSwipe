## VolumeSwipe Lsposed Module(HyperOS A14-16)

Bring volume panel when you swipe near your volume buttons.

It is system-level volume control via gesture modification on HyperOS. Helpful when :

- broken physical button
- don't want to press buttons
- unsuitable button placement

---

## ⚙️ What it does

The implementation hooks into:

- `GestureStubView.onTouchEvent`

1. Detect ACTION_DOWN in defined screen zone
2. Decide whether to trigger volume mode
3. Consume gesture to prevent back navigation
4. Adjust system volume using:
   - `AudioManager.adjustStreamVolume()`
5. Optionally inject key events for system-level compatibility

---

## 📱 Compatibility

Required:
- HyperOS 1-3 A14-16
- Gesture System Navigation
- latest MiuiHome
- Root + Lsposed

Tested on:
- HyperOS 2, A15
- MiuiHome v6.01
- Xiaomi 12 (cupid)

---

## 🚀 Future Improvements

- Haptic feedback tuning
- Customizable swipe regions
- Volume sensitivity scaling
- Multi-mode gesture mapping

---

## ⚠️ Disclaimer

This project modifies system behavior and is intended for research and development purposes only.  
Use at your own risk.

---

## 👨‍💻 Author

_artemis  

---
