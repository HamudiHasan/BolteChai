package com.aims.boltechai.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Zakir on 28/07/2016.
 */

@Table(name = "ActivityItem")
public class ActivityItem {

    // TODO: 28/07/2016 for sonet
    // make this class a model table for active android

    @Column(name = "acttivityId")
    public int acttivityId;

    @Column(name = "acttivityImage")
    public String acttivityImage;

    @Column(name = "acttivityTitle")
    public String acttivityTitle;

    @Column(name = "acttivityAudio")
    public String acttivityAudio;

    @Column(name = "acttivityCategory")
    public int acttivityCategory;

}
