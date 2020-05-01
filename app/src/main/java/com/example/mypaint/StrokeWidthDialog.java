package com.example.mypaint;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class StrokeWidthDialog extends AppCompatDialogFragment {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    float chosenStrokeWidth;
    private StrokeWidthDialog.StrokeWidthDialogListner strokeWidthDialogListner;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.activity_strokewidth, null);
        builder.setView(view)
                .setTitle("StrokeWidth")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        radioGroup = view.findViewById(R.id.radioGroup2);
                        radioButton = view.findViewById(radioGroup.getCheckedRadioButtonId());
                        if(radioButton != null)
                        {
                            chosenStrokeWidth = Float.parseFloat(radioButton.getText().toString());
                            strokeWidthDialogListner.changeStrokeWidth(chosenStrokeWidth);
                        }

                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            strokeWidthDialogListner = (StrokeWidthDialog.StrokeWidthDialogListner) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface StrokeWidthDialogListner{
        void changeStrokeWidth(float chosenStrokeWidth);
    }

}
