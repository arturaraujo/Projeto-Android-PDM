package br.edu.ifpb.tsi.pdm.pdmproject.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import br.edu.ifpb.tsi.pdm.pdmproject.R;
import br.edu.ifpb.tsi.pdm.pdmproject.R.id;
import br.edu.ifpb.tsi.pdm.pdmproject.R.layout;
import br.edu.ifpb.tsi.pdm.pdmproject.broadcast.AlarmReceiver;
import br.edu.ifpb.tsi.pdm.pdmproject.dao.AtividadeDAO;
import br.edu.ifpb.tsi.pdm.pdmproject.dao.DisciplinaDAO;
import br.edu.ifpb.tsi.pdm.pdmproject.dao.TarefaDAO;
import br.edu.ifpb.tsi.pdm.pdmproject.model.Atividade;
import br.edu.ifpb.tsi.pdm.pdmproject.model.Disciplina;
import br.edu.ifpb.tsi.pdm.pdmproject.model.Tarefa;

public class NovaTarefaActivity extends Activity {
	
	Spinner spnAtividade, spnDisciplina;
	EditText etDataTarefa;
	Button btnCriar;
	Calendar c = Calendar.getInstance(); //TODO tirar isso
	int posicaoTarefa;
	
	AtividadeDAO daoAtividade;
	DisciplinaDAO daoDisciplina;
	TarefaDAO daoTarefa;
	private int posicaoAtividade, posicaoDisciplina;
	
	List<Disciplina> disciplinas;
	List<Atividade> atividades;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nova_tarefa);
		this.carrregaComponentes();
		this.setListeners();
		this.carregaBanco();
		
		this.atividades = daoAtividade.get();
		setSpinnerAtividade(spnAtividade, atividades);

		this.disciplinas = daoDisciplina.get();
		setSpinnerDisciplina(spnDisciplina, disciplinas);
		
		//TODO Setar datas
		//TODO Setar as notificaï¿½ï¿½es

	}
	
	private void carregaBanco(){
		this.daoAtividade = new AtividadeDAO(this);
		this.daoDisciplina = new DisciplinaDAO(this);
		this.daoTarefa = new TarefaDAO(this);
	}

	private void carrregaComponentes(){
		this.spnAtividade = (Spinner) findViewById(R.id.spnAtividade);
		this.spnDisciplina = (Spinner) findViewById(R.id.spnDisciplina);
		this.etDataTarefa = (EditText) findViewById(R.id.etDataTarefa);
		this.btnCriar = (Button) findViewById(R.id.btnOkNovaTarefa);
	}
	
	private void setListeners(){
		this.btnCriar.setOnClickListener(new OnClickBotao());
	}
	
	private void setSpinnerAtividade(Spinner spinner, List<Atividade> list){
		
		ArrayAdapter<Atividade> adapterAtividade = new ArrayAdapter<Atividade>(this, android.R.layout.simple_spinner_item, list);
		adapterAtividade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapterAtividade);
		
		spinner.setOnItemSelectedListener(new AtividadeListener());
	}
	
	private void setSpinnerDisciplina(Spinner spinner, List<Disciplina> list){
		
		ArrayAdapter<Disciplina> adapterDisciplina = new ArrayAdapter<Disciplina>(this, android.R.layout.simple_spinner_item, list);
		adapterDisciplina.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapterDisciplina);
		
		spinner.setOnItemSelectedListener(new DisciplinaListener());
	}
	
	
	
	public class AtividadeListener implements OnItemSelectedListener {
		@Override
	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			posicaoAtividade = pos;
	    }

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}

		
	}
	
	public class DisciplinaListener implements OnItemSelectedListener {
		@Override
	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			posicaoDisciplina = pos;
	    }

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}

	}
	
	public class OnClickBotao implements OnClickListener{

		@Override
		public void onClick(View v) {
			Calendar notificacao = Calendar.getInstance();
			notificacao.add(Calendar.SECOND, 7);
			Tarefa tarefa =  new Tarefa(atividades.get(posicaoAtividade), disciplinas.get(posicaoDisciplina), c, notificacao);
			
			daoTarefa.inserir(tarefa);
			
			Intent intent = new Intent(NovaTarefaActivity.this, AlarmReceiver.class);
			intent.putExtra("ATIVIDADE", tarefa.getAtividade().toString());
			intent.putExtra("DISCIPLINA", tarefa.getDisciplina().toString());

			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			String dataHora = format.format(tarefa.getDataHora().getTime());
			intent.putExtra("DATA_HORA", dataHora);
			
			PendingIntent pi = PendingIntent.getBroadcast(NovaTarefaActivity.this, 0, intent, 0);
			AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
			manager.set(AlarmManager.RTC, notificacao.getTimeInMillis(), pi);
			
			setResult(RESULT_OK);
			finish();
			
			//TODO gerar notificação pra data setada.
		}

	}
}
