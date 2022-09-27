package com.core.models;

import javax.persistence.*;
import java.lang.annotation.Annotation;

@Entity
public class TokenBalances  implements ManyToMany{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Class targetEntity() {
        return null;
    }

    @Override
    public CascadeType[] cascade() {
        return new CascadeType[0];
    }

    @Override
    public FetchType fetch() {
        return null;
    }

    @Override
    public String mappedBy() {
        return null;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
