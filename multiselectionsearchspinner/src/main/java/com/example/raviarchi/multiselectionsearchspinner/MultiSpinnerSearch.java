package com.example.raviarchi.multiselectionsearchspinner;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiSpinnerSearch extends Spinner implements OnCancelListener {
    private static final String TAG = MultiSpinnerSearch.class.getSimpleName();
    public static AlertDialog.Builder builder;
    public static AlertDialog ad;
    MyAdapter adapter;
    private ArrayList<String> items;
    private String defaultText = "";
    private String spinnerTitle = "";
    private SpinnerListener listener;
    private int limit = 0;
    private int selected = 0;
    private LimitExceedListener limitListener;


    public MultiSpinnerSearch(Context context) {
        super(context);

    }

    public MultiSpinnerSearch(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);

        TypedArray a = arg0.obtainStyledAttributes(arg1, R.styleable.MultiSpinnerSearch);
        limit = a.getIndexCount();
        for (int i = 0; i < limit; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.MultiSpinnerSearch_hintText) {
                spinnerTitle = a.getString(attr);
                defaultText = spinnerTitle;
                break;
            }
        }
        Log.i(TAG, "spinnerTitle: " + spinnerTitle);
        a.recycle();
    }

    public MultiSpinnerSearch(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }



    public void setLimit(int limit, LimitExceedListener listener) {
        this.limit = limit;
        this.limitListener = listener;
    }

    public List<String> getSelectedItems() {
        List<String> selectedItems = new ArrayList<>();
        for (String item : items) {
            // if (item.isSelected()) {
            selectedItems.add(item);
            //}
        }
        return selectedItems;
    }

    public List<String> getSelectedIds() {
        /*List<String> selectedItemsIds = new ArrayList<>();
        for (UserProfileDetails item : items) {
            if (item.isSelected()) {
                selectedItemsIds.add(item.getUserInterestId());
            }
        }
        return selectedItemsIds;*/
        return null;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // refresh text on spinner

        StringBuilder spinnerBuffer = new StringBuilder();

        for (int i = 0; i < items.size(); i++) {
            // if (items.get(i).isSelected()) {
            spinnerBuffer.append(items.get(i));
            spinnerBuffer.append(",");
            //  }
        }

        String spinnerText = spinnerBuffer.toString();
        if (spinnerText.length() > 2)
            spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
        else
            spinnerText = defaultText;

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getContext(), R.layout.textview_for_spinner, new String[]{spinnerText});
        setAdapter(adapterSpinner);

        if (adapter != null)
            adapter.notifyDataSetChanged();

        listener.onItemsSelected(items);
    }

    @Override
    public boolean performClick() {

        builder = new AlertDialog.Builder(getContext(), R.style.myDialog);
        builder.setTitle(spinnerTitle);

        final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View view = inflater.inflate(R.layout.alert_dialog_listview_search, null);
        builder.setView(view);

        final ListView listView = (ListView) view.findViewById(R.id.alertSearchListView);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setFastScrollEnabled(false);
        adapter = new MyAdapter(getContext(), items);
        listView.setAdapter(adapter);

        final TextView emptyText = (TextView) view.findViewById(R.id.empty);
        listView.setEmptyView(emptyText);

        final EditText editText = (EditText) view.findViewById(R.id.alertSearchEditText);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Log.i(TAG, " ITEMS : " + items.size());
                dialog.cancel();
            }
        });

        builder.setOnCancelListener(this);
        ad = builder.show();
        return true;
    }

    public void setItems(ArrayList<String> items, int position, SpinnerListener listener) {

        this.items = items;
        this.listener = listener;

        StringBuilder spinnerBuffer = new StringBuilder();

        for (int i = 0; i < items.size(); i++) {
            // _items = items.toArray(new String[items.size()]);

            if (items.size() > 0) {
                spinnerBuffer.append(items.get(0));
                spinnerBuffer.append(", ");
                Arrays.fill(new boolean[]{items.get(i).length() < 0}, false);
            }
        }

      /*  _items = items.toArray(new String[items.size()]);
        mSelection = new boolean[_items.length];
        mSelectionAtStart = new boolean[_items.length];
        simple_adapter.clear();
        simple_adapter.add(_items[0]);
        Arrays.fill(mSelection, false);
        mSelection[0] = true;*/

        if (spinnerBuffer.length() > 2)
            defaultText = spinnerBuffer.toString().substring(0, spinnerBuffer.toString().length() - 2);

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getContext(), R.layout.textview_for_spinner, new String[]{defaultText});
        setAdapter(adapterSpinner);

        if (position != -1) {
            items.get(position);
            listener.onItemsSelected(items);
            onCancel(null);
        }
    }

    public interface LimitExceedListener {
        void onLimitListener(List<String> data);
    }

    //Adapter Class
    public class MyAdapter extends BaseAdapter implements Filterable {

        ArrayList<String> arrayList;
        List<String> mOriginalValues; // Original Values
        LayoutInflater inflater;

        public MyAdapter(Context context, ArrayList<String> arrayList) {
            this.arrayList = arrayList;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Log.i(TAG, "getView() enter");
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_listview_multiple, parent, false);
                holder.textView = (TextView) convertView.findViewById(R.id.alertTextView);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.alertCheckbox);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final int backgroundColor = (position % 2 == 0) ? R.color.list_even : R.color.list_odd;
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), backgroundColor));


           /* holder.textView.setText(data.getUserInterestName());
            holder.textView.setTypeface(null, Typeface.NORMAL);
            holder.checkBox.setChecked(data.isSelected());
*/
            convertView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (arrayList.get(position).length() > 0) { // unselect
                        selected--;
                    } else if (selected == limit) { // select with limit
                        if (limitListener != null)
                            limitListener.onLimitListener(arrayList);
                        return;
                    } else { // selected
                        selected++;
                    }

                    final ViewHolder temp = (ViewHolder) v.getTag();
                    temp.checkBox.setChecked(!temp.checkBox.isChecked());

                    //data.setSelected(!data.isSelected());
                    //Log.i(TAG, "On Click Selected Item : " + data.getUserInterestName() + " : " + data.isSelected());
                }
            });
            //   if (data.isSelected()) {
            holder.textView.setTypeface(null, Typeface.BOLD);
            holder.textView.setTextColor(Color.WHITE);
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.list_selected));
            // }
            holder.checkBox.setTag(holder);

            return convertView;
        }

        @SuppressLint("DefaultLocale")
        @Override
        public Filter getFilter() {
            return new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    arrayList = (ArrayList<String>) results.values; // has the filtered values
                    notifyDataSetChanged();  // notifies the data with new filtered values
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                    List<String> FilteredArrList = new ArrayList<>();

                    if (mOriginalValues == null) {
                        mOriginalValues = new ArrayList<>(arrayList); // saves the original data in mOriginalValues
                    }

                    /********
                     *
                     *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                     *  else does the Filtering and returns FilteredArrList(Filtered)
                     *
                     ********/
                    if (constraint == null || constraint.length() == 0) {

                        // set the Original result to return
                        results.count = mOriginalValues.size();
                        results.values = mOriginalValues;
                    } else {
                        constraint = constraint.toString().toLowerCase();
                        for (int i = 0; i < mOriginalValues.size(); i++) {
                            //  Log.i(TAG, "Filter : " + mOriginalValues.get(i).getUserInterestName() + " -> " + mOriginalValues.get(i).isSelected());
                            String data = mOriginalValues.get(i);
                            if (data.toLowerCase().contains(constraint.toString())) {
                                FilteredArrList.add(mOriginalValues.get(i));
                            }
                        }
                        // set the Filtered result to return
                        results.count = FilteredArrList.size();
                        results.values = FilteredArrList;
                    }
                    return results;
                }
            };
        }

        private class ViewHolder {
            TextView textView;
            CheckBox checkBox;
        }
    }
}