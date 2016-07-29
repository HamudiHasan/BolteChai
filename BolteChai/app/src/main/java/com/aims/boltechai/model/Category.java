package com.aims.boltechai.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Zakir on 17/02/2016.
 */

@Table(name = "Category")
public class Category {
    // TODO: 28/07/2016 for sonet
    // make this class a model table for active android


    @Column(name = "categoryId")
    public int categoryId;

    @Column(name = "parentId")
    public int parentId;

    @Column (name = "categoryImage")
    public String categoryImage;

    @Column (name = "categoryTitle")
    public String categoryTitle;
}
