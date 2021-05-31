package org.example;

public class ASubject {
    private int id, index_number, rate;
    private String subject;

    public ASubject(int id, int index_number, String subject, int rate) {
        this.id = id;
        this.index_number = index_number;
        this.rate = rate;
        this.subject = subject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndex_number() {
        return index_number;
    }

    public void setIndex_number(int index_number) {
        this.index_number = index_number;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
