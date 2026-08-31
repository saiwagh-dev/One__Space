package com.file_handlers.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.file_handlers.config.FirebaseConfig;
import com.file_handlers.model.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;

public class AdminUserDAO {

    private final FirebaseAuth auth;
    private final AdminAlertDAO alertDAO;

    public AdminUserDAO() {
        FirebaseConfig.getFirebaseApp();
        auth = FirebaseAuth.getInstance();
        alertDAO = new AdminAlertDAO();
    }

    // =========================================================
    // LIST ALL USERS
    // =========================================================

    public List<UserData> listUsers() throws Exception {

        List<UserData> users = new ArrayList<>();
        ListUsersPage page = auth.listUsers(null);

        while (page != null) {

            for (UserRecord user : page.getValues()) {

                String name = user.getDisplayName();

                if (name == null || name.isBlank()) {
                    name = "User";
                }

                String status =
                        user.isDisabled()
                                ? "Disabled"
                                : "Active";

                long lastSignIn =
                        user.getUserMetadata()
                                .getLastSignInTimestamp();

                String lastLogin =
                        formatTimestamp(lastSignIn);

                users.add(
                        new UserData(
                                user.getUid(),
                                name,
                                user.getEmail(),
                                status,
                                lastLogin
                        )
                );
            }

            String token = page.getNextPageToken();

            if (token == null || token.isBlank()) {
                break;
            }

            page = auth.listUsers(token);
        }

        return users;
    }

    // =========================================================
    // ENABLE / DISABLE USER
    // =========================================================

    public boolean setUserDisabled(
            String uid,
            boolean disabled
    ) throws Exception {

        if (uid == null || uid.isBlank()) {
            throw new IllegalArgumentException(
                    "User UID is required."
            );
        }

        UserRecord.UpdateRequest request =
                new UserRecord.UpdateRequest(uid)
                        .setDisabled(disabled);

        auth.updateUser(request);

        if (disabled) {
            try {
                alertDAO.logAlert(
                        "User Deactivated",
                        "A user account was deactivated by an administrator."
                );
            } catch (Exception e) {
                System.err.println(
                        "[ALERT] Could not log deactivation: "
                                + e.getMessage()
                );
            }
        }

        return true;
    }

    // =========================================================
    // FORMAT LAST LOGIN
    // =========================================================

    private String formatTimestamp(long timestamp) {

        if (timestamp <= 0) {
            return "Never";
        }

        return new SimpleDateFormat(
                "dd MMM yyyy, HH:mm"
        ).format(new Date(timestamp));
    }
}
