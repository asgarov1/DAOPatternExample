package com.asgarov.student.domain;

public class StudentCourse {
    private int studentCourseId;
    private int studentId;
    private int courseId;

    public StudentCourse() {
    }

    public StudentCourse(int studentId, int courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    public StudentCourse(int studentCourseId, int studentId, int courseId) {
        this.studentCourseId = studentCourseId;
        this.studentId = studentId;
        this.courseId = courseId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studendId) {
        this.studentId = studendId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getStudentCourseId() {
        return studentCourseId;
    }

    public void setStudentCourseId(int studentCourseId) {
        this.studentCourseId = studentCourseId;
    }

    @Override
    public String toString() {
        return "StudentCourse [studentCourseId=" + studentCourseId + ", studentId=" + studentId + ", courseId="
                + courseId + "]";
    }
}
