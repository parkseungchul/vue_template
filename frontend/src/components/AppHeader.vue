<template>
  <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
    <a class="navbar-brand" href="#">Gogh</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarsExampleDefault"
            aria-controls="navbarsExampleDefault" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarsExampleDefault">
      <ul class="navbar-nav mr-auto">
        <li class="nav-item active">
          <router-link to="/" class="nav-link">Main</router-link>
        </li>
        <li class="nav-item">
          <router-link to="/login" class="nav-link" v-if="!$store.state.account.id">Login</router-link>
          <a to="/login" class="nav-link" @click="logout()" v-else>Logout</a>
        </li>
      </ul>
      <form class="form-inline my-2 my-lg-0">
        <router-link to="/cart" class="cart">
          <i class="fa fa-shopping-cart" aria-hidden="true"></i>
        </router-link>
      </form>
    </div>
  </nav>
</template>

<script>
import router from "@/scripts/router";
import store from "@/scripts/store";
import axios from "axios";

export default {
  name: 'Header',
  setup() {
    const logout = () => {
      axios.post("/api/member/logout").then(()=>{
        store.commit('setAccount', 0);
        router.push({path: "/"});
      }).catch(error => {
        console.error('Error addHeader:', error);
        router.push('/login'); // 'error-page'를 에러 페이지의 경로로 바꾸세요
      });
    }

    return {logout}
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.cart {
  color: wheat;
}
</style>
