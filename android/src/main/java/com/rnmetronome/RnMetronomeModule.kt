package com.rnmetronome

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise

import com.rnmetronome.metronome.Metronome

class RnMetronomeModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  val metronome = Metronome();

  override fun getName(): String {
    return NAME
  }

  @ReactMethod
  fun play(bpm: Int) {
    metronome.play(bpm)
  }

  @ReactMethod
  fun stop() {
    metronome.stop();
  }

  @ReactMethod
  fun release() {
    metronome.playerRelease();
  }



  // Example method
  // See https://reactnative.dev/docs/native-modules-android
//  @ReactMethod
//  fun multiply(a: Double, b: Double, promise: Promise) {
//    promise.resolve(a * b)
//  }

  companion object {
    const val NAME = "RnMetronome"
  }
}
