package br.ufrn.stronda.newlostandfound.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.ufrn.stronda.newlostandfound.Model.AcheiObjeto;
import br.ufrn.stronda.newlostandfound.Model.PerdiObjeto;
import br.ufrn.stronda.newlostandfound.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
        CircleImageView foto;
        TextView nome;
    private GoogleMap maps;
    Intent initObjeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Achados e Perdidos UFRN");
        setSupportActionBar(toolbar);

        //Apertando esse floating button, é aberto o google maps
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PrincipalActivity.this,MapsActivity.class);
                startActivity(i);
            }
        });*/


        //Apertando nesse floating button o usuário da um logout do sistema
        /*FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 FirebaseAuth.getInstance().signOut();
                finish();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Declarados para pegar os valores do usuário logado no sistema
        View header = navigationView.getHeaderView(0);
        nome = (TextView)header.findViewById(R.id.nameProfile);
        foto = (CircleImageView) header.findViewById(R.id.profile_image);


        //Obtendo o usuário que está logado atualmente no sistema e atribuindo a uma variável
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {

                    Log.d("google", "onAuthStateChanged:signed_in:" + user.getUid());
                //Obtem os valores de nome,email e a imagem do usuário logado atualmente
                    String name = user.getDisplayName();
                    String email = user.getEmail();
                    Uri photoUrl = user.getPhotoUrl();
                    nome.setText(name);
                //Biblioteca externa que faz com que se pegue o URL
                //da foto que o google disponibiliza e coloque no local que escolher
                Glide.with(this)
                        .load(photoUrl)
                        .into(foto);
            }
            else {

                    Log.d("google", "onAuthStateChanged:signed_out");
                }

        SupportMapFragment mapFragmentA = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapaInicial);
        mapFragmentA.getMapAsync(this);

        initObjeto = new Intent(this,ObjetoActivity.class);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            Intent i = new Intent(this, PerfilActivity.class);
            startActivity(i);
            // Handle the camera action
        } else if (id == R.id.nav_achei) {
            Intent i = new Intent(this,AcheiActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_perdi) {
            Intent i = new Intent(this,PerdiActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_listagem) {
            Intent i = new Intent(this,ListagemActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_objetos) {
            Intent intent = new Intent(this,ObjetosCadastradosActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_configuracoes) {


        } else if (id == R.id.nav_sobre) {
            Intent intent = new Intent(this,SobreActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
    //Transição de tela
    public void achei(View view) {
        Intent i = new Intent(this,AcheiActivity.class);
        startActivity(i);
    }

    //Transição de tela
    public void perdi(View view) {
        Intent i = new Intent(this,PerdiActivity.class);
        startActivity(i);
    }
    //Transição de tela
    public void listagem(View view) {
        Intent i = new Intent(this,ListagemActivity.class);
        startActivity(i);
    }*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        maps = googleMap;
        maps.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        maps.clear();
        LatLng reitoria = new LatLng(-5.8396243,-35.2020049);
        /*
        LatLng imd = new LatLng(-5.8325288,-35.2056435);
        LatLng cet = new LatLng(-5.843550, -35.199261);
        LatLng cb = new LatLng(-5.8420593,-35.2019848);
        LatLng bczm = new LatLng(-5.839793, -35.198982);
        LatLng setor1 = new LatLng(-5.837527, -35.199564);
        LatLng setor2 = new LatLng(-5.840543, -35.197225);
        LatLng setor3 = new LatLng(-5.841103, -35.200399);
        LatLng setor4 = new LatLng(-5.842451, -35.199776);
        LatLng setor5 = new LatLng(-5.838520, -35.197563);
        */
        /*
        maps.addMarker(new MarkerOptions().position(reitoria).title("REITORIA").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        maps.addMarker(new MarkerOptions().position(imd).title("IMD").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        maps.addMarker(new MarkerOptions().position(cet).title("C&T").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        maps.addMarker(new MarkerOptions().position(cb).title("CB").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        maps.addMarker(new MarkerOptions().position(bczm).title("BCZM").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

        maps.addMarker(new MarkerOptions().position(setor1).title("SETOR I").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        maps.addMarker(new MarkerOptions().position(setor2).title("SETOR II").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        maps.addMarker(new MarkerOptions().position(setor3).title("SETOR III").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        maps.addMarker(new MarkerOptions().position(setor4).title("SETOR IV").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        maps.addMarker(new MarkerOptions().position(setor5).title("SETOR V").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        */
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userid = user.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for( DataSnapshot dsp : dataSnapshot.getChildren() ) {
                    final AcheiObjeto o = new AcheiObjeto();

                    for (DataSnapshot dspc : dsp.child("Objetos").child("Achados").getChildren()) {


                        o.setNomeDoObjeto(dspc.child("nomeDoObjeto").getValue().toString());
                        o.setDescricao(dspc.child("descricao").getValue().toString());
                        o.setCategoria(dspc.child("categoria").getValue().toString());
                        o.setLocalizacao(dspc.child("localizacao").getValue().toString());
                        o.setNome(dspc.child("nome").getValue().toString());
                        o.setEmail(dspc.child("email").getValue().toString());
                        o.setLatitude(new Double(dspc.child("latitude").getValue().toString()));
                        o.setLongitude(new Double(dspc.child("longitude").getValue().toString()));

                        Log.d("ID", dspc.getKey());
                        Log.d("DESCRICAO", o.getDescricao());
                        Log.d("CATEGORIA", o.getCategoria());
                        Log.d("LOCALIZACAO", o.getLocalizacao());

                        LatLng local = new LatLng(o.getLatitude(), o.getLongitude());
                        maps.addMarker(new MarkerOptions().position(local).title("ACHADO").snippet(o.getNomeDoObjeto()+"\n"+o.getDescricao()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                        maps.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                            @Override
                            public View getInfoWindow(Marker arg0) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {

                                Context mContext = getApplicationContext();

                                LinearLayout info = new LinearLayout(mContext);
                                info.setOrientation(LinearLayout.VERTICAL);

                                TextView title = new TextView(mContext);
                                title.setTextColor(Color.GREEN);
                                title.setGravity(Gravity.CENTER);
                                title.setTypeface(null, Typeface.BOLD);
                                title.setText(marker.getTitle());

                                TextView snippet = new TextView(mContext);
                                snippet.setTextColor(Color.GRAY);
                                snippet.setText(marker.getSnippet());

                                info.addView(title);
                                info.addView(snippet);

                                return info;
                            }
                        });

                        /*maps.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                String desca = o.getDescricao();
                                String cata = o.getCategoria();
                                String loca = o.getLocalizacao();
                                String nomea = o.getNome();
                                String emaila= o.getEmail();

                                initObjeto.putExtra("descricao",desca);
                                initObjeto.putExtra("categoria",cata);
                                initObjeto.putExtra("localizacao",loca);
                                initObjeto.putExtra("imagemint",R.drawable.general);
                                initObjeto.putExtra("nome",nomea);
                                initObjeto.putExtra("email",emaila);
                                startActivity(initObjeto);
                            }
                        });*/

                    }



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<PerdiObjeto> objetosp = new ArrayList<PerdiObjeto>();
                for( DataSnapshot dsp1 : dataSnapshot.getChildren() ){
                    final PerdiObjeto o = new PerdiObjeto();
                    for (DataSnapshot dspc1 : dsp1.child("Objetos").child("Perdidos").getChildren()){

                        o.setDescricao(dspc1.child("descricao").getValue().toString());
                        o.setCategoria(dspc1.child("categoria").getValue().toString());
                        o.setLocalizacao(dspc1.child("localizacao").getValue().toString());
                        o.setImagem(dspc1.child("imagem").getValue().toString());
                        o.setNome(dspc1.child("nome").getValue().toString());
                        o.setEmail(dspc1.child("email").getValue().toString());
                        o.setLatitude(new Double(dspc1.child("latitude").getValue().toString()));
                        o.setLongitude(new Double(dspc1.child("longitude").getValue().toString()));
                        o.setNomeDoObjeto(dspc1.child("nomeDoObjeto").getValue().toString());

                        objetosp.add(o);
                        Log.d("ID",dspc1.getKey());

                        Log.d("DESCRICAO", o.getDescricao());
                        Log.d("CATEGORIA", o.getCategoria());
                        Log.d("LOCALIZACAO", o.getLocalizacao());

                        LatLng local = new LatLng(o.getLatitude(), o.getLongitude());
                        maps.addMarker(new MarkerOptions().position(local).title("PERDIDO").snippet(o.getNomeDoObjeto()+"\n"+o.getDescricao()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                        //alterar info window do marker
                        maps.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                            @Override
                            public View getInfoWindow(Marker arg0) {
                                return null;
                            }


                            @Override
                            public View getInfoContents(Marker marker) {
                                Context mContext = getApplicationContext();

                                LinearLayout info = new LinearLayout(mContext);
                                info.setOrientation(LinearLayout.VERTICAL);

                                TextView title = new TextView(mContext);
                                title.setTextColor(Color.GRAY);
                                title.setGravity(Gravity.CENTER);
                                title.setTypeface(null, Typeface.BOLD);
                                title.setText(marker.getTitle());

                                TextView snippet = new TextView(mContext);
                                snippet.setTextColor(Color.GRAY);
                                snippet.setText(marker.getSnippet());

                                info.addView(title);
                                info.addView(snippet);

                                return info;
                            }
                        });

                        /*maps.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {

                                String desca = o.getDescricao();
                                String cata = o.getCategoria();
                                String loca = o.getLocalizacao();
                                String nomea = o.getNome();
                                String emaila= o.getEmail();
                                String imagema = o.getImagem();

                                initObjeto.putExtra("descricao",desca);
                                initObjeto.putExtra("categoria",cata);
                                initObjeto.putExtra("localizacao",loca);
                                initObjeto.putExtra("imagemint",R.drawable.general);
                                initObjeto.putExtra("imagem",imagema);
                                initObjeto.putExtra("nome",nomea);
                                initObjeto.putExtra("email",emaila);
                                startActivity(initObjeto);
                            }
                        });*/

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        maps.moveCamera(CameraUpdateFactory.newLatLngZoom(reitoria, 15));
    }

}
