package com.lcorp.foxhound.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.lcorp.foxhound.R;
import com.lcorp.foxhound.config.ConfiguracaoFirebase;
import com.lcorp.foxhound.helper.Base64Custom;
import com.lcorp.foxhound.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoNome = findViewById(R.id.edit_login_email);
        campoEmail = findViewById(R.id.edit_email);
        campoSenha = findViewById(R.id.edit_senha);
    }

    public void cadastrarUsuario(Usuario usuario){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() ){
                    Toast.makeText(CadastroActivity.this,
                            "Bem-vindo, Snake",
                            Toast.LENGTH_SHORT).show();
                    finish();
                    try {

                        String identificadorUsuario = Base64Custom.codificarBase64( usuario.getEmail() );
                        usuario.setIdUsuario( identificadorUsuario );
                        usuario.salvar();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch ( FirebaseAuthWeakPasswordException e){
                        excecao = "Snake, essa senha é muito previsível";
                    }catch ( FirebaseAuthInvalidCredentialsException e){
                        excecao = "Snake, que email é esse?";
                    }catch ( FirebaseAuthUserCollisionException e){
                        excecao = "Nós já nos conhecemos! Eu já vi esse email antes";
                    }catch ( Exception e){
                        excecao = "Erro! Não sabemos quem você é intruso! Tente de novo!" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void validarCadastroUsuario(View view){

        //Recuperar texto dos campos
        String textoNome = campoNome.getText().toString();
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();

        if ( !textoNome.isEmpty() ){//verifica nome
            if ( !textoEmail.isEmpty() ){//verifica email
                if (!textoSenha.isEmpty()){

                    Usuario usuario = new Usuario();
                    usuario.setNome( textoNome );
                    usuario.setEmail( textoEmail );
                    usuario.setSenha( textoSenha );

                    cadastrarUsuario( usuario );

                }else {
                    Toast.makeText(CadastroActivity.this,
                            "Preencha a senha!",
                            Toast.LENGTH_LONG).show();
                }

            }else {
                Toast.makeText(CadastroActivity.this,
                        "Preencha seu email!",
                        Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(CadastroActivity.this,
                    "Preencha seu nome!",
                    Toast.LENGTH_LONG).show();
        }
    }
}