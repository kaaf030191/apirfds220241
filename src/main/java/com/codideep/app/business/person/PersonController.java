package com.codideep.app.business.person;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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

import com.codideep.app.business.person.request.SoInsert;
import com.codideep.app.business.person.request.SoUpdate;
import com.codideep.app.business.person.response.SoGetAll;
import com.codideep.app.dto.DtoPerson;
import com.codideep.app.service.PersonService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("person")
public class PersonController {
	@Autowired
	private PersonService personService;

	@PostMapping(path = "insert", consumes = { "multipart/form-data" })
	public ResponseEntity<com.codideep.app.business.person.response.SoInsert> actionInsert(@Valid @ModelAttribute SoInsert soInsert, BindingResult bindingResult) {
		com.codideep.app.business.person.response.SoInsert responseSoInsert = new com.codideep.app.business.person.response.SoInsert();

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

			responseSoInsert.setType("success");
			responseSoInsert.addResponseMesssage("Operación realizada correctamente.");

			return new ResponseEntity<>(responseSoInsert, HttpStatus.CREATED);
		} catch(Exception e) {
			return new ResponseEntity<>(responseSoInsert, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(path = "getall")
	public ResponseEntity<com.codideep.app.business.person.response.SoGetAll> actionGetAll() {
		com.codideep.app.business.person.response.SoGetAll responseSoGetAll = new com.codideep.app.business.person.response.SoGetAll();

		List<DtoPerson> listDtoPerson = personService.getAll();

		responseSoGetAll.setDto(new ArrayList<>());

		for (DtoPerson dtoPerson : listDtoPerson) {
			responseSoGetAll.getDto().add(new SoGetAll(
				dtoPerson.getIdPerson(),
				dtoPerson.getFirstName(),
				dtoPerson.getSurName(),
				dtoPerson.getDni(),
				dtoPerson.getGender(),
				dtoPerson.getBirthDate()
			));
		}

		responseSoGetAll.setType("success");

		return new ResponseEntity<>(responseSoGetAll, HttpStatus.OK);
	}

	@DeleteMapping(path = "delete/{idPerson}")
	public ResponseEntity<Boolean> actionDelete(@PathVariable String idPerson) {
		personService.delete(idPerson);

		return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
	}

	@PostMapping(path = "update", consumes = { "multipart/form-data" })
	public ResponseEntity<Boolean> actionUpdate(@ModelAttribute SoUpdate soUpdate) {
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