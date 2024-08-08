package com.codideep.app.business.person;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codideep.app.business.person.request.RequestInsert;
import com.codideep.app.business.person.request.RequestUpdate;
import com.codideep.app.business.person.response.ResponseGetAll;
import com.codideep.app.business.person.response.ResponseInsert;
import com.codideep.app.dto.DtoPerson;
import com.codideep.app.service.PersonService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("person")
public class PersonController {
	@Autowired
	private PersonService personService;

	@PostMapping(path = "insert", consumes = { "multipart/form-data" })
	public ResponseEntity<ResponseInsert> actionInsert(@Valid @ModelAttribute RequestInsert soInsert, BindingResult bindingResult) {
		ResponseInsert responseSoInsert = new ResponseInsert();

		try {
			if(bindingResult.hasErrors()) {
				bindingResult.getAllErrors().forEach(error -> {
					responseSoInsert.addResponseMesssage(error.getDefaultMessage());
				});
				
				return new ResponseEntity<>(responseSoInsert, HttpStatus.OK);
			}

			DtoPerson dtoPerson = new DtoPerson();

			dtoPerson.setFirstName(soInsert.getFirstName());
			dtoPerson.setSurName(soInsert.getSurName());
			dtoPerson.setDni(soInsert.getDni());
			dtoPerson.setGender(soInsert.isGender());
			dtoPerson.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse(soInsert.getBirthDate()));

			personService.insert(dtoPerson);

			responseSoInsert.success();
			responseSoInsert.addResponseMesssage("Operación realizada correctamente.");

			return new ResponseEntity<>(responseSoInsert, HttpStatus.CREATED);
		} catch(Exception e) {
			return new ResponseEntity<>(responseSoInsert, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(path = "getall")
	public ResponseEntity<ResponseGetAll> actionGetAll() {
		ResponseGetAll responseSoGetAll = new ResponseGetAll();

		List<DtoPerson> listDtoPerson = personService.getAll();

		for (DtoPerson dtoPerson : listDtoPerson) {
			Map<String, Object> map = new HashMap<>();

			map.put("idPerson", dtoPerson.getIdPerson());
			map.put("firstName", dtoPerson.getFirstName());
			map.put("surName", dtoPerson.getSurName());
			map.put("dni", dtoPerson.getDni());
			map.put("gender", dtoPerson.getGender());
			map.put("birthDate", dtoPerson.getBirthDate());

			responseSoGetAll.dto.listPerson.add(map);
		}

		responseSoGetAll.success();

		return new ResponseEntity<>(responseSoGetAll, HttpStatus.OK);
	}

	@DeleteMapping(path = "delete/{idPerson}")
	public ResponseEntity<Boolean> actionDelete(@PathVariable String idPerson) {
		personService.delete(idPerson);

		return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
	}

	@PostMapping(path = "update", consumes = { "multipart/form-data" })
	public ResponseEntity<Boolean> actionUpdate(@ModelAttribute RequestUpdate soUpdate) {
		try {
			DtoPerson dtoPerson = new DtoPerson();

			dtoPerson.setIdPerson(soUpdate.getIdPerson());
			dtoPerson.setFirstName(soUpdate.getFirstName());
			dtoPerson.setSurName(soUpdate.getSurName());
			dtoPerson.setDni(soUpdate.getDni());
			dtoPerson.setGender(soUpdate.isGender());
			dtoPerson.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse(soUpdate.getBirthDate()));

			personService.update(dtoPerson);
		} catch(Exception e) { }

		return new ResponseEntity<>(true, HttpStatus.OK);
	}
}