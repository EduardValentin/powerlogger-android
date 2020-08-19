package com.eduardv.powerlogger.ui.login;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.eduardv.powerlogger.BuildConfig;
import com.eduardv.powerlogger.MainActivity;
import com.eduardv.powerlogger.R;
import com.eduardv.powerlogger.databinding.LoginFragmentBindingImpl;
import com.eduardv.powerlogger.dto.user.GoogleUserAuthenticationDTO;
import com.eduardv.powerlogger.dto.user.UserAuthenticationResponseDTO;
import com.eduardv.powerlogger.dto.user.UserDTO;
import com.eduardv.powerlogger.repositories.UserRepository;
import com.eduardv.powerlogger.ui.forgotPassword.ForgotPasswordFragment;
import com.eduardv.powerlogger.ui.register.RegisterActivity;
import com.eduardv.powerlogger.ui.userSettings.UserSettingsFragment;
import com.eduardv.powerlogger.utils.Constants;
import com.eduardv.powerlogger.utils.Result;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment {
    private static final int GOOGLE_SIGNIN_RC = 1;

    private LoginViewModel loginViewModel;
    private GoogleSignInClient googleSignInClient;
    private LoginFragmentBindingImpl binding;

    private final Gson gson;

    public LoginFragment() {
        gson = new Gson();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.GOOGLE_SIGN_IN_CLIENT_ID)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false);
        binding.setView(this);
        binding.setLifecycleOwner(this);

        loginViewModel = ViewModelProviders.of(getActivity(), new LoginViewModelFactory())
                .get(LoginViewModel.class);


        binding.googleSignInButton.setOnClickListener(this::onGoogleSignInClick);

        binding.registerButton.setOnClickListener(v -> {
            Intent registerIntent = new Intent(getActivity(), RegisterActivity.class);
            startActivity(registerIntent);
        });

        binding.forgotPasswordButton.setOnClickListener(v -> goToForgotPassword());

        loginViewModel.getLoginFormState().observe(getViewLifecycleOwner(), loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            binding.login.setEnabled(loginFormState.isDataValid());

            if (loginFormState.getUsernameError() != null) {
                binding.usernameLayout.setError(getString(loginFormState.getUsernameError()));
            } else {
                binding.usernameLayout.setError(null);
            }

            if (loginFormState.getPasswordError() != null) {
                binding.passwordLayout.setError(getString(loginFormState.getPasswordError()));
            } else {
                binding.passwordLayout.setError(null);
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(binding.username.getText().toString(),
                        binding.password.getText().toString());
            }
        };
        binding.username.addTextChangedListener(afterTextChangedListener);
        binding.password.addTextChangedListener(afterTextChangedListener);
        binding.password.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(binding.username.getText().toString(),
                        binding.password.getText().toString(), this::loginCallback);
            }
            return false;
        });

        binding.login.setOnClickListener(v -> {
            binding.loading.setVisibility(View.VISIBLE);
            loginViewModel.login(binding.username.getText().toString(),
                    binding.password.getText().toString(), this::loginCallback);
        });

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        UserDTO user = getCurrentlyLoggedUser();

        if (user == null) {
            // Didn't log at all in the past
            binding.loading.setVisibility(View.INVISIBLE);
            binding.loginViewContent.setVisibility(View.VISIBLE);
            return;
        }

        // Check for existing Google Sign In account, if the user is already signed in
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());

        if (account != null) {
            validateLastGoogleSignInAndRenewIfNeeded(account);
            return;
        }

        validateToken(getUserAccessToken(), getCurrentlyLoggedUser().getUsername());
    }

    public void goToForgotPassword() {
        ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.loginFragmentContainer, forgotPasswordFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void loginCallback(Object response) {
        binding.loading.setVisibility(View.GONE);

        if (response instanceof Result.Error) {
            showIncorrectCredentials();
            return;
        }

        Result.Success<UserAuthenticationResponseDTO> result = (Result.Success<UserAuthenticationResponseDTO>) response;
        Log.i("LOGIN_RESULT", "User logged with token: " + result.getData().getToken());

        SharedPreferences.Editor editor = getActivity().getSharedPreferences(Constants.USER_INFO, MODE_PRIVATE).edit();
        editor.putString(Constants.TOKEN, result.getData().getToken());
        editor.putString(Constants.USER_DATA_SHAREDPREF, gson.toJson(result.getData().getUser()));
        editor.apply();

        finishAndGoToMain();
    }

    public void onGoogleSignInClick(View v) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if (account != null) {
            validateLastGoogleSignInAndRenewIfNeeded(account);
            return;
        }

        Intent signInIntent = googleSignInClient.getSignInIntent();
        getActivity().startActivityForResult(signInIntent, GOOGLE_SIGNIN_RC);
    }

    private void validateLastGoogleSignInAndRenewIfNeeded(GoogleSignInAccount account) {
        if (account == null) {
            return;
        }

        if (account.isExpired()) {
            googleSignInClient.silentSignIn().addOnCompleteListener(
                    getActivity(), this::handleSignInResult);
        } else {
            loginViewModel.validateAndCreateIfNotExistsGoogleAccount(account.getIdToken(),
                    this::storeCurrentlyLoggedUserAndGoToMain, this::stopLoadingOnFail);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            loginViewModel.validateAndCreateIfNotExistsGoogleAccount(account.getIdToken(),
                    this::storeCurrentlyLoggedUserAndGoToMain, this::stopLoadingOnFail);

//            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            this.stopLoadingOnFail(e);
            Log.e("GOOGLE_SIGNIN", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void stopLoadingOnFail(Throwable throwable) {
        binding.loading.setVisibility(View.INVISIBLE);
        binding.loginViewContent.setVisibility(View.VISIBLE);
    }

    private void showIncorrectCredentials() {
        Toast.makeText(getContext(), R.string.incorect_credentials, Toast.LENGTH_SHORT).show();
    }

    private void validateToken(String token, String username) {
        loginViewModel.validateToken(token, username,
                this::onInternalTokenValidCb, this::stopLoadingOnFail);
    }

    private void storeCurrentlyLoggedUserAndGoToMain(GoogleUserAuthenticationDTO authDTO) {
        UserRepository.getInstance().setToken(authDTO.getAuthToken());
        UserRepository.getInstance().setUser(authDTO.getData());

        SharedPreferences.Editor editor = getActivity().getSharedPreferences(Constants.USER_INFO, MODE_PRIVATE).edit();

        editor.putString(Constants.TOKEN, authDTO.getAuthToken());
        editor.putString(Constants.USER_DATA_SHAREDPREF, gson.toJson(authDTO.getData()));
        editor.apply();

        if (authDTO.isNewAccount() || authDTO.getData().getSettings() == null) {
            // Go to user settings setup
            goToUserSettings(authDTO.getData().getUsername());
            return;
        }

        finishAndGoToMain();
    }

    private String getUserAccessToken() {
        Map<String, ?> preferences = getActivity().getSharedPreferences(Constants.USER_INFO, MODE_PRIVATE).getAll();
        return (String) preferences.get(Constants.TOKEN);
    }

    private UserDTO getCurrentlyLoggedUser() {
        Map<String, ?> preferences = getActivity().getSharedPreferences(Constants.USER_INFO, MODE_PRIVATE).getAll();

        String userDataString = (String) preferences.get(Constants.USER_DATA_SHAREDPREF);
        return gson.fromJson(userDataString, UserDTO.class);
    }

    private void onInternalTokenValidCb(Object res) {
        binding.loading.setVisibility(View.INVISIBLE);
        storeCurrentlyLoggedUserAndGoToMain(getUserAccessToken(), getCurrentlyLoggedUser());
    }

    private void goToUserSettings(String username) {
        UserSettingsFragment userSettingsFragment = new UserSettingsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("username", username);

        userSettingsFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.loginFragmentContainer, userSettingsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void storeCurrentlyLoggedUserAndGoToMain(String token, UserDTO user) {
        UserRepository.getInstance().setToken(token);
        UserRepository.getInstance().setUser(user);

        finishAndGoToMain();
    }

    private void updateUiWithUser() {
        String welcome = getString(R.string.welcome);
        Toast.makeText(getContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void finishAndGoToMain() {
        updateUiWithUser();
        getActivity().setResult(Activity.RESULT_OK);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
