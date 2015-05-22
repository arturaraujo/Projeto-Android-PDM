package br.edu.ifpb.tsi.pdm.pdmproject.model;

import java.util.Calendar;

public class Tarefa {
	private int id;
	private Atividade atividade;
	private Disciplina disciplina;
	private Calendar dataHora;
	private Calendar dataHoraNotificacao;

	public Tarefa(Atividade atividade, Disciplina disciplina,
			Calendar dataHora, Calendar dataHoraNotificacao) {
		super();
		this.atividade = atividade;
		this.disciplina = disciplina;
		this.dataHora = dataHora;
		this.dataHoraNotificacao = dataHoraNotificacao;
	}
	
	public Tarefa() {
	}

	public Calendar getDataHoraNotificacao() {
		return dataHoraNotificacao;
	}

	public void setDataHoraNotificacao(Calendar dataHoraNotificacao) {
		this.dataHoraNotificacao = dataHoraNotificacao;
	}
	
	public void setDataHoraNotificacao(long dataHoraNotificacao) {
		this.dataHoraNotificacao = Calendar.getInstance();
		this.dataHoraNotificacao.setTimeInMillis(dataHoraNotificacao);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public Calendar getDataHora() {
		return dataHora;
	}

	public void setDataHora(Calendar dataHora) {
		this.dataHora = dataHora;
	}
	
	public void setDataHora(long dataHora) {
		this.dataHora = Calendar.getInstance();
		this.dataHora.setTimeInMillis(dataHora);
	}
	
	public String toString(){
		StringBuilder string = new StringBuilder();
		string.append(getAtividade().toString() + " de ");
		string.append(this.disciplina + ". \n");
		string.append("Data: " + this.dataHora.get(Calendar.DAY_OF_MONTH) + "/" +this.dataHora.get(Calendar.MONTH) + "/" + this.dataHora.get(Calendar.YEAR));
		
		return string.toString();
	}
}
