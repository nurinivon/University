package com.palmer.main;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.io.FileWriter;

/*
 * Nur Nivon 2021
 * 
 * This class is the main class for the Palmer Module Program.
 * This is the interface of the program.
 * 
 * The program  gets as an input an ".xlsx" file. The file should be contain the followings:
 * The first row contains the jobs names
 * The first column contains the machines names
 * The rest of the table will contain the timings for each job on a machine
 * 
 * Excel file Schema:
 * 
 * machines\jobs	J1		J2		J3... 	Jn
 * M1				t(1,1)	t(2,1)
 * 
 * M2								t(3,2)
 * 
 * M3
 * .
 * .
 * .
 * Mm										t(n,m)
 * 
 * 
 * The program's output is the following:
 * "result.txt" file with the scheduled jobs
 * (Optional) "debug.txt" file with descriptions of all the program's stages (could be done with "-d" argument for the program)
 */
public class MainApp {

	//constant variables for the program
	final static String RESULT_FILE_NAME = "result.txt";
	final static String DEBUG_FILE_NAME = "debug.txt";
	final static String SCHEDULE_FILE_NAME = "schedule.xlsx";
	
	/*
	 * The interface for the program.
	 * The method create an excel file instance and perform the Palmer's Module on the machines and jobs in the excel file.
	 * The Method provides a "result.txt" file with the scheduled jobs.
	 * Running the method with "-d" argument will provide "debug.txt" with full descriptions of all the program's stages
	 */
	public static void main(String args[]) {
		int numberOfMachines;
		int numberOfJobs;
		ExcelFile excelFile = new ExcelFile(); //local excel file instance
		excelFile.init(SCHEDULE_FILE_NAME); //init excel file instance with the excel file path
		numberOfMachines = excelFile.getMachines().size();
		numberOfJobs = excelFile.getJobs().size();
		Boolean debugMode = false; //debug mode flag
		//check arguments for debug flag "-d"
		if(args.length > 0) {
			if(args.length > 1) {//only 1 argument or 0 allowed
				print("\nto many arguments.");
				System.exit(0);
			}
			if(args[0].equals("-d")) { //only "-d" allowed
				debugMode = true;
			}else {
				print("\nundefined argument");
				System.exit(0);				
			}
		}
		if(numberOfMachines > 0) { //check there are machines in the file
			if(numberOfJobs > 0) { //check there are jobs in the file
				setMachinesWeight(excelFile); //1st stage in Palmer's Module - calculate machines' weights
				setJobsScore(excelFile); //2nd step in Palmer's Module - calculate jobs' score
				createResultFile(excelFile.getResult()); //provide the "result.txt" file with the scheduled jobs
				if(debugMode) {
					createDebugFile(excelFile.getDebug()); //provide the "debug.txt" with full descriptions of all the program's stages
				}
			}else {
				print("\nno jobs specified.");
			}
		}else {
			print("\n no machines specified.");
		}
	}
	
	/*
	 * setMachinesWeight is implementation of the Palmer's method's 1st stage. for each machine we will calculate it's weight. this will allow us move on to second stage.
	 * The machines's weight calculated as the following:
	 * Wi = (2 * i) - m - 1
	 * (2 * the machine's index) - number of machines - 1
	 * The method gets an excel file instance as an input. it contains the machines list
	 * complexity - O(m)
	 */
	public static void setMachinesWeight(ExcelFile excelFile) {
		List<Machine> machines = excelFile.getMachines(); //the machines list in the excel
		int i = 1;
		excelFile.addDebugStr("\n\nStage 1: Setting The Machines Weights. Wi = (2 * i) - m - 1"); //add description to debug file
		for(Machine machine : machines) { //iterate all machines
			excelFile.addDebugStr("\n\n" + machine.getMachineName() + ":\n"); //add debug description
			excelFile.addDebugStr("W" + i + " = (2 * " + i + ") - " + machines.size() + " - 1 = " + ((2 * i) - machines.size() - 1)); //add debug description
            machine.setMachineWeight((2 * i) - machines.size() - 1); //update the machine's weight
			i++;
		}
	}
	
	/*
	 * setJobsScore is implementation of the Palmer's method's 2nd stage. for each job it will calculate it's score, this would be the parameter for ordering the schedule.
	 * The Job's score calculated as the following:
	 * Si = sum(Wj * tj)
	 * sum of all (machine's weight * the job's time for the machine)
	 * The method gets an excel file instance as an input. it contains the jobs and machines lists
	 * complexity - O(n * m)
	 */
	public static void setJobsScore(ExcelFile excelFile) {
		List<Job> jobs = excelFile.getJobs(); //the jobs list in the excel
		List<Machine> machines = excelFile.getMachines(); //the machines list in the excel
		excelFile.addDebugStr("\n\n\nStage 2: Setting The Jobs Scores. Si = sum(Wj * tj)"); //add description to debug file
		int i = 1; //the job index
		for(Job job : jobs) { //iterate all jobs
			if(job.getTimeList().size() == machines.size()) { //check number of machines in general is the same as number of timings for the jobs
				excelFile.addDebugStr("\n\n" + job.getJobName() + ":\n"); //add debug description
				excelFile.addDebugStr("S" + i + " ="); //add debug description
				int currentScore = 0; //init current score
				int machineIndex = 0; //init machine index
				while(machineIndex < machines.size()) { //iterate all machines
					excelFile.addDebugStr("(" + machines.get(machineIndex).getMachineWeight() + " * " + job.getTimeList().get(machineIndex) + ")"); //add debug description
					currentScore += job.getTimeList().get(machineIndex) * machines.get(machineIndex).getMachineWeight(); //add the current machine score to total score
					machineIndex++;
					if(machineIndex < machines.size()) { //to prevent " + " at the end of the debug string
						excelFile.addDebugStr(" + ");
					}
				}
				job.setJobScore(currentScore); //update the score for the job
				excelFile.addDebugStr(" = " + currentScore); //add debug comment
			}else {
				print("\nerror occurred in job: " + job.getJobName() + " not enough or too much machines timings.");
				System.exit(0);
			}
			i++;
		}
	}
	
	/*
	 * shortcut method for print to standard output
	 * complexity - O(1)
	 */
	public static void print(String str) {
		System.out.print(str);
	}

	/*
	 * createResultFile is a method that gets a string and creates "result.txt" file in the location of the program.
	 * The method gets a string as parameter this would be the ".txt" file's content
	 * The method delete's already existing file if there is.
	 * complexity - O(1)
	 */
	public static void createResultFile(String str) {
		try {
        	File myObj = new File(RESULT_FILE_NAME); //create file instance
            myObj.delete(); //delete file if exists
            myObj.createNewFile(); //create mew file
            FileWriter myWriter = new FileWriter(RESULT_FILE_NAME); //write to the file
            myWriter.write(str); //write the string
            myWriter.close(); //close the writer
            System.out.println("Scheduling is done. your result will appear in the file: " + myObj.getName());
        } catch(IOException e){ //catch file exception
        	e.printStackTrace();
        }
	}
	
	/*
	 * createDebugFile is a method that gets a string and creates "debug.txt" file in the location of the program.
	 * The method gets a string as parameter this would be the ".txt" file's content
	 * The method delete's already existing file if there is.
	 * complexity - O(1)
	 */
	public static void createDebugFile(String str) {
		try {
        	File myObj = new File(DEBUG_FILE_NAME); //create file instance
            myObj.delete(); //delete file if exists
            myObj.createNewFile(); //create mew file
            FileWriter myWriter = new FileWriter(DEBUG_FILE_NAME); //write to the file
            myWriter.write(str); //write the string
            myWriter.close(); //close the writer
            System.out.println("Full steps traces wil appear in the file: " + myObj.getName());
        } catch(IOException e){ //catch file exception
        	e.printStackTrace();
        }
	}
}//end of class MainApp
