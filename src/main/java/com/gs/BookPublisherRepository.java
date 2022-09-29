package com.gs;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import com.core.models.BookPublisher;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class BookPublisherRepository implements PanacheRepository<BookPublisher> {

    public String getBookFormat(String title, String name) {
        // TODO - Makes this dynamic
        String q = String.format(
                "SELECT format from book JOIN book_publisher bp on book.id = bp.book_id join publisher p on p.id = bp.publisher_id where book.title like '%s' and name like '%s'",
                title, name);
        Query query = this.getEntityManager().createNativeQuery(q);
        return (String) query.getSingleResult();

    }
}
