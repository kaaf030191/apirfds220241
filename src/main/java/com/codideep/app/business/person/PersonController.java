package com.codideep.app.business.person;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
	public ResponseEntity<Map<String, String>> actionInsert(@Valid @ModelAttribute SoInsert soInsert, BindingResult bindingResult) {
		try {
			if(bindingResult.hasErrors()) {
				StringBuilder errors = new StringBuilder();

				bindingResult.getAllErrors().forEach(error -> {
					errors.append(error.getDefaultMessage()).append("; ");
				});
				
				Map<String, String> response = new HashMap<>();

				response.put("error", errors.toString());

				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		// 	DtoPerson dtoPerson = new DtoPerson();

		// 	dtoPerson.setFirstName(soInsert.getFirstName());
		// 	dtoPerson.setSurName(soInsert.getSurName());
		// 	dtoPerson.setDni(soInsert.getDni());
		// 	dtoPerson.setGender(soInsert.isGender());
		// 	dtoPerson.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse(soInsert.getBirthDate()));

		// 	personService.insert(dtoPerson);
		} catch(Exception e) {
			Map<String, String> response = new HashMap<>();

			response.put("type", e.getMessage());

			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		Map<String, String> response = new HashMap<>();

		response.put("type", "success");

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping(path = "getall")
	public ResponseEntity<List<SoGetAll>> actionGetAll() {
		List<DtoPerson> listDtoPerson = personService.getAll();

		List<SoGetAll> listSoPersonGet = new ArrayList<>();

		for (DtoPerson dtoPerson : listDtoPerson) {
			listSoPersonGet.add(new SoGetAll(
				dtoPerson.getIdPerson(),
				dtoPerson.getFirstName(),
				dtoPerson.getSurName(),
				dtoPerson.getDni(),
				dtoPerson.getGender(),
				dtoPerson.getBirthDate()
			));
		}

		return new ResponseEntity<>(listSoPersonGet, HttpStatus.OK);
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