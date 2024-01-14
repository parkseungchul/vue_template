<template>
  <main role="main">

    <!-- Main jumbotron for a primary marketing message or call to action -->
    <div class="jumbotron">
      <div class="container">
        <h1 class="display-3">Hello, world!</h1>
        <p>This is a template for a simple marketing or informational website. It includes a large callout called a
          jumbotron and three supporting pieces of content. Use it as a starting point to create something more
          unique.</p>
        <p><a class="btn btn-primary btn-lg" href="#" role="button">Learn more &raquo;</a></p>
      </div>
    </div>

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
    })
    return {state}
  }
}
</script>
<style scoped>

</style>