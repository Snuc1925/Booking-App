import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  fullName: '',
  // Thêm các thông tin khác nếu cần
};

const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    setUserInfo: (state, action) => {
      state.fullName = action.payload.fullName;
    },
    clearUserInfo: (state) => {
      state.fullName = '';
    },
  },
});

export const { setUserInfo, clearUserInfo } = userSlice.actions;
export default userSlice.reducer;