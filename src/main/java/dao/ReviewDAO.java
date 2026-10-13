package dao;

import model.Review;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class ReviewDAO {

    public void save(Review review) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.persist(review);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public List<Review> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Review", Review.class).list();
        }
    }

    // 🔥 STATYSTYKI
    public List<Object[]> getReviewStats() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql = """
                SELECT a.title, ar.name, COUNT(r), AVG(r.rating)
                FROM Review r
                JOIN r.album a
                JOIN a.artist ar
                GROUP BY a.id, a.title, ar.name
                ORDER BY AVG(r.rating) DESC
                """;

            return session.createQuery(hql).list();
        }
    }
}