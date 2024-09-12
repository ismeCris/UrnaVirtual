package app.entity;

import javax.management.relation.Role;

import org.hibernate.validator.constraints.br.CPF;

import ch.qos.logback.core.status.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Candidato {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O nome é obrigatório")
	private String nome;

	@Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$", message = "Insira um CPF válido")
	@Column(unique = true)
	private String cpf;

	@Column(name = "numero", unique = true)
	@NotBlank(message = "O número do candidato é obrigatório")
	private String numero;

	@NotNull(message = "A função é obrigatória")
	private Integer funcao; // 1 Prefeito - 2 Vereador

	@Enumerated(EnumType.STRING)
	private Status status;

	@Transient
	private Integer votosApurados;

	public boolean isPrefeito() {
		return this.funcao != null && this.funcao == 1;
	}

	public boolean isVereador() {
		return this.funcao != null && this.funcao == 2;
	}

	public enum Status {
		ATIVO, INATIVO;
	}
}
