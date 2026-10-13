package model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int rating; // skala 1-10

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

    @Column(name = "review_date", nullable = false)
    private LocalDate reviewDate;

    @Column(length = 1000)
    private String comment;

    public Review() {}

    public Review(int rating, Album album, LocalDate reviewDate, String comment) {
        this.rating = rating;
        this.album = album;
        this.reviewDate = reviewDate;
        this.comment = comment;
    }

    public Long getId() { return id; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public Album getAlbum() { return album; }
    public void setAlbum(Album album) { this.album = album; }

    public LocalDate getReviewDate() { return reviewDate; }
    public void setReviewDate(LocalDate reviewDate) { this.reviewDate = reviewDate; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}