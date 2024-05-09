<template>
  <h2>{{ lib.toUpperCaseFirst(item.name) }}</h2>
  <div class="text-center">
    <img :src="item.imgPath" style="max-width: 100%; height: auto;">
  </div>
  <div class="row">
    <div class="col">
      <s class="text-muted">{{ lib.getNumberFormatted(item.price) }}</s> {{ item.discountPer }}%
      <strong>{{ lib.getNumberFormatted(item.price - (item.discountPer / 100 * item.price)) }}원</strong>
    </div>
  </div>
  <p>
    <router-link to="/cart" class="cart">
      <i class="fa fa-shopping-cart" aria-hidden="true" @click="addToCart(item.id)"></i>
    </router-link>
  </p>


</template>
<script>
import lib from "@/scripts/lib";
import axios from "axios";
import router from "@/scripts/router";
import eventBus from "@/scripts/eventBus";
import '@/scripts/axiosInterceptors'

export default {
  name: "AppCard",
  props: {
    item: Object
  },
  setup() {
    const addToCart = (itemId) => {
      axios.post(`/api/cart/items/${itemId}`).then(() => {
        eventBus.emit('item-added', itemId);
        console.log('success');
      }).catch(error => {
        console.error('AppCard(addToCart): ', error);
        router.push('/login');
      });
    };
    return {lib, addToCart}
  }
}

</script>
<style scoped>
.img {
  width: 100%;
  height: 250px;
}

</style>