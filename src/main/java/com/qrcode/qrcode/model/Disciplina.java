package com.qrcode.qrcode.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Disciplina {
	String cdDisciplina;
	String qtCredito;
	String turma;
	String cdProfessor;
}
