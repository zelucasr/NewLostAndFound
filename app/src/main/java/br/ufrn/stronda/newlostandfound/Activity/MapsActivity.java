package br.ufrn.stronda.newlostandfound.Activity;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap maps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map1 is ready to be used.
        SupportMapFragment mapFragmentA = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragmentA.getMapAsync(this);

    }

    /**
     * Manipulates the map1 once available.
     * This callback is triggered when the map1 is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app. -5.8323,-35.2074909
     */

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
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final ArrayList<AcheiObjeto> objetos = new ArrayList<AcheiObjeto>();

                for( DataSnapshot dsp : dataSnapshot.getChildren() ) {


                    for (DataSnapshot dspc : dsp.child("Objetos").child("Achados").getChildren()) {

                        AcheiObjeto o = new AcheiObjeto();
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
                        maps.addMarker(new MarkerOptions().position(local).title(o.getDescricao()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        objetos.add(o);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<PerdiObjeto> objetosp = new ArrayList<PerdiObjeto>();

                for( DataSnapshot dsp1 : dataSnapshot.getChildren() ){

                    for (DataSnapshot dspc1 : dsp1.child("Objetos").child("Perdidos").getChildren()){

                        PerdiObjeto o = new PerdiObjeto();
                        o.setDescricao(dspc1.child("descricao").getValue().toString());
                        o.setCategoria(dspc1.child("categoria").getValue().toString());
                        o.setLocalizacao(dspc1.child("localizacao").getValue().toString());
                        o.setImagem(dspc1.child("imagem").getValue().toString());
                        o.setNome(dspc1.child("nome").getValue().toString());
                        o.setEmail(dspc1.child("email").getValue().toString());
                        o.setLatitude(new Double(dspc1.child("latitude").getValue().toString()));
                        o.setLongitude(new Double(dspc1.child("longitude").getValue().toString()));


                        Log.d("ID",dspc1.getKey());

                        Log.d("DESCRICAO", o.getDescricao());
                        Log.d("CATEGORIA", o.getCategoria());
                        Log.d("LOCALIZACAO", o.getLocalizacao());

                        LatLng local = new LatLng(o.getLatitude(), o.getLongitude());
                        maps.addMarker(new MarkerOptions().position(local).title(o.getDescricao()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        objetosp.add(o);
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
