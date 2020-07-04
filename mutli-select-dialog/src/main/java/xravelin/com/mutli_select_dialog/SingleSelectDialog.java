package xravelin.com.mutli_select_dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

public class SingleSelectDialog extends AppCompatDialogFragment implements SearchView.OnQueryTextListener, View.OnClickListener {

    public static String selectedIdForCallback;

    public ArrayList<MultiSelectModel> mainListOfAdapter = new ArrayList<>();
    private SingleSelectAdapter singleSelectAdapter;
    //Default Values
    private String title;
    private float titleSize = 25;
    private String positiveText = "DONE";
    private String negativeText = "CANCEL";
    private TextView dialogTitle, dialogSubmit, dialogCancel;
    private RelativeLayout _addRl;
    private TextView _addButton, _addLabel,_addError;
    private EditText _addName;
    private ProgressBar progress;
    private String previouslySelectedId= null;


    private ArrayList<MultiSelectModel> tempMainListOfAdapter = new ArrayList<>();

    private SingleSelectDialog.SubmitCallbackListener submitCallbackListener;
    private SingleSelectDialog.ViewRenderedListener viewRenderedListener;

    private int minSelectionLimit = 1;
    private String minSelectionMessage = null;
    private int maxSelectionLimit = 0;
    private String maxSelectionMessage = null;

    private RecyclerViewEmptySupport mrecyclerView;

    private boolean addNewButton;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dialog.setContentView(R.layout.custom_multi_select);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mrecyclerView=  dialog.findViewById(R.id.recycler_view);
        SearchView searchView =  dialog.findViewById(R.id.search_view);
        dialogTitle =  dialog.findViewById(R.id.title);
        dialogSubmit =  dialog.findViewById(R.id.done);
        dialogCancel =  dialog.findViewById(R.id.cancel);



        _addRl =  dialog.findViewById(R.id.addRl);
        _addName =  dialog.findViewById(R.id.addName);
        _addButton =  dialog.findViewById(R.id.addButton);
        _addLabel =  dialog.findViewById(R.id.addLabel);
        _addError =  dialog.findViewById(R.id.addError);
        progress =  dialog.findViewById(R.id.progress);


        mrecyclerView.setEmptyView(dialog.findViewById(R.id.list_empty1));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mrecyclerView.setLayoutManager(layoutManager);



        dialogSubmit.setOnClickListener(this);
        dialogCancel.setOnClickListener(this);
        _addButton.setOnClickListener(this);

        settingValues();

        //mainListOfAdapter = setCheckedIDS(mainListOfAdapter, previouslySelectedId);
        singleSelectAdapter = new SingleSelectAdapter(mainListOfAdapter, getContext());
        mrecyclerView.setAdapter(singleSelectAdapter);

        searchView.setOnQueryTextListener(this);
        searchView.onActionViewExpanded();
        searchView.clearFocus();


        return dialog;
    }

    public SingleSelectDialog title(String title) {

        this.title = title;
        return this;
    }

    public SingleSelectDialog showAddNewButton(boolean addNewButton){
        this.addNewButton=addNewButton;
        return this;
    }


    public SingleSelectDialog titleSize(float titleSize) {
        this.titleSize = titleSize;
        return this;
    }

    public SingleSelectDialog positiveText(@NonNull String message) {
        this.positiveText = message;
        return this;
    }

    public SingleSelectDialog negativeText(@NonNull String message) {
        this.negativeText = message;
        return this;
    }

    public SingleSelectDialog preSelectedId(String preSelectedId) {
        this.previouslySelectedId = preSelectedId;
        return this;
    }



    public SingleSelectDialog multiSelectList(ArrayList<MultiSelectModel> list) {
        this.mainListOfAdapter = list;
        this.tempMainListOfAdapter = new ArrayList<>(mainListOfAdapter);
        if(maxSelectionLimit == 0)
            maxSelectionLimit = list.size();
        return this;
    }


    public SingleSelectDialog onSubmit(@NonNull SingleSelectDialog.SubmitCallbackListener callback) {
        this.submitCallbackListener = callback;
        return this;
    }

    public SingleSelectDialog onAdd(@NonNull SingleSelectDialog.ViewRenderedListener callback) {
        this.viewRenderedListener = callback;
        return this;
    }

    private void settingValues() {
        dialogTitle.setText(title);
        dialogTitle.setTextSize(titleSize);
        dialogSubmit.setText(positiveText.toUpperCase());
        dialogCancel.setText(negativeText.toUpperCase());

        if(addNewButton){
            _addButton.setVisibility(View.VISIBLE);
        }
        else{
            _addButton.setVisibility(View.GONE);
        }


    }

    private ArrayList<MultiSelectModel> setCheckedIDS(ArrayList<MultiSelectModel> multiselectdata, String selectedId) {
        for (int i = 0; i < multiselectdata.size(); i++) {
            multiselectdata.get(i).setSelected(false);
           if(selectedId!=null){
               if (selectedId.equals(multiselectdata.get(i).getId())) {
                   multiselectdata.get(i).setSelected(true);
               }
           }
        }
        return multiselectdata;
    }

    private ArrayList<MultiSelectModel> filter(ArrayList<MultiSelectModel> models, String query) {
        query = query.toLowerCase();
        final ArrayList<MultiSelectModel> filteredModelList = new ArrayList<>();
        if (query.equals("") | query.isEmpty()) {
            filteredModelList.addAll(models);
            return filteredModelList;
        }

        for (MultiSelectModel model : models) {
            final String name = model.getName().toLowerCase();
            if (name.contains(query)) {
                filteredModelList.add(model);
            }
        }


        return filteredModelList;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        selectedIdForCallback = previouslySelectedId;
        mainListOfAdapter = setCheckedIDS(mainListOfAdapter, selectedIdForCallback);
        ArrayList<MultiSelectModel> filteredlist = filter(mainListOfAdapter, newText);
        singleSelectAdapter.setData(filteredlist, newText.toLowerCase(), singleSelectAdapter);
        return false;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.done) {
            if(selectedIdForCallback==null){
                Toast.makeText(getActivity(), "Please select at least one option", Toast.LENGTH_LONG).show();
            }else{
                if(submitCallbackListener !=null) {
                    submitCallbackListener.onSelected(selectedIdForCallback, getSelectedName());
                }
                dismiss();
            }
        }

        if (view.getId() == R.id.cancel) {
            if(submitCallbackListener!=null){
                submitCallbackListener.onCancel();
            }
            dismiss();
        }

        if (view.getId() == R.id.addButton) {
            if(viewRenderedListener!=null){
                _addRl.setVisibility(View.VISIBLE);
                viewRenderedListener.onAdded(_addRl,_addName,_addButton,_addLabel,_addError,progress,this);
            }

        }
    }

    public void notifyAdapter(){
        singleSelectAdapter = new SingleSelectAdapter(mainListOfAdapter, getContext());
        mrecyclerView.setAdapter(singleSelectAdapter);
        singleSelectAdapter.notifyDataSetChanged();
    }


    private String getSelectedName() {
        String name=null;
        for(int i=0;i<tempMainListOfAdapter.size();i++){
            if(checkForSelection(tempMainListOfAdapter.get(i).getId())){
                name =tempMainListOfAdapter.get(i).getName();
            }
        }
        return name;
    }

    private boolean checkForSelection(String id) {
        if (id.equals(selectedIdForCallback)) {
            return true;
        }
        return false;
    }

   /* public void setCallbackListener(SubmitCallbackListener submitCallbackListener) {
        this.submitCallbackListener = submitCallbackListener;
    }*/

    public interface SubmitCallbackListener {
        void onSelected(String selectedId,String selectedName);
        void onCancel();
    }

    public interface ViewRenderedListener{
        void onAdded(RelativeLayout addRl, EditText addName,TextView addButton,TextView addLabel,TextView addError,ProgressBar progress,SingleSelectDialog sig
        );
    }

}

