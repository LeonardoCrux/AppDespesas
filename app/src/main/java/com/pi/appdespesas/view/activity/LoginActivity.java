package com.pi.appdespesas.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.pi.appdespesas.R;
import com.pi.appdespesas.config.AppUtil;
import com.pi.appdespesas.config.ConfiguracaoFirebase;
import com.pi.appdespesas.model.Usuario;

import static com.pi.appdespesas.R.id.inputEmail;

public class LoginActivity extends AppCompatActivity {
    private EditText inputEmail, inputSenha;
    private Button botaoEntrar;
    private FirebaseAuth firebaseAuth;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        validarLogin();
        getSupportActionBar().setTitle("Acesse sua conta");
        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarLogin();
            }
        });
    }

    private void initView(){
        inputEmail =  findViewById(R.id.inputEmailLogin);
        inputSenha = findViewById(R.id.inputSenhaLogin);
        botaoEntrar = findViewById(R.id.btEntrar);
    }

    public void validarLogin(){
        String email = inputEmail.getText().toString();
        String senha = inputSenha.getText().toString();
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
        usuario = new Usuario();
        usuario.setSenha(senha);
        usuario.setEmail(email);
        validarFirebase();
    }

    public void validarFirebase(){
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        firebaseAuth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(), PrincipalActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Email e/ou senha inválidos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
