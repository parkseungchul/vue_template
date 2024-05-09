<template>
  <div class="text-center">
    <div class="container">
      <ul>
        <li class="row" v-for="item in state.items" :key="item.id" >
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
import {reactive} from "vue";
import axios from "axios";
import router from "@/scripts/router";
import lib from "@/scripts/lib";
import eventBus from "@/scripts/eventBus";

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
        console.error('AppCart(loadItem):', error);
        router.push('/login');
      });
    };

    const delToCart = (itemId) => {
      axios.delete(`api/cart/items/${itemId}`)
          .then(() => {
            loadCartItems(); // 아이템 삭제 후 목록 새로 고침
          })
          .catch(error => {
            console.error('AppCart(delToCart): ', error);
            router.push('/login');
          });
    };

    if (eventBus) {
      console.log('eventBus');
      eventBus.on('item-added', (itemId) => {
        console.log('eventBus-on');
        console.log(`Item added with id: ${itemId}`);
        loadCartItems(); // Call loadCartItems when the event occurs
      });
      const existingListeners = eventBus._events && eventBus._events['item-added'];
      if (!existingListeners || existingListeners.length === 0) {
        console.log('eventBus-off');
        loadCartItems(); // Load only if there are no event handlers
      }
    }else{
      // not work here
      console.log('if eventbus does\'nt have a existence');
    }
    return { state, delToCart };
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