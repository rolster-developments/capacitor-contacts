# Rolster Contacts

Use plugin for manage contact list of Device.

### iOS

For iOS you need to set a usage description in your info.plist file. (Privacy Setting)
Open xCode search for your info.plist file and press the tiny "+". Add the following entry:

```
Privacy - Contacts Usage Description
```

Give it a value like:

```
"We need access to your contacts in order to do something."
```

### Android Notes

For Android you have to add the permissions in your AndroidManifest.xml. Add the following permissions before the closing of the "manifest" tag.

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

**NOTE**: On Android you have to ask for permission first, before you can fetch the contacts. Use the `grantPermissions()` method before you try to fetch contacts using `getContacts()`.
