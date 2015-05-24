package br.edu.ifpb.tsi.pdm.pdmproject.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.edu.ifpb.tsi.pdm.pdmproject.model.Atividade;

public class AtividadeDAO {
	private static final String TABELA_ATIVIDADE = "atividade";

	private SQLiteDatabase banco;

	public AtividadeDAO(Context context) {
		this.banco = new BancoHelper(context).getWritableDatabase();
	}

	public void inserir(Atividade atividade) {
		ContentValues cv = new ContentValues();
		cv.put("nome", atividade.getNome());

		banco.insert(TABELA_ATIVIDADE, null, cv);
		
	}
	
	public void update(Atividade atividade){
		ContentValues cv = new ContentValues();
		cv.put("nome", atividade.getNome());
		String[] idAtividade = {atividade.getId() + ""};
		this.banco.update(TABELA_ATIVIDADE, cv, " id = ?", idAtividade);
	}
	

	public void remover(int id) {
		banco.delete(TABELA_ATIVIDADE, "id = ?",
				new String[] { Integer.toString(id) });
	}

	public Atividade ler(int id) {
		String[] colunas = { "id", "nome" };
		String[] where = { Integer.toString(id) };
		Cursor c = banco.query(TABELA_ATIVIDADE, colunas, "id = ?", where, null, null, "nome");
		Atividade atividade = new Atividade();
		if (c.getCount() > 0) {
			c.moveToFirst();
			do {
				atividade.setId(c.getInt(c.getColumnIndex("id")));
				atividade.setNome(c.getString(c.getColumnIndex("nome")));
			} while (c.moveToNext());
		}
		return atividade;
	}
	
	public Atividade ler(String nome){
		String[] colunas = { "id", "nome" };
		String[] where = { nome };
		Cursor c = banco.query(TABELA_ATIVIDADE, colunas, "nome = ?", where, null, null, "nome");
		Atividade atividade = new Atividade();
		if (c.getCount() > 0) {
			c.moveToFirst();
			do {
				atividade.setId(c.getInt(c.getColumnIndex("id")));
				atividade.setNome(c.getString(c.getColumnIndex("nome")));
			} while (c.moveToNext());
		}
		return atividade;
	}

	public List<Atividade> get() {
		List<Atividade> lista = new ArrayList<Atividade>();
		String[] colunas = { "id", "nome" };
		Cursor c = banco.query(TABELA_ATIVIDADE, colunas, null, null, null,
				null, null);

		Atividade atividade;
		if (c.getCount() > 0) {
			c.moveToFirst();
			do {
				atividade = new Atividade();
				atividade.setId(c.getInt(c.getColumnIndex("id")));
				atividade.setNome(c.getString(c.getColumnIndex("nome")));
				lista.add(atividade);
			} while (c.moveToNext());
		}
		return lista;
	}
}
