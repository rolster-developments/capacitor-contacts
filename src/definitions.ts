declare module '@capacitor/core' {
  interface PluginRegistry {
    Contacts: ContactsPlugin;
  }
}

export interface ContactsPlugin {
  grantPermissions(): Promise<PermissionsStatus>;
  getContacts(): Promise<ContactList>;
}

export interface PermissionsStatus {
  granted: boolean;
}

export interface ContactList {
  contacts: Contact[];
}

export interface Contact {
  contactId: string;
  displayName?: string;
  phoneNumbers: PhoneNumber[];
  emails: EmailAddress[];
  photoThumbnail?: string;
  organizationName?: string;
  organizationRole?: string;
  birthday?: string;
}

export interface PhoneNumber {
  label?: string;
  number?: string;
}

export interface EmailAddress {
  label?: string;
  address?: string;
}
