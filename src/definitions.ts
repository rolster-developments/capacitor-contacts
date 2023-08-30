export interface PhoneNumber {
  label?: string;
  number?: string;
}

export interface EmailAddress {
  label?: string;
  address?: string;
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

export interface ContactList {
  contacts: Contact[];
}

export interface PermissionsStatus {
  granted: boolean;
}

export interface ContactsPlugin {
  hasPermissions(): Promise<PermissionsStatus>;
  getContacts(): Promise<ContactList>;
}
