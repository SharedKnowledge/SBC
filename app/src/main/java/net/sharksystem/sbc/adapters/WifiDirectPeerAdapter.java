package net.sharksystem.sbc.adapters;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.sharkfw.asip.ASIPInterest;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.asip.engine.ASIPSerializer;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.android.protocols.wifidirect.WifiDirectPeer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sharksystem.sbc.R;

/**
 * Created by micha on 17.12.15.
 */
public class WifiDirectPeerAdapter extends BaseAdapter {

    private List<WifiDirectPeer> mDeviceList;
    private Context mContext;

    public WifiDirectPeerAdapter(Context context){
        mContext = context;
        mDeviceList = new LinkedList<>();
    }

    public void setContext(Context context){
        mContext = context;
    }

    @Override
    public int getCount() {
        return mDeviceList.size();
    }

    @Override
    public WifiDirectPeer getItem(int position) {
        return mDeviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setList(List<WifiDirectPeer> peers){
        Iterator iterator = peers.iterator();
        mDeviceList.clear();
        while(iterator.hasNext()){
            mDeviceList.add((WifiDirectPeer) iterator.next());
        }
        notifyDataSetChanged();
    }

    public void clearList(){
        mDeviceList.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WifiDirectPeer peer = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
//            convertView = LayoutInflater.from(mContext)
//                    .inflate(R.layout.list_item_wifidirectdevice, parent, false);
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.list_item_peer, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.textViewDeviceName);
        TextView address = (TextView) convertView.findViewById(R.id.textViewDeviceAddress);
//        TextView status = (TextView) convertView.findViewById(R.id.textViewDeviceStatus);
        TextView interest = (TextView) convertView.findViewById(R.id.textViewTopic);

        name.setText(peer.deviceName);
//        String statusText = "";
//        switch (peer.status){
//            case WifiP2pDevice.CONNECTED:
//                statusText = "CONNECTED";
//                break;
//            case WifiP2pDevice.INVITED:
//                statusText = "INVITED";
//                break;
//            case WifiP2pDevice.FAILED:
//                statusText = "FAILED";
//                break;
//            case WifiP2pDevice.AVAILABLE:
//                statusText = "AVAILABLE";
//                break;
//            case WifiP2pDevice.UNAVAILABLE:
//                statusText = "UNAVAILABLE";
//                break;
//        }
//        status.setText(statusText);
        try{
            ASIPInterest asipInterest = peer.getInterest();
            Iterator<SemanticTag> semanticTagIterator = asipInterest.getTopics().stTags();
            String text = "";
            while(semanticTagIterator.hasNext()) {
                if(text.isEmpty()){
                    text = semanticTagIterator.next().getName();
                } else {
                    text += ", " + semanticTagIterator.next().getName();
                }
            }
            if(text.isEmpty()){
                interest.setText("No topic found");
            } else {
                interest.setText(text);
            }
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        address.setText(peer.deviceAddress);

//        TextView name = (TextView) convertView.findViewById(R.id.textViewDeviceName);
//        TextView status = (TextView) convertView.findViewById(R.id.textViewDeviceStatus);
//        TextView owner = (TextView) convertView.findViewById(R.id.textViewDeviceIsGroupOwner);
//        TextView discovery = (TextView) convertView.findViewById(R.id.textViewDeviceIsServiceDiscoverCapable);
//        TextView primeType = (TextView) convertView.findViewById(R.id.textViewDevicePrimeType);
//        TextView secondType = (TextView) convertView.findViewById(R.id.textViewDeviceSecType);
//        name.setText(device.deviceName);
//        String statusText = "";
//        switch (device.status){
//            case WifiP2pDevice.CONNECTED:
//                statusText = "CONNECTED";
//                break;
//            case WifiP2pDevice.INVITED:
//                statusText = "INVITED";
//                break;
//            case WifiP2pDevice.FAILED:
//                statusText = "FAILED";
//                break;
//            case WifiP2pDevice.AVAILABLE:
//                statusText = "AVAILABLE";
//                break;
//            case WifiP2pDevice.UNAVAILABLE:
//                statusText = "UNAVAILABLE";
//                break;
//        }
//        status.setText(statusText);
//        owner.setText(""+device.isGroupOwner());
//        discovery.setText(""+device.isServiceDiscoveryCapable());
//        primeType.setText(device.primaryDeviceType);
//        secondType.setText(device.secondaryDeviceType);
        return convertView;
    }
}
