package dao;

import model.Track;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class TrackDAO {

    public void save(Track track) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(track);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public Track findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Track.class, id);
        }
    }

    public List<Track> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Track", Track.class).list();
        }
    }

    public List<Track> findByAlbumId(Long albumId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Track t WHERE t.album.id = :albumId ORDER BY t.trackNumber",
                    Track.class
            ).setParameter("albumId", albumId).list();
        }
    }

    public List<Track> findTopTracks(int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Track t ORDER BY t.playCount DESC",
                    Track.class
            ).setMaxResults(limit).list();
        }
    }
}