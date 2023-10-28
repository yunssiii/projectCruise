package com.cruise.project_cruise;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.sql.DataSource;

@SpringBootApplication
public class ProjectCruiseApplication {

	public static void main(String[] args) {

		SpringApplication.run(ProjectCruiseApplication.class, args);
	}


	// dataSource와 mapper 파일들을 포함하고 있는 SqlSessionFactory 객체를 반환하는 메소드
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {

		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();

		sessionFactoryBean.setDataSource(dataSource);

		//sql의 위치정보 읽어오기 (org.springframework.core.io.Resource)
		Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/*.xml");
		// src/main/resource/mybatis/mapper 폴더에 있는 xml파일들을 읽어낼 것
		sessionFactoryBean.setMapperLocations(resources);
		// SessionFactory의 객체 내에는 dataSource와 sql이 담긴 resource가 담김

		return sessionFactoryBean.getObject(); // 객체화 시켜서 내보내기

	}

}
