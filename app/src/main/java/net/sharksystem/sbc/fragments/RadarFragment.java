package net.sharksystem.sbc.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import net.sharksystem.android.protocols.wifidirect.WifiDirectPeer;
import net.sharksystem.android.peer.SharkServiceController;
import net.sharksystem.android.Application;
import net.sharksystem.sbc.R;
import net.sharksystem.sbc.adapters.WifiDirectPeerAdapter;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class RadarFragment extends Fragment {

    private final SharkServiceController _serviceController;
    private final Handler _threadHandler;
    private final Runnable _thread;
    private ListView _listView;
    private WifiDirectPeerAdapter _peerAdapter;
    private final Context _context;
    private CopyOnWriteArrayList<WifiDirectPeer> mPeerList;

    public RadarFragment() {
        _context = Application.getAppContext();

        _peerAdapter = new WifiDirectPeerAdapter(_context);

        _serviceController = SharkServiceController.getInstance(_context);

        _threadHandler = new Handler();
        _thread = new Runnable() {
            @Override
            public void run() {
                mPeerList = _serviceController.getPeers();

                long current = System.currentTimeMillis();

                Iterator<WifiDirectPeer> iterator = mPeerList.iterator();
                while (iterator.hasNext()){
                    WifiDirectPeer peer = iterator.next();
                    if((current - peer.getLastUpdated()) > 1000 * 60){
                        mPeerList.remove(peer);
                    }
                }

                _peerAdapter.setList(mPeerList);
                _threadHandler.postDelayed(this, 5000);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_radar, container, false);
        _listView = (ListView) view.findViewById(R.id.wifidirectListView);
        _listView.setAdapter(_peerAdapter);
        return view;
    }

    @Override
    public void onResume() {
        _threadHandler.post(_thread);
        super.onResume();
    }

    @Override
    public void onPause() {
        _threadHandler.removeCallbacks(_thread);
        super.onPause();
    }
}
