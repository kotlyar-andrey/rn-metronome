package com.rnmetronome.metronome

import kotlin.math.sin;
import kotlin.math.PI;
import kotlin.concurrent.thread;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

val MIN_BPM = 10;
val MAX_BPM = 600;

class Metronome {
  private var player: AudioTrack? = null;

  private val SAMPLE_RATE: Int = 8000;
  private val FREQUENCY: Double = 440.0;
  private val SOUND_CONST = SAMPLE_RATE  / FREQUENCY
  private val SAMPLES_SOUNDING: Int = 200;
  private val AMPLITUDE = 32767;

  var tone1 = 70;

  private var isPlaying: Boolean = false;
  private var bpm: Int = 80
  private var bps: Double = bpm / 60.0;
  private var samplesForOneTik: Int = (SAMPLE_RATE  / bps).toInt();
  var samplesCounter: Int = 1;


  private fun playerInitialize() {
    player = AudioTrack.Builder()
      .setAudioAttributes(
        AudioAttributes.Builder()
          .setUsage(AudioAttributes.USAGE_MEDIA)
          .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
          .build())
      .setAudioFormat(
        AudioFormat.Builder()
          .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
          .setSampleRate(SAMPLE_RATE )
          .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
          .build())
      .setTransferMode(AudioTrack.MODE_STREAM)
      .build();
  }

  public fun playerRelease() {
    player?.stop();
    player?.release();
    player = null;
  }

  private fun generateSoundByte(variable: Int) : Short {
    return (sin(3 * PI * variable / SOUND_CONST)*AMPLITUDE).toInt().toShort();
  }

  private fun getTone(samplesForOneTik: Int): ShortArray {
    var sample = ShortArray(SAMPLE_RATE);
    for (i in 1..SAMPLE_RATE ) {
      if (samplesCounter <= SAMPLES_SOUNDING) {
        sample[i-1] = generateSoundByte(samplesCounter);
      } else {
        sample[i-1] = 0;
      }
      samplesCounter++;
      if (samplesCounter % samplesForOneTik == 0) {
        samplesCounter = 0;
      }
    }
    return sample;
  }

  public fun play(newBpm: Int) {
    if (newBpm >= MIN_BPM && newBpm <= MAX_BPM) {
      bpm = newBpm
      bps = bpm / 60.0;
      samplesForOneTik = (SAMPLE_RATE / bps).toInt();
      if (isPlaying) {
        player?.flush();
      } else {
        if (player == null) {
          playerInitialize();
        }
        isPlaying = true;
        thread {
          player?.play();
          while (isPlaying) {
            val tone = getTone(samplesForOneTik);
            player?.write(tone, 0, tone.size)
          }

        }
      }

    }
  }

  public fun stop() {
    isPlaying = false;
    player?.stop();
  }
}
