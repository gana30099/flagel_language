package com.example.foraddingtoserverio;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class FragmentIpPlusPort extends Fragment {
    {

    }

    public FragmentIpPlusPort() {
        super(R.layout.add_fragment);
    }

    /*
    maybe called by the activity and then failed
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View v = null;
        try {
            v = inflater.inflate(R.layout.add_fragment, parent, false);
        } catch (Exception e) {
            Log.e("e", "onCreateView", e);
        }

        return v;

    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);

        super.onViewCreated(view, savedInstanceState);

        ((MainActivity)getActivity()).getPropertyAndSetEditText(null, null);
        ((MainActivity)getActivity()).setExpendable();

        //getChildFragmentManager()

        // todo see if it's wrong
        //Button b = (Button) getActivity().findViewById(R.id.add);
        //Button b = view.findViewById(R.id.add);
        //Socket finalMySocket = mySocket;
/*        b.setOnClickListener(new View.OnClickListener() {

            EditText mother = (EditText) getActivity().findViewById(R.id.mother);
            EditText other = (EditText) getActivity().findViewById(R.id.foreign);

            @Override
            public void onClick(View v) {

                ((IpPlusPortMain)getActivity()).add_sentence(mother, other);

                //((MainActivity) v.getContext()).makeText(mother.getText().toString());
                // Write data to the output stream of the Client Socket.
            }


        });*/

    }


}