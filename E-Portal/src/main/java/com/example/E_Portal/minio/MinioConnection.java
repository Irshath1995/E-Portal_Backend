package com.example.e_portal.minio;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;

@Configuration
public class MinioConnection {
	
	@Bean
	public MinioClient createConnection() {
		try {
			MinioClient minioClient = MinioClient.builder()
					.endpoint("http://localhost:9000")
					.credentials("admin", "password")
					.build();
			
			System.out.println("Connetion success");
			return minioClient;
		}catch(Exception e){
			System.out.println("Connetion failed: " + e.getMessage());
		}
		return null;
	}
	
}
