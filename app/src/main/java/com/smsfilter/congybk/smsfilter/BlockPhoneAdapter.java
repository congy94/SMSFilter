package com.smsfilter.congybk.smsfilter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by congybk on 22/06/2016.
 */
public class BlockPhoneAdapter extends RecyclerView.Adapter<BlockPhoneAdapter.BlockPhoneViewHolder> {
    private Context mContext;
    private List<BlockPhone> mListBlockPhone;
    private OnClickListener mOnClickListener;
    public BlockPhoneAdapter(Context context,List<BlockPhone> listBlockPhone,OnClickListener onClickListener){
        this.mContext = context;
        this.mListBlockPhone = listBlockPhone;
        this.mOnClickListener = onClickListener;
    }

    @Override
    public BlockPhoneViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_blocklist, viewGroup, false);
        BlockPhoneViewHolder blockPhoneViewHolder = new BlockPhoneViewHolder(v);
        return blockPhoneViewHolder;
    }

    @Override
    public void onBindViewHolder(BlockPhoneViewHolder holder, final int i) {
        holder.tvFirtName.setText((mListBlockPhone.get(i).getName().charAt(0)+"").toUpperCase());
        holder.tvName.setText(mListBlockPhone.get(i).getName());
        holder.tvPhone.setText(mListBlockPhone.get(i).getNumber());
        holder.tvReason.setText(mListBlockPhone.get(i).getReason());
        holder.cv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mOnClickListener.onClick(view,i);
                return false;
            }
        });
    }
    public interface OnClickListener{
       void  onClick(View view,int position);
    }

    @Override
    public int getItemCount() {
        return mListBlockPhone.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class BlockPhoneViewHolder extends RecyclerView.ViewHolder {
         CardView cv;
         TextView tvFirtName;
         TextView tvName;
         TextView tvPhone;
         TextView tvReason;

        public BlockPhoneViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            tvFirtName = (TextView)itemView.findViewById(R.id.tvFirtName);
            tvName = (TextView)itemView.findViewById(R.id.tvName);
            tvPhone = (TextView)itemView.findViewById(R.id.tvPhone);
            tvReason = (TextView)itemView.findViewById(R.id.tvReason);
        }
    }
}
