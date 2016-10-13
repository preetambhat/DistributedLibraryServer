package com.opteamix.library.common;

import java.util.Date;
import java.util.List;

public class Book {

	private String book_id;
	private String name;
	private String author;
	private String description;
	private Date publishedDate;
	private String publisher_text;
	private List<Author> author_data;
	private String language;
	private String publisher_name;
	private String publisher_id;
	private String isbn10;
	private String title;
	
	

	public String getBook_id() {
		return book_id;
	}
	public void setBook_id(String book_id) {
		this.book_id = book_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getPublishedDate() {
		return publishedDate;
	}
	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}
	public String getPublisher_text() {
		return publisher_text;
	}
	public void setPublisher_text(String publisher_text) {
		this.publisher_text = publisher_text;
	}
	public List<Author> getAuthor_data() {
		return author_data;
	}
	public void setAuthor_data(List<Author> author_data) {
		this.author_data = author_data;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getPublisher_name() {
		return publisher_name;
	}
	public void setPublisher_name(String publisher_name) {
		this.publisher_name = publisher_name;
	}
	public String getPublisher_id() {
		return publisher_id;
	}
	public void setPublisher_id(String publisher_id) {
		this.publisher_id = publisher_id;
	}
	public String getIsbn10() {
		return isbn10;
	}
	public void setIsbn10(String isbn10) {
		this.isbn10 = isbn10;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
