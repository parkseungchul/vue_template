<template>
  <main role="main">


    <div class="container">
      <!-- Example row of columns -->
      <div class="row">
        <div class="col-md-4" v-for="(item, idx) in state.items" :key="idx">
          <AppCard :item="item"></AppCard>
        </div>
      </div>

      <hr>

    </div> <!-- /container -->

  </main>
</template>
<script>
import axios from "axios";
import {reactive} from "vue";
import AppCard from "@/components/AppCard.vue";
import '@/scripts/axiosInterceptors'
import router from "@/scripts/router";
export default {
  name: 'AppHome', components: {
    AppCard
  },
  setup() {
    const state = reactive({
      items: []
    })
    axios.get("/api/items").then((res) => {
      console.log(res);
      state.items = res.data;
    }).catch(error => {
      console.error('AppHome():', error);
      router.push('/error');
    });
    return {state}
  }
}
</script>
<style scoped>

</style>