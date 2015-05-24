package br.edu.ifpb.tsi.pdm.pdmproject.activities;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import br.edu.ifpb.tsi.pdm.pdmproject.R;
import br.edu.ifpb.tsi.pdm.pdmproject.dao.DisciplinaDAO;
import br.edu.ifpb.tsi.pdm.pdmproject.model.Disciplina;

public class DisciplinasActivity extends Activity {
	private static final int ID_MENU_NOVA_DISCIPLINA = 1;
	private static final String MENU_NOVA_DISCIPLINA = "Nova Disciplina";

	private static final int EDITAR= 0;
	private static final int EXCLUIR= 1;
	private static final String[] OPCOES_ATIVIDADE = {"Editar", "Excluir"};
	
	ListView lvDisciplinas;
	List<Disciplina> disciplinas;
	
	DisciplinaDAO daoDisciplina;
	
	ArrayAdapter<Disciplina> adapterDisciplinas;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disciplinas);
		
		this.carregarComponentes();
		this.carregarListenners();
		this.carregaBanco();
		
		this.disciplinas = daoDisciplina.get();
		
		if (disciplinas == null || disciplinas.isEmpty()){
			AlertDialog.Builder dialog =  new AlertDialog.Builder(this);
			dialog.setTitle("Nenhuma Disciplina Cadastrada!");
			dialog.setMessage("Voce não tem nenhuma disciplina cadastrada. \n "
					+ "Acesse Menu -> Nova Disciplina para cadastrar uma nova diciplina!");
			dialog.setNeutralButton("OK", null);
			dialog.create().show();
		}
		
		adapterDisciplinas = new ArrayAdapter<Disciplina>(this, android.R.layout.simple_list_item_1, disciplinas);
		this.lvDisciplinas.setAdapter(adapterDisciplinas);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		menu.add(0, ID_MENU_NOVA_DISCIPLINA, ID_MENU_NOVA_DISCIPLINA, MENU_NOVA_DISCIPLINA);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case ID_MENU_NOVA_DISCIPLINA:
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle("Nova Disciplina");
			alert.setMessage("Nome: ");
			
			final EditText input = new EditText(this);
			alert.setView(input);
			
			alert.setPositiveButton("OK", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (!input.getText().toString().trim().equals("")){
						Disciplina disciplina = new Disciplina(input.getText().toString());
						daoDisciplina.inserir(disciplina);
						adapterDisciplinas.add(daoDisciplina.ler(disciplina.getNome()));
						adapterDisciplinas.notifyDataSetChanged();
					}
				}
			});
			
			alert.setNegativeButton("Cancelar", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			alert.show();
			break;
		default:
			break;
		}
		return true;
	}
	
	public class OnAtividadeListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position,
				long id) {
			AlertDialog.Builder builder = new AlertDialog.Builder(DisciplinasActivity.this);
			ArrayAdapter<String> adapterOpcoes = new ArrayAdapter<String>(DisciplinasActivity.this, android.R.layout.simple_list_item_1, OPCOES_ATIVIDADE);
			builder.setAdapter(adapterOpcoes, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which){
					case EXCLUIR:
						daoDisciplina.remover(disciplinas.get(position).getId());
						adapterDisciplinas.remove(disciplinas.get(position));
						adapterDisciplinas.notifyDataSetChanged();
						setResult(RESULT_OK);
						break;
					case EDITAR:
						AlertDialog.Builder alert = new AlertDialog.Builder(DisciplinasActivity.this);

						alert.setTitle("Editar Atividade");
						alert.setMessage("Digite o nome da atividade: ");
						
						final EditText input = new EditText(DisciplinasActivity.this);
						input.setText(disciplinas.get(position).getNome());
						alert.setView(input);
						
						alert.setPositiveButton("OK", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (!input.getText().toString().trim().equals("")){
									Disciplina disciplina = disciplinas.get(position);
									disciplina.setNome(input.getText().toString());
									daoDisciplina.update(disciplina);
									adapterDisciplinas.notifyDataSetChanged();
									setResult(RESULT_OK);
								}
							}
						});
						
						alert.setNegativeButton("Cancelar", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
						});
						
						alert.show();
						break;
					}
				}
			});
			builder.create();
			builder.show();
		}
	}
	
	private void carregarListenners(){
		this.lvDisciplinas.setOnItemClickListener(new OnAtividadeListener());
	}

	private void carregarComponentes(){
		this.lvDisciplinas = (ListView) findViewById(R.id.lvDisciplinas);
	}
	
	private void carregaBanco(){
		this.daoDisciplina = new DisciplinaDAO(this);
	}
}
