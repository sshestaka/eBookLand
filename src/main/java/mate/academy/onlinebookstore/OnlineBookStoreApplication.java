package mate.academy.onlinebookstore;

import java.math.BigDecimal;
import mate.academy.onlinebookstore.model.Book;
import mate.academy.onlinebookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OnlineBookStoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(OnlineBookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book bigFish = new Book();
            bigFish.setTitle("The Big Fish");
            bigFish.setDescription("The book about a big dream.");
            bigFish.setAuthor("Unknown");
            bigFish.setPrice(BigDecimal.valueOf(1000));
            bigFish.setIsbn("BF12345");

            Book javaCourse = new Book();
            javaCourse.setTitle("Lava Core Course");
            javaCourse.setDescription("The book about java core");
            javaCourse.setAuthor("Bogdan Chupika");
            javaCourse.setPrice(BigDecimal.valueOf(199));
            javaCourse.setIsbn("JC12345");

            bookService.save(bigFish);
            bookService.save(javaCourse);
            bookService.findAll().forEach(System.out::println);
        };
    }
}
