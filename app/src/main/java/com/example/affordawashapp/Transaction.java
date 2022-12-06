package com.example.affordawashapp;

import java.util.ArrayList;

public class Transaction {
    String name;
    int employee_id;
    ArrayList<PairItem> listItems;
    boolean isWashing;
    boolean isDrying;
    String machineIdentifier;

    public Transaction(String name, int employeeId){
        this.name = name;
        this.employee_id = employeeId;
        this.listItems = new ArrayList<>();
    }

    public void addListItems(String itemname, int itemQuantity){
        listItems.add(new PairItem(itemname, itemQuantity));
    }

    public class PairItem{
        private String itemname;
        private int itemQuantity;
        private double total;
        public PairItem(String itemname, int itemQuantity){
            this.itemname = itemname;
            this.itemQuantity = itemQuantity;
        }

        public String getItemname() {
            return itemname;
        }

        public int getItemQuantity() {
            return itemQuantity;
        }

        public double getTotal() {
            return total;
        }
    }

}
