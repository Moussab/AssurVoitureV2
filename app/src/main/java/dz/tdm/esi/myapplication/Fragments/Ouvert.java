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
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import dz.tdm.esi.myapplication.Activities.AddDossier;
import dz.tdm.esi.myapplication.Activities.FolderDetail;
import dz.tdm.esi.myapplication.Activities.FolderList;
import dz.tdm.esi.myapplication.Adapters.FolderAdapter;
import dz.tdm.esi.myapplication.DAO.DossierDAO;
import dz.tdm.esi.myapplication.R;
import dz.tdm.esi.myapplication.Util.ClickListener;
import dz.tdm.esi.myapplication.Util.DividerItemDecoration;
import dz.tdm.esi.myapplication.Util.RecyclerTouchListener;
import dz.tdm.esi.myapplication.models.Dossier;

/**
 * Created by Amine on 21/05/2017.
 */

public class Ouvert extends Fragment {


    private List<Dossier> agendaList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FolderAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    FloatingActionButton addVehicule;
    DossierDAO dossierDAO;

    public Ouvert() {
        // Required empty public constructor
    }

    public Ouvert(List<Dossier> agendas) {
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

        View view = inflater.inflate(R.layout.fragment_ouvert, container, false);

        dossierDAO = new DossierDAO(this.getActivity());

        agendaList = dossierDAO.selectionnerOuvert();


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_ouvert);
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
                Long cpt = Long.parseLong(String.valueOf(dossier.getId()));
                intent.putExtra("id_folder", cpt);
                intent.putExtra("btn",false);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        addVehicule = (FloatingActionButton)view.findViewById(R.id.addVehicule);
        addVehicule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), AddDossier.class);
                startActivity(it);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        agendaList = dossierDAO.selectionnerOuvert();
        mAdapter.updateData((ArrayList<Dossier>) agendaList);
        mAdapter.refresh();
    }

}
