package com.example.smartpro.network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Results {
    @SerializedName("channel")
     private ChannelData channelDataList;
    @SerializedName("feeds")
    private ArrayList<FeedData>feedDataList;

    public ChannelData getChannelDataList(){
        return channelDataList;
    }

    public ArrayList<FeedData> getFeedDataList() {
        return feedDataList;
    }

    public void setFeedDataList(ArrayList<FeedData> feedDataList) {
        this.feedDataList = feedDataList;
    }

    public void setChannelDataList(ChannelData channelDataList) {
        this.channelDataList = channelDataList;
    }

}
