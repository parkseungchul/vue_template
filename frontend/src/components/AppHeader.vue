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
        <!-- isAuthenticated가 false일 경우 (로그인하지 않은 상태) -->
        <router-link to="/login" class="nav-link" v-if="!isAuthenticated">Login</router-link>

        <!-- isAuthenticated가 true일 경우 (로그인한 상태) -->
        <a class="nav-link" @click="logout" v-else>Logout</a>
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
import {computed} from "vue";

export default {
  name: 'Header',
  setup: function () {
    const isAuthenticated = computed(() => store.state.isAuthenticated);


    const logout = () => {
      axios.post("/api/member/logout").then(() => {
        store.commit('clearUser');
        sessionStorage.removeItem('isAuthenticated');
        sessionStorage.removeItem('userId');
        router.push('/login');
      }).catch(error => {
        console.error('AppHeader(logout): ', error);
        router.push('/login');
      });
    };

    return {isAuthenticated, logout};
  }
}
</script>


<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.cart {
  color: wheat;
}
</style>
