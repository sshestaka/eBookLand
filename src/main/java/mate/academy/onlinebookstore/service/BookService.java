package mate.academy.onlinebookstore.service;

import java.util.List;
import mate.academy.onlinebookstore.dto.BookDto;
import mate.academy.onlinebookstore.dto.BookSearchParametersDto;
import mate.academy.onlinebookstore.dto.CreateBookRequestDto;
import mate.academy.onlinebookstore.dto.UpdateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto update(Long id, UpdateBookRequestDto updateRequestDto);

    List<BookDto> getAll(Pageable pageable);

    BookDto findById(Long id);

    List<BookDto> searchBooks(BookSearchParametersDto searchParameters, Pageable pageable);

    void deleteById(Long id);
}
