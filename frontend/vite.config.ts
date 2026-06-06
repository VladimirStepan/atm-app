import tailwindcss from '@tailwindcss/vite';
import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vitest/config';

export default defineConfig({
	plugins: [
		tailwindcss(),
		sveltekit()
	],
	build: {
		assetsInlineLimit: Infinity
	},
	test: {
		coverage: {
			provider: 'v8',
			reporter: ['text', 'html', 'lcov'],
			include: [
				'src/lib/accounts.ts',
				'src/lib/currency.ts',
				'src/lib/localization.ts'
			],
			thresholds: {
				lines: 80,
				functions: 80,
				branches: 80,
				statements: 80
			}
		}
	}
});
 
