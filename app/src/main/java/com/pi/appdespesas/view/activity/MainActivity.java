package com.pi.appdespesas.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.pi.appdespesas.R;
import com.pi.appdespesas.config.AppUtil;
import com.pi.appdespesas.config.ConfiguracaoFirebase;

public class MainActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verificarUsuarioLogado();

        addSlide( new FragmentSlide.Builder().background(android.R.color.white).fragment(R.layout.introimage1).canGoBackward(false).build());
        addSlide( new FragmentSlide.Builder().background(android.R.color.white).fragment(R.layout.introimage2).build());
        addSlide( new FragmentSlide.Builder().background(android.R.color.white).fragment(R.layout.introimage3).build());
        addSlide( new FragmentSlide.Builder().background(android.R.color.white).fragment(R.layout.introimage4).build());
        addSlide( new FragmentSlide.Builder().background(android.R.color.white).fragment(R.layout.intro_login).canGoForward(false).build());
        setButtonBackVisible(false);
        setButtonNextVisible(false);
        }


    public void btConta(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }
    public void txtCadastro(View view){
        startActivity(new Intent(this, CadastroActivity.class));
    }
    public void verificarUsuarioLogado(){
        FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), PrincipalActivity.class));
            finish();
        }
    }
}
