package com.workout.pahapp;

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

    @Nullable
    @ColumnInfo(name = "age")
    private String age;

    @Nullable
    @ColumnInfo(name = "sex")
    private String sex;

    @Ignore
    public User(@NonNull Integer id,
            @Nullable String username,
            @Nullable Integer height,
            @Nullable Float weight,@Nullable String age) {
        this.id = id;
        this.username      = username;
        this.height        = height;
        this.weight        = weight;
        this.age = age;
    }

    public User(@Nullable String username,
                @Nullable Integer height,
                @Nullable Float weight,
                @Nullable String age,@Nullable String sex) {

        this.username      = username;
        this.height        = height;
        this.weight        = weight;
        this.age = age;
        this.sex = sex;
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
    @Nullable
    public String getAge() {
        return age;
    }
    @Nullable
    public String getSex() {
        return sex;
    }
}
