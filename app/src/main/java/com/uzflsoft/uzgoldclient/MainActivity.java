package com.uzflsoft.uzgoldclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends Activity
{

    final String HOST = "files.000.webhost.com";
    final String USERNAME = "mute";
    final String PASSWORD = "mamaevaxe0303";
    final int PORT = 21;
    final String FILE1 = "currency_course.txt";
    final String FILE2 = "gold_course.txt";


    EditText dollarEdit, goldEdit;
    Button update;

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        new DownloadFromFTP().execute("course.txt");
        //new DownloadFromFTP().execute(FILE2);
        new UploadToFTP().execute("test.txt","25");
        dollarEdit = (EditText) findViewById(R.id.dollar_tashkent);

        dollarEdit.setHint(readTxt(FILE1).trim());

        dollarEdit = (EditText) findViewById(R.id.dollar_tashkent);
        goldEdit = (EditText) findViewById(R.id.gold_tashkent);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:/*
						int dollar = Integer.parseInt(dollarEdit.getText().toString());
						int gold = Integer.parseInt(goldEdit.getText().toString());
						if(dollar > 1000)
							if(gold > 10) {
								new UploadToFTP().execute(FILE1, dollarEdit.getText().toString());
								new UploadToFTP().execute(FILE2, goldEdit.getText().toString());
							} */
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        builder = new AlertDialog.Builder(this);
        builder.setMessage("Вы уверены?").setPositiveButton("Да", dialogClickListener)
                .setNegativeButton("Нет", dialogClickListener);




    }

    private class UploadToFTP extends AsyncTask<String,Void,Void>
    {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            dialog = ProgressDialog.show(MainActivity.this,null,"Отправка данных..");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result)
        {
            dialog.dismiss();

            Toast tt = Toast.makeText(getApplicationContext(),"Отправка успешна!",Toast.LENGTH_SHORT);
            tt.show();

            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                String filename = params[0];
                int number = Integer.parseInt(params[1]);
                ftp_upload(filename, number);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class DownloadFromFTP extends AsyncTask<String,Void,Void>
    {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            dialog = ProgressDialog.show(MainActivity.this,null,"Получение данных..");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result)
        {
            dialog.dismiss();

            dollarEdit = (EditText) findViewById(R.id.dollar_tashkent);
            goldEdit = (EditText) findViewById(R.id.gold_tashkent);
            String s1 = readTxt(FILE1).trim();
            String s2 = readTxt(FILE2).trim();
            dollarEdit.setHint(s1);
            goldEdit.setHint(s2);

            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/uzgoldClient");
                dir.mkdir();
                String filename = params[0];
                ftp_get_save(HOST, PORT, USERNAME, PASSWORD, filename, new File(dir, filename));

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void ftp_upload(String filename, int number) {


        FTPClient con = new FTPClient();

        try
        {
            con.connect(HOST, PORT);
            con.login(USERNAME, PASSWORD);

            con.setFileType(FTP.ASCII_FILE_TYPE);
            con.setFileTransferMode(FTP.ASCII_FILE_TYPE);
            con.enterLocalPassiveMode();
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/uzgoldClient", filename);


            InputStream in = new FileInputStream(file);
            con.storeFile(filename, in);
            in.close();

            con.logout();
            con.disconnect();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Boolean ftp_get_save(String server, int portNumber,
                                String user, String password, String filename, File localFile)
            throws IOException {
        FTPClient ftp = null;

        try {
            ftp = new FTPClient();
            ftp.connect(server, portNumber);
            Log.d("LOG:", "Connected. Reply: " + ftp.getReplyString());

            ftp.login(user, password);
            Log.d("LOG:", "Logged in");
            ftp.setFileType(FTP.ASCII_FILE_TYPE);
            Log.d("LOG:", "Downloading");
            ftp.enterLocalPassiveMode();


            OutputStream outputStream = null;
            boolean success = false;
            try {
                outputStream = new BufferedOutputStream(new FileOutputStream(
                        localFile));
                success = ftp.retrieveFile(filename, outputStream);
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }

            return success;
        } finally {
            if (ftp != null) {
                ftp.logout();
                ftp.disconnect();
            }
        }
    }

    public void onUpdateClick(View view) {
        builder.show();
    }


    public String readTxt(String filename) {

        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/uzgoldClient", filename);

        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            br.close();


        } catch(Exception ex) {

        }

        return sb.toString();
    }
}
