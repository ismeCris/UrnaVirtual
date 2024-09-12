package app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Eleitor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotBlank(message = "O nome é obrigatório")
	private String nomeCompleto;

	@Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$", message = "Insira um CPF válido")
	@Column(unique = true)
	private String cpf;

	@NotBlank(message = "Insira a sua profissão")
	private String profissao;

	@NotBlank(message = "O celular é obrigatório")
	@Pattern(regexp = "^\\(?\\d{2}\\)?\\s?\\d{5}-?\\d{4}$", message = "Insira um celular válido")
	private String celular;

	@Pattern(regexp = "^\\(?\\d{2}\\)?\\s?\\d{4}-?\\d{4}$", message = "Insira um telefone válido")
	private String telefone;

	@Email(message = "Insira um endereço email válido")
	private String email;

	@Enumerated(EnumType.STRING)
	private Status status;

	public enum Status {
		APTO, INATIVO, BLOQUEADO, PENDENTE, VOTOU
	}

}
