package com.lcorp.foxhound.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.lcorp.foxhound.config.ConfiguracaoFirebase;

public class Usuario {

    private String nome;
    private String idUsuario;
    private String email;
    private String senha;

    public Usuario() {
    }

    public void salvar(){
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference usuario = referenciaFirebase.child("Usuários").child( getIdUsuario() );

        usuario.setValue( this );
    }
    @Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
