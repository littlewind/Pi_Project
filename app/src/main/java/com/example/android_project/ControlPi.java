package com.example.android_project;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ControlPi extends AppCompatActivity {

    WebView wvPiCamera;
    Button btnUp;
    Button btnLeft;
    Button btnRight;
    Button btnDown;
    TextView tvPiState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_pi);

        wvPiCamera = findViewById(R.id.showFace);
        btnUp = findViewById(R.id.up);
        btnLeft = findViewById(R.id.left);
        btnRight = findViewById(R.id.right);
        btnDown = findViewById(R.id.down);

        tvPiState = findViewById(R.id.tvPiStatus);

        MyClientTask myClientTask = new MyClientTask(
                PiProperties.IP,
                PiProperties.portToMessage,
                "_remote");
        myClientTask.execute();

        String src = "http://"+PiProperties.IP+":8081";
        playStream(src);



        btnUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    MyClientTask myClientTask = new MyClientTask(
                            PiProperties.IP,
                            PiProperties.portToMessage,
                            "Up On");
                    myClientTask.execute();
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    MyClientTask myClientTask = new MyClientTask(
                            PiProperties.IP,
                            PiProperties.portToMessage,
                            "Up Off");
                    myClientTask.execute();
                }
                return false;
            }
        });

        btnDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    MyClientTask myClientTask = new MyClientTask(
                            PiProperties.IP,
                            PiProperties.portToMessage,
                            "Down On");
                    myClientTask.execute();
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    MyClientTask myClientTask = new MyClientTask(
                            PiProperties.IP,
                            PiProperties.portToMessage,
                            "Down Off");
                    myClientTask.execute();
                }
                return false;
            }
        });

        btnLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    MyClientTask myClientTask = new MyClientTask(
                            PiProperties.IP,
                            PiProperties.portToMessage,
                            "Left On");
                    myClientTask.execute();
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    MyClientTask myClientTask = new MyClientTask(
                            PiProperties.IP,
                            PiProperties.portToMessage,
                            "Left Off");
                    myClientTask.execute();
                }
                return false;
            }
        });

        btnRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    MyClientTask myClientTask = new MyClientTask(
                            PiProperties.IP,
                            PiProperties.portToMessage,
                            "Right On");
                    myClientTask.execute();
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    MyClientTask myClientTask = new MyClientTask(
                            PiProperties.IP,
                            PiProperties.portToMessage,
                            "Right Off");
                    myClientTask.execute();
                }
                return false;
            }
        });
    }

    private class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        InetAddress mInetAddress;
        String messageToSend;
        String response;

        MyClientTask(String address, int port, String message) {
            dstAddress = address;
            dstPort = port;
            messageToSend = message;
            try {
                mInetAddress = InetAddress.getByName(dstAddress);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Socket socket = null;
            OutputStreamWriter mOutputStreamWriter = null ;
            Writer mWrite = null;

            try {
//                Socket socket = new Socket(dstAddress, dstPort);
                socket = new Socket(mInetAddress, dstPort);
                Log.d("MainActivity", mInetAddress.toString());
                Log.d("MainActivity", String.valueOf(dstPort));
                Log.d("MainActivity", socket.toString());


//                /*
//                InputStream inputStream = socket.getInputStream();
//                ByteArrayOutputStream byteArrayOutputStream =
//                        new ByteArrayOutputStream(1024);
//                byte[] buffer = new byte[1024];
//
//                int bytesRead;
//                while ((bytesRead = inputStream.read(buffer)) != -1){
//                    byteArrayOutputStream.write(buffer, 0, bytesRead);
//                }
//
//                socket.close();
//
//                response = byteArrayOutputStream.toString("UTF-8");
//                */
//
//
//                /*
//                //write to socket using ObjectOutputStream
//                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//                oos.writeObject(messageToSend);
//                //read the server response message
//                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
//                try {
//                    response = (String) ois.readObject();
//
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//                //close resources
//                ois.close();
//                oos.close();
//                */

//                mOutputStreamWriter = new OutputStreamWriter(socket.getOutputStream(),"UTF8");
//                mWrite = new BufferedWriter(mOutputStreamWriter);
//                mWrite.append(messageToSend);
//                mWrite.flush();

                DataOutputStream dout=new DataOutputStream(socket.getOutputStream());
                dout.writeUTF(messageToSend);
                dout.flush();


            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    DataInputStream din = new DataInputStream(socket.getInputStream());
                    String str = din.readUTF();//in.readLine();
                    if (str != null) {
                        response = str;
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (mWrite != null) {
                    try {
                        mWrite.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mOutputStreamWriter != null) {
                    try {
                        mOutputStreamWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (response != null) {
                tvPiState.setText(response);
            }

        }
    }

    private void playStream(String src) {
        Uri UriSrc = Uri.parse(src);
        if(UriSrc == null){
            Toast.makeText(ControlPi.this, "UriSrc == null", Toast.LENGTH_LONG).show();
        }else{
            wvPiCamera.loadUrl(src);
//            mMediaController = new MediaController(this);
//            vvPiCamera.setMediaController(mMediaController);
//            vvPiCamera.start();

            Toast.makeText(ControlPi.this, "Connect to: " + src, Toast.LENGTH_SHORT).show();
        }
    }

    public void destroyWebView(WebView mWebView) {

        // Make sure you remove the WebView from its parent view before doing anything.
//        mWebContainer.removeAllViews();

        mWebView.clearHistory();

        // NOTE: clears RAM cache, if you pass true, it will also clear the disk cache.
        // Probably not a great idea to pass true if you have other WebViews still alive.
        mWebView.clearCache(true);

        // Loading a blank page is optional, but will ensure that the WebView isn't doing anything when you destroy it.
        mWebView.loadUrl("about:blank");

        mWebView.onPause();
        mWebView.removeAllViews();
        mWebView.destroyDrawingCache();

        // NOTE: This pauses JavaScript execution for ALL WebViews,
        // do not use if you have other WebViews still alive.
        // If you create another WebView after calling this,
        // make sure to call mWebView.resumeTimers().
        mWebView.pauseTimers();

        // NOTE: This can occasionally cause a segfault below API 17 (4.2)
        mWebView.destroy();

        // Null out the reference so that you don't end up re-using it.
        mWebView = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyClientTask myClientTask = new MyClientTask(
                PiProperties.IP,
                PiProperties.portToMessage,
                "_remote Off");
        myClientTask.execute();
    }
}
