package com.elms.api.mapper;


import com.elms.api.storage.tenant.model.Category;
import com.elms.api.dto.category.CategoryDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToCategoryAllDto")
    CategoryDto entityToACategoryAllDto(Category category);
    @IterableMapping(elementTargetType = CategoryDto.class, qualifiedByName = "fromEntityToCategoryAllDto")
    List<CategoryDto> fromEntityToCategoryALlDtoList(List<Category> categories);
}
