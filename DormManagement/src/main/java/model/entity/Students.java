package model.entity;

import java.sql.Date;

public class Students {
    private int studentId;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private Date dob;
    private String gender;
    private String phone;
    private String address;
    private String statusRoom;
    private Date createdAt;
    private Date updatedAt;
    private String cccd;
    private int isApproved;

    public Students() {
    }

    public Students(int studentId, String username, String password, String email, String fullName, Date dob, String gender, String phone, String address, String statusRoom, Date createdAt, Date updatedAt, String cccd) {
        this.studentId = studentId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.dob = dob;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.statusRoom = statusRoom;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.cccd = cccd;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatusRoom() {
        return statusRoom;
    }

    public void setStatusRoom(String statusRoom) {
        this.statusRoom = statusRoom;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCccd() {
        return cccd;
    }
    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public int getIsApproved() {
        return isApproved;
    }
    public void setIsApproved(int isApproved) {
        this.isApproved = isApproved;
    }
}