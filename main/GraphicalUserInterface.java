package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author Group 2
 * COMP3381 - Course Scheduling Application
 * GraphicalUserInterface
 * 11/12/2015
 * 
 * This class is the launcher. You execute the program using this class.
 * Displays all information through the GUI and allows user to interact with the application.
 * Saves all information appropriately and when specified by user.
 *
 *
 */
public class GraphicalUserInterface {

	// create and instantiate instance variables used to create, modify, and monitor the GUI
	private static String[] columnNames = {"Quarter","Year","Type","CRN","Course Number", 
		"Course Name", "Hrs","Course Type","Section","Seat","En","Instructor", "Days","Begin",
		"End","Final","Room","Room Cap","Cross List","Section Text","Notes" };
	private static JTable table, table1;
	private static ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
	private static DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
	private static JPanel panel;
	private static JFrame frame;
	private static JScrollPane ConflictPanel;
	private static int hide = 0;
	private static ScheduleData allData;
	private static String quarterToDisplay = "Autumn"; //autumn is default
	private static String[] columnNamesConflict = {"Type","Courses","Day","Room","Time","Professor"};
	private static DefaultTableModel tableModelConflict = new DefaultTableModel(columnNamesConflict, 0);
	private static ArrayList<Object[]> conflicts = new ArrayList<Object[]>();


	/**
	 * Main method of the program application
	 * @throws IOException 
	 */
	public static void main(String[] args) 
	{
		// create new file chooser object, allowing user to select only CSV file formats
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV file", "csv");
		chooser.setFileFilter(filter);
		// allow user to browse through file directories and prompt them for a selection, set file path to empty
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Please choose the csv file you would like to upload classes from.");
		String filePath = "";
		// test if a valid file is selected, if so then create full file path from it. Else, exit program
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
		{
			File file1 = chooser.getSelectedFile();
			String afilePath = file1.getAbsolutePath();
			filePath = afilePath.replace('\\', '/');
		}
		else
		{
			//NOTE: System exits if file is not chosen because there is no file to upload from
			System.exit(1);
		}

		// scan in data from the file
		InfoScanner scan = new InfoScanner(filePath);
		// create a new "database" of the schedule, USE THIS TO ACCESS DATA (do not access from elsewhere)
		allData = new ScheduleData(scan.getAutumnCourses(), scan.getWinterCourses(), scan.getSpringCourses(), 
				scan.getSummerCourses(), scan.getFacultyData(), filePath);
		// try a save, just to ensure saving is functioning properly (ensure no loss of data during use)
		allData.saveDataToFile();
		//allData.saveDataToFile();
		// create outline of GUI and enter command loop
		frame = new JFrame("Course Scheduling");
		commandLoop(allData, "Autumn");
	}

	/**
	 * Method initializes the GUI and sets up panels and buttons. Note that the default 
	 * 		quarter shown is the Autumn Quarter
	 * @param db containing information about all classes available
	 * @param quarterToDisplay string representation of the quarter
	 */
	private static void commandLoop(ScheduleData db, String quarterToDisplay)
	{
//		ArrayList<Object[]> conflicts = db.getConflictsArray(quarterToDisplay);
//		for (int i = 0; i < conflicts.size(); i++)
//		{
//			tableModelConflict.addRow(conflicts.get(i));
//		}

		table1 = new JTable(tableModelConflict);

		ConflictPanel = new JScrollPane(table1);

		//Add Buttons to the Button Panel
		JPanel ButtonPanel = new JPanel();

		ButtonPanel = new JPanel();
		//addClassButton
		JButton addClassButton = new JButton("Add Class");
		addClassButton.addActionListener(new AddClassButtonListener());
		ButtonPanel.add(addClassButton);
		//DeleteClassButton
		JButton deleteClassButton = new JButton("Delete Class");
		deleteClassButton.addActionListener(new DeleteClassButtonListener());
		ButtonPanel.add(deleteClassButton);
		//ConflictsButton
		JButton conflictsButton = new JButton("View/Hide Conflicts");
		conflictsButton.addActionListener(new ConflictsButtonListener());
		ButtonPanel.add(conflictsButton);
		//ConflictsButton
		JButton selectQuarterButton = new JButton("View By Quarter");
		selectQuarterButton.addActionListener(new SelectQuarterListener());
		ButtonPanel.add(selectQuarterButton);
		//ProfessorButton
		JButton professorButton = new JButton("Faculty Info");
		professorButton.addActionListener(new ProfessorButtonListener());
		ButtonPanel.add(professorButton);
		//statisticsButton
		JButton statButton = new JButton("Quarter Statistics");
		statButton.addActionListener(new StatButtonListener());
		ButtonPanel.add(statButton);
		//saveButton
		JButton saveButton = new JButton("<html><font color='blue'> Save </font></html>");
		saveButton.addActionListener(new SaveButtonListener());
		ButtonPanel.add(saveButton);


		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(1300, 710));
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		quarterToDisplay = "Autumn";
		changeAndUpdateTable(quarterToDisplay);

		table = new JTable(tableModel);
		//the table listener
		table.getTableHeader().setReorderingAllowed(false);
		table.getModel().addTableModelListener(new TableModelListener() {

			public void tableChanged(TableModelEvent e) {
				//System.out.println(e);
				if(e.getType() == TableModelEvent.UPDATE){
					int row = e.getFirstRow();
					int col = e.getColumn();
					String val = table.getModel().getValueAt(row, col).toString();
					//table.setValueAt(val, row, col);
					//data[row][col] = val;
					data.get(row).set(col, val);
					
					// EDITS START HERE----------------------
					System.out.println("The data1 value = "+ val+ " row = "+row+" col = "+col);
					
					ArrayList<String> names = allData.getListInstructors(); //added the function getInstructors to ScheduleData
					if(col == 11) { //instructor validity test
						if (!names.contains(val)) {
							System.out.println("Error");
							JFrame theframe = new JFrame();
							JOptionPane.showMessageDialog(theframe, "New Instructor Warning!\nSave to Update!");							
						}
					}
					if(col == 12) { //days of the week
						ArrayList<String> valids = new ArrayList<String>(Arrays.asList("", "MW","TR","WF","MWF","F")); //These I left as preset, I did pull from file and update for instructor & room
						if (!valids.contains(val)) {
							System.out.println("Error");
							JFrame theframe = new JFrame();
							JOptionPane.showMessageDialog(theframe, "Invalid Day! Use MW, TR, WF, MWF, F");
						}
					}
					ArrayList<String> rooms = allData.getListRooms(); //added the function getInstructors to ScheduleData
					if(col == 16) { 
						if (!rooms.contains(val)) {
							System.out.println("Error");
							JFrame theframe = new JFrame();
							JOptionPane.showMessageDialog(theframe, "New Rooms Warning!\nSave to Update Rooms!");							
						}
					}
				}
			}

		});

		//DUCS IMAGE: Cannot use with JAR file :(
		JPanel imgPan = new JPanel();
//		ImageIcon myPicture = null;
//		myPicture = new ImageIcon("ducs.jpg");
//		JLabel picLabel = new JLabel(myPicture);
//		picLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		JPanel formPan = new JPanel();
		formPan.setLayout(new BoxLayout(formPan, BoxLayout.X_AXIS));
		JPanel rPan = new JPanel();
		rPan.setBackground(Color.decode("#660000"));
		formPan.setBackground(Color.decode("#660000"));

		imgPan.setLayout(new BoxLayout(imgPan, BoxLayout.Y_AXIS));
		imgPan.setBackground(Color.decode("#660000"));
		JLabel universityLabel = new JLabel("<html><font color='white'> University of Denver - DUCS </font></html>");
		universityLabel.setFont(new Font("Verdana",Font.BOLD,20));
		universityLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		JLabel nameLabel = new JLabel("<html><font color='white'> Course Scheduling Application </font></html>");
		nameLabel.setFont(new Font("Verdana",Font.BOLD,25));
		nameLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		//imgPan.add(picLabel);
		imgPan.add(nameLabel);
		imgPan.add(universityLabel);
		formPan.add(rPan);
		formPan.add(imgPan);
		
		panel.add(formPan);


		JScrollPane tableContainer = new JScrollPane(table);

		panel.add(ButtonPanel);
		panel.add(tableContainer);
		if(conflicts.size() != 0){
			panel.add(ConflictPanel);
			hide = 1;
		}

		frame.getContentPane().add(panel);

		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Method updates GUI with desired quarter information
	 * @param quarterToDisplay string representation of the quarter
	 */
	private static void changeAndUpdateTable(String quarterToDisplay){

		tableModel.setRowCount(0);
		tableModelConflict.setRowCount(0);
		data.clear();
		
		conflicts.clear();
		conflicts = allData.getConflictsArray(quarterToDisplay);
		for (int i = 0; i < conflicts.size(); i++)
		{
			tableModelConflict.addRow(conflicts.get(i));
		}

		ArrayList<Course> courseList;
		if(quarterToDisplay.equalsIgnoreCase("Autumn")){courseList = allData.getAutumnCourses();}
		else if(quarterToDisplay.equalsIgnoreCase("Winter")){courseList = allData.getWinterCourses();}
		else if(quarterToDisplay.equalsIgnoreCase("Spring")){courseList = allData.getSpringCourses();}
		else if(quarterToDisplay.equals("Summer")){courseList = allData.getSummerCourses();}
		else{courseList = allData.getAutumnCourses();}

		for (int i = 0 ; i < courseList.size(); i++)
		{
			Object[] objs = createCourseObject(courseList.get(i));//create the Object list for each class
			//			System.out.println("CL = "+courseList.get(i).getQuarter());
			tableModel.addRow(objs);//add to the table
			ArrayList<String> temp = new ArrayList<String>(21);
			for(int k = 0; k < 21; k++){
				String str = objs[k].toString();
				//System.out.println("The Element = "+str);
				temp.add(str);
			}
			data.add(temp);//add to the data so that we can keep track of changes via the listener
		}
		frame.setVisible(true);
	}

	/**
	 * Method creates a object[] that will be added to the table list.
	 * @param course object with information about course
	 */
	public static Object[] createCourseObject(Course course)
	{
		Object[] objs = {course.getQuarter() , course.getYear() ,course.getType() ,
				course.getCrn() , course.getTitle() , course.getName() ,
				course.getHrs() , course.getMedium() , course.getSection() ,
				course.getSeat() , course.getEn() , course.getInstructor() ,
				course.getDay() , course.getBegin() ,course.getEnd() ,
				course.getFin() , course.getRoom() , course.getCap(),
				course.getCrossList() , course.getSecText() , course.getNotes()};
		return objs;
	}

	/**
	 * The following inner classes are button listeners. The show output of update GUI when pressed
	 * 
	 */
	private static class DeleteClassButtonListener implements ActionListener{
		private static int row;
		private static JFrame aframe;
		public void actionPerformed(ActionEvent ev){
			row = table.getSelectedRow();
			if(row != -1){
				JPanel bPanel = new JPanel();
				JPanel apanel = new JPanel();
				apanel.setLayout(new BoxLayout(apanel, BoxLayout.Y_AXIS));
				JLabel label = new JLabel("Are you sure you want to delete this class? ");
				JLabel label1 = new JLabel("COMP "+ table.getModel().getValueAt(row, 4)+" "+table.getModel().getValueAt(row, 5));
				JLabel label2 = new JLabel("CRN: "+ table.getModel().getValueAt(row, 3));
				label.setFont(new Font("Verdana",1,15));
				apanel.add(label);
				apanel.add(label1);
				apanel.add(label2);
				//yesButton
				JButton yesButton = new JButton("Yes");
				yesButton.addActionListener(new YesButtonListener());
				bPanel.add(yesButton);
				//noButton
				JButton noButton = new JButton("No");
				noButton.addActionListener(new NoButtonListener());
				bPanel.add(noButton);
				aframe = new JFrame("Delete Class");
				aframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				aframe.setPreferredSize(new Dimension(500, 300));
				apanel.add(bPanel);
				aframe.getContentPane().add(apanel);
				aframe.pack();
				aframe.setVisible(true);
			}
		}
		private static class YesButtonListener implements ActionListener{
			public void actionPerformed(ActionEvent ev){
				data.remove(row);
				tableModel.removeRow(row);
				allData.saveData(data, quarterToDisplay);
				
				//re-find conflicts and display them
				tableModelConflict.setRowCount(0);
				conflicts.clear();
				conflicts = allData.getConflictsArray(quarterToDisplay);
				for (int i = 0; i < conflicts.size(); i++)
				{
					tableModelConflict.addRow(conflicts.get(i));
				}
				
				aframe.dispose();
			}

		}
		private static class NoButtonListener implements ActionListener{
			public void actionPerformed(ActionEvent ev){
				aframe.dispose();
			}
		}
	}

	private static class AddClassButtonListener implements ActionListener{
		private static JFrame theframe;
		private static JTextField textfeild;
		private static JPanel main;
		public void actionPerformed(ActionEvent ev){
			theframe = new JFrame();
			main = new JPanel();
			main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
			JLabel label = new JLabel("Please give this class a name. ");
			label.setFont(new Font("Verdana",1,15));
			textfeild = new JTextField( 7 );
			label.setAlignmentX(JComponent.CENTER_ALIGNMENT);
			textfeild.setAlignmentX(JComponent.CENTER_ALIGNMENT);
			main.add(label);
			main.add(textfeild);
			//addButton
			JButton addButton = new JButton("Add");
			addButton.addActionListener(new addButtonListener());
			addButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
			main.add(addButton);

			theframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			theframe.setPreferredSize(new Dimension(500, 300));
			theframe.getContentPane().add(main);
			theframe.pack();
			theframe.setVisible(true);
		}
		private static class addButtonListener implements ActionListener{
			public void actionPerformed(ActionEvent ev){
				String name;
				name = textfeild.getText();
				JLabel labelError = new JLabel("<html><font color='red'> Class Name Cannot Be Empty!</font></html>");
				if(name.equals("")){
					labelError.setAlignmentX(JComponent.CENTER_ALIGNMENT);
					main.add(labelError);
					theframe.setVisible(true);
				}
				else{
					Object[] newClass = {quarterToDisplay, "", "", "", "", name,"","","","","","","","","","","","","",""};
					ArrayList<String> temp = new ArrayList<String>(21);
					temp.add(quarterToDisplay);
					for(int i = 1; i < 21; i++){
						if(i==5){
							temp.add(name);
						}
						else{
							temp.add("");
						}
					}
					//NEED TO REMOVE THIS CLASS FROM ALL DATA AS WELL
					data.add(0, temp);
					tableModel.insertRow(0, newClass);
					allData.saveData(data, quarterToDisplay);
					
					//re-find conflicts and display them
					tableModelConflict.setRowCount(0);
					conflicts.clear();
					conflicts = allData.getConflictsArray(quarterToDisplay);
					for (int i = 0; i < conflicts.size(); i++)
					{
						tableModelConflict.addRow(conflicts.get(i));
					}
					
					theframe.dispose();
				}
			}
		}
	}

	private static class ConflictsButtonListener implements ActionListener{

		public void actionPerformed(ActionEvent ev){
			if(hide == 0 ){
				//NOTE: we probably don't need the following line here because things are saved when class is added and deleted. But included for safety.
				allData.saveData(data, quarterToDisplay);
				//re-find conflicts and display them
				tableModelConflict.setRowCount(0);
				conflicts.clear();
				conflicts = allData.getConflictsArray(quarterToDisplay);
				for (int i = 0; i < conflicts.size(); i++)
				{
					tableModelConflict.addRow(conflicts.get(i));
				}
				
				panel.add(ConflictPanel);
				hide = 1;
				frame.setVisible(true);

			}
			else {
				panel.remove(ConflictPanel);
				hide = 0;
				frame.setVisible(true);
			}
		}
	}

	private static class ProfessorButtonListener implements ActionListener{
		private static JFrame theframe;
		private static JPanel main, rightPanel, leftPanel;
		private static JScrollPane lPanel;
		private static JList facultyList;
		private static ArrayList<Faculty> faculty;
		public void actionPerformed(ActionEvent ev){
			theframe = new JFrame();
			main = new JPanel();
			main.setLayout(new BoxLayout(main, BoxLayout.X_AXIS));
			rightPanel = new JPanel();
			leftPanel = new JPanel();
			leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
			leftPanel.setSize(new Dimension(500, 900));

			rightPanel.setSize(new Dimension(500, 20));
			rightPanel.setLayout(new BorderLayout());

			allData.saveData(data, quarterToDisplay);

			faculty = allData.getFacultyData();
			String [] listData = new String[faculty.size()];
			for(int i = 0; i < faculty.size(); i ++){
				listData[i] = faculty.get(i).getName();
			}

			facultyList = new JList(listData);
			facultyList.setLayoutOrientation(JList.VERTICAL);
			facultyList.addListSelectionListener(new FacultyListListener());
			updateProfInfo(0);

			rightPanel.add(facultyList, BorderLayout.CENTER);
			lPanel = new JScrollPane(leftPanel);
			//rPanel = new JScrollPane(rightPanel);
			lPanel.setPreferredSize(new Dimension(500, 800));
			main.add(rightPanel);
			main.add(lPanel);

			theframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			theframe.setPreferredSize(new Dimension(800, 500));
			theframe.getContentPane().add(main);
			theframe.pack();
			theframe.setVisible(true);
		}

		private static void updateProfInfo(int ind){
			leftPanel.removeAll();
			//save data to get latest stats
			allData.saveData(data, quarterToDisplay);
			JLabel name = new JLabel(""+ faculty.get(ind).getName());
			name.setFont(new Font("Verdana",1,16));
			JLabel head1 = new JLabel("Number Courses Teaching");
			JLabel head2 = new JLabel("Course Load Info");
			head1.setFont(new Font("Verdana",1,12));
			head2.setFont(new Font("Verdana",1,12));
			JLabel autLabel = new JLabel("Autumn Quarter: " + faculty.get(ind).getLoad("Autumn"));
			JLabel winLabel = new JLabel("Winter Quarter: " + faculty.get(ind).getLoad("Winter"));
			JLabel sprLabel = new JLabel("Spring Quarter: " + faculty.get(ind).getLoad("Spring"));
			JLabel sumLabel = new JLabel("Summer Quarter: " + faculty.get(ind).getLoad("Summer"));

			ArrayList<Course> tempList = faculty.get(ind).getCoursesTaught();
			String aString = "<html> Quarter Year CRN  Course Number  Course Name  Section  Days  Begin  End  Room <br>";

			for(int i = 0; i < tempList.size(); i++){
				aString += " 	"+(i+1)+")";
				aString += " "+tempList.get(i).getQuarter();
				aString += ", "+tempList.get(i).getYear();
				aString += ", "+tempList.get(i).getCrn();
				aString += ", "+tempList.get(i).getTitle();
				aString += ", "+tempList.get(i).getName();
				aString += ", "+tempList.get(i).getSection();
				aString += ", "+tempList.get(i).getDay();
				aString += ", "+tempList.get(i).getBegin();
				aString += ", "+tempList.get(i).getEnd();
				aString += ", "+tempList.get(i).getRoom();
				aString += "<br>";
			}
			aString += " </html>";
			JLabel space = new JLabel("			");
			JLabel space1 = new JLabel("			");
			JLabel classList = new JLabel(aString);
			JPanel ww = new JPanel();
			ww.setLayout(new BoxLayout(ww, BoxLayout.Y_AXIS));

			leftPanel.add(name);
			leftPanel.add(space1);
			leftPanel.add(head1);
			leftPanel.add(autLabel);
			leftPanel.add(winLabel);
			leftPanel.add(sprLabel);
			leftPanel.add(sumLabel);
			leftPanel.add(space);
			leftPanel.add(head2);
			leftPanel.add(classList);
			theframe.setVisible(true);
		}

		private static class FacultyListListener implements ListSelectionListener{

			public void valueChanged(ListSelectionEvent e) {
				if(facultyList.getSelectedIndex() != -1){
					updateProfInfo(facultyList.getSelectedIndex());
				}
			}
		}
	}

	private static class StatButtonListener implements ActionListener{
		private static JFrame theframe;
		private static JPanel main;
		public void actionPerformed(ActionEvent ev){
			theframe = new JFrame();
			main = new JPanel();
			main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

			//save data to get latest stats
			allData.saveData(data, quarterToDisplay);

			float[] percentages = allData.getPercentages(quarterToDisplay);
			JLabel label = new JLabel("    Quarter Statistics");
			label.setFont(new Font("Verdana",1,15));
			label.setAlignmentX(JComponent.CENTER_ALIGNMENT);
			JLabel label1 = new JLabel("          % on Monday/Wednesday = "+percentages[0]);
			label1.setAlignmentX(JComponent.CENTER_ALIGNMENT);
			JLabel label2 = new JLabel("          % on Tuesday/Thursday = "+percentages[1]);
			label2.setAlignmentX(JComponent.CENTER_ALIGNMENT);
			JLabel label3 = new JLabel("          % on Tuesday/Friday = "+percentages[2]);
			label3.setAlignmentX(JComponent.CENTER_ALIGNMENT);
			JLabel label4 = new JLabel("          % on Wednesday/Friday = "+percentages[3]);
			label4.setAlignmentX(JComponent.CENTER_ALIGNMENT);


			main.add(label);
			main.add(label1);
			main.add(label2);
			main.add(label3);
			main.add(label4);

			theframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			theframe.setPreferredSize(new Dimension(500, 300));
			theframe.getContentPane().add(main);
			theframe.pack();
			theframe.setVisible(true);
		}
	}

	private static class SaveButtonListener implements ActionListener{
		private static JFrame theframe;
		private static JPanel main;
		public void actionPerformed(ActionEvent ev){
			theframe = new JFrame();
			main = new JPanel();
			JLabel label = new JLabel("Please wait while saving...");
			label.setFont(new Font("Verdana",1,15));
			main.add(label);
			theframe.setPreferredSize(new Dimension(500, 300));
			theframe.getContentPane().add(main);
			theframe.pack();
			theframe.setVisible(true);

			allData.saveData(data, quarterToDisplay);
			allData.saveDataToFile();
			//re-find conflicts and display them
			tableModelConflict.setRowCount(0);
			conflicts.clear();
			conflicts = allData.getConflictsArray(quarterToDisplay);
			for (int i = 0; i < conflicts.size(); i++)
			{
				tableModelConflict.addRow(conflicts.get(i));
			}
			
			//allData = new ScheduleData(scan.getAutumnCourses(), scan.getWinterCourses(), scan.getSpringCourses(), 
			//scan.getSummerCourses(), scan.getFacultyData(), filePath);

			frame.setVisible(true);
			label.setText("Saving Complete!");
			theframe.setVisible(true);
			theframe.dispose();
		}
	}

	private static class SelectQuarterListener implements ActionListener{
		private static JFrame aframe;
		public void actionPerformed(ActionEvent ev){
			//JPanel bPanel = new JPanel();
			JPanel aPanel = new JPanel();
			aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.Y_AXIS));
			//aPanel.add(BorderLayout.CENTER);
			JLabel label = new JLabel("Which quarter would you like to view?");
			label.setFont(new Font("Verdana",1,15));
			label.setAlignmentX(JComponent.CENTER_ALIGNMENT);
			aPanel.add(label,BorderLayout.CENTER);

			//AutumnButton
			JButton autumnButton = new JButton(" Autumn ");
			autumnButton.addActionListener(new AutumnButtonListener());
			autumnButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
			aPanel.add(autumnButton);
			//winterButton
			JButton winterButton = new JButton("  Winter  ");
			winterButton.addActionListener(new WinterButtonListener());
			winterButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
			aPanel.add(winterButton);
			//springButton
			JButton springButton = new JButton("  Spring  ");
			springButton.addActionListener(new SpringButtonListener());
			springButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
			aPanel.add(springButton);
			//summerButton
			JButton summerButton = new JButton("Summer");
			summerButton.addActionListener(new SummerButtonListener());
			summerButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
			aPanel.add(summerButton);

			aframe = new JFrame("Select Quarter");
			aframe.setLayout(new BorderLayout());
			aframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			aframe.setPreferredSize(new Dimension(500, 300));
			//apanel.add(bPanel);
			aframe.getContentPane().add(aPanel);
			aframe.pack();
			aframe.setVisible(true);
		}
		private static class AutumnButtonListener implements ActionListener{
			public void actionPerformed(ActionEvent ev){
				allData.saveData(data, quarterToDisplay);//save data with current value of quarterToDisplay
				quarterToDisplay = "Autumn";//change the quarterToDisplay to desired quarter
				changeAndUpdateTable(quarterToDisplay);//show this quarter
				allData.saveData(data, quarterToDisplay);
				updateConflicts();
				aframe.dispose();//close sub frame
			}

		}
		private static class WinterButtonListener implements ActionListener{
			public void actionPerformed(ActionEvent ev){
				allData.saveData(data, quarterToDisplay);
				quarterToDisplay = "Winter";
				changeAndUpdateTable(quarterToDisplay);
				allData.saveData(data, quarterToDisplay);
				updateConflicts();
				aframe.dispose();
			}
		}
		private static class SpringButtonListener implements ActionListener{
			public void actionPerformed(ActionEvent ev){
				allData.saveData(data, quarterToDisplay);
				quarterToDisplay = "Spring";
				changeAndUpdateTable(quarterToDisplay);
				allData.saveData(data, quarterToDisplay);
				updateConflicts();
				aframe.dispose();
			}
		}
		private static class SummerButtonListener implements ActionListener{
			public void actionPerformed(ActionEvent ev){
				allData.saveData(data, quarterToDisplay);
				quarterToDisplay = "Summer";
				changeAndUpdateTable(quarterToDisplay);
				allData.saveData(data, quarterToDisplay);
				updateConflicts();
				aframe.dispose();
			}
		}
		
		private static void updateConflicts(){
			tableModelConflict.setRowCount(0);
			conflicts.clear();
			conflicts = allData.getConflictsArray(quarterToDisplay);
			for (int i = 0; i < conflicts.size(); i++)
			{
				tableModelConflict.addRow(conflicts.get(i));
			}
			frame.setVisible(true);
		}
	}
}
