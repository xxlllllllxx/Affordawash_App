package com.example.affordawashapp;

import java.util.ArrayList;

public class Transaction {
    String name;
    int employee_id;
    ArrayList<ItemInfo> listItems;
    boolean isWashing;
    boolean isDrying;
    double wash;
    double dry;
    String dateTime;
    String machineIdentifier;

    public Transaction(String name, int employeeId, String dateTime){
        this.name = name;
        this.employee_id = employeeId;
        this.listItems = new ArrayList<>();
        this.dateTime = dateTime;
    }

    public void addListItems(String itemname, int itemQuantity, double price){
        listItems.add(new ItemInfo(itemname, itemQuantity, price));
    }

    public class ItemInfo {
        private String itemname;
        private int itemQuantity;
        private double price;
        public ItemInfo(String itemname, int itemQuantity, double price){
            this.itemname = itemname;
            this.itemQuantity = itemQuantity;
            this.price = price;
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
    }

}
