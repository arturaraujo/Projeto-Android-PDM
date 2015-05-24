package br.edu.ifpb.tsi.pdm.pdmproject.activities;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import br.edu.ifpb.tsi.pdm.pdmproject.dao.AtividadeDAO;
import br.edu.ifpb.tsi.pdm.pdmproject.model.Atividade;

public class AtividadesActivity extends Activity {
	private static final int ID_MENU_NOVA_ATIVIDADE = 1;
	private static final String MENU_NOVA_ATIVIDADE = "Nova Atividade";
	
	private static final int EDITAR= 0;
	private static final int EXCLUIR= 1;
	private static final String[] OPCOES_ATIVIDADE = {"Editar", "Excluir"};

	ListView lvAtividades;
	List<Atividade> atividades;
	
	AtividadeDAO daoAtividade;
	
	ArrayAdapter<Atividade> adapterAtividades;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atividades);
		
		this.carregarComponentes();
		this.carregarListenners();
		this.carregaBanco();
		
		this.atividades = daoAtividade.get();
		
		if (atividades == null || atividades.isEmpty()){
			AlertDialog.Builder dialog =  new AlertDialog.Builder(this);
			dialog.setTitle("Nenhuma Atividade Cadastrada");
			dialog.setMessage("Você não tem nenhuma atividade cadastrada. \n"
					+ "Acesse Menu -> Nova Atividade para cadastrar uma nova atividade!");
			dialog.setNeutralButton("OK", null);
			dialog.create().show();
		}
		
		adapterAtividades = new ArrayAdapter<Atividade>(this, android.R.layout.simple_list_item_1, atividades);
		this.lvAtividades.setAdapter(adapterAtividades);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		menu.add(0, ID_MENU_NOVA_ATIVIDADE, ID_MENU_NOVA_ATIVIDADE, MENU_NOVA_ATIVIDADE);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case ID_MENU_NOVA_ATIVIDADE:
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle("Criar atividade");
			alert.setMessage("Digite o nome da atividade: ");
			
			final EditText input = new EditText(this);
			alert.setView(input);
			
			alert.setPositiveButton("OK", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Atividade atividade = new Atividade(input.getText().toString());
					daoAtividade.inserir(atividade);
					adapterAtividades.add(daoAtividade.ler(atividade.getNome()));
					adapterAtividades.notifyDataSetChanged();
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
			AlertDialog.Builder builder = new AlertDialog.Builder(AtividadesActivity.this);
			ArrayAdapter<String> adapterOpcoes = new ArrayAdapter<String>(AtividadesActivity.this, android.R.layout.simple_list_item_1, OPCOES_ATIVIDADE);
			builder.setAdapter(adapterOpcoes, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which){
					case EXCLUIR:
						daoAtividade.remover(atividades.get(position).getId());
						adapterAtividades.remove(atividades.get(position));
						adapterAtividades.notifyDataSetChanged();
						break;
					case EDITAR:
						AlertDialog.Builder alert = new AlertDialog.Builder(AtividadesActivity.this);

						alert.setTitle("Editar Atividade");
						alert.setMessage("Digite o nome da atividade: ");
						
						final EditText input = new EditText(AtividadesActivity.this);
						input.setText(atividades.get(position).getNome());
						alert.setView(input);
						
						alert.setPositiveButton("OK", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
								Atividade atividade = atividades.get(position);
								atividade.setNome(input.getText().toString());
								daoAtividade.update(atividade);
								adapterAtividades.notifyDataSetChanged();
								setResult(RESULT_OK);
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
	
	private void carregarComponentes(){
		this.lvAtividades = (ListView) findViewById(R.id.lvAtividades);
	}
	
	private void carregarListenners(){
		this.lvAtividades.setOnItemClickListener(new OnAtividadeListener());
	}
	
	private void carregaBanco(){
		this.daoAtividade = new AtividadeDAO(this);
	}
}
