package com.tylersuehr.chipexample;
import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * This activity is just to load contact information on the phone using the LoaderManager
 * APIs, and then callback with the data.
 *
 * The reason for this abstraction is so that it's easy to comprehend the code specific
 * to the chips in {@link ExampleChipsActivity}, as this is an example to observe how
 * to use the chips, not load contact information.
 *
 * Simply call {@link #loadContactsWithRuntimePermission()} to load contacts. Runtime
 * permissions must be granted for API 23+
 *
 * @author Tyler Suehr
 * @version 1.0
 */
abstract class ContactLoadingActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,// Content provider
                null, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY + " ASC"); // OrderBy clause
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Collect all the contact information from the Cursor in a List
        List<ContactChip> chips = new ArrayList<>(data.getCount());
        for (int i = 0; i < data.getCount(); i++) {
            data.moveToPosition(i);

            ContactChip chip = new ContactChip();
            chip.setId(i);
            chip.setName(data.getString(data.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)));
            chip.setPhone(data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            int phoneType = data.getInt(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
            chip.setPhoneType(ContactsContract.CommonDataKinds.Phone.getTypeLabel(getResources(), phoneType, "").toString());

            String avatar = data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
            if (avatar != null) {
                chip.setAvatarUri(Uri.parse(avatar));
            }

            chips.add(chip);
        }

        // Set the list of chips on the ChipsInput and normal adapter
        onContactsAvailable(chips);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        onContactsReset();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            // If the request is cancelled, the result arrays are empty
            boolean allGranted = false;
            for (int grantResult : grantResults) {
                allGranted = grantResult == PackageManager.PERMISSION_GRANTED;
                if (!allGranted) { break; }
            }

            if (allGranted) {
                // Permissions were granted, yay!!
                getSupportLoaderManager().initLoader(0, null, this);
            } else {
                // Permissions were denied! User is a piece of shit
                Toast.makeText(this, "Permissions are needed to load your contacts!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    protected void loadContactsWithRuntimePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Runtime permission must be granted for
            // (1) Reading contacts
            // (2) Sending SMS messages
            String[] permissions = new String[] {
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.READ_PHONE_STATE
            };

            // If the app doesn't have all the permissions above, request for them!
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                getSupportLoaderManager().initLoader(0, null, this);
            } else {
                ActivityCompat.requestPermissions(this, permissions, 0);
            }
        } else {
            // APIs older than Lollipop don't have runtime permissions :)
            getSupportLoaderManager().initLoader(0, null, this);
        }
    }

    /**
     * Called when the current user's contacts are available as a list of {@link ContactChip}.
     *
     * @param chips List of {@link ContactChip}
     */
    protected abstract void onContactsAvailable(List<ContactChip> chips);

    /**
     * Called when the current user's contacts should be reset.
     */
    protected abstract void onContactsReset();
}