package com.elms.api.service.feign;


import com.elms.api.cfg.CustomFeignConfig;
import com.elms.api.dto.ApiMessageDto;
import com.elms.api.dto.dbConfig.DbConfigDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "dbconfig-svr", url = "http://localhost:8787", configuration = CustomFeignConfig.class)
public interface FeignDbConfigAuthService {

    @GetMapping(value = "/v1/db-config/get/{storeId}")
    ApiMessageDto<DbConfigDto> authGetByStoreId(@PathVariable("storeId") Long id);

    @GetMapping(value = "/v1/db-config/get_by_name")
    ApiMessageDto<DbConfigDto> authGetByName(@RequestParam(value = "name") String name);
}
