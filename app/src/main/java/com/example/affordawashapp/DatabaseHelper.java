package com.example.affordawashapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "db_affordawash";
    public static final String TBLMANAGER = "tbl_manager";
    public static final String TBLEMPLOYEE = "tbl_employee";
    public static final String TBLITEM = "tbl_item";
    public static final String TBLMACHINE = "tbl_machine";
    public static final String TBLCUSTOMER = "tbl_customer";
    
    public static final String[] managerFields = {"id", "manager_username", "manager_password"};
    public static final String[] employeeFields = {"id", "employee_username", "employee_password", "employee_salary"};
    public static final String[] itemFields = {"id", "item_name", "item_quantity", "item_init_price", "item_lowest_price", "item_selling_price"};
    public static final String[] machineFields = {"id", "is_available", "washing", "drying", "washing_price", "drying_price"};
    public static final String[] customerFields = {"id", "customer_alias", "employee_id", "machine_id_list", "item_id_list", "transaction_payment", "transaction_datetime"};
    
    
    public DatabaseHelper(Context context){
        super(context, DBNAME, null, 1);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TBLMANAGER + "("+managerFields[0]+" INTEGER PRIMARY KEY AUTOINCREMENT, "+managerFields[1]+" TEXT, "+managerFields[2]+" TEXT)");
        db.execSQL("CREATE TABLE " + TBLEMPLOYEE + "("+employeeFields[0]+" INTEGER PRIMARY KEY AUTOINCREMENT, "+employeeFields[1]+" TEXT, "+employeeFields[2]+" TEXT, "+employeeFields[3]+" DOUBLE)");
        db.execSQL("CREATE TABLE " + TBLITEM + "("+itemFields[0]+" INTEGER PRIMARY KEY AUTOINCREMENT, "+itemFields[1]+" TEXT, "+itemFields[2]+" INTEGER, "+itemFields[3]+" DOUBLE, "+itemFields[4]+" DOUBLE, "+itemFields[5]+" DOUBLE)");
        db.execSQL("CREATE TABLE " + TBLMACHINE + "("+machineFields[0]+" INTEGER PRIMARY KEY AUTOINCREMENT, "+machineFields[1]+" BOOLEAN, "+machineFields[2]+" BOOLEAN, "+machineFields[3]+" BOOLEAN, "+machineFields[4]+" DOUBLE, "+machineFields[5]+" DOUBLE)");
        db.execSQL("CREATE TABLE " + TBLCUSTOMER + "("+customerFields[0]+" INTEGER PRIMARY KEY AUTOINCREMENT, "+customerFields[1]+" TEXT, "+customerFields[2]+" INTEGER, "+customerFields[3]+" TEXT, "+customerFields[4]+" TEXT, "+customerFields[5]+" DOUBLE, "+customerFields[6]+" TEXT)");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TBLMANAGER);
        db.execSQL("DROP TABLE IF EXISTS "+ TBLEMPLOYEE);
        db.execSQL("DROP TABLE IF EXISTS "+ TBLITEM);
        db.execSQL("DROP TABLE IF EXISTS "+ TBLMACHINE);
        db.execSQL("DROP TABLE IF EXISTS "+ TBLCUSTOMER);
        onCreate(db);
    }
    
    //INTERNAL METHOD
    private ContentValues putContent(String tbl, String[] arr, int id){
        ContentValues values = new ContentValues();
        if(id > 0){
            values.put("id", id);
        }
        
        switch (tbl){
            case TBLMANAGER:
                values.put("manager_username", arr[0]);
                values.put("manager_password", arr[1]);
                break;
            case TBLEMPLOYEE:
                values.put("employee_username", arr[0]);
                values.put("employee_password", arr[1]);
                values.put("employee_salary", Double.parseDouble(arr[2]));
                break;
            case TBLITEM:
                values.put("item_name", arr[0]);
                values.put("item_quantity", Integer.parseInt(arr[1]));
                values.put("item_init_price", Double.parseDouble(arr[2]));
                values.put("item_lowest_price", Double.parseDouble(arr[3]));
                values.put("item_selling_price", Double.parseDouble(arr[4]));
                break;
            case TBLMACHINE:
                values.put("is_available", Boolean.parseBoolean(arr[0]));
                values.put("washing", Boolean.parseBoolean(arr[1]));
                values.put("drying", Boolean.parseBoolean(arr[2]));
                values.put("washing_price", Double.parseDouble(arr[3]));
                values.put("drying_price", Double.parseDouble(arr[4]));
                break;
            case TBLCUSTOMER:
                values.put("customer_alias", arr[0]);
                values.put("employee_id", Integer.parseInt(arr[1]));
                values.put("machine_id_list", arr[2]);
                values.put("item_id_list", arr[3]);
                values.put("transaction_payment", Double.parseDouble(arr[4]));
                values.put("transaction_datetime", arr[5]);
                break;
            default:
        }
        return values;
    }
    
    //CRUD
    public boolean createData(String tbl, String[] arr){
        SQLiteDatabase liteDatabase = this.getWritableDatabase();
        long result = liteDatabase.insert(tbl, null, putContent(tbl, arr, 0));
        return !(result <= -1);
    }
    
    //RETURNS A MULTI-DIMENTIONAL ARRAY
    public String[][] retrieveData(String tbl, int id){
        SQLiteDatabase liteDatabase = this.getWritableDatabase();
        Cursor res;
        if(id == 0){
            res = liteDatabase.rawQuery("SELECT * FROM " + tbl, null);
        }
        else {
            res = liteDatabase.rawQuery("SELECT * FROM " + tbl + " WHERE id = '" + id + "'", null);
        }
        int column = 0;
        switch(tbl){
            case TBLMANAGER: column = 3; break;
            case TBLEMPLOYEE: column = 4; break;
            case TBLMACHINE: column = 6; break;
            case TBLITEM: column = 6; break;
            case TBLCUSTOMER: column = 7; break;
            default: column = 0;
        }
        String[][] result = new String[res.getCount()][column];
        if(res.getCount() == 0){
            return new String[][]{{"NO DATA!"}};
        } else {
            int ctr = 0;
            while (res.moveToNext()) {
                for(int i = 0; i < result[ctr].length; i++) {
                    result[ctr][i] = res.getString(i);
                }
                ctr++;
            }
        }
        return result;
    }
    
    public boolean updateData(String tbl, int id,  String[] arr){
        SQLiteDatabase liteDatabase = this.getWritableDatabase();
        long result = 0;
        if(liteDatabase.rawQuery("SELECT * FROM " + tbl + " WHERE id = '" + id + "'", null).getCount() < 1){
            result = liteDatabase.insert(tbl, null, putContent(tbl, arr, id));
            
        } else {
            result = liteDatabase.update(tbl, putContent(tbl, arr, id), "id = ?", new String[]{Integer.toString(id)});
        }
        return (!(result <= -1)) && (id > 0);
    }
    
    public boolean deleteData(String tbl, int id){
        SQLiteDatabase liteDatabase = this.getWritableDatabase();
        long result = liteDatabase.delete(tbl,"id = ?", new String[]{String.valueOf(id)});
        return (!(result <= -1)) && (id > 0);
}

    //SEARCH ALGORITHM FOR ANY COLUMN OF ANY TABLE
    public String[][] searchData(String tbl, String column, String data){
        SQLiteDatabase liteDatabase = this.getWritableDatabase();
        Cursor res = liteDatabase.rawQuery("SELECT * FROM " + tbl + " WHERE " + column + " = '" + data + "'", null);
        int col = 0;
        switch(tbl){
            case TBLMANAGER: col = 3; break;
            case TBLEMPLOYEE: col = 4; break;
            case TBLMACHINE: col = 6; break;
            case TBLITEM: col = 6; break;
            case TBLCUSTOMER: col = 7; break;
            default: col = 0;
        }
        String[][] result = new String[res.getCount()][col];
        if(res.getCount() == 0){
            return new String[][]{{"NO DATA!"}};
        } else {
            int ctr = 0;
            while (res.moveToNext()) {
                for(int i = 0; i < result[ctr].length; i++) {
                    result[ctr][i] = res.getString(i);
                }
                ctr++;
            }
        }
        return result;
    }
}
