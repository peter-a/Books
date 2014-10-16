package com.example.books.database.objects;

public class Book {
    private int id;
	private String isbn;
	private String title;
	private Author author;
	private Genre genre;
	
	public Book(int id, String isbn, String title, Author author, Genre genre) {
        this.id = id;
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.genre = genre;
	}

    public int getId() {
        return id;
    }

    public Author getAuthor() {
		return author;
	}
	
	public Genre getGenre() {
		return genre;
	}
	
	public String getIsbn() {
		return isbn;
	}
	
	public String getTitle() {
		return title;
	}
}
