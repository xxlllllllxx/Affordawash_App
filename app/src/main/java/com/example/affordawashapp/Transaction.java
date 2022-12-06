package com.example.affordawashapp;

import java.util.ArrayList;

public class Transaction {
    String name;
    int employee_id;
    ArrayList<ItemInfo> listItems;
    int machineId;
    boolean isWashing;
    boolean isDrying;
    double wash;
    double dry;
    double payment;
    String dateTime;

    public Transaction(String name, int employeeId, String dateTime){
        this.name = name;
        this.employee_id = employeeId;
        this.listItems = new ArrayList<>();
        this.dateTime = dateTime;
    }

    public void addListItems(String itemname, int itemQuantity, double price, int id){
        listItems.add(new ItemInfo(itemname, itemQuantity, price, id));
    }

    public class ItemInfo {
        private String itemname;
        private int itemQuantity;
        private double price;
        private int itemId;
        public ItemInfo(String itemname, int itemQuantity, double price, int id){
            this.itemname = itemname;
            this.itemQuantity = itemQuantity;
            this.price = price;
            this.itemId = id;
        }

        public String getItemname() {
            return itemname;
        }

        public int getItemQuantity() {
            return itemQuantity;
        }

        public double getPrice() {
            return price;
        }

        public int getItemId() {
            return itemId;
        }
    }

}
