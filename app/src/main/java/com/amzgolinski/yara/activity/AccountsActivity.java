package com.amzgolinski.yara.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.amzgolinski.yara.R;
import com.amzgolinski.yara.YaraApplication;
import com.amzgolinski.yara.callbacks.AccountRetrievedCallback;
import com.amzgolinski.yara.tasks.FetchLoggedInAccountTask;
import com.amzgolinski.yara.ui.AddAccountDialogFragment;
import com.amzgolinski.yara.util.Utils;

import net.dean.jraw.auth.AuthenticationManager;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.models.LoggedInAccount;


import java.util.ArrayList;

import butterknife.OnClick;

public class AccountsActivity extends AppCompatActivity
    implements AccountRetrievedCallback, AddAccountDialogFragment.AddAccountDialogListener {

  private static final String LOG_TAG = AccountsActivity.class.getName();
  private static final String ADD_USER_DIALOG = "add_user_fragment_dialog";

  private ArrayList<String> mUsers;
  //private LoggedInAccount mRedditAccount;

  @OnClick(R.id.add_account)
  public void addAccount(View view) {

    FragmentManager fm = getSupportFragmentManager();
    DialogFragment fragment =
        AddAccountDialogFragment.newInstance(getString(R.string.dialog_add_user_title));
    fragment.show(fm, ADD_USER_DIALOG);

  }

  @OnClick(R.id.add_account)
  public void deleteAccount(View view) {

  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.d(LOG_TAG, "onCreate");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_accounts);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    mUsers = Utils.getUsers(this.getApplicationContext());
    //new FetchLoggedInAccountTask(this.getApplicationContext(), this).execute();
    Log.d(LOG_TAG, mUsers.toString());

  }

  public void onAccountRetrieved(LoggedInAccount account) {
    Log.d(LOG_TAG, "onAccountRetrieved");
    //mRedditAccount = account;
  }

  @Override
  public void onAddAccount(String username) {
    Toast.makeText(this, username, Toast.LENGTH_SHORT).show();
    CookieManager cookieManager = CookieManager.getInstance();
    cookieManager.removeAllCookie();
    startActivity(new Intent(this, LoginActivity.class));
  }

}

