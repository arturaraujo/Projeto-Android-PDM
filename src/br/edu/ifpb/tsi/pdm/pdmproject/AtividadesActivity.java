package br.edu.ifpb.tsi.pdm.pdmproject;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import br.edu.ifpb.tsi.pdm.pdmproject.dao.AtividadeDAO;
import br.edu.ifpb.tsi.pdm.pdmproject.model.Atividade;

public class AtividadesActivity extends Activity {
	private static final int ID_MENU_NOVA_ATIVIDADE = 1;
	private static final String MENU_NOVA_ATIVIDADE = "Nova Atividade";

	ListView lvAtividades;
	List<Atividade> atividades;
	
	AtividadeDAO daoAtividade;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atividades);
		
		this.carregarComponentes();
		this.carregarListenners();
		this.carregaBanco();
		
		this.atividades = daoAtividade.get();
		
		ArrayAdapter<Atividade> adapter = new ArrayAdapter<Atividade>(this, android.R.layout.simple_list_item_1, atividades);
		this.lvAtividades.setAdapter(adapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		menu.add(0, ID_MENU_NOVA_ATIVIDADE, 1, MENU_NOVA_ATIVIDADE);
		//menu.add(0, SOBRE, 2, "Sobre");
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case ID_MENU_NOVA_ATIVIDADE:
			
			break;

		default:
			break;
		}
		return true;
	}
	
	private void carregarComponentes(){
		this.lvAtividades = (ListView) findViewById(R.id.lvAtividades);
	}
	
	private void carregarListenners(){
		
	}
	
	private void carregaBanco(){
		this.daoAtividade = new AtividadeDAO(this);
	}
}
