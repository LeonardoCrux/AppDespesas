package com.pi.appdespesas.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pi.appdespesas.R;
import com.pi.appdespesas.config.AppUtil;
import com.pi.appdespesas.config.ConfiguracaoFirebase;
import com.pi.appdespesas.config.CustomDate;
import com.pi.appdespesas.model.Movimentacao;
import com.pi.appdespesas.model.Usuario;

public class DespesaActivity extends AppCompatActivity {
    private TextInputEditText inputData, inputCategoria, inputDescricao;
    private EditText editValor;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private Double totalDespesas, despesa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesa);
        initView();
        databaseReference = ConfiguracaoFirebase.getDatabaseReference();
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        recuperarTotalDespesas();
    }

    private void initView() {
        inputData = findViewById(R.id.inputData);
        inputCategoria = findViewById(R.id.inputCategoria);
        inputDescricao = findViewById(R.id.inputDescricao);
        editValor = findViewById(R.id.editValor);
        inputData.setText(CustomDate.dataAtual());
    }

    public void fabSalvarDespesa(View view) {
        if (validarCampos()) {
            double valorDespesa = Double.parseDouble(editValor.getText().toString());
            Movimentacao movimentacao = new Movimentacao(valorDespesa, inputData.getText().toString(), inputCategoria.getText().toString(), inputDescricao.getText().toString(), "d");
            despesa = valorDespesa;
            Double despesaAtualizada = totalDespesas + despesa;
            DatabaseReference usuarioReference = databaseReference.child("usuarios").child(AppUtil.getIdUsuario(getApplicationContext()));
            usuarioReference.child("despesaTotal").setValue(despesaAtualizada);
            String dataFirebase = CustomDate.dataMesFormatada(inputData.getText().toString());
            databaseReference.child("movimentacao").child(AppUtil.getIdUsuario(getApplicationContext()))
                    .child(dataFirebase).push().setValue(movimentacao);
            startActivity(new Intent(this, PrincipalActivity.class));

            finish();
        }

    }

    public boolean validarCampos() {
        if (editValor.getText().toString().isEmpty() || inputData.getText().toString().isEmpty() || inputDescricao.getText().toString().isEmpty() || inputCategoria.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void recuperarTotalDespesas() {
        DatabaseReference usuarioReference = databaseReference.child("usuarios").child(AppUtil.getIdUsuario(getApplicationContext()));
        usuarioReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario =  dataSnapshot.getValue(Usuario.class);
                totalDespesas = usuario.getDespesaTotal();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

