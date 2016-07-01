package com.smsfilter.congybk.smsfilter;

/**
 * Created by congybk on 30/06/2016.
 */
public class Message {
    private String number;
    private String content;
    public Message(){

    }
    public Message(String number,String content){
        this.number = number;
        this.content = content;

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
