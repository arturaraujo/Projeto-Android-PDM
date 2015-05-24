package br.edu.ifpb.tsi.pdm.pdmproject.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
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
	Button btnDefinirDataTarefa, btnDefinirDataNotificacao, btnDefinirHoraNotificacao, btnCriar;
	
	DatePickerDialog.OnDateSetListener date;
	TimePickerDialog.OnTimeSetListener time;

	AtividadeDAO daoAtividade;
	DisciplinaDAO daoDisciplina;
	TarefaDAO daoTarefa;
	private int posicaoAtividade, posicaoDisciplina;
	
	List<Disciplina> disciplinas;
	List<Atividade> atividades;
	boolean notificacao;
	
	Calendar calendarTarefa = Calendar.getInstance();
	Calendar calendarNotificacao = Calendar.getInstance();
	SimpleDateFormat formatData = new SimpleDateFormat("EE, dd/MM/yyyy");
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
	}

	
	private void carrregaComponentes(){
		this.spnAtividade = (Spinner) findViewById(R.id.spnAtividade);
		this.spnDisciplina = (Spinner) findViewById(R.id.spnDisciplina);
		this.tvDataTarefa = (TextView) findViewById(R.id.tvDataTarefa);
		this.btnDefinirDataTarefa = (Button) findViewById(R.id.btnDefinirDataTarefa);
		this.switchDefinirLembrete = (Switch) findViewById(R.id.swDefinirAtividade);
		this.tvDataNotificacao = (TextView) findViewById(R.id.tvDataNotificacao);
		this.btnDefinirDataNotificacao = (Button) findViewById(R.id.btnDefinirData);
		this.tvHoraNotificacao = (TextView) findViewById(R.id.tvHoraNotificacao);
		this.btnDefinirHoraNotificacao = (Button) findViewById(R.id.btnDefinirHora);
		
		this.tvDataTarefa.setText(formatData.format(calendarTarefa.getTime()));
		this.tvDataNotificacao.setText(formatData.format(calendarTarefa.getTime()));
		this.tvDataTarefa.setText(formatData.format(calendarTarefa.getTime()));
		this.tvHoraNotificacao.setText(formatHora.format(calendarTarefa.getTime()));
		
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
		this.btnDefinirDataTarefa.setOnClickListener(new OnDataClickListener());
		this.btnDefinirDataNotificacao.setOnClickListener(new OnDataClickListener());
		this.btnDefinirHoraNotificacao.setOnClickListener(new OnHoraClickListener());
		//this.btnDefinirHoraNotificacao.setOnClickListener(l);
		this.date = new DataListenerTarefa();
		this.time = new HoraListenerTarefa();
	}
	
	private void habilitarNotificacao(boolean enabled){
//		this.tvDataNotificacao.setEnabled(enabled);
//		this.btnDefinirDataNotificacao.setEnabled(enabled);
//		this.tvHoraNotificacao.setEnabled(enabled);
//		this.btnDefinirHoraNotificacao.setEnabled(enabled);
		int visibility = enabled ? View.VISIBLE : View.INVISIBLE;
		
		this.tvDataNotificacao.setVisibility(visibility);
		this.btnDefinirDataNotificacao.setVisibility(visibility);
		this.tvHoraNotificacao.setVisibility(visibility);
		this.btnDefinirHoraNotificacao.setVisibility(visibility);
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
	
	public class OnDataClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			new DatePickerDialog(NovaTarefaActivity.this, date,
					calendarTarefa.get(Calendar.YEAR),
					calendarTarefa.get(Calendar.MONTH),
					calendarTarefa.get(Calendar.DAY_OF_MONTH)).show();
		}
		
	}
	
	public class OnHoraClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			new TimePickerDialog(NovaTarefaActivity.this, time, 
					calendarNotificacao.get(Calendar.HOUR_OF_DAY),
					calendarNotificacao.get(Calendar.MINUTE), true).show();
		}
		
	}
	
	public class HoraListenerTarefa implements OnTimeSetListener{

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			calendarNotificacao.set(Calendar.HOUR_OF_DAY, hourOfDay);
			calendarNotificacao.set(Calendar.MINUTE, minute);
			tvHoraNotificacao.setText(formatHora.format(calendarNotificacao.getTime()));
		}
	}
	
	public class DataListenerTarefa implements OnDateSetListener{

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			calendarTarefa.set(Calendar.YEAR, year);
	        calendarTarefa.set(Calendar.MONTH, monthOfYear);
	        calendarTarefa.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			tvDataTarefa.setText(formatData.format(calendarTarefa.getTime()));
		}
		
	}
	
	public class DataListenerNotificacao implements OnDateSetListener{

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			calendarNotificacao.set(Calendar.YEAR, year);
			calendarNotificacao.set(Calendar.MONTH, monthOfYear);
	        calendarNotificacao.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			tvDataNotificacao.setText(formatData.format(calendarTarefa.getTime()));
		}
		
	}
	
	public class OnClickBotao implements OnClickListener{

		@Override
		public void onClick(View v) {
			//Calendar notificacao = Calendar.getInstance();
			//notificacao.add(Calendar.SECOND, 7);
			Tarefa tarefa =  new Tarefa(atividades.get(posicaoAtividade), disciplinas.get(posicaoDisciplina), calendarTarefa, calendarNotificacao);
			
			daoTarefa.inserir(tarefa);
			
			Intent intent = new Intent(NovaTarefaActivity.this, AlarmReceiver.class);
			intent.putExtra("ATIVIDADE", tarefa.getAtividade().toString());
			intent.putExtra("DISCIPLINA", tarefa.getDisciplina().toString());
			
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			String dataHora = format.format(tarefa.getDataHora().getTime());
			intent.putExtra("DATA_HORA", dataHora);
			
			PendingIntent pi = PendingIntent.getBroadcast(NovaTarefaActivity.this, 0, intent, 0);
			AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
			manager.set(AlarmManager.RTC, calendarNotificacao.getTimeInMillis(), pi);
			
			setResult(RESULT_OK);
			finish();
		}

	}
}
