package app;

import dao.*;
import model.*;
import util.HibernateUtil;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        ArtistDAO artistDAO = new ArtistDAO();
        AlbumDAO albumDAO = new AlbumDAO();
        TrackDAO trackDAO = new TrackDAO();
        PlaylistDAO playlistDAO = new PlaylistDAO();
        ReviewDAO reviewDAO = new ReviewDAO();
        StatisticsDAO statisticsDAO = new StatisticsDAO();
        ExportDAO exportDAO = new ExportDAO();

        System.out.println("=== START TESTU PROJEKTU ===");

        // 1. Dodanie artystów
        Artist travis = new Artist("Travis Scott", "USA", 2013, "Hip-Hop");
        Artist weeknd = new Artist("The Weeknd", "Canada", 2010, "Pop/R&B");

        artistDAO.save(travis);
        artistDAO.save(weeknd);

        System.out.println("\nDodano artystów.");

        // 2. Odczyt wszystkich artystów
        System.out.println("\n=== WSZYSCY ARTYŚCI ===");
        for (Artist a : artistDAO.findAll()) {
            System.out.println(a.getId() + " | " + a.getName() + " | " + a.getCountry() + " | " + a.getGenre());
        }

        // 3. FindById
        System.out.println("\n=== SZUKANIE ARTYSTY PO ID ===");
        Artist foundArtist = artistDAO.findById(travis.getId());
        System.out.println("Znaleziono: " + foundArtist.getName());

        // 4. Filtrowanie po kraju
        System.out.println("\n=== ARTYŚCI Z USA ===");
        for (Artist a : artistDAO.findByCountry("USA")) {
            System.out.println(a.getName());
        }

        // 5. Filtrowanie po gatunku
        System.out.println("\n=== ARTYŚCI HIP-HOP ===");
        for (Artist a : artistDAO.findByGenre("Hip-Hop")) {
            System.out.println(a.getName());
        }

        // 6. Update artysty
        System.out.println("\n=== UPDATE ARTYSTY ===");
        travis.setGenre("Rap");
        artistDAO.update(travis);

        Artist updatedArtist = artistDAO.findById(travis.getId());
        System.out.println("Po zmianie gatunek Travisa: " + updatedArtist.getGenre());

        // 7. Dodanie albumów
        Album astroworld = new Album("Astroworld", 2018, "Hip-Hop", travis);
        Album afterHours = new Album("After Hours", 2020, "Pop", weeknd);

        albumDAO.save(astroworld);
        albumDAO.save(afterHours);

        System.out.println("\nDodano albumy.");

        // 8. Albumy danego artysty
        System.out.println("\n=== ALBUMY TRAVISA ===");
        for (Album a : albumDAO.findByArtistId(travis.getId())) {
            System.out.println(a.getTitle() + " | " + a.getReleaseYear());
        }

        // 9. Dodanie utworów
        Track t1 = new Track("SICKO MODE", 312, 1, astroworld);
        Track t2 = new Track("STARGAZING", 270, 2, astroworld);
        Track t3 = new Track("Blinding Lights", 200, 1, afterHours);
        Track t4 = new Track("Save Your Tears", 215, 2, afterHours);

        t1.setPlayCount(1000);
        t2.setPlayCount(600);
        t3.setPlayCount(2000);
        t4.setPlayCount(1500);

        trackDAO.save(t1);
        trackDAO.save(t2);
        trackDAO.save(t3);
        trackDAO.save(t4);

        System.out.println("\nDodano utwory.");

        // 10. Utwory z albumu
        System.out.println("\n=== UTWORY Z ALBUMU ASTROWORLD ===");
        for (Track t : trackDAO.findByAlbumId(astroworld.getId())) {
            System.out.println(t.getTrackNumber() + ". " + t.getTitle() + " | " + t.getDurationSeconds() + " s");
        }

        // 11. Najpopularniejsze utwory
        System.out.println("\n=== TOP 3 UTWORY ===");
        for (Track t : trackDAO.findTopTracks(3)) {
            System.out.println(t.getTitle() + " | odtworzenia: " + t.getPlayCount());
        }

        // 12. Playlista
        Playlist playlist = new Playlist("Moja Playlista", "Krzys", LocalDate.now());
        playlistDAO.save(playlist);

        playlistDAO.addTrackToPlaylist(playlist.getId(), t1.getId(), 1);
        playlistDAO.addTrackToPlaylist(playlist.getId(), t3.getId(), 2);
        playlistDAO.addTrackToPlaylist(playlist.getId(), t4.getId(), 3);

        System.out.println("\n=== ZAWARTOŚĆ PLAYLISTY ===");
        List<PlaylistTrack> entries = playlistDAO.getEntries(playlist.getId());

        for (PlaylistTrack pt : entries) {
            System.out.println(pt.getPosition() + ". " + pt.getTrack().getTitle());
        }

        // 13. Recenzje
        Review r1 = new Review(9, astroworld, LocalDate.now(), "Świetny album");
        Review r2 = new Review(8, astroworld, LocalDate.now(), "Bardzo dobry");
        Review r3 = new Review(10, afterHours, LocalDate.now(), "Bardzo mocny album");
        Review r4 = new Review(9, afterHours, LocalDate.now(), "Super klimat");

        reviewDAO.save(r1);
        reviewDAO.save(r2);
        reviewDAO.save(r3);
        reviewDAO.save(r4);

        System.out.println("\nDodano recenzje.");

        // 14. Statystyki recenzji HQL
        System.out.println("\n=== STATYSTYKI RECENZJI ===");

        for (Object[] row : reviewDAO.getReviewStats()) {
            String albumTitle = (String) row[0];
            String artistName = (String) row[1];
            Long count = (Long) row[2];
            Double avg = (Double) row[3];

            System.out.println(albumTitle + " | " + artistName +
                    " | liczba recenzji: " + count +
                    " | średnia: " + avg);
        }

        // 15. Criteria API
        System.out.println("\n=== STATYSTYKI ARTYSTÓW — CRITERIA API ===");

        for (Object[] row : statisticsDAO.getArtistStatistics()) {
            String artistName = (String) row[0];
            Long albumCount = (Long) row[1];
            Long trackCount = (Long) row[2];

            Number totalDurationNumber = (Number) row[3];
            long totalDuration = totalDurationNumber != null ? totalDurationNumber.longValue() : 0L;

            System.out.println(
                    artistName +
                            " | albumy: " + albumCount +
                            " | utwory: " + trackCount +
                            " | czas łącznie: " + totalDuration + " s"
            );
        }

        // 16. Eksport CSV
        System.out.println("\n=== EKSPORT CSV ===");
        exportDAO.exportTracksToCSV("tracks.csv");

        System.out.println("\n=== KONIEC TESTU PROJEKTU ===");

        HibernateUtil.shutdown();
    }
}