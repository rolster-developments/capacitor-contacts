import { registerPlugin } from '@capacitor/core';
import { ContactsPlugin } from './definitions';

const Contacts = registerPlugin<ContactsPlugin>('Contacts', {
  web: () => import('./web').then((m) => new m.ContactsWeb())
});

export * from './definitions';
export { setContactsMocks as setContactsWebData } from './web';
export { Contacts };
