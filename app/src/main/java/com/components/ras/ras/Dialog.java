package com.components.ras.ras;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Dialog extends AppCompatDialogFragment {
    private EditText editText;
    private dialogListener listener;
    private EditText editText2;
    private Button addImg;
    String result;

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_item, null);
        editText = view.findViewById(R.id.item_name);
        editText2 = view.findViewById(R.id.quantity);
        addImg = view.findViewById(R.id.addImage);
        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, 0);
            }
        });
        builder.setView(view).setTitle("Add a component").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }

        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String item_name = editText.getText().toString().trim();
                String quantity = editText2.getText().toString().trim();
                if (!item_name.isEmpty() && !quantity.isEmpty()) {
                    listener.applyTexts(item_name, quantity, result);

                } else if (item_name.isEmpty()) {
                    dismiss();
                    Toast.makeText(getActivity(), "Please specify the item's name", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "مكسل تكتب كام واحد يعني؟", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (dialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement dialog listener");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
            String result = data.toURI();
            Uri myUri = Uri.parse(result);
        }
    }

    public interface dialogListener {
        void applyTexts(String item, String Quantity, String imgUrl);
    }

}