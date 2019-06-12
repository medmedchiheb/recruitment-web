package fr.d2factory.libraryapp.library;

import fr.d2factory.libraryapp.LibraryProperties;
import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.Resident;
import fr.d2factory.libraryapp.member.Student;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class LibraryTest {
	private Library library;
	private BookRepository bookRepository;
	private LibraryProperties properties;

	@Before
	public void setup() {
		bookRepository = BookRepository.getInstance();
		library = new LibraryService();
		properties = LibraryProperties.getInstance();

		try (InputStream input = new FileInputStream("src/test/resources/books.json")) {

			String json = CharStreams.toString(new InputStreamReader(input, "UTF-8"));

			ObjectMapper mapper = new ObjectMapper();
			List<Book> books = mapper.readValue(json, new TypeReference<List<Book>>() {
			});
			bookRepository.addBooks(books);

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	@Test
	public void member_can_borrow_a_book_if_book_is_available() {

		// pre-condition
		Member member = new Student(LocalDate.of(2019, 01, 01), 150);
		library.borrowBook(3326456467846L, member, LocalDate.now());

		assertFalse(member.getBorrowedBooks().isEmpty());

	}

	@Test
	public void borrowed_book_is_no_longer_available() {

		Member member = new Student(LocalDate.of(2019, 01, 01), 150);
		Book book = bookRepository.findBook(968787565445L);
		library.borrowBook(968787565445L, member, LocalDate.now());

		assertFalse(bookRepository.isBookAvailable(book.getIsbn().getIsbnCode()));
	}

	@Test
	public void residents_are_taxed_10cents_for_each_day_they_keep_a_book() {
		Member member = new Resident(LocalDate.of(2019, 01, 01), 150);
		Book book = bookRepository.findBook(465789453149L);
		LocalDate borrowedAt = LocalDate.of(2019, 06, 11);
		library.borrowBook(465789453149L, member, borrowedAt);
		library.returnBook(book, member);

		int numberOfDays = (int) ChronoUnit.DAYS.between(borrowedAt, LocalDate.now());
		System.out.println("days" + numberOfDays);
		float cost = numberOfDays * properties.getCostPerDay();

		assertEquals(cost, properties.getCostPerDay(), 0);
	}

	@Test
	public void students_pay_10_cents_the_first_30days() {
		Member member = new Student(LocalDate.of(2019, 01, 01), 150);
		Book book = bookRepository.findBook(465789453149L);
		LocalDate borrowedAt = LocalDate.of(2019, 05, 12);
		library.borrowBook(465789453149L, member, borrowedAt);
		library.returnBook(book, member);

		int numberOfDays = (int) ChronoUnit.DAYS.between(borrowedAt, LocalDate.now());
		float cost = (numberOfDays * properties.getCostPerDay()) / numberOfDays;

		assertEquals(cost, properties.getCostPerDay(), 0);
	}

	@Test
	public void students_in_1st_year_are_not_taxed_for_the_first_15days() {

		Member member = new Student(LocalDate.of(2019, 01, 01), 150);
		float memberWallet = member.getWallet();
		Book book = bookRepository.findBook(465789453149L);
		LocalDate borrowedAt = LocalDate.of(2019, 05, 29);
		library.borrowBook(465789453149L, member, borrowedAt);
		library.returnBook(book, member);

		assertEquals(member.getWallet(), memberWallet, 0);
	}

	@Test
	public void students_pay_15cents_for_each_day_they_keep_a_book_after_the_initial_30days() {

		Member member = new Student(LocalDate.of(2019, 01, 01), 150);
		Book book = bookRepository.findBook(465789453149L);
		LocalDate borrowedAt = LocalDate.of(2019, 05, 01);
		library.borrowBook(465789453149L, member, borrowedAt);
		library.returnBook(book, member);

		int numberOfDays = (int) ChronoUnit.DAYS.between(borrowedAt, LocalDate.now());
		int daysAfterInitialAllowedDays = numberOfDays - LibraryProperties.getInstance().getAllowedDaysStudents();
		float costAfterInitialAllowedDays = daysAfterInitialAllowedDays
				* LibraryProperties.getInstance().getLateStudentsCost();

		assertEquals(costAfterInitialAllowedDays / daysAfterInitialAllowedDays,
				LibraryProperties.getInstance().getLateStudentsCost(), 0);
	}

	@Test
	public void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days() {
		Member member = new Resident(LocalDate.of(2019, 01, 01), 150);
		Book book = bookRepository.findBook(465789453149L);
		LocalDate borrowedAt = LocalDate.of(2019, 05, 01);
		library.borrowBook(465789453149L, member, borrowedAt);
		library.returnBook(book, member);
		int numberOfDays = (int) ChronoUnit.DAYS.between(borrowedAt, LocalDate.now());

		int daysAfterInitialAllowedDays = numberOfDays - LibraryProperties.getInstance().getAllowedDaysResidents();
		float costAfterInitialAllowedDays = daysAfterInitialAllowedDays
				* LibraryProperties.getInstance().getLateResidentsCost();

		assertEquals(costAfterInitialAllowedDays / daysAfterInitialAllowedDays,
				LibraryProperties.getInstance().getLateResidentsCost(), 0);
	}

	@Test(expected = HasLateBooksException.class)
	public void members_cannot_borrow_book_if_they_have_late_books() {
		Member member = new Student(LocalDate.of(2019, 01, 01), 150);

		LocalDate borrowedAt = LocalDate.of(2019, 05, 01);
		library.borrowBook(465789453149L, member, borrowedAt);

		Book anotherBook = bookRepository.findBook(3326456467846L);
		library.borrowBook(anotherBook.getIsbn().getIsbnCode(), member, LocalDate.now());

		assertTrue(bookRepository.isBookAvailable(anotherBook.getIsbn().getIsbnCode()));
	}
}
