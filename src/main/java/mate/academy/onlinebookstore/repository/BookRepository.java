package mate.academy.onlinebookstore.repository;

import java.util.List;
import java.util.Optional;
import mate.academy.onlinebookstore.model.Book;

public interface BookRepository {
    Book save(Book book);

    Optional<Book> findById(Long id);

    List<Book> getAll();
}
