package com.example.androids7;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import Moka7.S7;
import Moka7.S7Client;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    S7Client client = new S7Client();

    public void readAll(View view) {
        new PLCRead().execute("");
    }

    public class PLCRead extends AsyncTask<String, Void, String> {
        String data1 = "null";
        String data2 = "null";

        @Override
        protected String doInBackground(String... strings) {
            try {
                client.SetConnectionType(S7.S7_BASIC);
                int check = client.ConnectTo("172.17.80.97", 0, 1);

                if (check == 0) {
                    byte[] data = new byte[256];
                    client.ReadArea(S7.S7AreaDB, 1, 0, 4, data);
                    data1 = "" + S7.GetWordAt(data, 0);
                    data2 = "" + S7.GetWordAt(data, 2);
                } else {

                }
                client.Disconnect();
            } catch (Exception e) {
                Thread.interrupted();
            }
            return "execute";
        }

        @Override
        protected void onPostExecute(String s) {
            TextView textOut1 = (TextView) findViewById(R.id.textView8);
            textOut1.setText(data1);
            TextView textOut2 = (TextView) findViewById(R.id.textView9);
            textOut2.setText(data2);
        }
    }

     public void writeAll(View view) {
        new PLCWrite().execute("");
     }

     public class PLCWrite extends AsyncTask<String, Void, String> {
         @Override
         protected String doInBackground(String... strings) {
             try {
                 client.SetConnectionType(S7.S7_BASIC);
                 int check = client.ConnectTo("172.17.80.97", 0, 1);

                 if (check == 0) {
                     EditText writeData1 = findViewById(R.id.editText2);
                     String data1 = writeData1.getText().toString();

                     EditText writeData2 = findViewById(R.id.editText3);
                     String data2 = writeData2.getText().toString();

                     byte[] dat = new byte[256];

                     S7.SetWordAt(dat, 0, Integer.parseInt(data1));
                     S7.SetWordAt(dat, 2, Integer.parseInt(data2));

                     client.WriteArea(S7.S7AreaDB, 1, 0, 4, dat);
                 } else {}

                 client.Disconnect();
             } catch (Exception e) {
                 Thread.interrupted();
             }
             return "execute";
         }
     }
}
