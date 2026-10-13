package dao;

import model.Playlist;
import model.PlaylistTrack;
import model.Track;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class PlaylistDAO {

    public void save(Playlist playlist) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(playlist);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public Playlist findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Playlist.class, id);
        }
    }

    public List<Playlist> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Playlist", Playlist.class).list();
        }
    }

    public void addTrackToPlaylist(Long playlistId, Long trackId, int position) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Playlist playlist = session.get(Playlist.class, playlistId);
            Track track = session.get(Track.class, trackId);

            if (playlist == null) {
                throw new IllegalArgumentException("Nie znaleziono playlisty o id: " + playlistId);
            }

            if (track == null) {
                throw new IllegalArgumentException("Nie znaleziono utworu o id: " + trackId);
            }

            if (position < 1) {
                throw new IllegalArgumentException("Pozycja na playliście musi być >= 1");
            }

            PlaylistTrack entry = new PlaylistTrack(playlist, track, position);

            session.persist(entry);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public List<PlaylistTrack> getEntries(Long playlistId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM PlaylistTrack pt " +
                            "JOIN FETCH pt.track " +
                            "WHERE pt.playlist.id = :playlistId " +
                            "ORDER BY pt.position",
                    PlaylistTrack.class
            ).setParameter("playlistId", playlistId).list();
        }
    }
}