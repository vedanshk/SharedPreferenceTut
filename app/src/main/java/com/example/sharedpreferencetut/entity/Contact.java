package com.example.sharedpreferencetut.entity;

public class Contact {

    //constants
    public static final String TABLE_NAME ="contacts";
    public static final String COLUMN_ID ="contact_id";
    public static final String COLUMN_EMAIL ="contact_email";

    public static final String COLUMN_NAME = "contact_name";

    //variables
    private String name;
    private String email;
    private int id;

    //constructor
    public Contact() {

    }

    public Contact(String name, String email, int id) {
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Sql Query : Creating the table

    public static final String CREATE_TABLE ="CREATE TABLE " + TABLE_NAME +"("+ COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
        + COLUMN_NAME + " text ," + COLUMN_EMAIL + ", DATETIME DEFAULT CURRENT_TIMESTAMP);";

}
