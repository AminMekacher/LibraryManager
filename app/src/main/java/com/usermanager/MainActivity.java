package com.usermanager;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.usermanager_demo.R;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by SONU on 21/03/16.
 */
public class MainActivity extends AppCompatActivity {

    private static DrawerLayout mDrawerLayout;
    private static ActionBarDrawerToggle mDrawerToggle;
    private static Toolbar toolbar;
    private static FragmentManager fragmentManager;
    private static NavigationView navigationView;

    private static String mUsername;
    private static Uri mProfilePic;

    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;

    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.custom_navigation_view_activity);

        //Check if user is connected via intent

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser != null) {
            mEmail = firebaseUser.getEmail();
            mUsername = firebaseUser.getDisplayName();
            mProfilePic = firebaseUser.getPhotoUrl();

        }

        initViews();
        setUpHeaderView();
        onMenuItemSelected();

        //At start set home fragment
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.home);
            MenuItem item = navigationView.getMenu().findItem(R.id.home);
            setFragment(item);
        }
    }

    /*  Init all views  */
    private void initViews() {
        toolbar = findViewById(R.id.main_toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.slider_menu);

        if (firebaseUser == null) { // user is not connected yet
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.navigation_with_login);
        } else {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.navigation_with_logout);
        }

        fragmentManager = getSupportFragmentManager();
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, toolbar, // nav menu toggle icon
                R.string.drawer_open, // nav drawer open - description for
                // accessibility
                R.string.drawer_close // nav drawer close - description for
                // accessibility
        ) {
            public void onDrawerClosed(View view) {
            }

            public void onDrawerOpened(View drawerView) {

            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    /**
     * For using header view use this method
     **/
    private void setUpHeaderView() {
        View headerView = navigationView.inflateHeaderView(R.layout.header_view);
        TextView usernameText = headerView.findViewById(R.id.username);
        TextView emailText = headerView.findViewById(R.id.email_address);
        ImageView profileImage = headerView.findViewById(R.id.pictureHeader);

        if (firebaseUser == null) {
            usernameText.setText("Guest");
            emailText.setText(" ");
        } else {
            usernameText.setText(mUsername);
            emailText.setText(mEmail);
            Picasso.get().load(mProfilePic)
                         .resize(150, 150)
                         .transform(new CropCircleTransformation())
                         .into(profileImage);
        }
    }


    /*  Method for Navigation View item selection  */
    private void onMenuItemSelected() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                //Check and un-check menu item if they are checkable behaviour
                if (item.isCheckable()) {
                    if (item.isChecked()) item.setChecked(false);
                    else item.setChecked(true);
                }

                //Closing drawer on item click
                mDrawerLayout.closeDrawers();

                switch (item.getItemId()) {
                    case R.id.home:
                        //Replace fragment


                        setFragment(item);
                        break;

                    case R.id.register:
                        Intent myIntent = new Intent(MainActivity.this, RegisterActivity.class);
                        MainActivity.this.startActivity(myIntent);
                        //Replace fragment


                        setFragment(item);
                        break;
                    case R.id.chat:
                        //Replace fragment


                        setFragment(item);
                        break;
                    case R.id.notifications:
                        //Replace fragment


                        setFragment(item);

                        break;
                    case R.id.share_app:

                        //Start new Activity or do your stuff


                        Toast.makeText(MainActivity.this, "You Clicked on \"" + item.getTitle().toString() + "\" menu item.", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rate_app:
                        //Start new Activity or do your stuff


                        Toast.makeText(MainActivity.this, "You Clicked on \"" + item.getTitle().toString() + "\" menu item.", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings:
                        //Start new Activity or do your stuff


                        Toast.makeText(MainActivity.this, "You Clicked on \"" + item.getTitle().toString() + "\" menu item.", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.help:
                        //Start new Activity or do your stuff


                        Toast.makeText(MainActivity.this, "You Clicked on \"" + item.getTitle().toString() + "\" menu item.", Toast.LENGTH_SHORT).show();

                        break;

                    case R.id.logout:

                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        break;

                    case R.id.login:

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        MainActivity.this.startActivity(intent);
                        break;


                }

                return false;
            }
        });
    }

    /*  Set Fragment, setting toolbar title and passing item title via bundle to fragments*/
    public void setFragment(MenuItem item) {
        toolbar.setTitle(item.getTitle());

        //Find fragment by tag
        Fragment fr = fragmentManager.findFragmentByTag(item.getTitle().toString());

        Fragment dummyFragment = new Dummy_Fragment();
        Bundle b = new Bundle();

        //If fragment is null replace fragment
        if (fr == null) {
            b.putString("data", item.getTitle().toString());
            dummyFragment.setArguments(b);//Set Arguments
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_container,
                            dummyFragment, item.getTitle().toString())
                    .commit();
        }
    }




    //On back press check if drawer is open and closed
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT))
            mDrawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }
}
