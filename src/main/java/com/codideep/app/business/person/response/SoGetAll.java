package com.codideep.app.business.person.response;

import java.util.Date;
import java.util.List;

import com.codideep.app.business.ResponseGeneral;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class SoGetAll extends ResponseGeneral<List<SoGetAll>> {
	public String idPerson;
	public String firstName;
	public String surName;
	public String dni;
	public Boolean gender;
	public Date birthDate;
}