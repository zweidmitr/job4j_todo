package ru.job4j.todo.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;

import java.util.List;
import java.util.function.Function;

@Repository
public class ItemDBStore {
    private final SessionFactory sf;

    public ItemDBStore(SessionFactory sf) {
        this.sf = sf;
    }

    private <T> T tx(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public Item add(Item item) {
        return this.tx(
                session -> {
                    session.save(item);
                    return item;
                }
        );
    }

    public boolean update(Item item) {
        return this.tx(
                session -> {
                    int index = session.createQuery("update Item i set "
                                    + "i.name = :name, "
                                    + "i.description = :description, "
                                    + "i.created = :created, "
                                    + "i.done = :done "
                                    + "where i.id = :fId")
                            .setParameter("name", item.getName())
                            .setParameter("description", item.getDescription())
                            .setParameter("created", item.getCreated())
                            .setParameter("fId", item.getId())
                            .setParameter("done", item.isDone())
                            .executeUpdate();
                    return index != 0;
                }
        );
    }

    public boolean delete(int id) {
        return this.tx(
                session -> {
                    int index = session.createQuery("delete from Item i where i.id = :fId")
                            .setParameter("fId", id)
                            .executeUpdate();
                    return index != 0;
                }
        );
    }

    public Item findById(int id) {
        return this.tx(
                session -> {
                    Item result = (Item) session.createQuery("from Item i where i.id = :fId")
                            .setParameter("fId", id)
                            .getSingleResult();
                    return result;
                }
        );
    }

    public List<Item> findAll() {
        return this.tx(
                session -> session.createQuery("from Item ").list()
        );
    }

    public List<Item> findByDone(boolean isDone) {
        return this.tx(
                session -> session.createQuery("from Item i where i.done = :isDone")
                        .setParameter("isDone", isDone).list()
        );
    }

    public boolean setDone(int id) {
        return this.tx(
                session -> {
                    int index = session.createQuery("update Item  i set "
                                    + "i.done = :done where i.id = :fId")
                            .setParameter("done", true)
                            .setParameter("fId", id)
                            .executeUpdate();
                    return index != 0;
                }
        );
    }
}
