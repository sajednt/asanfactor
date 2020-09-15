package com.sajednt.html2pdf.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.sajednt.html2pdf.R;
import com.sajednt.html2pdf.ReportViewer;
import com.sajednt.html2pdf.fragment.fragment_factor;
import com.sajednt.html2pdf.function.Functions;
import com.sajednt.html2pdf.item.itemFactor;
import com.sajednt.html2pdf.item.itemListFactor;

import java.io.File;
import java.util.List;

public class AdapterFactors extends RecyclerView.Adapter<AdapterFactors.ContactViewHolder> {

    List<itemFactor> item;
    public static  Context context;
    Activity activity;


    public AdapterFactors(List<itemFactor> contents , Context con , Activity act) {
        this.item = contents;
        this.context =con;
        this.activity =act;

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
                .inflate(R.layout.list_factor, parent, false);

        return new AdapterFactors.ContactViewHolder(view , context) {
        };
    }


    public static class ContactViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        protected TextView text_title , text_subtitle , text_number , text_date , text_sellername;
        protected TextView text_id , text_price , text_customername , text_count, textShare, textShow, textEdit;
        Functions func;
        Context context;

        public ContactViewHolder(View v , final Context a) {
            super(v);
            context = a;
            func = new Functions();
            text_title =  v.findViewById(R.id.fac_title);
            text_subtitle =  v.findViewById(R.id.fac_subtitle);
            text_number =  v.findViewById(R.id.fac_number);
            text_sellername =  v.findViewById(R.id.fac_seller);
            text_date =  v.findViewById(R.id.fac_date);
            text_id =  v.findViewById(R.id.fac_id);
            text_price =  v.findViewById(R.id.fac_price);
            text_customername =  v.findViewById(R.id.fac_customer);
            text_count =  v.findViewById(R.id.fac_count);
            textShare =  v.findViewById(R.id.btnShare);
            textShow =  v.findViewById(R.id.btnShow);
            textEdit =  v.findViewById(R.id.btnEdit);

            textShare.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                    File copied = new File(Environment.getExternalStorageDirectory(), "/factor/reports/"+text_number.getText().toString()+".pdf");
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(copied));
                    intent.setType("application/pdf");
                    context.startActivity(intent);
                }
            });

            textShow.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    File copied = new File(Environment.getExternalStorageDirectory(), "/factor/reports/"+text_number.getText().toString()+".pdf");
                    Intent i = new Intent(context, ReportViewer.class);
                    i.putExtra("fileName", copied);
                    context.startActivity(i);
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
        itemFactor ie = item.get(arg1);
        arg0.text_title.setText(ie.getTitle());
        arg0.text_subtitle.setText(ie.getSubtitle());
        arg0.text_number.setText(ie.getNumber());
        arg0.text_sellername.setText(ie.getSellerId());
        arg0.text_date.setText(ie.getDate());
        arg0.text_id.setText(ie.getId());
        arg0.text_price.setText(ie.getPrice());
        arg0.text_count.setText(ie.getCount() + " عدد");
        arg0.text_customername.setText(ie.getCustomerId());

    }

    public void updateList(List<itemFactor> data) {
        item = data;
        notifyDataSetChanged();
    }



}


