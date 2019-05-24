package com.example.android_project;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    Button connect;
    ProgressBar progressBar;
    EditText editIP;
    String returnMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connect=(Button)findViewById(R.id.connect);
        progressBar= (ProgressBar)findViewById(R.id.process_bar);
        editIP=(EditText)findViewById(R.id.editIP);

        editIP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0){
                    connect.setEnabled(false);
                    connect.setTextColor(Color.parseColor("#71a0b3"));
                    connect.setBackgroundResource(R.drawable.background_button_continue_disable);
                } else {
                    connect.setEnabled(true);
                    connect.setTextColor(Color.parseColor("#FFFFFF"));
                    connect.setBackgroundResource(R.drawable.background_button_done);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                connect.setEnabled(false);
                String ip= editIP.getText().toString();
                PiProperties.IP = ip;
                //get IP of the Pi

                MyClientTask myClientTask = new MyClientTask(
                        PiProperties.IP,
                        PiProperties.portToMessage,
                        "_start");
                myClientTask.execute();

                CountDownTimer countDownTimer = new CountDownTimer(5000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        progressBar.setProgress(progressBar.getProgress()+10);
                    }

                    @Override
                    public void onFinish() {
                        progressBar.setProgress(0);
                        //kiem tra xem co du lieu gui ve
//                        if (returnMessage == null)
                        if (returnMessage != null && returnMessage.equals("_ready")) {
                            Intent intent = new Intent(MainActivity.this, Menu.class);
                            startActivity(intent);
                        } else {
                            connect.setEnabled(true);
                            Toast.makeText(MainActivity.this, "Cannot connect to Pi", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                countDownTimer.start();

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
                socket = new Socket(mInetAddress, dstPort);

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
                returnMessage = response;
            }

        }
    }

}
