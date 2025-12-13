<template>
  <div class="landing">

    <section class="hero d-flex align-items-center justify-content-center text-white">
      <div class="text-center">
        <h1 class="display-3 fw-bold">Photo&Film4You</h1>
        <p class="lead mt-3">El mejor material audiovisual al alcance de un clic.</p>

        <button class="btn btn-light btn-lg mt-4" @click="goLogin">
          Iniciar sesión
        </button>
      </div>
    </section>

  </div>
</template>

<script>
export default {
  methods: {
    goLogin() {
      this.$router.push("/login");
    }
  }
};
</script>

<style scoped>
.hero {
  height: 90vh;
  background: url("@/assets/home.png") center/cover no-repeat;
}
</style>
