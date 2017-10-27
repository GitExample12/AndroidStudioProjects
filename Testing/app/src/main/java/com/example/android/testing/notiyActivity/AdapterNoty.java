package com.example.android.testing.notiyActivity;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.testing.R;

import java.util.List;

/**
 * Created by Android on 5/18/2017.
 */

public class AdapterNoty extends ArrayAdapter<ModelNoty> {
    public AdapterNoty(@NonNull Context context, @LayoutRes int resource, @NonNull List<ModelNoty> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.list_notify, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.list_noty_title);
        TextView message = (TextView) convertView.findViewById(R.id.list_noty_message);

        ModelNoty mn = getItem(position);

        title.setText(mn.getTitle());
        message.setText(mn.getMessage());

        return convertView;
    }
}
