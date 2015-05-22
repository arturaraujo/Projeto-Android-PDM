package br.edu.ifpb.tsi.pdm.pdmproject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import br.edu.ifpb.tsi.pdm.pdmproject.dao.TarefaDAO;
import br.edu.ifpb.tsi.pdm.pdmproject.model.Tarefa;

public class MainActivity extends Activity {
	
	private static final int ID_MENU_NOVA_TAREFA = 1;
	private static final int ID_MENU_GERENCIAR_DISCIPLINA = 3;
	private static final int ID_MENU_GERENCIAR_ATIVIDADES = 2;
	private static final String MENU_NOVA_TAREFA = "Nova tarefa";
	private static final String MENU_GERENCIAR_DISCIPLINA = "Gerenciar Disciplinas";
	private static final String MENU_GERENCIAR_ATIVIDADES = "Gerenciar Atividades";

	private ListView lvProximasTarefas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.carregaComponentes();
		
		TarefaDAO daoTarefa = new TarefaDAO(this);
		
		List<String> string = new ArrayList<String>();
		List<Tarefa> tarefas = daoTarefa.get();
		
		for (Tarefa f : tarefas){
			StringBuilder builder = new StringBuilder();
			builder.append(f.getAtividade().getNome() + " de ");
			builder.append(f.getDisciplina().getNome() + ". \n");
			builder.append("Data: " + f.getDataHora().get(Calendar.DAY_OF_MONTH) + "/" + f.getDataHora().get(Calendar.MONTH) + "/" + f.getDataHora().get(Calendar.YEAR));
			string.add(builder.toString());
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, string);
		this.lvProximasTarefas.setAdapter(adapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		menu.add(1, ID_MENU_NOVA_TAREFA, ID_MENU_NOVA_TAREFA, MENU_NOVA_TAREFA);
		menu.add(2, ID_MENU_GERENCIAR_DISCIPLINA, ID_MENU_GERENCIAR_DISCIPLINA, MENU_GERENCIAR_DISCIPLINA);
		menu.add(2, ID_MENU_GERENCIAR_ATIVIDADES, ID_MENU_GERENCIAR_ATIVIDADES, MENU_GERENCIAR_ATIVIDADES);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		
		switch (item.getItemId()) {
		case ID_MENU_NOVA_TAREFA:
			startActivity(new Intent(this, NovaTarefaActivity.class));
			break;
		case ID_MENU_GERENCIAR_ATIVIDADES:
			startActivity(new Intent(this, AtividadesActivity.class));
			break;
		}
		
		return true;
	}

	private void carregaComponentes() {
		this.lvProximasTarefas = (ListView) findViewById(R.id.lvProximasTarefas);
		
		this.lvProximasTarefas.setOnItemClickListener(new OnAtividadeListener());
	}

	public class OnAtividadeListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Log.d("tag", position + "  " + id);
			
		}

	}
}
