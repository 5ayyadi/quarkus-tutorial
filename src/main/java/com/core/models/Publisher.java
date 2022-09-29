package com.core.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Publisher extends PanacheEntity {

    // @Id
    // @GeneratedValue
    // private Long id;

    @Column()
    private String name;

    // @OneToMany(mappedBy = "publisher")
    // private Set<BookPublisher> books = new HashSet<>();

    // @OneToMany(mappedBy = "publisher")
    // private Set<BookPublisher> bookPublishers = new HashSet<>();

    @OneToMany(mappedBy = "publisher")
    private Set<BookPublisher> bookPublishers = new HashSet<BookPublisher>();

    public void setName(String name) {
        this.name = name;
    }

    public Set<BookPublisher> getBookPublishers() {
        return bookPublishers;
    }

    // public Set<BookPublisher> getBookPublishers() {
    // return bookPublishers;
    // }

    // public Set<BookPublisher> getBooks() {
    // return books;
    // }
}