package com.mannash.simcardvalidation.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mannash.simcardvalidation.pojo.RequestClientLogPojo;
import com.mannash.simcardvalidation.service.LoggerService;
import com.mannash.simcardvalidation.service.SimVerifyServerCommunicationServiceImpl;
import com.mannash.simcardvalidation.pojo.RequestClientLogPojo;
//import com.mannash.trakme.client.pojo.RequestClientLogPojo;
//import com.mannash.trakme.client.service.LoggerService;
//import com.mannash.trakme.client.service.TrakmeServerCommunicationServiceImpl;

public class FieldTestingLoggerThread extends Thread {

	private LoggerService loggerService;
	private SimVerifyServerCommunicationServiceImpl simVerifyServerCommunicationServiceImpl;
	private static final String SPACE_SEPARATOR = " ";
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	

	public FieldTestingLoggerThread(LoggerService loggerService, SimVerifyServerCommunicationServiceImpl simVerifyServerCommunicationServiceImpl) {
		this.loggerService=loggerService;
		this.simVerifyServerCommunicationServiceImpl = simVerifyServerCommunicationServiceImpl;
	}

	public void run() {
		while (true) {

			try {
				List<RequestClientLogPojo> requestLogPojos = new ArrayList<RequestClientLogPojo>();
				int count = 0;
				while(count < 100)
				{
					count++;
//					RequestClientLogPojo requestLogPojo = loggerService.pollLogs();
					RequestClientLogPojo requestLogPojo = loggerService.pollLogs();
					if(requestLogPojo != null)
					{
						requestLogPojos.add(requestLogPojo);
					}
					else
					{
						break;
					}
				}
				if(requestLogPojos.size() > 0)
				{
					
//					File file = new File("trakme_client_"+simpleDateFormat.format(new Date())+".log");
//					if(!file.exists())
//					{
//						file.createNewFile();
//					}
//					try (FileWriter fw = new FileWriter(file, true);
//						       BufferedWriter bw = new BufferedWriter(fw)) {
//						
//						for(RequestLogPojo fieldTestingLogPojo : requestLogPojos)
//						{
//							String logLine = "[" + fieldTestingLogPojo.getLogType() + "]" + SPACE_SEPARATOR
//									+ fieldTestingLogPojo.getLogMessage();
//							 bw.write(logLine);
//						     bw.newLine();
//						}
//					}
					simVerifyServerCommunicationServiceImpl.sendLogsToServer(requestLogPojos);
					
					requestLogPojos.clear();
				}
				
				Thread.sleep(1000);
				
			} catch (Throwable e) {
				System.out.println("Exception occured in FieldTestingLoggerThread " +e.getMessage());
				//loggerService.logError("Exception occured in FieldTestingLoggerThread "+e.getMessage());
			}
		}
	}

}
