package br.edu.ifpb.tsi.pdm.pdmproject;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import br.edu.ifpb.tsi.pdm.pdmproject.dao.AtividadeDAO;
import br.edu.ifpb.tsi.pdm.pdmproject.dao.DisciplinaDAO;
import br.edu.ifpb.tsi.pdm.pdmproject.model.Atividade;
import br.edu.ifpb.tsi.pdm.pdmproject.model.Disciplina;

public class DisciplinasActivity extends Activity {
	private static final int ID_MENU_NOVA_DISCIPLINA = 1;
	private static final String MENU_NOVA_DISCIPLINA = "Nova Disciplina";

	ListView lvDisciplinas;
	List<Disciplina> disciplinas;
	
	DisciplinaDAO daoDisciplinas;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atividades);
		
		this.carregarComponentes();
		this.carregarListenners();
		this.carregaBanco();
		
		this.disciplinas = daoDisciplinas.get();
		
		ArrayAdapter<Disciplina> adapter = new ArrayAdapter<Disciplina>(this, android.R.layout.simple_list_item_1, disciplinas);
		this.lvDisciplinas.setAdapter(adapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		menu.add(0, ID_MENU_NOVA_DISCIPLINA, ID_MENU_NOVA_DISCIPLINA, MENU_NOVA_DISCIPLINA);
		//menu.add(0, SOBRE, 2, "Sobre");
		
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
					daoDisciplinas.inserir(new Disciplina(input.getText().toString()));
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
	
	private void carregarComponentes(){
		this.lvDisciplinas = (ListView) findViewById(R.id.lvDisciplinas);
	}
	
	private void carregarListenners(){
		
	}
	
	private void carregaBanco(){
		this.daoDisciplinas = new DisciplinaDAO(this);
	}
}
