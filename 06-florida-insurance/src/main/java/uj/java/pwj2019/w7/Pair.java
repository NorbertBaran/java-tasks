package uj.java.pwj2019.w7;

import java.math.BigDecimal;

public class Pair {
    String first;
    BigDecimal second;
    public Pair(String first, BigDecimal second){
        this.first=first;
        this.second=second;
    }
    public String getFirst(){
        return first;
    }
    public BigDecimal getSecond(){
        return second;
    }
}
