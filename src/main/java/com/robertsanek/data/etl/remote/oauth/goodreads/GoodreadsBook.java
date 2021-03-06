package com.robertsanek.data.etl.remote.oauth.goodreads;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "goodreads_books")
public class GoodreadsBook {

  @Id
  private Long id;
  private String isbn13;
  private String title;
  @Column(name = "author_name")
  private String authorName;
  @Column(name = "author_image_url")
  private String authorImageUrl;
  @Column(name = "year_published")
  private Long yearPublished;
  @Column(name = "added_on")
  private ZonedDateTime addedOn;
  @Column(name = "found_in_anki")
  private boolean foundInAnki;

  public Long getId() {
    return id;
  }

  public String getIsbn13() {
    return isbn13;
  }

  public String getTitle() {
    return title;
  }

  public String getAuthorName() {
    return authorName;
  }

  public String getAuthorImageUrl() {
    return authorImageUrl;
  }

  public Long getYearPublished() {
    return yearPublished;
  }

  public ZonedDateTime getAddedOn() {
    return addedOn;
  }

  public boolean isFoundInAnki() {
    return foundInAnki;
  }

  public static final class BookBuilder {

    private Long id;
    private String isbn13;
    private String title;
    private String authorName;
    private String authorImageUrl;
    private Long yearPublished;
    private ZonedDateTime addedOn;
    private boolean foundInAnki;

    private BookBuilder() {}

    public static BookBuilder aBook() {
      return new BookBuilder();
    }

    public BookBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public BookBuilder withIsbn13(String isbn13) {
      this.isbn13 = isbn13;
      return this;
    }

    public BookBuilder withTitle(String title) {
      this.title = title;
      return this;
    }

    public BookBuilder withAuthorName(String authorName) {
      this.authorName = authorName;
      return this;
    }

    public BookBuilder withAuthorImageUrl(String authorImageUrl) {
      this.authorImageUrl = authorImageUrl;
      return this;
    }

    public BookBuilder withYearPublished(Long yearPublished) {
      this.yearPublished = yearPublished;
      return this;
    }

    public BookBuilder withAddedOn(ZonedDateTime addedOn) {
      this.addedOn = addedOn;
      return this;
    }

    public BookBuilder withFoundInAnki(boolean foundInAnki) {
      this.foundInAnki = foundInAnki;
      return this;
    }

    public GoodreadsBook build() {
      GoodreadsBook goodreadsBook = new GoodreadsBook();
      goodreadsBook.isbn13 = this.isbn13;
      goodreadsBook.authorName = this.authorName;
      goodreadsBook.foundInAnki = this.foundInAnki;
      goodreadsBook.id = this.id;
      goodreadsBook.authorImageUrl = this.authorImageUrl;
      goodreadsBook.title = this.title;
      goodreadsBook.addedOn = this.addedOn;
      goodreadsBook.yearPublished = this.yearPublished;
      return goodreadsBook;
    }
  }
}
