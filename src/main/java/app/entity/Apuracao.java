package app.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Apuracao {
	
	private int totalVotos;
	
	private List<Candidato> candidatosVereador;
	
	private List<Candidato> candidatosPrefeito;
	

}
