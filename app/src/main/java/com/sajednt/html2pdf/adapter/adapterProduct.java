package com.sajednt.html2pdf.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.sajednt.html2pdf.R;
import com.sajednt.html2pdf.fragment.fragment_factor;
import com.sajednt.html2pdf.item.itemListFactor;

import java.util.ArrayList;
import java.util.List;

public class adapterProduct extends RecyclerView.Adapter<adapterProduct.ContactViewHolder> {

    List<itemListFactor> item;
    public static  Context context;
    Activity activity;
    fragment_factor factor;


    public adapterProduct(List<itemListFactor> contents , Context con , Activity act , fragment_factor ff) {
        this.item = contents;
        this.context =con;
        this.activity =act;
        this.factor = ff;

    }

    @Override
    public int getItemViewType(int position) {

        return position;

    }

    @Override
    public int getItemCount() {
        return item.size();
    }



    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_product_item, parent, false);

        return new adapterProduct.ContactViewHolder(view , activity , factor, item) {
        };
    }


    public static class ContactViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        protected TextView text_name;
        protected TextView text_id , text_price;
        LinearLayout linear;


        public ContactViewHolder(View v , final Activity a, final fragment_factor factor , final List<itemListFactor> item) {
            super(v);

            text_name =  v.findViewById(R.id.textName);
            text_id = v.findViewById(R.id.textId);
            text_price = v.findViewById(R.id.textPrice);
            linear = v.findViewById(R.id.linearlistItem);

            linear.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragment_factor f = factor;
                    itemListFactor il = new itemListFactor();
                    f.addItemFromList(item.get(getAdapterPosition()));
                }
            });
        }

        @Override
        public void onClick(View v) {




        }
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder arg0, int arg1) {
        // TODO Auto-generated method stub
        itemListFactor ie = item.get(arg1);
        arg0.text_name.setText(ie.getDescription());
        arg0.text_id.setText(ie.getId());
        arg0.text_price.setText( "قیمت : "+ ie.getPrice());




    }

    public void updateList(List<itemListFactor> data) {
        item = data;
        notifyDataSetChanged();
    }



}


