package com.hdpsolution.quranindotranslation;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hdpsolution.quranindotranslation.Adapter.ListMainAdapter;
import com.hdpsolution.quranindotranslation.Database.DatabaseHelper;
import com.hdpsolution.quranindotranslation.Database.DatabaseQuran;
import com.hdpsolution.quranindotranslation.Database.DatabaseTRANS;
import com.hdpsolution.quranindotranslation.Entity.MyLanguage;
import com.hdpsolution.quranindotranslation.Entity.Sura_Quran;
import com.hdpsolution.quranindotranslation.Entity.Vers_Quran;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.roughike.bottombar.TabSelectionInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import yalantis.com.sidemenu.interfaces.Resourceble;
import yalantis.com.sidemenu.interfaces.ScreenShotable;
import yalantis.com.sidemenu.model.SlideMenuItem;

import yalantis.com.sidemenu.util.ViewAnimator;


public class MainActivity extends AppCompatActivity implements ViewAnimator.ViewAnimatorListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private List<SlideMenuItem> list = new ArrayList<>();
    private ContentFragment contentFragment;
    private ViewAnimator viewAnimator;
    private int res = R.drawable.malay_indo;
    private LinearLayout linearLayout;
    private MaterialSearchView searchView;
    private ArrayList<Sura_Quran> lstSearching;
    private ArrayList<Sura_Quran> lstSearching_ver;
    private LinearLayoutManager linearLayoutManager;

    private DatabaseHelper dbh;
    private DatabaseQuran db;
    private DatabaseTRANS dbt;
    private RecyclerView rcv_main;
    private ListMainAdapter adapterMain;
    private ArrayList<Sura_Quran> lstSura;
    private ArrayList<Vers_Quran> lstVerseTran;

    private SharedPreferences preLastVerse;
    private SharedPreferences.Editor editLastVerse;


    private int language;
    private String sugg[];
    private int exit = 1;
    BottomBar bottomBar;
    private int SelectItemJump = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        contentFragment = ContentFragment.newInstance(R.drawable.ic_close_black_24dp, MainActivity.this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, contentFragment)
                .commit();

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        linearLayout = findViewById(R.id.left_drawer);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });
        searchView = findViewById(R.id.search_view);


        setActionBar();
        createMenuList();
        viewAnimator = new ViewAnimator<>(this, list, contentFragment, drawerLayout, this);

        dbh = new DatabaseHelper(MainActivity.this);
        db = new DatabaseQuran(MainActivity.this);

        //Lấy ra danh sách số trang của các trương từ cơ sở dữ liệu
//        ArrayList<Integer> rs= new ArrayList<>();
//        rs=db.getSizePart();
//        for (int i=0;i<rs.size();i++){
//            Log.e("rs",rs.get(i)+",");
//        }


        if (dbh.getLanguage().size() > 0) {
            language = dbh.getLanguage().get(0).getFlag();
        } else {
            language = 1;
        }
        changeLocale();
        lstVerseTran = dbt.getAllVerseTrans();

        lstSura = new ArrayList<>();
        lstSura = db.getSuraQuran();
        rcv_main = findViewById(R.id.rcv_main);
        adapterMain = new ListMainAdapter(MainActivity.this, lstSura, language);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_main.setLayoutManager(linearLayoutManager);
        rcv_main.setAdapter(adapterMain);
        adapterMain.notifyDataSetChanged();

        clickItemRcv();
        handleSearch();

        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.selectTabAtPosition(0);


        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

                if (tabId == R.id.tab_main) {
                    Log.e("1", "1");
                }
                if (tabId == R.id.tab_favorites) {
                    Log.e("2", "2");
                    Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
                    startActivityForResult(intent, 2);


                }
                if (tabId == R.id.tab_plus) {
                    final Dialog dialog = new Dialog(MainActivity.this, R.style.mydialogstyle);
                    dialog.setContentView(R.layout.dialog_more);
                    dialog.setCancelable(false);

                    ImageButton btn_cancel, btnShare, btnRate;
                    btn_cancel = dialog.findViewById(R.id.di_cancel);
                    btnShare = dialog.findViewById(R.id.di_share);
                    btnRate = dialog.findViewById(R.id.di_rate);

                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            bottomBar.selectTabAtPosition(0);
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
                            bottomBar.selectTabAtPosition(0);
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
                            bottomBar.selectTabAtPosition(0);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                int result = data.getIntExtra(QuranRules.BAR1, -1);
                if (result != -1) {
                    bottomBar.selectTabAtPosition(0);
                }

                int resultL = data.getIntExtra(QuranRules.MAINTODETAIL, -1);
                if (resultL != -1) {
                    language = resultL;
                    changeLocale();
                    switch (resultL) {
                        case 1:
                            switchLangugeSuggest(language);
                            adapterMain.changedLanguage(1);

                            break;
                        case 2:
                            switchLangugeSuggest(language);
                            adapterMain.changedLanguage(2);

                            break;
                        case 3:
                            switchLangugeSuggest(language);
                            adapterMain.changedLanguage(3);

                            break;
                        case 4:
                            switchLangugeSuggest(language);
                            adapterMain.changedLanguage(4);

                            break;
                        case 5:
                            switchLangugeSuggest(language);
                            adapterMain.changedLanguage(5);

                            break;
                        case 6:
                            switchLangugeSuggest(language);
                            adapterMain.changedLanguage(6);

                            break;
                        case 7:
                            switchLangugeSuggest(language);
                            adapterMain.changedLanguage(7);

                        case 8:
                            switchLangugeSuggest(language);
                            adapterMain.changedLanguage(8);

                            break;
                        case 9:
                            switchLangugeSuggest(language);
                            adapterMain.changedLanguage(9);

                            break;

                    }

                }
            }
        }
        if (requestCode == 4 || requestCode == 5 || requestCode == 6) {
            if (resultCode == Activity.RESULT_OK) {
                int result = data.getIntExtra(QuranRules.MAINTODETAIL, -1);
                if (result != -1) {
                    language = result;
                    changeLocale();
                    switch (result) {
                        case 1:
                            switchLangugeSuggest(language);
                            adapterMain.changedLanguage(1);

                            break;
                        case 2:
                            switchLangugeSuggest(language);
                            adapterMain.changedLanguage(2);

                            break;
                        case 3:
                            switchLangugeSuggest(language);
                            adapterMain.changedLanguage(3);

                            break;
                        case 4:
                            switchLangugeSuggest(language);
                            adapterMain.changedLanguage(4);

                            break;
                        case 5:
                            switchLangugeSuggest(language);
                            adapterMain.changedLanguage(5);

                            break;
                        case 6:
                            switchLangugeSuggest(language);
                            adapterMain.changedLanguage(6);

                            break;
                        case 7:
                            switchLangugeSuggest(language);
                            adapterMain.changedLanguage(7);

                            break;
                        case 8:
                            switchLangugeSuggest(language);
                            adapterMain.changedLanguage(8);

                            break;
                        case 9:
                            switchLangugeSuggest(language);
                            adapterMain.changedLanguage(9);

                            break;

                    }

                    Log.e("Nhay vao day?", result + "");

                }
            }
        }

    }

    private void switchLangugeSuggest(int language) {
        sugg = new String[lstSura.size()];
        switch (language) {
            case 1:
                for (int i = 0; i < lstSura.size(); i++) {
                    sugg[i] = lstSura.get(i).getName_ind();
                }

                break;
            case 2:
                for (int i = 0; i < lstSura.size(); i++) {
                    sugg[i] = lstSura.get(i).getName_english();
                }
                break;
            case 3:
                for (int i = 0; i < lstSura.size(); i++) {
                    sugg[i] = lstSura.get(i).getName_tur();
                }
                break;
            case 4:
                for (int i = 0; i < lstSura.size(); i++) {
                    sugg[i] = lstSura.get(i).getName_fra();
                }
                break;
            case 5:
                for (int i = 0; i < lstSura.size(); i++) {
                    sugg[i] = lstSura.get(i).getName_ger();
                }
                break;
            case 6:
                for (int i = 0; i < lstSura.size(); i++) {
                    sugg[i] = lstSura.get(i).getName_ita();
                }
                break;
            case 7:
                for (int i = 0; i < lstSura.size(); i++) {
                    sugg[i] = lstSura.get(i).getName_rus();
                }
                break;
            case 8:
                for (int i = 0; i < lstSura.size(); i++) {
                    sugg[i] = lstSura.get(i).getName_som();
                }
                break;
            case 9:
                for (int i = 0; i < lstSura.size(); i++) {
                    sugg[i] = lstSura.get(i).getName_spa();
                }
                break;
            default:
                for (int i = 0; i < lstSura.size(); i++) {
                    sugg[i] = lstSura.get(i).getName_ind();
                }
                break;
        }
        searchView.setSuggestions(sugg);
    }

    private void handleSearch() {
        switchLangugeSuggest(language);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Wait for result...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        lstSearching = new ArrayList<>();
                        //Search theo tieu de
                        for (int i = 0; i < sugg.length; i++) {
                            if (sugg[i].toString().toUpperCase().indexOf(query.toUpperCase()) >= 0) {
                                lstSearching.add(lstSura.get(i));
                            }
                        }
                        lstSearching_ver = new ArrayList<>();
                        for (int i = 0; i < lstVerseTran.size(); i++) {
                            if (lstVerseTran.get(i).getText().toString().toUpperCase().indexOf(query.toUpperCase()) >= 0) {
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

                        //Lọc phần tử trùng, ưu tiên theo tiêu đề
                        for (int i = 0; i < lstSearching.size(); i++) {
                            for (int j = 0; j < lstSearching_ver.size(); j++) {
                                if (lstSearching.get(i).getSuraid() == lstSearching_ver.get(j).getSuraid()) {
                                    lstSearching_ver.remove(j);
                                }
                            }
                        }


                        //Ghép lại
                        for (int i = 0; i < lstSearching_ver.size(); i++) {
                            lstSearching.add(lstSearching_ver.get(i));
                        }


                        if (lstSearching.size() == 0) {
                            final Snackbar snackbar = Snackbar.make(findViewById(R.id.content_overlay), getResources().getString(R.string.noresults), Snackbar.LENGTH_LONG);
                            snackbar.setAction(getResources().getString(R.string.dismiss), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    snackbar.dismiss();
                                }
                            });
                            snackbar.show();
                        } else {
                            if (lstSearching.size() == 1) {
                                oneResultsSearch();
                            } else {
                                if (lstSearching.size() > 1) {
                                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                                    intent.putExtra(QuranRules.SEARCHKEY, query);
                                    startActivityForResult(intent, 6);
                                }
                            }
                        }
                        try {
                            Thread.sleep(QuranRules.TIME_WAIT_SEARCH);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }).start();
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
            }
        });
    }

    private void oneResultsSearch() {
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra(QuranRules.IDKEY, lstSearching.get(0).getSuraid());
        switch (language) {
            case 1:
                intent.putExtra(QuranRules.NAMEKEY, lstSura.get(lstSearching.get(0).getSuraid() - 1).getName_ind());
                break;
            case 2:
                intent.putExtra(QuranRules.NAMEKEY, lstSura.get(lstSearching.get(0).getSuraid() - 1).getName_english());
                break;
            case 3:
                intent.putExtra(QuranRules.NAMEKEY, lstSura.get(lstSearching.get(0).getSuraid() - 1).getName_tur());
                break;
            case 4:
                intent.putExtra(QuranRules.NAMEKEY, lstSura.get(lstSearching.get(0).getSuraid() - 1).getName_fra());
                break;
            case 5:
                intent.putExtra(QuranRules.NAMEKEY, lstSura.get(lstSearching.get(0).getSuraid() - 1).getName_ger());
                break;
            case 6:
                intent.putExtra(QuranRules.NAMEKEY, lstSura.get(lstSearching.get(0).getSuraid() - 1).getName_ita());
                break;
            case 7:
                intent.putExtra(QuranRules.NAMEKEY, lstSura.get(lstSearching.get(0).getSuraid() - 1).getName_rus());
                break;
            case 8:
                intent.putExtra(QuranRules.NAMEKEY, lstSura.get(lstSearching.get(0).getSuraid() - 1).getName_som());
                break;
            case 9:
                intent.putExtra(QuranRules.NAMEKEY, lstSura.get(lstSearching.get(0).getSuraid() - 1).getName_spa());
                break;
            default:
                intent.putExtra(QuranRules.NAMEKEY, lstSura.get(lstSearching.get(0).getSuraid() - 1).getName_ind());
                break;
        }
        startActivityForResult(intent, 5);
    }

    private void clickItemRcv() {
        adapterMain.setOnItemClickListener(new ListMainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position, int id) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra(QuranRules.IDKEY, id);
                switch (language) {
                    case 1:
                        intent.putExtra(QuranRules.NAMEKEY, lstSura.get(position).getName_ind());
                        break;
                    case 2:
                        intent.putExtra(QuranRules.NAMEKEY, lstSura.get(position).getName_english());
                        break;
                    case 3:
                        intent.putExtra(QuranRules.NAMEKEY, lstSura.get(position).getName_tur());
                        break;
                    case 4:
                        intent.putExtra(QuranRules.NAMEKEY, lstSura.get(position).getName_fra());
                        break;
                    case 5:
                        intent.putExtra(QuranRules.NAMEKEY, lstSura.get(position).getName_ger());
                        break;
                    case 6:
                        intent.putExtra(QuranRules.NAMEKEY, lstSura.get(position).getName_ita());
                        break;
                    case 7:
                        intent.putExtra(QuranRules.NAMEKEY, lstSura.get(position).getName_rus());
                        break;
                    case 8:
                        intent.putExtra(QuranRules.NAMEKEY, lstSura.get(position).getName_som());
                        break;
                    case 9:
                        intent.putExtra(QuranRules.NAMEKEY, lstSura.get(position).getName_spa());
                        break;
                    default:
                        intent.putExtra(QuranRules.NAMEKEY, lstSura.get(position).getName_ind());
                        break;
                }

                startActivityForResult(intent, 4);
            }
        });
    }

    private void createMenuList() {
        SlideMenuItem menuItem0 = new SlideMenuItem(ContentFragment.CLOSE, R.drawable.ic_close_black_24dp);
        list.add(menuItem0);
        SlideMenuItem menuItem = new SlideMenuItem(ContentFragment.IC1, R.drawable.malay_indo);
        list.add(menuItem);
        SlideMenuItem menuItem2 = new SlideMenuItem(ContentFragment.IC2, R.drawable.england);
        list.add(menuItem2);
        SlideMenuItem menuItem3 = new SlideMenuItem(ContentFragment.IC3, R.drawable.turkey);
        list.add(menuItem3);
        SlideMenuItem menuItem4 = new SlideMenuItem(ContentFragment.IC4, R.drawable.france);
        list.add(menuItem4);
        SlideMenuItem menuItem5 = new SlideMenuItem(ContentFragment.IC5, R.drawable.germany);
        list.add(menuItem5);
        SlideMenuItem menuItem6 = new SlideMenuItem(ContentFragment.IC6, R.drawable.italy);
        list.add(menuItem6);
        SlideMenuItem menuItem7 = new SlideMenuItem(ContentFragment.IC7, R.drawable.russia);
        list.add(menuItem7);
        SlideMenuItem menuItem8 = new SlideMenuItem(ContentFragment.IC8, R.drawable.somalia);
        list.add(menuItem8);
        SlideMenuItem menuItem9 = new SlideMenuItem(ContentFragment.IC9, R.drawable.spain);
        list.add(menuItem9);
    }


    private void setActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                linearLayout.removeAllViews();
                linearLayout.invalidate();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0.6 && linearLayout.getChildCount() == 0)
                    viewAnimator.showMenuContent();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.mLast_verse:
                goToLastVerse();
                return true;
            case R.id.mJump:
                jumpToPart();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean MyAppActivity(Intent intent) {
        try {
            startActivity(intent);
            return (true);
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    private void goToLastVerse() {
        preLastVerse = getSharedPreferences(QuranRules.LASTVERSE, MODE_PRIVATE);
        int lvKey = preLastVerse.getInt(QuranRules.LASTVERSE_KEY, -1);
        int idKey = preLastVerse.getInt(QuranRules.LASTVERSE_IDKEY, -1);

        Log.e("pre id: ", preLastVerse.getInt(QuranRules.LASTVERSE_IDKEY, -1) + "");
        Log.e("pre k: ", preLastVerse.getInt(QuranRules.LASTVERSE_KEY, -1) + "");

        if (lvKey >= 0 && idKey >= 0) {
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            intent.putExtra(QuranRules.IDKEY, idKey);
            intent.putExtra(QuranRules.LASTVERSE_IN_KEY, lvKey);
            switch (language) {
                case 1:
                    intent.putExtra(QuranRules.NAMEKEY, lstSura.get(idKey - 1).getName_ind());
                    break;
                case 2:
                    intent.putExtra(QuranRules.NAMEKEY, lstSura.get(idKey - 1).getName_english());
                    break;
                case 3:
                    intent.putExtra(QuranRules.NAMEKEY, lstSura.get(idKey - 1).getName_tur());
                    break;
                case 4:
                    intent.putExtra(QuranRules.NAMEKEY, lstSura.get(idKey - 1).getName_fra());
                    break;
                case 5:
                    intent.putExtra(QuranRules.NAMEKEY, lstSura.get(idKey - 1).getName_ger());
                    break;
                case 6:
                    intent.putExtra(QuranRules.NAMEKEY, lstSura.get(idKey - 1).getName_ita());
                    break;
                case 7:
                    intent.putExtra(QuranRules.NAMEKEY, lstSura.get(idKey - 1).getName_rus());
                    break;
                case 8:
                    intent.putExtra(QuranRules.NAMEKEY, lstSura.get(idKey - 1).getName_som());
                    break;
                case 9:
                    intent.putExtra(QuranRules.NAMEKEY, lstSura.get(idKey - 1).getName_spa());
                    break;
                default:
                    intent.putExtra(QuranRules.NAMEKEY, lstSura.get(idKey - 1).getName_ind());
                    break;
            }
            startActivity(intent);
        } else {
            final Snackbar snackBar = Snackbar.make(findViewById(R.id.content_overlay), getResources().getString(R.string.havenotlastverse), Snackbar.LENGTH_LONG);
            snackBar.setAction(getResources().getString(R.string.dismiss), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackBar.dismiss();
                }
            });
            snackBar.show();
        }
    }

    private void jumpToPart() {
        final Dialog dialog = new Dialog(MainActivity.this, R.style.mydialogstyle);

        dialog.setContentView(R.layout.dialog_jump_part);

        final EditText num_jump = dialog.findViewById(R.id.num_jump);
        num_jump.setHint("1 - 7");
        Spinner spinner = dialog.findViewById(R.id.spin_part);

        String[] resource = new String[sugg.length];
        for (int i = 0; i < sugg.length; i++) {
            resource[i] = (i + 1) + " - " + sugg[i].toString() + ", " + lstSura.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                resource
        );
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("SSSS", position + " " + id);
                num_jump.setHint("1 - " + QuranRules.ARR_SIZE_PART[position]);
                SelectItemJump = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


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
                int num = 0;
                try {
                    num = Integer.parseInt(String.valueOf(num_jump.getText()));
                } catch (NumberFormatException e) {
                    num_jump.setText("");
                    num_jump.setHint(getResources().getString(R.string.faild) + " 1 - " + QuranRules.ARR_SIZE_PART[SelectItemJump]);
                }

                if (num <= QuranRules.ARR_SIZE_PART[SelectItemJump] && num > 0) {

                    int idKey = SelectItemJump + 1;

                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtra(QuranRules.IDKEY, idKey);
                    intent.putExtra(QuranRules.LASTVERSE_IN_KEY, num - 1);
                    switch (language) {
                        case 1:
                            intent.putExtra(QuranRules.NAMEKEY, lstSura.get(idKey - 1).getName_ind());
                            break;
                        case 2:
                            intent.putExtra(QuranRules.NAMEKEY, lstSura.get(idKey - 1).getName_english());
                            break;
                        case 3:
                            intent.putExtra(QuranRules.NAMEKEY, lstSura.get(idKey - 1).getName_tur());
                            break;
                        case 4:
                            intent.putExtra(QuranRules.NAMEKEY, lstSura.get(idKey - 1).getName_fra());
                            break;
                        case 5:
                            intent.putExtra(QuranRules.NAMEKEY, lstSura.get(idKey - 1).getName_ger());
                            break;
                        case 6:
                            intent.putExtra(QuranRules.NAMEKEY, lstSura.get(idKey - 1).getName_ita());
                            break;
                        case 7:
                            intent.putExtra(QuranRules.NAMEKEY, lstSura.get(idKey - 1).getName_rus());
                            break;
                        case 8:
                            intent.putExtra(QuranRules.NAMEKEY, lstSura.get(idKey - 1).getName_som());
                            break;
                        case 9:
                            intent.putExtra(QuranRules.NAMEKEY, lstSura.get(idKey - 1).getName_spa());
                            break;
                        default:
                            intent.putExtra(QuranRules.NAMEKEY, lstSura.get(idKey - 1).getName_ind());
                            break;
                    }
                    startActivity(intent);

                    dialog.dismiss();
                } else {
                    num_jump.setText("");
                    num_jump.setHint(getResources().getString(R.string.faild) + " 1 - " + QuranRules.ARR_SIZE_PART[SelectItemJump]);
                }
            }
        });

        dialog.show();
    }

    private ScreenShotable replaceFragment(ScreenShotable screenShotable, int topPosition) {

        this.res = this.res == R.drawable.ic_close_black_24dp ? R.drawable.ic_close_black_24dp : R.drawable.ic_close_black_24dp;
        View view = findViewById(R.id.content_frame);
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(view, 0, topPosition, 0, finalRadius);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(ViewAnimator.CIRCULAR_REVEAL_ANIMATION_DURATION);

        findViewById(R.id.content_overlay).setBackgroundDrawable(new BitmapDrawable(getResources(), screenShotable.getBitmap()));
        animator.start();

        ContentFragment contentFragment = ContentFragment.newInstance(this.res, MainActivity.this);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, contentFragment).commit();
        return contentFragment;
    }

    @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable, int position) {
        dbh = new DatabaseHelper(MainActivity.this);
        ArrayList<MyLanguage> myLanguages;

//        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.noanim);

        myLanguages = dbh.getLanguage();
        switch (slideMenuItem.getName()) {
            case ContentFragment.CLOSE:
                return screenShotable;
            case ContentFragment.IC1:
                if (myLanguages.size() > 0) {
                    dbh.EditLanguage(1);
                } else {
                    dbh.ChooseLanguage(1);
                }
                language = 1;
                switchLangugeSuggest(language);
                adapterMain.changedLanguage(1);
                editLocale(QuranRules.L_INDO);
                finish();
                startActivity(getIntent());

                return screenShotable;
            case ContentFragment.IC2:
                if (myLanguages.size() > 0) {
                    dbh.EditLanguage(2);
                } else {
                    dbh.ChooseLanguage(2);
                }
                language = 2;
                switchLangugeSuggest(language);
                editLocale(QuranRules.L_ENG);
                adapterMain.changedLanguage(2);
                finish();
                startActivity(getIntent());
                return screenShotable;
            case ContentFragment.IC3:
                if (myLanguages.size() > 0) {
                    dbh.EditLanguage(3);
                } else {
                    dbh.ChooseLanguage(3);
                }
                language = 3;
                switchLangugeSuggest(language);
                adapterMain.changedLanguage(3);
                editLocale(QuranRules.L_TUR);
                finish();
                startActivity(getIntent());
                return screenShotable;
            case ContentFragment.IC4:
                if (myLanguages.size() > 0) {
                    dbh.EditLanguage(4);
                } else {
                    dbh.ChooseLanguage(4);
                }
                language = 4;
                switchLangugeSuggest(language);
                editLocale(QuranRules.L_FRA);
                adapterMain.changedLanguage(4);
                finish();
                startActivity(getIntent());
                return screenShotable;
            case ContentFragment.IC5:
                if (myLanguages.size() > 0) {
                    dbh.EditLanguage(5);
                } else {
                    dbh.ChooseLanguage(5);
                }
                language = 5;
                switchLangugeSuggest(language);
                editLocale(QuranRules.L_GER);
                adapterMain.changedLanguage(5);
                finish();
                startActivity(getIntent());
                return screenShotable;
            case ContentFragment.IC6:
                if (myLanguages.size() > 0) {
                    dbh.EditLanguage(6);
                } else {
                    dbh.ChooseLanguage(6);
                }
                language = 6;
                switchLangugeSuggest(language);
                adapterMain.changedLanguage(6);
                editLocale(QuranRules.L_ITA);
                finish();
                startActivity(getIntent());
                return screenShotable;
            case ContentFragment.IC7:
                if (myLanguages.size() > 0) {
                    dbh.EditLanguage(7);
                } else {
                    dbh.ChooseLanguage(7);
                }
                language = 7;
                switchLangugeSuggest(language);
                adapterMain.changedLanguage(7);
                editLocale(QuranRules.L_RUS);
                finish();
                startActivity(getIntent());
                return screenShotable;
            case ContentFragment.IC8:
                if (myLanguages.size() > 0) {
                    dbh.EditLanguage(8);
                } else {
                    dbh.ChooseLanguage(8);
                }
                language = 8;
                switchLangugeSuggest(language);
                adapterMain.changedLanguage(8);
                editLocale(QuranRules.L_SOM);
                finish();
                startActivity(getIntent());
                return screenShotable;
            case ContentFragment.IC9:
                if (myLanguages.size() > 0) {
                    dbh.EditLanguage(9);
                } else {
                    dbh.ChooseLanguage(9);
                }
                language = 9;
                switchLangugeSuggest(language);
                adapterMain.changedLanguage(9);
                editLocale(QuranRules.L_SPA);
                finish();
                startActivity(getIntent());
                return screenShotable;
            default:
                return replaceFragment(screenShotable, position);
        }
    }

    private void changeLocale() {
        switch (language) {
            case 1:
                editLocale(QuranRules.L_INDO);
                dbt = new DatabaseTRANS(MainActivity.this, QuranRules.DATABASE_INDO);

                break;
            case 2:
                editLocale(QuranRules.L_ENG);
                dbt = new DatabaseTRANS(MainActivity.this, QuranRules.DATABASE_ENG);
                break;
            case 3:
                editLocale(QuranRules.L_TUR);
                dbt = new DatabaseTRANS(MainActivity.this, QuranRules.DATABASE_TUR);
                break;
            case 4:
                editLocale(QuranRules.L_FRA);
                dbt = new DatabaseTRANS(MainActivity.this, QuranRules.DATABASE_FRE);
                break;
            case 5:
                editLocale(QuranRules.L_GER);
                dbt = new DatabaseTRANS(MainActivity.this, QuranRules.DATABASE_GER);
                break;
            case 6:
                editLocale(QuranRules.L_ITA);
                dbt = new DatabaseTRANS(MainActivity.this, QuranRules.DATABASE_ITA);
                break;
            case 7:
                editLocale(QuranRules.L_RUS);
                dbt = new DatabaseTRANS(MainActivity.this, QuranRules.DATABASE_RUS);
                break;
            case 8:
                editLocale(QuranRules.L_SOM);
                dbt = new DatabaseTRANS(MainActivity.this, QuranRules.DATABASE_SOM);
                break;
            case 9:
                editLocale(QuranRules.L_SPA);
                dbt = new DatabaseTRANS(MainActivity.this, QuranRules.DATABASE_SPA);
                break;
            default:
                editLocale(QuranRules.L_INDO);
                dbt = new DatabaseTRANS(MainActivity.this, QuranRules.DATABASE_INDO);
                break;
        }
        lstVerseTran=dbt.getAllVerseTrans();
    }

    @Override
    public void disableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(false);

    }

    @Override
    public void enableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout.closeDrawers();

    }

    @Override
    public void addViewToContainer(View view) {
        linearLayout.addView(view);
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            if (exit == 0) {
                super.onBackPressed();
            }
            final Snackbar snackBar = Snackbar.make(findViewById(R.id.content_overlay), getResources().getString(R.string.exitmain), Snackbar.LENGTH_LONG);
            snackBar.setAction(getResources().getString(R.string.dismiss), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackBar.dismiss();
                }
            });
            snackBar.show();
            exit--;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(QuranRules.TIME_WAIT_EXIT);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    snackBar.dismiss();
                    exit = 1;
                }
            }).start();

        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void editLocale(String strLocale) {
        Locale locale = new Locale(strLocale);
        Locale.setDefault(locale);
        Resources res = MainActivity.this.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
    }


}