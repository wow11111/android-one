package com.example.parking.btomNaviActivity.ui.notifications;

public class rechargeUtil {

    public rechargeUtil(int id, String username, int balances) {
        this.id = id;
        this.username = username;
        this.balances = balances;
    }

    /**
     * id : 32
     * username : 徐磊
     * balances : 200
     */

    private int id;
    private String username;
    private int balances;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getBalances() {
        return balances;
    }

    public void setBalances(int balances) {
        this.balances = balances;
    }
}
