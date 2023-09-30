package com.elms.api.dto.category;

import com.elms.api.dto.ABasicAdminDto;
import lombok.Data;


@Data
public class CategoryDto extends ABasicAdminDto {
    private String name;
}
