package br.ufrn.stronda.newlostandfound.Activity;


import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import br.ufrn.stronda.newlostandfound.R;


public class CadastroActivity extends AppCompatActivity {
    EditText name,email,senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        name = (EditText) findViewById(R.id.nameTxtc);
        email = (EditText) findViewById(R.id.emailTxtc);
        senha = (EditText) findViewById(R.id.senhaTxtc);

    }


    public void cancelar(View view) {
        this.finish();
    }
}
