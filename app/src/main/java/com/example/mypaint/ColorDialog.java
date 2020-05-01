package com.example.mypaint;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ColorDialog extends AppCompatDialogFragment {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    int chosenColor;
    EditText colorEditText;
    private ColorDialogListner colorDialogListner;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.activity_color, null);
        builder.setView(view)
                .setTitle("Color")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        radioGroup = view.findViewById(R.id.radioGroup);
                        colorEditText = view.findViewById(R.id.colorEditText);
                        if(view.findViewById(radioGroup.getCheckedRadioButtonId())!=null) {
                            radioButton = view.findViewById(radioGroup.getCheckedRadioButtonId());
                            chosenColor = radioButton.getCurrentTextColor();
                            colorDialogListner.changeColor(chosenColor);
                        }
                        else if(!colorEditText.getText().toString().trim().isEmpty())
                        {
                            String colourSeq = colorEditText.getText().toString().trim();
                            if(colourSeq.length() == 7 && colourSeq.startsWith("#") && colourSeq.matches("#-?[0-9a-fA-F]+"))
                            {
                                colorDialogListner.changeColor(Color.parseColor(colourSeq));
                            }
                        }
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            colorDialogListner = (ColorDialogListner) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface ColorDialogListner{
        void changeColor(int chosenColor);
    }
}
