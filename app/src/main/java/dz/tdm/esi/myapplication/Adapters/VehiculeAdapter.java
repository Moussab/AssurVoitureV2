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
import dz.tdm.esi.myapplication.models.Vehicule;

/**
 * Created by Amine on 21/05/2017.
 */

public class VehiculeAdapter  extends RecyclerView.Adapter<VehiculeAdapter.MyViewHolder> {

    private List<Vehicule> vehicules;
    private Context context;
    private Vehicule vehicule;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nom, matricule;

        public MyViewHolder(View view) {
            super(view);
            nom = (TextView) view.findViewById(R.id.nomVehicule);
            matricule = (TextView) view.findViewById(R.id.matricule);
        }
    }

    public void setItem(int index, Vehicule item) {
        vehicules.set(index, item);
        this.notifyItemChanged(index);
    }

    public void addItem(Vehicule item) {
        vehicules.add(item);
        this.notifyItemInserted(vehicules.size() - 1);
    }

    public VehiculeAdapter(List<Vehicule> vehicules, Context context) {
        this.vehicules = vehicules;
        this.context = context;
    }


    public void refresh() {
        this.notifyItemInserted(vehicules.size() - 1);
    }

    public void updateData(ArrayList<Vehicule> postList) {
        this.vehicules = postList;
        this.notifyDataSetChanged();
    }


    @Override
    public VehiculeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vhicule, parent, false);

        return new VehiculeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VehiculeAdapter.MyViewHolder holder, int position) {
        Vehicule vehicule = vehicules.get(position);
        holder.nom.setText(vehicule.getNom());
        holder.matricule.setText(vehicule.getMatricule());
    }

    @Override
    public int getItemCount() {
        return vehicules.size();
    }

}