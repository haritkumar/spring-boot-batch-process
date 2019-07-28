package com.batch.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.batch.util.AsycJobs;
import com.batch.util.FileUtil;

@RestController
public class RestEndpoints {

	private static final Logger logger = Logger.getLogger(RestEndpoints.class.getName());

	@Value("${batch.csv.dir}")
	private String csvDir;
	
	@Autowired
	AsycJobs asycJobs;
	
	@PostMapping("/upload")
	public ResponseEntity<String> uploadData(@RequestParam("file") MultipartFile file) throws Exception {
		if (file == null) {
			throw new RuntimeException("You must select the a file for uploading");
		}
		InputStream inputStream = file.getInputStream();
		String originalName = file.getOriginalFilename();
		String name = file.getName();
		String contentType = file.getContentType();
		long size = file.getSize();

		logger.info("inputStream: " + inputStream);
		logger.info("originalName: " + originalName);
		logger.info("name: " + name);
		logger.info("contentType: " + contentType);
		logger.info("size: " + FileUtil.readableFileSize(size));

		try {
			String n_name = csvDir+System.currentTimeMillis()+"_"+originalName;
			File write_file = new File(n_name);
			OutputStream out = new FileOutputStream(write_file);
			out.write(file.getBytes());
			out.close();
			asycJobs.processBatch(n_name);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>("Successfully uploaded "+originalName, HttpStatus.OK);
	}

	
}
