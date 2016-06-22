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

    private ViewPager mPager;
    private TabLayout mTabLayout;
    private SharkServiceController mServiceController;
    private FragmentManager mFragmentManager;
    private WifiManager mWifiManager;
    private Menu mMenu;

    private String mName = "";
    private String mInterest = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.setLogLevel(L.LOGLEVEL_ALL);

        initFragments();

        mWifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);

        Intent intent = getIntent();
        mName = intent.getStringExtra("name");
        mInterest = intent.getStringExtra("interest");


        if(mWifiManager.isWifiEnabled()){
            mServiceController = SharkServiceController.getInstance(this);
            mServiceController.setOffer(mName, mInterest);
            mServiceController.bindToService();
        } else {
            showWifiActivationDialog();
        }
    }

    @Override
    protected void onResume() {
        mServiceController.bindToService();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        if(mWifiManager.isWifiEnabled()){
            mMenu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_signal_wifi_4_bar, null));
        } else {
            mMenu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_signal_wifi_off, null));
        }
        return true;
    }

    @Override
    protected void onStop() {
        mServiceController.unbindFromService();
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset:
                mServiceController.unbindFromService();
                mServiceController.resetPeers();
                Intent intent = new Intent(this, IntroActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_toggle_wifi:
                if(mWifiManager.isWifiEnabled()){
                    mWifiManager.setWifiEnabled(false);
                    mMenu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_signal_wifi_off, null));
                    mServiceController.unbindFromService();
                } else {
                    mWifiManager.setWifiEnabled(true);
                    mMenu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_signal_wifi_4_bar, null));
                    mServiceController.setOffer(mName, mInterest);
                    mServiceController.bindToService();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    // So onDestroy won't be triggered.
//    @Override
//    public void onBackPressed() {
//        moveTaskToBack(true);
//    }

    public void initFragments(){
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mPager = (ViewPager) findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        mFragmentManager = getSupportFragmentManager();

        //object of PagerAdapter passing fragment manager object as a parameter of constructor of PagerAdapter class.
        SampleFragmentPagerAdapter adapter = new SampleFragmentPagerAdapter(mFragmentManager);

        //set Adapter to view mPager
        mPager.setAdapter(adapter);

        //set tablayout with viewpager
        mTabLayout.setupWithViewPager(mPager);

        // adding functionality to tab and viewpager to manage each other when a page is changed or when a tab is selected
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        //Setting tabs from adpater
        mTabLayout.setTabsFromPagerAdapter(adapter);
    }

    public void showWifiActivationDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new WifiActivationFragment();
        dialog.show(getSupportFragmentManager(), "WifiActivationFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        mWifiManager.setWifiEnabled(true);

        mMenu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_signal_wifi_4_bar, null));
        mServiceController = SharkServiceController.getInstance(this);
        mServiceController.setOffer(mName, mInterest);
        mServiceController.bindToService();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);
    }
}
