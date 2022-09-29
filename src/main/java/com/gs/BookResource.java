package com.gs;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.core.models.Book;
import com.core.models.BookPublisher;
import com.core.models.Publisher;

@Path("/book")
public class BookResource {

    PublisherRepository publisherRepository;
    BookPublisherRepository bookPublisherRepository;
    BookRepository bookRepository;

    public BookResource(PublisherRepository publisherRepository, BookRepository bookRepository,
            BookPublisherRepository bookPublisherRepository) {
        this.publisherRepository = publisherRepository;
        this.bookPublisherRepository = bookPublisherRepository;
        this.bookRepository = bookRepository;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getPublisherBooks(@QueryParam("title") String title, @QueryParam("name") String name) {
        // Publisher publisher = publisherRepository.findByName(titl);
        // if (publisher != null) {
        // Set<BookPublisher> bookPublishers = publisher.getBookPublishers();
        // System.out.println(bookPublishers.toArray()[0]);
        // // return bookPublishers.toArray();
        // }
        // return null;
        Book book = bookRepository.findByTitle(title);
        if (book != null) {
            String format = bookPublisherRepository.getBookFormat(title, name);
            System.out.println(format);
            return format;
            // Set<BookPublisher> bookPublishers = book.getBookPublishers();
            // for (BookPublisher bookPublisher : bookPublishers) {
            // // System.out.println(bookPublisher.book);
            // // System.out.println(bookPublisher.publisher);
            // }
        }
        return null;
    }

    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void Add() {

        Book b = new Book();
        b.setTitle("Hibernate");
        b.persist();

        Book b2 = new Book();
        b2.setTitle("Hibernate2");
        b2.persist();

        Publisher p = new Publisher();
        p.setName("Thorben Janssen 2");
        p.persist();

        BookPublisher bp1 = new BookPublisher();
        bp1.format = "350";
        bp1.setBook(b);
        bp1.setPublisher(p);

        BookPublisher bp2 = new BookPublisher();
        bp2.format = "150";
        bp2.setBook(b2);
        bp2.setPublisher(p);

        b.getBookPublishers().add(bp1);
        b2.getBookPublishers().add(bp2);
        p.getBookPublishers().add(bp1);
        p.getBookPublishers().add(bp2);

        // BookPublisher bp2 = new BookPublisher();
        // bp2.format = "asdkjhajksh";
        // bp2.book = b;
        // bp2.publisher = p;
        // b.getBookPublishers().add(bp2);
        // p.getBookPublishers().add(bp2);

        // BookPublisher bp = new BookPublisher();
        // bp.format = "asdkjhajksh";
        // bp.book = b;
        // bp.publisher = p;
        // b.getBookPublishers().add(bp);
        // p.getBookPublishers().add(bp);

        // bp2.persist();
        // bp.persist();
        bp1.persist();
        bp2.persist();

    }

}
