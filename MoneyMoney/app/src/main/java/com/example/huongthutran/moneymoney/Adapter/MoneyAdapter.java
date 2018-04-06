package com.example.huongthutran.moneymoney.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huongthutran.moneymoney.Model.Money;
import com.example.huongthutran.moneymoney.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Huong Thu Tran on 4/4/2018.
 */

public class MoneyAdapter  extends BaseAdapter implements Filterable {
    private Context context;
    private List<Money> monies =new ArrayList<>();

    public MoneyAdapter(Context context, List<Money> l){
        this.context=context;
        monies=l;
    }
    @Override
    public int getCount() {
        return monies.size();
    }

    @Override
    public Object getItem(int i) {
        return monies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(context, R.layout.item_list_money, null);
        }

        Money money = monies.get(i);
        ImageView imageView = view.findViewById(R.id.imgMoney);
        TextView tvDate_Money = view.findViewById(R.id.tvDate_Money);
        TextView tvAmount_Money = view.findViewById(R.id.tvAmount_Money);
        TextView tvNote_Money = view.findViewById(R.id.tvNote_Money);
        String date = money.getDate().substring(0, 10);
        tvDate_Money.setText("Date:" + date);
//        tvType_Money.setText("Type:"+money.getType());
//        tvContent_Money.setText("Content:"+money.getContentM());
        tvAmount_Money.setText("" + money.getAmount());
        tvNote_Money.setText("note:" + money.getNote());

        switch (money.getType().trim()) {
            case "income":
                tvAmount_Money.setTextColor(Color.rgb(0, 0, 0));
                break;
            case "outcome":
                tvAmount_Money.setTextColor(Color.rgb(201, 28, 28));
                break;
            default:tvAmount_Money.setTextColor(Color.rgb(201, 28, 28));
                break;
        }

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
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
