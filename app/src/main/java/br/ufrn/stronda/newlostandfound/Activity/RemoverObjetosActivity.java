package br.ufrn.stronda.newlostandfound.Activity;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class RemoverObjetosActivity extends AppCompatActivity {

    TextView descricao,localizacao,categoria,n,e;
    CircleImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remover_objetos);



        Bundle extras;
        final String newString;
        final String newString1;
        final String newString2;
        final String imagemstring;
        final String nome;
        final String email;
        boolean achados = false;
        boolean perdidos=false;
        String chave = null;
        int imagemint=0;
        descricao = (TextView) findViewById(R.id.removerdescricaoobjeto);
        localizacao = (TextView) findViewById(R.id.removerlocalizacaoobjeto);
        categoria = (TextView) findViewById(R.id.removercategoriaobjeto);
        image = (CircleImageView) findViewById(R.id.removerimagemObjeto);
        n = (TextView) findViewById(R.id.removernomeob);
        e = (TextView) findViewById(R.id.removeremailob);
        FloatingActionButton remover = (FloatingActionButton) findViewById(R.id.fabremover);





        if (savedInstanceState == null) {
            extras = getIntent().getExtras();
            if (extras == null) {
                newString = null;
                newString1 = null;
                newString2 = null;
                imagemstring = null;
                imagemint = 0;
                nome = null;
                email = null;

            } else {
                newString = extras.getString("removerdescricao");
                newString1 = extras.getString("removercategoria");
                newString2 = extras.getString("removerlocalizacao");
                imagemstring = extras.getString("removerimagem");
                imagemint = extras.getInt("removerimagemint");
                nome = extras.getString("nome");
                email = extras.getString("email");
                chave = extras.getString("key");
                achados = extras.getBoolean("achados");
                perdidos = extras.getBoolean("perdidos");

                if (imagemstring != null) {
                    Glide.with(this).load(imagemstring).into(image);
                } else if (imagemint != 0) {
                    image.setImageResource(imagemint);
                }

                descricao.setText(newString);
                categoria.setText(newString1);
                localizacao.setText(newString2);
                n.setText(nome);
                e.setText(email);


                descricao.setAllCaps(true);
                categoria.setAllCaps(true);
                localizacao.setAllCaps(true);
                n.setAllCaps(true);
                e.setAllCaps(true);
            }
        }

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userid = user.getUid();


        final DatabaseReference refa = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userid).child("Objetos").child("Achados");
        final DatabaseReference refp = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userid).child("Objetos").child("Perdidos");
        final String finalChave = chave;

        final boolean finalAchados = achados;
        final boolean finalPerdidos = perdidos;
        remover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(RemoverObjetosActivity.this);
                builder1.setMessage("Deseja remover esse objeto?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (finalAchados){
                                refa.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        refa.child(finalChave).removeValue();

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                }

                                if(finalPerdidos){
                                refp.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        refp.child(finalChave).removeValue();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                }
                                dialog.cancel();
                                finish();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

    }

}
