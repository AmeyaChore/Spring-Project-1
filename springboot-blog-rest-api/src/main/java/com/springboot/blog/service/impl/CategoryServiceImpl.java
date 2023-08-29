package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Category;
import com.springboot.blog.exceptons.ResourceNotFoundException;
import com.springboot.blog.payload.CategoryDto;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {


    private CategoryRepository categoryRepository;
    private ModelMapper mapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category=dtoToEntity(categoryDto);
        Category savedCategory=categoryRepository.save(category);

        return entityToDto(savedCategory );
    }

    @Override
    public CategoryDto getCategory(long categoryId) {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","Id",categoryId));
        return entityToDto(category);
    }

    @Override
    public List<CategoryDto> getAllCategory() {
        List<Category> categories=categoryRepository.findAll();
        return categories.stream().map((category) -> mapper.map(category,CategoryDto.class)).collect(Collectors.toList());
    }

    @Override
    public CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto) {
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category","Id",categoryId));

        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category.setId(categoryId);

        Category updatedCategory=categoryRepository.save(category);

        return mapper.map(updatedCategory,CategoryDto.class);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","Id",categoryId));
        categoryRepository.delete(category);
    }

    private Category dtoToEntity(CategoryDto categoryDto){
        Category category=mapper.map(categoryDto, Category.class);
        return category;
    }

    private CategoryDto entityToDto(Category category){
        CategoryDto categoryDto=mapper.map(category, CategoryDto.class);
        return categoryDto;
    }
}
