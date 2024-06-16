import { fileURLToPath, URL } from "node:url";

import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import vueJsx from "@vitejs/plugin-vue-jsx";
import typescript from "@rollup/plugin-typescript";

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [ vue(),typescript(), vueJsx()],
    resolve: {
        alias: {
            "@": fileURLToPath(new URL("./src", import.meta.url)),
        },
    },
    // css: {
    //    preprocessorOptions: {
    //     scss: {
    //       additionalData: '@import "./src/assets/scss/_variables";',
    //       additionalData: '@import "./src/assets/scss/_mixins";',
    //     }
    //   }
    // },
    server: {
        proxy: {
            "/api": {
                target: "http://localhost:8080",
                rewrite: (path) => path.replace(/^\/api/, ""),
            },
        },
    },
});
