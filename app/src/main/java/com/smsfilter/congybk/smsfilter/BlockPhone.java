package com.smsfilter.congybk.smsfilter;


import java.io.Serializable;

/**
 * Created by congybk on 22/06/2016.
 */
public class BlockPhone implements Serializable {
    private int id;
    private String name;
    private String reason;
    private String number;
    public BlockPhone(){

    }

    public BlockPhone(String name,String number,String reason){
        this.name = name;
        this.reason = reason;
        this.number = number;

    }
    public BlockPhone(int id,String name,String number,String reason){
        this.name = name;
        this.reason = reason;
        this.number = number;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
