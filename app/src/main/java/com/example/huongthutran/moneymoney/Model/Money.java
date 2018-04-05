package com.example.huongthutran.moneymoney.Model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Huong Thu Tran on 4/4/2018.
 */

public class Money  implements Serializable {
    private int id;
    private String type;
    private String contentM;
    private String note;
    private String date;
    private int amount;

    public Money(){

    }
    public Money(int id, String type, String contentM, String note, String date, int amount) throws ParseException {

        this.id = id;
        this.type = type;
        this.contentM = contentM;
        this.note = note;
        this.date = date;  ;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContentM() {
        return contentM;
    }

    public void setContentM(String contentM) {
        this.contentM = contentM;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
