package fr.d2factory.libraryapp.book;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The book repository emulates a database via 2 HashMaps
 */
public class BookRepository {
	private Map<ISBN, Book> availableBooks = new HashMap<>();
	private Map<Book, LocalDate> borrowedBooks = new HashMap<>();

	private static BookRepository instance;

	private BookRepository() {
		availableBooks = new HashMap<>();
		borrowedBooks = new HashMap<>();
	}

	public static synchronized BookRepository getInstance() {
		if (instance == null)
			instance = new BookRepository();
		return instance;
	}

	public void addBooks(List<Book> books) {
		books.forEach(b -> {
			availableBooks.put(b.isbn, b);
		});
	}

	public Book findBook(long isbnCode) {
		if (!isBookAvailable(isbnCode))
			return null;

		return this.availableBooks
				.get(this.availableBooks.keySet().stream().filter(b -> b.isbnCode == isbnCode).findAny().get());
	}

	public void saveBookBorrow(Book book, LocalDate borrowedAt) {
		if (isBookAvailable(book.isbn.isbnCode)) {
			borrowedBooks.put(book, borrowedAt);
			availableBooks.remove(book.isbn);
		}
	}

	public LocalDate findBorrowedBookDate(Book book) {

		if (isBookAvailable(book.isbn.isbnCode)) {
			return borrowedBooks.get(book);
		}

		return null;
	}

	public boolean isBookAvailable(long isbnCode) {
		return this.availableBooks.keySet().stream().filter(b -> b.isbnCode == isbnCode).findAny().isPresent();
	}
}
