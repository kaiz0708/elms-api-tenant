package com.elms.api;

import com.elms.api.dto.account.LoginAuthDto;
import com.elms.api.service.feign.FeignAccountAuthService;
import com.elms.api.service.feign.FeignConst;
import com.elms.api.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy
@Slf4j
//@ComponentScan(basePackages = {"com.elms.api"})
@EnableFeignClients
@EnableConfigurationProperties({LiquibaseProperties.class})
public class Application {

	@Autowired
	FeignAccountAuthService accountAuthService;

	@Autowired
	UserServiceImpl userService;

	@Value("${auth.internal.username}")
	private String username;
	@Value("${auth.internal.password}")
	private String password;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


	@PostConstruct
	public void initialize() {
		MultiValueMap<String,String> request = new LinkedMultiValueMap<>();
		request.add("grant_type","password");
		request.add("username",username);
		request.add("password",password);
		LoginAuthDto result = accountAuthService.authLogin(FeignConst.LOGIN_TYPE_INTERNAL,request);
		if(result == null || result.getAccessToken() == null){
			throw new RuntimeException("APPLICATION FAILED TO START: CAN NOT GET KEY ");
		}
		userService.AUTH_SERVER_TOKEN = result.getAccessToken();
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
}
