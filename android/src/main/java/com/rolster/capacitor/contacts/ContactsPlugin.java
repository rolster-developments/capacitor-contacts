package com.rolster.capacitor.contacts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.util.Base64;
import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;
import java.util.HashMap;
import org.json.JSONException;

@CapacitorPlugin(
  name = "Contacts",
  permissions = {
    @Permission(
      alias = ContactsPlugin.CONTACTS_ALIAS,
      strings = { Manifest.permission.READ_CONTACTS }
    )
  }
)
public class ContactsPlugin extends Plugin {

  public static final String CONTACTS_ALIAS = "contacts";

  private static final String CONTACT_ID = "contactId";
  private static final String EMAILS = "emails";
  private static final String EMAIL_LABEL = "label";
  private static final String EMAIL_ADDRESS = "address";
  private static final String PHONE_NUMBERS = "phoneNumbers";
  private static final String PHONE_LABEL = "label";
  private static final String PHONE_NUMBER = "number";
  private static final String DISPLAY_NAME = "displayName";
  private static final String PHOTO_THUMBNAIL = "photoThumbnail";
  private static final String ORGANIZATION_NAME = "organizationName";
  private static final String ORGANIZATION_ROLE = "organizationRole";
  private static final String BIRTHDAY = "birthday";

  @PluginMethod
  public void hasPermissions(PluginCall call) {
    if (hasPermission()) {
      requestPermissions(call);
    } else {
      JSObject result = new JSObject();
      result.put("granted", true);
      call.resolve(result);
    }
  }

  @SuppressLint("Range")
  @PluginMethod
  public void getContacts(PluginCall call) {
    JSArray jsContacts = new JSArray();

    ContentResolver contentResolver = getContext().getContentResolver();

    String[] projection = new String[] {
      ContactsContract.Data.MIMETYPE,
      Organization.TITLE,
      ContactsContract.Contacts._ID,
      ContactsContract.Data.CONTACT_ID,
      ContactsContract.Contacts.DISPLAY_NAME,
      ContactsContract.Contacts.Photo.PHOTO,
      ContactsContract.CommonDataKinds.Contactables.DATA,
      ContactsContract.CommonDataKinds.Contactables.TYPE,
      ContactsContract.CommonDataKinds.Contactables.LABEL
    };

    String selection = ContactsContract.Data.MIMETYPE + " in (?, ?, ?, ?, ?)";

    String[] selectionArgs = new String[] {
      Email.CONTENT_ITEM_TYPE,
      Phone.CONTENT_ITEM_TYPE,
      Event.CONTENT_ITEM_TYPE,
      Organization.CONTENT_ITEM_TYPE,
      Photo.CONTENT_ITEM_TYPE
    };

    Cursor contactsCursor = contentResolver.query(
      ContactsContract.Data.CONTENT_URI,
      projection,
      selection,
      selectionArgs,
      null
    );

    if (contactsCursor != null && contactsCursor.getCount() > 0) {
      HashMap<Object, JSObject> contactsById = new HashMap<>();

      while (contactsCursor.moveToNext()) {
        String _id = contactsCursor.getString(
          contactsCursor.getColumnIndex(ContactsContract.Contacts._ID)
        );
        String contactId = contactsCursor.getString(
          contactsCursor.getColumnIndex(ContactsContract.Data.CONTACT_ID)
        );

        JSObject jsContact = new JSObject();

        if (!contactsById.containsKey(contactId)) {
          jsContact.put(CONTACT_ID, contactId);

          String displayName = contactsCursor.getString(
            contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
          );

          jsContact.put(DISPLAY_NAME, displayName);
          JSArray jsPhoneNumbers = new JSArray();
          jsContact.put(PHONE_NUMBERS, jsPhoneNumbers);
          JSArray jsEmailAddresses = new JSArray();
          jsContact.put(EMAILS, jsEmailAddresses);

          jsContacts.put(jsContact);
        } else {
          jsContact = contactsById.get(contactId);
        }

        if (jsContact != null) {
          String mimeType = contactsCursor.getString(
            contactsCursor.getColumnIndex(ContactsContract.Data.MIMETYPE)
          );
          String data = contactsCursor.getString(
            contactsCursor.getColumnIndex(
              ContactsContract.CommonDataKinds.Contactables.DATA
            )
          );
          int type = contactsCursor.getInt(
            contactsCursor.getColumnIndex(
              ContactsContract.CommonDataKinds.Contactables.TYPE
            )
          );
          String label = contactsCursor.getString(
            contactsCursor.getColumnIndex(
              ContactsContract.CommonDataKinds.Contactables.LABEL
            )
          );

          // email
          switch (mimeType) {
            case Email.CONTENT_ITEM_TYPE:
              try {
                // add this email to the list
                JSArray emailAddresses = (JSArray) jsContact.get(EMAILS);
                JSObject jsEmail = new JSObject();
                jsEmail.put(EMAIL_LABEL, mapEmailTypeToLabel(type, label));
                jsEmail.put(EMAIL_ADDRESS, data);
                emailAddresses.put(jsEmail);
              } catch (JSONException e) {
                e.printStackTrace();
              }
              break;
            // phone
            case Phone.CONTENT_ITEM_TYPE:
              try {
                // add this phone to the list
                JSArray jsPhoneNumbers = (JSArray) jsContact.get(PHONE_NUMBERS);
                JSObject jsPhone = new JSObject();
                jsPhone.put(PHONE_LABEL, mapPhoneTypeToLabel(type, label));
                jsPhone.put(PHONE_NUMBER, data);
                jsPhoneNumbers.put(jsPhone);
              } catch (JSONException e) {
                e.printStackTrace();
              }
              break;
            // birthday
            case Event.CONTENT_ITEM_TYPE:
              int eventType = contactsCursor.getInt(
                contactsCursor.getColumnIndex(
                  ContactsContract.CommonDataKinds.Contactables.TYPE
                )
              );

              if (eventType == Event.TYPE_BIRTHDAY) {
                jsContact.put(BIRTHDAY, data);
              }
              break;
            // organization
            case Organization.CONTENT_ITEM_TYPE:
              jsContact.put(ORGANIZATION_NAME, data);

              String organizationRole = contactsCursor.getString(
                contactsCursor.getColumnIndex(Organization.TITLE)
              );

              if (organizationRole != null) {
                jsContact.put(ORGANIZATION_ROLE, organizationRole);
              }
              break;
            // photo
            case Photo.CONTENT_ITEM_TYPE:
              byte[] thumbnailPhoto = contactsCursor.getBlob(
                contactsCursor.getColumnIndex(ContactsContract.Contacts.Photo.PHOTO)
              );

              if (thumbnailPhoto != null) {
                String encodedThumbnailPhoto = Base64.encodeToString(
                  thumbnailPhoto,
                  Base64.NO_WRAP
                );
                jsContact.put(
                  PHOTO_THUMBNAIL,
                  "data:image/png;base64," + encodedThumbnailPhoto
                );
              }
              break;
          }

          contactsById.put(contactId, jsContact);
        }
      }
    }

    if (contactsCursor != null) {
      contactsCursor.close();
    }

    JSObject result = new JSObject();
    result.put("contacts", jsContacts);
    call.resolve(result);
  }

  @PermissionCallback
  private void contactsPermissionsCallback(PluginCall call) {
    JSObject result = new JSObject();

    if (hasPermission()) {
      result.put("granted", true);
    } else {
      result.put("granted", false);
    }

    call.resolve(result);
  }

  private String mapPhoneTypeToLabel(int type, String defaultLabel) {
    switch (type) {
      case Phone.TYPE_MOBILE:
        return "mobile";
      case Phone.TYPE_HOME:
        return "home";
      case Phone.TYPE_WORK:
        return "work";
      case Phone.TYPE_FAX_WORK:
        return "fax work";
      case Phone.TYPE_FAX_HOME:
        return "fax home";
      case Phone.TYPE_PAGER:
        return "pager";
      case Phone.TYPE_OTHER:
        return "other";
      case Phone.TYPE_CALLBACK:
        return "callback";
      case Phone.TYPE_CAR:
        return "car";
      case Phone.TYPE_COMPANY_MAIN:
        return "company main";
      case Phone.TYPE_ISDN:
        return "isdn";
      case Phone.TYPE_MAIN:
        return "main";
      case Phone.TYPE_OTHER_FAX:
        return "other fax";
      case Phone.TYPE_RADIO:
        return "radio";
      case Phone.TYPE_TELEX:
        return "telex";
      case Phone.TYPE_TTY_TDD:
        return "tty";
      case Phone.TYPE_WORK_MOBILE:
        return "work mobile";
      case Phone.TYPE_WORK_PAGER:
        return "work pager";
      case Phone.TYPE_ASSISTANT:
        return "assistant";
      case Phone.TYPE_MMS:
        return "mms";
      default:
        return defaultLabel;
    }
  }

  private String mapEmailTypeToLabel(int type, String defaultLabel) {
    switch (type) {
      case Email.TYPE_HOME:
        return "home";
      case Email.TYPE_WORK:
        return "work";
      case Email.TYPE_OTHER:
        return "other";
      case Email.TYPE_MOBILE:
        return "mobile";
      default:
        return defaultLabel;
    }
  }

  private boolean hasPermission() {
    return getPermissionState(CONTACTS_ALIAS) == PermissionState.GRANTED;
  }
}
