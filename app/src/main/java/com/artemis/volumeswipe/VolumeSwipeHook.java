package com.artemis.volumeswipe;

import android.content.Context;
import android.media.AudioManager;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class VolumeSwipeHook implements IXposedHookLoadPackage {

    private float startX;
    private float startY;

    private boolean volumeMode = false;
    private boolean triggered = false;

    private static final float HORIZONTAL_THRESHOLD = 90f;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {

        if (!lpparam.packageName.equals("com.miui.home"))
            return;

        XposedHelpers.findAndHookMethod(
                "com.miui.home.recents.GestureStubView",
                lpparam.classLoader,
                "onTouchEvent",
                MotionEvent.class,
                new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {

                        MotionEvent ev = (MotionEvent) param.args[0];
                        View view = (View) param.thisObject;

                        int action = ev.getActionMasked();

                        DisplayMetrics dm =
                                view.getResources().getDisplayMetrics();

                        float screenHeight = dm.heightPixels;
                        float upperZoneLimit = screenHeight * 0.35f;

                        switch (action) {

                            case MotionEvent.ACTION_DOWN:

                                startX = ev.getRawX();
                                startY = ev.getRawY();
                                triggered = false;

                                volumeMode = (startY < upperZoneLimit);

                                if (volumeMode) {
                                    param.setResult(true); // block MIUI immediately
                                }
                                break;

                            case MotionEvent.ACTION_MOVE:

                                if (!volumeMode)
                                    return;

                                float deltaX = ev.getRawX() - startX;

                                if (!triggered &&
                                        Math.abs(deltaX) > HORIZONTAL_THRESHOLD) {

                                    Context context = view.getContext();
                                    AudioManager audioManager =
                                            (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

                                    if (audioManager != null) {

                                        if (deltaX > 0) {
                                            audioManager.adjustStreamVolume(
                                                    AudioManager.STREAM_MUSIC,
                                                    AudioManager.ADJUST_RAISE,
                                                    AudioManager.FLAG_SHOW_UI);
                                        } else {
                                            audioManager.adjustStreamVolume(
                                                    AudioManager.STREAM_MUSIC,
                                                    AudioManager.ADJUST_LOWER,
                                                    AudioManager.FLAG_SHOW_UI);
                                        }
                                        // 🔥 HAPTIC FEEDBACK
                                        view.performHapticFeedback(
                                                android.view.HapticFeedbackConstants.LONG_PRESS
                                        );
                                    }

                                    triggered = true;
                                }

                                param.setResult(true);
                                break;

                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                if (volumeMode) {
                                    param.setResult(true);
                                }
                                volumeMode = false;
                                triggered = false;
                                break;
                        }
                    }
                });
    }
}