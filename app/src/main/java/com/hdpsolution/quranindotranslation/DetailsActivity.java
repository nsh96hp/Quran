package com.hdpsolution.quranindotranslation;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hdpsolution.quranindotranslation.Adapter.DetailsAdapter;
import com.hdpsolution.quranindotranslation.Adapter.FavoriteAdapter;
import com.hdpsolution.quranindotranslation.Database.DatabaseHelper;
import com.hdpsolution.quranindotranslation.Database.DatabaseQuran;
import com.hdpsolution.quranindotranslation.Database.DatabaseTRANS;
import com.hdpsolution.quranindotranslation.Entity.FavoriteVerse;
import com.hdpsolution.quranindotranslation.Entity.MyLanguage;
import com.hdpsolution.quranindotranslation.Entity.Sura_Quran;
import com.hdpsolution.quranindotranslation.Entity.Vers_Quran;

import java.util.ArrayList;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {

    private RecyclerView rcv_details;
    private DetailsAdapter adapter;
    private ArrayList<Vers_Quran> lstVers;
    private ArrayList<Vers_Quran> lstVersTran;
    private ArrayList<FavoriteVerse> lstLove;
    private ArrayList<Sura_Quran> lstSURA;


    private DatabaseHelper dbh;
    private DatabaseQuran db;
    private DatabaseTRANS dbTrans;
    private int language;
    LinearLayoutManager linearLayoutManager;

    private int IDKEY;
    private int LASTVER;
    private String NAMEKEY;
    private SharedPreferences preLastVerse;
    private SharedPreferences.Editor editLastVerse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = findViewById(R.id.d_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rcv_details = findViewById(R.id.rcv_details);

        db = new DatabaseQuran(DetailsActivity.this);
        dbh = new DatabaseHelper(DetailsActivity.this);
        lstVers = new ArrayList<>();
        lstVersTran = new ArrayList<>();
        lstLove = new ArrayList<>();
        lstSURA = new ArrayList<>();


        Intent intent = this.getIntent();
        IDKEY = intent.getIntExtra(QuranRules.IDKEY, -1);
        NAMEKEY = intent.getStringExtra(QuranRules.NAMEKEY);
        LASTVER = intent.getIntExtra(QuranRules.LASTVERSE_IN_KEY, -1);


        getSupportActionBar().setTitle(NAMEKEY);

        lstLove = dbh.getFavoriteBySURAID(IDKEY);
        lstVers = db.getVerse(IDKEY);

        if (dbh.getLanguage().size() > 0) {
            language = dbh.getLanguage().get(0).getFlag();
        } else {
            language = 1;
        }
        switch (language) {
            case 1:
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_INDO);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                break;
            case 2:
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_ENG);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                break;
            case 3:
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_TUR);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                break;
            case 4:
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_FRE);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                break;
            case 5:
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_GER);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                break;
            case 6:
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_ITA);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                break;
            case 7:
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_RUS);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                break;
            case 8:
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_SOM);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                break;
            case 9:
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_SPA);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                break;
            default:
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_INDO);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                break;
        }

        adapter = new DetailsAdapter(DetailsActivity.this, lstVers, lstVersTran);
        linearLayoutManager = new LinearLayoutManager(DetailsActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_details.setLayoutManager(linearLayoutManager);
        rcv_details.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (LASTVER > 0) {
            linearLayoutManager.scrollToPositionWithOffset(LASTVER, 0);
        }

        handleItemClick();
    }

    private void handleItemClick() {
        adapter.setOnLongItemClickListener(new DetailsAdapter.OnLongItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position, final int idSura, final int idVerse) {
                Log.e("Poss", idSura + "");
                Log.e("Poss", idVerse + "");
                final Dialog dialog = new Dialog(DetailsActivity.this, R.style.mydialogstyle);
                dialog.setContentView(R.layout.dialog_details);

                TextView tv = dialog.findViewById(R.id.txt_verse_title);
                tv.setText(getResources().getString(R.string.verse) + " " + idVerse);

                ImageButton dialog_fav, dialog_share, dialog_coppy;
                dialog_fav = dialog.findViewById(R.id.dialog_fav);
                dialog_share = dialog.findViewById(R.id.dialog_share);
                dialog_coppy = dialog.findViewById(R.id.dialog_coppy);

                dialog_fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int temp = 0;
                        int posLove = -1;
                        for (int i = 0; i < lstLove.size(); i++) {
                            if (lstLove.get(i).getVerseid() == idVerse) {
                                temp = 1;
                                posLove = i;
                            }
                        }
                        if (temp == 1) {
                            dbh.DeleteFavorite(lstLove.get(posLove));
                            lstLove.remove(posLove);
                            adapter.changeLove(lstLove);
                        } else {
                            FavoriteVerse favoriteVerse = new FavoriteVerse();
                            favoriteVerse.setSuraid(idSura);
                            favoriteVerse.setVerseid(idVerse);
                            dbh.AddFavorite(favoriteVerse);
                            lstLove.add(favoriteVerse);
                            adapter.changeLove(lstLove);
                        }
                        dialog.dismiss();
                    }
                });

                dialog_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String verShare = lstVers.get(idVerse - 1).getText() + "\n" + lstVersTran.get(idVerse - 1).getText();

                        Intent intentShare = new Intent(Intent.ACTION_SEND);
                        intentShare.setType("text/plain");
                        intentShare.putExtra(Intent.EXTRA_TEXT, verShare);
                        try {
                            startActivity(Intent.createChooser(intentShare, getResources().getString(R.string.share)));
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(getApplicationContext(), R.string.not_share, Toast.LENGTH_LONG);
                        }
                        dialog.dismiss();
                    }
                });
                dialog_coppy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String verShare = lstVers.get(idVerse - 1).getText() + "\n" + lstVersTran.get(idVerse - 1).getText();
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        clipboard.setText(verShare);

                        Toast.makeText(DetailsActivity.this, getResources().getString(R.string.verse) + " " + idVerse + " " + getResources().getString(R.string.copy), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent returnIntent = new Intent();
                returnIntent.putExtra(QuranRules.FAV_RETURN, 1);
                returnIntent.putExtra(QuranRules.MAINTODETAIL, language);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                break;
            case R.id.m_Read:
                adapter.changeRead();
                break;
            case R.id.m_Jump:
                final Dialog dialog = new Dialog(DetailsActivity.this, R.style.mydialogstyle);
                dialog.setContentView(R.layout.dialog_jump);

                final EditText num_jump = dialog.findViewById(R.id.num_jump);
                num_jump.setHint("1 - " + lstVers.size());
                Button btn_ok = dialog.findViewById(R.id.d_ok);
                Button btn_cancel = dialog.findViewById(R.id.d_cancel);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int num=0;
                        try{
                            num=Integer.parseInt(String.valueOf(num_jump.getText()));
                        }catch (NumberFormatException e){
                            num_jump.setText("");
                            num_jump.setHint(getResources().getString(R.string.faild)+" 1 - "+QuranRules.ARR_SIZE_PART[IDKEY-1]);
                        }


                        if (num <= lstVers.size() && num > 0) {
                            linearLayoutManager.scrollToPositionWithOffset(num - 1, 0);
                            dialog.dismiss();
                        } else {
                            num_jump.setText("");
                            num_jump.setHint(getResources().getString(R.string.faild)+" 1 - "+QuranRules.ARR_SIZE_PART[IDKEY-1]);

                        }
                    }
                });
                dialog.show();
                break;
            case R.id.m_NextPart:
                handleNextPart();
                break;
            case R.id.m_ChangeLanguage:
                lstSURA=db.getSuraQuran();
                final Dialog dialog1 = new Dialog(DetailsActivity.this, R.style.mydialogstyle);
                dialog1.setContentView(R.layout.dialog_language);

                TextView t1, t2, t3, t4, t5, t6, t7, t8, t9;
                ImageButton l1, l2, l3, l4, l5, l6, l7, l8, l9;
                l1 = dialog1.findViewById(R.id.l1);
                l2 = dialog1.findViewById(R.id.l2);
                l3 = dialog1.findViewById(R.id.l3);
                l4 = dialog1.findViewById(R.id.l4);
                l5 = dialog1.findViewById(R.id.l5);
                l6 = dialog1.findViewById(R.id.l6);
                l7 = dialog1.findViewById(R.id.l7);
                l8 = dialog1.findViewById(R.id.l8);
                l9 = dialog1.findViewById(R.id.l9);

                t1 = dialog1.findViewById(R.id.t1);
                t2 = dialog1.findViewById(R.id.t2);
                t3 = dialog1.findViewById(R.id.t3);
                t4 = dialog1.findViewById(R.id.t4);
                t5 = dialog1.findViewById(R.id.t5);
                t6 = dialog1.findViewById(R.id.t6);
                t7 = dialog1.findViewById(R.id.t7);
                t8 = dialog1.findViewById(R.id.t8);
                t9 = dialog1.findViewById(R.id.t9);

                t1.setText(QuranRules.IC1);
                t2.setText(QuranRules.IC2);
                t3.setText(QuranRules.IC3);
                t4.setText(QuranRules.IC4);
                t5.setText(QuranRules.IC5);
                t6.setText(QuranRules.IC6);
                t7.setText(QuranRules.IC7);
                t8.setText(QuranRules.IC8);
                t9.setText(QuranRules.IC9);

                l1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleChangeLanguage(1);
                        dialog1.dismiss();
                    }
                });
                l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleChangeLanguage(2);
                        dialog1.dismiss();
                    }
                });
                l3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleChangeLanguage(3);
                        dialog1.dismiss();
                    }
                });
                l4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleChangeLanguage(4);
                        dialog1.dismiss();
                    }
                });
                l5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleChangeLanguage(5);
                        dialog1.dismiss();
                    }
                });
                l6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleChangeLanguage(6);
                        dialog1.dismiss();
                    }
                });
                l7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleChangeLanguage(7);
                        dialog1.dismiss();
                    }
                });
                l8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleChangeLanguage(8);
                        dialog1.dismiss();
                    }
                });
                l9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleChangeLanguage(9);
                        dialog1.dismiss();
                    }
                });
                dialog1.show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleChangeLanguage(int select) {
        dbh = new DatabaseHelper(DetailsActivity.this);
        ArrayList<MyLanguage> myLanguages;
        myLanguages = dbh.getLanguage();

        switch (select) {
            case 1:
                if (myLanguages.size() > 0) {
                    dbh.EditLanguage(1);
                } else {
                    dbh.ChooseLanguage(1);
                }
                language = 1;
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_INDO);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                adapter.changedLanguage(lstVersTran);
                editLocale(QuranRules.L_INDO);
                getSupportActionBar().setTitle(lstSURA.get(IDKEY-1).getName_ind());
                break;
            case 2:
                if (myLanguages.size() > 0) {
                    dbh.EditLanguage(2);
                } else {
                    dbh.ChooseLanguage(2);
                }
                language = 2;
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_ENG);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                adapter.changedLanguage(lstVersTran);
                editLocale(QuranRules.L_ENG);
                getSupportActionBar().setTitle(lstSURA.get(IDKEY-1).getName_english());
                break;
            case 3:
                if (myLanguages.size() > 0) {
                    dbh.EditLanguage(3);
                } else {
                    dbh.ChooseLanguage(3);
                }
                language = 3;
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_TUR);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                adapter.changedLanguage(lstVersTran);
                editLocale(QuranRules.L_TUR);
                getSupportActionBar().setTitle(lstSURA.get(IDKEY-1).getName_tur());
                break;
            case 4:
                if (myLanguages.size() > 0) {
                    dbh.EditLanguage(4);
                } else {
                    dbh.ChooseLanguage(4);
                }
                language = 4;
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_FRE);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                adapter.changedLanguage(lstVersTran);
                editLocale(QuranRules.L_FRA);
                getSupportActionBar().setTitle(lstSURA.get(IDKEY-1).getName_fra());
                break;
            case 5:
                if (myLanguages.size() > 0) {
                    dbh.EditLanguage(5);
                } else {
                    dbh.ChooseLanguage(5);
                }
                language = 5;
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_GER);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                adapter.changedLanguage(lstVersTran);
                editLocale(QuranRules.L_GER);
                getSupportActionBar().setTitle(lstSURA.get(IDKEY-1).getName_ger());
                break;
            case 6:
                if (myLanguages.size() > 0) {
                    dbh.EditLanguage(6);
                } else {
                    dbh.ChooseLanguage(6);
                }
                language = 6;
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_ITA);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                adapter.changedLanguage(lstVersTran);
                editLocale(QuranRules.L_ITA);
                getSupportActionBar().setTitle(lstSURA.get(IDKEY-1).getName_ita());
                break;
            case 7:
                if (myLanguages.size() > 0) {
                    dbh.EditLanguage(7);
                } else {
                    dbh.ChooseLanguage(7);
                }
                language = 7;
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_RUS);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                adapter.changedLanguage(lstVersTran);
                editLocale(QuranRules.L_RUS);
                getSupportActionBar().setTitle(lstSURA.get(IDKEY-1).getName_rus());
                break;
            case 8:
                if (myLanguages.size() > 0) {
                    dbh.EditLanguage(8);
                } else {
                    dbh.ChooseLanguage(8);
                }
                language = 8;
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_SOM);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                adapter.changedLanguage(lstVersTran);
                editLocale(QuranRules.L_SOM);
                getSupportActionBar().setTitle(lstSURA.get(IDKEY-1).getName_som());
                break;
            case 9:
                if (myLanguages.size() > 0) {
                    dbh.EditLanguage(9);
                } else {
                    dbh.ChooseLanguage(9);
                }
                language = 9;
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_SPA);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                adapter.changedLanguage(lstVersTran);
                editLocale(QuranRules.L_SPA);
                getSupportActionBar().setTitle(lstSURA.get(IDKEY-1).getName_spa());
                break;
            default:
                break;
        }
    }

    private void handleNextPart() {
        // Set lai lstVerTran,lstVerse
        ++IDKEY;
        ArrayList<Sura_Quran> lstSura = new ArrayList<>();
        lstSura = db.getSuraQuran();
        lstVers = db.getVerse(IDKEY);
        switch (language) {
            case 1:
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_INDO);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                getSupportActionBar().setTitle(lstSura.get(IDKEY-1).getName_ind());
                break;
            case 2:
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_ENG);
                getSupportActionBar().setTitle(lstSura.get(IDKEY-1).getName_english());
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                break;
            case 3:
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_TUR);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                getSupportActionBar().setTitle(lstSura.get(IDKEY-1).getName_tur());

                break;
            case 4:
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_FRE);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                getSupportActionBar().setTitle(lstSura.get(IDKEY - 1).getName_fra());

                break;
            case 5:
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_GER);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                getSupportActionBar().setTitle(lstSura.get(IDKEY - 1).getName_ger());

                break;
            case 6:
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_ITA);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                getSupportActionBar().setTitle(lstSura.get(IDKEY - 1).getName_ita());

                break;
            case 7:
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_RUS);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                getSupportActionBar().setTitle(lstSura.get(IDKEY - 1).getName_rus());

                break;
            case 8:
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_SOM);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                getSupportActionBar().setTitle(lstSura.get(IDKEY - 1).getName_som());

                break;
            case 9:
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_SPA);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                getSupportActionBar().setTitle(lstSura.get(IDKEY - 1).getName_spa());

                break;
            default:
                dbTrans = new DatabaseTRANS(DetailsActivity.this, QuranRules.DATABASE_INDO);
                lstVersTran = dbTrans.getVerseTrans(IDKEY);
                getSupportActionBar().setTitle(lstSura.get(IDKEY - 1).getName_ind());
                break;
        }
        lstLove = dbh.getFavoriteBySURAID(IDKEY);
        adapter.nextPart(lstVers, lstVersTran, lstLove);
        linearLayoutManager.scrollToPositionWithOffset(0, 0);

    }

    @Override
    protected void onPause() {
        preLastVerse = getSharedPreferences(QuranRules.LASTVERSE, MODE_PRIVATE);
        editLastVerse = preLastVerse.edit();

        linearLayoutManager = ((LinearLayoutManager) rcv_details.getLayoutManager());
        int firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();

        editLastVerse.putInt(QuranRules.LASTVERSE_KEY, firstVisiblePosition);
        editLastVerse.putInt(QuranRules.LASTVERSE_IDKEY, IDKEY);
        editLastVerse.commit();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(QuranRules.FAV_RETURN, 1);
        returnIntent.putExtra(QuranRules.MAINTODETAIL, language);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void editLocale(String strLocale) {
        Locale locale = new Locale(strLocale);
        Locale.setDefault(locale);
        Resources res = DetailsActivity.this.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
    }
}
