import { describe, expect, it } from 'vitest';

import { currencyToId, idToCurrency } from '../src/lib/currency';

describe('currency mappings', () => {
	it('keeps API currency ids mapped to frontend currency codes', () => {
		expect(idToCurrency).toEqual({
			0: 'rub',
			1: 'usd',
			2: 'eur'
		});
	});

	it('builds the reverse mapping used by edit forms', () => {
		expect(currencyToId).toEqual({
			rub: '0',
			usd: '1',
			eur: '2'
		});
	});

	it('has a reverse mapping for every available currency code', () => {
		for (const [id, currency] of Object.entries(idToCurrency)) {
			expect(currencyToId[currency]).toBe(id);
		}
	});

	it('does not expose duplicate currency codes', () => {
		const currencies = Object.values(idToCurrency);

		expect(new Set(currencies).size).toBe(currencies.length);
	});
});
