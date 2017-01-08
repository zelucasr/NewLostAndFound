package br.ufrn.stronda.newlostandfound.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.ufrn.stronda.newlostandfound.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilActivity extends AppCompatActivity {
    CircleImageView imagem;
    TextView nome,emai,telefone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        nome = (TextView) findViewById(R.id.nomeperfil);
        emai = (TextView) findViewById(R.id.emailperfil);
        imagem = (CircleImageView) findViewById(R.id.fotoperfil);
        telefone = (TextView) findViewById(R.id.telefoneperfil);
        final ProgressDialog progress = new ProgressDialog(this);

        progress.setMessage("AGUARDE, ATUALIZANDO");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                progress.cancel();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 2500);

        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab2);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PerfilActivity.this,TelefoneActivity.class);
                startActivity(intent);

            }
        });


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid().toString();
        if (user != null) {
            Log.d("google", "onAuthStateChanged:signed_in:" + user.getUid());
            //Obtem os valores de nome,email e a imagem do usuário logado atualmente
            String name = user.getDisplayName();
            String email = user.getEmail();

            Uri photoUrl = user.getPhotoUrl();
            //nome.setText(name);
            //Biblioteca externa que faz com que se pegue o URL
            //da foto que o google disponibiliza e coloque no local que escolher
            Glide.with(this)
                    .load(photoUrl)
                    .into(imagem);

        }



        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //telefone.setText("TELEFONE: "+ dataSnapshot.child("telefone").getValue().toString());
                nome.setText("Nome: "+ dataSnapshot.child("nome").getValue().toString());
                emai.setText("Email: "+ dataSnapshot.child("email").getValue().toString());
                nome.setAllCaps(true);
                emai.setAllCaps(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
