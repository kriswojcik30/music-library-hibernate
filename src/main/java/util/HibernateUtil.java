package util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import model.*;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();

            configuration.configure("hibernate.cfg.xml");

            // REJESTRACJA ENCJI
            configuration.addAnnotatedClass(Artist.class);
            configuration.addAnnotatedClass(Album.class);
            configuration.addAnnotatedClass(Track.class);
            configuration.addAnnotatedClass(Playlist.class);
            configuration.addAnnotatedClass(PlaylistTrack.class);
            configuration.addAnnotatedClass(Review.class);

            return configuration.buildSessionFactory();

        } catch (Throwable ex) {
            System.err.println("Błąd przy tworzeniu SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}