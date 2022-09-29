package com.core.models;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Table(name = "book_publisher")
@Entity
public class BookPublisher extends PanacheEntity {

    // @EmbeddedId
    // private BookPublisherId id = new BookPublisherId();

    // @ManyToOne
    // @MapsId("book_id")
    // @JoinColumn(name = "book_id")
    // public Book book;

    // @ManyToOne
    // @MapsId("publisher_id")
    // @JoinColumn(name = "publisher_id")
    // public Publisher publisher;

    public BookPublisher() {
    }

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    public Book book;

    @ManyToOne
    @JoinColumn(name = "publisher_id", nullable = false)
    public Publisher publisher;

    @Column(length = 255)
    public String format;

    public void setBook(Book book) {
        this.book = book;
        // this.id.setBookId(book.id);
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
        // this.getId().setPublisherId(publisher.id);
    }

    // public void setBcartook(Book book) {
    // this.book = book;
    // }

    // public void setFormat(String format) {
    // this.format = format;
    // }

    // public void setPublisher(Publisher publisher) {
    // this.publisher = publisher;
    // }

}