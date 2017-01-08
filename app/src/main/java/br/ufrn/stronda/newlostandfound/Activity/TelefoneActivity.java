package br.ufrn.stronda.newlostandfound.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.ufrn.stronda.newlostandfound.R;

public class TelefoneActivity extends AppCompatActivity {
    EditText telefone,nome,email1;
    private DatabaseReference mDatabase;
    Button pross,canc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telefone);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        pross = (Button) findViewById(R.id.prosseguirBtn);
        telefone = (EditText) findViewById(R.id.telefoneeditar);
        nome = (EditText) findViewById(R.id.nomeeditar);
        email1 = (EditText) findViewById(R.id.emaileditar);
        canc = (Button) findViewById(R.id.cancelarBtn);


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            final String userid = user.getUid().toString();
            String name = user.getDisplayName();
            String email = user.getEmail();

            nome.setText(name);
            email1.setText(email);

                pross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDatabase.child("Usuarios").child(userid).child("telefone").setValue(telefone.getText().toString());
                        mDatabase.child("Usuarios").child(userid).child("nome").setValue(nome.getText().toString());
                        mDatabase.child("Usuarios").child(userid).child("email").setValue(email1.getText().toString());
                        Toast.makeText(TelefoneActivity.this,"Atualizado com sucesso",Toast.LENGTH_SHORT).show();
                        finish();
                            }
                        });
                canc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

    }
}
