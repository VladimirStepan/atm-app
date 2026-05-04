// See https://svelte.dev/docs/kit/types#app.d.ts

import type { Currency } from "$lib/currency.ts";
import { Currency } from './lib/currency.ts';

// for information about these interfaces
declare global {
	namespace App {
		// interface Error {}
		// interface Locals {}
		// interface PageData {}
		// interface PageState {}
		// interface Platform {}
	}

    type Account {
        "id": number,
        "ownerName": string,
        "balance": number,
        "currency": Currency,
        "accountStatus": 'active' | 'frozen' | 'closed',
        "createdAt": string,
        "updatedAt": string
    }

    type AccountFormData = Omit<Account, 'currency'> & { currency: CurrencyCode }
}

export {};
