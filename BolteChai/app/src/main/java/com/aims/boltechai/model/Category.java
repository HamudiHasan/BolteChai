package com.aims.boltechai.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Zakir on 17/02/2016.
 */

@Table(name = "Category", id = "categoryId")
public class Category extends Model {
    // TODO: 28/07/2016 for sonet
    // make this class a model table for active android

    // for adapter
    public int id;

    @Column(name = "parentId")
    public int parentId;

    @Column(name = "categoryTitle")
    public String categoryTitle;

    @Column(name = "categoryImage")
    public String categoryImage;


    @Column(name = "categoryAudio")
    public String categoryAudio;

    @Column(name = "language")
    public String language;

    // constructor for lib
    public Category() {
    }

    public Category(int parentId, String categoryTitle, String categoryImage,String language) {

        this.parentId = parentId;
        this.categoryTitle = categoryTitle;
        this.categoryImage = categoryImage;
        this.language = language;
    }

    public Category(int parentId, String categoryTitle, String categoryImage, String categoryAudio , String language) {

        this.parentId = parentId;
        this.categoryTitle = categoryTitle;
        this.categoryImage = categoryImage;
        this.categoryAudio = categoryAudio;
        this.language = language;
    }
}
