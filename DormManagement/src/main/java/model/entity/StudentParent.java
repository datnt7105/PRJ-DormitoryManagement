/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entity;

/**
 *
 * @author ngtiendat
 */
public class StudentParent {
    private int studentParentId;
    private int studentId;
    private String parentName;
    private String phone;
    private String relationship;

    public StudentParent() {
    }

    public StudentParent(int studentParentId, int studentId, String parentName, String phone, String relationship) {
        this.studentParentId = studentParentId;
        this.studentId = studentId;
        this.parentName = parentName;
        this.phone = phone;
        this.relationship = relationship;
    }

    public int getStudentParentId() {
        return studentParentId;
    }

    public void setStudentParentId(int studentParentId) {
        this.studentParentId = studentParentId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
    
    
}
