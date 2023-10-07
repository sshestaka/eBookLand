package mate.academy.onlinebookstore.service;

import java.util.List;
import mate.academy.onlinebookstore.dto.BookDto;
import mate.academy.onlinebookstore.dto.BookSearchParametersDto;
import mate.academy.onlinebookstore.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    List<BookDto> getAll();

    BookDto findById(Long id);

    List<BookDto> searchBooks(BookSearchParametersDto searchParameters);

    void deleteById(Long id);
}
