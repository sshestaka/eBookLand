package mate.academy.onlinebookstore.service.category;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import mate.academy.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.onlinebookstore.dto.category.CategoryDto;
import mate.academy.onlinebookstore.dto.category.CreateCategoryDto;
import mate.academy.onlinebookstore.dto.category.UpdateCategoryDto;
import mate.academy.onlinebookstore.mapper.BookMapper;
import mate.academy.onlinebookstore.mapper.CategoryMapper;
import mate.academy.onlinebookstore.model.Category;
import mate.academy.onlinebookstore.repository.book.BookRepository;
import mate.academy.onlinebookstore.repository.category.CategoryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final CategoryMapper categoryMapper;
    private final BookMapper bookMapper;

    @Override
    public CategoryDto save(CreateCategoryDto createCategoryDto) {
        Category category = categoryMapper.toEntity(createCategoryDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(Long id, UpdateCategoryDto updateCategoryDto) {
        Category updatedCategory = categoryRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Can't find a category by id: " + id));
        updateCategory(updatedCategory, updateCategoryDto);
        return categoryMapper.toDto(categoryRepository.save(updatedCategory));
    }

    private void updateCategory(Category updatedCategory, UpdateCategoryDto updateCategoryDto) {
        if (updateCategoryDto.getName() != null) {
            updatedCategory.setName(updateCategoryDto.getName());
        }
        if (updateCategoryDto.getDescription() != null) {
            updatedCategory.setDescription(updateCategoryDto.getDescription());
        }
    }

    @Override
    public List<CategoryDto> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto findById(Long id) {
        return categoryMapper.toDto(categoryRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Can't find a category by id: " + id)));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findBooksByCategoryId(Long categoryId) {
        return bookRepository.findAllByCategoryId(categoryId)
                .stream()
                .map(bookMapper::toDtoWithoutCategoryIds)
                .toList();
    }
}
