import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': {
        // TODO
        // http://localhost:8080
        // https://subscription-manager-wpds.onrender.com
        target: 'https://subscription-manager-wpds.onrender.com',
        changeOrigin: true,
        secure: false,
        rewrite: (path) => path.replace(/^\/api/, ''),
        headers: {
          'Connection': 'keep-alive',
        },
      },
    },
  },
});
