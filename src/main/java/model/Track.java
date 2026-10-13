package model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tracks")
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private int durationSeconds;
    private int trackNumber;
    private long playCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;

    @ManyToMany(mappedBy = "tracks")
    private List<Artist> artists = new ArrayList<>();

    public Track() {}

    public Track(String title, int durationSeconds, int trackNumber, Album album) {
        this.title = title;
        this.durationSeconds = durationSeconds;
        this.trackNumber = trackNumber;
        this.album = album;
    }

    public Long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }

    public int getTrackNumber() { return trackNumber; }
    public void setTrackNumber(int trackNumber) { this.trackNumber = trackNumber; }

    public long getPlayCount() { return playCount; }
    public void setPlayCount(long playCount) { this.playCount = playCount; }

    public Album getAlbum() { return album; }
    public void setAlbum(Album album) { this.album = album; }

    public List<Artist> getArtists() { return artists; }
}