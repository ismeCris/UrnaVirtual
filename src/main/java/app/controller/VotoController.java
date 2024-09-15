package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import app.entity.Apuracao;
import app.entity.Voto;
import app.service.EleitorService;
import app.service.VotoService;

@RestController
@RequestMapping("/api/votos")
public class VotoController {

	@Autowired
	private VotoService votoService;

	@PostMapping("/votar/{eleitorId}")
	public ResponseEntity<String> votar(@RequestBody Voto voto, @PathVariable Long eleitorId) {
		try {
			String comprovante = votoService.votar(voto, eleitorId);
			return new ResponseEntity<>(comprovante, HttpStatus.CREATED);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/apuracao")
	public ResponseEntity<Apuracao> realizarApuracao() {
		try {
			Apuracao apuracao = votoService.realizarAapuracao();
			return new ResponseEntity<>(apuracao, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
