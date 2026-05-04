import tailwindcss from '@tailwindcss/vite';
import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vite';
import { viteSingleFile } from 'vite-plugin-singlefile';

export default defineConfig({
    plugins: [
        tailwindcss(),
        sveltekit()
    ],
    build: {
		assetsInlineLimit: Infinity
	}
});
 