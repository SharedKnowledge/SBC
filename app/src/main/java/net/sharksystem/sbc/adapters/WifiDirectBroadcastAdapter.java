package net.sharksystem.sbc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.sharkfw.asip.ASIPInterest;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.android.protocols.wifidirect.WifiDirectPeer;
import net.sharksystem.sbc.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by micha on 17.12.15.
 */
public class WifiDirectBroadcastAdapter extends BaseAdapter {

    private List<String> _messages;
    private Context mContext;

    public WifiDirectBroadcastAdapter(Context context){
        mContext = context;
        _messages = new ArrayList<>();
    }

    public void setContext(Context context){
        mContext = context;
    }

    @Override
    public int getCount() {
        return _messages.size();
    }

    @Override
    public String getItem(int position) {
        return _messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void add(String message){
        _messages.add(message);
        notifyDataSetChanged();
    }

    public void setMessages(List messages){
        _messages.clear();
        _messages.addAll(messages);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String message = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.list_item_broadcasts, parent, false);
        }

        TextView messageView = (TextView) convertView.findViewById(R.id.textViewBroadcastMessage);
        messageView.setText(message);
        return convertView;
    }
}
