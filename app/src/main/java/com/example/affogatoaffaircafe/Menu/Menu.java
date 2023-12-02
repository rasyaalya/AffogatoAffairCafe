package com.example.affogatoaffaircafe.Menu;

import android.os.Parcel;
import android.os.Parcelable;

public class Menu implements Parcelable {
    private String nama;
    private String detail;
    private String harga;
    private String picture;
    private int quantity;
    private boolean isOrdered = false;

    public Menu() {

    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isOrdered() {
        return isOrdered;
    }

    public void setOrdered(boolean ordered) {
        isOrdered = ordered;
    }


    // Konstruktor Parcelable
    protected Menu(Parcel in) {
        nama = in.readString();
        detail = in.readString();
        harga = in.readString();
        picture = in.readString();
        quantity = in.readInt();
        isOrdered = in.readByte() != 0;
    }

    public static final Creator<Menu> CREATOR = new Creator<Menu>() {
        @Override
        public Menu createFromParcel(Parcel in) {
            return new Menu(in);
        }

        @Override
        public Menu[] newArray(int size) {
            return new Menu[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nama);
        dest.writeString(detail);
        dest.writeString(harga);
        dest.writeString(picture);
        dest.writeInt(quantity);
        dest.writeByte((byte) (isOrdered ? 1 : 0));
    }
}