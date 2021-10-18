package com.erickmxav.apppets.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.erickmxav.apppets.R;
import com.erickmxav.apppets.model.Pet;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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

        if(pet.getPhoto() != null){
            Uri uri = Uri.parse(pet.getPhoto());
            Glide.with( context ).load( uri ).into( holder.photo );

        }else {
            holder.photo.setImageResource(R.drawable.perfil);
        }
    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    public interface ItemClickListener{
        void onItemClick(Pet pet);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, specie;
        CircleImageView photo;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textAdapterName);
            specie = itemView.findViewById(R.id.textAdapterSpecie);
            photo = itemView.findViewById(R.id.photoPetAdapter);
        }
    }
}