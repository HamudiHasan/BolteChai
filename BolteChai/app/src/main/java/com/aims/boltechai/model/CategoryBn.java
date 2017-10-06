package com.aims.boltechai.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Zakir on 17/02/2016.
 */

@Table(name = "Category", id = "categoryId")
public class CategoryBn extends Model {
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

    // constructor for lib
    public CategoryBn() {
    }

    public CategoryBn(int parentId, String categoryTitle, String categoryImage) {

        this.parentId = parentId;
        this.categoryTitle = categoryTitle;
        this.categoryImage = categoryImage;
    }

    public CategoryBn(int parentId, String categoryTitle, String categoryImage, String categoryAudio) {

        this.parentId = parentId;
        this.categoryTitle = categoryTitle;
        this.categoryImage = categoryImage;
        this.categoryAudio = categoryAudio;
    }
}
