package net.sharksystem.sbc.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import net.sharksystem.android.peer.SharkServiceController;
import net.sharksystem.android.protocols.wifidirect_obsolete.WifiDirectKPNotifier;
import net.sharksystem.android.Application;
import net.sharksystem.sbc.R;
import net.sharksystem.sbc.adapters.WifiDirectBroadcastAdapter;

public class BroadcastsFragment extends Fragment implements View.OnClickListener {

    private final SharkServiceController mServiceController;
    private final WifiDirectBroadcastAdapter mBroadCastAdapter;
//    private final BroadcastReceiver _broadcastReceiver;
    private EditText mEditText;
    private Button mButton;
    private ListView mListView;
    private final Context mContext;

    public BroadcastsFragment() {
        mContext = Application.getAppContext();

        mServiceController = SharkServiceController.getInstance(getContext());
        mBroadCastAdapter = new WifiDirectBroadcastAdapter(mContext);

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                mBroadCastAdapter.setMessages(mServiceController.getStringMessages());
                handler.postDelayed(this, 1000);
            }
        });

//        _broadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                String action = intent.getAction();
//
//                if(WifiDirectKPNotifier.NEW_BROADCAST_ACTION.equals(action)){
//                    L.d("Received a broadcast", this);
////                    _broadCasts.add(intent.getStringExtra("broadcast_message"));
//                    mBroadCastAdapter.add(intent.getStringExtra("broadcast_message"));
//                }
//            }
//        };




//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(WifiDirectKPNotifier.NEW_BROADCAST_ACTION);
//        LocalBroadcastManager.getInstance(mContext).registerReceiver(
//                _broadcastReceiver, intentFilter);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onResume() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiDirectKPNotifier.NEW_BROADCAST_ACTION);
//        LocalBroadcastManager.getInstance(mContext).registerReceiver(
//                _broadcastReceiver, intentFilter);
        super.onResume();
    }

    @Override
    public void onPause() {
//        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(_broadcastReceiver);
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_broadcasts, container, false);

        mEditText = (EditText) view.findViewById(R.id.editTextBroadcast);
        mButton = (Button) view.findViewById(R.id.buttonSendBroadcast);
        mButton.setOnClickListener(this);

        mListView = (ListView) view.findViewById(R.id.listViewBroadcasts);
        mListView.setAdapter(mBroadCastAdapter);
        return view;
    }

    @Override
    public void onClick(View v) {
        String broadcast = mEditText.getText().toString();

        hideSoftKeyboard(getActivity());

        if(!broadcast.isEmpty()){
            mServiceController.sendBroadcast(broadcast);
            Toast.makeText(getContext(), "Broadcast sent", Toast.LENGTH_SHORT).show();
            mEditText.getText().clear();
        } else {
            Toast.makeText(getContext(), "Broadcast is empty", Toast.LENGTH_SHORT).show();
        }
    }


}
