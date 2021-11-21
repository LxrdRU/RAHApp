package com.example.pahapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private final String id;

    @Nullable
    @ColumnInfo(name = "username")
    private final String username;

    @Nullable
    @ColumnInfo(name = "height")
    private final Integer height;

    @Nullable
    @ColumnInfo(name = "weight")
    private final Double weight;

    public User(@NonNull String id,
                @Nullable String username,
                @Nullable Integer height,
                @Nullable Double weight) {
        this.id            = id;
        this.username      = username;
        this.height        = height;
        this.weight        = weight;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Nullable
    public String getUsername() {
        return username;
    }

    @Nullable
    public Integer getHeight() {
        return height;
    }

    @Nullable
    public Double getWeight() {
        return weight;
    }

}
