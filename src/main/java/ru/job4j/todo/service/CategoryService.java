package ru.job4j.todo.service;

import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.persistence.CategoryDBStore;

import java.util.Collection;

@Service
public class CategoryService {
    private final CategoryDBStore store;

    public CategoryService(CategoryDBStore store) {
        this.store = store;
    }

    public Category add(Category category) {
        return store.add(category);
    }

    public Collection<Category> findAll() {
        return store.findAll();
    }

    public Category findById(int id) {
        return store.findById(id);
    }
}
