package main;

import java.util.ArrayList;

/**
 * 
 * @author Group 2
 * COMP3381 - Course Scheduling Application
 * Faculty
 * 11/12/2015
 * 
 * Represents a faculty member, his/her associated courses, name, and course load by quarter
 *
 */
public class Faculty 
{
	// instance variables for name and list of courses taught by this faculty member
	private String name;
	private ArrayList<Course> coursesTaught = new ArrayList<Course>();
	
	/**
	 * Constructor for a faculty member
	 * @param name1 name of faculty member
	 */
	public Faculty (String name1)
	{
		name = name1;
	}
	
	/**
	 * Method to get the faculty member's course load based on a quarter
	 * @param quarter string representation of quarter to calculate load
	 * @return int load = number of courses taught
	 */
	public int getLoad(String quarter)
	{
		// initialize result, iterate over all courses taught, increment result if quarter values match
		int result = 0;
		for (Course singleCourse : coursesTaught)
		{
			if (quarter.equalsIgnoreCase(singleCourse.getQuarter()))  // test if course is taught in desired quarter
			{
				++result;
			}
		}
		return result;
	}
	
	/**
	 * Method to add course to the list of courses taught by this faculty member
	 * @param newOne course to add
	 */
	public void addCourse(Course newOne)
	{
		coursesTaught.add(newOne);
	}
	
	/**
	 * Method to remove a course from list of courses taught
	 * @param removeThis course to remove
	 * @return success value
	 */
	public boolean removeCourse(Course removeThis)
	{
		for (Course singleCourse : coursesTaught)
		{
			if (singleCourse.equals(removeThis))
			{
				coursesTaught.remove(singleCourse);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method to remove a course based on course values
	 * @param quarter string of quarter
	 * @param title string of title of course
	 * @param day string of day value
	 * @return success value
	 */
	public boolean removeCourse(String quarter, String title, String day)
	{
		for (Course singleCourse : coursesTaught)
		{
			if (quarter.equalsIgnoreCase(singleCourse.getQuarter()) && 
					title.equalsIgnoreCase(singleCourse.getTitle()) &&
					day.equalsIgnoreCase(singleCourse.getDay()))
			{
				coursesTaught.remove(singleCourse);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Getters/Accessors for data members
	 * return desired data member
	 */
	
	public ArrayList<Course> getCoursesTaught()
	{
		return coursesTaught;
	}
	
	public String getName()
	{
		return name;
	}
}
