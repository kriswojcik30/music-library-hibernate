package model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artists")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String country;
    private int debutYear;
    private String genre;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Album> albums = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "artist_track",
            joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "track_id")
    )
    private List<Track> tracks = new ArrayList<>();

    // konstruktory
    public Artist() {}

    public Artist(String name, String country, int debutYear, String genre) {
        this.name = name;
        this.country = country;
        this.debutYear = debutYear;
        this.genre = genre;
    }

    // gettery i settery
    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public int getDebutYear() { return debutYear; }
    public void setDebutYear(int debutYear) { this.debutYear = debutYear; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public List<Album> getAlbums() { return albums; }
    public List<Track> getTracks() { return tracks; }
}