package com.example.huongthutran.moneymoney.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huongthutran.moneymoney.Model.Money;
import com.example.huongthutran.moneymoney.R;

import java.util.List;

/**
 * Created by Huong Thu Tran on 4/4/2018.
 */

public class DialogOverview {
    Context context;
    int income=0,outcome=0;
    public DialogOverview(Context context, List<Money> monies){
        this.context=context;
        final Dialog dialog=new Dialog(context);
        dialog.setTitle("Overview");
        dialog.setContentView(R.layout.overview);
        dialog.show();
        final TextView tvMoney=dialog.findViewById(R.id.tvMoney);
        final TextView tvMoneyIncome=dialog.findViewById(R.id.tvMoneyIncome);
        final TextView tvMoneyOutcome=dialog.findViewById(R.id.tvMoneyOutcome);
        final Button btnClose=dialog.findViewById(R.id.close);
        for(int i=0;i<monies.size();i++){
            if(monies.get(i).getType().trim().equals("income")) {
                income += monies.get(i).getAmount();
            }
                else outcome+=monies.get(i).getAmount();
        }

       tvMoneyIncome.setText(income+"");
        tvMoneyOutcome.setText(outcome+"");
        tvMoney.setText(income-outcome+"");
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }


}
