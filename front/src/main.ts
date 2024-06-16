import "./assets/main.css";
import "reflect-metadata";

import { createApp } from "vue";
import ElementPlus from "element-plus";
import "normalize.css";
import "element-plus/dist/index.css";
import "bootstrap/dist/css/bootstrap-utilities.css";

import App from "./App.vue";
import router from "./router";

const app = createApp(App);
app.use(ElementPlus);
app.use(router);

app.mount("#app");
