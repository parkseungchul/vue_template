<template>

  <AppHeader></AppHeader>
  <RouterView></RouterView>
  <AppFooter></AppFooter>


</template>

<script>
import AppHeader from "@/components/AppHeader.vue";
import AppFooter from "@/components/AppFooter.vue";
import store from "@/scripts/store";
import axios from "axios";
import {useRoute} from "vue-router";
import {watch} from "vue";

export default {
  name: 'App',
  components: {
    AppHeader,
    AppFooter
  },
  setup() {
    const check = () => {
      axios.get("/api/member/check").then(({data}) => {
        console.log(data);
        if (data) {
          store.commit("setAccount", data ||"");
        }
      })
    };
    const route = useRoute();
    watch(route, () => {
      check();
    });
  }
}
</script>

<style>

</style>
