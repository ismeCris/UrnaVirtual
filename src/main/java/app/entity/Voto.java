package app.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.xml.crypto.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Voto {

	@NotNull(message = "Data e Hora Sao obrigatorios")
	private LocalDateTime datas;

    @NotBlank(message = "O comprovante (hash) é obrigatório")
	private String comprovante;

	@ManyToOne
	@NotNull(message = "O voto para Prefeito é obrigatorio")
	private Candidato prefeito;

	@ManyToOne
	@NotNull(message = "O voto para Vereador é obrigatorio")
	private Candidato vereador;

}
