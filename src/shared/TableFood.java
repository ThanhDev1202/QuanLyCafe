/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author HAI
 */

    package shared;

import java.io.Serializable;

public class TableFood implements Serializable {
    private int id;
    private String ten;
    private String status;

    public TableFood() {
    }

    public TableFood(int id, String ten, String status) {
        this.id = id;
        this.ten = ten;
        this.status = status;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

