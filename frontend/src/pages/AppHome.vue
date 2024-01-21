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
    }); // 홈은 에러 없어
    return {state}
  }
}
</script>
<style scoped>

</style>