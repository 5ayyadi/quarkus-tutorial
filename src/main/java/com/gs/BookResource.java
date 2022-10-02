package com.gs;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.core.models.Book;
import com.core.models.BookPublisher;
import com.core.models.Publisher;

@Path("/book")
public class BookResource {

    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void transfer() {

        Book b = new Book();
        b.setTitle("Hibernate Tips - More than 70 solutions to common Hibernate problems 3");
        b.persist();

        Publisher p = new Publisher();
        p.setName("Thorben Janssen 2");
        p.persist();

        BookPublisher bp1 = new BookPublisher();
        bp1.format = "asdkjhajksh";
        bp1.book = b;
        bp1.publisher = p;
        b.getBookPublishers().add(bp1);
        p.getBookPublishers().add(bp1);

        BookPublisher bp2 = new BookPublisher();
        bp2.format = "asdkjhajksh";
        bp2.book = b;
        bp2.publisher = p;
        b.getBookPublishers().add(bp2);
        p.getBookPublishers().add(bp2);

        BookPublisher bp = new BookPublisher();
        bp.format = "asdkjhajksh";
        bp.book = b;
        bp.publisher = p;
        b.getBookPublishers().add(bp);
        p.getBookPublishers().add(bp);

        bp1.persist();
        bp2.persist();
        bp.persist();

    }

}
