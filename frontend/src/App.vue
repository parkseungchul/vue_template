<template>

    <AppHeader></AppHeader>
    <RouterView></RouterView>
    <AppFooter></AppFooter>


</template>

<script>
import AppHeader from "@/components/AppHeader.vue";
import AppFooter from "@/components/AppFooter.vue";
import store from "@/scripts/store";

export default {
  name: 'App',
  components: {
      AppHeader,
      AppFooter
  },
  setup(){
    const id = sessionStorage.getItem("id");
    if(id){
      store.commit("setAccount", id);
    }
  }
}
</script>

<style>

</style>
