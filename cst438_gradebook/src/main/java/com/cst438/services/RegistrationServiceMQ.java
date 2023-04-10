package com.cst438.services;


import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.AssignmentGrade;
import com.cst438.domain.Course;
import com.cst438.domain.CourseDTOG;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;


public class RegistrationServiceMQ extends RegistrationService {

	@Autowired
	EnrollmentRepository enrollmentRepository;

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public RegistrationServiceMQ() {
		System.out.println("MQ registration service ");
	}

	// ----- configuration of message queues

	@Autowired
	Queue registrationQueue;


	// ----- end of configuration of message queue

	// receiver of messages from Registration service
	
	@RabbitListener(queues = "gradebook-queue")
	@Transactional
	public void receive(EnrollmentDTO enrollmentDTO) {
		
		
		Enrollment enrollment= new Enrollment();
		enrollment.setStudentName(enrollmentDTO.studentName);
		enrollment.setStudentEmail(enrollmentDTO.studentEmail);
		Course course= courseRepository.findById(enrollmentDTO.course_id)
		        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CourseW not found"));
		
		enrollment.setCourse(course);
		
		enrollment=enrollmentRepository.save(enrollment);
		enrollmentDTO.id=enrollment.getId();
		System.out.println("Enrollment received");
	}

	// sender of messages to Registration Service
	@Override
	public void sendFinalGrades(int course_id, CourseDTOG courseDTO) {
	    List<Enrollment> enrollments = enrollmentRepository.findCourseById(course_id);
	    List<CourseDTOG.GradeDTO> gradeDTOs = new ArrayList<>();

	    for (Enrollment enrollment : enrollments) {
	    	// create new gradeDTO obj for every enrollment in course 
	        CourseDTOG.GradeDTO gradeDTO = new CourseDTOG.GradeDTO();
	        // set corresponding email and name field 
	        gradeDTO.student_email = enrollment.getStudentEmail();
	        gradeDTO.student_name = enrollment.getStudentName();
	        
	        // Get the assignment grades for this enrollment
	        List<AssignmentGrade> assignmentGrades = enrollment.getAssignmentGrades();
	        
	        // Convert the assignment grades to a list of doubles
	        List<Double> grades = new ArrayList<>();
	        for (AssignmentGrade ag : assignmentGrades) {
	        	grades.add(Double.parseDouble(ag.getScore()));
	        }

	        // Calculate the final grade
	        double finalGrade = calculateGrade(grades);

	        // Set the grade field in the gradeDTO object
	        gradeDTO.grade = String.format("%.2f", finalGrade);
	        // add this gradeDTO obj to the list of grades for enrollment in course 
	        gradeDTOs.add(gradeDTO);
	    }

	    rabbitTemplate.convertAndSend(registrationQueue.getName(), gradeDTOs);
	    System.out.println("Final grades sent to gradebook service");
	}

	private double calculateGrade(List<Double> grades) {
	    double totalGrade = 0;
	    for (Double grade : grades) {
	        totalGrade += grade;
	    }
	    double averageGrade = totalGrade / grades.size();
	    return averageGrade;
	}

}
