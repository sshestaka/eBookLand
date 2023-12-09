package mate.academy.onlinebookstore.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import mate.academy.onlinebookstore.model.Book;
import mate.academy.onlinebookstore.repository.book.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find all books by category id")
    @Sql(
            scripts = "classpath:database/books/add-three-default-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/categories/add-books-in-books-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/categories/remove-all-from-books-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/books/remove-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void findAllByCategoryId_GivenBooksWithCategories_ReturnListBooks() {
        List<Book> expectedListOneBook = new ArrayList<>();
        expectedListOneBook.add(getRedBookWithPrice19_99());
        List<Book> actualOneBook = bookRepository.findAllByCategoryId(1L);
        Assertions.assertEquals(1, actualOneBook.size());
        Assertions.assertEquals(expectedListOneBook, actualOneBook);

        List<Book> expectedListThreeBooks = new ArrayList<>();
        expectedListThreeBooks.add(getRedBookWithPrice19_99());
        expectedListThreeBooks.add(getGreenBookWithPrice19_99());
        expectedListThreeBooks.add(getBlackBookWithPrice21_99());
        List<Book> actualThreeBooks = bookRepository.findAllByCategoryId(2L);
        Assertions.assertEquals(3, actualThreeBooks.size());
        Assertions.assertEquals(expectedListThreeBooks, actualThreeBooks);
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
}
