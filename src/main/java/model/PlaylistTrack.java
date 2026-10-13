package model;

import jakarta.persistence.*;

@Entity
@Table(name = "playlist_track")
public class PlaylistTrack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id", nullable = false)
    private Track track;

    @Column(nullable = false)
    private int position;

    public PlaylistTrack() {}

    public PlaylistTrack(Playlist playlist, Track track, int position) {
        this.playlist = playlist;
        this.track = track;
        this.position = position;
    }

    public Long getId() { return id; }

    public Playlist getPlaylist() { return playlist; }
    public void setPlaylist(Playlist playlist) { this.playlist = playlist; }

    public Track getTrack() { return track; }
    public void setTrack(Track track) { this.track = track; }

    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
}