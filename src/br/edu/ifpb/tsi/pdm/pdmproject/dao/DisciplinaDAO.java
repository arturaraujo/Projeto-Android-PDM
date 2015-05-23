package br.edu.ifpb.tsi.pdm.pdmproject.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.edu.ifpb.tsi.pdm.pdmproject.model.Disciplina;

public class DisciplinaDAO{
	private static final String TABELA_DISCIPLINA = "disciplina";

	private SQLiteDatabase banco;

	public DisciplinaDAO(Context context) {
		this.banco = new BancoHelper(context).getWritableDatabase();
	}

	public void inserir(Disciplina disciplina) {
		ContentValues cv = new ContentValues();
		cv.put("nome", disciplina.getNome());

		banco.insert(TABELA_DISCIPLINA, null, cv);
	}

	public void remover(int id) {
		banco.delete(TABELA_DISCIPLINA, "id = ?",
				new String[] { Integer.toString(id) });
	}

	public Disciplina ler(int id) {
		String[] colunas = { "id", "nome" };
		String[] where = { Integer.toString(id) };
		Cursor c = banco.query(TABELA_DISCIPLINA, colunas, "id = ?", where, null, null, "nome");
		Disciplina disciplina = new Disciplina();
		if (c.getCount() > 0) {
			c.moveToFirst();
			do {
				disciplina.setId(c.getInt(c.getColumnIndex("id")));
				disciplina.setNome(c.getString(c.getColumnIndex("nome")));
			} while (c.moveToNext());
		}
		return disciplina;
	}
	
	public Disciplina ler(String nome){
		String[] colunas = { "id", "nome" };
		String[] where = { nome };
		Cursor c = banco.query(TABELA_DISCIPLINA, colunas, "nome = ?", where, null, null, "nome");
		Disciplina disciplina = new Disciplina();
		if (c.getCount() > 0) {
			c.moveToFirst();
			do {
				disciplina.setId(c.getInt(c.getColumnIndex("id")));
				disciplina.setNome(c.getString(c.getColumnIndex("nome")));
			} while (c.moveToNext());
		}
		return disciplina;
	}

	public List<Disciplina> get() {
		List<Disciplina> lista = new ArrayList<Disciplina>();
		String[] colunas = { "id", "nome" };
		Cursor c = banco.query(TABELA_DISCIPLINA, colunas, null, null, null,
				null, null);

		Disciplina disciplina;
		if (c.getCount() > 0) {
			c.moveToFirst();
			do {
				disciplina = new Disciplina();
				disciplina.setId(c.getInt(c.getColumnIndex("id")));
				disciplina.setNome(c.getString(c.getColumnIndex("nome")));
				lista.add(disciplina);
			} while (c.moveToNext());
		}
		return lista;
	}
	
	public void close(){
		banco.close();
	}
}
