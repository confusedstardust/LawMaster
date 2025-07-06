import {
	createSSRApp
} from "vue";
import quill from "quill";
window.Quill = quill;
import App from "./App.vue";
export function createApp() {
	const app = createSSRApp(App);
	return {
		app,
	};
}
