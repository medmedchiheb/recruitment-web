package fr.d2factory.libraryapp.member;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.library.Library;

/**
 * A member is a person who can borrow and return books to a {@link Library} A
 * member can be either a student or a resident
 */
public abstract class Member {

	private int membershipId;

	// date when the member registered
	private LocalDate membershipDate;
	/**
	 * An initial sum of money the member has
	 */
	private float wallet;

	protected Map<Book, LocalDate> borrowedBooks = new HashMap<>();

	public Member() {
		this.membershipId = MembershipIdHelper.nextId();
		this.membershipDate = LocalDate.now();
		this.wallet = 0;
	}

	public Member(LocalDate registeredDate) {
		this.membershipId = MembershipIdHelper.nextId();
		this.membershipDate = registeredDate;
		this.wallet = 0;
	}

	public Member(LocalDate registeredDate, float wallet) {
		this.membershipId = MembershipIdHelper.nextId();
		this.membershipDate = registeredDate;
		this.wallet = wallet;
	}

	/**
	 * The member should pay their books when they are returned to the library
	 *
	 * @param numberOfDays the number of days they kept the book
	 */
	public abstract void payBook(int numberOfDays);

	public float getWallet() {
		return wallet;
	}

	public void setWallet(float wallet) {
		this.wallet = wallet;
	}

	public int getMembershipId() {
		return membershipId;
	}

	public void setMembershipId(int membershipId) {
		this.membershipId = membershipId;
	}

	public LocalDate getMembershipDate() {
		return membershipDate;
	}

	public void setMembershipDate(LocalDate membershipDate) {
		this.membershipDate = membershipDate;
	}

	public Map<Book, LocalDate> getBorrowedBooks() {
		return borrowedBooks;
	}

	public void setBorrowedBooks(Map<Book, LocalDate> borrowedBooks) {
		this.borrowedBooks = borrowedBooks;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + membershipId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Member other = (Member) obj;
		if (membershipId != other.membershipId)
			return false;
		return true;
	}

	public void addBookBorrow(Book book, LocalDate borrowedAt) {
		this.borrowedBooks.put(book, borrowedAt);
	}

	public void returnBookBorrow(Book book) {
		this.borrowedBooks.remove(book);
	}

	public abstract boolean hasLateBooks();

}
