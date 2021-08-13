package com.eduardv.powerlogger.ui.forgotPassword;

import androidx.lifecycle.ViewModel;

import com.eduardv.powerlogger.dto.TokenDTO;
import com.eduardv.powerlogger.dto.user.ForgotPasswordRequestDTO;
import com.eduardv.powerlogger.repositories.UserRepository;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPasswordViewModel extends ViewModel {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private UserRepository userRepository = UserRepository.getInstance();
    private ForgotPasswordRequestDTO dto;

    public ForgotPasswordViewModel() {
        dto = new ForgotPasswordRequestDTO();
    }

    public ForgotPasswordRequestDTO getDto() {
        return dto;
    }

    public void sendResetPasswordEmail(Consumer<Void> onSuccess, Consumer<Throwable> onError) {
        userRepository.sendResetPasswordEmail(onSuccess, onError, dto);
    }

    public boolean validateMail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}