<template>
  <AppHeader></AppHeader>
  <RouterView></RouterView>
  <AppFooter></AppFooter>
</template>

<script>
import AppHeader from "@/components/AppHeader.vue";
import AppFooter from "@/components/AppFooter.vue";

export default {
  name: 'App',
  components: {
    AppHeader,
    AppFooter
  },
  setup() {
  }
}
</script>
<style scoped>
</style>
