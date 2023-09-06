# Rolster Capacitor Contacts

Use plugin for manage contact list of Device.

## Installation

Package only supports Capacitor 5

```
npm i @rolster/capacitor-contacts
```

### iOS Config

To access the contact list make sure you provide a value for NSContactsUsageDescription; otherwise your app may crash on iOS devices. You should add something like the following example to App/info.plist:

```xml
<key>NSContactsUsageDescription</key>
<string>We need access to your contacts in order to do something.</string>
```

### Android Config

For Android you have to add the permissions in your `AndroidManifest.xml`. Add the following permissions before the closing of the `manifest` tag.

```xml
<uses-permission android:name="android.permission.READ_CONTACTS" />
```

And register the plugin by adding it to you MainActivity's onCreate:

```java
import com.rolster.capacitor.contacts.Contacts;

public class MainActivity extends BridgeActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    registerPlugin(Contacts.class);
    // Others register plugins

    super.onCreate(savedInstanceState);
  }
}
```

**NOTE**: On Android you have to ask for permission first, before you can fetch the contacts. Use the `hasPermissions()` method before you try to fetch contacts using `getContacts()`.
