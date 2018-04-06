package com.example.huongthutran.moneymoney;

import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huongthutran.moneymoney.Adapter.MoneyAdapter;
import com.example.huongthutran.moneymoney.Dialog.Dialog_view_info;
import com.example.huongthutran.moneymoney.Model.Money;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListMoneyFilterActivity extends AppCompatActivity {
    ListView listViewMoneyFilter;
    TextView tvIncome,tvOutcome,tvMoney,tvtitle;
    List<Money> monies=new ArrayList<>();
    MoneyAdapter moneyAdapter;

    int income;
    int outcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_money_filter);
        listViewMoneyFilter=findViewById(R.id.listViewMoneyFilter);
        tvIncome=findViewById(R.id.tvMoneyIncomeFilter);
        tvOutcome=findViewById(R.id.tvMoneyOutcomeFilter);

        tvtitle=findViewById(R.id.tvtitle);
        tvMoney=findViewById(R.id.tvMoneyFilter);
        String URL = getIntent().getExtras().getString("URL");
        String title = getIntent().getExtras().getString("TITLE");
        tvtitle.setText(title);
        new DoGet().execute(URL);
        listViewMoneyFilter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                new Dialog_view_info(ListMoneyFilterActivity.this,monies.get(i));

                return false;
            }
        });
    }
    class DoGet extends AsyncTask<String, Void, Integer> {
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
                if(monies.size()==0){
                    Toast.makeText(ListMoneyFilterActivity.this,"No history income/outcome",Toast.LENGTH_SHORT).show();
                }
                moneyAdapter=new MoneyAdapter(ListMoneyFilterActivity.this,monies);
                listViewMoneyFilter.setAdapter(moneyAdapter);
                listViewMoneyFilter.deferNotifyDataSetChanged();
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
                httpURLConnection=(HttpURLConnection) url.openConnection();
                Log.d("Test", "Dang connect");
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                inputStream=httpURLConnection.getInputStream();
                while ((c=inputStream.read())!=-1) {
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
