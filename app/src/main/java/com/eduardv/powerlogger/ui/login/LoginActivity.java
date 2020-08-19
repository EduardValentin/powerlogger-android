package com.eduardv.powerlogger.ui.login;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.eduardv.powerlogger.MainActivity;
import com.eduardv.powerlogger.R;
import com.eduardv.powerlogger.dto.user.GoogleUserAuthenticationDTO;
import com.eduardv.powerlogger.dto.user.UserSettingsDTO;
import com.eduardv.powerlogger.repositories.UserRepository;
import com.eduardv.powerlogger.ui.userSettings.UserSettingsActionsListener;
import com.eduardv.powerlogger.ui.userSettings.UserSettingsFragment;
import com.eduardv.powerlogger.utils.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.time.LocalDate;

public class LoginActivity extends AppCompatActivity implements UserSettingsActionsListener, DatePickerDialog.OnDateSetListener {
    private LoginViewModel loginViewModel;
    private static final int GOOGLE_SIGNIN_RC = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        Fragment loginFragment = new LoginFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.loginFragmentContainer, loginFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGNIN_RC) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            loginViewModel.validateAndCreateIfNotExistsGoogleAccount(account.getIdToken(),
                    this::storeCurrentlyLoggedUserAndGoToMain, null);

//            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("GOOGLE_SIGNIN", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void updateUiWithUser() {
        String welcome = getString(R.string.welcome);
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }


    private void storeCurrentlyLoggedUserAndGoToMain(GoogleUserAuthenticationDTO authDTO) {
        UserRepository.getInstance().setToken(authDTO.getAuthToken());
        UserRepository.getInstance().setUser(authDTO.getData());

        SharedPreferences.Editor editor = getSharedPreferences(Constants.USER_INFO, MODE_PRIVATE).edit();

        editor.putString(Constants.TOKEN, authDTO.getAuthToken());
        editor.putString(Constants.USER_DATA_SHAREDPREF, new Gson().toJson(authDTO.getData()));
        editor.apply();

        if (authDTO.isNewAccount() || authDTO.getData().getSettings() == null) {
            // Go to user settings setup
            goToUserSettings(authDTO.getData().getUsername());
            return;
        }

        finishAndGoToMain();
    }

    private void goToUserSettings(String username) {
        UserSettingsFragment userSettingsFragment = new UserSettingsFragment();

        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        userSettingsFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.loginFragmentContainer, userSettingsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    private void finishAndGoToMain() {
        updateUiWithUser();
        setResult(Activity.RESULT_OK);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        LocalDate ld = LocalDate.of(year, month + 1, dayOfMonth);

        loginViewModel.setUserBirthDate(ld.toString());
        ((EditText) findViewById(R.id.userBirthDate)).setText(ld.toString());
    }

    @Override
    public void onConfirmSettings(String username, UserSettingsDTO userSettingsDTO) {
        loginViewModel.saveUserSettings(response -> {
            UserRepository.getInstance().setUser(response);
            finishAndGoToMain();
        }, userSettingsDTO);
    }
}
