package org.unification.adklab;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

    private Transmitter mTransmitter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                start();
            }

        });

        Button stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                stop();
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    void start() {
        mTransmitter = new Transmitter();
        new Thread(mTransmitter).start();
    }

    void stop() {
        mTransmitter.halt();
        mTransmitter = null;
    }

    class Transmitter implements Runnable {
        private boolean mRunning = true;

        public void run() {
            UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
            UsbAccessory[] accessoryList = manager.getAccessoryList();
            UsbAccessory accessory = accessoryList[0];
            ParcelFileDescriptor fd = manager.openAccessory(accessory);
            if (fd != null) {
                InputStream inputStream = new ParcelFileDescriptor.AutoCloseInputStream(
                        fd);
                OutputStream outputStream = new ParcelFileDescriptor.AutoCloseOutputStream(
                        fd);
                byte counter = 0;
                while (mRunning) {
                    byte[] buffer = { counter };
                    try {
                        outputStream.write(buffer);
                    }
                    catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    counter++;
                }
                try {
                    inputStream.close();
                    outputStream.close();
                }
                catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }

        synchronized public void halt() {
            mRunning = false;
        }

    }
}
