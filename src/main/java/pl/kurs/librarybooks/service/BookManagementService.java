package pl.kurs.librarybooks.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kurs.librarybooks.model.Book;
import pl.kurs.librarybooks.repository.BookRepository;

import java.time.LocalDate;
import java.time.Period;


@Service
public class BookManagementService extends GenericManagementService<Book, BookRepository>{
    public BookManagementService(BookRepository repository) {
        super(repository);
    }

    public boolean doesBookExist(long id){
        return repository.existsBookById(id);
    }

    public boolean isBookByIdBorrowed(long id){
            return repository.isBookBorrowed(id);
    }

    public long getBorrowedDays(Book book){
        LocalDate borrowDate = book.getBorrowDate();
        Period period = Period.between(borrowDate, LocalDate.now());
        return period.getDays();
    }
}
