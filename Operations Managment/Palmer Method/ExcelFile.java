package com.palmer.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/*
 * This class defines the excel file object that is the input for this program
 * the excel file object will contain all the information about the jobs and machines.
 */

public class ExcelFile {

	//excel file object properties
	private List<Machine> machines; //the list of machines in the excel file (rows)
	private List<Job> jobs; //the list of the jobs in the excel file (columns)
	private String debugStr; //a string contains descriptions of the program stages
	
	//ExcelFile object constructor
	public ExcelFile() {
		this.machines = new ArrayList<Machine>();
		this.jobs = new ArrayList<Job>();
		this.debugStr = "";
	}
	
	/*
	 * getters
	 * complexity - O(1)
	 */
	public List<Machine> getMachines(){
		return this.machines;
	}
	
	public List<Job> getJobs(){
		return this.jobs;
	}
	
	public String getDebug() {
		return this.debugStr;
	}
	
	/*
	 * init method gets a file path to the excel file and initialize the excel file object according to the file in the path.
	 * the method creates the machines and jobs lists according to file in the path.
	 * complexity - O(n * m)
	 */
	public void init(String filePath) {
        try {
            File file = new File(filePath); //creating a new file instance
            FileInputStream fis = new FileInputStream(file); //obtaining bytes from the file
            //creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0); //creating a Sheet object to retrieve object
            Iterator < Row > itr = sheet.iterator(); //iterating over excel file
            int rowCounter = 0;
            //while there are more rows in the file
            while (itr.hasNext()) {
                Row row = itr.next();
                rowCounter++;
                Iterator < Cell > cellIterator = row.cellIterator(); //iterating over each column
                int cellCounter = 0;
                //while there are more cells in the row
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    cellCounter++;
                    String cellValue; //will contain the cell value as string
                    if(cell.getCellType() == CellType.STRING) {
                    	cellValue = cell.getStringCellValue();
                    }else {
                    	//Cell Type == NUMERIC
                    	//convert numeric to int and than to string
                    	cellValue = String.valueOf(((int) cell.getNumericCellValue()));
                    }
                    if(rowCounter == 1) { //first row contains the jobs names
                    	if(cellCounter != 1) { //column A1 is the corner
                    		if(this.isJobExist(cellValue)) { //no duplicate jobs names allowed
                    			System.out.println("duplicate jobs names");
                    			System.exit(0);
                    		}else{
                    			//add the job to the jobs list
                    			Job newJob = new Job(cellValue);
                    			this.addJob(newJob);
                    		}
                    	}
                    }else {//not first row
                    	if(cellCounter == 1) { //in the first cell there are the machines names
                    		if(this.isMachineExist(cellValue)) { //no duplicate machines names allowed
                    			System.out.println("duplicate machines names");
                    			System.exit(0);
                    		}else {
                    			//add the machine to the machines list
                    			Machine newMachine = new Machine(cellValue);
                    			this.addMachine(newMachine);
                    		}
                    	}else {
                    		//in the rest of the columns there are the timings.
                    		this.jobs.get((cellCounter - 2)).addTime(Integer.parseInt(cellValue));
                    	}
                    }
                }
            }
            wb.close(); //close work book object
            this.createDebugStr(); //create debug string
        } catch (Exception e) { //catch exception for the file methods
            e.printStackTrace();
        }
    }
	
	/*
	 * this method is called at the end of init function. this method is starting to add the descriptions to the debug string.
	 * starts with the list of jobs and the list of machines.
	 * complexity - O(max(n,m))
	 */
	public void createDebugStr() {
		int i = 0;
		//add openning sentence and jobs list.
		this.debugStr = "Full Palmer Module Stages Traces:\n\nDetails:\nJobs (" + this.jobs.size() + "):\n";
		while(i < this.jobs.size()) {
			this.debugStr += this.jobs.get(i).getJobName() + "\n";
			i++;
		}
		i = 0;
		//add machines list
		this.debugStr += "\nMachines (" + this.machines.size() + "):\n";
		while(i < this.machines.size()) {
			this.debugStr += this.machines.get(i).getMachineName() + "\n";
			i++;
		}
	}
	
	/*
	 * addDebugStr is an instance method that get a string and add it to the existing debug string of the excel file object.
	 * complexity - O(1)
	 */
	public void addDebugStr(String str) {
		this.debugStr += str;
	}
	
	/*
	 * isJobExist is an instance method that gets a job name as string, it checks if this job name is already exists in the instance's jobs list.
	 * complexity - O(n)
	 */
	public Boolean isJobExist(String jobName) {
		int i = 0;
		while(i < this.jobs.size()) {
			if(this.jobs.get(i).getJobName().equals(jobName)) {
				return true;
			}
			i++;
		}
		return false;
	}

	/*
	 * isMachineExist is an instance method that gets a machine name as string, it checks if this machine name is already exists in the instance's machines list.
	 * complexity - O(n)
	 */
	public Boolean isMachineExist(String machineName) {
		int i = 0;
		while(i < this.machines.size()) {
			if(this.machines.get(i).getMachineName().equals(machineName)) {
				return true;
			}
			i++;
		}
		return false;
	}

	/*
	 * addMachine is an instance method that gets a machine object and add it to the excelFile instance's machines list
	 * complexity - O(1)
	 */
	public void addMachine(Machine machine) {
		this.machines.add(machine);
	}

	/*
	 * addJob is an instance method that gets a job object and add it to the excelFile instance's jobs list
	 * complexity - O(1)
	 */
	public void addJob(Job job) {
		this.jobs.add(job);
	}
	
	/*
	 * getResult is an instance method.
	 * the function iterate the instance's jobs list and returns a list of the jobs ordered by their score. the list returns as string.
	 * the function add the result to the debug string also
	 * complexity - O(n^2)
	 */
	public String getResult() {
		String result; //the result string with the scheduled jobs
		int i; //jobs list index
		int currentMaxScore; //will save the max score while iterating the jobs list
		int maxJobIndex = 0; //will save the current job's index with the max score
		List<String> usedJobs = new ArrayList<String>(); //list to save the jobs that scheduled
		result = "The scheduling according to Palmer's module is:\n"; //open sentence for the result file
		this.addDebugStr("\n\n\nStage 3: Scheduling The Jobs By Descending Scores\n\n"); //description for the debug file
		while(usedJobs.size() != this.jobs.size()) { //loop until all jobs are scheduled
			i = 0; //init jobs index
			currentMaxScore = Integer.MIN_VALUE; //init max score
			for(Job job : jobs) { //iterate the jobs
				if(!usedJobs.contains(job.getJobName())) { //check that job not scheduled yet
					if(job.getJobScore() > currentMaxScore) { //check if this is the current max job
						currentMaxScore = job.getJobScore(); //save the score for next iteration
						maxJobIndex = i; //save the current job index
					}
				}
				i++;
			}
			usedJobs.add(this.jobs.get(maxJobIndex).getJobName()); //add the job to scheduled list
			result += this.jobs.get(maxJobIndex).getJobName(); //add the job name to result string
			this.addDebugStr(this.jobs.get(maxJobIndex).getJobName()); //add the job name to the debug string
			if(usedJobs.size() != this.jobs.size()) { //check if there are more jobs
				//add separators to jobs strings
				result += " -> ";
				this.addDebugStr(" -> ");
			}
		}
		return result;
	}
}//end of class ExcelFile
