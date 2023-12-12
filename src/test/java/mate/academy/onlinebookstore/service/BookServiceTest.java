package mate.academy.onlinebookstore.service;

import static org.mockito.Mockito.times;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mate.academy.onlinebookstore.dto.book.BookDto;
import mate.academy.onlinebookstore.dto.book.CreateBookRequestDto;
import mate.academy.onlinebookstore.dto.book.UpdateBookRequestDto;
import mate.academy.onlinebookstore.mapper.BookMapper;
import mate.academy.onlinebookstore.model.Book;
import mate.academy.onlinebookstore.repository.book.BookRepository;
import mate.academy.onlinebookstore.service.book.BookServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    private static final long NON_EXISTING_BOOK_ID = 100L;
    private static final double UPDATED_PRICE = 1.11;
    private static final String NON_EXISTING_ID_EXCEPTION = "Can't find a updatedBook by id: ";
    @InjectMocks
    private BookServiceImpl bookService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;

    @Test
    @DisplayName("Verify save() method works")
    public void save_ValidCreateBookRequestDto_ReturnsBookDto() {
        CreateBookRequestDto requestDto = getCreateTestBookRequestDto();
        Book book = getBook(requestDto);
        BookDto bookDto = getBookDto(book);
        Mockito.when(bookMapper.toModel(requestDto)).thenReturn(book);
        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);
        BookDto savedBookDto = bookService.save(requestDto);
        Assertions.assertEquals(savedBookDto, bookDto);
        Mockito.verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify getAll method works")
    public void getAll_ValidPageable_ReturnAllBooks() {
        List<Book> books = new ArrayList<>();
        books.add(getRedBookWithPrice19_99());
        books.add(getGreenBookWithPrice19_99());

        List<BookDto> bookDtosExpected = new ArrayList<>();
        bookDtosExpected.add(getRedBookDtoDtoWithPrice19_99());
        bookDtosExpected.add(getGreenBookDtoWithPrice19_99());

        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        Mockito.when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        Mockito.when(bookMapper.toDto(books.get(0))).thenReturn(bookDtosExpected.get(0));
        Mockito.when(bookMapper.toDto(books.get(1))).thenReturn(bookDtosExpected.get(1));

        List<BookDto> bookDtosActual = bookService.getAll(pageable);

        Assertions.assertEquals(2, bookDtosActual.size());
        Assertions.assertEquals(bookDtosActual, bookDtosExpected);
        Mockito.verify(bookRepository, times(1)).findAll(pageable);
        Mockito.verify(bookMapper, times(1)).toDto(books.get(0));
        Mockito.verify(bookMapper, times(1)).toDto(books.get(1));
        Mockito.verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Check if findById() method throws correct "
            + "exception with non existing book id")
    public void findBook_GivenNonExistingBookId_ShouldThrowException() {
        Long nonExistingBookId = NON_EXISTING_BOOK_ID;
        Mockito.when(bookRepository.findById(nonExistingBookId)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> bookService.findById(nonExistingBookId)
        );
        String expected = "Can't find a book by id: " + nonExistingBookId;
        String actual = exception.getMessage();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Check if update() method throws correct "
            + "exception with non existing book id")
    public void update_GivenNonExistingBookId_ShouldThrowException() {
        UpdateBookRequestDto updateBookRequestDto = getUpdateBookRequestDto();
        updateBookRequestDto.setPrice(BigDecimal.valueOf(UPDATED_PRICE));
        Long nonExistingBookId = NON_EXISTING_BOOK_ID;
        Mockito.when(bookRepository.findById(nonExistingBookId)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> bookService.update(nonExistingBookId, updateBookRequestDto)
        );
        String expected = NON_EXISTING_ID_EXCEPTION + nonExistingBookId;
        String actual = exception.getMessage();
        Assertions.assertEquals(expected, actual);
    }

    private UpdateBookRequestDto getUpdateBookRequestDto() {
        return new UpdateBookRequestDto()
                .setTitle("updated")
                .setAuthor("updated")
                .setIsbn("updated")
                .setPrice(BigDecimal.valueOf(UPDATED_PRICE));
    }

    private Book getRedBookWithPrice19_99() {
        return new Book()
                .setId(1L)
                .setTitle("Red Book")
                .setAuthor("Red Author")
                .setIsbn("Red-ISBN")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("Red description")
                .setCoverImage("Red cover image");

    }

    private Book getGreenBookWithPrice19_99() {
        return new Book()
                .setId(2L)
                .setTitle("Green Book")
                .setAuthor("Green Author")
                .setIsbn("Green-ISBN")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("Green description")
                .setCoverImage("Green cover image");

    }

    private Book getBlackBookWithPrice21_99() {
        return new Book()
                .setId(3L)
                .setTitle("Black Book")
                .setAuthor("Black Author")
                .setIsbn("Black-ISBN")
                .setPrice(BigDecimal.valueOf(21.99))
                .setDescription("Black description")
                .setCoverImage("Black cover image");

    }

    private BookDto getRedBookDtoDtoWithPrice19_99() {
        return new BookDto()
                .setId(1L)
                .setTitle("Red Book")
                .setAuthor("Red Author")
                .setIsbn("Red-ISBN")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("Red description")
                .setCoverImage("Red cover image");

    }

    private BookDto getGreenBookDtoWithPrice19_99() {
        return new BookDto()
                .setId(2L)
                .setTitle("Green Book")
                .setAuthor("Green Author")
                .setIsbn("Green-ISBN")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("Green description")
                .setCoverImage("Green cover image");

    }

    private BookDto getBlackBookDtoWithPrice21_99() {
        return new BookDto()
                .setId(3L)
                .setTitle("Black Book")
                .setAuthor("Black Author")
                .setIsbn("Black-ISBN")
                .setPrice(BigDecimal.valueOf(21.99))
                .setDescription("Black description")
                .setCoverImage("Black cover image");

    }

    private BookDto getBookDto(Book book) {
        return new BookDto()
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setIsbn(book.getIsbn())
                .setPrice(book.getPrice())
                .setDescription(book.getDescription())
                .setCoverImage(book.getCoverImage());
    }

    private Book getBook(CreateBookRequestDto requestDto) {
        return new Book()
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice())
                .setDescription(requestDto.getDescription())
                .setCoverImage(requestDto.getCoverImage());
    }

    private CreateBookRequestDto getCreateTestBookRequestDto() {
        return new CreateBookRequestDto()
                .setTitle("Test-Book")
                .setAuthor("Test-Author")
                .setIsbn("Test-ISBN")
                .setPrice(BigDecimal.valueOf(44.95))
                .setDescription("Test-Description")
                .setCoverImage("Test-Cover-Image");
    }
}
