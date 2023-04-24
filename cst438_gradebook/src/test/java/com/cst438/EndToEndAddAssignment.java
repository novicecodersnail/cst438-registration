package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentGrade;
import com.cst438.domain.AssignmentGradeRepository;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;



@SpringBootTest
public class EndToEndAddAssignment {

	public static final String CHROME_DRIVER_FILE_LOCATION = "C:/Users/18315/Downloads/chromedriver_win32/chromedriver.exe";

	public static final String URL = "http://localhost:3000/addAssignment";
	public static final String TEST_USER_EMAIL = "test@csumb.edu";
	public static final String TEST_INSTRUCTOR_EMAIL = "dwisneski@csumb.edu";
	public static final int SLEEP_DURATION = 1000; // 1 second.
	public static final String TEST_ASSIGNMENT_NAME = "Test Assignment";
	public static final String TEST_COURSE_TITLE = "Test Course";
	public static final String TEST_STUDENT_NAME = "Test";
	public static final String TEST_DUE_DATE = "2023-05-01";
	public static final String TEST_COURSE_ID = "999001";

	@Autowired
	EnrollmentRepository enrollmentRepository;

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	AssignmentGradeRepository assignnmentGradeRepository;

	@Autowired
	AssignmentRepository assignmentRepository;

	@Test
    public void addAssignmentTest() throws Exception {

        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);


        driver.get(URL);
        Thread.sleep(SLEEP_DURATION);

        try {
            WebDriverWait wait = new WebDriverWait(driver, 20);

            // Fill out the "Assignment Name" field
            WebElement assignmentNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("name")));
            assignmentNameField.sendKeys(TEST_ASSIGNMENT_NAME);

            // Fill out the "Due Date" field
            WebElement dueDateField = driver.findElement(By.name("dueDate"));
            dueDateField.sendKeys(TEST_DUE_DATE);

            // Fill out the "Course ID" field
            WebElement courseIdField = driver.findElement(By.name("courseId"));
            courseIdField.sendKeys(TEST_COURSE_ID);

            // Click the "Add Assignment" button to submit the form
            driver.findElement(By.xpath("//button[contains(text(), 'Add Assignment')]")).click();

            // Wait for the success toast message to appear
            WebElement successToast = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@class, 'Toastify__toast--success')]")));
            assertTrue(successToast.isDisplayed(), "Assignment added successfully.");
            

        } catch (Exception ex) {
            throw ex;
        } finally {

            driver.quit();
        }

    }
}