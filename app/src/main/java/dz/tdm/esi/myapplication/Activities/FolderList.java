package dz.tdm.esi.myapplication.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import dz.tdm.esi.myapplication.Adapters.FolderAdapter;
import dz.tdm.esi.myapplication.Adapters.VehiculeAdapter;
import dz.tdm.esi.myapplication.Adapters.ViewPagerAdapter;
import dz.tdm.esi.myapplication.Fragments.Ouvert;
import dz.tdm.esi.myapplication.Fragments.Traitement;
import dz.tdm.esi.myapplication.R;
import dz.tdm.esi.myapplication.Util.ClickListener;
import dz.tdm.esi.myapplication.Util.RecyclerTouchListener;
import dz.tdm.esi.myapplication.models.Dossier;
import dz.tdm.esi.myapplication.models.User;

public class FolderList extends AppCompatActivity {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicule_list);
        getSupportActionBar().setTitle("Liste des Dossiers");

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Ouvert(), "Ouvert");
        adapter.addFrag(new Traitement(), "En Traitement");
        viewPager.setAdapter(adapter);
    }






    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
