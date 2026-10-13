package dao;

import jakarta.persistence.criteria.*;
import model.Album;
import model.Artist;
import model.Track;
import org.hibernate.Session;
import util.HibernateUtil;

import java.util.List;

public class StatisticsDAO {

    public List<Object[]> getArtistStatistics() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

            Root<Artist> artistRoot = query.from(Artist.class);

            Join<Artist, Album> albumJoin = artistRoot.join("albums", JoinType.LEFT);
            Join<Album, Track> trackJoin = albumJoin.join("tracks", JoinType.LEFT);

            query.multiselect(
                    artistRoot.get("name"),
                    cb.countDistinct(albumJoin.get("id")),
                    cb.count(trackJoin.get("id")),
                    cb.sum(trackJoin.get("durationSeconds"))
            );

            query.groupBy(
                    artistRoot.get("id"),
                    artistRoot.get("name")
            );

            return session.createQuery(query).getResultList();
        }
    }
}