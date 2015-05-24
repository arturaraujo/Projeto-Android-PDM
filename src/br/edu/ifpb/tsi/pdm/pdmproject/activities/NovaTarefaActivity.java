package br.edu.ifpb.tsi.pdm.pdmproject.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import br.edu.ifpb.tsi.pdm.pdmproject.R;
import br.edu.ifpb.tsi.pdm.pdmproject.broadcast.AlarmReceiver;
import br.edu.ifpb.tsi.pdm.pdmproject.dao.AtividadeDAO;
import br.edu.ifpb.tsi.pdm.pdmproject.dao.DisciplinaDAO;
import br.edu.ifpb.tsi.pdm.pdmproject.dao.TarefaDAO;
import br.edu.ifpb.tsi.pdm.pdmproject.model.Atividade;
import br.edu.ifpb.tsi.pdm.pdmproject.model.Disciplina;
import br.edu.ifpb.tsi.pdm.pdmproject.model.Tarefa;

@SuppressLint("SimpleDateFormat")
public class NovaTarefaActivity extends Activity {
	
	Spinner spnAtividade, spnDisciplina;
	DatePicker datePickerTarefa, datePickerNotificacao;
	TimePicker timePickerHoraNotificacao;
	Switch switchDefinirLembrete;
	TextView tvDataTarefa, tvDataNotificacao, tvHoraNotificacao;
	Button btnDefinirDataNotificacao, btnDefinirHoraNotificacao, btnCriar;

	AtividadeDAO daoAtividade;
	DisciplinaDAO daoDisciplina;
	TarefaDAO daoTarefa;
	private int posicaoAtividade, posicaoDisciplina;
	
	List<Disciplina> disciplinas;
	List<Atividade> atividades;
	boolean notificacao;
	
	Calendar calendar = Calendar.getInstance(); //TODO tirar isso
	SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat formatHora = new SimpleDateFormat("HH:mm");

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
		//TODO Setar as notifica��es
	}

	
	private void carrregaComponentes(){
		this.spnAtividade = (Spinner) findViewById(R.id.spnAtividade);
		this.spnDisciplina = (Spinner) findViewById(R.id.spnDisciplina);
		this.tvDataTarefa = (TextView) findViewById(R.id.tvDataTarefa);
		this.switchDefinirLembrete = (Switch) findViewById(R.id.swDefinirAtividade);
		this.tvDataNotificacao = (TextView) findViewById(R.id.tvDataNotificacao);
		this.btnDefinirDataNotificacao = (Button) findViewById(R.id.btnDefinirData);
		this.tvHoraNotificacao = (TextView) findViewById(R.id.tvHoraNotificacao);
		this.btnDefinirHoraNotificacao = (Button) findViewById(R.id.btnDefinirHora);
		
		this.tvDataTarefa.setText(formatData.format(calendar.getTime()));
		this.tvDataNotificacao.setText(formatData.format(calendar.getTime()));
		this.tvDataTarefa.setText(formatData.format(calendar.getTime()));
		this.tvHoraNotificacao.setText(formatHora.format(calendar.getTime()));
		
		this.habilitarNotificacao(false);

		this.btnCriar = (Button) findViewById(R.id.btnOk);
	}
	
	private void carregaBanco(){
		this.daoAtividade = new AtividadeDAO(this);
		this.daoDisciplina = new DisciplinaDAO(this);
		this.daoTarefa = new TarefaDAO(this);
	}
	
	private void setListeners(){
		this.btnCriar.setOnClickListener(new OnClickBotao());
		this.switchDefinirLembrete.setOnClickListener(new OnClickSwitch());
	}
	
	private void habilitarNotificacao(boolean enabled){
		this.tvDataNotificacao.setEnabled(enabled);
		this.btnDefinirDataNotificacao.setEnabled(enabled);
		this.tvHoraNotificacao.setEnabled(enabled);
		this.btnDefinirHoraNotificacao.setEnabled(enabled);
		notificacao = enabled;
	}

	private class OnClickSwitch implements OnClickListener{

		@Override
		public void onClick(View v) {
			habilitarNotificacao(switchDefinirLembrete.isChecked());
		}
		
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
			Tarefa tarefa =  new Tarefa(atividades.get(posicaoAtividade), disciplinas.get(posicaoDisciplina), calendar, notificacao);
			
			daoTarefa.inserir(tarefa);
			
			Intent intent = new Intent(NovaTarefaActivity.this, AlarmReceiver.class);
			intent.putExtra("ATIVIDADE", tarefa.getAtividade().toString());
			intent.putExtra("DISCIPLINA", tarefa.getDisciplina().toString());
			//dialog.setItems(itemsId, listener)
			
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			String dataHora = format.format(tarefa.getDataHora().getTime());
			intent.putExtra("DATA_HORA", dataHora);
			
			PendingIntent pi = PendingIntent.getBroadcast(NovaTarefaActivity.this, 0, intent, 0);
			AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
			manager.set(AlarmManager.RTC, notificacao.getTimeInMillis(), pi);
			
			setResult(RESULT_OK);
			finish();
			
			//TODO gerar notifica��o pra data setada.
		}

	}
}
