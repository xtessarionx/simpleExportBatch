package com.sat.demo.BatchDemoAgain;

import com.sat.demo.BatchDemoAgain.service.BatchDemoExportFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StopWatch;

@Slf4j
@SpringBootApplication
public class BatchDemoAgainApplication implements CommandLineRunner {
	@Value("${filetype}")
	private String filetype;

	@Autowired
	BatchDemoExportFileService batchDemoExportFileService;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(BatchDemoAgainApplication.class);
		app.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		log.info("Request export file type is: "+filetype);
		try{
			if(filetype.equalsIgnoreCase("csv")){
				batchDemoExportFileService.exportDBtoCSV();
			}
			else if(filetype.equalsIgnoreCase("txt")){
				batchDemoExportFileService.exportDBtoTxt();
			}
			else if(filetype.equalsIgnoreCase("pdf")){
				batchDemoExportFileService.exportDBtoPDF();
			}
			else {
				log.error("Illegal File type");
				System.exit(1);
			}
		}catch (Exception e){
			log.error("Exception:  ",e);
			System.exit(1);
		}finally{
			stopWatch.stop();
			log.info("Total time :{} seconds",stopWatch.getTotalTimeSeconds());
			System.exit(1);
		}
	}
}
