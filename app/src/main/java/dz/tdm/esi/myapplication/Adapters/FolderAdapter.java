package dz.tdm.esi.myapplication.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dz.tdm.esi.myapplication.R;
import dz.tdm.esi.myapplication.models.Dossier;

/**
 * Created by Amine on 21/05/2017.
 */

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.MyViewHolder> {

    private List<Dossier> dossiers;
    private Context context;
    private Dossier dossier;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nom, matricule;

        public MyViewHolder(View view) {
            super(view);
            nom = (TextView) view.findViewById(R.id.nomVehicule);
            matricule = (TextView) view.findViewById(R.id.matricule);
        }
    }

    public void setItem(int index, Dossier item) {
        dossiers.set(index, item);
        this.notifyItemChanged(index);
    }

    public void addItem(Dossier item) {
        dossiers.add(item);
        this.notifyItemInserted(dossiers.size() - 1);
    }

    public FolderAdapter(List<Dossier> dossiers, Context context) {
        this.dossiers = dossiers;
        this.context = context;
    }


    public void refresh() {
        this.notifyItemInserted(dossiers.size() - 1);
    }

    public void updateData(ArrayList<Dossier> postList) {
        this.dossiers = postList;
        this.notifyDataSetChanged();
    }


    @Override
    public FolderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vhicule, parent, false);

        return new FolderAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FolderAdapter.MyViewHolder holder, int position) {
        Dossier dossier = dossiers.get(position);
        holder.nom.setText("Dossier N° = "+dossier.getId());
        holder.matricule.setText(dossier.getDate());
    }

    @Override
    public int getItemCount() {
        return dossiers.size();
    }

}