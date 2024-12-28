DROP DATABASE IF EXISTS hospital;
CREATE DATABASE IF NOT EXISTS hospital;
USE hospital;

-- Bảng Admin
CREATE TABLE Admin (
    adminID VARCHAR(20) PRIMARY KEY,
    password VARCHAR(255) NOT NULL
);

-- Bảng Doctor
CREATE TABLE Doctor (
    doctorID VARCHAR(20) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    firstname VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    faculty VARCHAR(100),
    phoneNumber VARCHAR(15),
    email VARCHAR(100),
    joinDate DATE
);

-- Bảng Patient
CREATE TABLE Patient (
    cccd VARCHAR(20) PRIMARY KEY,
    firstname VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    gender ENUM('Male', 'Female') NOT NULL,
    dateOfBirth DATE NOT NULL,
    address VARCHAR(255),
    phoneNumber VARCHAR(15),
    insurancePayPercent FLOAT
);

-- Bảng MedicalRecord
CREATE TABLE MedicalRecord (
    recordID VARCHAR(20) PRIMARY KEY,
    cccd VARCHAR(20) NOT NULL,
    doctorID VARCHAR(20),
    diagnosis TEXT,
    treatment TEXT,
    prescription TEXT,
    dateOfVisit DATE NOT NULL,
    followUpDate DATE,
    note TEXT,
    paid TINYINT(1),
    subTotalFee FLOAT,
    FOREIGN KEY (cccd) REFERENCES Patient(cccd) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (doctorID) REFERENCES Doctor(doctorID) ON DELETE SET NULL ON UPDATE CASCADE,
    lengthOfHospitalStay INT
);
