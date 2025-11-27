<template>
  <div id="app">
    <UserLogin />
    <ProductForm />
  </div>
</template>

<script>
  import ProductForm from "./components/ProductForm.vue";
  import UserLogin from "./components/UserLogin.vue";

  export default {
    components: { ProductForm, UserLogin },
  };
</script>
