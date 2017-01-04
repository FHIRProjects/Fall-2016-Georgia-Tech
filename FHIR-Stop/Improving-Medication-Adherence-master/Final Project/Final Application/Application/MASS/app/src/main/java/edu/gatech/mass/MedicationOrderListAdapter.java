package edu.gatech.mass;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import edu.gatech.mass.model.MedicationOrder;

public class MedicationOrderListAdapter extends ArrayAdapter<MedicationOrder> {

    private List<MedicationOrder> medicationOrders;

    public MedicationOrderListAdapter(Context context, int resource, List<MedicationOrder> objects) {
        super(context, resource, objects);
        medicationOrders = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_medication_order, parent, false);
        }

        MedicationOrder medicationOrder = medicationOrders.get(position);

        TextView nameText = (TextView) convertView.findViewById(R.id.nameText);
        nameText.setText(medicationOrder.getMedicationShortName());

        TextView doseText = (TextView) convertView.findViewById(R.id.doseText);
        doseText.setText(medicationOrder.getDoseQuantityWithUnits());

        ImageView iv = (ImageView) convertView.findViewById(R.id.imageView);
        Bitmap bitmap = getBitmapFromAsset(medicationOrder.getMedicationCode());
        iv.setImageBitmap(bitmap);

        return convertView;
    }

    private Bitmap getBitmapFromAsset(String medicationCode) {
        AssetManager assetManager = getContext().getAssets();
        InputStream stream = null;

        try {
            stream = assetManager.open(medicationCode + ".jpeg");
            return BitmapFactory.decodeStream(stream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}