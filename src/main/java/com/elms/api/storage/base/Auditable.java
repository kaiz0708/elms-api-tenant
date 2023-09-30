package com.elms.api.storage.base;


import com.elms.api.storage.id.DateGenerator;
import com.elms.api.storage.id.ModifiedDateGenerator;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public class Auditable<T> {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "com.elms.api.storage.id.IdGenerator")
    @GeneratedValue(generator = "idGenerator")
    private Long id;

    @CreatedBy
    @Column(name = "created_by" ,nullable = false, updatable = false)
    private T createdBy;

    @Column(name = "created_date" ,nullable = false, updatable = false)
    private Date createdDate;

    @LastModifiedBy
    @Column(name = "modified_by",nullable = false)
    private T modifiedBy;

    @Column(name = "modified_date",nullable = false)
    private Date modifiedDate;

    private int status = 1;

    @PrePersist
    protected void onCreate() {
        DateGenerator dateGenerator = new DateGenerator();
        ModifiedDateGenerator modifiedDateGenerator = new ModifiedDateGenerator();
        createdDate = (Date) dateGenerator.generate(null, this);
        modifiedDate = (Date) modifiedDateGenerator.generate(null, this);
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedDate = new Date();
    }
}
