package com.rnmetronome;

import androidx.annotation.NonNull;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;



public class RNMetronomeModule extends ReactContextBaseJavaModule {
    RNMetronomeModule(ReactApplicationContext context) {
        super(context);
    }

    Thread m_PlayThread = null;
    AudioTrack m_audioTrack = null;

    int sampleRate = 8000;
    double mIfreq = 1000; // tone of sound
    double[] sample = null;
    byte[] generatedSnd = null;
    boolean mStop = true;

    int mBpm = 80; // beats per minute
    double mBps = mBpm / 60.0; // beats per second
    int mSamplesForTik = (int) (sampleRate / mBps); // samples for one beat
    int mSamplesCounter = 0; // samples counter


    synchronized void genTone(int iStep) {
        sample = new double[sampleRate];
        for (int i = 0; i < sampleRate; ++i) {
            if (mSamplesCounter < 120) {
                sample[i] = Math.sin(2 * Math.PI * (mSamplesCounter + iStep * sampleRate) / (sampleRate / mIfreq));
            } else {
                sample[i] = 0;
            }
            mSamplesCounter += 1;
            if (mSamplesCounter % mSamplesForTik == 0) {
                mSamplesCounter = 0;
            }
        }
        generatedSnd = new byte[2 * sampleRate];
        int idx = 0;
        for (final double dVal : sample) {
            final short val = (short) ((dVal * 32767));
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }
    }

    @ReactMethod
    public void play(int newBpm) {
        if (newBpm >= 10 && newBpm <= 1000) {
            if (mStop) {
                mBpm = newBpm;
                mBps = mBpm / 60.0;
                mSamplesForTik = (int) (sampleRate / mBps);
                mSamplesCounter = 0;
                mStop = false;
                m_PlayThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);
                        int iToneStep = 0;
                        m_audioTrack = new AudioTrack(
                                new AudioAttributes.Builder()
                                        .setUsage(AudioAttributes.USAGE_ALARM)
                                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                        .build(),
                                new AudioFormat.Builder()
                                        .setSampleRate(sampleRate)
                                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build(),
                                2 * sampleRate,
                                AudioTrack.MODE_STREAM,
                                AudioManager.AUDIO_SESSION_ID_GENERATE);
                        m_audioTrack.play();
                        while (!mStop) {
                            genTone(iToneStep++);
                            if (m_audioTrack != null) {
                                m_audioTrack.write(generatedSnd, 0, generatedSnd.length);
                            }
                        }
                    }
                });
                m_PlayThread.start();
            } else {
                m_audioTrack.flush();
                mBpm = newBpm;
                mBps = mBpm / 60.0;
                mSamplesForTik = (int) (sampleRate / mBps);
                mSamplesCounter = 0;
            }
        }
    }

    @ReactMethod
    public void stop() {
        mStop = true;
        if (m_audioTrack != null) {
            m_audioTrack.stop();
            m_audioTrack.release();
            m_audioTrack = null;
        }
        if (m_PlayThread != null) {
            m_PlayThread.interrupt();
            m_PlayThread = null;
        }
    }

    @ReactMethod
    public void isPlaying(Callback callBack) {
        callBack.invoke(!mStop);
    }

    @NonNull
    @Override
    public String getName() {
        return "RNMetronome";
    }
}