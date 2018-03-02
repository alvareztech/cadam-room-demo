package tech.alvarez.contacts.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

import tech.alvarez.contacts.db.converter.DateConverter;

@Entity(tableName = "contacts")
@TypeConverters(DateConverter.class)
public class Contact {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String address;
    private Date birthday;
    private String phone;
    private String email;

    @Ignore
    public Contact() {
        this.name = "";
        this.address = "";
        this.birthday = null;
        this.phone = "";
        this.email = "";
    }

    public Contact(String name, String address, Date birthday, String phone, String email) {
        this.name = name;
        this.address = address;
        this.birthday = birthday;
        this.phone = phone;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

