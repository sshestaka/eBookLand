package mate.academy.onlinebookstore.service.category;

import java.util.List;
import mate.academy.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.onlinebookstore.dto.category.CategoryDto;
import mate.academy.onlinebookstore.dto.category.CreateCategoryDto;
import mate.academy.onlinebookstore.dto.category.UpdateCategoryDto;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDto save(CreateCategoryDto createCategoryDto);

    CategoryDto update(Long id, UpdateCategoryDto updateCategoryDto);

    List<CategoryDto> getAll(Pageable pageable);

    CategoryDto findById(Long id);

    void deleteById(Long id);

    List<BookDtoWithoutCategoryIds> findBooksByCategoryId(Long categoryId);
}
