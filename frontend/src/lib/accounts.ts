import { currencyToId, type CurrencyCode } from './currency';

type BrowserLocation = Pick<Location, 'protocol' | 'hostname'>;

export function getAtmApiUrl(location?: BrowserLocation) {
	const currentLocation = location ?? (typeof window === 'undefined' ? undefined : window.location);

	if (!currentLocation) {
		return 'http://localhost:8084/api/v1/atm';
	}

	return `${currentLocation.protocol}//${currentLocation.hostname}:8084/api/v1/atm`;
}

export const ATM_API_URL = getAtmApiUrl();

export type AccountId = Account['id'] | string | null;

export type CreateAccountPayload = {
	ownerName?: string;
	balance?: number;
	currency?: CurrencyCode;
};

export type UpdateAccountPayload = Pick<Account, 'ownerName' | 'balance' | 'accountStatus'> & {
	currency: CurrencyCode;
};

export function accountToFormData(account: Account): UpdateAccountPayload {
	return {
		ownerName: account.ownerName,
		balance: account.balance,
		accountStatus: account.accountStatus,
		currency: currencyToId[account.currency]
	};
}

export async function fetchAccounts(): Promise<Account[]> {
	return fetch(ATM_API_URL).then((response) => response.json());
}

export async function fetchAccount(id: AccountId): Promise<Account> {
	return fetch(`${ATM_API_URL}/${id}`).then((response) => response.json());
}

export async function createAccount(payload: CreateAccountPayload): Promise<void> {
	await fetch(ATM_API_URL, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(payload)
	});
}

export async function updateAccount(id: AccountId, payload: UpdateAccountPayload): Promise<void> {
	await fetch(`${ATM_API_URL}/${id}`, {
		method: 'PATCH',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(payload)
	});
}

export async function deleteAccount(id: AccountId): Promise<void> {
	await fetch(`${ATM_API_URL}/${id}`, { method: 'DELETE' });
}
