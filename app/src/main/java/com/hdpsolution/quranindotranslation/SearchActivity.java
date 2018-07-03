package com.hdpsolution.quranindotranslation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hdpsolution.quranindotranslation.Adapter.ListMainAdapter;
import com.hdpsolution.quranindotranslation.Adapter.ListSearchAdapter;
import com.hdpsolution.quranindotranslation.Database.DatabaseHelper;
import com.hdpsolution.quranindotranslation.Database.DatabaseQuran;
import com.hdpsolution.quranindotranslation.Database.DatabaseTRANS;
import com.hdpsolution.quranindotranslation.Entity.Sura_Quran;
import com.hdpsolution.quranindotranslation.Entity.Vers_Quran;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private String keySearch;

    private RecyclerView rcv_search;
    private DatabaseHelper dbh;
    private DatabaseQuran db;
    private DatabaseTRANS dbt;

    private ListSearchAdapter adapterS;
    private ArrayList<Sura_Quran> lstSura;
    private int language;
    private String sugg[];
    private ArrayList<Sura_Quran> lstSearching;
    private ArrayList<Sura_Quran> lstSearching_ver;
    private ArrayList<Vers_Quran> lstVerseTran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.s_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        keySearch = intent.getStringExtra(QuranRules.SEARCHKEY);


        db = new DatabaseQuran(SearchActivity.this);
        dbh = new DatabaseHelper(SearchActivity.this);

        getLanguage();
        getSupportActionBar().setTitle(getResources().getString(R.string.results) + keySearch + " (" + lstSearching.size() + ")");


        clickItemRcv();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent returnIntent = new Intent();
                returnIntent.putExtra(QuranRules.MAINTODETAIL, language);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(QuranRules.MAINTODETAIL, language);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
        super.onBackPressed();
    }

    public void getLanguage() {
        if (dbh.getLanguage().size() > 0) {
            language = dbh.getLanguage().get(0).getFlag();
        } else {
            language = 1;
        }
        lstSura = new ArrayList<>();
        lstSura = db.getSuraQuran();

        handleSearch(language);


        rcv_search = findViewById(R.id.rcv_search);
        adapterS = new ListSearchAdapter(SearchActivity.this, lstSearching, language);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_search.setLayoutManager(linearLayoutManager);
        rcv_search.setAdapter(adapterS);
        adapterS.notifyDataSetChanged();
    }


    private void handleSearch(int language) {
        sugg = new String[lstSura.size()];
        switch (language) {
            case 1:
                for (int i = 0; i < lstSura.size(); i++) {
                    sugg[i] = lstSura.get(i).getName_ind();
                }
                dbt = new DatabaseTRANS(SearchActivity.this, QuranRules.DATABASE_INDO);
                break;
            case 2:
                for (int i = 0; i < lstSura.size(); i++) {
                    sugg[i] = lstSura.get(i).getName_english();
                }
                dbt = new DatabaseTRANS(SearchActivity.this, QuranRules.DATABASE_ENG);
                break;
            case 3:
                for (int i = 0; i < lstSura.size(); i++) {
                    sugg[i] = lstSura.get(i).getName_tur();
                }
                dbt = new DatabaseTRANS(SearchActivity.this, QuranRules.DATABASE_TUR);
                break;
            case 4:
                for (int i = 0; i < lstSura.size(); i++) {
                    sugg[i] = lstSura.get(i).getName_fra();
                }
                dbt = new DatabaseTRANS(SearchActivity.this, QuranRules.DATABASE_FRE);
                break;
            case 5:
                for (int i = 0; i < lstSura.size(); i++) {
                    sugg[i] = lstSura.get(i).getName_ger();
                }
                dbt = new DatabaseTRANS(SearchActivity.this, QuranRules.DATABASE_GER);
                break;
            case 6:
                for (int i = 0; i < lstSura.size(); i++) {
                    sugg[i] = lstSura.get(i).getName_ita();
                }
                dbt = new DatabaseTRANS(SearchActivity.this, QuranRules.DATABASE_ITA);
                break;
            case 7:
                for (int i = 0; i < lstSura.size(); i++) {
                    sugg[i] = lstSura.get(i).getName_rus();
                }
                dbt = new DatabaseTRANS(SearchActivity.this, QuranRules.DATABASE_RUS);
                break;
            case 8:
                for (int i = 0; i < lstSura.size(); i++) {
                    sugg[i] = lstSura.get(i).getName_som();
                }
                dbt = new DatabaseTRANS(SearchActivity.this, QuranRules.DATABASE_SOM);
                break;
            case 9:
                for (int i = 0; i < lstSura.size(); i++) {
                    sugg[i] = lstSura.get(i).getName_spa();
                }
                dbt = new DatabaseTRANS(SearchActivity.this, QuranRules.DATABASE_SPA);
                break;
            default:
                for (int i = 0; i < lstSura.size(); i++) {
                    sugg[i] = lstSura.get(i).getName_ind();
                }
                dbt = new DatabaseTRANS(SearchActivity.this, QuranRules.DATABASE_INDO);
                break;
        }

        lstVerseTran = dbt.getAllVerseTrans();
        lstSearching = new ArrayList<>();
        for (int i = 0; i < sugg.length; i++) {
            if (sugg[i].toString().toUpperCase().indexOf(keySearch.toUpperCase()) >= 0) {
                lstSearching.add(lstSura.get(i));
            }
        }


        //Search theo câu
        lstSearching_ver = new ArrayList<>();
        for (int i = 0; i < lstVerseTran.size(); i++) {
            if (lstVerseTran.get(i).getText().toString().toUpperCase().indexOf(keySearch.toUpperCase()) >= 0) {
                int x = 0;
                for (int k = 0; k < lstSearching_ver.size(); k++) {
                    if (lstSearching_ver.get(k).getSuraid() == lstSura.get(lstVerseTran.get(i).getSuraid() - 1).getSuraid()) {
                        x = 1;
                    }
                }
                if (x == 0) {
                    lstSearching_ver.add(lstSura.get(lstVerseTran.get(i).getSuraid() - 1));
                }

            }
        }
        Log.e("Tong ver", lstSearching_ver.size() + "");

        //Lọc phần tử trùng, ưu tiên theo tiêu đề
        for (int i = 0; i < lstSearching.size(); i++) {
            for (int j = 0; j < lstSearching_ver.size(); j++) {
                if (lstSearching.get(i).getSuraid() == lstSearching_ver.get(j).getSuraid()) {
                    lstSearching_ver.remove(j);
                }
            }
        }


        Log.e("Tong ver loc", lstSearching_ver.size() + "");
        //Ghép lại
        for (int i = 0; i < lstSearching_ver.size(); i++) {
            lstSearching.add(lstSearching_ver.get(i));
        }
        Log.e("Tong ghep", lstSearching.size() + "");
    }

    private void clickItemRcv() {
        adapterS.setOnItemClickListener(new ListSearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position, int id) {
                Intent intent = new Intent(SearchActivity.this, DetailsActivity.class);
                intent.putExtra(QuranRules.IDKEY, id);

                switch (language) {
                    case 1:
                        intent.putExtra(QuranRules.NAMEKEY, lstSearching.get(position).getName_ind());
                        break;
                    case 2:
                        intent.putExtra(QuranRules.NAMEKEY, lstSearching.get(position).getName_english());
                        break;
                    case 3:
                        intent.putExtra(QuranRules.NAMEKEY, lstSearching.get(position).getName_tur());
                        break;
                    case 4:
                        intent.putExtra(QuranRules.NAMEKEY, lstSearching.get(position).getName_fra());
                        break;
                    case 5:
                        intent.putExtra(QuranRules.NAMEKEY, lstSearching.get(position).getName_ger());
                        break;
                    case 6:
                        intent.putExtra(QuranRules.NAMEKEY, lstSearching.get(position).getName_ita());
                        break;
                    case 7:
                        intent.putExtra(QuranRules.NAMEKEY, lstSearching.get(position).getName_rus());
                        break;
                    case 8:
                        intent.putExtra(QuranRules.NAMEKEY, lstSearching.get(position).getName_som());
                        break;
                    case 9:
                        intent.putExtra(QuranRules.NAMEKEY, lstSearching.get(position).getName_spa());
                        break;
                    default:
                        intent.putExtra(QuranRules.NAMEKEY, lstSearching.get(position).getName_ind());
                        break;
                }

                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                int resultL = data.getIntExtra(QuranRules.MAINTODETAIL, -1);
                if(resultL!=-1){
                    language=resultL;
                    adapterS.changedLanguage(resultL);
                }
            }
        }

    }
}
