import { createRouter, createWebHistory } from "vue-router";
import { jwtDecode } from "jwt-decode";
import { userStore } from "@/store/userStore.js";

import UserPanel from "@/views/UserPanel.vue";
import Login from "@/views/Login.vue";
import LandingPage from "@/views/LandingPage.vue";
import ProductForm from "@/views/ProductForm.vue";
import AdminPanel from "@/views/AdminPanel.vue";
import CategoryManager from "@/views/CategoryManager.vue";
import ProductDetail from "@/views/ProductDetail.vue";
import CatalogPage from "@/views/CatalogPage.vue";
import CategoryView from "@/views/CategoryView.vue";
import RentalRequestManager from '@/views/RentalRequestManager.vue';
import RentalRequestDetail from '@/views/RentalRequestDetail.vue';

const routes = [
  { 
    path: "/", 
    name: "Landing", 
    component: LandingPage 
  },
  { 
    path: "/home", 
    name: "UserPanel", 
    component: UserPanel,
    meta: { requiresAuth: true, allowedRoles: ["user"] },
  },
  { 
    path: "/login", 
    name: "Login", 
    component: Login 
  },
  {
    path: "/product/add",
    name: "AddProduct",
    component: ProductForm,
    meta: { requiresAuth: true, allowedRoles: ["admin"] },
  },

  {
    path: "/product/:id",
    name: "ProductDetail",
    component: ProductDetail,
    meta: { requiresAuth: true, allowedRoles: ["admin", "user"] },
  },

  {
    path: "/adminHome",
    name: "AdminPanel",
    component: AdminPanel,
    meta: { requiresAuth: true, allowedRoles: ["admin"] },
  },

  {
    path: "/catalog",
    name: "Catalog",
    component: CatalogPage,
    meta: { requiresAuth: true, allowedRoles: ["admin", "user"] },
  },
  { 
    path: "/catalog/:id", 
    name: "CategoryView", 
    component: CategoryView, 
    meta: { requiresAuth: true, allowedRoles: ["admin", "user"] },
  },
  {
    path: "/admin/categories",
    name: "CategoryManager",
    component: CategoryManager,
    meta: { requiresAuth: true, allowedRoles: ["admin"] },
  },
  {
    path: '/rental-requests',
    name: 'RentalRequestManager',
    component: RentalRequestManager,
    meta: {requiresAuth: true, allowedRoles: ['admin']}
  },
  {
    path: '/rental-request/:id',
    name: 'RentalRequestDetail',
    component: RentalRequestDetail,
    meta: {requiresAuth: true, allowedRoles: ['admin', 'user']}
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});


const decodeToken = (token) => {
  if (!token) return { payload: null, expired: false, invalid: false };

  try {
    const payload = jwtDecode(token);
    const expired = payload.exp * 1000 < Date.now();
    return { payload: expired ? null : payload, expired, invalid: false };
  } catch (err) {
    return { payload: null, expired: false, invalid: true };
  }
};

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem("token");
  const { payload, expired, invalid } = decodeToken(token);

  if (expired || invalid) {
    userStore.logout();
    if (to.name !== "Login") return next({ name: "Login" });
  }

  if (token && payload && !userStore.user) {
    userStore.setUser({
      email: payload.sub,
      fullName: payload.fullName || "",
      role: payload.role
    });
  }

  if (!token && userStore.user) {
    userStore.logout();
  }

  if (to.meta.requiresAuth && !payload) {
    return next({ name: "Login", query: { redirect: to.fullPath } });
  }

  if (to.meta.requiresAuth && to.meta.allowedRoles) {
    if (!to.meta.allowedRoles.includes(payload.role)) {
      return next("/home");
    }
  }

  if (to.name === "Login" && payload) {
    return next(payload.role === "admin" ? "/adminHome" : "/home");
  }

  next();
});

export default router;
