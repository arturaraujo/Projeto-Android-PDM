package br.edu.ifpb.tsi.pdm.pdmproject.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import br.edu.ifpb.tsi.pdm.pdmproject.R;
import br.edu.ifpb.tsi.pdm.pdmproject.R.id;
import br.edu.ifpb.tsi.pdm.pdmproject.R.layout;
import br.edu.ifpb.tsi.pdm.pdmproject.dao.TarefaDAO;
import br.edu.ifpb.tsi.pdm.pdmproject.model.Tarefa;

public class MainActivity extends Activity {
	
	private static final int ID_MENU_NOVA_TAREFA = 1;
	private static final int ID_MENU_GERENCIAR_DISCIPLINA = 3;
	private static final int ID_MENU_GERENCIAR_ATIVIDADES = 2;
	private static final String MENU_NOVA_TAREFA = "Nova tarefa";
	private static final String MENU_GERENCIAR_DISCIPLINA = "Gerenciar Disciplinas";
	private static final String MENU_GERENCIAR_ATIVIDADES = "Gerenciar Atividades";
	
	private static final int IDEDITAR = 0;

	private ListView lvProximasTarefas;
	
	TarefaDAO daoTarefa;
	
	List<Tarefa> tarefas;
	
	ArrayAdapter<Tarefa> adapterTarefas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.carregaComponentes();
		
		daoTarefa = new TarefaDAO(this);
		tarefas = daoTarefa.get();
		
		adapterTarefas = new ArrayAdapter<Tarefa>(MainActivity.this, android.R.layout.simple_list_item_1, tarefas);
		this.lvProximasTarefas.setAdapter(adapterTarefas);
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
			startActivityForResult(new Intent(this, NovaTarefaActivity.class), ID_MENU_NOVA_TAREFA);
			break;
		case ID_MENU_GERENCIAR_ATIVIDADES:
			startActivity(new Intent(this, AtividadesActivity.class));
			break;
		case ID_MENU_GERENCIAR_DISCIPLINA:
			startActivity(new Intent(this, DisciplinasActivity.class));
			break;
		}
		
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK){
			if(requestCode == ID_MENU_NOVA_TAREFA){
				refreshAdapter();
			}
		}
	}
	
	private void refreshAdapter(){
		tarefas = daoTarefa.get();
		adapterTarefas = new ArrayAdapter<Tarefa>(MainActivity.this, android.R.layout.simple_list_item_1, tarefas);
		//this.lvProximasTarefas.setAdapter(adapterTarefas);
		adapterTarefas.notifyDataSetChanged();
		this.lvProximasTarefas.setAdapter(adapterTarefas);
	}

	private void carregaComponentes() {
		this.lvProximasTarefas = (ListView) findViewById(R.id.lvProximasTarefas);
		
		this.lvProximasTarefas.setOnItemClickListener(new OnAtividadeListener());
	}

	public class OnAtividadeListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position,
				long id) {
			Log.d("tag", "  O id do bd e: " + tarefas.get(position).getId() +  " e o id clicado eh: " + id);
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			String[] opcoes = {"Editar", "Excluir", "Compartilhar"};
			ArrayAdapter<String> adapterOpcoes = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, opcoes);
			builder.setAdapter(adapterOpcoes, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Log.i("tag", "Menu selecionado: " + which);
					switch (which){
					case 1:
						daoTarefa.remover(tarefas.get(position).getId());
						adapterTarefas.remove(tarefas.get(position));
						adapterTarefas.notifyDataSetChanged();
						break;
					}
				}
			});
			builder.create();
			builder.show();
			
		}

	}
}
