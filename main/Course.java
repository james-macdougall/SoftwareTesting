package main;

/**
 * 
 * @author Group 2
 * COMP3381 - Course Scheduling Application
 * Course
 * 11/12/2015
 * 
 * Represents a Course and all of its associated values in string form
 *
 */
public class Course 
{
	private String quarter, year, type, crn, title, name, hrs, medium, section, 
	seat, en, instructor, day, begin, end, fin, room, cap, crossList, 
	secText, notes;

	/**
	 * Constructor for a course, takes in all possible course-related values
	 * @param quarter1
	 * @param year1
	 * @param type1
	 * @param crn1
	 * @param title1
	 * @param name1
	 * @param hrs1
	 * @param medium1
	 * @param section1
	 * @param seat1
	 * @param en1
	 * @param instructor1
	 * @param day1
	 * @param begin1
	 * @param end1
	 * @param fin1
	 * @param room1
	 * @param cap1
	 * @param crossList1
	 * @param secText1
	 * @param notes1
	 */
	public Course(String quarter1, String year1, String type1, String crn1, String title1, String name1, 
			String hrs1, String medium1, String section1, String seat1, 
			String en1, String instructor1, String day1, String begin1,
			String end1, String fin1, String room1, String cap1,
			String crossList1, String secText1, String notes1)
	{
		type = type1;
		crn = crn1;
		title = title1;
		name = name1;
		hrs = hrs1;
		medium = medium1;
		section = section1;
		seat = seat1;
		en = en1;
		instructor = instructor1;
		day = day1;
		begin = begin1;
		end = end1;
		fin = fin1;
		room = room1;
		cap = cap1;
		crossList = crossList1;
		secText = secText1;
		notes = notes1;
		quarter = quarter1;
		year = year1;
	}

	/**
	 * Accessors and Mutators for all data members
	 * return desired data member
	 */
	
	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCrn() {
		return crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String course) {
		this.title = course;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHrs() {
		return hrs;
	}

	public void setHrs(String hrs) {
		this.hrs = hrs;
	}

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}

	public String getEn() {
		return en;
	}

	public void setEn(String en) {
		this.en = en;
	}

	public String getInstructor() {
		return instructor;
	}

	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getBegin() {
		return begin;
	}

	public void setBegin(String begin) {
		this.begin = begin;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getFin() {
		return fin;
	}

	public void setFin(String fin) {
		this.fin = fin;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getCrossList() {
		return crossList;
	}

	public void setCrossList(String crossList) {
		this.crossList = crossList;
	}

	public String getSecText() {
		return secText;
	}

	public void setSecText(String secText) {
		this.secText = secText;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}	
}
