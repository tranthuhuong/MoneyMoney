package com.example.huongthutran.moneymoney.Dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huongthutran.moneymoney.AddMoneyActivity;
import com.example.huongthutran.moneymoney.MainActivity;
import com.example.huongthutran.moneymoney.Model.Money;
import com.example.huongthutran.moneymoney.Network.CallAPI;
import com.example.huongthutran.moneymoney.R;

import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Huong Thu Tran on 4/5/2018.
 */

public class Dialog_view_info {
    Context context;
    Money money;
    TextView tvContentInfo,tvAmountInfo,tvNoteInfo,tvDateInfo;
    Button btnEdit,btnDelete;
    ImageView imageView;
    @SuppressLint("ResourceAsColor")
    public Dialog_view_info(final Context context, final Money money){
        this.context=context;
        this.money=money;
        final Dialog dialog=new Dialog(context);
        dialog.setTitle("Info Itemp");
        dialog.setContentView(R.layout.dialog_view_info);
        dialog.show();
        //------init--------
        tvAmountInfo=dialog.findViewById(R.id.tvAmountInfo);
        tvContentInfo=dialog.findViewById(R.id.tvContentInfo);
        tvNoteInfo=dialog.findViewById(R.id.tvNoteInfo);
        tvDateInfo=dialog.findViewById(R.id.tvDateInfo);
        btnEdit=dialog.findViewById(R.id.btnEdit);
        btnDelete=dialog.findViewById(R.id.btnDelete);
        imageView=dialog.findViewById(R.id.imgContentInfo);
        //------setView--------
        if(money.getType()=="income"){
            tvAmountInfo.setTextColor(R.color.black);
        }
        setValueToView();
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogMess = new AlertDialog.Builder(context);
                dialogMess.setTitle("Messenger ");
                dialogMess.setMessage("Are you want Delete Item?");

                dialogMess.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        new doDelete().execute(CallAPI.urlMoney+"/"+money.getId());
                    }
                });
                dialogMess.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialogMess.show();

            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAdd = new Intent(context, AddMoneyActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("METHOD","PUT");
                intentAdd.putExtra("MONEY",(Serializable) money);
                intentAdd.putExtras(mBundle);
                ((Activity)context).startActivityForResult(intentAdd, 204);
                dialog.dismiss();
            }
        });
    }
    void setValueToView(){
        switch (money.getContentM().trim()) {
            case "Bills & Utiltites":
                imageView.setImageResource(R.drawable.bill_icon);
                break;
            case "Food & Beverage":
                imageView.setImageResource(R.drawable.food_beverage_icon);
                break;
            case "Transportation":
                imageView.setImageResource(R.drawable.transportation_icon);
                break;
            case "Shopping":
                imageView.setImageResource(R.drawable.shopping_icon);
                break;
            case "Friend & Lover":
                imageView.setImageResource(R.drawable.heart_icon);
                break;
            case "Health & Fitness":
                imageView.setImageResource(R.drawable.health_icon);
                break;
            case "Education":
                imageView.setImageResource(R.drawable.edu_icon);
                break;
            case "Salary":
                imageView.setImageResource(R.drawable.income_money);
                break;
            case "Orther":
                imageView.setImageResource(R.drawable.other_icon);
                break;
        }
        tvAmountInfo.setText(money.getAmount()+"");
        tvContentInfo.setText(money.getContentM());
        tvNoteInfo.setText(money.getNote());
        tvDateInfo.setText(money.getDate());
    }
    private class doDelete extends AsyncTask<String,Void,Integer> {
        @Override
        protected Integer doInBackground(String... strings) {
            String urlString = strings[0];
            URL url = null;
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;
            int c;
            String result = "";
            try {
                url = new URL(urlString);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("DELETE");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                inputStream = httpURLConnection.getInputStream();

                while ((c = inputStream.read()) != -1) {
                    result += (char) c;
                }
                return 200;
            } catch (Exception e) {
                e.printStackTrace();
                return 400;
            }

        }

        @Override
        protected void onPostExecute(Integer integer) {
            final AlertDialog.Builder dialogMess = new AlertDialog.Builder(context);
            if(integer == 200){
                dialogMess.setTitle("Successful :3 ");
                dialogMess.setMessage("Delete action success !!! ");

                dialogMess.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent intent=new Intent(context,MainActivity.class);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }
                });
                dialogMess.show();
            }else if(integer ==400){
                dialogMess.setTitle("Fail!");
                dialogMess.setMessage("Delect action Fail");
                dialogMess.setPositiveButton(":(", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialogMess.show();
            }
        }

    }


}
