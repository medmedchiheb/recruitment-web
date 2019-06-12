package fr.d2factory.libraryapp.library;

import java.time.Duration;
import java.time.LocalDate;
import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.member.Member;

/**
 * 
 * @author benjemaam
 *
 */
public class LibraryService implements Library {

	BookRepository bookRepository;

	public LibraryService() {
		this.bookRepository = BookRepository.getInstance();
	}

	@Override
	public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException {

		if (member.hasLateBooks())
			throw new HasLateBooksException();

		if (this.bookRepository.isBookAvailable(isbnCode)) {
			Book bookToBorrow = bookRepository.findBook(isbnCode);
			this.bookRepository.saveBookBorrow(bookToBorrow, borrowedAt);
			member.addBookBorrow(bookToBorrow, borrowedAt);
			return bookToBorrow;
		}

		return null;
	}

	@Override
	public void returnBook(Book book, Member member) {
		if (bookRepository.isBookAvailableOnBorrowedBooks(book)) {

			LocalDate borrowedAt = bookRepository.findBorrowedBookDate(book);
			int numberOfDays = (int) Duration.between(LocalDate.now(), borrowedAt).toDays();
			member.payBook(numberOfDays);
			this.bookRepository.returnBook(book);
			member.returnBookBorrow(book);

		}

	}

}
