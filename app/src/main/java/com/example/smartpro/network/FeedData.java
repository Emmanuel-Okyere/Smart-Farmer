package com.example.smartpro.network;

import com.google.gson.annotations.SerializedName;

public class FeedData {
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("entry_id")
    private String entry_id;
    @SerializedName("field1")
    private int field1;
    @SerializedName("field2")
    private int field2;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getEntry_id() {
        return entry_id;
    }

    public void setEntry_id(String entry_id) {
        this.entry_id = entry_id;
    }

    public int getField1() {
        return field1;
    }

    public void setField1(int field1) {
        this.field1 = field1;
    }

    public int getField2() {
        return field2;
    }

    public void setField2(int field2) {
        this.field2 = field2;
    }
}
