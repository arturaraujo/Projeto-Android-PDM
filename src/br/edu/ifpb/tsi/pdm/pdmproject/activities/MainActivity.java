package br.edu.ifpb.tsi.pdm.pdmproject.activities;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import br.edu.ifpb.tsi.pdm.pdmproject.R;
import br.edu.ifpb.tsi.pdm.pdmproject.dao.AtividadeDAO;
import br.edu.ifpb.tsi.pdm.pdmproject.dao.DisciplinaDAO;
import br.edu.ifpb.tsi.pdm.pdmproject.dao.TarefaDAO;
import br.edu.ifpb.tsi.pdm.pdmproject.model.Atividade;
import br.edu.ifpb.tsi.pdm.pdmproject.model.Disciplina;
import br.edu.ifpb.tsi.pdm.pdmproject.model.Tarefa;

public class MainActivity extends Activity {
	
	private static final int ID_MENU_NOVA_TAREFA = 1;
	private static final int ID_MENU_GERENCIAR_DISCIPLINA = 3;
	private static final int ID_MENU_GERENCIAR_ATIVIDADES = 2;
	private static final String MENU_NOVA_TAREFA = "Nova tarefa";
	private static final String MENU_GERENCIAR_DISCIPLINA = "Gerenciar Disciplinas";
	private static final String MENU_GERENCIAR_ATIVIDADES = "Gerenciar Atividades";
	
	private static final String[] OPCOES_TAREFA = {"Ver", /*"Editar", */"Excluir", "Compartilhar"};
	
	//private static final int EDITAR = 0;
	private static final int VER = 0;
	private static final int EXCLUIR = 1;
	private static final int COMPARTILHAR = 2;

	private ListView lvProximasTarefas;
	
	TarefaDAO daoTarefa;
	DisciplinaDAO daoDisciplina;
	AtividadeDAO daoAtividade;
	
	List<Tarefa> tarefas;
	
	ArrayAdapter<Tarefa> adapterTarefas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.carregaComponentes();
		this.carregaBanco();
		
		tarefas = daoTarefa.get();
		
		if(tarefas == null || tarefas.isEmpty()){
			AlertDialog.Builder dialog =  new AlertDialog.Builder(this);
			dialog.setTitle("Nenhuma Tarefa");
			dialog.setMessage("Você não tem nenhuma tarefa!");
			dialog.setNeutralButton("OK", null);
			dialog.create().show();
		}
		
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
			List<Atividade> atividades = daoAtividade.get();
			List<Disciplina> disciplinas = daoDisciplina.get();
			
			if (atividades == null || atividades.isEmpty()){
				AlertDialog.Builder dialog =  new AlertDialog.Builder(this);
				dialog.setTitle("Nenhuma atividade cadastrada");
				dialog.setMessage("Para criar tarefas é necessário primeiro ter atividades cadastradas!");
				dialog.setNeutralButton("OK", null);
				dialog.create().show();
				break;
			}
			
			if (disciplinas == null || disciplinas.isEmpty()){
				AlertDialog.Builder dialog =  new AlertDialog.Builder(this);
				dialog.setTitle("Nenhuma disciplina cadastrada");
				dialog.setMessage("Para criar tarefas é necessário primeiro ter disciplina cadastradas!");
				dialog.setNeutralButton("OK", null);
				dialog.create().show();
				break;
			}
			
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
		adapterTarefas.notifyDataSetChanged();
		this.lvProximasTarefas.setAdapter(adapterTarefas);
	}

	private void carregaComponentes() {
		this.lvProximasTarefas = (ListView) findViewById(R.id.lvProximasTarefas);
		this.lvProximasTarefas.setOnItemClickListener(new OnAtividadeListener());
	}
	
	private void carregaBanco(){
		this.daoAtividade = new AtividadeDAO(this);
		this.daoDisciplina = new DisciplinaDAO(this);
		this.daoTarefa = new TarefaDAO(this);
	}

	public class OnAtividadeListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position,
				long id) {
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			ArrayAdapter<String> adapterOpcoes = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, OPCOES_TAREFA);
			builder.setAdapter(adapterOpcoes, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which){
					case EXCLUIR:
						daoTarefa.remover(tarefas.get(position).getId());
						adapterTarefas.remove(tarefas.get(position));
						adapterTarefas.notifyDataSetChanged();
						break;
					case COMPARTILHAR:
						Intent intent = new Intent(Intent.ACTION_SEND);
						intent.setType("text/plain");
						intent.putExtra(Intent.EXTRA_TEXT, tarefas.get(position).toString());
						
						startActivity(Intent.createChooser(intent, "Compartilhar tarefa"));
					}
				}
			});
			builder.create();
			builder.show();
		}
	}
}
