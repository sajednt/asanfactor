package com.sajednt.html2pdf.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.sajednt.html2pdf.R;
import com.sajednt.html2pdf.database.Database;
import com.sajednt.html2pdf.fragment.fragment_factor;
import com.sajednt.html2pdf.item.itemFactor;
import com.sajednt.html2pdf.item.itemListFactor;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class AdapterFactorItem extends RecyclerView.Adapter<AdapterFactorItem.ContactViewHolder> {

    List<itemListFactor> listItem;
    public static  Context context;
    Activity activity;
    fragment_factor factor;
    Database db;
    public AdapterFactorItem(List<itemListFactor> contents , Context con , Activity act , fragment_factor ff) {
        this.listItem = contents;
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
        return listItem.size();
    }



    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_item, parent, false);

        return new AdapterFactorItem.ContactViewHolder(view) {
        };
    }


    public class ContactViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        protected TextView text_id,edit_total;
        protected EditText edit_name, edit_count, edit_price , edit_info;
        protected ImageView image_remove;
        protected CardView card_header;
        DecimalFormat df = new DecimalFormat("###.#");
        Button save;

        public ContactViewHolder(View v ) {
            super(v);
            text_id =  (TextView) v.findViewById(R.id.textViewId);
            edit_name = v.findViewById(R.id.edit_name);
            edit_count = v.findViewById(R.id.edit_count);
            edit_price = v.findViewById(R.id.edit_price);
            edit_total = v.findViewById(R.id.edit_total);
            edit_info = v.findViewById(R.id.edit_info);
            image_remove = v.findViewById(R.id.image_remove);
            card_header = v.findViewById(R.id.card_header);
            save =  v.findViewById(R.id.btn_addToList);

            db = new Database(context);

            save.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListFactor ilf = new itemListFactor();
                    ilf.setDescription(edit_name.getText().toString());
                    ilf.setPrice(edit_price.getText().toString());
                    ilf.setInfo(edit_info.getText().toString());

                    if(isEmpty(edit_name)){
                        edit_name.setError("لطفا نام آیتم را وارد کنید");
                    }
                    else if(isEmpty(edit_price)){

                        edit_price.setError("لطفا قیمت آیتم را وارد کنید");
                    }
                    else if(db.addProduct(ilf)){
                        Toast.makeText(activity, "آیتم در لیست شما ثبت شد", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(activity, "این آیتم در لیست شما وجود دارد", Toast.LENGTH_LONG).show();
                    }
                }
            });

            image_remove.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    removeSingleItem(getAdapterPosition());
                    totalCalculate();
                }
            });

            edit_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    listItem.get(getAdapterPosition()).setDescription(String.valueOf(charSequence));
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            edit_count.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    listItem.get(getAdapterPosition()).setCount(String.valueOf(charSequence));
                    try {

                        Double d = Double.parseDouble(edit_price.getText().toString().replaceAll(",","")) * Double.parseDouble(edit_count.getText().toString());

                        edit_total.setText(NumberFormat.getInstance().format(d));

                    }
                    catch (Exception e){
                        edit_total.setText("0");
                    }
                    totalCalculate();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            edit_price.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    listItem.get(getAdapterPosition()).setPrice(String.valueOf(charSequence));

                    try {
                        Double d = Double.parseDouble(edit_price.getText().toString().replaceAll(",","")) * Double.parseDouble(edit_count.getText().toString());

                        edit_total.setText(NumberFormat.getInstance().format(d));
                    }
                    catch (Exception e){
                        edit_total.setText("0");
                    }
                    totalCalculate();
                }

                @Override
                public void afterTextChanged(Editable editable) {


                    try
                    {
                        edit_price.removeTextChangedListener(this);
                        String value = edit_price.getText().toString();


                        if (value != null && !value.equals(""))
                        {

                            if(value.startsWith(".")){
                                edit_price.setText("0.");
                            }
                            if(value.startsWith("0") && !value.startsWith("0.")){
                                edit_price.setText("");

                            }


                            String str = edit_price.getText().toString().replaceAll(",", "");
                            if (!value.equals(""))
                                edit_price.setText(getDecimalFormattedString(str));
                            edit_price.setSelection(edit_price.getText().toString().length());
                        }
                        edit_price.addTextChangedListener(this);
                        return;
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                        edit_price.addTextChangedListener(this);
                    }
                }
            });

            edit_total.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    listItem.get(getAdapterPosition()).setTotal(String.valueOf(charSequence));
                }

                @Override
                public void afterTextChanged(Editable editable) {





                }
            });

            edit_info.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    listItem.get(getAdapterPosition()).setInfo(String.valueOf(charSequence));
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


        }

        public void removeSingleItem(int position){

            listItem.remove(position);
            renewIds();
            notifyItemRemoved(getAdapterPosition());


        }

        public String getDecimalFormattedString(String value) {
            StringTokenizer lst = new StringTokenizer(value, ".");
            String str1 = value;
            String str2 = "";
            if (lst.countTokens() > 1)
            {
                str1 = lst.nextToken();
                str2 = lst.nextToken();
            }
            String str3 = "";
            int i = 0;
            int j = -1 + str1.length();
            if (str1.charAt( -1 + str1.length()) == '.')
            {
                j--;
                str3 = ".";
            }
            for (int k = j;; k--)
            {
                if (k < 0)
                {
                    if (str2.length() > 0)
                        str3 = str3 + "." + str2;
                    return str3;
                }
                if (i == 3)
                {
                    str3 = "," + str3;
                    i = 0;
                }
                str3 = str1.charAt(k) + str3;
                i++;
            }

        }

        public  String trimCommaOfString(String string) {
//        String returnString;
            if(string.contains(",")){
                return string.replace(",","");}
            else {
                return string;
            }

        }

        private boolean isEmpty(EditText etText) {
            return etText.getText().toString().trim().length() == 0;
        }
        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder arg0, int arg1) {
        // TODO Auto-generated method stub



        itemListFactor ie = listItem.get(arg1);
        arg0.text_id.setText(ie.getId());
        arg0.edit_name.setText(listItem.get(arg1).getDescription());
        arg0.edit_count.setText(listItem.get(arg1).getCount());
        arg0.edit_price.setText(listItem.get(arg1).getPrice());
        arg0.edit_total.setText(listItem.get(arg1).getTotal());
        arg0.edit_info.setText(listItem.get(arg1).getInfo());


    }


    public void updateList(List<itemListFactor> data) {
        listItem = data;
        notifyDataSetChanged();
    }

    public void totalCalculate(){



                Double total = (double) 0;

                for (itemListFactor it: listItem) {

                    try{
                        Double x = Double.parseDouble(it.getTotal().replaceAll(",",""));
                        total += x;
                    }
                    catch (Exception e){

                    }


                }
                fragment_factor f = factor;
                f.settextTotal(total);



    }

    public void renewIds(){

        int s=1;
        for (itemListFactor it: listItem) {

            it.setId(Integer.toString(s));
            s++;

        }

    }

}


