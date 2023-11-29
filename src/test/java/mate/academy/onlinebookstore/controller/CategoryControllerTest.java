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
import mate.academy.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.onlinebookstore.dto.category.CategoryDto;
import mate.academy.onlinebookstore.dto.category.CreateCategoryDto;
import mate.academy.onlinebookstore.dto.category.UpdateCategoryDto;
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
public class CategoryControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

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
                    new ClassPathResource("database/categories/add-three-default-categories.sql")
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
                    new ClassPathResource("database/categories/remove-all-categories.sql")
            );
        }
    }

    @Test
    @DisplayName("Create a new category")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(
            scripts = "classpath:database/categories/remove-test-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void createCategory_ValidRequestDto_Success() throws Exception {
        CreateCategoryDto createCategoryDto = new CreateCategoryDto();
        createCategoryDto.setName("Test Category");
        createCategoryDto.setDescription("Test Category Description");

        CategoryDto expected = new CategoryDto();
        expected.setName(createCategoryDto.getName());
        expected.setDescription(createCategoryDto.getDescription());

        String jsonRequest = objectMapper.writeValueAsString(createCategoryDto);
        MvcResult mvcResult = mockMvc.perform(
                        post("/api/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                CategoryDto.class
        );
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("Get list of all categories")
    @WithMockUser(username = "user", roles = {"USER"})
    void getAll_GivenCategoriesInCatalog_ShouldReturnAllCategories() throws Exception {
        List<CategoryDto> expected = new ArrayList<>();
        expected.add(new CategoryDto().setId(1L).setName("Category1").setDescription("Category1"));
        expected.add(new CategoryDto().setId(2L).setName("Category2").setDescription("Category2"));
        expected.add(new CategoryDto().setId(3L).setName("Category3").setDescription("Category3"));
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto[] actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsByteArray(),
                CategoryDto[].class
        );
        Assertions.assertEquals(3, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @DisplayName("Find the category by id")
    @WithMockUser(username = "user", roles = {"USER"})
    void findById_GivenExistingCategoryId_ShouldReturnCategory() throws Exception {
        CategoryDto expected = new CategoryDto()
                .setId(1L)
                .setName("Category1")
                .setDescription("Category1");
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/categories/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                CategoryDto.class
        );
        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @DisplayName("Update an existed category")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateCategory_UpdateExistedCategory_Success() throws Exception {
        UpdateCategoryDto updateCategoryDto = new UpdateCategoryDto();
        updateCategoryDto.setName("Updated Name");
        updateCategoryDto.setDescription("Updated Description");
        CategoryDto expected = new CategoryDto()
                .setName("Updated Name")
                .setDescription("Updated Description");
        String jsonRequest = objectMapper.writeValueAsString(updateCategoryDto);
        MvcResult mvcResult = mockMvc.perform(
                        patch("/api/categories/1")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                CategoryDto.class
        );
        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @DisplayName("Delete an existed category")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteCategory_DeleteExistedCategory_Success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        delete("/api/categories/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
        String actual = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("Find all books by category id")
    @WithMockUser(username = "user", roles = {"USER"})
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
    void getBooksByCategoryId_GivenCategoriesWithBooks_ShouldReturnAllBooksByCategoryId()
            throws Exception {
        List<BookDtoWithoutCategoryIds> expectedListOneBook = new ArrayList<>();
        expectedListOneBook.add(new BookDtoWithoutCategoryIds()
                .setId(1L)
                .setTitle("Red Book")
                .setAuthor("Red Author")
                .setIsbn("Red-ISBN")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("Red description")
                .setCoverImage("Red cover image")
        );
        MvcResult mvcResultOneBook = mockMvc.perform(
                        get("/api/categories/1/books")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        List<BookDtoWithoutCategoryIds> actualOneBook = Arrays.stream(objectMapper.readValue(
                mvcResultOneBook.getResponse().getContentAsByteArray(),
                BookDtoWithoutCategoryIds[].class
        )).toList();
        Assertions.assertEquals(1, actualOneBook.size());
        Assertions.assertEquals(expectedListOneBook, actualOneBook);

        List<BookDtoWithoutCategoryIds> expectedListThreeBooks = new ArrayList<>();
        expectedListThreeBooks.add(new BookDtoWithoutCategoryIds()
                .setId(1L)
                .setTitle("Red Book")
                .setAuthor("Red Author")
                .setIsbn("Red-ISBN")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("Red description")
                .setCoverImage("Red cover image")
        );
        expectedListThreeBooks.add(new BookDtoWithoutCategoryIds()
                        .setId(2L)
                        .setTitle("Green Book")
                        .setAuthor("Green Author")
                        .setIsbn("Green-ISBN")
                        .setPrice(BigDecimal.valueOf(19.99))
                        .setDescription("Green description")
                        .setCoverImage("Green cover image")
        );
        expectedListThreeBooks.add(new BookDtoWithoutCategoryIds()
                        .setId(3L)
                        .setTitle("Black Book")
                        .setAuthor("Black Author")
                        .setIsbn("Black-ISBN")
                        .setPrice(BigDecimal.valueOf(21.99))
                        .setDescription("Black description")
                        .setCoverImage("Black cover image")
        );
        MvcResult mvcResultThreeBooks = mockMvc.perform(
                        get("/api/categories/2/books")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        List<BookDtoWithoutCategoryIds> actualThreeBooks = Arrays.stream(objectMapper.readValue(
                mvcResultThreeBooks.getResponse().getContentAsByteArray(),
                BookDtoWithoutCategoryIds[].class
        )).toList();
        Assertions.assertEquals(3, actualThreeBooks.size());
        Assertions.assertEquals(expectedListThreeBooks, actualThreeBooks);
    }
}
