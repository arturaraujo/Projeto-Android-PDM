package br.edu.ifpb.tsi.pdm.pdmproject.model;

public class Atividade {
	public Atividade(String nome) {
		super();
		this.nome = nome;
	}

	public Atividade() {
	}

	private int id;
	private String nome;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return this.nome;
	}
}
