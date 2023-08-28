import { WebPlugin } from '@capacitor/core';
import {
  Contact,
  ContactList,
  ContactsPlugin,
  PermissionsStatus
} from './definitions';

interface WebDataPlugin {
  contacts: Contact[];
  granted: boolean;
}

let webDataPlugin: WebDataPlugin = {
  contacts: [],
  granted: false
};

export function configWebDataPlugin(dataPlugin: Partial<WebDataPlugin>): void {
  webDataPlugin = { ...webDataPlugin, ...dataPlugin };
}

export class ContactsWeb extends WebPlugin implements ContactsPlugin {
  constructor() {
    super({
      name: 'Contacts',
      platforms: ['web']
    });
  }

  public async grantPermissions(): Promise<PermissionsStatus> {
    return {
      granted: webDataPlugin.granted
    };
  }

  public async getContacts(): Promise<ContactList> {
    return {
      contacts: webDataPlugin.contacts
    };
  }
}
