package br.ufrn.stronda.newlostandfound.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

import br.ufrn.stronda.newlostandfound.Model.AcheiObjeto;
import br.ufrn.stronda.newlostandfound.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class AcheiActivity extends AppCompatActivity implements OnMapReadyCallback {
    Spinner catspn,locspn;
    EditText descricao;
    EditText nomeObjeto;
    Button confirmar;
    AcheiObjeto acheiObjeto;
    byte[] data1;
    private DatabaseReference mDatabase;

    private GoogleMap maps;
    LatLng clicado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achei);

        //Associando os elementos da tela a variáveis na classe
        nomeObjeto = (EditText) findViewById(R.id.nomeA);
        descricao = (EditText) findViewById(R.id.descricaoA);
        confirmar = (Button) findViewById(R.id.acheiconfimarBtn);
        catspn = (Spinner) findViewById(R.id.acheicategoriaSpn);
        locspn = (Spinner) findViewById(R.id.acheilocalizacaoSpn);

        clicado = new LatLng(0,0);

        //Criando um arrayadapter para dispor os elementos no spinner de categoria
        ArrayAdapter adaptercat = ArrayAdapter.createFromResource(this, R.array.itens, R.layout.spinner_item);
        adaptercat.setDropDownViewResource(R.layout.spinner_dropdown_item);
        catspn.setAdapter(adaptercat);

        //Criando um arrayadapter para dispor os elementos no spinner de categoria
        ArrayAdapter adapterloc = ArrayAdapter.createFromResource(this, R.array.local, R.layout.spinner_item);
        adapterloc.setDropDownViewResource(R.layout.spinner_dropdown_item);
        locspn.setAdapter(adapterloc);

        //Atribuindo a instância do banco de dados Firebase a variável do firebase na classe
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Obtendo o usuário que está logado atualmente no sistema e atribuindo a uma variável
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            //Possui um usuário logado no sistema
            Log.d("google", "onAuthStateChanged:signed_in:" + user.getUid());
            //Obtem os valores de nome,email e o id do usuário logado atualmente
            final String name = user.getDisplayName();
            final String email = user.getEmail();
            final String userid = user.getUid();

            //função vai executar quando o botão confirmar for clicado, pega os valores que estão nos spinners
            // e no campo de texto e vai cadastrar no banco de dados com as tags que estão abaixo, cada child é um nó
            // na tabela do banco.

            confirmar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clicado.latitude == 0 && clicado.longitude == 0) {
                        Toast.makeText(getBaseContext(), "Por favor, marque a posição no mapa", Toast.LENGTH_SHORT).show();
                    } else {
                        mDatabase.child("Usuarios").child(userid).child("nome").setValue(name);
                        mDatabase.child("Usuarios").child(userid).child("email").setValue(email);
                        //chama a função para cadastrar no banco
                        novoObjeto(nomeObjeto.getText().toString(),descricao.getText().toString(), catspn.getSelectedItem().toString(), locspn.getSelectedItem().toString(), userid, user.getDisplayName(), user.getEmail());

                        //após cadastrar, gera um toast para informar que foi cadastrado no banco
                        Toast.makeText(getBaseContext(), "Cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });

        }
        else {
            Log.d("google", "onAuthStateChanged:signed_out");
        }

        SupportMapFragment mapFragmentA = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapaAchei);
        mapFragmentA.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        maps = googleMap;
        maps.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng reitoria = new LatLng(-5.8396243,-35.2020049);

        maps.moveCamera(CameraUpdateFactory.newLatLngZoom(reitoria, 15));

        maps.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // Coloca o marcador e salva a posição
                maps.clear();
                maps.addMarker(new MarkerOptions().position(point));
                clicado = point;
                //Toast.makeText(getBaseContext(),clicado.latitude + " " + clicado.longitude,Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Aqui é tratado a parte de tirar foto do celular
        if (data!=null){
            Bundle bundle = data.getExtras();
            if(bundle != null){
                //Após o processo de tirar a foto, ela é colocada dentro do
                //CircleImageView para ser exibida
                Bitmap img = (Bitmap) bundle.get("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                data1 = baos.toByteArray();
                CircleImageView iv = (CircleImageView) findViewById(R.id.imgvw);
                iv.setImageBitmap(img);
            }
        }


    }



    public void cancelar(View view) {
        this.finish();
    }


    //É chamada apenas para cadastrar um objeto no banco.
    private void novoObjeto(String nomeDoObjeto,String descricao, String categoria,String localizacao,String userId,String nome, String email) {
        String id = mDatabase.child("Usuarios").child(userId).child("Objetos").child("Achados").push().getKey();
        AcheiObjeto acheiObjeto = new AcheiObjeto(nomeDoObjeto,descricao,categoria,localizacao,nome,email,clicado.latitude,clicado.longitude,id);
        //"setValue" coloca o valor que está no parâmetro, dentro do banco.

        mDatabase.child("Usuarios").child(userId).child("Objetos").child("Achados").child(id).setValue(acheiObjeto);
    }


}
