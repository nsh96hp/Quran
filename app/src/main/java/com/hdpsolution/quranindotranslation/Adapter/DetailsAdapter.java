package com.hdpsolution.quranindotranslation.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hdpsolution.quranindotranslation.Database.DatabaseHelper;
import com.hdpsolution.quranindotranslation.DetailsActivity;
import com.hdpsolution.quranindotranslation.Entity.FavoriteVerse;
import com.hdpsolution.quranindotranslation.Entity.Sura_Quran;
import com.hdpsolution.quranindotranslation.Entity.Vers_Quran;
import com.hdpsolution.quranindotranslation.FavoriteActivity;
import com.hdpsolution.quranindotranslation.R;

import java.util.ArrayList;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Vers_Quran> lstVers;
    private ArrayList<Vers_Quran> lstVersTrans;
    private ArrayList<FavoriteVerse> lstLove;
    private int flag=-1;
    private DatabaseHelper dbh;

    public DetailsAdapter(Context mContext, ArrayList<Vers_Quran> lstVers,
                          ArrayList<Vers_Quran> lstVersTrans ) {
        this.mContext = mContext;
        this.lstVers = lstVers;
        this.lstVersTrans = lstVersTrans;
    }

    private static OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position, int id);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private static OnLongItemClickListener listener1;

    public interface OnLongItemClickListener {
        void onItemClick(View itemView, int position, int idSura, int idVerse);
    }

    public void setOnLongItemClickListener(OnLongItemClickListener listener1) {
        this.listener1 = listener1;
    }

    public void changedLanguage(ArrayList<Vers_Quran> lstChange){
        lstVersTrans=lstChange;
        notifyDataSetChanged();
    }
    public void changeRead(){
        flag=flag*-1;
        notifyDataSetChanged();
    }
    public void nextPart(ArrayList<Vers_Quran> lstVersNext,ArrayList<Vers_Quran> lstVerTransNext,ArrayList<FavoriteVerse> lstLovex){
        lstVersTrans=lstVerTransNext;
        lstVers=lstVersNext;
        lstLove=lstLovex;
        notifyDataSetChanged();
    }
    public void changeLove(ArrayList<FavoriteVerse> lstLovex){
        lstLove=lstLovex;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_details, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        int temp=0;
        for (int i=0;i<lstLove.size();i++){
            if(lstLove.get(i).getVerseid()==lstVers.get(position).getVersid()){
                temp=1;
            }
        }
        if (temp==1){
            holder.imgID.setImageResource(R.drawable.star_love);
        }else {
            holder.imgID.setImageResource(R.drawable.star_detail);
        }

        holder.id_verse.setText(lstVers.get(position).getVersid()+"");
        holder.content_ver.setText(lstVers.get(position).getText());
        holder.content_tran.setText(lstVersTrans.get(position).getText());
        if(flag==-1){
            holder.content_tran.setVisibility(View.VISIBLE);
        }else {
            if(flag==1){
                holder.content_tran.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return lstVers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id_verse,content_ver,content_tran;
        ImageView imgID;

        public ViewHolder(final View itemView) {
            super(itemView);
            dbh=new DatabaseHelper(mContext);
            lstLove=dbh.getFavoriteBySURAID(lstVers.get(0).getSuraid());



            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int idSura = lstVers.get(getAdapterPosition()).getSuraid();
                    int idVer = lstVers.get(getAdapterPosition()).getVersid();
                    if (listener1 != null)
                        listener1.onItemClick(itemView, getAdapterPosition(), idSura,idVer);
                    return false;
                }
            });
            id_verse=itemView.findViewById(R.id.id_verse);
            content_ver=itemView.findViewById(R.id.content_ver);
            content_tran=itemView.findViewById(R.id.content_tran);

            imgID=itemView.findViewById(R.id.imgID);


        }
    }


}
