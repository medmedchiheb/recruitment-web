package fr.d2factory.libraryapp.member;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;

import fr.d2factory.libraryapp.LibraryProperties;

/**
 * 
 * @author benjemaam
 *
 */
public class Student extends Member {

	public Student() {
		super();
	}

	public Student(LocalDate registeredDate) {
		super(registeredDate);
	}

	public Student(LocalDate registeredDate, float wallet) {
		super(registeredDate, wallet);
	}

	@Override
	public void payBook(int numberOfDays) {

		float amount = 0;

		if (isMembershipFirstYear() && numberOfDays <= 15)
			return;

		if (numberOfDays <= LibraryProperties.getInstance().getAllowedDaysStudents()) {
			amount += numberOfDays * LibraryProperties.getInstance().getCostPerDay();
		} else {
			amount += LibraryProperties.getInstance().getAllowedDaysStudents()
					* LibraryProperties.getInstance().getCostPerDay();
			amount += (numberOfDays - LibraryProperties.getInstance().getAllowedDaysStudents())
					* LibraryProperties.getInstance().getLateStudentsCost();
		}

		if (isMembershipFirstYear())
			amount -= LibraryProperties.getInstance().getAllowedFreeDaysStudents()
					* LibraryProperties.getInstance().getCostPerDay();

		this.setWallet(this.getWallet() - amount);

	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	public boolean isMembershipFirstYear() {
		return Period.between(LocalDate.now(), this.getMembershipDate()).toTotalMonths() <= 12;

	}

	@Override
	public boolean hasLateBooks() {
		
		return this.getBorrowedBooks()
        .values().stream()
        .filter(ld -> {
        	return Duration.between(LocalDate.now(), ld).toDays() > LibraryProperties.getInstance().getAllowedDaysStudents();
        })
        .count() >= 1;
	}

}
