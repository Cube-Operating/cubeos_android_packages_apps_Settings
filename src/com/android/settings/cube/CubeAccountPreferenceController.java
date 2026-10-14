/*
 * Copyright (C) 2026 The CubeOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.cube;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;

/**
 * The Cubeline card at the top of Settings: shows the phone's Cubeline account ("@username" and
 * the Cubeline), or invites the user to create one. Tapping it opens the account app
 * (org.cubeapp.account), which signs in through Cube App.
 */
public class CubeAccountPreferenceController extends BasePreferenceController {

    static final String ACCOUNT_TYPE = "org.cubeapp.cubeline";
    private static final String CUBE_ACCOUNT_PACKAGE = "org.cubeapp.account";
    private static final String KEY_DISPLAY_NAME = "display_name";

    public CubeAccountPreferenceController(Context context, String key) {
        super(context, key);
    }

    @Override
    public int getAvailabilityStatus() {
        try {
            mContext.getPackageManager().getPackageInfo(CUBE_ACCOUNT_PACKAGE, 0);
            return AVAILABLE;
        } catch (PackageManager.NameNotFoundException e) {
            return UNSUPPORTED_ON_DEVICE;
        }
    }

    @Override
    public CharSequence getSummary() {
        final AccountManager am = AccountManager.get(mContext);
        final Account[] accounts = am.getAccountsByType(ACCOUNT_TYPE);
        if (accounts.length == 0) {
            return mContext.getString(R.string.cube_account_summary);
        }
        final Account account = accounts[0];
        String name = am.getUserData(account, KEY_DISPLAY_NAME);
        if (TextUtils.isEmpty(name)) {
            name = account.name;
        }
        return mContext.getString(R.string.cube_account_signed_in_summary, name, account.name);
    }
}
