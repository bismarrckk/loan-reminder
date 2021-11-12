package com.example.loansbookkeeping;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    TextView nameTxt,posTxt;
    ImageView imageViewDelete,imageconfirm;
    ItemClickListener itemClickListener;
    ConstraintLayout confirm_linear;
    CardView card_confirm;


    public MyHolder(View itemView) {
        super(itemView);

        //ASSIGN

        card_confirm=itemView.findViewById(R.id.card_confirm);
        confirm_linear=itemView.findViewById(R.id.constraint_confirm);
        nameTxt=  itemView.findViewById(R.id.nameTxt);
        posTxt=  itemView.findViewById(R.id.posTxt);
        imageViewDelete=itemView.findViewById(R.id.imageViewDelete);
        imageconfirm=itemView.findViewById(R.id.imageConfirm);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(v,getLayoutPosition());
    }

    public void setItemClickListener(ItemClickListener ic)
    {
        this.itemClickListener=ic;
    }
}
