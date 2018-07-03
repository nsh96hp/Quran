package com.hdpsolution.quranindotranslation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hdpsolution.quranindotranslation.Adapter.ListMainAdapter;
import com.hdpsolution.quranindotranslation.Database.DatabaseHelper;
import com.hdpsolution.quranindotranslation.Database.DatabaseQuran;
import com.hdpsolution.quranindotranslation.Entity.Sura_Quran;

import java.util.ArrayList;

import yalantis.com.sidemenu.interfaces.ScreenShotable;


/**
 * Created by Konstantin on 22.12.2014.
 */
public class ContentFragment extends Fragment implements ScreenShotable {
    public static final String CLOSE = "Close";
    public static final String IC1 = "Malaysia-Indonesia";
    public static final String IC2 = "England";
    public static final String IC3 = "Turkey";
    public static final String IC4 = "France";
    public static final String IC5 = "Germany";
    public static final String IC6 = "Italy";
    public static final String IC7 = "russia";
    public static final String IC8 = "Somalia";
    public static final String IC9 = "Spain";

    private static Context mContext;

    private View containerView;
    protected ImageView mImageView;
    protected int res;
    private Bitmap bitmap;


    public static ContentFragment newInstance(int resId, Context context) {
        ContentFragment contentFragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Integer.class.getName(), resId);
        contentFragment.setArguments(bundle);

        mContext=context;
        return contentFragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.containerView = view.findViewById(R.id.container);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        res = getArguments().getInt(Integer.class.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    @Override
    public void takeScreenShot() {
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }
}