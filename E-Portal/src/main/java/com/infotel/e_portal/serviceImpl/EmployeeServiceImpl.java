package com.infotel.e_portal.serviceImpl;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import com.infotel.e_portal.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.infotel.e_portal.constant.Constant;
import com.infotel.e_portal.dto.EmployeeDto;
import com.infotel.e_portal.model.Employee;
import com.infotel.e_portal.service.EmployeeService;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	@Autowired
	private EmployeeRepo employeeRepo;
	@Autowired
	private MinioClient minioClient;
	
//	private EmployeeServiceImpl(MinioConnection minioConnection) {
//		this.minioClient = minioConnection.createConnection();
//	}
	
	@Override
	public ResponseEntity<String> uploadImage(Integer empId, MultipartFile empImage) {
		try {
			Optional<Employee> employeeOptional = employeeRepo.findById(empId);
			if(employeeOptional.isEmpty()) 
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found!");
			
			Employee employee = employeeOptional.get();
			
			// this folderName will store the image inside the folder
			String folderName = Constant.IMAGEFOLDER_NAME + empId + "_" + empImage.getOriginalFilename();
			// this fileName will store imageUrl in database
			String fileName = empId + "_" + empImage.getOriginalFilename();
			
			boolean bucketExists = minioClient.bucketExists(io.minio.BucketExistsArgs.builder().bucket(Constant.BUCKET_NAME).build());
			if (!bucketExists) {
	            System.out.println("Bucket not found. Creating bucket: " + Constant.BUCKET_NAME);
	            minioClient.makeBucket(io.minio.MakeBucketArgs.builder().bucket(Constant.BUCKET_NAME).build());
	        }
			
			// Get the image input stream
			InputStream inputStream = empImage.getInputStream();
			
			// Upload the file to s3
			minioClient.putObject(
					PutObjectArgs.builder()
					.bucket(Constant.BUCKET_NAME)
					.object(folderName)
					.stream(inputStream, empImage.getSize(), -1)
					.contentType(empImage.getContentType())
					.build()
				);
			
			employee.setImageUrl(fileName);
			employeeRepo.save(employee);
			
			return ResponseEntity.status(HttpStatus.OK).body("Employee image uploaded successfully: " + empImage.getOriginalFilename());
		}catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Employee image upload failed: " + e.getMessage());
	    }
	}

	@Override
	public EmployeeDto addEmployee(EmployeeDto employeeDto) {
		Optional<Employee> employee = employeeRepo.findById(employeeDto.getEmpId());
		if(!employee.isEmpty()) {
			return null;
		}
		
		Employee emp = EmployeeDto.toEntity(employeeDto);
		employeeRepo.save(emp);
		
		return employeeDto;
	}

	@Override
	public List<EmployeeDto> getAllEmployees() {
		List<Employee> employees = employeeRepo.findAll();
		return employees.stream().map(emp -> EmployeeDto.toDTO(emp)).toList();
	}

}
