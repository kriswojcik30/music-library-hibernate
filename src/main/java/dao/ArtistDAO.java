package dao;

import model.Artist;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class ArtistDAO {

    public void save(Artist artist) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.persist(artist);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public Artist findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Artist.class, id);
        }
    }

    public List<Artist> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Artist", Artist.class).list();
        }
    }

    public void update(Artist artist) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.merge(artist);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void delete(Artist artist) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.remove(session.contains(artist) ? artist : session.merge(artist));

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public List<Artist> findByGenre(String genre) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Artist a WHERE a.genre = :genre",
                            Artist.class
                    )
                    .setParameter("genre", genre)
                    .list();
        }
    }

    public List<Artist> findByCountry(String country) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Artist a WHERE a.country = :country",
                            Artist.class
                    )
                    .setParameter("country", country)
                    .list();
        }
    }

    public List<Artist> findByDebutYearAfter(int year) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Artist a WHERE a.debutYear >= :year",
                            Artist.class
                    )
                    .setParameter("year", year)
                    .list();
        }
    }
}