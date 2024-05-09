<template>
  <div class="content">
    <div class="text-center"> SERVER ERROR </div>
  </div>
</template>
<script>

export default {
  setup() {
  }
}


</script>
<style scoped>
.content {
  flex-grow: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 텍스트를 중앙 정렬 */
.text-center {
  text-align: center;
}

.content .text-center {
  font-size: 2em; /* 폰트 크기 증가 */
  color: red; /* 색상을 빨간색으로 설정 */
  font-weight: bold; /* 폰트를 굵게 */
  margin: 20px; /* 주변 여백 추가 */
}
</style>