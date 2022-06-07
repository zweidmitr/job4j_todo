package ru.job4j.todo.persistence;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.List;

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
}
