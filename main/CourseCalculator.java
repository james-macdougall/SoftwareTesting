package main;

import java.util.ArrayList;

/**
 * 
 * @author Group 2
 * COMP3381 - Course Scheduling Application
 * CourseCalculator
 * 11/12/2015
 * 
 * Calculates percentage of classes on a particular day
 *
 */
public class CourseCalculator 
{
    private int monWedCounter;
    private int tuesThursCounter;
    private int tuesFriCounter;
    private int wedFriCounter;
    
    private float[] percentages = new float[4];

    /**
     * Method to calculate course percentages by day and return an array of percentages
     * @param courseList list of all possible courses in the schedule
     * @return array of float percentages
     */
    public float[] calculatePercentages (ArrayList<Course> allCourses)
    {
    	// normalCourses list to hold courses on two days, typical times
    	//   these are the only courses we care about for this calculation
    	ArrayList<Course> normalCourses = new ArrayList<Course>();
    	// loop through all courses to find normal courses (on two days)
    	for(Course singleCourse : allCourses)
    	{
    		// check if on two days, put in list of so
    		if (singleCourse.getDay().equals("MW") || singleCourse.getDay().equals("TR")
    				|| singleCourse.getDay().equals("TF") || singleCourse.getDay().equals("WF"))
    		{
    			normalCourses.add(singleCourse);
    		}
    	}
    	//iterate over all normal courses, separate by day
        for (Course singleCourse : normalCourses) 
        {
        	//check what days, add one if on those days
            if (singleCourse.getDay().equals("MW")) 
            {
                monWedCounter += 1;
            }
            else if (singleCourse.getDay().equals("TR")) 
            {
                tuesThursCounter += 1;
            }
            else if (singleCourse.getDay().equals("WF")) 
            {
                wedFriCounter += 1;
            }
            else if (singleCourse.getDay().equals("TF")) 
            {
                tuesFriCounter += 1;
            }
        }
        //get total number of normal classes, divide each days counter
        //  by the total number
        float total = (float)normalCourses.size();
        percentages[0] = ((monWedCounter/total) * 100);
        percentages[1] = ((tuesThursCounter/total) * 100);
        percentages[2] = ((tuesFriCounter/total) * 100);
        percentages[3] = ((wedFriCounter/total) * 100);
        //return an array of 4 floats representing the percentages
        return percentages;
    }
}
