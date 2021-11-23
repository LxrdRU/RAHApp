package com.example.pahapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private Integer id;

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    @Nullable
    @ColumnInfo(name = "username")
    private String username;

    @Nullable
    @ColumnInfo(name = "height")
    private Integer height;

    @Nullable
    @ColumnInfo(name = "weight")
    private Float weight;


    @Ignore
    public User(@NonNull Integer id,
            @Nullable String username,
            @Nullable Integer height,
            @Nullable Float weight) {
        this.id = id;
        this.username      = username;
        this.height        = height;
        this.weight        = weight;
    }

    public User(@Nullable String username,
                @Nullable Integer height,
                @Nullable Float weight) {

        this.username      = username;
        this.height        = height;
        this.weight        = weight;
    }




    @NonNull
    public Integer getId() {
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
    public Float getWeight() {
        return weight;
    }

}
