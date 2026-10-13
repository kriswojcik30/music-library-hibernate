package dao;

import model.Track;
import org.hibernate.Session;
import util.HibernateUtil;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportDAO {

    public void exportTracksToCSV(String filename) {
        try (Session session = HibernateUtil.getSessionFactory().openSession();
             BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {

            String hql = """
                    SELECT DISTINCT t
                    FROM Track t
                    JOIN FETCH t.album a
                    JOIN FETCH a.artist
                    """;

            List<Track> tracks = session.createQuery(hql, Track.class).list();

            writer.write("ID,Tytul,Artysta,Album,CzasSekundy,Odtworzenia");
            writer.newLine();

            for (Track t : tracks) {
                writer.write(
                        t.getId() + "," +
                                t.getTitle() + "," +
                                t.getAlbum().getArtist().getName() + "," +
                                t.getAlbum().getTitle() + "," +
                                t.getDurationSeconds() + "," +
                                t.getPlayCount()
                );
                writer.newLine();
            }

            System.out.println("Eksport zakończony: " + filename);

        } catch (IOException e) {
            throw new RuntimeException("Błąd eksportu CSV", e);
        }
    }
}