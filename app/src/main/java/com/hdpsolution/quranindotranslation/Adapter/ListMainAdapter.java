package com.hdpsolution.quranindotranslation.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hdpsolution.quranindotranslation.Database.DatabaseQuran;
import com.hdpsolution.quranindotranslation.Entity.Sura_Quran;
import com.hdpsolution.quranindotranslation.QuranRules;
import com.hdpsolution.quranindotranslation.R;

import java.util.ArrayList;

public class ListMainAdapter extends RecyclerView.Adapter<ListMainAdapter.ViewHolder> {

    Context mContext;
    int language;
    private ArrayList<Sura_Quran> lstSura;

    public ListMainAdapter(Context mContext, ArrayList<Sura_Quran> lstSura, int language) {
        this.mContext = mContext;
        this.lstSura = lstSura;
        this.language = language;
    }

    private static OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position, int id);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void changedLanguage(int lang){
        language=lang;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_quran, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.id_part.setText((position+1)+"");
        holder.id_part.setText(lstSura.get(position).getSuraid() + "");
        //Tên nước nào lấy nước đó
        switch (language) {
            case 1:
                holder.item_name_part.setText(lstSura.get(position).getName_ind());
                break;
            case 2:
                holder.item_name_part.setText(lstSura.get(position).getName_english());
                break;
            case 3:
                holder.item_name_part.setText(lstSura.get(position).getName_tur());
                break;
            case 4:
                holder.item_name_part.setText(lstSura.get(position).getName_fra());
                break;
            case 5:
                holder.item_name_part.setText(lstSura.get(position).getName_ger());
                break;
            case 6:
                holder.item_name_part.setText(lstSura.get(position).getName_ita());
                break;
            case 7:
                holder.item_name_part.setText(lstSura.get(position).getName_rus());
                break;
            case 8:
                holder.item_name_part.setText(lstSura.get(position).getName_som());
                break;
            case 9:
                holder.item_name_part.setText(lstSura.get(position).getName_spa());
                break;
            default:
                holder.item_name_part.setText(lstSura.get(position).getName_ind());
                break;
        }

        holder.item_name_part_def.setText(lstSura.get(position).getName());

        if (lstSura.get(position).getPlace() == 1) {
            holder.item_info_part.setText(QuranRules.ARR_SIZE_PART[position]+" "+mContext.getResources().getString(R.string.verse)+", Makkia");
        } else {
            holder.item_info_part.setText(QuranRules.ARR_SIZE_PART[position]+" "+mContext.getResources().getString(R.string.verse)+", Madinia");
        }


    }

    @Override
    public int getItemCount() {
        return lstSura.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id_part, item_name_part, item_name_part_def, item_info_part;

        public ViewHolder(final View itemView) {
            super(itemView);

            id_part = itemView.findViewById(R.id.id_part);
            item_name_part = itemView.findViewById(R.id.item_name_part);
            item_name_part_def = itemView.findViewById(R.id.item_name_part_def);
            item_info_part = itemView.findViewById(R.id.item_info_part);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int idSura = lstSura.get(getAdapterPosition()).getSuraid();
                    if (listener != null)
                        listener.onItemClick(itemView, getLayoutPosition(), idSura);
                }
            });
        }
    }
}
