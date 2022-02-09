package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * @author Group 2
 * COMP3381 - Course Scheduling Application
 * InfoScanner
 * 11/12/2015
 * 
 * Reads in and parses data from a specified CSV which follows a conventional format of
 * 	having some value in every column, even if just whitespace. Otherwise, this will not
 * 	properly parse the data. The data is stored in two ArrayLists of respective types.
 * 	4 array lists of type course for each quarter are parsed out of the data.
 *
 */
public class InfoScanner 
{
	// instance variables to hold each quarter's courses
	private ArrayList<Course> autumnCourses = new ArrayList<Course>();
	private ArrayList<Course> winterCourses = new ArrayList<Course>();
	private ArrayList<Course> springCourses = new ArrayList<Course>();
	private ArrayList<Course> summerCourses = new ArrayList<Course>();
	private ArrayList<Faculty> facultyData  = new ArrayList<Faculty>(); // Holds all faculty personnel

	/**
	 * InfoScanner constructor, takes in file name to be scanned/parsed
	 * @param fileName file to be scanned
	 * @throws IOException 
	 */
	public InfoScanner(String fileName)
	{
		readCourseFile(fileName);
	}

	/**
	 * Method to actually perform the scanning and parsing
	 * @param fileName file to be scanned
	 * @throws IOException 
	 */
	private void readCourseFile(String fileName)
	{
		// given the file IO nature, surround in try/catch block
		try 
		{
			ArrayList<Course> scannedCourses = new ArrayList<Course>(); // temporary list for scanned courses
			// create scanner, set it's delimiter to comma, start scanning loop
			Scanner scan = new Scanner(new File(fileName));
			scan.useDelimiter(",");
			while(scan.hasNextLine())  // Continue until EOF, grab all pieces of data
			{
				scannedCourses.add(new Course(scan.next(), scan.next(), scan.next(), scan.next(), scan.next(), scan.next(), scan.next(),
						scan.next(), scan.next(), scan.next(), scan.next(), scan.next(), scan.next(), 
						scan.next(), scan.next(), scan.next(), scan.next(), scan.next(), scan.next(), 
						scan.next(), scan.next()));
				scan.nextLine();  //move scanner to next line
			}
			scan.close();

			//call parsing functions to extract useful information from scanned data
			parseCourseData(scannedCourses);
			parseFacultyData(scannedCourses);
		} 
		// Bad file input, inform user and terminate program.
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Function to extract valuable data from scanned information
	 * @param scannedCourses list of course data just scanned
	 */
	private void parseCourseData(ArrayList<Course> scannedCourses)
	{
		// iterate through all values and appropriately place them in an ArrayList
		for (Course singleCourse : scannedCourses)
		{
			if (singleCourse.getQuarter().equalsIgnoreCase("Autumn"))
			{
				autumnCourses.add(singleCourse);
			}
			else if (singleCourse.getQuarter().equalsIgnoreCase("Winter"))
			{
				winterCourses.add(singleCourse);
			}
			else if (singleCourse.getQuarter().equalsIgnoreCase("Spring"))
			{
				springCourses.add(singleCourse);
			}
			else if (singleCourse.getQuarter().equalsIgnoreCase("Summer"))
			{
				summerCourses.add(singleCourse);
			}
			else  /** Error. Handles a course that does not have a quarter value. **/
			{
				System.out.println("Parsing Error: Did not find a quarter value for: " + singleCourse.getTitle() + " taught by " + singleCourse.getInstructor());
			}
		}
	}

	/**
	 * Method to parse valuable faculty data from the scanned information
	 * @param scannedCourses list of course information just scanned
	 */
	private void parseFacultyData(ArrayList<Course> scannedCourses)
	{
		ArrayList<String> seenListOfNames = new ArrayList<String>(); // array list to track which instructor names have been seen
		// iterate through all scanned courses to create valuable faculty data
		for (Course singleCourse : scannedCourses)
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
	 * Accessors/Getters for all data members
	 * return desired data structure
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
