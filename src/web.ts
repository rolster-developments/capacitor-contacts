import { WebPlugin } from '@capacitor/core';
import {
  Contact,
  ContactList,
  ContactsPlugin,
  PermissionsStatus
} from './definitions';

interface PluginResults {
  granted: boolean;
  contacts: Contact[];
}

const results: { data: PluginResults } = {
  data: {
    granted: false,
    contacts: []
  }
};

export function setPluginResults(resultsPartial: Partial<PluginResults>): void {
  results.data = { ...results.data, ...resultsPartial };
}

export class ContactsWeb extends WebPlugin implements ContactsPlugin {
  constructor() {
    super();
  }

  public hasPermissions(): Promise<PermissionsStatus> {
    const { granted } = results.data;

    return Promise.resolve({ granted });
  }

  public getContacts(): Promise<ContactList> {
    const { contacts } = results.data;

    return Promise.resolve({ contacts });
  }
}
