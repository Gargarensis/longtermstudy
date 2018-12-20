package usi.memotion2personal;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

public class AccountUtils {

    private static final String TYPE_ACCOUNT = "usi.memotion2";

    public static void addAccount(Context context, String username, String password) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = new Account(username, TYPE_ACCOUNT);
        accountManager.addAccountExplicitly(account, password, null);
    }

    public static String getPassword(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = null;
        try {
            account = accountManager.getAccountsByType(TYPE_ACCOUNT)[0];
            return accountManager.getPassword(account);
        } catch (Exception ignored) {

        }
        return null;
    }
}
