package com.palmer.main;

import java.util.ArrayList;
import java.util.List;

/*
 * This class defines the Job object that is used that the program is scheduling
 */

public class Job {
	
	//job object properties
	private String jobName; //job name as string
	private int jobScore; //job score will be calculated later
	private List<Integer> timeList; //timeList is a list of integers represents the timings for the job on each machine
	
	//A Job object super constructor
	public Job() {
		
	}
	
	//A duplication Job constructor.
	public Job(Job job) {
		this.jobName = job.jobName;
		this.timeList = new ArrayList<Integer>();
	}
	
	//A Job object constructor with job name only.
	public Job(String jobName) {
		this.jobName = jobName;
		this.timeList = new ArrayList<Integer>();
	}
	
	/*
	 * getters
	 * complexity - O(1)
	 */
	public String getJobName() {
		return this.jobName;
	}
	
	public int getJobScore() {
		return this.jobScore;
	}
	
	public List<Integer> getTimeList(){
		return this.timeList;
	}
	
	/*
	 * setter for job score only
	 * complexity - O(1)
	 */
	public void setJobScore(int jobScore) {
		this.jobScore = jobScore;
	}
	
	/*
	 * add time for the timeList for a a specific job
	 * complexity - O(1)
	 */
	public void addTime(Integer ti) {
		this.timeList.add(ti);
	}
}//end of class Job
