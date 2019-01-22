package com.turnyur.gasdatalogger.MODEL;

public class ObjectGas {
	private long obect_ID;
	private String temperature = "";
	private String gasIntensity = "";
	private String rec_time = "";
	private String rec_date = "";

	public ObjectGas(long obect_ID, String temperature, String gasIntensity,
			String rec_time, String rec_date) {
		super();
		this.obect_ID = obect_ID;
		this.temperature = temperature;
		this.gasIntensity = gasIntensity;
		this.rec_time = rec_time;
		this.rec_date = rec_date;
	}

	public ObjectGas() {

	}

	public long getObect_ID() {
		return obect_ID;
	}

	public void setObect_ID(long insertid) {
		this.obect_ID = insertid;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getGasIntensity() {
		return gasIntensity;
	}

	public void setGasIntensity(String gasIntensity) {
		this.gasIntensity = gasIntensity;
	}

	public String getRec_time() {
		return rec_time;
	}

	public void setRec_time(String rec_time) {
		this.rec_time = rec_time;
	}

	public String getRec_date() {
		return rec_date;
	}

	public void setRec_date(String rec_date) {
		this.rec_date = rec_date;
	}

}
