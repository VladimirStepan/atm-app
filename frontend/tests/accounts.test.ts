import { afterEach, describe, expect, it, vi } from 'vitest';

import {
	ATM_API_URL,
	accountToFormData,
	createAccount,
	deleteAccount,
	fetchAccount,
	fetchAccounts,
	getAtmApiUrl,
	updateAccount
} from '../src/lib/accounts';

const account: Account = {
	id: 7,
	ownerName: 'Ada Lovelace',
	balance: 1200,
	currency: 'usd',
	accountStatus: 'active',
	createdAt: '2026-05-04T00:00:00.000Z',
	updatedAt: '2026-05-04T00:00:00.000Z'
};

function mockFetchJson(data: unknown) {
	const fetchMock = vi.fn().mockResolvedValue({
		json: vi.fn().mockResolvedValue(data)
	});

	vi.stubGlobal('fetch', fetchMock);

	return fetchMock;
}

describe('accounts API module', () => {
	afterEach(() => {
		vi.unstubAllGlobals();
	});

	it('loads the account list from the ATM API', async () => {
		const fetchMock = mockFetchJson([account]);

		await expect(fetchAccounts()).resolves.toEqual([account]);
		expect(fetchMock).toHaveBeenCalledWith(ATM_API_URL);
	});

	it('builds the ATM API URL from browser location', () => {
		expect(getAtmApiUrl({
			protocol: 'https:',
			hostname: 'atm.example.test'
		})).toBe('https://atm.example.test:8084/api/v1/atm');
	});

	it('loads a single account by id', async () => {
		const fetchMock = mockFetchJson(account);

		await expect(fetchAccount(account.id)).resolves.toEqual(account);
		expect(fetchMock).toHaveBeenCalledWith(`${ATM_API_URL}/${account.id}`);
	});

	it('creates an account with JSON payload', async () => {
		const fetchMock = mockFetchJson(undefined);
		const payload = { ownerName: 'Grace Hopper', balance: 500, currency: 1 as const };

		await createAccount(payload);

		expect(fetchMock).toHaveBeenCalledWith(ATM_API_URL, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(payload)
		});
	});

	it('updates an account with JSON payload', async () => {
		const fetchMock = mockFetchJson(undefined);
		const payload = accountToFormData(account);

		await updateAccount(account.id, payload);

		expect(fetchMock).toHaveBeenCalledWith(`${ATM_API_URL}/${account.id}`, {
			method: 'PATCH',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(payload)
		});
	});

	it('deletes an account by id', async () => {
		const fetchMock = mockFetchJson(undefined);

		await deleteAccount(account.id);

		expect(fetchMock).toHaveBeenCalledWith(`${ATM_API_URL}/${account.id}`, { method: 'DELETE' });
	});

	it('converts an account to edit form data', () => {
		expect(accountToFormData(account)).toEqual({
			ownerName: account.ownerName,
			balance: account.balance,
			accountStatus: account.accountStatus,
			currency: '1'
		});
	});
});
