package mate.academy.onlinebookstore.service.book;

import java.util.List;
import mate.academy.onlinebookstore.dto.book.BookDto;
import mate.academy.onlinebookstore.dto.book.BookSearchParametersDto;
import mate.academy.onlinebookstore.dto.book.CreateBookRequestDto;
import mate.academy.onlinebookstore.dto.book.UpdateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto update(Long id, UpdateBookRequestDto updateRequestDto);

    List<BookDto> getAll(Pageable pageable);

    BookDto findById(Long id);

    List<BookDto> searchBooks(BookSearchParametersDto searchParameters, Pageable pageable);

    void deleteById(Long id);
}
