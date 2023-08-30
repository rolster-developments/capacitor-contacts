import { WebPlugin } from '@capacitor/core';
import {
  Contact,
  ContactList,
  ContactsPlugin,
  PermissionsStatus
} from './definitions';

interface WebPluginResults {
  contacts: Contact[];
  granted: boolean;
}

let webResults: WebPluginResults = {
  contacts: [],
  granted: false
};

export function setWebPluginResults(results: Partial<WebPluginResults>): void {
  webResults = { ...webResults, ...results };
}

export class ContactsWeb extends WebPlugin implements ContactsPlugin {
  constructor() {
    super();
  }

  public async hasPermissions(): Promise<PermissionsStatus> {
    const { granted } = webResults;

    return { granted };
  }

  public async getContacts(): Promise<ContactList> {
    const { contacts } = webResults;

    return { contacts };
  }
}
