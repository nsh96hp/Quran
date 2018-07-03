package com.hdpsolution.quranindotranslation;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hdpsolution.quranindotranslation.Adapter.DetailsAdapter;
import com.hdpsolution.quranindotranslation.Adapter.FavoriteAdapter;
import com.hdpsolution.quranindotranslation.Adapter.ListMainAdapter;
import com.hdpsolution.quranindotranslation.Database.DatabaseHelper;
import com.hdpsolution.quranindotranslation.Database.DatabaseQuran;
import com.hdpsolution.quranindotranslation.Database.DatabaseTRANS;
import com.hdpsolution.quranindotranslation.Entity.FavoriteVerse;
import com.hdpsolution.quranindotranslation.Entity.Sura_Quran;
import com.hdpsolution.quranindotranslation.Entity.Vers_Quran;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView rcv_favorite;
    private FavoriteAdapter adapterF;
    private ArrayList<FavoriteVerse> lstLove;
    private ArrayList<Sura_Quran> lstSura;


    private DatabaseHelper dbh;
    private DatabaseQuran db;

    private int language;
    LinearLayoutManager linearLayoutManager;


    private SharedPreferences preLastVerse;
    private SharedPreferences.Editor editLastVerse;
    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favorite);

        Toolbar toolbar = findViewById(R.id.f_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.favorite));

        dbh = new DatabaseHelper(FavoriteActivity.this);
        db = new DatabaseQuran(FavoriteActivity.this);

        if (dbh.getLanguage().size() > 0) {
            language = dbh.getLanguage().get(0).getFlag();
        } else {
            language = 1;
        }

        lstSura = new ArrayList<>();
        lstSura = db.getSuraQuran();

        lstLove = new ArrayList<>();
        lstLove = dbh.getFavorite();
        Collections.sort(lstLove);

        rcv_favorite = findViewById(R.id.rcv_favorite);
        adapterF = new FavoriteAdapter(FavoriteActivity.this, lstLove, lstSura, language);
        linearLayoutManager = new LinearLayoutManager(FavoriteActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_favorite.setLayoutManager(linearLayoutManager);
        rcv_favorite.setAdapter(adapterF);
        adapterF.notifyDataSetChanged();

        clickItemRcv();
        bottomBar = (BottomBar) findViewById(R.id.bottomBarS);
        bottomBar.selectTabAtPosition(1);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

                if (tabId == R.id.tab_main) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(QuranRules.BAR1,2);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();

                }
                if (tabId == R.id.tab_favorites) {

                }
                if (tabId == R.id.tab_plus) {
                    final Dialog dialog= new Dialog(FavoriteActivity.this, R.style.mydialogstyle);
                    dialog.setContentView(R.layout.dialog_more);
                    dialog.setCancelable(false);

                    ImageButton btn_cancel,btnShare,btnRate;
                    btn_cancel=dialog.findViewById(R.id.di_cancel);
                    btnShare=dialog.findViewById(R.id.di_share);
                    btnRate=dialog.findViewById(R.id.di_rate);

                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            bottomBar.selectTabAtPosition(1);
                        }
                    });

                    btnShare.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intentShare = new Intent(Intent.ACTION_SEND);
                            intentShare.setType("text/plain");
                            intentShare.putExtra(Intent.EXTRA_TEXT, QuranRules.LINK_APP1);
                            try {
                                startActivity(Intent.createChooser(intentShare, getResources().getString(R.string.share)));
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(getApplicationContext(), R.string.not_share, Toast.LENGTH_LONG);
                            }

                            dialog.dismiss();
                            bottomBar.selectTabAtPosition(1);
                        }
                    });
                    btnRate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri uri = Uri.parse(QuranRules.LINK_APP2);
                            Intent intentRate = new Intent(Intent.ACTION_VIEW, uri);

                            if (!MyAppActivity(intentRate)) {
                                Uri uri1 = Uri.parse(QuranRules.LINK_APP1);
                                Intent intent1 = new Intent(Intent.ACTION_VIEW, uri1);
                                if (!MyAppActivity(intent1)) {
                                    Toast.makeText(getApplicationContext(), R.string.not_rate, Toast.LENGTH_LONG);
                                }
                            }
                            dialog.dismiss();
                            bottomBar.selectTabAtPosition(1);
                        }
                    });
                    dialog.show();

                }
            }
        });



        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {

            }
        });
    }

    private boolean MyAppActivity(Intent intent) {
        try {
            startActivity(intent);
            return (true);
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }
    private void clickItemRcv() {
        adapterF.setOnItemClickListener(new ListMainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position, int id) {
                Intent intent = new Intent(FavoriteActivity.this, DetailsActivity.class);
                intent.putExtra(QuranRules.LASTVERSE_IN_KEY, lstLove.get(position).getVerseid() - 1);
                intent.putExtra(QuranRules.IDKEY, id);
                int pos = id - 1;
                switch (language) {
                    case 1:
                        intent.putExtra(QuranRules.NAMEKEY, lstSura.get(pos).getName_ind());
                        break;
                    case 2:
                        intent.putExtra(QuranRules.NAMEKEY, lstSura.get(pos).getName_english());
                        break;
                    case 3:
                        intent.putExtra(QuranRules.NAMEKEY, lstSura.get(pos).getName_tur());
                        break;
                    case 4:
                        intent.putExtra(QuranRules.NAMEKEY, lstSura.get(pos).getName_fra());
                        break;
                    case 5:
                        intent.putExtra(QuranRules.NAMEKEY, lstSura.get(pos).getName_ger());
                        break;
                    case 6:
                        intent.putExtra(QuranRules.NAMEKEY, lstSura.get(pos).getName_ita());
                        break;
                    case 7:
                        intent.putExtra(QuranRules.NAMEKEY, lstSura.get(pos).getName_rus());
                        break;
                    case 8:
                        intent.putExtra(QuranRules.NAMEKEY, lstSura.get(pos).getName_som());
                        break;
                    case 9:
                        intent.putExtra(QuranRules.NAMEKEY, lstSura.get(pos).getName_spa());
                        break;
                    default:
                        intent.putExtra(QuranRules.NAMEKEY, lstSura.get(pos).getName_ind());
                        break;
                }
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent returnIntent = new Intent();
                returnIntent.putExtra(QuranRules.BAR1,2);
                returnIntent.putExtra(QuranRules.MAINTODETAIL, language);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
                break;
            case R.id.m_delete:
                adapterF.DeleteTime();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(QuranRules.BAR1,2);
        returnIntent.putExtra(QuranRules.MAINTODETAIL, language);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        dbh = new DatabaseHelper(FavoriteActivity.this);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                int result = data.getIntExtra(QuranRules.FAV_RETURN, -1);
                int resultLanguage = data.getIntExtra(QuranRules.MAINTODETAIL, -1);
                Log.e("LEEEEE",resultLanguage+"");
                if (result != -1) {
                    lstLove = dbh.getFavorite();
                    adapterF.edit(lstLove);
                }

                if(resultLanguage!=-1){
                    language=resultLanguage;
                    switch (resultLanguage){
                        case 1:
                            adapterF.changedLanguage(1);
                            editLocale(QuranRules.L_INDO);
                            break;
                        case 2:
                            adapterF.changedLanguage(2);
                            editLocale(QuranRules.L_ENG);
                            break;
                        case 3:
                            adapterF.changedLanguage(3);
                            editLocale(QuranRules.L_TUR);
                            break;
                        case 4:
                            adapterF.changedLanguage(4);
                            editLocale(QuranRules.L_FRA);
                            break;
                        case 5:
                            adapterF.changedLanguage(5);
                            editLocale(QuranRules.L_GER);
                            break;
                        case 6:
                            adapterF.changedLanguage(6);
                            editLocale(QuranRules.L_ITA);
                            break;
                        case 7:
                            adapterF.changedLanguage(7);
                            editLocale(QuranRules.L_RUS);
                            break;
                        case 8:
                            adapterF.changedLanguage(8);
                            editLocale(QuranRules.L_SOM);
                            break;
                        case 9:
                            adapterF.changedLanguage(9);
                            editLocale(QuranRules.L_SPA);
                            break;

                    }
                }
            }
        }
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void editLocale(String strLocale) {
        Locale locale = new Locale(strLocale);
        Locale.setDefault(locale);
        Resources res = FavoriteActivity.this.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
    }
}
