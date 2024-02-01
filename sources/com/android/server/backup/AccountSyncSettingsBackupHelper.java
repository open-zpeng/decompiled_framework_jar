package com.android.server.backup;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.backup.BackupDataInputStream;
import android.app.backup.BackupDataOutput;
import android.app.backup.BackupHelper;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncAdapterType;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes3.dex */
public class AccountSyncSettingsBackupHelper implements BackupHelper {
    private static final boolean DEBUG = false;
    private static final String JSON_FORMAT_ENCODING = "UTF-8";
    private static final String JSON_FORMAT_HEADER_KEY = "account_data";
    private static final int JSON_FORMAT_VERSION = 1;
    private static final String KEY_ACCOUNTS = "accounts";
    private static final String KEY_ACCOUNT_AUTHORITIES = "authorities";
    private static final String KEY_ACCOUNT_NAME = "name";
    private static final String KEY_ACCOUNT_TYPE = "type";
    private static final String KEY_AUTHORITY_NAME = "name";
    private static final String KEY_AUTHORITY_SYNC_ENABLED = "syncEnabled";
    private static final String KEY_AUTHORITY_SYNC_STATE = "syncState";
    private static final String KEY_MASTER_SYNC_ENABLED = "masterSyncEnabled";
    private static final String KEY_VERSION = "version";
    private static final int MD5_BYTE_SIZE = 16;
    private static final String STASH_FILE = Environment.getDataDirectory() + "/backup/unadded_account_syncsettings.json";
    private static final int STATE_VERSION = 1;
    private static final int SYNC_REQUEST_LATCH_TIMEOUT_SECONDS = 1;
    private static final String TAG = "AccountSyncSettingsBackupHelper";
    private AccountManager mAccountManager;
    private Context mContext;

    public AccountSyncSettingsBackupHelper(Context context) {
        this.mContext = context;
        this.mAccountManager = AccountManager.get(this.mContext);
    }

    @Override // android.app.backup.BackupHelper
    public void performBackup(ParcelFileDescriptor oldState, BackupDataOutput output, ParcelFileDescriptor newState) {
        try {
            JSONObject dataJSON = serializeAccountSyncSettingsToJSON();
            byte[] dataBytes = dataJSON.toString().getBytes(JSON_FORMAT_ENCODING);
            byte[] oldMd5Checksum = readOldMd5Checksum(oldState);
            byte[] newMd5Checksum = generateMd5Checksum(dataBytes);
            if (!Arrays.equals(oldMd5Checksum, newMd5Checksum)) {
                int dataSize = dataBytes.length;
                output.writeEntityHeader(JSON_FORMAT_HEADER_KEY, dataSize);
                output.writeEntityData(dataBytes, dataSize);
                Log.i(TAG, "Backup successful.");
            } else {
                Log.i(TAG, "Old and new MD5 checksums match. Skipping backup.");
            }
            writeNewMd5Checksum(newState, newMd5Checksum);
        } catch (IOException | NoSuchAlgorithmException | JSONException e) {
            Log.e(TAG, "Couldn't backup account sync settings\n" + e);
        }
    }

    private JSONObject serializeAccountSyncSettingsToJSON() throws JSONException {
        Account[] accounts;
        Account[] accounts2 = this.mAccountManager.getAccounts();
        SyncAdapterType[] syncAdapters = ContentResolver.getSyncAdapterTypesAsUser(this.mContext.getUserId());
        HashMap<String, List<String>> accountTypeToAuthorities = new HashMap<>();
        int i = 0;
        for (SyncAdapterType syncAdapter : syncAdapters) {
            if (syncAdapter.isUserVisible()) {
                if (!accountTypeToAuthorities.containsKey(syncAdapter.accountType)) {
                    accountTypeToAuthorities.put(syncAdapter.accountType, new ArrayList<>());
                }
                accountTypeToAuthorities.get(syncAdapter.accountType).add(syncAdapter.authority);
            }
        }
        JSONObject backupJSON = new JSONObject();
        backupJSON.put("version", 1);
        backupJSON.put(KEY_MASTER_SYNC_ENABLED, ContentResolver.getMasterSyncAutomatically());
        JSONArray accountJSONArray = new JSONArray();
        int length = accounts2.length;
        while (i < length) {
            Account account = accounts2[i];
            List<String> authorities = accountTypeToAuthorities.get(account.type);
            if (authorities == null) {
                accounts = accounts2;
            } else if (authorities.isEmpty()) {
                accounts = accounts2;
            } else {
                JSONObject accountJSON = new JSONObject();
                accountJSON.put("name", account.name);
                accountJSON.put("type", account.type);
                JSONArray authoritiesJSONArray = new JSONArray();
                for (String authority : authorities) {
                    int syncState = ContentResolver.getIsSyncable(account, authority);
                    boolean syncEnabled = ContentResolver.getSyncAutomatically(account, authority);
                    JSONObject authorityJSON = new JSONObject();
                    authorityJSON.put("name", authority);
                    authorityJSON.put(KEY_AUTHORITY_SYNC_STATE, syncState);
                    authorityJSON.put(KEY_AUTHORITY_SYNC_ENABLED, syncEnabled);
                    authoritiesJSONArray.put(authorityJSON);
                    accounts2 = accounts2;
                }
                accounts = accounts2;
                accountJSON.put("authorities", authoritiesJSONArray);
                accountJSONArray.put(accountJSON);
            }
            i++;
            accounts2 = accounts;
        }
        backupJSON.put("accounts", accountJSONArray);
        return backupJSON;
    }

    private byte[] readOldMd5Checksum(ParcelFileDescriptor oldState) throws IOException {
        DataInputStream dataInput = new DataInputStream(new FileInputStream(oldState.getFileDescriptor()));
        byte[] oldMd5Checksum = new byte[16];
        try {
            int stateVersion = dataInput.readInt();
            if (stateVersion <= 1) {
                for (int i = 0; i < 16; i++) {
                    oldMd5Checksum[i] = dataInput.readByte();
                }
            } else {
                Log.i(TAG, "Backup state version is: " + stateVersion + " (support only up to version 1)");
            }
        } catch (EOFException e) {
        }
        return oldMd5Checksum;
    }

    private void writeNewMd5Checksum(ParcelFileDescriptor newState, byte[] md5Checksum) throws IOException {
        DataOutputStream dataOutput = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(newState.getFileDescriptor())));
        dataOutput.writeInt(1);
        dataOutput.write(md5Checksum);
    }

    private byte[] generateMd5Checksum(byte[] data) throws NoSuchAlgorithmException {
        if (data == null) {
            return null;
        }
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return md5.digest(data);
    }

    @Override // android.app.backup.BackupHelper
    public void restoreEntity(BackupDataInputStream data) {
        byte[] dataBytes = new byte[data.size()];
        try {
            data.read(dataBytes);
            String dataString = new String(dataBytes, JSON_FORMAT_ENCODING);
            JSONObject dataJSON = new JSONObject(dataString);
            boolean masterSyncEnabled = dataJSON.getBoolean(KEY_MASTER_SYNC_ENABLED);
            JSONArray accountJSONArray = dataJSON.getJSONArray("accounts");
            boolean currentMasterSyncEnabled = ContentResolver.getMasterSyncAutomatically();
            if (currentMasterSyncEnabled) {
                ContentResolver.setMasterSyncAutomatically(false);
            }
            restoreFromJsonArray(accountJSONArray);
            ContentResolver.setMasterSyncAutomatically(masterSyncEnabled);
            Log.i(TAG, "Restore successful.");
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Couldn't restore account sync settings\n" + e);
        }
    }

    private void restoreFromJsonArray(JSONArray accountJSONArray) throws JSONException {
        HashSet<Account> currentAccounts = getAccounts();
        JSONArray unaddedAccountsJSONArray = new JSONArray();
        for (int i = 0; i < accountJSONArray.length(); i++) {
            JSONObject accountJSON = (JSONObject) accountJSONArray.get(i);
            String accountName = accountJSON.getString("name");
            String accountType = accountJSON.getString("type");
            try {
                Account account = new Account(accountName, accountType);
                if (currentAccounts.contains(account)) {
                    restoreExistingAccountSyncSettingsFromJSON(accountJSON);
                } else {
                    unaddedAccountsJSONArray.put(accountJSON);
                }
            } catch (IllegalArgumentException e) {
            }
        }
        int i2 = unaddedAccountsJSONArray.length();
        if (i2 > 0) {
            try {
                FileOutputStream fOutput = new FileOutputStream(STASH_FILE);
                String jsonString = unaddedAccountsJSONArray.toString();
                DataOutputStream out = new DataOutputStream(fOutput);
                out.writeUTF(jsonString);
                $closeResource(null, fOutput);
                return;
            } catch (IOException ioe) {
                Log.e(TAG, "unable to write the sync settings to the stash file", ioe);
                return;
            }
        }
        File stashFile = new File(STASH_FILE);
        if (stashFile.exists()) {
            stashFile.delete();
        }
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 == null) {
            x1.close();
            return;
        }
        try {
            x1.close();
        } catch (Throwable th) {
            x0.addSuppressed(th);
        }
    }

    private void accountAddedInternal() {
        try {
            FileInputStream fIn = new FileInputStream(new File(STASH_FILE));
            DataInputStream in = new DataInputStream(fIn);
            String jsonString = in.readUTF();
            $closeResource(null, fIn);
            try {
                JSONArray unaddedAccountsJSONArray = new JSONArray(jsonString);
                restoreFromJsonArray(unaddedAccountsJSONArray);
            } catch (JSONException jse) {
                Log.e(TAG, "there was an error with the stashed sync settings", jse);
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e2) {
        }
    }

    public static void accountAdded(Context context) {
        AccountSyncSettingsBackupHelper helper = new AccountSyncSettingsBackupHelper(context);
        helper.accountAddedInternal();
    }

    private HashSet<Account> getAccounts() {
        Account[] accounts = this.mAccountManager.getAccounts();
        HashSet<Account> accountHashSet = new HashSet<>();
        for (Account account : accounts) {
            accountHashSet.add(account);
        }
        return accountHashSet;
    }

    private void restoreExistingAccountSyncSettingsFromJSON(JSONObject accountJSON) throws JSONException {
        int i;
        JSONArray authorities = accountJSON.getJSONArray("authorities");
        String accountName = accountJSON.getString("name");
        String accountType = accountJSON.getString("type");
        Account account = new Account(accountName, accountType);
        for (int i2 = 0; i2 < authorities.length(); i2++) {
            JSONObject authority = (JSONObject) authorities.get(i2);
            String authorityName = authority.getString("name");
            boolean wasSyncEnabled = authority.getBoolean(KEY_AUTHORITY_SYNC_ENABLED);
            int wasSyncable = authority.getInt(KEY_AUTHORITY_SYNC_STATE);
            ContentResolver.setSyncAutomaticallyAsUser(account, authorityName, wasSyncEnabled, 0);
            if (!wasSyncEnabled) {
                if (wasSyncable == 0) {
                    i = 0;
                } else {
                    i = 2;
                }
                ContentResolver.setIsSyncable(account, authorityName, i);
            }
        }
    }

    @Override // android.app.backup.BackupHelper
    public void writeNewStateDescription(ParcelFileDescriptor newState) {
    }
}
