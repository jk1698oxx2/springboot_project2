import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';
import UpdTime from '../views/UpdTime.vue';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: UpdTime
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;