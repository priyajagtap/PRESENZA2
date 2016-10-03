package com.infogird.www.presenza;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Data_Model.Subject_Details_Att;

/**
 * Created by infogird31 on 02/09/2016.
 */
public class subject_cv_adapter extends RecyclerView.Adapter<subject_cv_adapter.MyViewHolder> {

    private ArrayList<Subject_Details_Data_Model> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        TextView Sub_Name;
        TextView Sub_Type;
        TextView Sub_class;
        TextView Sub_batch;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.Sub_Name = (TextView)itemView.findViewById(R.id.tv_sub_name_Cv);
            this.Sub_Type = (TextView) itemView.findViewById(R.id.tv_sub_type_cv);
            this.Sub_class = (TextView)itemView.findViewById(R.id.tv_sub_class_cv);
            this.Sub_batch = (TextView) itemView.findViewById(R.id.tv_sub_batch_cv);

            cardView = (CardView) itemView.findViewById(R.id.card_view_subject);
        }
    }

    public subject_cv_adapter(ArrayList<Subject_Details_Data_Model> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_subject, parent, false);

        view.setOnClickListener(Drawer_Activity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView tv_sub_name= holder.Sub_Name;
        TextView tv_sub_type = holder.Sub_Type;
        TextView tv_sub_class= holder.Sub_class;
        TextView tv_sub_batch = holder.Sub_batch;

        tv_sub_name.setText(dataSet.get(listPosition).getSub_name());
        tv_sub_type.setText(dataSet.get(listPosition).getTheory_or_practical());
        tv_sub_class.setText(dataSet.get(listPosition).getSub_Class()+"");
        tv_sub_batch.setText(dataSet.get(listPosition).getBatch());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Log","clicked on"+dataSet.get(listPosition).getSub_name());
                //implement onClick
                Intent intent_take_Attendence = new Intent(v.getContext(),Take_Attendence_Activity.class);
                intent_take_Attendence.putExtra("Sub_Name",dataSet.get(listPosition).getSub_name()+"");
                intent_take_Attendence.putExtra("Sub_class",dataSet.get(listPosition).getSub_Class());

                Subject_Details_Att adt2 = new Subject_Details_Att();
                adt2.setSub_Name(dataSet.get(listPosition).getSub_name());
                adt2.setSub_class(dataSet.get(listPosition).getSub_Class());


                Log.i("Log","values passed to take atte"+dataSet.get(listPosition).getSub_name()+dataSet.get(listPosition).getSub_Class());
                v.getContext().startActivity(intent_take_Attendence);

            }
        });


/*//new code to swipe

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDirection) {

                if (swipeDirection == ItemTouchHelper.LEFT){
                   Log.i("swipe","swipe left");
                    stud_ar_list.remove(viewHolder.getAdapterPosition());
                    adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                } else if(swipeDirection == ItemTouchHelper.RIGHT) {
                    Log.i("swipe","swipe right");
                    stud_ar_list.remove(viewHolder.getAdapterPosition());
                    adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                }
            }

            @Override
            public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

                return super.getMovementFlags(recyclerView, viewHolder);
            }
*/


        }



    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
