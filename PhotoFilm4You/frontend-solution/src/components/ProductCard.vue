<!-- src/components/ProductCard.vue -->
<template>
  <div class="card product-card h-100" @click="openDetail">
    <div class="product-image-placeholder d-flex align-items-center justify-content-center">
      <template v-if="product.imageUrl">
        <img :src="product.imageUrl" class="img-fluid w-100 h-100" alt="product" />
      </template>
      <template v-else>
        <div class="placeholder-content">
          <div class="initial">{{ initial }}</div>
          <div class="pname">{{ product.name }}</div>
        </div>
      </template>
    </div>

    <div class="card-body text-center">
      <h5 class="card-title mb-1 text-truncate">{{ product.name }}</h5>
      <p class="mb-1 text-muted small">{{ product.brand ?? product.model ?? '' }}</p>
      <p class="fw-bold mb-0">{{ priceLabel }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "ProductCard",
  props: {
    product: { type: Object, required: true }
  },
  computed: {
    initial() {
      return (this.product.name || "P").charAt(0).toUpperCase();
    },
    priceLabel() {
      // product.dailyPrice in backend (daillyPrice) — adapt if different
      const p = this.product.dailyPrice ?? this.product.price ?? this.product.daily_price;
      return p ? `${p} € / día` : "Consultar precio";
    }
  },
  methods: {
    openDetail() {
      this.$router.push(`/product/${this.product.id}`);
    }
  }
};
</script>

<style scoped>
.product-card { cursor: pointer; transition: transform .15s ease, box-shadow .15s ease; }
.product-card:hover { transform: translateY(-6px); box-shadow: 0 10px 25px rgba(0,0,0,.12); }

.product-image-placeholder {
  height: 170px;
  background: linear-gradient(135deg, #eceff1, #f8f9fa);
  overflow: hidden;
}

.product-image-placeholder img { object-fit: cover; height: 100%; width: 100%; display: block; }

.placeholder-content { text-align: center; padding: 1rem; }
.initial {
  width: 68px; height: 68px; border-radius: 50%;
  display: inline-flex; align-items: center; justify-content: center;
  background: linear-gradient(135deg,#6f42c1,#20c997); color: white;
  font-size: 28px; font-weight: 700; margin-bottom: 8px;
}
.pname { font-size: 0.9rem; color: #333; padding: 0 8px; }
</style>
