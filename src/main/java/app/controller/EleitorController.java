package app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.entity.Eleitor;
import app.service.EleitorService;

@RestController
@RequestMapping("/api/eleitor")
@CrossOrigin(origins = "http://localhost:4200")
public class EleitorController{

	@Autowired
	private EleitorService eleitorService;

	@PostMapping("/save")
	public ResponseEntity<String> save(@RequestBody Eleitor eleitor) {
		try {
			String message = this.eleitorService.save(eleitor);
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Erro: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<String> update(@RequestBody Eleitor eleitor, @PathVariable long id) {
		try {
			String message = this.eleitorService.update(eleitor, id);
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Erro: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/findById/{id}")
	public ResponseEntity<Eleitor> findById(@PathVariable long id) {
		try {
			Eleitor eleitor = this.eleitorService.findById(id);
			return new ResponseEntity<>(eleitor, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findAll")
	public ResponseEntity<List<Eleitor>> findAll(){
		try {
			List<Eleitor> eleitores = this.eleitorService.findAll();
			return new ResponseEntity<>(eleitores, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable long id) {
		try {
			String message = this.eleitorService.delete(id);
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (Exception e) {
			 return new ResponseEntity<>("Eleitor não encontrado", HttpStatus.NOT_FOUND);
		}
	}
}
