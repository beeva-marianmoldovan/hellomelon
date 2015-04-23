package com.beeva.hellomelon;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.axio.melonplatformkit.AnalysisResult;
import com.axio.melonplatformkit.DeviceHandle;
import com.axio.melonplatformkit.DeviceManager;
import com.axio.melonplatformkit.IDeviceManagerListener;
import com.axio.melonplatformkit.ISignalAnalyzerListener;
import com.axio.melonplatformkit.SignalAnalyzer;

import java.util.Arrays;


public class MainActivity extends ActionBarActivity {

    private final static String TAG = "XMelon";
    private DeviceHandle deviceHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DeviceManager manager = DeviceManager.getManager();
        manager.addListener(new IDeviceManagerListener() {
            @Override
            public void onDeviceScanStopped() {
                Log.d(TAG, "onDeviceScanStopped");
            }

            @Override
            public void onDeviceScanStarted() {
                Log.d(TAG, "onDeviceScanStarted");

            }

            @Override
            public void onDeviceFound(DeviceHandle deviceHandle) {
                Log.d(TAG, "onDeviceFound");
                deviceHandle.connect();

            }

            @Override
            public void onDeviceReady(DeviceHandle deviceHandle) {
                Log.d(TAG, "onDeviceReady");
                deviceHandler = deviceHandle;
                SignalAnalyzer analyzer = new SignalAnalyzer();
                analyzer.addListener(new ISignalAnalyzerListener() {
                    @Override
                    public void onAnalyzedSamples(SignalAnalyzer signalAnalyzer, AnalysisResult analysisResult, AnalysisResult analysisResult1) {
//                        Log.e(TAG, "RAW - " + Arrays.toString(analysisResult.getRawSignal()));
//                        Log.e(TAG, "Filtered - " + Arrays.toString(analysisResult.getFilteredSignal()));
                        Log.e(TAG, "Focus - " + analysisResult.getFocusScore());
//                        Log.e(TAG, "FocusX - " + analysisResult1.getFocusScore());
//                        Log.e(TAG, "Channel - " + analysisResult.getChannel());
//                        Log.e(TAG, "ChannelX - " + analysisResult1.getChannel());
//                        Log.e(TAG, "Analysis rate - " + analysisResult.getAnalysisRate());
//                        Log.e(TAG, "Sampling rate - " + analysisResult.getSamplingRate());
                    }
                });
                deviceHandle.addAnalyzer(analyzer);
                deviceHandle.startStreaming();
            }

            @Override
            public void onDeviceDisconnected(DeviceHandle deviceHandle) {
                Log.d(TAG, "onDeviceDisconnected");
            }
        });
        manager.startScan();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(deviceHandler != null){
            deviceHandler.stopStreaming();
            deviceHandler.disconnect();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
