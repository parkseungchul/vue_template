<template>
  <div class="card mb-4 box-shadow">
    <img class="card-img-top" data-src="holder.js/100px225?theme=thumb&bg=55595c&fg=eceeef&text=Thumbnail"
         alt="Card image cap">
    <div class="card-body">
      <p class="card-text">{{ item.id_dto }} {{ lib.toUpperCaseFirst(item.name_dto) }}</p>
      <div class="d-flex justify-content-between align-items-center">
        <div class="btn-group">
          <button type="button" class="btn btn-sm btn-outline-secondary">View</button>
          <button type="button" class="btn btn-sm btn-outline-secondary">Edit</button>
        </div>
      </div>
    </div>
  </div>
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