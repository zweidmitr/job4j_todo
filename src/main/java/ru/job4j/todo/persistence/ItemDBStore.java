package ru.job4j.todo.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;

import java.util.List;
import java.util.function.Function;

@Repository
public class ItemDBStore implements DBStoreSession {
    private final SessionFactory sf;

    public ItemDBStore(SessionFactory sf) {
        this.sf = sf;
    }

    public Item add(Item item) {
        return tx(
                session -> {
                    session.save(item);
                    return item;
                }, sf
        );
    }

    public Item update(Item item) {
        return tx(
                session -> {
                    session.update(item);
                    return item;
                }, sf
        );
    }

    public boolean delete(int id) {
        return tx(
                session -> {
                    int index = session.createQuery("delete from Item i where i.id = :fId")
                            .setParameter("fId", id)
                            .executeUpdate();
                    return index != 0;
                }, sf
        );
    }

    public Item findById(int id) {
        return tx(
                session -> {
                    Item result = (Item) session.createQuery("from Item i where i.id = :fId")
                            .setParameter("fId", id)
                            .getSingleResult();
                    return result;
                }, sf
        );
    }

    public List<Item> findAll() {
        return tx(
                session -> session.createQuery("select distinct i from Item i join fetch i.categories ").list(), sf
        );
    }

    public List<Item> findByDone(boolean isDone) {
        return tx(
                session -> session.createQuery("select distinct i from Item i join fetch i.categories where i.done = :isDone")
                        .setParameter("isDone", isDone).list(), sf
        );
    }

    public boolean setDone(int id) {
        return tx(
                session -> {
                    int index = session.createQuery("update Item  i set "
                                    + "i.done = :done where i.id = :fId")
                            .setParameter("done", true)
                            .setParameter("fId", id)
                            .executeUpdate();
                    return index != 0;
                }, sf
        );
    }

    public Item findByIdWitchCategories(int id) {
        return tx(session -> {
            return (Item) session.createQuery(
                            "select distinct i from Item i left join fetch i.categories where i.id = :fId")
                    .setParameter("fId", id)
                    .getSingleResult();
        }, sf);
    }
}
