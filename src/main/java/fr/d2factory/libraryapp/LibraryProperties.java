package fr.d2factory.libraryapp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @author benjemaam
 *
 */

public class LibraryProperties {
	private int allowedDaysStudents;
	private int allowedDaysResidents;
	private float costPerDay;
	private float lateStudentsCost;
	private float lateResidentsCost;
	private int allowedFreeDaysStudents;

	private static LibraryProperties instance;

	public static synchronized LibraryProperties getInstance() {
		if (instance == null)
			instance = new LibraryProperties();
		return instance;
	}

	private LibraryProperties() {
		readProps();
	}

	/*
	 * read library properties
	 */
	private void readProps() {
		try (InputStream input = new FileInputStream("src/main/java/resources/library.properties")) {

			Properties prop = new Properties();

			prop.load(input);

			this.allowedDaysStudents = Integer.parseInt(prop.getProperty("library.allowed-days-students"));
			this.allowedDaysResidents = Integer.parseInt(prop.getProperty("library.allowed-days-residents"));
			this.costPerDay = Float.parseFloat(prop.getProperty("library.cost-perday"));
			this.lateStudentsCost = Float.parseFloat(prop.getProperty("library.cost-perday.late-students"));
			this.lateResidentsCost = Float.parseFloat(prop.getProperty("library.cost-perday.late-residents"));
			this.allowedFreeDaysStudents = Integer
					.parseInt(prop.getProperty("library.free-days-book.students.allowed.first-year"));

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public int getAllowedDaysStudents() {
		return allowedDaysStudents;
	}

	public void setAllowedDaysStudents(int allowedDaysStudents) {
		this.allowedDaysStudents = allowedDaysStudents;
	}

	public int getAllowedDaysResidents() {
		return allowedDaysResidents;
	}

	public void setAllowedDaysResidents(int allowedDaysResidents) {
		this.allowedDaysResidents = allowedDaysResidents;
	}

	public float getCostPerDay() {
		return costPerDay;
	}

	public void setCostPerDay(float costPerDay) {
		this.costPerDay = costPerDay;
	}

	public float getLateStudentsCost() {
		return lateStudentsCost;
	}

	public void setLateStudentsCost(float lateStudentsCost) {
		this.lateStudentsCost = lateStudentsCost;
	}

	public float getLateResidentsCost() {
		return lateResidentsCost;
	}

	public void setLateResidentsCost(float lateResidentsCost) {
		this.lateResidentsCost = lateResidentsCost;
	}

	public int getAllowedFreeDaysStudents() {
		return allowedFreeDaysStudents;
	}

	public void setAllowedFreeDaysStudents(int allowedFreeDaysStudents) {
		this.allowedFreeDaysStudents = allowedFreeDaysStudents;
	}

}
