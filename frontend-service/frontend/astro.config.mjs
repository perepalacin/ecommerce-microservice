import { defineConfig } from 'astro/config';
import preact from '@astrojs/preact';

export default defineConfig({
  outDir: '../src/main/resources/static',
  integrations: [preact()],
});