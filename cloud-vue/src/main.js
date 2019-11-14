import Vue from 'vue'
import App from './App'
import router from './router'

import ElementUI from 'element-ui';
// import 'element-ui/lib/theme-chalk/index.css';
import './assets/theme/index.css'

Vue.use(ElementUI);

import CloudLine from './components/line';
Vue.use(CloudLine);

import CloudPie from './components/pie';
Vue.use(CloudPie);

import DrawPie from './components/drawpie';
Vue.use(DrawPie);

import * as filters from './assets/js/filters.js'
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key]);
})

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
