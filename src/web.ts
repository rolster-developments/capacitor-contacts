import { WebPlugin } from '@capacitor/core';
import {
  Contact,
  ContactList,
  ContactsPlugin,
  PermissionsStatus
} from './definitions';

interface ContactsWebData {
  granted: boolean;
  contacts: Contact[];
}

let contactsWebData: ContactsWebData = {
  granted: true,
  contacts: []
};

export function setContactsWebData(data: Partial<ContactsWebData>): void {
  contactsWebData = { ...contactsWebData, ...data };
}

export class ContactsWeb extends WebPlugin implements ContactsPlugin {
  public hasPermissions(): Promise<PermissionsStatus> {
    const { granted } = contactsWebData;

    return Promise.resolve({ granted });
  }

  public getContacts(): Promise<ContactList> {
    const { contacts, granted } = contactsWebData;

    return Promise.resolve({ contacts: granted ? contacts : [] });
  }
}
