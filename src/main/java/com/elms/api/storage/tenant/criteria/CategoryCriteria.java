package com.elms.api.storage.tenant.criteria;

import com.elms.api.storage.tenant.model.Category;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


@Data
public class CategoryCriteria {

    private Long id;
    private String name;
    private Integer status;

    public Specification<Category> getCriteria() {
        return new Specification<Category>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(getId() != null){
                    predicates.add(cb.equal(root.get("id"), getId()));
                }

                if(getStatus() != null){
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }

                if(getName() != null){
                    predicates.add(cb.like(cb.lower(root.get("name")),"%"+ getName()+"%"));
                }
                query.orderBy(cb.asc(root.get("rank")));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
