package app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import app.entity.Candidato;
import app.service.CandidatoService;

@RestController
@RequestMapping("/api/candidato")
public class CandidatoController {

	@Autowired
	private CandidatoService candidatoService;

	 @PostMapping("/cadastrar")
	// No serviço ou controlador
	 public ResponseEntity<String> cadastrarCandidato(@RequestBody Candidato candidato) {
	     try {
	         if (candidato.getFuncao() == null) {
	             return new ResponseEntity<>("A função do candidato é obrigatória", HttpStatus.BAD_REQUEST);
	         }
	         String response = candidatoService.cadastrarCandidato(candidato);
	         return new ResponseEntity<>(response, HttpStatus.CREATED);
	     } catch (RuntimeException e) {
	         return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	     }
	 }


	@PutMapping("/update/{id}")
	public ResponseEntity<String> update(@RequestBody Candidato candidato, @PathVariable long id) {
		try {
			String msg = this.candidatoService.atualizar(candidato, id);
			return new ResponseEntity<>(msg, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Erro:" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/findById/{id}")
	public ResponseEntity<Candidato> findById(@PathVariable long id) {
		try {
			Candidato candidato = this.candidatoService.findById(id);
			return new ResponseEntity<>(candidato, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/findAll")
	public ResponseEntity<List<Candidato>> findAll() {
		try {
			List<Candidato> candidato = this.candidatoService.listarCandidatosAtivos();
			return new ResponseEntity<>(candidato, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/VereAtivos")
	public ResponseEntity<List<Candidato>> VereadoresAtivos() {
		try {
			List<Candidato> candidato = this.candidatoService.vereadoresAtivos();
			return new ResponseEntity<>(candidato, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/prefeAtivos")
	public ResponseEntity<List<Candidato>> PrefeitosAtivos() {
		try {
			List<Candidato> candidato = this.candidatoService.prefeitosAtivos();
			return new ResponseEntity<>(candidato, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable long id) {
		try {
			String msg = this.candidatoService.inativaCandidato(id);
			return new ResponseEntity<>(msg, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Erro: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
