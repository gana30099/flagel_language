package com.example.foraddingtoserverio;

public class DataCreator extends Thread {
    private Object syntocken;
private MainActivity2 ma;
    public DataCreator(Object syntocken, MainActivity2 mainActivity) {
        this.syntocken = syntocken;
        this.ma = mainActivity;
    }

    public void run() {
        while(true) {
            synchronized (syntocken) {
                try {
                    syntocken.wait();
                    //ma.setExpendable();
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
                ma.setExpendable();
            }
        }
    }
}
