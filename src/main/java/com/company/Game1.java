package com.company;

public class Game1 {
    private int n;
    private int low;
    private int high;
    private int attampts;

    public Game1() {
        this.n = 0;
        this.low = 0;
        this.high = 100;
        this.attampts = 0;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getAttampts() {
        return attampts;
    }

    public void setAttampts(int attampts) {
        this.attampts = attampts;
    }
}
