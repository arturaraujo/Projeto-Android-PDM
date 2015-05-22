package br.edu.ifpb.tsi.pdm.pdmproject.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.edu.ifpb.tsi.pdm.pdmproject.model.Tarefa;

public class TarefaDAO {
	private static final String TABELA_TAREFA = "tarefa";
	
	private SQLiteDatabase banco;
	private Context context;

	public TarefaDAO(Context context) {
		this.banco = new BancoHelper(context).getWritableDatabase();
		this.context = context;
	}
	
	public void inserir(Tarefa tarefa){
		ContentValues cv= new ContentValues();
		cv.put("id_atividade", tarefa.getAtividade().getId());
		cv.put("id_disciplina", tarefa.getDisciplina().getId());
		cv.put("datahora", tarefa.getDataHora().getTimeInMillis());
		cv.put("dataHoraNotificacao", tarefa.getDataHoraNotificacao().getTimeInMillis());
		
		banco.insert(TABELA_TAREFA, null, cv);
	}
	
	public void remover(int codigo){
		banco.delete(TABELA_TAREFA, "codigo = ?", new String[]{Integer.toString(codigo)} );
	}
	
	public List<Tarefa> get(){
		List<Tarefa> lista = new ArrayList<Tarefa>();
		String[] colunas = {"id", "id_disciplina", "id_atividade", "dataHora", "dataHoraNotificacao"};
		Cursor c = banco.query(TABELA_TAREFA, colunas, null, null, null, null, null);
		
		Tarefa tarefa;
		if(c.getCount() > 0){
			c.moveToFirst();
			DisciplinaDAO daoDisciplina = new DisciplinaDAO(context);
			DisciplinaDAO daoDisciplina1 = new DisciplinaDAO(context);
			AtividadeDAO daoAtividade = new AtividadeDAO(context);
			do{
				tarefa = new Tarefa();
				tarefa.setId(c.getInt(c.getColumnIndex("id")));
				tarefa.setAtividade(daoAtividade.ler(c.getInt(c.getColumnIndex("id_atividade"))));
				tarefa.setDisciplina(daoDisciplina.ler(c.getInt(c.getColumnIndex("id_disciplina"))));
				tarefa.setDataHora(c.getLong(c.getColumnIndex("dataHora")));
				tarefa.setDataHoraNotificacao(c.getLong(c.getColumnIndex("dataHoraNotificacao")));
				
				lista.add(tarefa);
			} while(c.moveToNext());
			daoDisciplina.close();
		}
		return lista;
	}
	
}
