package com.example.e_portal.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


public interface BannerService {

	ResponseEntity<String> uploadBanner(List<MultipartFile> banners);

	ResponseEntity<String> deleteBanner(List<String> bannerImageName);

	List<String> getAllBanners();
	
}
