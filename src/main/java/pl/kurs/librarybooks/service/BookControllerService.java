package pl.kurs.librarybooks.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pl.kurs.librarybooks.command.CreateBookCommand;
import pl.kurs.librarybooks.command.UpdateBookCommand;
import pl.kurs.librarybooks.dto.BookDTO;
import pl.kurs.librarybooks.dto.GetBookDTO;
import pl.kurs.librarybooks.dto.StatusDTO;
import pl.kurs.librarybooks.model.Book;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookControllerService {
    private BookManagementService bookManagementService;
    private ModelMapper modelMapper;


    public List<GetBookDTO> search(String search) {
        List<Book> bookList = bookManagementService.search(search);
        return bookList.stream().map(book -> modelMapper.map(book, GetBookDTO.class)).collect(Collectors.toList());
    }






    public BookDTO addBook(CreateBookCommand command) {
        Book bookForSave = modelMapper.map(command, Book.class);
        bookForSave.setBorrowed(false);
        bookForSave = bookManagementService.add(bookForSave);
        return modelMapper.map(bookForSave, BookDTO.class);
    }

    public GetBookDTO getBook(long id) {
        return modelMapper.map(bookManagementService.get(id), GetBookDTO.class);
    }

    public List<GetBookDTO> getBooks(int page, int size) {
        Page<Book> booksPage = bookManagementService.getAll(page, size);
        List<Book> booksList = booksPage.getContent();
        return booksList.stream().map(x -> modelMapper.map(x, GetBookDTO.class)).collect(Collectors.toList());
    }


    public List<GetBookDTO> getBooks() {
        return bookManagementService.getAll().stream()
                .map(x -> modelMapper.map(x, GetBookDTO.class))
                .collect(Collectors.toList());
    }

    public GetBookDTO updateBook(UpdateBookCommand command) {
        Book bookForUpdate = modelMapper.map(command, Book.class);
        bookForUpdate = bookManagementService.edit(bookForUpdate);
        return modelMapper.map(bookForUpdate, GetBookDTO.class);
    }

    public StatusDTO deleteBook(long id) {
        if (bookManagementService.doesBookExist(id)) {
            bookManagementService.delete(id);
            return new StatusDTO("Deleted id:" + id);
        } else return new StatusDTO("Book by given id not found");
    }

    public StatusDTO borrowBook(long idBook, long idStudent) {
        Book bookToBorrow = bookManagementService.get(idBook);
        bookToBorrow.setBorrowed(true);
        bookToBorrow.setStudentId(idStudent);
        bookToBorrow.setBorrowDate(LocalDate.now());
        bookManagementService.edit(bookToBorrow);
        return new StatusDTO("The book with id:" + idBook + " was borrowed to a student with id:" + idStudent);
    }

    public boolean isBookBorrowed(long id) {
        return bookManagementService.isBookByIdBorrowed(id);
    }
}
