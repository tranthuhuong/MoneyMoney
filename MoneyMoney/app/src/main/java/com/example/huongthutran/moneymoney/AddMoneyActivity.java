package com.example.huongthutran.moneymoney;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.huongthutran.moneymoney.Model.Money;
import com.example.huongthutran.moneymoney.Network.CallAPI;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;

public class AddMoneyActivity extends AppCompatActivity {
    EditText edtDate, edtAmount,edtNote;
    Spinner spnContaint;
    RadioButton rbtnIncome, rbtnOutcome;
    Button btnAddMoney,btnCancle;
    TextView tvError;
    Money money=new Money();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        edtDate = findViewById(R.id.edtDate);
        edtNote = findViewById(R.id.edtNote);
        edtAmount = findViewById(R.id.edtAmount);
        spnContaint = findViewById(R.id.spnContaint);
        rbtnIncome = findViewById(R.id.rbtnIncome);
        rbtnOutcome = findViewById(R.id.rbtnOutcome);
        btnAddMoney = findViewById(R.id.addNewbtn);
        btnCancle = findViewById(R.id.Canclebtn);
        tvError = findViewById(R.id.tvError);
        final String contentOutcome[],contentIncome[];

        final String[] cont = {""};
        //
        String method = getIntent().getExtras().getString("METHOD");
        money = (Money) getIntent().getExtras().getSerializable("MONEY");
        contentOutcome = new String[]{"Bills & Utiltites", "Food & Beverage", "Transportation", "Shopping", "Friend & Lover", "Health & Fitness", "Education", "Others"};
        contentIncome = new String[]{"Salary","Others"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddMoneyActivity.this, android.R.layout.simple_spinner_item, contentIncome);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnContaint.setAdapter(adapter);

        spnContaint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cont[0] = spnContaint.getSelectedItem().toString();
                //Log.d("Test Spinnert", cont[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        rbtnIncome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (rbtnIncome.isChecked() == true) {

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddMoneyActivity.this, android.R.layout.simple_spinner_item, contentIncome);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                    spnContaint.setAdapter(adapter);

                } else {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddMoneyActivity.this, android.R.layout.simple_spinner_item, contentOutcome);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                    spnContaint.setAdapter(adapter);
                }

            }
        });
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if(method.equals("POST")){
            btnAddMoney.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (edtAmount.getText().toString().trim().isEmpty() || edtDate.getText().toString().isEmpty()) {
                        tvError.setText("Enter enough Amout/Date");
                        return;
                    }
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        format.parse(edtDate.getText().toString());
                        int id = randomNumber();
                        String type = "", contentM = "", note = "", amount = "";
                        String date = "";
                        contentM = cont[0];
                        if (rbtnIncome.isChecked()) {
                            type = "income"; //type
                        } else {

                            type = "outcome";
                        }

                        note = edtNote.getText().toString(); //note
                        date = edtDate.getText().toString();//date
                        amount = edtAmount.getText().toString();//amout
                        new DoPost().execute(
                                CallAPI.urlMoney,
                                id + "", type,
                                contentM,
                                note,
                                date,
                                amount);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        tvError.setText(R.string.error_format_date);
                    }

                }
            });
        }
        if (method.equals("PUT") ) {
            edtNote.setText(money.getNote());
            edtAmount.setText(money.getAmount() + "");
            edtDate.setText(money.getDate());
            if (money.getContentM().equals("income") ) {
                rbtnIncome.setChecked(true);
                adapter = new ArrayAdapter<String>(AddMoneyActivity.this, android.R.layout.simple_spinner_item, contentIncome);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                spnContaint.setAdapter(adapter);
            } else {
                rbtnOutcome.isChecked();
                adapter = new ArrayAdapter<String>(AddMoneyActivity.this, android.R.layout.simple_spinner_item, contentOutcome);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                spnContaint.setAdapter(adapter);
            }
            btnAddMoney.setText("SAVE");
            btnAddMoney.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        if (edtAmount.getText().toString().trim().isEmpty() || edtDate.getText().toString().isEmpty()) {
                            tvError.setText("Enter enough Amout/Date");
                            return;
                        }
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            format.parse(edtDate.getText().toString());
                            String type = "", contentM = "", note = "", amount = "";
                            String date = "";
                            contentM = cont[0];
                            if (rbtnIncome.isChecked()) {
                                type = "income"; //type
                            } else {
                                type = "outcome";
                            }

                            note = edtNote.getText().toString(); //note
                            date = edtDate.getText().toString();//date
                            amount = edtAmount.getText().toString();
                            ;//date

                            jsonObject.put("id", money.getId() + "");
                            jsonObject.put("contentM", contentM);
                            jsonObject.put("amount", amount);
                            jsonObject.put("note", note);
                            jsonObject.put("type", type);
                            jsonObject.put("date", date);
                            String objMoney = jsonObject.toString();
                            new DoUpdate().execute(CallAPI.urlMoney + "/" + money.getId(), objMoney);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });
        }
    }
        private class DoPost extends AsyncTask<String, Void, Integer> {
            @Override
            protected void onPostExecute(Integer integer) {
                // super.onPostExecute(integer);
                if(integer==500){
                    Log.d("Test", "Fail_Post");
                } else{
                    Log.d("Test", "Connect_success");

                }
            }
        @Override
        protected Integer doInBackground(String... strings) {
            String urlString = strings[0];
            URL url = null;
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;
            OutputStream outStream;
            int c;
            String result = "";
            try {
                url = new URL(urlString);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                JSONObject object = new JSONObject();
                object.put("id", Integer.parseInt(strings[1]));
                object.put("type", strings[2]);
                object.put("contentM", strings[3]);
                object.put("note", strings[4]);
                object.put("date", strings[5]);
                object.put("amount", Integer.parseInt(strings[6]));

                outStream = new BufferedOutputStream(httpURLConnection.getOutputStream());
                outStream.write(object.toString().getBytes(Charset.forName("UTF-8")));
                outStream.flush();
                outStream.close();

                inputStream = httpURLConnection.getInputStream();
                while ((c = inputStream.read()) != -1) {
                    result += (char) c;
                }
                JSONObject jsonObject=new JSONObject(result);
                Money money=new Money(jsonObject.getInt("id"),
                        jsonObject.getString("type").trim(),
                        jsonObject.getString("contentM").trim(),
                        jsonObject.getString("note").trim(),
                        jsonObject.getString("date"),
                        jsonObject.getInt("amount")
                );
                Intent intent = new Intent(AddMoneyActivity.this, MainActivity.class);
                intent.putExtra("moneyNew",(Serializable) money);
                setResult(RESULT_OK, intent);

                finish();
                Log.i("Test", result);
            } catch (Exception e) {
                //that bai
                Log.i("Test", e.toString());
                e.printStackTrace();
                return 400;
            }
            //thanh cong
            return 200;
        }

    }
    private class DoUpdate extends AsyncTask<String,Void,Integer>{
        @Override
        protected Integer doInBackground(String... strings) {
            String urlString = strings[0];
            URL url = null;
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;
            OutputStream outStream;
            int c;
            String result = "";
            try {
                url = new URL(urlString);
                httpURLConnection = (HttpURLConnection) url.openConnection();//truyen vao method

                httpURLConnection.setRequestMethod("PUT");

                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                httpURLConnection.setRequestProperty("Content-Type","application/json");
                httpURLConnection.setRequestProperty("Accept","application/json");

                outStream = new BufferedOutputStream(httpURLConnection.getOutputStream());
                Log.i("OBJECT", strings[1]);
                outStream.write(strings[1].getBytes(Charset.forName("UTF-8")));
                outStream.flush();
                outStream.close();

                inputStream = httpURLConnection.getInputStream();

                while ((c=inputStream.read()) != -1){
                    result+=(char)c;

                }
                return 200;
            } catch (Exception e) {
                e.printStackTrace();
                return 400;
            }

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            final AlertDialog.Builder dialog = new AlertDialog.Builder(AddMoneyActivity.this);
            Log.i("CODES", String.valueOf(integer));
            if(integer == 200){
                dialog.setTitle("Successful!");
                dialog.setMessage("Update successful!");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                        dialogInterface.dismiss();
                        Intent intent = new Intent(AddMoneyActivity.this, MainActivity.class);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                dialog.show();
            }else if(integer ==400){
                dialog.setTitle("ERROR!");
                dialog.setMessage("Update failed!");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.show();
            }
        }
    }
    private int randomNumber() {
        Random random = new Random();
        String number = "1234567890987654321567892345658437346983726423895741";
        int leng = number.length();
        String ran = "";
        for (int i = 0; i < 4; i++) {
            ran += number.charAt(random.nextInt(leng - 1));
        }
        return Integer.parseInt(ran);
    }
    public int kq(){
        return 1;
    }

}