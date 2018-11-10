package com.components.ras.ras;

public class item {
    String name;
    String imageId;
    String quantity;
    String description;
    String datasheet;
    int position;
    boolean isConfirmed;

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public item(String name, String imageId, String quantity) {
        this.name = name;
        this.imageId = imageId;
        this.quantity = quantity;
    }
    public item(String name, String imageId, String quantity,Boolean isConfirmed) {
        this.name = name;
        this.imageId = imageId;
        this.quantity = quantity;
    }
    public item(String name, String imageId, String quantity, String description) {
        this.name = name;
        this.imageId = imageId;
        this.quantity = quantity;
        this.description = description;
    }
    public item(String name, String imageId, String quantity, String description,String datasheet) {
        this.name = name;
        this.imageId = imageId;
        this.quantity = quantity;
        this.description = description;
        this.datasheet = datasheet;

    }

    public String getDatasheet() {
        return datasheet;
    }

    public void setDatasheet(String datasheet) {
        this.datasheet = datasheet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPosition() {
        return position;
    }
}
