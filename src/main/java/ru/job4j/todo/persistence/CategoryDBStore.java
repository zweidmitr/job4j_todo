package ru.job4j.todo.persistence;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class CategoryDBStore implements DBStoreSession {
    private final SessionFactory sf;

    public CategoryDBStore(SessionFactory sf) {
        this.sf = sf;
    }

    public Category add(Category category) {
        tx(session -> session.save(category), sf);
        return category;
    }

    public List<Category> findAll() {
        return tx(session -> session.createQuery("from Category ").list(), sf);
    }

    public Category findById(int id) {
        return tx(session -> session.get(Category.class, id), sf);
    }

    public Set<Category> findCategoriesFromItem(List<Integer> list) {
        Set<Category> categories = new HashSet<>();
        for (int id : list) {
            Category category = findById(id);
            categories.add(category);
        }
        return categories;
    }
}
