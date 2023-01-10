package com.filemanager.file;

import jakarta.persistence.*;

@Entity
public class FileModel {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String mainFieldOfInterest;
    private String secondaryFieldOfInterest;
    private String registrationNumber;
    private String year1;
    private String type;
    @Lob
    private byte[] data;

    public FileModel(){

    }

    public FileModel(String name, String mainFieldOfInterest, String secondaryFieldOfInterest, String registrationNumber, String year, String type, byte[] data) {
        this.name = name;
        this.mainFieldOfInterest = mainFieldOfInterest;
        this.secondaryFieldOfInterest = secondaryFieldOfInterest;
        this.registrationNumber = registrationNumber;
        this.year1 = year;
        this.type = type;
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMainFieldOfInterest() {
        return mainFieldOfInterest;
    }

    public void setMainFieldOfInterest(String mainFieldOfInterest) {
        this.mainFieldOfInterest = mainFieldOfInterest;
    }

    public String getSecondaryFieldOfInterest() {
        return secondaryFieldOfInterest;
    }

    public void setSecondaryFieldOfInterest(String secondaryFieldOfInterest) {
        this.secondaryFieldOfInterest = secondaryFieldOfInterest;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getYear1() {
        return year1;
    }

    public void setYear1(String year) {
        this.year1 = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
