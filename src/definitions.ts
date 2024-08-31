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

export interface HasPermissionsResult {
  granted: boolean;
  readContacts?: string;
}

export interface RequestResult {
  contacts: Contact[];
}

export interface ContactsPlugin {
  hasPermissions(): Promise<HasPermissionsResult>;
  request(): Promise<RequestResult>;
}
