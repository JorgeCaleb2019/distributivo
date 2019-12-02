package com.ecodeup.calendar;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class Calendar {
	private Date date;
	
	public void onClick(Date date) {
		SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/YYYY");
		System.out.println(dateFormat.format(date));
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	

}
