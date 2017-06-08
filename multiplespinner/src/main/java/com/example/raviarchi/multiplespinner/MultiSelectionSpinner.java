package com.example.raviarchi.multiplespinner;

/** * Created by archi on 03-Mar-17.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MultiSelectionSpinner extends android.support.v7.widget.AppCompatSpinner implements
        OnMultiChoiceClickListener, Filterable {
    public OnMultipleItemsSelectedListener listener;
    ListView listView;
    String[] _items = null;
    String[] _search_items = null;
    boolean[] mSelection = null;
    ArrayList<String> searchItems;
    boolean[] mSelectionAtStart = null;
    String _itemsAtStart = null;
    ArrayAdapter<String> simple_adapter;
    List<String> mOriginalValues;
    EditText editSearch;
    TextView emptyText;

    public MultiSelectionSpinner(Context context) {
        super(context);

        simple_adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, _items);
        super.setAdapter(simple_adapter);
    }

    public MultiSelectionSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        simple_adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item);
        super.setAdapter(simple_adapter);
    }

    public void setListener(OnMultipleItemsSelectedListener listener) {
        this.listener = listener;
    }


    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

        if (mSelection != null && which < mSelection.length) {
            if (getSelectedIndices().size() > 1) {
                if (getSelectedIndices().size() < 4) {
                    mSelection[which] = isChecked;
                    simple_adapter.clear();
                    simple_adapter.add(buildSelectedItemString());
                } else {
                    Toast.makeText(getContext(), "You can select maximum 3 Interests", Toast.LENGTH_SHORT).show();
                    setDeselection(which);
                }
            } else {
                Toast.makeText(getContext(), "Please select minimum 1 Interests", Toast.LENGTH_SHORT).show();
                setAllowDeselection(which);
            }
        } else {
            throw new IllegalArgumentException(
                    "Argument 'which' is out of bounds.");
        }

        Log.d("which", "" + which);
    }

    public void setDeselection(int index) {
        Log.d(TAG, "setDeselection() called with: " + "index = [" + index + "]");

        if (index >= 0 && index < mSelection.length) {
            mSelection[index] = false;
            mSelectionAtStart[index] = false;

        } else {
            throw new IllegalArgumentException("Index " + index
                    + " is out of bounds.");
        }
        simple_adapter.clear();
        simple_adapter.notifyDataSetChanged();
        simple_adapter.add(buildSelectedItemString());

    }

    public void setAllowDeselection(int index) {
        //Log.d(TAG, "setAllowselection() called with: " + "index = [" + index + "]");
        if (index <= 0 && index < mSelection.length) {
            mSelection[index] = true;
            mSelectionAtStart[index] = true;
        }/* else {
            throw new IllegalArgumentException("Index " + index
                    + " is  of bounds.");
        }*/
        simple_adapter.clear();
        simple_adapter.notifyDataSetChanged();
        simple_adapter.add(buildSelectedItemString());

    }

    @Override
    public boolean performClick() {
        buildDialogue();
        return true;
    }

    private void buildDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.alert_dialog_listview_search, null);
        builder.setView(view);
        listView = (ListView) view.findViewById(R.id.alertSearchListView);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setFastScrollEnabled(false);

        listView.setAdapter(simple_adapter);
        simple_adapter.notifyDataSetChanged();

        emptyText = (TextView) view.findViewById(R.id.empty);
        listView.setEmptyView(emptyText);

        editSearch = (EditText) view.findViewById(R.id.alertSearchEditText);
        editSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               /* searchItems = new ArrayList<String>();
                for (int i = 0; i < _items.length; i++) {
                    if (_items[i].toLowerCase().contains(s.toString().toLowerCase())) {
                        searchItems.add(_items[i]);
                    }
                }
                _search_items = new String[searchItems.size()];
                searchItems.toArray(_search_items);
                Log.d("Array", searchItems.toString());*/
                //setItems(_search_items);
                // buildDialogue();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchItems = new ArrayList<>();
                for (int i = 0; i < _items.length; i++) {
                    if (_items[i].toLowerCase().contains(s.toString().toLowerCase())) {
                        searchItems.add(_items[i]);
                    }
                }
                simple_adapter = new ArrayAdapter<String>(getContext(), Integer.parseInt(String.valueOf(searchItems)));
                listView.setAdapter(simple_adapter);
            }
        });

        builder.setTitle("Please Select !!!");
        builder.setMultiChoiceItems(_items, mSelection, this);
        _itemsAtStart = getSelectedItemsAsString();
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.arraycopy(mSelection, 0, mSelectionAtStart, 0, mSelection.length);
                listener.selectedIndices(getSelectedIndices());
                listener.selectedStrings(getSelectedStrings());

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                simple_adapter.clear();
                simple_adapter.add(_itemsAtStart);
                System.arraycopy(mSelectionAtStart, 0, mSelection, 0, mSelectionAtStart.length);
            }
        });

        builder.show();
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        throw new RuntimeException(
                "setAdapter is not supported by MultiSelectSpinner.");
    }

    public void setItems(String[] items) {
        _items = items;
        mSelection = new boolean[_items.length];
        mSelectionAtStart = new boolean[_items.length];
        simple_adapter.clear();
        simple_adapter.add(_items[0]);
        Arrays.fill(mSelection, false);
        mSelection[0] = true;
        mSelectionAtStart[0] = true;
    }

    public void setItems(ArrayList<String> items) {
        _items = items.toArray(new String[items.size()]);
        mSelection = new boolean[_items.length];
        mSelectionAtStart = new boolean[_items.length];
        simple_adapter.clear();
        simple_adapter.add(_items[0]);
        simple_adapter.notifyDataSetChanged();
        Arrays.fill(mSelection, false);
        mSelection[0] = true;
        listener.selectedIndices(getSelectedIndices());
        listener.selectedStrings(getSelectedStrings());

    }

    public void setSelection(String[] selection) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
            mSelectionAtStart[i] = false;
        }
        for (String cell : selection) {
            for (int j = 0; j < _items.length; ++j) {
                if (_items[j].equals(cell)) {
                    mSelection[j] = true;
                    mSelectionAtStart[j] = true;
                }
            }
        }
        simple_adapter.clear();
        simple_adapter.notifyDataSetChanged();
        simple_adapter.add(buildSelectedItemString());
    }

    public void setSelection(List<String> selection) {

        _items = selection.toArray(new String[selection.size()]);
        mSelection = new boolean[_items.length];
        mSelectionAtStart = new boolean[_items.length];
        Log.d("start_size==>", "" + mSelectionAtStart.length);

        /*for (int i = 0; i <= mSelection.length; i++) {
            mSelection[i] = false;
            mSelectionAtStart[i] = false;

            Toast.makeText(getContext(), "selection[1]==>" + Arrays.toString(mSelection), Toast.LENGTH_SHORT).show();
        }

        for (String sel : selection) {

            for (int j = 0; j <= _items.length; ++j) {
                if (_items[j].equals(sel)) {
                    mSelection[j] = true;
                    mSelectionAtStart[j] = true;
                }
                Toast.makeText(getContext(), "selection[2]==>" + Arrays.toString(mSelection), Toast.LENGTH_SHORT).show();
            }
        }
        simple_adapter.clear();*/
        //simple_adapter.notifyDataSetChanged();
        // simple_adapter.add(buildSelectedItemString());
    }

    public void setSelection(int index) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
            mSelectionAtStart[i] = false;
        }
        if (index >= 0 && index < mSelection.length) {
            mSelection[index] = true;
            mSelectionAtStart[index] = true;
        } else {
            throw new IllegalArgumentException("Index " + index
                    + " is out of bounds.");
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    public void setSelection(int[] selectedIndices) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
            mSelectionAtStart[i] = false;
        }
        for (int index : selectedIndices) {
            if (index >= 0 && index < mSelection.length) {
                mSelection[index] = true;
                mSelectionAtStart[index] = true;
            } else {
                throw new IllegalArgumentException("Index " + index
                        + " is out of bounds.");
            }
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    public List<String> getSelectedStrings() {
        List<String> selectionName = new LinkedList<>();
        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                selectionName.add(_items[i]);
            }
        }
        return selectionName;
    }

    public List<Integer> getSelectedIndices() {
        List<Integer> selectionId = new LinkedList<>();
        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                selectionId.add(i);
            }
        }
        return selectionId;
    }

    private String buildSelectedItemString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;

                sb.append(_items[i]);
            }
        }
        return sb.toString();
    }

    public String getSelectedItemsAsString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;
                sb.append(_items[i]);
            }
        }
        return sb.toString();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //arrayList = (ArrayList<String>) results.values;

                _items = (String[]) results.values;
                Log.d("filter", "" + _items);
                // has the filtered values
                simple_adapter.notifyDataSetChanged(); // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();// Holds the results of a filtering operation in values
                List<String> FilteredArrList = new ArrayList<>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<>(Arrays.asList(_items));// saves the original data in mOriginalValues
                }

                if (constraint == null || constraint.length() == 0) {
                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        //  Log.i(TAG, "Filter : " + mOriginalValues.get(i).getUserInterestName() + " -> " + mOriginalValues.get(i).isSelected());
                        String data = mOriginalValues.get(i);
                        Log.d("filter_data", "" + data);
                        if (data.toLowerCase().contains(constraint.toString())) {
                            FilteredArrList.add(mOriginalValues.get(i));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                Log.d("results", "" + results);
                return results;

            }
        };
    }

    public interface OnMultipleItemsSelectedListener {
        void selectedIndices(List<Integer> indices);

        void selectedStrings(List<String> strings);
    }

}