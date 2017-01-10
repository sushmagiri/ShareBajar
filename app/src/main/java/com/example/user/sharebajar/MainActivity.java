package com.example.user.sharebajar;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //TextView tv_company, tv_fname, tv_lname, tv_applicatipon_no, tv_app_share
    EditText et_fname, et_lname, et_application_no, et_app_share;
    String[] items = new String[]{"Select Company","abc", "efg", "hij"};
    String url = "http://192.168.1.8/sharebajar/list.php?";
    Button btn_search;
    TextView textView;
   TextView cname,fullname,lastname,share_kitta;
    public String s_company;
    TableLayout tb,tb1;
    TextView hcname,hfname,hlname,hshare_kitta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=(TextView)findViewById(R.id.textView);
        et_fname = (EditText) findViewById(R.id.editText);
        et_lname = (EditText) findViewById(R.id.et_lname);
        et_application_no = (EditText) findViewById(R.id.et_applicationno_);
        et_app_share = (EditText) findViewById(R.id.editText4);
        btn_search = (Button) findViewById(R.id.bt_search);
        tb=(TableLayout) findViewById(R.id.table_layout);
        tb1=(TableLayout)findViewById(R.id.table_layout2);
        tb.setVisibility(View.INVISIBLE);



        final Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        final List<String> arrayList = new ArrayList<>(Arrays.asList(items));
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, arrayList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }

        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedItemText = (String) adapterView.getItemAtPosition(position);

// If user change the default selection
// First item is disable and it is used for hint
                if (position > 0) {
// Notify the selected item text
// Toast.makeText
// (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
// .show();
                   s_company=selectedItemText;

                }


            }


            @Override
            public void onNothingSelected (AdapterView < ? > adapterView){

            }
        });


        btn_search.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        String fname=et_fname.getText().toString();
        String lname=et_lname.getText().toString();
        String s_kitta=et_app_share.getText().toString();
//        view.startAnimation(animScale);
         tb.removeAllViews();
           tb1.removeAllViews();
        fetch(fname,lname,s_kitta,s_company);

    }
});
//      btn_search.setOnTouchListener(new View.OnTouchListener() {
//          @Override
//          public boolean onTouch(View view, MotionEvent motionEvent) {
//
//              if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                  btn_search.setBackgroundColor(Color.GRAY);
//              }
//              return false;
//          }
//      } );

    }


public void fetch(final String fname, final String lname, String s_kitta, String s_company){

    VolleySingleton.getInstance(getApplicationContext())
            .addToRequestQueue(new StringRequest(url+"company_name="+s_company+"&firstname="+fname+"&lastname="+lname+"&share_kitta="+s_kitta, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                  layout();
                    try {
                        JSONArray responseArray = new JSONArray(response);
                        JSONObject jsonObject=new JSONObject();

                        Log.d("json array length", String.valueOf(responseArray.length()));


                        for (int i = 0; i < responseArray.length(); i++) {
                             jsonObject = responseArray.getJSONObject(i);

                            LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View tablerow=inflater.inflate(R.layout.table,null,false);
                            cname=(TextView)tablerow.findViewById(R.id.l_cname);
                            fullname=(TextView)tablerow.findViewById(R.id.l_fname);
                            lastname=(TextView)tablerow.findViewById(R.id.caste);
                            share_kitta=(TextView)tablerow.findViewById(R.id.l_share_kitta);
                            cname.setText(jsonObject.getString("company_name"));
                            fullname.setText(jsonObject.getString("firstname"));
                            lastname.setText(jsonObject.getString("lastname"));
                            share_kitta.setText(jsonObject.getString("share_kitta"));
                            tb1.addView(tablerow);



                        }

                    } catch (JSONException e) {
                        tb.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }));
}
    void layout(){
        tb.setVisibility(View.VISIBLE);
        LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v=inflater.inflate(R.layout.table_header,null,false);
        hcname=(TextView)v.findViewById(R.id.hcom);
        hfname=(TextView)v.findViewById(R.id.h_fname);
        hlname=(TextView)v.findViewById(R.id.h_lastname);
        hshare_kitta=(TextView)v.findViewById(R.id.h_share_kitta);
        tb.addView(v);

    }


}

