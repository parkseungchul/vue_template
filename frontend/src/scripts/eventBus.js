import {reactive, readonly, watch} from 'vue';

const state = reactive({
    // 이벤트 상태 저장
});

// 이벤트 발생 및 감지 기능을 제공하는 객체
const eventBus = {
    emit(event, data) {
        state[event] = data;
    },
    on(event, callback) {
        watch(() => state[event], (newVal) => {
            if (newVal !== undefined) {
                callback(newVal);
            }
        });
    }
};

export default readonly(eventBus);
