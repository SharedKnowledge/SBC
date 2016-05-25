package net.sharksystem.sbc.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import net.sharksystem.android.peer.SharkServiceController;
import net.sharksystem.sbc.R;

public class BroadcastsFragment extends Fragment implements View.OnClickListener {

    private final SharkServiceController _serviceController;
    private EditText _editText;
    private Button _button;

    public BroadcastsFragment() {
        _serviceController = SharkServiceController.getInstance(getContext().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_broadcasts, container, false);

        _editText = (EditText) view.findViewById(R.id.editTextBroadcast);
        _button = (Button) view.findViewById(R.id.buttonSendBroadcast);
        _button.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        String broadcast = _editText.getText().toString();

        if(!broadcast.isEmpty()){
            _serviceController.sendBroadcast(broadcast);
        }
    }
}
