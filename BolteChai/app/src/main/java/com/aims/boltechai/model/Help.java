package com.aims.boltechai.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by hhson on 11/16/2016.
 */

@Table(name = "Help")
public class Help extends Model {

    @Column(name = "name")
    public String name;
    @Column(name = "phone")
    public String phone;
    @Column(name = "image")
    public String image;

    public Help(String name, String phone, String image) {
        this.name = name;
        this.phone = phone;
        this.image = image;
    }

    public Help() {
    }
}
