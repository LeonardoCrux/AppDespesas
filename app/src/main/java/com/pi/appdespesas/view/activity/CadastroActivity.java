package com.pi.appdespesas.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.pi.appdespesas.R;
import com.pi.appdespesas.config.AppUtil;
import com.pi.appdespesas.config.ConfiguracaoFirebase;
import com.pi.appdespesas.model.Usuario;

public class CadastroActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private EditText inputNome, inputEmail, inputSenha, inputConfirmarSenha;
    private Button botaoCadastro;
    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        firebaseAuth = FirebaseAuth.getInstance();
        initView();
        usuario = new Usuario();
        getSupportActionBar().setTitle("Cadastro");

        botaoCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarLogin();
            }
        });
    }

    private void initView() {
        botaoCadastro = findViewById(R.id.btCadastrar);
        inputNome = findViewById(R.id.inputNome);
        inputEmail = findViewById(R.id.inputEmail);
        inputSenha = findViewById(R.id.inputSenha);
        inputConfirmarSenha = findViewById(R.id.inputConfirmar);
    }

    private void validarLogin() {
        String nome = inputNome.getText().toString();
        String email = inputEmail.getText().toString();
        String senha = inputSenha.getText().toString();
        String confirmar = inputConfirmarSenha.getText().toString();
        if (email.isEmpty()) {
            Toast.makeText(this, "Digite o email", Toast.LENGTH_SHORT).show();
            inputEmail.requestFocus();
            return;
        }
        if (senha.isEmpty()) {
            Toast.makeText(this, "Digite a senha", Toast.LENGTH_SHORT).show();
            inputSenha.requestFocus();
            return;
        }
        if (nome.isEmpty()) {
            Toast.makeText(this, "Digite o nome", Toast.LENGTH_SHORT).show();
            inputNome.requestFocus();
            return;
        }
        if (!senha.equals(confirmar)) {
            Toast.makeText(this, "As senhas não conferem", Toast.LENGTH_SHORT).show();
            inputSenha.requestFocus();
            inputConfirmarSenha.requestFocus();
            return;
        }
        usuario.setEmail(email);
        usuario.setNome(nome);
        usuario.setSenha(senha);
        usuario.setDespesaTotal(0);
        usuario.setReceitaTotal(0);
        cadastarFirebase();
    }

    public void salvarUsuario(){
        DatabaseReference firebase = ConfiguracaoFirebase.getDatabaseReference();
        firebase.child("usuarios").child(AppUtil.getIdUsuario(this)).setValue(usuario);
    }

    public void cadastarFirebase(){
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        firebaseAuth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    AppUtil.salvarIdUsuario(getApplicationContext(), firebaseAuth.getCurrentUser().getUid());
                    salvarUsuario();
                    startActivity(new Intent(getApplicationContext(), PrincipalActivity.class));
                    finish();
                }
                else {
                    String exception = "";
                    try{
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        exception = "Digite uma senha com 6 ou mais caracteres";
                    }catch (FirebaseAuthUserCollisionException e){
                        exception = "Usuário já cadastrado";
                    }catch(FirebaseAuthInvalidCredentialsException e){
                        exception = "Digite um email válido";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this, exception, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}