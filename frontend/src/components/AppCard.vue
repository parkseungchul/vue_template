<template>
  <span>
    <h2>{{ item.id_dto }}</h2>
    <p>{{ lib.toUpperCaseFirst(item.name_dto) }} </p>
    <p><a class="btn btn-secondary" href="#" role="button">View details &raquo;</a></p>
  </span>
</template>
<script>
import lib from "@/scripts/lib";

export default {
  name: "AppCard",
  props: {
    item: Object
  },
  setup() {
    return {lib}
  }
}

</script>
<style scoped>

</style>