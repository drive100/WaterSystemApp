package com.example.danielr258.watersystem;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
//import android.util.PrintWriter;
import java.io.PrintWriter;
import android.util.Log;
import java.io.InputStream;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.os.Environment;
import android.graphics.*;

import static java.lang.Thread.sleep;


public class MainActivity extends Activity {

    TextView textResponse, plantText, waterText, textResponse2, modeText;
    EditText editTextAddress, editTextPort;
    Button buttonConnect, buttonClear, buttonOn, buttonOff, buttonManual, buttonAuto;
    ProgressBar progressBar, progressBar1, progressBar2, progressBar3, progressBar4, progressBar5;
    ImageView iv;

    //boolean z;
    OnMessageReceived mMessageListener = null;
    BufferedReader mBufferIn;
    int on, off, manual, auto1, connect = 0;
    boolean on1, off1 = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //editTextAddress = (EditText) findViewById(R.id.ipText);
        //editTextPort = (EditText) findViewById(R.id.portText);
        buttonConnect = (Button) findViewById(R.id.buttonConnect);
        buttonClear = (Button) findViewById(R.id.buttonClear);
        buttonOn = (Button) findViewById(R.id.buttonOn);
        buttonOff = (Button) findViewById(R.id.buttonOff);
        buttonAuto = (Button) findViewById(R.id.buttonAuto);
        buttonManual = (Button) findViewById(R.id.buttonManual);

        progressBar = (ProgressBar) findViewById(R.id.Indicator);
        progressBar1 = (ProgressBar) findViewById(R.id.Indicator2);
        progressBar2 = (ProgressBar) findViewById(R.id.Indicator3);
        progressBar3 = (ProgressBar) findViewById(R.id.indicator4);
        progressBar4 = (ProgressBar) findViewById(R.id.indicator5);
        progressBar5 = (ProgressBar) findViewById(R.id.indicator6);

        iv =(ImageView) findViewById(R.id.imageView5);
        iv.setVisibility(View.INVISIBLE);





        textResponse = (TextView) findViewById(R.id.responseText);
        textResponse2 = (TextView) findViewById(R.id.responseText2);
        modeText = (TextView) findViewById(R.id.textMode);

        plantText = (TextView) findViewById(R.id.plantText);
        waterText = (TextView) findViewById(R.id.wateringText);

        buttonConnect.setOnClickListener(buttonConnectOnClickListener);
        buttonOn.setOnClickListener(buttonOnOnClickListener);
        buttonOff.setOnClickListener(buttonOffOnClickListener);
        buttonAuto.setOnClickListener(buttonAutoOnClickListener);
        buttonManual.setOnClickListener(buttonManualOnClickListener);



        buttonClear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                waterText.setText("");
                modeText.setText("");
            }
        });

    }

    OnClickListener buttonConnectOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
           // closed();
            connect++;

            MyClientTask myClientTask = new MyClientTask("10.1.93.45", 8133, "1");
            myClientTask.execute();
        }
    };

    OnClickListener buttonOnOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            off = 0;
            on = 1;

            off1 = false;
            on1 = true;
            iv.setVisibility(View.VISIBLE);

        }
    };

    OnClickListener buttonOffOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            on1 = false;
            off1 = true;

            on  = 0;
            off = 1;

            iv.setVisibility(View.INVISIBLE);


        }
    };

    OnClickListener buttonAutoOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            auto1  = 1;
            manual = 0;
        }
    };

    OnClickListener buttonManualOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            auto1  = 0;
            manual = 1;
        }
    };

    public void appear(boolean x){
        if (x){
            iv.setVisibility(View.VISIBLE);
        }
        else
            iv.setVisibility(View.INVISIBLE);

        //   iv.setVisibility(View.INVISIBLE);
    }


    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String response = "";
        String msgToServer;
        String id;
        int length;
        char c;
        MyClientTask(String addr, int port, String msgTo) {
            dstAddress = addr;
            dstPort = port;
            msgToServer = msgTo;
        }


        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;


            try {
                socket = new Socket("10.1.93.45", 8133);

                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));


                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());


                while (true && connect % 2 != 0) {
                    //sendMessage(dataOutputStream);
                    //if (socket != null) {
                    try {
                        //while(mBufferIn.readLine() != "" || mBufferIn.readLine() != null) {
                        //if (on1 == true) {iv.setVisibility(View.VISIBLE);} else if (off1 == true){iv.setVisibility(View.INVISIBLE);}
                        if (on == 1) {
                            dataOutputStream.writeBytes("11");
                            waterText.setText("");
                            //iv.setVisibility(View.VISIBLE);
                            on = 0;
                            on1 = true;
                            off1 = false;
                            // sleep(1);
                        } else if (off == 1) {
                            dataOutputStream.writeBytes("10");
                            waterText.setText("");
                            // iv.setVisibility(View.INVISIBLE);
                            off = 0;
                            off1 = true;
                            on1 = false;
                            // sleep(1);
                        }
                        if (auto1 == 1) {
                            dataOutputStream.writeBytes("80");
                            waterText.setText("Manual Mode Off");
                            auto1 = 0;
                        }
                        if (manual == 1) {
                            dataOutputStream.writeBytes("90");
                            waterText.setText("Manual Mode On");
                            manual = 0;
                        }


                        response = mBufferIn.readLine();
                        c = response.charAt(0);


                        if (c == '!') {
                            modeText.setText("Manual Mode is On");
                            if (on1 == false && off1 == true) {
                                // appear(false);
                                // on = 2;
                                waterText.setText("Not Watering...");

                            }
                            off1 = true;
                            on1 = false;
                            length = response.length();
                            id = response.substring(1, 2);
                            response = response.substring(2, (length));


                        } else if (c == '*') {

                            modeText.setText("Manual Mode is On");
                            if (on1 == true && off1 == false) {
                                //appear(true);
                                //iv.setVisibility(View.VISIBLE);
                                waterText.setText("Watering...");

                            }
                            on1 = true;
                            off1 = false;
                            length = response.length();
                            id = response.substring(1, 2);
                            response = response.substring(2, (length));

                        } else if (c == '?') {
                            modeText.setText("Manual Mode is Off");
                            if (on1 == true && off1 == false) {
                                //appear(true);
                                waterText.setText("Watering...");

                            }
                            on1 = true;
                            off1 = false;
                            length = response.length();
                            id = response.substring(1, 2);
                            response = response.substring(2, (length));
                        } else if (c == '1' || c == '2') {
                            modeText.setText("Manual Mode is Off");
                            if (off1 == true && on1 == false) {
                                //appear(false);
                                waterText.setText("Stopped Watering...");

                            }
                            // appear(false);
                            off1 = true;
                            on1 = false;
                            length = response.length();
                            id = response.substring(0, 1);

                            response = response.substring(1, (length));
                        }


                        //}else{ textResponse.setBackgroundColor(Color.BLACK);}
                        if (id.equals("1")) {

                            if (Integer.parseInt(response) > 640) {
                                progressBar.setProgress(100);
                                progressBar1.setProgress(0);
                                progressBar2.setProgress(0);
                            } else if (Integer.parseInt(response) > 600 && Integer.parseInt(response) < 640) {
                                progressBar.setProgress(90);
                                progressBar1.setProgress(0);
                                progressBar2.setProgress(0);

                            } else if (Integer.parseInt(response) >= 560 && Integer.parseInt(response) < 600) {
                                progressBar.setProgress(80);
                                progressBar1.setProgress(0);
                                progressBar2.setProgress(0);

                            } else if (Integer.parseInt(response) > 520 && Integer.parseInt(response) < 560) {
                                progressBar1.setProgress(70);
                                progressBar.setProgress(0);
                                progressBar2.setProgress(0);
                            } else if (Integer.parseInt(response) > 450 && Integer.parseInt(response) < 520) {
                                progressBar1.setProgress(60);
                                progressBar.setProgress(0);
                                progressBar2.setProgress(0);
                            } else if (Integer.parseInt(response) > 400 && Integer.parseInt(response) < 450) {
                                progressBar1.setProgress(50);
                                progressBar.setProgress(0);
                                progressBar2.setProgress(0);
                            } else if (Integer.parseInt(response) >= 300 && Integer.parseInt(response) < 400) {
                                progressBar1.setProgress(40);
                                progressBar.setProgress(0);
                                progressBar2.setProgress(0);
                            } else if (Integer.parseInt(response) > 200 && Integer.parseInt(response) < 300) {
                                progressBar2.setProgress(30);
                                progressBar1.setProgress(0);
                                progressBar.setProgress(0);
                            } else if (Integer.parseInt(response) > 100 && Integer.parseInt(response) < 200) {
                                progressBar2.setProgress(20);
                                progressBar1.setProgress(0);
                                progressBar.setProgress(0);
                            } else if (Integer.parseInt(response) > 20 && Integer.parseInt(response) < 100) {
                                progressBar2.setProgress(10);
                                progressBar1.setProgress(0);
                                progressBar.setProgress(0);
                            } else if (Integer.parseInt(response) < 20) {
                                progressBar2.setProgress(0);
                                progressBar1.setProgress(0);
                                progressBar.setProgress(0);
                            }
                            textResponse.setText(response);
                        } else {
                            if (Integer.parseInt(response) > 640) {
                                progressBar3.setProgress(100);
                                progressBar4.setProgress(0);
                                progressBar5.setProgress(0);
                            } else if (Integer.parseInt(response) > 600 && Integer.parseInt(response) < 640) {
                                progressBar3.setProgress(90);
                                progressBar4.setProgress(0);
                                progressBar5.setProgress(0);

                            } else if (Integer.parseInt(response) >= 560 && Integer.parseInt(response) < 600) {
                                progressBar3.setProgress(80);
                                progressBar4.setProgress(0);
                                progressBar5.setProgress(0);

                            } else if (Integer.parseInt(response) > 520 && Integer.parseInt(response) < 560) {
                                progressBar4.setProgress(70);
                                progressBar3.setProgress(0);
                                progressBar5.setProgress(0);
                            } else if (Integer.parseInt(response) > 450 && Integer.parseInt(response) < 520) {
                                progressBar4.setProgress(60);
                                progressBar3.setProgress(0);
                                progressBar5.setProgress(0);
                            } else if (Integer.parseInt(response) > 400 && Integer.parseInt(response) < 450) {
                                progressBar4.setProgress(50);
                                progressBar3.setProgress(0);
                                progressBar5.setProgress(0);
                            } else if (Integer.parseInt(response) >= 300 && Integer.parseInt(response) < 400) {
                                progressBar4.setProgress(40);
                                progressBar3.setProgress(0);
                                progressBar5.setProgress(0);
                            } else if (Integer.parseInt(response) > 200 && Integer.parseInt(response) < 300) {
                                progressBar5.setProgress(30);
                                progressBar4.setProgress(0);
                                progressBar3.setProgress(0);
                            } else if (Integer.parseInt(response) > 100 && Integer.parseInt(response) < 200) {
                                progressBar5.setProgress(20);
                                progressBar4.setProgress(0);
                                progressBar3.setProgress(0);
                            } else if (Integer.parseInt(response) > 20 && Integer.parseInt(response) < 100) {
                                progressBar5.setProgress(10);
                                progressBar4.setProgress(0);
                                progressBar3.setProgress(0);
                            } else if (Integer.parseInt(response) < 20) {
                                progressBar5.setProgress(0);
                                progressBar4.setProgress(0);
                                progressBar3.setProgress(0);
                            }
                            textResponse2.setText(response);
                        }
                        // sleep(1);

//
                    } catch (UnknownHostException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        response = "UnknownHostException: " + e.toString();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        response = "IOException: " + e.toString();
                    }

                }



            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                waterText.setText("Closed Connection");
                modeText.setText("");
                textResponse2.setText("");
                textResponse.setText("");
                progressBar.setProgress(0);
                progressBar1.setProgress(0);
                progressBar2.setProgress(0);
                progressBar3.setProgress(0);
                progressBar4.setProgress(0);
                progressBar5.setProgress(0);

            }
            return null;
        }



        @Override
        protected void onPostExecute(Void result) {
            //textResponse.setText(response);
            super.onPostExecute(result);
        }

    }
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

}
