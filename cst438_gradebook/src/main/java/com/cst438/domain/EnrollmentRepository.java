package com.cst438.domain;

import java.util.List;

//import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface EnrollmentRepository extends CrudRepository <Enrollment, Integer> {
	//Optional<Enrollment> findByStudentIdAndCourseId(int studentId, int courseId);
	
	 List<Enrollment> findByCourse(Course course);
	 
	 List<Enrollment> findCourseById(int course_id);

}
