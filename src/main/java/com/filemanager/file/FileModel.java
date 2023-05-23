package com.filemanager.file;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
public class FileModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq")
    @GenericGenerator(name = "seq", strategy = "increment")
    private long id;
    private String name;
    private String mainFieldOfInterest;
    private String secondaryFieldOfInterest;
    private String registrationNumber;
    private String date;
    @DateTimeFormat(pattern = "d/MM/yyyy")
    private LocalDate numberDate;

    private LocalDate fDate;

    private String fName;

    private String fNumber;
    private Double fValue;
    private String type;
    @Lob
    private byte[] data;

    public FileModel() {

    }

    public FileModel(String name, String mainFieldOfInterest, String secondaryFieldOfInterest, String registrationNumber, LocalDate numberDate, LocalDate fdate, String fName, String fNumber, double fValue, String date, String type, byte[] data) {
        this.name = name;
        this.mainFieldOfInterest = mainFieldOfInterest;
        this.secondaryFieldOfInterest = secondaryFieldOfInterest;
        this.registrationNumber = registrationNumber;
        this.numberDate = numberDate;
        this.fDate = fdate;
        this.fName = fName;
        this.fNumber = fNumber;
        this.fValue =  fValue;
        this.date = date;
        this.type = type;
        this.data = data;
    }

    public LocalDate getNumberDate() {
        return numberDate;
    }

    public void setNumberDate(LocalDate numberDate) {
        this.numberDate = numberDate;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public LocalDate getfDate() {
        return fDate;
    }

    public void setfDate(LocalDate fDate) {
        this.fDate = fDate;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getfNumber() {
        return fNumber;
    }

    public void setfNumber(String fNumber) {
        this.fNumber = fNumber;
    }

    public Double getfValue() {
        return fValue;
    }

    public void setfValue(Double fValue) {
        this.fValue = fValue;
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
