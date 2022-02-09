package main;

/**
 * 
 * @author Group 2
 * COMP3381 - Course Scheduling Application
 * Conflict
 * 11/12/2015
 * 
 * Represents a Conflict between courses, containing a conflict type, offending courses,
 * 	day of conflict, time of conflict, room of conflict, and professor of the class/es
 *
 */
public class Conflict 
{
	// all instance variables to hold data about the conflict
	private String type = "";
	private String courses = "";
	private String day = "";
	private String time = "";
	private String room = "";
	private String professor = "";
	
	/**
	 * Constructor for a Conflict = Room Conflict, takes in the two conflicting courses
	 * @param firstCourse first conflicting course
	 * @param secondCourse second conflicting course
	 */
	public Conflict (Course firstCourse, Course secondCourse)
	{
		// set all instance variables accordingly
		type =  firstCourse.getQuarter() + ": Room conflict";
		courses = firstCourse.getTitle() + " and " + secondCourse.getTitle();
		day = firstCourse.getDay() + " and " + secondCourse.getDay();
		time = firstCourse.getBegin(); 
		room = firstCourse.getRoom() + " and " + secondCourse.getRoom();
		professor = firstCourse.getInstructor() + " and " + secondCourse.getInstructor();
	}
	
	/**
	 * Constructor for a Conflict = Time Conflict, takes in the two conflicting courses
	 * @param firstCourse first conflicting course
	 * @param secondCourse second conflicting course
	 * @param NA is just a marker that this is a time conflict
	 */
	public Conflict (Course firstCourse, Course secondCourse, int NA)
	{
		// set all instance variables accordingly
		type =  firstCourse.getQuarter() + ": Time conflict";
		courses = firstCourse.getTitle() + " and " + secondCourse.getTitle();
		day = firstCourse.getDay() + " and " + secondCourse.getDay();
		time = firstCourse.getBegin(); 
		room = firstCourse.getRoom() + " and " + secondCourse.getRoom();
		professor = firstCourse.getInstructor() + " and " + secondCourse.getInstructor();
	}
	
	/**
	 * Constructor for a Conflict = Faculty Conflict, takes in the two conflicting courses
	 * @param firstCourse first conflicting course
	 * @param secondCourse second conflicting course
	 * @param prof faculty member that is teaching both
	 */
	public Conflict (Course firstCourse, Course secondCourse, String prof)
	{
		// sets instance variables accordingly
		type =  firstCourse.getQuarter() + ": Faculty conflict";
		courses = firstCourse.getTitle() + " and " + secondCourse.getTitle();
		day = firstCourse.getDay() + " and " + secondCourse.getDay();
		time = firstCourse.getBegin(); 
		room = firstCourse.getRoom() + " and " + secondCourse.getRoom();
		professor = firstCourse.getInstructor();
	}

	/**
	 * Accessor/Getter methods for all data members
	 * return desired data member
	 */
	
	public String getType() {
		return type;
	}

	public String getCourses() {
		return courses;
	}

	public String getDay() {
		return day;
	}

	public String getTime() {
		return time;
	}

	public String getRoom() {
		return room;
	}

	public String getProfessor() {
		return professor;
	}
	
}
