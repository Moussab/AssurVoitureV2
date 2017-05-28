package dz.tdm.esi.myapplication.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dz.tdm.esi.myapplication.Activities.FolderDetail;
import dz.tdm.esi.myapplication.Adapters.FolderAdapter;
import dz.tdm.esi.myapplication.DAO.DossierDAO;
import dz.tdm.esi.myapplication.R;
import dz.tdm.esi.myapplication.Util.ClickListener;
import dz.tdm.esi.myapplication.Util.DividerItemDecoration;
import dz.tdm.esi.myapplication.Util.FireBaseDB;
import dz.tdm.esi.myapplication.Util.RecyclerTouchListener;
import dz.tdm.esi.myapplication.models.Dossier;
import dz.tdm.esi.myapplication.models.EtatDossier;

/**
 * Created by Amine on 21/05/2017.
 */

public class Traitement extends Fragment {


    private List<Dossier> agendaList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FolderAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DossierDAO dossierDAO;

    public Traitement() {
        // Required empty public constructor
    }

    public Traitement(List<Dossier> agendas) {
        // Required empty public constructor
        agendaList = agendas;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_traitement, container, false);

        dossierDAO = new DossierDAO(this.getActivity());

        agendaList = dossierDAO.selectionnerTraitement();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_traitement);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));


        mAdapter = new FolderAdapter(agendaList,getContext());

        recyclerView.setAdapter(mAdapter);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Dossier dossier = agendaList.get(position);
                Intent intent = new Intent(getContext(), FolderDetail.class);
                Gson gson = new Gson();
                String jsonInString = gson.toJson(dossier);
                intent.putExtra("folder", jsonInString);
                intent.putExtra("btn",true);
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        getTraitementFolders();


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getTraitementFolders();
    }

    private void getTraitementFolders(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseReference myRef = FireBaseDB.getMyRef().child("AssurVoiture").child(currentUser.getUid()).child("folders");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.


                agendaList.clear();
                long cpt = dataSnapshot.getChildrenCount();
                for (int i = 1; i<= cpt; i++){
                    DataSnapshot dataSnapshot1 = dataSnapshot.child(String.valueOf(i));
                    Dossier dossier = dataSnapshot1.getValue(Dossier.class);
                    if (dossier.getEtat().toString().compareTo("traitement")==0)
                        agendaList.add(dossier);
                }

                mAdapter.updateData((ArrayList<Dossier>) agendaList);
                mAdapter.refresh();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }

}
