package com.uzflsoft.uzgoldclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity
{


    String SERVER_URL = "https://mute.000webhostapp.com/update_bd.php";
    EditText dollarEdit, goldEdit;
    Button update;

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        dollarEdit = (EditText) findViewById(R.id.dollar_tashkent);
        goldEdit = (EditText) findViewById(R.id.gold_tashkent);
        update = (Button) findViewById(R.id.sendbutton);
        builder = new AlertDialog.Builder(MainActivity.this);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String gold_tashkent, dollar_tashkent;
                gold_tashkent = dollarEdit.getText().toString();
                dollar_tashkent = goldEdit.getText().toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                builder.setTitle("Server Response");
                                builder.setMessage("Respionse" + response);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dollarEdit.setText("");
                                        goldEdit.setText("");
                                    }
                                });
                                AlertDialog alertdialog = builder.create();
                                alertdialog.show();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Error!",Toast.LENGTH_SHORT);
                        error.printStackTrace();
                    }

                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("gold_tashkent_course",gold_tashkent);
                        params.put("dollar_tashkent_course",dollar_tashkent);
                        return params;
                    }
                };

                MySingleton.getmInstance(MainActivity.this).addTorequestque(stringRequest);
            }
        });


    }





}
