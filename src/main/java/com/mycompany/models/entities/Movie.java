package com.mycompany.models.entities;

public class Movie {
    private int id;
    private String title;
    private String genre;
    private int yearOfRelease;

    public Movie(int id, String title, String genre, int yearOfRelease) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.yearOfRelease = yearOfRelease;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getYearOfRelease() {
        return yearOfRelease;
    }
}
