import { createSlice } from '@reduxjs/toolkit'; // <-- Dòng cần thêm!

const initialState = {
  accessToken: null,
  refreshToken: null,
  userId: null,
  user: null, // Đảm bảo có dòng này, hoặc user: {}
};

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    setAuthTokens: (state, action) => {
      state.accessToken = action.payload.accessToken;
      state.refreshToken = action.payload.refreshToken;
      state.userId = action.payload.userId; 
      state.user = action.payload.user;
    },
    setUserId: (state, action) => {
      state.userId = action.payload;
    },
    setAccessToken: (state, action) => {
      state.accessToken = action.payload;
    },
    updateUserProfile: (state, action) => {
      state.user = { ...state.user, ...action.payload }; // Cập nhật các trường của user
    },
    logout: (state) => {
      state.accessToken = null;
      state.refreshToken = null;
      state.userId = null;
      state.user = null;
    },
  },
});

export const { setAuthTokens, setUserId, setAccessToken, updateUserProfile, logout } = authSlice.actions;

export default authSlice.reducer;