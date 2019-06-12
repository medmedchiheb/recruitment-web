package fr.d2factory.libraryapp.member;

import java.time.Duration;
import java.time.LocalDate;

import fr.d2factory.libraryapp.LibraryProperties;

/**
 * 
 * @author benjemaam
 *
 */
public class Resident extends Member {

	public Resident() {
		super();
	}

	public Resident(LocalDate registeredDate, float wallet) {
		super(registeredDate, wallet);
	}

	@Override
	public void payBook(int numberOfDays) {
		float amount = 0;

		if (numberOfDays <= LibraryProperties.getInstance().getAllowedDaysResidents()) {
			amount += numberOfDays * LibraryProperties.getInstance().getCostPerDay();
		} else {
			amount += LibraryProperties.getInstance().getAllowedDaysResidents()
					* LibraryProperties.getInstance().getCostPerDay();
			amount += (numberOfDays - LibraryProperties.getInstance().getAllowedDaysStudents())
					* LibraryProperties.getInstance().getLateResidentsCost();
		}

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

	@Override
	public boolean hasLateBooks() {
		
		return this.getBorrowedBooks()
        .values().stream()
        .filter(ld -> {
        	return Duration.between(LocalDate.now(), ld).toDays() > LibraryProperties.getInstance().getAllowedDaysResidents();
        })
        .count() >= 1;
	}

}
