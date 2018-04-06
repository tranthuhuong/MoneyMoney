package com.example.huongthutran.moneymoney;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huongthutran.moneymoney.Adapter.MoneyAdapter;
import com.example.huongthutran.moneymoney.Dialog.DialogOverview;
import com.example.huongthutran.moneymoney.Dialog.Dialog_Filter;
import com.example.huongthutran.moneymoney.Dialog.Dialog_view_info;
import com.example.huongthutran.moneymoney.Model.Money;
import com.example.huongthutran.moneymoney.Network.CallAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView  listViewMoney;
    FloatingActionButton btnAdd;
    TextView tvIncome,tvOutcome,tvMoney;
    List<Money>monies=new ArrayList<>();
    MoneyAdapter moneyAdapter;
    int income;
    int outcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listViewMoney=findViewById(R.id.listViewMoney);
        btnAdd=(FloatingActionButton)findViewById(R.id.btnAdd);
        tvIncome=findViewById(R.id.tvMoneyIncome);
        tvOutcome=findViewById(R.id.tvMoneyOutcome);
        tvMoney=findViewById(R.id.tvMoney);
        new DoGet().execute(CallAPI.urlMoney);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAdd = new Intent(MainActivity.this, AddMoneyActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("METHOD","POST");
                intentAdd.putExtras(mBundle);
                startActivityForResult(intentAdd, 123);
            }
        });
        listViewMoney.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                new Dialog_view_info(MainActivity.this,monies.get(i));
                //o= new Dialog_view_info(MainActivity.this,monies.get(i));

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_filter) {
            new Dialog_Filter(MainActivity.this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    void init(){
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 123 && data != null) {
            Money moneyGetResult;
            moneyGetResult = (Money) data.getSerializableExtra("moneyNew");
            monies.add(moneyGetResult);
            moneyAdapter.notifyDataSetChanged();
            updateIn_Outcome();
            Toast.makeText(MainActivity.this,"Add new Money is successfull!",Toast.LENGTH_SHORT).show();
        }
        if (resultCode == RESULT_OK && requestCode == 204 && data != null) {
            monies.clear();
            new DoGet().execute(CallAPI.urlMoney);
            moneyAdapter.notifyDataSetChanged();
        }
    }

    void loadInOutMoneyDialog(){
        DialogOverview d=new DialogOverview(MainActivity.this,monies);
    }

    class DoGet extends AsyncTask<String, Void, Integer>{
        @Override
        protected void onPostExecute(Integer integer) {
            // super.onPostExecute(integer);
            if(integer==500){
                Log.d("Test", "Thất bại");


            } else{
                Log.d("Test", "Thành công");
                tvMoney.setText(income-outcome+"");
                tvOutcome.setText(outcome+"");
                tvIncome.setText(income+"");
                moneyAdapter=new MoneyAdapter(MainActivity.this,monies);
                listViewMoney.setAdapter(moneyAdapter);
                listViewMoney.deferNotifyDataSetChanged();
            }

        }

        @Override
        protected Integer doInBackground(String... strings) {
            String urlString=strings[0];
            URL url;
            HttpURLConnection httpURLConnection=null;
            InputStream inputStream;
            String result="";
            int c;
            income=0;
            outcome=0;
            try {
                url=new URL(urlString);
                monies.clear();
                httpURLConnection=(HttpURLConnection) url.openConnection();
                Log.d("Test", "Dang connect");
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                inputStream=httpURLConnection.getInputStream();
                while ((c=inputStream.read())!=-1) {
                    //Log.d("Test", c+"");
                    result += (char) c;
                }

                JSONArray jsonArray=new JSONArray(result);
                JSONObject jsonObject;
                for (int i=0;i<jsonArray.length();i++){
                    jsonObject=jsonArray.getJSONObject(i);
                    monies.add(new Money(jsonObject.getInt("id"),
                            jsonObject.getString("type").trim(),
                            jsonObject.getString("contentM").trim(),
                            jsonObject.getString("note").trim(),
                            jsonObject.getString("date"),
                            jsonObject.getInt("amount")
                    ));
                    if(monies.get(i).getType().trim().equals("income")) {
                        income += monies.get(i).getAmount();
                    }
                    else outcome+=monies.get(i).getAmount();

                }

                Toast.makeText(MainActivity.this,"xong",Toast.LENGTH_SHORT).show();
                httpURLConnection.disconnect();
            } catch (Exception e) {
                Log.d("Test", e.toString());

                return 400;
            }
            return 200;
        }
    }
    private void updateIn_Outcome(){
        income=0;
        outcome=0;
        for (Money money:monies) {
            if(money.getType().trim().equals("income")) {
                income += money.getAmount();
            }
            else outcome+=money.getAmount();
        }
        tvMoney.setText(income-outcome+"");
        tvOutcome.setText(outcome+"");
        tvIncome.setText(income+"");

    }

}
