package com.sajednt.html2pdf.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.print.*;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.sajednt.html2pdf.R;
import com.sajednt.html2pdf.ReportViewer;
import com.sajednt.html2pdf.adapter.AdapterFactorItem;
import com.sajednt.html2pdf.adapter.adapterProduct;
import com.sajednt.html2pdf.database.Database;
import com.sajednt.html2pdf.function.Functions;
import com.sajednt.html2pdf.function.PersianNumbersToLettersConverter;
import com.sajednt.html2pdf.item.itemFactor;
import com.sajednt.html2pdf.item.itemListFactor;
import com.sajednt.html2pdf.item.itemSeller;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;


public class fragment_factor extends Fragment {

    ImageView imageLogo;
    Button imageAdd, btnlist;
    EditText editTitle, editSubTitle, editNumber, editSellerName, editSellerPhone,
            editSellerCode, editSellerZip, editSellerAddress, editBuyerName, editBuyerPhone,
            editBuyerCode, editBuyerZip, editBuyerAddress, editTakhfif, editTax,editExtra;
    CardView cardNoItem;
    TextView textTotal, textPay,textPayChar, editDate;
    RecyclerView recyclerItems;
    CheckBox checkTakhfif , checkTax;
    List<itemListFactor> listItem = new ArrayList<>();
    int itemCount = 1;
    private int RESULT_LOAD_IMAGE = 1002;
    String logoPath ="";
    Functions func;
    Database db;
    AdapterFactorItem rv;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    public int finalPrice = 0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.factor, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.showfactor){

            generateReport(false);
        }
        else if(item.getItemId() == R.id.savefactor){
            generateReport(true);

            itemSeller is = new itemSeller();
            is.setName(editBuyerName.getText().toString());
            is.setPhone(editBuyerPhone.getText().toString());
            is.setCode(editBuyerCode.getText().toString());
            is.setZip(editBuyerZip.getText().toString());
            is.setAddress(editBuyerAddress.getText().toString());
            String idCustomer = db.addCustomer(is);

            itemFactor ifa= new itemFactor();
            ifa.setTitle(editTitle.getText().toString());
            ifa.setSubtitle(editSubTitle.getText().toString());
            ifa.setNumber(editNumber.getText().toString());
            ifa.setDate(editDate.getText().toString());
            ifa.setSellerId("1");
            ifa.setCustomerId(idCustomer);
            ifa.setDiscount(editTakhfif.getText().toString());
            ifa.setDiscountPercent(checkTakhfif.isChecked());
            ifa.setTax(editTax.getText().toString());
            ifa.setTaxPercent(checkTax.isChecked());
            ifa.setInfo(editExtra.getText().toString());
            ifa.setPrice(Integer.toString(finalPrice));

            String idFactor = db.addFactor(ifa);

            db.deleteAllFactorItems(idFactor);
            db.addFoctorItems(listItem, idFactor);
            Toast.makeText(getActivity(),   "فاکتور شما به شماره " + editNumber.getText().toString() + " ذخیره شد" , Toast.LENGTH_SHORT).show();


        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        logoPath = getActivity().getFilesDir() + "/image/";
        func = new Functions();

        db = new Database(getActivity());
        View view = inflater.inflate(R.layout.fragment_factor, container, false);

        imageAdd = view.findViewById(R.id.image_add);
        recyclerItems = view.findViewById(R.id.recyclerItems);
        cardNoItem = view.findViewById(R.id.card_noitem);
        textTotal = view.findViewById(R.id.text_total);
        textPay = view.findViewById(R.id.text_pay);
        textPayChar = view.findViewById(R.id.text_pay2);
        editTakhfif = view.findViewById(R.id.edit_takhfif);
        editTax = view.findViewById(R.id.edit_tax);
        checkTakhfif = view.findViewById(R.id.check_takhfif);
        checkTax = view.findViewById(R.id.check_tax);

        editSellerAddress = view.findViewById(R.id.edit_seller_address);
        editBuyerName = view.findViewById(R.id.edit_buyer_name);
        editBuyerPhone = view.findViewById(R.id.edit_buyer_phone);
        editBuyerCode = view.findViewById(R.id.edit_buyer_code);
        editBuyerZip = view.findViewById(R.id.edit_buyer_zip);
        editBuyerAddress = view.findViewById(R.id.edit_buyer_address);

        editTitle = view.findViewById(R.id.edit_title);
        editSubTitle = view.findViewById(R.id.edit_subtitle);
        editNumber = view.findViewById(R.id.edit_number);
        editDate = view.findViewById(R.id.edit_date);
        editSellerName = view.findViewById(R.id.edit_seller_name);
        editSellerPhone = view.findViewById(R.id.edit_seller_phone);
        editSellerCode = view.findViewById(R.id.edit_seller_code);
        editSellerZip = view.findViewById(R.id.edit_seller_zip);
        editExtra = view.findViewById(R.id.edit_extra);
        imageLogo = view.findViewById(R.id.image_logo);
        btnlist = view.findViewById(R.id.btnItemList);



        btnlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final adapterProduct apv = new adapterProduct( db.getAllProduct() , getContext(), getActivity(), fragment_factor.this);

                 builder = new AlertDialog.Builder(getActivity());
                // set the custom layout
                final View customLayout = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
                builder.setView(customLayout);
                 dialog = builder.create();
                dialog.show();

                RecyclerView recyclerP = customLayout.findViewById(R.id.recyclerProduct);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setReverseLayout(true);
                recyclerP.setLayoutManager(layoutManager);


                recyclerP.setAdapter(apv);
            }
        });


        editTakhfif.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //if(charSequence.length()>0)
                settextTotal(Double.parseDouble(textTotal.getText().toString().replaceAll(",","")));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editTax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                settextTotal(Double.parseDouble(textTotal.getText().toString().replaceAll(",","")));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editNumber.setText(Integer.toString(db.getLastNumber()));


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        recyclerItems.setLayoutManager(layoutManager);
        rv = new AdapterFactorItem( listItem , getContext(), getActivity() , this);

        recyclerItems.setAdapter(rv);

        PersianDate pdate = new PersianDate();
        PersianDateFormat pdformater1 = new PersianDateFormat("Y/m/d");
        editDate.setText(pdformater1.format(pdate));

        setSeller();

        File logo = new File (logoPath+"logo");

        if(logo.exists()){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            imageLogo.setImageBitmap(BitmapFactory.decodeFile(logo.getPath()  ,  options));

        }


        imageLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });


        checkTakhfif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                settextTotal(Double.parseDouble(textTotal.getText().toString().replaceAll(",","")));

            }
        });

        checkTax.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                settextTotal(Double.parseDouble(textTotal.getText().toString().replaceAll(",","")));

            }
        });

        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(listItem.size() < 10){


                itemCount = listItem.size()+1;
                itemListFactor it = new itemListFactor();
                it.setId(Integer.toString(itemCount));
                it.setCount("1");
                listItem.add(it);
                rv.notifyItemInserted(listItem.size()-1);

                itemCount++;

                if(cardNoItem.getVisibility() == View.VISIBLE){

                    YoYo.with(Techniques.FlipOutX)
                            .duration(700)
                            .playOn(cardNoItem);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cardNoItem.setVisibility(View.GONE);
                        }
                    }, 700);

                }
                }
                else{
                    Toast.makeText(getActivity(), "حداکثر تعداد آیتم فاکتور 10 عدد می باشد", Toast.LENGTH_LONG).show();
                }
            }
        });

        final Typeface font = Typeface.createFromAsset(getContext().getAssets(), "iransans.ttf");

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersianDatePickerDialog picker = new PersianDatePickerDialog(getActivity())
                        .setPositiveButtonString("انتخاب")
                        .setNegativeButton("بیخیال")
                        .setTodayButton("امروز")
                        .setTodayButtonVisible(true)
                        .setActionTextColor(Color.GRAY)
                        .setTypeFace(font)
                        .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                        .setShowInBottomSheet(true)
                        .setListener(new Listener() {
                            @Override
                            public void onDateSelected(PersianCalendar persianCalendar) {

                                editDate.setText(persianCalendar.getPersianYear() + "/" + persianCalendar.getPersianMonth() + "/" + persianCalendar.getPersianDay());
                            }

                            @Override
                            public void onDismissed() {

                            }
                        });

                picker.show();
            }
        });

        getActivity().getWindow().getDecorView().clearFocus();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            func.copyFile(picturePath , "logo" , logoPath);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            imageLogo.setImageBitmap(BitmapFactory.decodeFile(picturePath, options));

        }
    }

    public void generateReport(final Boolean save){

        String tContents = "";

        try {
            InputStream stream = getActivity().getAssets().open("styles.css");

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            tContents = new String(buffer);
        } catch (IOException e) {
            // Handle exceptions here
        }




        PdfConverter converter = PdfConverter.getInstance();

        String htmlString = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <meta charset=\"utf-8\" />\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "\t<style>\n" +
                tContents+
                "</style>\n" +
                "\t\n" +
                "  </head>\n" +
                "  <body>\n" +
                "\n" +
                "\n" +
                "    <div class='main'>\n" +
                "      <div class='header'>\n" +
                "        <div class='container_number'>\n" +
                "          <span dir='rtl' class='number'>@number</span>\n" +
                "          <span dir='rtl'>شماره :</span>\n" +
                "          <br />\n" +
                "          <span dir='rtl' class='date'>@date</span>\n" +
                "          <span dir='rtl'>تاریخ :</span>\n" +
                "        </div>\n" +
                "        <div class='container_title'>\n" +
                "          <p class='title'>@title</p>\n" +
                "          <p class='subtitle'>@subtitle</p>\n" +
                "        </div>\n" +
                "        <div class='container_image'>\n" +
                "          <img class='logo' src='@logopath' />\n" +
                "        </div>\n" +
                "      </div>\n" +
                "\n" +
                "      <div class='container_seller'>\n" +
                "        <div class='seller_info'>\n" +
                "          <span id='seller_name'>@sellername</span>\n" +
                "          <span id='seller_phone'>@sellerphone</span>\n" +
                "          <span id='seller_code'>@sellercode<br /></span>\n" +
                "          <span id='seller_address'>@selleraddress</span>\n" +
                "          <span id='seller_zip'>@sellerzip</span>\n" +
                "        </div>\n" +
                "        <div class='seller_title'>\n" +
                "          فروشنده\n" +
                "        </div>\n" +
                "      </div>\n" +
                "\n" +
                "      <div class='container_buyer'>\n" +
                "        <div class='buyer_info'>\n" +
                "          <span id='seller_name'>@customername</span>\n" +
                "          <span id='seller_phone'>@customerphone</span>\n" +
                "          <span id='seller_code'>@customercode <br /></span>\n" +
                "          <span id='seller_address'>@customeraddress</span>\n" +
                "          <span id='seller_zip'>@customerzip</span>\n" +
                "        </div>\n" +
                "        <div class='buyer_title'>\n" +
                "          خریدار\n" +
                "        </div>\n" +
                "      </div>\n" +
                "\n" +
                "      <table cellpadding='1' cellspacing='0' class='items'>\n" +
                "        <thead>\n" +
                "          <tr>\n" +
                "            <th class='item-number'>ردیف</th>\n" +
                "            <th class='item-description'>شرح</th>\n" +
                "            <th class='item-count'>تعداد</th>\n" +
                "            <th class='item-price'>قیمت واحد</th>\n" +
                "            <th class='item-total'>قیمت کل</th>\n" +
                "            <th class='item-info'>ملاحظات</th>\n" +
                "          </tr>\n" +
                "        </thead>\n" +
                "        <tbody>\n";


        for (itemListFactor ilf: listItem) {
            String tr = "";
            String formatable="";

            tr = "            <tr>\n" +
                    "            <th class='item-number'>%s</th>\n" +
                    "            <th class='item-description'>%s</th>\n" +
                    "            <th class='item-count'>%s</th>\n" +
                    "            <th class='item-price'>%s</th>\n" +
                    "            <th class='item-total'>%s</th>\n" +
                    "            <th class='item-info'>%s</th>\n" +
                    "          </tr>\n";

            formatable = String.format(tr , ilf.getId() , ilf.getDescription(), ilf.getCount(), ilf.getPrice() ,
                                        ilf.getTotal(), ilf.getInfo());

            htmlString += formatable;

        }


                htmlString +=                "        </tbody>\n" +
                "      </table>\n" +
                "      <br />\n" +
                "\n" +
                "      <table class='items'>\n" +
                "        <thead>\n" +
                "          <tr>\n" +
                "            <th colspan='2' style='text-align: left;' class='item-number'>\n" +
                "              جمع فاکتور\n" +
                "            </th>\n" +
                "            <th style='max-width:80px; text-align: right;' class='item-total'>\n" +
                "              @total\n" +
                "            </th>\n" +
                "          </tr>\n" +
                "        </thead>\n" +
                "        <tbody>\n" +
                "          <tr>\n" +
                "            <th\n" +
                "              rowspan='3'\n" +
                "              style='min-width: 100px; max-width:100px;text-align: right;'\n" +
                "            >\n" +
                "              @info\n" +
                "            </th>\n" +
                "            <th style='text-align: left;'>تخفیف</th>\n" +
                "            <th style='text-align: right;'>@discount</th>\n" +
                "          </tr>\n" +
                "          <tr>\n" +
                "            <th style='text-align: left;'>مالیات</th>\n" +
                "            <th style='text-align: right;'>@tax</th>\n" +
                "          </tr>\n" +
                "\n" +
                "          <tr>\n" +
                "            <th style='text-align: left;'>مبلغ قابل پرداخت</th>\n" +
                "            <th style='text-align: right;'>@pay</th>\n" +
                "          </tr>\n" +
                "        </tbody>\n" +
                "      </table>\n" +
                "\n" +
                "      <div style='margin-top:80px;' class='header'>\n" +
                "        <div style='flex: 1; text-align: center;'>امضاء فروشنده</div>\n" +
                "        <div style='flex: 1; text-align: center;'>امضاء خریدار</div>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>\n";



        String result = htmlString.replace("@title", editTitle.getText().toString());
        result = result.replace("@subtitle", editSubTitle.getText().toString());


        File logo = new File (logoPath+"logo");

        if(logo.exists())
            result = result.replace("@logopath", logoPath+"logo");
        else
            result = result.replace("@logopath", "///android_asset/factorlogo.png");

        result = result.replace("@number", editNumber.getText().toString());
        result = result.replace("@date", editDate.getText().toString());

        if(!isEmpty(editSellerName))
            result = result.replace("@sellername"," <b> نام :</b> " + editSellerName.getText().toString());
        else
            result = result.replace("@sellername", "");

        if(!isEmpty(editSellerPhone))
            result = result.replace("@sellerphone"," <b> تلفن :</b> " + editSellerPhone.getText().toString());
        else
            result = result.replace("@sellerphone", "");

        if(!isEmpty(editSellerCode))
            result = result.replace("@sellercode"," <b> کد ملی :</b> " + editSellerCode.getText().toString());
        else
            result = result.replace("@sellercode", "");

        if(!isEmpty(editSellerAddress))
            result = result.replace("@selleraddress"," <b> آدرس :</b> " + editSellerAddress.getText().toString());
        else
            result = result.replace("@selleraddress", "");


        if(!isEmpty(editSellerZip))
            result = result.replace("@sellerzip"," <b> کد پستی :</b> " + editSellerZip.getText().toString());
        else
            result = result.replace("@sellerzip", "");


        //---------------------------------------------------------------------------------------------


        if(!isEmpty(editBuyerName))
            result = result.replace("@customername"," <b> نام :</b> " + editBuyerName.getText().toString());
        else
            result = result.replace("@customername", "");

        if(!isEmpty(editBuyerPhone))
            result = result.replace("@customerphone"," <b> تلفن :</b> " + editBuyerPhone.getText().toString());
        else
            result = result.replace("@customerphone", "");

        if(!isEmpty(editBuyerCode))
            result = result.replace("@customercode"," <b> کد ملی :</b> " + editBuyerCode.getText().toString());
        else
            result = result.replace("@customercode", "");

        if(!isEmpty(editBuyerAddress))
            result = result.replace("@customeraddress"," <b> آدرس :</b> " + editBuyerAddress.getText().toString());
        else
            result = result.replace("@customeraddress", "");


        if(!isEmpty(editBuyerZip))
            result = result.replace("@customerzip"," <b> کد پستی :</b> " + editBuyerZip.getText().toString());
        else
            result = result.replace("@customerzip", "");


        result = result.replace("@total", textTotal.getText().toString());

        if(checkTakhfif.isChecked()){
            result = result.replace("@discount", editTakhfif.getText().toString() + " %");
        }
        else{
            result = result.replace("@discount", editTakhfif.getText().toString());
        }

        if(checkTax.isChecked()){
            result = result.replace("@tax", editTax.getText().toString() + " %");
        }
        else{
            result = result.replace("@tax", editTax.getText().toString());
        }
        result = result.replace("@pay", textPay.getText().toString()+"<br>"+textPayChar.getText().toString());
        result = result.replace("@info", editExtra.getText().toString());

        final File file = new File(getActivity().getFilesDir()+"/report", editNumber.getText().toString() + ".pdf");


        addSeller();

        converter.convert(getContext(), result , file);

        converter.setCustomObjectListener(new PdfConverter.MyCustomObjectListener() {
            @Override
            public void onObjectReady(String bitmap) {
                func.copyFile(file.getPath(), file.getName() , Environment.getExternalStorageDirectory().toString() + "/factor/reports/");

                if(!save) {
                    Intent i = new Intent(getActivity(), ReportViewer.class);
                    i.putExtra("fileName", file);
                    startActivity(i);
                }
            }
        });






    }

    public void addItemFromList(itemListFactor ilf) {

        if (listItem.size() < 10) {

            dialog.dismiss();
            itemCount = listItem.size() + 1;
            itemListFactor it = new itemListFactor();
            it.setId(Integer.toString(itemCount));
            it.setCount("1");
            it.setDescription(ilf.getDescription());
            it.setPrice(ilf.getPrice());
            it.setInfo(ilf.getInfo());
            listItem.add(it);
            rv.notifyItemInserted(listItem.size() - 1);

            itemCount++;

            if (cardNoItem.getVisibility() == View.VISIBLE) {

                YoYo.with(Techniques.FlipOutX)
                        .duration(700)
                        .playOn(cardNoItem);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cardNoItem.setVisibility(View.GONE);
                    }
                }, 700);

            }
        } else {
            Toast.makeText(getActivity(), "حداکثر تعداد آیتم فاکتور 10 عدد می باشد", Toast.LENGTH_LONG).show();
        }

    }

    public  void settextTotal(Double x){


        if(listItem.size() < 1){
            cardNoItem.setVisibility(View.VISIBLE);

            YoYo.with(Techniques.FlipInX)
                    .duration(700)
                    .playOn(cardNoItem);
        }


        Double digi = x;
        Double takhfif;
        Double tax;
        PersianNumbersToLettersConverter pntlc = new PersianNumbersToLettersConverter();
        DecimalFormat df = new DecimalFormat("#");
        textTotal.setText(NumberFormat.getInstance().format(x));

        if(editTakhfif.getText().toString().length() !=0 ){
            takhfif = Double.parseDouble(editTakhfif.getText().toString());
        }
        else{
            takhfif = 0.;
        }

        if(editTax.getText().toString().length() !=0 ){
            tax = Double.parseDouble(editTax.getText().toString());
        }
        else{
            tax = 0.;
        }

        if(checkTakhfif.isChecked()){


            digi = digi - ((digi / 100) * takhfif);

        }
        else{

            digi = digi - takhfif;


        }

        if(checkTax.isChecked()){


            digi = digi + ((digi / 100) * tax);

        }
        else{

            digi = digi + tax;


        }
        String ss = df.format(digi);
        finalPrice = (int) Math.round(digi);
        String resultString = pntlc.getParsedString(ss) + " ریال ";
        textPay.setText(NumberFormat.getInstance().format(Double.parseDouble(ss)));
        textPayChar.setText( resultString);
    }

    public void addSeller(){

        itemSeller is = new itemSeller();
        is.setAddress(editSellerAddress.getText().toString());
        is.setCode(editSellerCode.getText().toString());
        is.setName(editSellerName.getText().toString());
        is.setZip(editSellerZip.getText().toString());
        is.setPhone(editSellerPhone.getText().toString());
        is.setStoreName(editSubTitle.getText().toString());

        db.addSeller(is);

    }

    public void setSeller(){

        if(db.sellerExist("")){

            itemSeller is = new itemSeller();
            is = db.getSeller();
            if (is != null) {

                editSellerName.setText(is.getName());
                editSellerPhone.setText(is.getPhone());
                editSellerCode.setText(is.getCode());
                editSellerZip.setText(is.getZip());
                editSellerAddress.setText(is.getAddress());
                editSellerAddress.setText(is.getAddress());
                editSubTitle.setText(is.getStoreName());

            }
        }

    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
