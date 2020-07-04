package xravelin.com.mutli_select_dialog;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SingleSelectAdapter extends RecyclerView.Adapter<SingleSelectAdapter.SingleSelectDialogViewHolder> {

    private ArrayList<MultiSelectModel> mDataSet = new ArrayList<>();
    private String mSearchQuery = "";
    private Context mContext;
    private int lastCheckedPosition=0;
    private AppCompatCheckBox lastCheckBox;
    SingleSelectAdapter(ArrayList<MultiSelectModel> dataSet, Context context) {
        this.mDataSet = dataSet;
        this.mContext = context;
    }

    @Override
    public SingleSelectAdapter.SingleSelectDialogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.multi_select_item, parent, false);
        return new SingleSelectAdapter.SingleSelectDialogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SingleSelectAdapter.SingleSelectDialogViewHolder holder, int position) {

        if (!mSearchQuery.equals("") && mSearchQuery.length() > 1) {
            setHighlightedText(position, holder.dialog_name_item);
        } else {
            holder.dialog_name_item.setText(mDataSet.get(position).getName());
        }



      /*  if (checkForSelection(mDataSet.get(position).getId())) {
            holder.dialog_item_checkbox.setChecked(true);
        } else {
            holder.dialog_item_checkbox.setChecked(false);
        }*/

        /*holder.dialog_item_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.dialog_item_checkbox.isChecked()) {
                    MultiSelectDialog.selectedIdsForCallback.add(mDataSet.get(holder.getAdapterPosition()).getId());
                    holder.dialog_item_checkbox.setChecked(true);
                } else {
                    removeFromSelection(mDataSet.get(holder.getAdapterPosition()).getId());
                    holder.dialog_item_checkbox.setChecked(false);
                }
            }
        });*/


        holder.main_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatCheckBox appCompatCheckBox=holder.dialog_item_checkbox;
                lastCheckedPosition=holder.getAdapterPosition();

                if(!appCompatCheckBox.isChecked()){
                    if(lastCheckBox != null)
                    {
                        lastCheckBox.setChecked(false);
                        //removeFromSelection(mDataSet.get(lastCheckedPosition).getId());
                        mDataSet.get(lastCheckedPosition).setSelected(false);
                        //notifyItemChanged(lastCheckedPosition);
                    }
                    SingleSelectDialog.selectedIdForCallback=mDataSet.get(holder.getAdapterPosition()).getId();
                    appCompatCheckBox.setChecked(true);
                    mDataSet.get(holder.getAdapterPosition()).setSelected(true);
                    //notifyItemChanged(holder.getAdapterPosition());

                    lastCheckBox = appCompatCheckBox;
                    lastCheckedPosition = holder.getAdapterPosition();

                }/*else{
                    lastCheckBox.setChecked(false);
                    removeFromSelection(mDataSet.get(lastCheckedPosition).getId());
                    mDataSet.get(lastCheckedPosition).setSelected(false);
                    notifyItemChanged(lastCheckedPosition);
                }*/



                /*if (!holder.dialog_item_checkbox.isChecked()) {
                    MultiSelectDialog.selectedIdsForCallback.add(mDataSet.get(holder.getAdapterPosition()).getId());
                    holder.dialog_item_checkbox.setChecked(true);
                    mDataSet.get(holder.getAdapterPosition()).setSelected(true);
                    notifyItemChanged(holder.getAdapterPosition());

                    lastCheckBox=holder.dialog_item_checkbox;
                    lastCheckedPosition=holder.getAdapterPosition();
                } else {
                    removeFromSelection(mDataSet.get(holder.getAdapterPosition()).getId());
                    removeFromSelection(mDataSet.get(lastCheckedPosition).getId());
                    holder.dialog_item_checkbox.setChecked(false);
                    mDataSet.get(holder.getAdapterPosition()).setSelected(false);
                    notifyItemChanged(holder.getAdapterPosition());
                }*/
            }
        });
    }

    private void setHighlightedText(int position, TextView textview) {
        String name = mDataSet.get(position).getName();
        SpannableString str = new SpannableString(name);
        int endLength = name.toLowerCase().indexOf(mSearchQuery) + mSearchQuery.length();
        ColorStateList highlightedColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{ContextCompat.getColor(mContext, R.color.colorAccent)});
        TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(null, Typeface.NORMAL, -1, highlightedColor, null);
        str.setSpan(textAppearanceSpan, name.toLowerCase().indexOf(mSearchQuery), endLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textview.setText(str);
    }

    private void removeFromSelection(String id) {
        /*for (int i = 0; i < SingleSelectDialog.selectedIdsForCallback.size(); i++) {
            if (id.equals(SingleSelectDialog.selectedIdsForCallback.get(i))) {
                SingleSelectDialog.selectedIdsForCallback.remove(i);
            }
        }*/
    }


    private boolean checkForSelection(String id) {
        /*for (int i = 0; i < SingleSelectDialog.selectedIdsForCallback.size(); i++) {
            if (id.equals(SingleSelectDialog.selectedIdsForCallback.get(i))) {
                return true;
            }
        }*/
        return false;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    void setData(ArrayList<MultiSelectModel> data, String query, SingleSelectAdapter singleSelectAdapter) {
        this.mDataSet = data;
        this.mSearchQuery = query;
        singleSelectAdapter.notifyDataSetChanged();
    }

    class SingleSelectDialogViewHolder extends RecyclerView.ViewHolder {
        private TextView dialog_name_item;
        private AppCompatCheckBox dialog_item_checkbox;
        private LinearLayout main_container;

        SingleSelectDialogViewHolder(View view) {
            super(view);
            dialog_name_item = (TextView) view.findViewById(R.id.dialog_item_name);
            dialog_item_checkbox = (AppCompatCheckBox) view.findViewById(R.id.dialog_item_checkbox);
            main_container = (LinearLayout) view.findViewById(R.id.main_container);
        }
    }
}

