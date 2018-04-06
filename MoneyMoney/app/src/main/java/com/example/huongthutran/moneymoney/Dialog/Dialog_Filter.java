package com.example.huongthutran.moneymoney.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.huongthutran.moneymoney.ListMoneyFilterActivity;
import com.example.huongthutran.moneymoney.MainActivity;
import com.example.huongthutran.moneymoney.Model.Money;
import com.example.huongthutran.moneymoney.Network.CallAPI;
import com.example.huongthutran.moneymoney.R;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Huong Thu Tran on 4/5/2018.
 */

public class Dialog_Filter {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Context context;
    EditText edtDay;
    RadioButton rbtnWeekFilter,rbtnMonth,rbtnYear,rbtnSeason,rbtnDay;
    Button btnOk;
    Spinner spinerFilter;
    CheckBox checkboxIncome,checkboxOutcome;
    private static final int quatity = 5;
    final String[] weeks = new String[quatity];
    String[] months=new String[quatity];
    String[] years=new String[quatity];
    String[] seasons=new String[quatity];
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Dialog_Filter(final Context context){
        this.context=context;
        final Dialog dialog=new Dialog(context);
        dialog.setTitle("Filter");
        dialog.setContentView(R.layout.dialogfilter);
        dialog.show();
        btnOk=dialog.findViewById(R.id.btnOkFiler);
        checkboxIncome=dialog.findViewById(R.id.checkboxIncome);
        checkboxOutcome=dialog.findViewById(R.id.checkboxOutcome);
        edtDay=dialog.findViewById(R.id.edtDay);
        rbtnWeekFilter=dialog.findViewById(R.id.rbtnWeekFilter);
        rbtnMonth=dialog.findViewById(R.id.rbtnMonthFilter);
        rbtnYear=dialog.findViewById(R.id.rbtnYearFilter);
        rbtnSeason=dialog.findViewById(R.id.rbtnSeasonFilter);
        rbtnDay=dialog.findViewById(R.id.rbtnDateFilter);
        spinerFilter=dialog.findViewById(R.id.spinnerFilter);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.MONTH+1);
        final int year= calendar.get(Calendar.YEAR);

        setListAll();
        final String[] strFilter = new String[1];

        rbtnDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(rbtnDay.isChecked()){
                    spinerFilter.setVisibility(View.GONE);
                    edtDay.setVisibility(View.VISIBLE);
                }
            }
        });
        rbtnSeason.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(rbtnSeason.isChecked()){
                    spinerVisible();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, seasons);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                    spinerFilter.setAdapter(adapter);
                }
            }
        });
        rbtnMonth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(rbtnMonth.isChecked()){
                    spinerVisible();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, months);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                    spinerFilter.setAdapter(adapter);
                }

            }
        });
        rbtnWeekFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(rbtnWeekFilter.isChecked()){
                    spinerVisible();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, weeks);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                    spinerFilter.setAdapter(adapter);
                }
            }
        });
        rbtnYear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(rbtnYear.isChecked()){
                    spinerVisible();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, years);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                    spinerFilter.setAdapter(adapter);
                }
            }
        });

        spinerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinerVisible();
                strFilter[0] = spinerFilter.getSelectedItem().toString();
                Log.d("Test Spinnert", strFilter[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ListMoneyFilterActivity.class);
                Bundle mBundle = new Bundle();
                String type;
                String Url="";
                if((checkboxIncome.isChecked()&&checkboxOutcome.isChecked())||(!checkboxIncome.isChecked()&&!checkboxOutcome.isChecked())){
                    type="all";
                    Url=CallAPI.urlMoney+"?";
                } else if(checkboxOutcome.isChecked()){
                    type="outcome";
                    Url=CallAPI.urlMoney+"?type=outcome&";
                } else {
                    type="income";
                    Url=CallAPI.urlMoney+"?type=income &";
                }

                if(rbtnDay.isChecked()){
                    String date=edtDay.getText().toString().trim();
                    try {
                        format.parse(date);
                        mBundle.putString("URL",Url+"date="+date);
                        mBundle.putString("TITLE","Date: "+date);
                        mBundle.putString("TYPE",type);
                        i.putExtras(mBundle);
                        context.startActivity(i);
                    } catch (ParseException e) {
                        Toast.makeText(context,R.string.error_format_date,Toast.LENGTH_SHORT).show();
                    }
                }
                if(rbtnWeekFilter.isChecked()){
                    String[] str=strFilter[0].split(" -- ");
                    mBundle.putString("URL", Url+"week="+str[0]);
                    mBundle.putString("TITLE","Week "+str[0]+" -- "+str[1]);
                    mBundle.putString("TYPE",type);
                    i.putExtras(mBundle);
                    context.startActivity(i);
                }
                if(rbtnSeason.isChecked()){
                    String[] str=strFilter[0].split(" - ");
                    mBundle.putString("URL", Url+"season="+str[0]+"&year="+str[1]);
                    mBundle.putString("TITLE","Quarter "+str[0]+" - "+str[1]);
                    mBundle.putString("TYPE",type);
                    i.putExtras(mBundle);
                    context.startActivity(i);
                }
                if(rbtnMonth.isChecked()){
                    String[] str=strFilter[0].split(" - ");
                    mBundle.putString("URL",Url+"month="+str[0]+"&year="+str[1]);
                    mBundle.putString("TITLE",getMonth(str[0])+" - "+str[1]);
                    mBundle.putString("TYPE",type);
                    i.putExtras(mBundle);
                    context.startActivity(i);
                }
                if(rbtnYear.isChecked()){
                    mBundle.putString("URL", Url+"year="+strFilter[0]);
                    mBundle.putString("TITLE",""+strFilter[0]);
                    mBundle.putString("TYPE",type);
                    i.putExtras(mBundle);
                    context.startActivity(i);
                }

            }
        });
    }
    public String getMonth(String month) {
        int intMonth=Integer.parseInt(month);
        return new DateFormatSymbols().getShortMonths()[intMonth-1];
    }
    public void spinerVisible(){
        spinerFilter.setVisibility(View.VISIBLE);
        edtDay.setVisibility(View.GONE);
    }
    public void setListAll() {
        Calendar cal = Calendar.getInstance();
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        cal.set(Calendar.WEEK_OF_YEAR, week);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        String currentDateandTime = format.format(Calendar.getInstance().getTime());
        String dateBegin=format.format(cal.getTime());
        for(int i=0;i<5;i++){
            Date now = null;
            try {
                now = format.parse(dateBegin);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.DAY_OF_YEAR, -7);
            Date newDate = calendar.getTime();
            String date = format.format(newDate);
            weeks[i]= date+" -- "+dateBegin;
            dateBegin=date;
            Log.d("huong",date);
        }
        int month = cal.get(Calendar.MONTH)+1;
        int year=cal.get(Calendar.YEAR);
        int season;
        years  = new String[]{year+"",year-1+""};
        if(month%3==0){
            season=month/3;
        } else season = (month/3)+1;
        for(int i=0;i<quatity;i++){
            months[i]=month+" - "+year;
            seasons[i]=season+" - "+year;
            if(month>1){
                month--;
            } else {
                month=12;
                year--;
            }
        }
        year=cal.get(Calendar.YEAR);
        for(int i=0;i<quatity;i++){
            seasons[i]=season+" - "+year;
            if(season>1){
                season--;
            } else {
                season=4;
                year--;
            }
        }
    }
}
