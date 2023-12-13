package mate.academy.onlinebookstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.onlinebookstore.dto.book.BookDto;
import mate.academy.onlinebookstore.dto.book.CreateBookRequestDto;
import mate.academy.onlinebookstore.dto.book.UpdateBookRequestDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    public static final double TEST_BOOK_PRICE = 44.95;
    public static final double UPDATED_BOOK_PRICE = 999.99;
    private static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/remove-all-data-before-tests/remove-all-data-before-tests.sql"
                    )
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/add-three-default-books.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/remove-all-books.sql")
            );
        }
    }

    @Test
    @DisplayName("Create a new Book")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(
            scripts = "classpath:database/books/delete-test-book-1.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void createBook_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto requestDto = getTestBookDto();
        BookDto expected = getBookDto(requestDto);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult mvcResult = mockMvc.perform(
                        post("/api/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                BookDto.class
        );
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("Get all books")
    @WithMockUser(username = "user", roles = {"USER"})
    void getAll_GivenBooksInCatalog_ShouldReturnAllBooks() throws Exception {
        List<BookDto> expected = new ArrayList<>();
        expected.add(getRedBookDtoWithPrice19_99());
        expected.add(getGreenBookDtoWithPrice19_99());
        expected.add(getBlackBookDtoWithPrice21_99());
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/books")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        BookDto[] actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsByteArray(),
                BookDto[].class
        );
        Assertions.assertEquals(3, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @DisplayName("Find book by id")
    @WithMockUser(username = "user", roles = {"USER"})
    void findById_GivenExistingBookId_ShouldReturnBook() throws Exception {
        BookDto expected1 = getRedBookDtoWithPrice19_99();
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/books/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual1 = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                BookDto.class
        );
        Assertions.assertNotNull(actual1);
        EqualsBuilder.reflectionEquals(expected1, actual1);
        BookDto expected3 = getBlackBookDtoWithPrice21_99();
        mvcResult = mockMvc.perform(
                        get("/api/books/3")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual3 = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                BookDto.class
        );
        Assertions.assertNotNull(actual3);
        EqualsBuilder.reflectionEquals(expected3, actual3);
    }

    @Test
    @DisplayName("Delete book by id")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void delete_DeleteExistedBook_Success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        delete("/api/books/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
        String actual = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("Update book by id")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateBook_UpdateExistedBook_Success() throws Exception {
        UpdateBookRequestDto updateBookRequestDto = getUpdateBookRequestDto();
        BookDto expected = getBookDtoFromUpdatedBookRequestDto(updateBookRequestDto);
        String jsonRequest = objectMapper.writeValueAsString(updateBookRequestDto);
        MvcResult mvcResult = mockMvc.perform(
                        patch("/api/books/1")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                BookDto.class
        );
        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    private BookDto getBookDtoFromUpdatedBookRequestDto(UpdateBookRequestDto updateBookRequestDto) {
        return new BookDto()
                .setId(1L)
                .setTitle(updateBookRequestDto.getTitle())
                .setAuthor(updateBookRequestDto.getAuthor())
                .setIsbn(updateBookRequestDto.getIsbn())
                .setPrice(BigDecimal.valueOf(UPDATED_BOOK_PRICE))
                .setDescription(updateBookRequestDto.getDescription())
                .setCoverImage(updateBookRequestDto.getCoverImage());
    }

    private UpdateBookRequestDto getUpdateBookRequestDto() {
        return new UpdateBookRequestDto()
                .setTitle("Updated Red Book")
                .setAuthor("Updated Red Author")
                .setIsbn("Updated Red-ISBN")
                .setPrice(BigDecimal.valueOf(UPDATED_BOOK_PRICE))
                .setDescription("Updated Red description")
                .setCoverImage("Updated Red cover image");
    }

    private BookDto getRedBookDtoWithPrice19_99() {
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

    private BookDto getBookDto(CreateBookRequestDto requestDto) {
        return new BookDto()
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice())
                .setDescription(requestDto.getDescription())
                .setCoverImage(requestDto.getCoverImage());
    }

    private CreateBookRequestDto getTestBookDto() {
        return new CreateBookRequestDto()
                .setTitle("Test-Book")
                .setAuthor("Test-Author")
                .setIsbn("Test-ISBN")
                .setPrice(BigDecimal.valueOf(TEST_BOOK_PRICE))
                .setDescription("Test-Description")
                .setCoverImage("Test-Cover-Image");
    }
}
