import { describe, expect, it } from 'vitest';

import { statusCodeToName } from '../src/lib/localization';

describe('status localization', () => {
	it('contains labels for all account statuses returned by the API', () => {
		expect(Object.keys(statusCodeToName).sort()).toEqual(['active', 'closed', 'frozen']);
	});

	it('keeps every status label non-empty', () => {
		for (const label of Object.values(statusCodeToName)) {
			expect(label.trim().length).toBeGreaterThan(0);
		}
	});
});
