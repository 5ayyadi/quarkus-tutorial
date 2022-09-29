package com.gs;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Entity;

import org.hibernate.Session;

import com.core.models.Book;
import com.core.models.Publisher;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class BookRepository implements PanacheRepository<Book> {

    public Book findByTitle(String title) {
        return find("lower(title)", title.toLowerCase()).firstResult();
    }

}
