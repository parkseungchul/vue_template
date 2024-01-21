<template>
  <div class="text-center">
    <div class="container">
      <ul>


        <li v-for="item in state.items" :key="item.id" class="row"> <!-- 'item.id'로 변경 -->
          <img :src="item.imgPath" class="img"/>
          <span class="name"> {{ item.name }}</span>
          <span class="price"> {{ lib.getNumberFormatted(item.price) }}원 </span>
          <span @click.stop="delToCart(item.id)">
            <i class="fa fa-trash" aria-hidden="true"></i>
          </span>


        </li>


      </ul>
    </div>

  </div>
</template>
<script>

import {reactive, onMounted} from "vue";
import axios from "axios";
import router from "@/scripts/router";
import eventBus from "@/scripts/eventBus";
import lib from "@/scripts/lib";

export default {
  computed: {
    lib() {
      return lib;
    }
  },
  setup() {
    const state = reactive({
      items: []
    });

    const loadCartItems = () => {
      axios.get("api/cart/items").then(({ data }) => {
        state.items = data;
      }).catch(error => {
        console.error('Error loading cart items:', error);
        router.push('/login');
      });
    };

    const delToCart = (itemId) => {
      axios.delete(`api/cart/items/${itemId}`)
          .then(() => {
            loadCartItems();
          })
          .catch(error => {
            console.error('Error deleting cart item:', error);
          });
    };

    onMounted(loadCartItems);
    eventBus.on('item-added', loadCartItems);

    return { state, delToCart }; // 'delToCart' 함수 추가
  }
}


</script>
<style scoped>
/* 리스트 아이템 스타일 */
li.row {
  display: flex;
  align-items: center; /* 세로 중앙 정렬 */
  padding: 10px;
  border-bottom: 1px solid #ccc; /* 구분선 추가 */
}

/* 이미지 스타일 */
.img {
  max-width: 80px; /* 이미지 최대 너비 설정 */
  height: auto;
  margin-right: 15px; /* 이미지와 텍스트 사이 여백 */
}

/* 상품 이름 스타일 */
.name {
  font-weight: bold;
  margin-right: 10px;
}

/* 가격 스타일 */
.price {
  color: #666;
  margin-right: 10px; /* 가격과 아이콘 사이 여백 */
}

/* 휴지통 아이콘 스타일 */
.fa-trash {
  margin-left: 10px; /* 휴지통 아이콘과 가격 사이 여백 */
}


</style>