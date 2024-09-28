package michal.malek.diagnosticsapp.auth.services;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import michal.malek.diagnosticsapp.auth.configuration.EmailConfiguration;
import michal.malek.diagnosticsapp.core.models.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Setter
public class EmailService {

    private final EmailConfiguration emailConfiguration;

    @Value("${front.url}")
    private String frontUrl;

    @Value("classpath:/templates/auth/email/activate.html")
    private Resource activeTemplate;

    @Value("classpath:/templates/auth/email/retrieve-password-email.html")
    private Resource retrieveTemplate;

    public void sendActivation(UserEntity user) {
        try {
            String html = loadTemplate(activeTemplate);
            String newUrl = frontUrl + "/account-activate?uid=" + user.getUid();
            html = html.replace("https://google.com", newUrl);
            emailConfiguration.sendMail(user.getEmail(), html, "DiagnoseYourself Account Activation", true);
        } catch (IOException e) {
            throw new RuntimeException("Error while sending activation email", e);
        }
    }

    public void sendPasswordRecovery(UserEntity user, String operationUid) {
        try {
            String html = loadTemplate(retrieveTemplate);
            String newUrl = frontUrl + "/reset-password?operationUid=" + operationUid;
            html = html.replace("https://google.com", newUrl);
            emailConfiguration.sendMail(user.getEmail(), html, "DiagnoseYourself Retrieve Password", true);
        } catch (IOException e) {
            throw new RuntimeException("Error while sending password recovery email", e);
        }
    }

    private String loadTemplate(Resource resource) throws IOException {
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
