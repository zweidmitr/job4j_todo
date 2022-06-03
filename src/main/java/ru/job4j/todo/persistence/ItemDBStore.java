package ru.job4j.todo.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;

import java.util.List;

@Repository
public class ItemDBStore {
    private final SessionFactory sf;

    public ItemDBStore(SessionFactory sf) {
        this.sf = sf;
    }

    public Item add(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();

        session.save(item);

        session.getTransaction().commit();
        session.close();
        return item;
    }

    public boolean update(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();

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

        session.getTransaction().commit();
        session.close();
        return index != 0;
    }

    public boolean delete(int id) {
        Session session = sf.openSession();
        session.beginTransaction();

        int index = session.createQuery("delete from Item i where i.id = :fId")
                .setParameter("fId", id)
                .executeUpdate();

        session.getTransaction().commit();
        session.close();
        return index != 0;
    }

    public Item findById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();

        Item result = (Item) session.createQuery("from Item i where i.id = :fId")
                .setParameter("fId", id)
                .getSingleResult();

        session.getTransaction().commit();
        session.close();
        return result;
    }

    public List<Item> findAll() {
        Session session = sf.openSession();
        session.beginTransaction();

        List result = session.createQuery("from Item").list();

        session.getTransaction();
        session.close();
        return result;
    }

    public List<Item> findByDone(boolean isDone) {
        Session session = sf.openSession();
        session.beginTransaction();

        List result = session.createQuery("from Item i where i.done = :isDone")
                .setParameter("isDone", isDone).list();

        session.getTransaction().commit();
        session.close();
        return result;
    }

    public boolean setDone(int id) {
        Session session = sf.openSession();
        session.beginTransaction();

        int result = session.createQuery("update Item  i set "
                        + "i.done = :done where i.id = :fId")
                .setParameter("done", true)
                .setParameter("fId", id)
                .executeUpdate();

        session.getTransaction().commit();
        session.close();
        return result != 0;
    }

}
