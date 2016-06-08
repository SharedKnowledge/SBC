package net.sharksystem.sbc.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import net.sharkfw.system.L;
import net.sharksystem.android.peer.SharkServiceController;
import net.sharksystem.android.protocols.wifidirect.WifiDirectKPNotifier;
import net.sharksystem.android.Application;
import net.sharksystem.sbc.R;
import net.sharksystem.sbc.adapters.WifiDirectBroadcastAdapter;

import java.util.ArrayList;

public class BroadcastsFragment extends Fragment implements View.OnClickListener {

    private final SharkServiceController _serviceController;
    private final WifiDirectBroadcastAdapter _broadCastAdapter;
    private final BroadcastReceiver _broadcastReceiver;
    private EditText _editText;
    private Button _button;
    private ArrayList<String> _broadCasts;
    private ListView _listView;
    private final Context _context;

    public BroadcastsFragment() {
        _context = Application.getAppContext();

        _serviceController = SharkServiceController.getInstance(getContext());
        _broadCasts = new ArrayList<>();
        _broadCastAdapter = new WifiDirectBroadcastAdapter(_context);

        _broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if(WifiDirectKPNotifier.NEW_BROADCAST_ACTION.equals(action)){
                    L.d("Received a broadcast", this);
//                    _broadCasts.add(intent.getStringExtra("broadcast_message"));
                    _broadCastAdapter.add(intent.getStringExtra("broadcast_message"));
                }
            }
        };


//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(WifiDirectKPNotifier.NEW_BROADCAST_ACTION);
//        LocalBroadcastManager.getInstance(_context).registerReceiver(
//                _broadcastReceiver, intentFilter);
    }

    @Override
    public void onResume() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiDirectKPNotifier.NEW_BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(_context).registerReceiver(
                _broadcastReceiver, intentFilter);
        super.onResume();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(_context).unregisterReceiver(_broadcastReceiver);
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_broadcasts, container, false);

        _editText = (EditText) view.findViewById(R.id.editTextBroadcast);
        _button = (Button) view.findViewById(R.id.buttonSendBroadcast);
        _button.setOnClickListener(this);

        _listView = (ListView) view.findViewById(R.id.listViewBroadcasts);
        _listView.setAdapter(_broadCastAdapter);
        return view;
    }

    @Override
    public void onClick(View v) {
        String broadcast = _editText.getText().toString();
//
        if(!broadcast.isEmpty()){
            _serviceController.sendBroadcast(broadcast);
        }
    }


}
