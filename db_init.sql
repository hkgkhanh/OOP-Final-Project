DROP DATABASE IF EXISTS hospital;
CREATE DATABASE IF NOT EXISTS hospital;
USE hospital;

-- Bảng Admin
CREATE TABLE Admin (
    adminID INT PRIMARY KEY,
    password VARCHAR(255) NOT NULL
);

-- Bảng Doctor
CREATE TABLE Doctor (
    doctorID INT PRIMARY KEY,
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
    phoneNumber VARCHAR(15)
);

-- Bảng MedicalRecord
CREATE TABLE MedicalRecord (
    recordID INT PRIMARY KEY,
    cccd VARCHAR(20) NOT NULL,
    doctorID INT,
    diagnosis TEXT,
    treatment TEXT,
    prescription TEXT,
    dateOfVisit DATE NOT NULL,
    followUpDate DATE,
    note TEXT,
    FOREIGN KEY (cccd) REFERENCES Patient(cccd) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (doctorID) REFERENCES Doctor(doctorID) ON DELETE SET NULL ON UPDATE CASCADE
);
