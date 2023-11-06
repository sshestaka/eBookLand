package mate.academy.onlinebookstore.service.book;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import mate.academy.onlinebookstore.dto.book.BookDto;
import mate.academy.onlinebookstore.dto.book.BookSearchParametersDto;
import mate.academy.onlinebookstore.dto.book.CreateBookRequestDto;
import mate.academy.onlinebookstore.dto.book.UpdateBookRequestDto;
import mate.academy.onlinebookstore.mapper.BookMapper;
import mate.academy.onlinebookstore.model.Book;
import mate.academy.onlinebookstore.repository.book.BookRepository;
import mate.academy.onlinebookstore.repository.book.BookSpecificationBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        //some logic for autogenerate a hidden data
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public BookDto update(Long id, UpdateBookRequestDto updateRequestDto) {
        Book updatedBook = bookRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Can't find a updatedBook by id: " + id));
        updateBook(updatedBook, updateRequestDto);
        return bookMapper.toDto(bookRepository.save(updatedBook));
    }

    @Override
    public List<BookDto> getAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Can't find a book by id: " + id)
        ));
    }

    @Override
    public List<BookDto> searchBooks(BookSearchParametersDto searchParameters, Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(searchParameters);
        return bookRepository.findAll(bookSpecification, pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    private void updateBook(Book updatedBook, UpdateBookRequestDto updateRequestDto) {
        if (updateRequestDto.getTitle() != null) {
            updatedBook.setTitle(updateRequestDto.getTitle());
        }
        if (updateRequestDto.getAuthor() != null) {
            updatedBook.setAuthor(updateRequestDto.getAuthor());
        }
        if (updateRequestDto.getIsbn() != null) {
            updatedBook.setIsbn(updateRequestDto.getIsbn());
        }
        if (updateRequestDto.getPrice() != null) {
            updatedBook.setPrice(updateRequestDto.getPrice());
        }
        if (updateRequestDto.getDescription() != null) {
            updatedBook.setDescription(updateRequestDto.getDescription());
        }
        if (updateRequestDto.getCoverImage() != null) {
            updatedBook.setCoverImage(updateRequestDto.getCoverImage());
        }
    }
}
