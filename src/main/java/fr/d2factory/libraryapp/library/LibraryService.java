package fr.d2factory.libraryapp.library;

import java.time.Duration;
import java.time.LocalDate;

import fr.d2factory.libraryapp.LibraryProperties;
import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.member.Member;
public class LibraryService implements Library {

	BookRepository bookRepository;
	
	
	public LibraryService() {
		this.bookRepository=BookRepository.getInstance();
	}
	
	
	@Override
	public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException {
	
      if(member.hasLateBooks()) 
    	  throw new HasLateBooksException();
    	  
      
      
		
		return null;
	}

	@Override
	public void returnBook(Book book, Member member) {
		
		
	}

}
