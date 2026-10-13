# Music Library (Hibernate)

Java console application for managing a music library: artists, albums, tracks, playlists and album reviews, stored in a relational database through Hibernate ORM. Built as a university project at AGH University of Krakow (Computational Engineering), with a focus on object-relational mapping and diagnosing common persistence problems.

## Features

**Music library**
- Managing artists, albums, tracks, playlists and album reviews (create, read, update, delete)
- Playlists with ordered tracks (position on the playlist)
- Searching artists by genre, country and debut year

**Statistics**
- Per artist: number of albums, number of tracks, total duration (Criteria API with `GROUP BY`)
- Per album: number of reviews and average rating (HQL aggregate queries)

**Data export**
- CSV export of tracks with artist and album data
- Memory-efficient export of large datasets with `ScrollableResults`

## Data model

- Artist → Album → Track (one-to-many)
- Artist ↔ Track (many-to-many, featured artists, `artist_track` join table)
- Playlist ↔ Track (many-to-many through the `PlaylistTrack` entity with a position attribute)
- Album → Review (one-to-many, rating 1–10)
- Automatic `created_at` / `updated_at` timestamps through a shared `AuditableEntity` base class

## Persistence problems diagnosed and fixed

- **N+1 queries:** counted SQL queries in the logs and fixed them with `JOIN FETCH` and `@BatchSize`
- **Missing transaction:** data was not saved because `persist()` ran outside a transaction
- **LazyInitializationException:** loaded related data with `JOIN FETCH` instead of switching to `EAGER` fetching
- **Concurrent edits:** prevented lost updates with optimistic locking (`@Version`) and a clear error message
- **Aggregate queries:** fixed missing rows (`LEFT JOIN`) and double counting (`COUNT(DISTINCT ...)`), mapped results to a DTO
- **Unsafe queries:** replaced string concatenation in HQL with named parameters

## Tech stack

Java · Hibernate 6 / JPA (Jakarta Persistence) · H2 database · HQL · Criteria API · Maven

## How to run

Requirements: JDK 26 and Maven. Dependencies (Hibernate, H2) are downloaded automatically.

From IntelliJ IDEA: open the project and run `app.Main`.

From the terminal:

    mvn compile exec:java -Dexec.mainClass="app.Main"

`Main` runs a demonstration of all features step by step. The H2 database file is created automatically in the project folder on first run (`hibernate.hbm2ddl.auto = update`).

Note: console output is in Polish.

## What I learned

- Mapping a relational model to Java classes, including many-to-many relations with extra attributes
- Reading SQL logs to find performance problems instead of guessing
- Managing sessions and transactions correctly in Hibernate
- Choosing between fetching strategies and locking strategies based on observed behaviour
- Reviewing and fixing AI-generated persistence code: several tasks started from AI-generated code with deliberate bugs that had to be found, explained and fixed
