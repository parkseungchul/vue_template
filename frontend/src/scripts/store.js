// Create a new store instance.
import {createStore} from 'vuex'

const store = createStore({
    state() {
        return {
            isAuthenticated: false,
            userId: 0 // 기본값으로 0 또는 null을 설정
        };
    },
    mutations: {
        // 로그인 상태 설정
        setUser(state, { isAuthenticated, userId }) {
            state.isAuthenticated = isAuthenticated;
            state.userId = userId;
        },
        // 로그인 상태 초기화
        clearUser(state) {
            state.isAuthenticated = false;
            state.userId = 0; // 기본값으로 0 또는 null을 설정
        }
    }
});
export default store;