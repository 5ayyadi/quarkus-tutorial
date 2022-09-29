package com.core.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Book extends PanacheEntity {

    // @Id
    // @GeneratedValue
    // private Long id;

    @Column()
    private String title;

    // @ManyToMany
    // @JoinTable(name = "book_publisher", joinColumns = { @JoinColumn(name =
    // "book_id") }, inverseJoinColumns = {
    // @JoinColumn(name = "publisher_id") })
    // private Set<BookPublisher> bookPublishers = new HashSet<BookPublisher>();

    public void setTitle(String title) {
        this.title = title;
    }

    @OneToMany(mappedBy = "book")
    private Set<BookPublisher> bookPublishers = new HashSet<BookPublisher>();

    public Set<BookPublisher> getBookPublishers() {
        return bookPublishers;
    }

    // public Set<BookPublisher> getPublishers() {
    // return publishers;
    // }
}