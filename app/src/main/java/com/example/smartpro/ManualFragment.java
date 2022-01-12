package com.example.smartpro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ManualFragment extends Fragment {
    Button on;
    Button off;
    Button on1;
    Button off1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView= inflater.inflate(R.layout.fragment_manual, container,false);
        on= myView.findViewById(R.id.buttonOn);
        off= myView.findViewById(R.id.buttonOff);
        on1= myView.findViewById(R.id.buttonOn2);
        off1= myView.findViewById(R.id.buttonOff2);

        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseApp.initializeApp(requireActivity());
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Node_Status/Node1");

                myRef.setValue("on");
                Toast.makeText(getActivity(), "Irrigation on Node1 has started", Toast.LENGTH_SHORT).show();

            }
        });
        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                DatabaseReference myRef1 = database1.getReference("Node_Status/Node1");
                myRef1.setValue("off");
                Toast.makeText(getContext(), "Irrigation on Node1 has stopped", Toast.LENGTH_SHORT).show();

            }
        });

        on1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseApp.initializeApp(requireActivity());
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Node_Status/Node2");
                System.out.println("This is the firebase: "+myRef.getKey());
                myRef.setValue("on");
                Toast.makeText(getActivity(), "Irrigation on Node2 has started", Toast.LENGTH_SHORT).show();

            }
        });
        off1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                DatabaseReference myRef1 = database1.getReference("Node_Status/Node2");
                System.out.println("This is the firebase: "+myRef1.get());
                myRef1.setValue("off");
                Toast.makeText(getContext(), "Irrigation on Node2 has stopped", Toast.LENGTH_SHORT).show();

            }
        });


        return myView;
    }

    public static ManualFragment newInstance(String value) {
        ManualFragment fragmentclass1 = new ManualFragment();

        Bundle args = new Bundle();
        args.putString("message", value);
        fragmentclass1.setArguments(args);

        return fragmentclass1;
    }

}
