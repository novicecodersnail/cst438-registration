package com.cst438.domain;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Assignment {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="course_id")
	private Course course;
	
	@OneToMany(mappedBy = "assignment", cascade = CascadeType.REMOVE)
	private List<AssignmentGrade> assignmentGrades;
	
	private String name;
	private Date dueDate;
	private int needsGrading;  // 0 = false,  1= true (past due date and not all students have grades)
	
	
	
	public Assignment() {
		
	}


	public Assignment(Course course, List<AssignmentGrade> assignmentGrades, String name, Date dueDate,
			int needsGrading) {
		super();
		this.course = course;
		this.assignmentGrades = assignmentGrades;
		this.name = name;
		this.dueDate = dueDate;
		this.needsGrading = needsGrading;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public int getNeedsGrading() {
		return needsGrading;
	}
	public void setNeedsGrading(int needsGrading) {
		this.needsGrading = needsGrading;
	}
	
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	
	
	public List<AssignmentGrade> getAssignmentGrades() {
		return assignmentGrades;
	}


	public void setAssignmentGrades(List<AssignmentGrade> assignmentGrades) {
		this.assignmentGrades = assignmentGrades;
	}


	@Override
	public String toString() {
		return "Assignment [id=" + id + ", course_id=" + course.getCourse_id() + ", name=" + name + ", dueDate=" + dueDate
				+ ", needsGrading=" + needsGrading + "]";
	}
	
}
