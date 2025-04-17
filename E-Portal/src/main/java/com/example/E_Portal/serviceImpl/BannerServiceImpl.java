package com.example.e_portal.serviceImpl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.e_portal.constant.Constant;
import com.example.e_portal.service.BannerService;

import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.messages.Item;

@Service
public class BannerServiceImpl implements BannerService {

	@Autowired
	private MinioClient minioClient;

	@Override
	public ResponseEntity<String> uploadBanner(List<MultipartFile> banners) {
		try {
			for(MultipartFile file : banners) {
				// this folderName will store the image inside the folder
				String fileName = Constant.BANNERFOLDER_NAME + file.getOriginalFilename();

				boolean bucketExists = minioClient
						.bucketExists(io.minio.BucketExistsArgs.builder().bucket(Constant.BUCKET_NAME).build());
				if (!bucketExists) {
					System.out.println("Bucket not found. Creating bucket: " + Constant.BUCKET_NAME);
					minioClient.makeBucket(io.minio.MakeBucketArgs.builder().bucket(Constant.BUCKET_NAME).build());
				}

				// Get the image input stream
				InputStream inputStream = file.getInputStream();

				// Upload the file to s3
				minioClient.putObject(PutObjectArgs.builder()
						.bucket(Constant.BUCKET_NAME)
						.object(fileName)
						.stream(inputStream, file.getSize(), -1)
						.contentType(file.getContentType())
						.build()
					);
			}
			
			return ResponseEntity.status(HttpStatus.OK).body("Banner images uploaded successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Banner image upload failed: " + e.getMessage());
		}
	}

	@Override
	public ResponseEntity<String> deleteBanner(List<String> bannerImageName) {
		try {
			for(String bannerImage : bannerImageName) {
				String fileName = Constant.BANNERFOLDER_NAME + bannerImage;
				
				//check if the banner image exists in s3
				minioClient.statObject(
						io.minio.StatObjectArgs.builder()
						.bucket(Constant.BUCKET_NAME)
						.object(fileName)
						.build()
				      );
				
				RemoveObjectArgs object = RemoveObjectArgs.builder()
						.bucket(Constant.BUCKET_NAME)
						.object(fileName)
						.build();
				
				minioClient.removeObject(object);
			}
			
			return ResponseEntity.status(HttpStatus.OK).body("Banner image deleted successfully.");
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Banner image not found: " + e.getMessage());
		}
	}

	@Override
	public List<String> getAllBanners() {
		List<String> bannerUrls = new ArrayList<>();
		
		try {	
			Iterable<Result<Item>> objects = minioClient.listObjects(
					ListObjectsArgs
					.builder()
					.bucket(Constant.BUCKET_NAME)
					.prefix(Constant.BANNERFOLDER_NAME)
					.recursive(true)
					.build()
					);
			
			for(Result<Item> item : objects) {
				String objectName = item.get().objectName();
				String fileUrl = "http:locahost:9000/"+Constant.BUCKET_NAME+"/"+objectName;
				bannerUrls.add(fileUrl);
			}
		}catch(Exception e) {
			throw new RuntimeException("Error retrieving banners: "+e.getMessage(), e);
		}
		
		return bannerUrls;
	}

}
