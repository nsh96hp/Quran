package com.hdpsolution.quranindotranslation.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hdpsolution.quranindotranslation.Database.DatabaseHelper;
import com.hdpsolution.quranindotranslation.Database.DatabaseQuran;
import com.hdpsolution.quranindotranslation.Entity.FavoriteVerse;
import com.hdpsolution.quranindotranslation.Entity.Sura_Quran;
import com.hdpsolution.quranindotranslation.Entity.Vers_Quran;
import com.hdpsolution.quranindotranslation.QuranRules;
import com.hdpsolution.quranindotranslation.R;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<FavoriteVerse> lstLove;
    private ArrayList<Sura_Quran> lstSura;
    private DatabaseHelper dbh;
    private DatabaseQuran db;
    private int language;
    private int delete=-1;

    public FavoriteAdapter(Context mContext, ArrayList<FavoriteVerse> lstLove, ArrayList<Sura_Quran> lstSura, int language) {
        this.mContext = mContext;
        this.lstLove = lstLove;
        this.lstSura = lstSura;
        this.language = language;
    }

    private static ListMainAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position, int id);
    }

    public void setOnItemClickListener(ListMainAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    private static OnLongItemClickListener listener1;

    public interface OnLongItemClickListener {
        void onItemClick(View itemView, int position, int id);
    }

    public void setOnLongItemClickListener(OnLongItemClickListener listener1) {
        this.listener1 = listener1;
    }

    public void DeleteTime(){
        delete=delete*-1;
        notifyDataSetChanged();
    }
    public void edit(ArrayList<FavoriteVerse> lstLovex){
        lstLove=lstLovex;
        notifyDataSetChanged();
    }
    public void changedLanguage(int languagex){
        language=languagex;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_favourite, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, int position) {

        if(delete==-1){
            holder.ll_delete.setVisibility(View.GONE);
        }else {
            holder.ll_delete.setVisibility(View.VISIBLE);
        }

        holder.id_part_fav.setText(lstLove.get(position).getSuraid()+""); //Số phần
        holder.item_num_love.setText(lstLove.get(position).getVerseid()+" "+mContext.getResources().getString(R.string.verse));//Số trang thích

        int pos = lstLove.get(position).getSuraid() - 1;
        switch (language) {
            case 1:
                holder.item_name_part_fav.setText(lstSura.get(pos).getName_ind());
                break;
            case 2:
                holder.item_name_part_fav.setText(lstSura.get(pos).getName_english());
                break;
            case 3:
                holder.item_name_part_fav.setText(lstSura.get(pos).getName_tur());
                break;
            case 4:
                holder.item_name_part_fav.setText(lstSura.get(pos).getName_fra());
                break;
            case 5:
                holder.item_name_part_fav.setText(lstSura.get(pos).getName_ger());
                break;
            case 6:
                holder.item_name_part_fav.setText(lstSura.get(pos).getName_ita());
                break;
            case 7:
                holder.item_name_part_fav.setText(lstSura.get(pos).getName_rus());
                break;
            case 8:
                holder.item_name_part_fav.setText(lstSura.get(pos).getName_som());
                break;
            case 9:
                holder.item_name_part_fav.setText(lstSura.get(pos).getName_spa());
                break;
            default:
                holder.item_name_part_fav.setText(lstSura.get(pos).getName_ind());
                break;
        }

        if (lstSura.get(pos).getPlace() == 1) {
            holder.item_info_ver.setText(QuranRules.ARR_SIZE_PART[pos]+" "+mContext.getResources().getString(R.string.verse)+", Makkia");
        } else {
            holder.item_info_ver.setText(QuranRules.ARR_SIZE_PART[pos]+" "+mContext.getResources().getString(R.string.verse)+", Madinia");
        }

    }

    @Override
    public int getItemCount() {
        return lstLove.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id_part_fav, item_name_part_fav, item_info_ver, item_num_love;
        LinearLayout ll_delete;
        ImageButton item_fav_delete;

        public ViewHolder(final View itemView) {
            super(itemView);
            dbh= new DatabaseHelper(mContext);

            id_part_fav = itemView.findViewById(R.id.id_part_fav);
            item_name_part_fav = itemView.findViewById(R.id.item_name_part_fav);
            item_info_ver = itemView.findViewById(R.id.item_info_ver);
            item_num_love = itemView.findViewById(R.id.item_num_love);

            ll_delete = itemView.findViewById(R.id.ll_delete);
            item_fav_delete = itemView.findViewById(R.id.item_fav_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = lstLove.get(getAdapterPosition()).getSuraid();
                    if (listener != null)
                        listener.onItemClick(itemView, getLayoutPosition(), id);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int id = lstLove.get(getAdapterPosition()).getSuraid();
                    if (listener1 != null)
                        listener1.onItemClick(itemView, getAdapterPosition(), id);
                    return false;
                }
            });

            item_fav_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog= new Dialog(mContext,R.style.mydialogstyle);
                    dialog.setContentView(R.layout.dialog_delete);

                    Button btn_ok=dialog.findViewById(R.id.df_ok);
                    Button btn_cancel=dialog.findViewById(R.id.df_cancel);

                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dbh.DeleteFavorite(lstLove.get(getAdapterPosition()));
                            lstLove.remove(lstLove.get(getAdapterPosition()));
                            delete=-1;
                            notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });

        }
    }
}
