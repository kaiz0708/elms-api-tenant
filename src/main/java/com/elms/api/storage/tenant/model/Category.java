package com.elms.api.storage.tenant.model;

import com.elms.api.storage.base.Auditable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;


@Entity
@Table(name = "db_category")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Category extends Auditable<String> {
    private String name;

}
