import { WebPlugin } from '@capacitor/core';
import {
  Contact,
  ContactsPlugin,
  HasPermissionsResult,
  RequestResult
} from './definitions';

interface ContactsMocks {
  granted: boolean;
  contacts: Contact[];
}

let pluginMocks: ContactsMocks = {
  granted: true,
  contacts: []
};

export function setContactsMocks(mocks: Partial<ContactsMocks>): void {
  pluginMocks = { ...pluginMocks, ...mocks };
}

export class ContactsWeb extends WebPlugin implements ContactsPlugin {
  public hasPermissions(): Promise<HasPermissionsResult> {
    const { granted } = pluginMocks;

    return Promise.resolve({ granted });
  }

  public request(): Promise<RequestResult> {
    const { contacts, granted } = pluginMocks;

    return Promise.resolve({ contacts: granted ? contacts : [] });
  }
}
