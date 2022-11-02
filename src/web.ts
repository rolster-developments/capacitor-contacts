import { WebPlugin } from '@capacitor/core';
import { ContactList, ContactsPlugin, PermissionsStatus } from './definitions';

export class ContactsWeb extends WebPlugin implements ContactsPlugin {
  constructor() {
    super({
      name: 'Contacts',
      platforms: ['web']
    });
  }

  public async getPermissions(): Promise<PermissionsStatus> {
    return {
      granted: false
    };
  }

  public async getContacts(): Promise<ContactList> {
    return {
      contacts: [
        {
          contactId: '30199105',
          displayName: 'Daniel Andrés Castillo Pedroza',
          phoneNumbers: [
            {
              number: '3168213597',
              label: 'Personal'
            },
            {
              number: '3004636444',
              label: 'Work'
            }
          ],
          emails: []
        },
        {
          contactId: '52315521',
          displayName: 'Ricardo Andrés Urrego',
          phoneNumbers: [
            {
              number: '3025668712',
              label: 'Personal'
            }
          ],
          emails: []
        },
        {
          contactId: '62511202',
          displayName: 'Cristian Yair Melgarejo Florez',
          phoneNumbers: [
            {
              number: '318251341',
              label: 'Personal'
            }
          ],
          emails: []
        }
      ]
    };
  }
}
