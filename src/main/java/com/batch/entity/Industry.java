package com.batch.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Industry {

	@Id
    @Column (name = "id", nullable = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;
	
	@Column (name = "indutry_code")
	private String indutryCode;
	
	@Column (name = "indutry_name")
	private String indutryName;
	
	@Column (name = "rme_size")
	private String rmeSize;
	
	@Column (name = "rme_variable")
	private String rmeVariable;
	
	@Column (name = "rme_value")
	private String rmeValue;
	
	@Column (name = "rme_unit")
	private String rmeUnit;
	
	@Column (name = "indutry_year")
	private String indutryYear;
	
	@Column (name = "status")
	private String status; //Processed/Unprocessed

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIndutryCode() {
		return indutryCode;
	}

	public void setIndutryCode(String indutryCode) {
		this.indutryCode = indutryCode;
	}

	public String getIndutryName() {
		return indutryName;
	}

	public void setIndutryName(String indutryName) {
		this.indutryName = indutryName;
	}

	public String getRmeSize() {
		return rmeSize;
	}

	public void setRmeSize(String rmeSize) {
		this.rmeSize = rmeSize;
	}

	public String getRmeVariable() {
		return rmeVariable;
	}

	public void setRmeVariable(String rmeVariable) {
		this.rmeVariable = rmeVariable;
	}

	public String getRmeValue() {
		return rmeValue;
	}

	public void setRmeValue(String rmeValue) {
		this.rmeValue = rmeValue;
	}

	public String getRmeUnit() {
		return rmeUnit;
	}

	public void setRmeUnit(String rmeUnit) {
		this.rmeUnit = rmeUnit;
	}

	public String getIndutryYear() {
		return indutryYear;
	}

	public void setIndutryYear(String indutryYear) {
		this.indutryYear = indutryYear;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
