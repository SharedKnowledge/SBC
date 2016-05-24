package net.sharksystem.sbc.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import net.sharkfw.system.L;
import net.sharksystem.android.peer.SharkServiceController;
import net.sharksystem.sbc.R;
import net.sharksystem.sbc.adapters.SampleFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPager pager;
    private TabLayout tabLayout;
    private SharkServiceController _serviceController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        L.setLogLevel(L.LOGLEVEL_ALL);

        initFragments();

        _serviceController = SharkServiceController.getInstance(this);
    }

    @Override
    protected void onStop() {
        _serviceController.unbindFromService();
        super.onStop();
    }

    @Override
    protected void onStart() {
        _serviceController.bindToService();
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        _serviceController.unbindFromService();
        _serviceController.stopService();
        L.d("Service destroyed", this);

        super.onDestroy();

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

        FragmentManager manager = getSupportFragmentManager();

        //object of PagerAdapter passing fragment manager object as a parameter of constructor of PagerAdapter class.
        SampleFragmentPagerAdapter adapter = new SampleFragmentPagerAdapter(manager);

        //set Adapter to view pager
        pager.setAdapter(adapter);

        //set tablayout with viewpager
        tabLayout.setupWithViewPager(pager);

        // adding functionality to tab and viewpager to manage each other when a page is changed or when a tab is selected
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Setting tabs from adpater
        tabLayout.setTabsFromPagerAdapter(adapter);
    }

}
