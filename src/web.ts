import { WebPlugin } from '@capacitor/core';
import {
  Contact,
  ContactList,
  ContactsPlugin,
  PermissionsStatus
} from './definitions';

export class ContactsWeb extends WebPlugin implements ContactsPlugin {
  constructor(private _contacts: Contact[] = [], private _granted = true) {
    super({
      name: 'Contacts',
      platforms: ['web']
    });
  }

  public async grantPermissions(): Promise<PermissionsStatus> {
    return {
      granted: this._granted
    };
  }

  public async getContacts(): Promise<ContactList> {
    return {
      contacts: this._contacts
    };
  }
}
