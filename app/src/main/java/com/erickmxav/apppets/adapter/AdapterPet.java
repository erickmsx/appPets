package com.erickmxav.apppets.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.erickmxav.apppets.R;
import com.erickmxav.apppets.model.Pet;

import java.util.List;

public class AdapterPet extends RecyclerView.Adapter<AdapterPet.MyViewHolder> {

    private List<Pet> pets;
    Context context;

    public AdapterPet(List<Pet> pets, Context context) {
        this.pets = pets;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pet, parent, false);
        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Pet pet = pets.get(position);
        holder.name.setText(pet.getName());
        holder.specie.setText(pet.getSpecie());
    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, specie;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textAdapterName);
            specie = itemView.findViewById(R.id.textAdapterSpecie);
        }

    }
}