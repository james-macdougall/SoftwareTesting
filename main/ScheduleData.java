package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * 
 * @author Group 2
 * COMP3381 - Course Scheduling Application
 * ScheduleData
 * 11/12/2015
 * 
 * Stores all schedule related (and faculty) data for easy access and manipulation.
 * Also handles saving data back to the file before program termination.
 * Used throughout the program as the point of access for data.
 *
 */
public class ScheduleData 
{
	// instance variables to hold each quarter's courses
	private ArrayList<Course> autumnCourses = new ArrayList<Course>();
	private ArrayList<Course> winterCourses = new ArrayList<Course>();
	private ArrayList<Course> springCourses = new ArrayList<Course>();
	private ArrayList<Course> summerCourses = new ArrayList<Course>();
	// Holds all faculty personnel
	private ArrayList<Faculty> facultyData  = new ArrayList<Faculty>();
	// stores all conflicting courses as Conflict objects
	private ArrayList<Conflict> allConflicts = new ArrayList<Conflict>();
	// string for file path
	private String filePath = new String();

	/**
	 * Constructor for Schedule Data, uses scanned and parsed info to "build" a schedule.
	 * @param autumn list of autumn courses
	 * @param winter list of winter courses
	 * @param spring list of spring courses
	 * @param summer list of summer courses
	 * @param faculty list of faculty members
	 * @param path string file path for saving data
	 */
	public ScheduleData(ArrayList<Course> autumn, ArrayList<Course> winter, ArrayList<Course> spring, 
			ArrayList<Course> summer, ArrayList<Faculty> faculty, String path)
	{
		// set values of instance variables accordingly
		autumnCourses = autumn;
		winterCourses = winter;
		springCourses = spring;
		summerCourses = summer;
		facultyData = faculty;
		filePath = path;
		// immediately find conflicts among courses
		findConflicts("Autumn");
	}

	/**
	 * Method to find and return all course conflicts found in the schedule
	 * @return list of Conflict objects
	 */
	public ArrayList<Conflict> getQuarterConflicts(String quarter)
	{
		findConflicts(quarter);
		return allConflicts;
	}

	/**
	 * Method to look through the schedule and find all possible conflicts.
	 * Find two types of conflicts: Faculty and Room.
	 */
	private void findConflicts(String quarter)
	{
		// create a new array list to hold the desired quarter
		ArrayList<Course> allCourses = new ArrayList<Course>();
		allConflicts = new ArrayList<Conflict>();
		if(quarter.equalsIgnoreCase("Autumn"))
		{
			allCourses = autumnCourses;
		}
		else if(quarter.equalsIgnoreCase("Winter"))
		{
			allCourses = winterCourses;
		}
		else if(quarter.equalsIgnoreCase("Spring"))
		{
			allCourses = springCourses;
		}
		else if(quarter.equalsIgnoreCase("Summer"))
		{
			allCourses = summerCourses;
		}
		// iterate over all courses and compare them to find conflict
		for(int i = 0; i < allCourses.size(); i++)
		{
			// iterate over all courses other that course at index i, compare them
			for(int j = i + 1; j < allCourses.size(); j++)
			{

				allCourses.get(i).getBegin().replace(":", "");
				// test year, quarter, days, room, and start time for equality. That would result in a room conflict
				if (allCourses.get(i).getYear().equals(allCourses.get(j).getYear()) &&
						allCourses.get(i).getQuarter().equals(allCourses.get(j).getQuarter()) &&
						allCourses.get(i).getRoom().equals(allCourses.get(j).getRoom()) &&
						sameDays(allCourses.get(i), allCourses.get(j)) &&
						allCourses.get(i).getBegin().equals(allCourses.get(j).getBegin()))
				{
					// test validity of defining values: room and begin time. If they are blank, it is not a conflict
					if(allCourses.get(i).getRoom().equals(" ") || allCourses.get(i).getBegin().equals(" "))
					{
						//ignore, invalid room or start time entry
					}
					// valid values and they are equal, so it is a room conflict
					else
					{
						allConflicts.add(new Conflict(allCourses.get(i), allCourses.get(j)));
					}
				}
				// test year, quarter, days, and start time for equality. That would result in a time conflict
				else if (allCourses.get(i).getYear().equals(allCourses.get(j).getYear()) &&
						allCourses.get(i).getQuarter().equals(allCourses.get(j).getQuarter()) &&
						sameDays(allCourses.get(i), allCourses.get(j)) &&
						(allCourses.get(i).getBegin().equals(allCourses.get(j).getBegin()) || //EDIT STARTS expanding definition of time conflict
							(Integer.parseInt(allCourses.get(j).getBegin().replace(":", "")) < Integer.parseInt(allCourses.get(i).getBegin().replace(":", "")) && 
							Integer.parseInt(allCourses.get(j).getEnd().replace(":", "")) > Integer.parseInt(allCourses.get(i).getBegin().replace(":", ""))) || //begin time inside another class
							(Integer.parseInt(allCourses.get(j).getBegin().replace(":", "")) < Integer.parseInt(allCourses.get(i).getEnd().replace(":", "")) && 
							Integer.parseInt(allCourses.get(j).getEnd().replace(":", "")) > Integer.parseInt(allCourses.get(i).getEnd().replace(":", "")))	|| //end time inside another class
							(Integer.parseInt(allCourses.get(j).getBegin().replace(":", "")) < Integer.parseInt(allCourses.get(i).getBegin().replace(":", "")) && 
							Integer.parseInt(allCourses.get(j).getEnd().replace(":", "")) > Integer.parseInt(allCourses.get(i).getEnd().replace(":", ""))))) // start and end outside this class start end
				{ //to see if there is overlap see if i's begin or end time are inside j or one classes begin and end are outside anothers
					//very difficult with the currect time setup didn't 
					// test validity of defining values: begin time. If they are blank, it is not a conflict
					if(allCourses.get(i).getBegin().equals(" "))
					{
						//ignore, invalid room or start time entry
					}
					// valid values and they are equal, so it is a room conflict
					else
					{
						allConflicts.add(new Conflict(allCourses.get(i), allCourses.get(j),0));
					}
				}
				// test year, quarter, days, instructor, and start time for equality. That would result in a faculty conflict
				if (allCourses.get(i).getYear().equals(allCourses.get(j).getYear()) &&
						allCourses.get(i).getQuarter().equals(allCourses.get(j).getQuarter()) &&
						sameDays(allCourses.get(i), allCourses.get(j)) &&
						allCourses.get(i).getInstructor().equals(allCourses.get(j).getInstructor()) &&
						allCourses.get(i).getBegin().equals(allCourses.get(j).getBegin()))
				{
					// test validity of defining values: instructor and begin time. If they are blank, it is not a conflict
					if(allCourses.get(i).getRoom().equals(" ") || allCourses.get(i).getBegin().equals(" "))
					{
						//ignore, invalid instructor or start time entry
					}
					// valid values and they are equal, so it is a faculty conflict
					else
					{
						allConflicts.add(new Conflict(allCourses.get(i), allCourses.get(j), allCourses.get(i).getInstructor()));
					}
				}
			}
		}
	}

	/**
	 * Method to check if two courses are on the same day/s
	 * @param firstCourse first course to check days
	 * @param secondCourse second course to check days
	 * @return true if on same days, false otherwise
	 */
	private boolean sameDays(Course firstCourse, Course secondCourse)
	{
		// loop through all characters in the first course, compare them to the second, conflict if any characters are equal
		for(int i = 0; i < firstCourse.getDay().length(); i++)
		{
			// loop through all characters in the second course
			for (int j = 0; j < secondCourse.getDay().length(); j++)
			{
				// test if any characters are equal, if so they are on same day
				if(firstCourse.getDay().charAt(i) == secondCourse.getDay().charAt(j))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Method to return an ArrayList<Object[]> of all the conflicts in the schedule
	 * @return ArrayList<Object[]> of all conflicts
	 */
	public ArrayList<Object[]> getConflictsArray(String quarter)
	{
		// find all conflicts again for up-to-date info, create result array
		findConflicts(quarter);
		ArrayList<Object[]> result = new ArrayList<Object[]>();
		// iterate over every conflict and transform it into an Object array
		for(Conflict singleConflict : allConflicts)
		{
			// get all conflict values and place them in a temporary object array
			Object[] temp = {singleConflict.getType(), singleConflict.getCourses(), singleConflict.getDay(),
					singleConflict.getRoom(), singleConflict.getTime(), singleConflict.getProfessor()};
			// add object array to result
			result.add(temp);
		}
		return result;
	}

	/**
	 * Method to save modified table data to the schedule data (based on quarter)
	 * 		allows uploading a new quarter to the GUI for editing
	 * @param tableData list of list = list of rows with a list of columns for each row
	 * @param quarter string representation of the quarter
	 */
	public void saveData(ArrayList<ArrayList<String>> tableData, String quarter)
	{
		// new array list to hold all courses parsed from the table data
		ArrayList<Course> newCourses = new ArrayList<Course>();
		// iterate over all rows and extract all data from its respective columns
		for(int i = 0; i < tableData.size(); i++)
		{
			//take all 21 column values and create a new course with them
			newCourses.add(new Course(tableData.get(i).get(0), tableData.get(i).get(1), tableData.get(i).get(2),
					tableData.get(i).get(3), tableData.get(i).get(4), tableData.get(i).get(5),
					tableData.get(i).get(6), tableData.get(i).get(7), tableData.get(i).get(8),
					tableData.get(i).get(9), tableData.get(i).get(10), tableData.get(i).get(11),
					tableData.get(i).get(12), tableData.get(i).get(13), tableData.get(i).get(14),
					tableData.get(i).get(15), tableData.get(i).get(16), tableData.get(i).get(17),
					tableData.get(i).get(18), tableData.get(i).get(19), tableData.get(i).get(20)));
		}
		// test which class list to replace with the new data and replace it
		if(quarter.equalsIgnoreCase("Autumn"))
		{
			autumnCourses = newCourses;
		}
		else if(quarter.equalsIgnoreCase("Winter"))
		{
			winterCourses = newCourses;
		}
		else if(quarter.equalsIgnoreCase("Spring"))
		{
			springCourses = newCourses;
		}
		else if(quarter.equalsIgnoreCase("Summer"))
		{
			summerCourses = newCourses;
		}
		// call method to update all faculty data and find conflicts
		updateFacultyData();
		findConflicts(quarter);
	}

	/**
	 * Method to handle saving data back to the file.
	 * Ensures most up-to-date information is stored.
	 */
	public void saveDataToFile()
	{
		// given the file IO nature, surround in try/catch block
		try 
		{
			//instantiate new print writer for saving (using original file path)
			PrintWriter courseSaver = new PrintWriter(new File(filePath));
			// create a new array list and fill it will all courses by adding the existing lists to it
			ArrayList<Course> allCourses = new ArrayList<Course>();
			allCourses.addAll(autumnCourses);
			allCourses.addAll(winterCourses);
			allCourses.addAll(springCourses);
			allCourses.addAll(summerCourses);
			// iterate over all courses and write each course's data to the file
			for(Course singleCourse : allCourses)
			{
				courseSaver.println(singleCourse.getQuarter() + "," + singleCourse.getYear() + "," + singleCourse.getType() + "," +
						singleCourse.getCrn() + "," + singleCourse.getTitle() + "," + singleCourse.getName() + "," +
						singleCourse.getHrs() + "," + singleCourse.getMedium() + "," + singleCourse.getSection() + "," +
						singleCourse.getSeat() + "," + singleCourse.getEn() + "," + singleCourse.getInstructor() + "," +
						singleCourse.getDay() + "," + singleCourse.getBegin() + "," + singleCourse.getEnd() + "," +
						singleCourse.getFin() + "," + singleCourse.getRoom() + "," + singleCourse.getCap() + "," +
						singleCourse.getCrossList() + "," + singleCourse.getSecText() + "," + singleCourse.getNotes() + ",");
			}
			// flush buffer and close writer
			courseSaver.flush();
			courseSaver.close();
		} 
		// File deleted/moved during runtime, inform user and terminate program.
		catch (FileNotFoundException e) 
		{
			System.out.println("File deleted or moved during runtime.");
			System.out.print("Program terminated.");
			e.printStackTrace();
			System.exit(1);
		}
	}

	//EDIT STARTS 
	public ArrayList<String> getListRooms(){
		ArrayList<String> listOfRooms = new ArrayList<String>();
		ArrayList<Course> allCourses = new ArrayList<Course>();
		allCourses.addAll(autumnCourses);
		allCourses.addAll(winterCourses);
		allCourses.addAll(springCourses);
		allCourses.addAll(summerCourses);
		for (Course singleCourse : allCourses)
		{
			if(!listOfRooms.contains(singleCourse.getRoom()))
			{
				listOfRooms.add(singleCourse.getRoom());
			}
		}
		return listOfRooms;
	}
	public ArrayList<String> getListInstructors(){
		ArrayList<String> seenListOfNames = new ArrayList<String>();
		ArrayList<Course> allCourses = new ArrayList<Course>();
		allCourses.addAll(autumnCourses);
		allCourses.addAll(winterCourses);
		allCourses.addAll(springCourses);
		allCourses.addAll(summerCourses);
		for (Course singleCourse : allCourses)
		{
			if(!seenListOfNames.contains(singleCourse.getInstructor()))
			{
				seenListOfNames.add(singleCourse.getInstructor());
			}
		}
		return seenListOfNames;
	}
	/**
	 * Method to update all faculty data in case of a change or save
	 * @return void
	 */
	private void updateFacultyData()
	{
		//remove all data from the faculty data list, will be replaced by updated info
		facultyData = new ArrayList<Faculty>();
		//create new list to hold all courses and add all courses to the list
		ArrayList<Course> allCourses = new ArrayList<Course>();
		allCourses.addAll(autumnCourses);
		allCourses.addAll(winterCourses);
		allCourses.addAll(springCourses);
		allCourses.addAll(summerCourses);
		// array list to track which instructor names have been seen
		ArrayList<String> seenListOfNames = new ArrayList<String>();
		// iterate through all courses to create updated faculty data
		for (Course singleCourse : allCourses)
		{
			// if the name has been seen, find the corresponding instructor in facultyData, add course to that professor
			if(seenListOfNames.contains(singleCourse.getInstructor()))
			{
				for (Faculty singleInstructor : facultyData)  // iterate through facultyData to find the professor
				{
					if (singleInstructor.getName().equalsIgnoreCase(singleCourse.getInstructor()))
					{
						singleInstructor.addCourse(singleCourse);
					}
				}
			}
			// name has not been seen, create new instructor in facultyData, add course to that professor
			else
			{
				// add name to seen list, add faculty member to faculty data
				seenListOfNames.add(singleCourse.getInstructor());
				facultyData.add(new Faculty(singleCourse.getInstructor()));
				// iterate over faculty data to find instructor, add course to that instructor
				for (Faculty singleInstructor : facultyData)
				{
					if (singleInstructor.getName().equalsIgnoreCase(singleCourse.getInstructor()))
					{
						singleInstructor.addCourse(singleCourse);
					}
				}
			}
		}
	}

	/**
	 * Method to get course percentages and return them in array form
	 * @return float array of percentages
	 */
	public float[] getPercentages(String quarter)
	{
		//create new percentage calculator and result array to hold result
		CourseCalculator calculator = new CourseCalculator();
		float[] result = new float[4];
		// test which class list to calculate percentages for, use those courses
		if(quarter.equalsIgnoreCase("Autumn"))
		{
			result = calculator.calculatePercentages(autumnCourses);
		}
		else if(quarter.equalsIgnoreCase("Winter"))
		{
			result = calculator.calculatePercentages(winterCourses);
		}
		else if(quarter.equalsIgnoreCase("Spring"))
		{
			result = calculator.calculatePercentages(springCourses);
		}
		else if(quarter.equalsIgnoreCase("Summer"))
		{
			result = calculator.calculatePercentages(summerCourses);
		}
		//return the calculation of percentages, stored in array format by days
		return result;
	}

	/**
	 * Setter/Mutator methods for all schedule related data
	 * return void
	 */

	public void setAutumnCourses(ArrayList<Course> autumnCourses) {
		this.autumnCourses = autumnCourses;
	}

	public void setWinterCourses(ArrayList<Course> winterCourses) {
		this.winterCourses = winterCourses;
	}

	public void setSpringCourses(ArrayList<Course> springCourses) {
		this.springCourses = springCourses;
	}

	public void setSummerCourses(ArrayList<Course> summerCourses) {
		this.summerCourses = summerCourses;
	}

	public void setFacultyData(ArrayList<Faculty> facultyData) {
		this.facultyData = facultyData;
	}

	public void setAllConflicts(ArrayList<Conflict> allConflicts) {
		this.allConflicts = allConflicts;
	}

	/**
	 * Accessor methods for each data member
	 * return desired data member
	 */

	public ArrayList<Course> getAutumnCourses() {
		return autumnCourses;
	}

	public ArrayList<Course> getWinterCourses() {
		return winterCourses;
	}

	public ArrayList<Course> getSpringCourses() {
		return springCourses;
	}

	public ArrayList<Course> getSummerCourses() {
		return summerCourses;
	}

	public ArrayList<Faculty> getFacultyData() {
		return facultyData;
	}
}
