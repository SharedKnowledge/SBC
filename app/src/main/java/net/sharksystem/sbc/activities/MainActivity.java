package net.sharksystem.sbc.activities;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import net.sharkfw.system.L;
import net.sharksystem.android.peer.SharkServiceController;
import net.sharksystem.sbc.R;
import net.sharksystem.sbc.adapters.SampleFragmentPagerAdapter;
import net.sharksystem.sbc.fragments.WifiActivationFragment;

public class MainActivity extends AppCompatActivity
        implements WifiActivationFragment.NoticeDialogListener{

    private ViewPager pager;
    private TabLayout tabLayout;
    private SharkServiceController _serviceController;
    private FragmentManager _fragmentManager;
    private WifiManager _wifiManager;
    private Menu _menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.setLogLevel(L.LOGLEVEL_ALL);

        initFragments();

        _wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);

        if(_wifiManager.isWifiEnabled()){
//            _menu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_signal_wifi_4_bar, null));
            _serviceController = SharkServiceController.getInstance(this);
            _serviceController.bindToService();
        } else {
//            _menu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_signal_wifi_off, null));
            showWifiActivationDialog();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        _menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        if(_wifiManager.isWifiEnabled()){
            _menu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_signal_wifi_4_bar, null));
        } else {
            _menu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_signal_wifi_off, null));
        }
        return true;
    }

    @Override
    protected void onStop() {
        if(_wifiManager.isWifiEnabled()){
            _serviceController.unbindFromService();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if(_wifiManager.isWifiEnabled()){
            _serviceController.unbindFromService();
            _serviceController.stopService();
        }
        L.d("Service destroyed", this);

        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset:
                _serviceController.unbindFromService();
                _serviceController.stopService();
                _serviceController.resetPeers();
                Intent intent = new Intent(this, IntroActivity.class);
                startActivity(intent);
            case R.id.action_toggle_wifi:
                if(_wifiManager.isWifiEnabled()){
                    _wifiManager.setWifiEnabled(false);
                    _menu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_signal_wifi_off, null));
                } else {
                    _wifiManager.setWifiEnabled(true);
                    _menu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_signal_wifi_4_bar, null));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    // So onDestroy won't be triggered.
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void initFragments(){
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        pager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        _fragmentManager = getSupportFragmentManager();

        //object of PagerAdapter passing fragment manager object as a parameter of constructor of PagerAdapter class.
        SampleFragmentPagerAdapter adapter = new SampleFragmentPagerAdapter(_fragmentManager);

        //set Adapter to view pager
        pager.setAdapter(adapter);

        //set tablayout with viewpager
        tabLayout.setupWithViewPager(pager);

        // adding functionality to tab and viewpager to manage each other when a page is changed or when a tab is selected
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Setting tabs from adpater
        tabLayout.setTabsFromPagerAdapter(adapter);
    }

    public void showWifiActivationDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new WifiActivationFragment();
        dialog.show(getSupportFragmentManager(), "WifiActivationFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        _wifiManager.setWifiEnabled(true);

        _menu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_signal_wifi_4_bar, null));
        _serviceController = SharkServiceController.getInstance(this);
        _serviceController.bindToService();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);
    }
}
