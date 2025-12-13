<template>
  <div class="d-flex flex-column justify-content-center align-items-center" style="height: 100vh;">
    
    <h1 class="mb-4"> PhotoFilm4You</h1>

    <button class="btn btn-success btn-lg" @click="goToLogin">
      Login
    </button>

  </div>
</template>

<script>
export default {
  name: 'HomePage',
  methods: {
    goToLogin() {
      this.$router.push('/login');
    }
  }
};
</script>

<style>
h1 {
  font-size: 3rem;
  font-weight: bold;
}
</style>
