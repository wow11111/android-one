package com.example.parking.btomNaviActivity.ui.dashboard;

public class bookCar_list {

    /**
     * id : 49
     * username : tom
     * carId : 1
     * orderDate : 2020-05-26
     * orderTime : 2020-05-26 15:00:03
     * orderId : e94cRSYOaD8VkJ3
     * consume : 20
     * time_quantum : 06:00-10:00
     * time_number : 0
     * times : 06:00-08:00,08:00-10:00,
     */

    private int id;
    private String username;
    private int carId;
    private String orderDate;
    private String orderTime;
    private String orderId;
    private int consume;
    private String time_quantum;
    private int time_number;
    private String times;

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

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getConsume() {
        return consume;
    }

    public void setConsume(int consume) {
        this.consume = consume;
    }

    public String getTime_quantum() {
        return time_quantum;
    }

    public void setTime_quantum(String time_quantum) {
        this.time_quantum = time_quantum;
    }

    public int getTime_number() {
        return time_number;
    }

    public void setTime_number(int time_number) {
        this.time_number = time_number;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}
