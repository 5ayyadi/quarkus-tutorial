package com.core.models;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.Id;

@Embeddable
public final class BookPublisherId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long bookId;
    private Long publisherId;

    public BookPublisherId() {
        System.out.print("Constructor ... ");
        System.out.print(this.bookId);
        System.out.println(this.publisherId);
    }

    public BookPublisherId(Long bookId, Long publisherId) {
        super();
        System.out.println("HERE!");
        this.bookId = bookId;
        this.publisherId = publisherId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long id) {
        this.bookId = id;
        System.out.println(id);
    }

    public Long getPublisherId() {
        // System.out.println("HERE!");

        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((bookId == null) ? 0 : bookId.hashCode());
        result = prime * result
                + ((publisherId == null) ? 0 : publisherId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BookPublisherId other = (BookPublisherId) obj;
        return Objects.equals(getBookId(), other.getBookId())
                && Objects.equals(getPublisherId(), other.getPublisherId());
    }
}