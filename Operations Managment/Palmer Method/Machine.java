package com.palmer.main;

/*
 * This class defines the Machine object that is used for the scheduling
 */
public class Machine {
	
	private String machineName; //machine name as string
	private Integer machineWeight = null; //machine weight will be calculated later
	
	//A machine object super constructor
	public Machine() {
		
	}
	
	//A machine object constructor with machine name only.
	public Machine(String machineName) {
		this.machineName = machineName;
	}
	
	/*
	 * getters
	 * complexity - O(1)
	 */
	public String getMachineName() {
		return this.machineName;
	}
	
	public Integer getMachineWeight() {
		return this.machineWeight;
	}
	
	/*
	 * setter for machine weight only
	 * complexity - O(1)
	 */
	public void setMachineWeight(Integer machineWight) {
		this.machineWeight = machineWight;
	}
}//end of class Machine
