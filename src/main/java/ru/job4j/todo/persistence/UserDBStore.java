package ru.job4j.todo.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import javax.persistence.NoResultException;
import java.util.Optional;

@Repository
public class UserDBStore implements DBStoreSession {
    private final SessionFactory sf;

    public UserDBStore(SessionFactory sf) {
        this.sf = sf;
    }

    public Optional<User> add(User user) {
        try {
            tx(session -> session.save(user), sf);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.ofNullable(user);
    }

    public boolean update(User user) {
        return tx(session -> {
                    int index = session.createQuery("update User u set "
                                    + "u.name = :name, "
                                    + "u.email = :email, "
                                    + "u.password = :password "
                                    + "where u.id = :fId")
                            .setParameter("name", user.getName())
                            .setParameter("email", user.getEmail())
                            .setParameter("id", user.getId())
                            .setParameter("password", user.getPassword())
                            .executeUpdate();
                    return index != 0;
                }, sf
        );
    }

    public boolean delete(int id) {
        return tx(
                session -> {
                    int index = session.createQuery("delete from User u where u.id= :id")
                            .setParameter("id", id)
                            .executeUpdate();
                    return index != 0;
                }, sf
        );
    }

    public Optional<User> findByEMailAndPwd(String email, String password) {
        return tx(
                session -> {
                    Optional<User> optUser = Optional.empty();
                    Query query = session.createQuery("from User u "
                                    + "where u.email = :email and u.password = :password")
                            .setParameter("email", email)
                            .setParameter("password", password);
                    try {
                        optUser = Optional.of((User) query.getSingleResult());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return optUser;

                }, sf
        );
    }
}
