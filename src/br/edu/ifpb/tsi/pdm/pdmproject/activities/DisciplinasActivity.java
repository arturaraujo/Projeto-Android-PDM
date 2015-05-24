package br.edu.ifpb.tsi.pdm.pdmproject.activities;

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
import br.edu.ifpb.tsi.pdm.pdmproject.R;
import br.edu.ifpb.tsi.pdm.pdmproject.dao.DisciplinaDAO;
import br.edu.ifpb.tsi.pdm.pdmproject.model.Disciplina;

public class DisciplinasActivity extends Activity {
	private static final int ID_MENU_NOVA_DISCIPLINA = 1;
	private static final String MENU_NOVA_DISCIPLINA = "Nova Disciplina";

	ListView lvDisciplinas;
	List<Disciplina> disciplinas;
	
	DisciplinaDAO daoDisciplinas;
	
	ArrayAdapter<Disciplina> adapterDisciplinas;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disciplinas);
		
		this.disciplinas = daoDisciplinas.get();
		
		if (disciplinas == null || disciplinas.isEmpty()){
			AlertDialog.Builder dialog =  new AlertDialog.Builder(this);
			dialog.setTitle("Nenhuma disciplina cadastrada");
			dialog.setMessage("Acesse Menu -> Nova Disciplina para cadastrar uma nova diciplina!");
			dialog.setNeutralButton("OK", null);
			dialog.create().show();
		}
		
		this.carregarComponentes();
		this.carregarListenners();
		this.carregaBanco();
		
		adapterDisciplinas = new ArrayAdapter<Disciplina>(this, android.R.layout.simple_list_item_1, disciplinas);
		this.lvDisciplinas.setAdapter(adapterDisciplinas);
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
					Disciplina disciplina = new Disciplina(input.getText().toString());
					daoDisciplinas.inserir(disciplina);
					adapterDisciplinas.add(daoDisciplinas.ler(disciplina.getNome()));
					adapterDisciplinas.notifyDataSetChanged();
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
