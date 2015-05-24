package br.edu.ifpb.tsi.pdm.pdmproject.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BancoHelper extends SQLiteOpenHelper {
	private static final int VERSAO_BANCO = 5;
	private static final String NOME_BANCO = "tarefas.db";

	public BancoHelper(Context context) {
		super(context, NOME_BANCO, null, VERSAO_BANCO);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String[] sql = {
				"CREATE TABLE atividade (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nome TEXT)",

				"CREATE TABLE disciplina (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nome TEXT)",

				"CREATE TABLE tarefa (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, id_atividade INTEGER, "
						+ "id_disciplina INTEGER, dataHora DATETIME, dataHoraNotificacao DATETIME, "
						+ "FOREIGN KEY(id_disciplina) REFERENCES disciplina(id), "
						+ "FOREIGN KEY(id_atividade) REFERENCES atividade(id))" };

		for (String s : sql) {
			db.execSQL(s);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String[] sql = {"drop table tarefa", "drop table atividade", "drop table disciplina"};
		for (String s : sql) {
			db.execSQL(s);
		}
		onCreate(db);
	}
	
	@Override
	public synchronized void close() {
		super.close();
	}
}