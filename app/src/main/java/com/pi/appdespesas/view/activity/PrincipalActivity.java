package com.pi.appdespesas.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pi.appdespesas.R;
import com.pi.appdespesas.adapter.MovimentacaoAdapter;
import com.pi.appdespesas.config.AppUtil;
import com.pi.appdespesas.config.ConfiguracaoFirebase;
import com.pi.appdespesas.model.Movimentacao;
import com.pi.appdespesas.model.Usuario;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {
    private MaterialCalendarView calendarView;
    private TextView textSaldo, textPessoa;
    private RecyclerView recyclerView;
    private MovimentacaoAdapter adapter;
    private String mesAno;
    private List<Movimentacao> movimentacaoList = new ArrayList<>();
    private FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
    private DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private Double totalDespesas = 0.0;
    private Double totalReceita = 0.0;
    private Double resumoUsuario = 0.0;
    private Movimentacao movimentacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        initView();
        configuraCalendar();
        recuperaInformações();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        getSupportActionBar();
        swipeDel();
    }

    public void swipeDel(){
        ItemTouchHelper.Callback itemTouchHelper = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlag =  ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlag = ItemTouchHelper.END | ItemTouchHelper.START;
                return makeMovementFlags(dragFlag, swipeFlag);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluirMovimentacaoFirebase(viewHolder);
            }
        };

        new ItemTouchHelper (itemTouchHelper).attachToRecyclerView(recyclerView);
    }

    public void excluirMovimentacaoFirebase(final RecyclerView.ViewHolder viewHolder){
        AlertDialog.Builder alertDBuilder = new AlertDialog.Builder(this);
        alertDBuilder.setTitle("Excluir Movimentação");
        alertDBuilder.setMessage("Você tem certeza que deseja excluir essa movimentação ?");
        alertDBuilder.setCancelable(false);
        alertDBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Cancelado", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
        });
        alertDBuilder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                movimentacao = movimentacaoList.get(viewHolder.getAdapterPosition());
                final DatabaseReference movimentacoesReference = databaseReference.child("movimentacao").child(AppUtil.getIdUsuario(getApplicationContext())).child(mesAno);
                movimentacoesReference.child(movimentacao.getId()).removeValue();
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                atualizar();
            }
        });
        AlertDialog alertDialog =  alertDBuilder.create();
        alertDialog.show();
    }

    private void initView() {
        calendarView = findViewById(R.id.calendarView);
        textPessoa = findViewById(R.id.textPessoa);
        textSaldo = findViewById(R.id.textSaldo);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MovimentacaoAdapter(movimentacaoList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
    }

    public void addReceita(View view) {
        startActivity(new Intent(this, ReceitaActivity.class));
        finish();
    }


    public void addDespesa(View view) {
        startActivity(new Intent(this, DespesaActivity.class));
        finish();
    }

    private void configuraCalendar() {
        CalendarDay dataAtual = calendarView.getCurrentDate();
        String mes = String.format("%02d", dataAtual.getMonth());
        mesAno = String.valueOf(mes + "" + dataAtual.getYear());
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mes = String.format("%02d", date.getMonth());
                mesAno = String.valueOf(mes + "" + date.getYear());
                recuperaMovimentacoes();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void recuperaInformações() {
        DatabaseReference usuarioReference = databaseReference.child("usuarios").child(AppUtil.getIdUsuario(getApplicationContext()));
        usuarioReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                totalDespesas = usuario.getDespesaTotal();
                totalReceita = usuario.getReceitaTotal();
                resumoUsuario = totalReceita - totalDespesas;
                DecimalFormat decimalFormat = new DecimalFormat("0.##");
                String result = decimalFormat.format(resumoUsuario);
                textSaldo.setText("R$ " + result);
                textPessoa.setText("Olá, " + usuario.getNome());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void atualizar(){
        if(movimentacao.getTipo().equals("r")){
            totalReceita -= movimentacao.getValor();
            DatabaseReference usuarioReference = databaseReference.child("usuarios").child(AppUtil.getIdUsuario(getApplicationContext()));
            usuarioReference.child("receitaTotal").setValue(totalReceita);
        }if(movimentacao.getTipo().equals("d")){
            totalDespesas -= movimentacao.getValor();
            DatabaseReference usuarioReference = databaseReference.child("usuarios").child(AppUtil.getIdUsuario(getApplicationContext()));
            usuarioReference.child("despesaTotal").setValue(totalDespesas);
        }
    }

    private void recuperaMovimentacoes() {
        final DatabaseReference movimentacoesReference = databaseReference.child("movimentacao").child(AppUtil.getIdUsuario(getApplicationContext())).child(mesAno);
        movimentacoesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                movimentacaoList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Movimentacao movimentacao = data.getValue(Movimentacao.class);
                    movimentacaoList.add(movimentacao);
                    movimentacao.setId(data.getKey());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSair:
                firebaseAuth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperaInformações();
        recuperaMovimentacoes();
    }
}
