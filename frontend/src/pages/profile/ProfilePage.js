import React, { useState } from 'react';
import {
  Box,
  Container,
  Card,
  CardContent,
  Typography,
  TextField,
  Button,
  Avatar,
  Grid,
  Divider,
  Paper,
  IconButton,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  InputAdornment
} from '@mui/material';
import {
  Person,
  Email,
  Phone,
  Edit,
  Save,
  Cancel,
  ExitToApp,
  DirectionsCar,
  History,
  Settings,
  Visibility,
  VisibilityOff
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import { logout, updateUserProfile } from '../../redux/authSlice';
import { Formik, Form } from 'formik';
import * as Yup from 'yup';
import { toast } from 'react-toastify';
import theme from '../../theme/theme';
import axiosInstance from '../../api/axiosInstance';

const ProfilePage = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { user } = useSelector((state) => state.auth);
  const [isEditing, setIsEditing] = useState(false);
  const [logoutDialogOpen, setLogoutDialogOpen] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const validationSchema = Yup.object({
    fullName: Yup.string()
      .min(2, 'Tên phải có ít nhất 2 ký tự')
      .required('Vui lòng nhập họ và tên'),
    email: Yup.string()
      .email('Email không hợp lệ')
      .required('Vui lòng nhập email'),
    phone: Yup.string()
      .matches(/^[0-9+\-\s()]+$/, 'Số điện thoại không hợp lệ')
      .required('Vui lòng nhập số điện thoại')
  });

    const handleLogout = async () => {
        try {
            await axiosInstance.post('/auth/logout');

            dispatch(logout());
            toast.success('Đăng xuất thành công!');
            navigate('/');
            setLogoutDialogOpen(false);
        } catch (error) {
            console.error('Lỗi khi gọi API đăng xuất:', error);
            toast.error('Đăng xuất thất bại, vui lòng thử lại!');
        }
    };


  const getInitials = (name) => {
    return name
      ?.split(' ')
      .map(word => word[0])
      .join('')
      .toUpperCase()
      .slice(0, 2) || 'UN';
  };

  if (!user) {
    navigate('/login');
    return null;
  }

  return (
    <Box sx={{ minHeight: '100vh', bgcolor: theme.palette.background.default }}>
      {/* Header */}
      <Paper 
        elevation={0} 
        sx={{ 
          bgcolor: 'white', 
          borderBottom: `1px solid ${theme.palette.divider}`,
          py: 2
        }}
      >
        <Container maxWidth="lg">
          <Box display="flex" justifyContent="space-between" alignItems="center">
            <Typography 
              variant="h4" 
              sx={{ 
                fontWeight: 'bold', 
                color: theme.palette.primary.main,
                cursor: 'pointer'
              }}
              onClick={() => navigate('/')}
            >
              CarBooking
            </Typography>
            
            <Button 
              variant="outlined" 
              startIcon={<ExitToApp />}
              onClick={() => setLogoutDialogOpen(true)}
              color="error"
            >
              Đăng xuất
            </Button>
          </Box>
        </Container>
      </Paper>

      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Grid container spacing={4}>
          {/* Profile Info Card */}
          <Grid item xs={12} md={8}>
            <Card sx={{ mb: 3 }}>
              <CardContent sx={{ p: 4 }}>
                <Box display="flex" justifyContent="between" alignItems="center" sx={{ mb: 3 }}>
                  <Typography variant="h5" sx={{ fontWeight: 'bold' }}>
                    Thông Tin Cá Nhân
                  </Typography>
                  <IconButton 
                    onClick={() => setIsEditing(!isEditing)}
                    color="primary"
                  >
                    {isEditing ? <Cancel /> : <Edit />}
                  </IconButton>
                </Box>

                <Box display="flex" alignItems="center" sx={{ mb: 4 }}>
                  <Avatar 
                    sx={{ 
                      width: 80, 
                      height: 80, 
                      bgcolor: theme.palette.primary.main,
                      fontSize: '2rem',
                      mr: 3
                    }}
                  >
                    {getInitials(user.fullName)}
                  </Avatar>
                  <Box>
                    <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                      {user.fullName}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      Thành viên của CarBooking
                    </Typography>
                  </Box>
                </Box>

                {isEditing ? (
                  <Formik
                    initialValues={{
                      fullName: user.fullName || '',
                      email: user.email || '',
                      phone: user.phone || ''
                    }}
                    validationSchema={validationSchema}
                    onSubmit={async (values, { setSubmitting }) => {
                      try {
                        // Here you would typically make an API call to update user info
                        dispatch(updateUserProfile(values));
                        toast.success('Cập nhật thông tin thành công!');
                        setIsEditing(false);
                      } catch (error) {
                        toast.error('Cập nhật thất bại. Vui lòng thử lại.');
                      } finally {
                        setSubmitting(false);
                      }
                    }}
                  >
                    {({ errors, touched, isSubmitting, values, handleChange, handleBlur }) => (
                      <Form>
                        <Grid container spacing={3}>
                          <Grid item xs={12}>
                            <TextField
                              fullWidth
                              name="fullName"
                              label="Họ và tên"
                              value={values.fullName}
                              onChange={handleChange}
                              onBlur={handleBlur}
                              error={touched.fullName && Boolean(errors.fullName)}
                              helperText={touched.fullName && errors.fullName}
                              InputProps={{
                                startAdornment: (
                                  <InputAdornment position="start">
                                    <Person color="action" />
                                  </InputAdornment>
                                ),
                              }}
                            />
                          </Grid>

                          <Grid item xs={12} md={6}>
                            <TextField
                              fullWidth
                              name="email"
                              label="Email"
                              type="email"
                              value={values.email}
                              onChange={handleChange}
                              onBlur={handleBlur}
                              error={touched.email && Boolean(errors.email)}
                              helperText={touched.email && errors.email}
                              InputProps={{
                                startAdornment: (
                                  <InputAdornment position="start">
                                    <Email color="action" />
                                  </InputAdornment>
                                ),
                              }}
                            />
                          </Grid>

                          <Grid item xs={12} md={6}>
                            <TextField
                              fullWidth
                              name="phone"
                              label="Số điện thoại"
                              value={values.phone}
                              onChange={handleChange}
                              onBlur={handleBlur}
                              error={touched.phone && Boolean(errors.phone)}
                              helperText={touched.phone && errors.phone}
                              InputProps={{
                                startAdornment: (
                                  <InputAdornment position="start">
                                    <Phone color="action" />
                                  </InputAdornment>
                                ),
                              }}
                            />
                          </Grid>

                          <Grid item xs={12}>
                            <Box display="flex" gap={2}>
                              <Button
                                type="submit"
                                variant="contained"
                                disabled={isSubmitting}
                                startIcon={<Save />}
                              >
                                Lưu thay đổi
                              </Button>
                              <Button
                                variant="outlined"
                                onClick={() => setIsEditing(false)}
                                startIcon={<Cancel />}
                              >
                                Hủy
                              </Button>
                            </Box>
                          </Grid>
                        </Grid>
                      </Form>
                    )}
                  </Formik>
                ) : (
                  <Grid container spacing={3}>
                    <Grid item xs={12}>
                      <Box display="flex" alignItems="center" sx={{ py: 2 }}>
                        <Person sx={{ mr: 2, color: 'text.secondary' }} />
                        <Box>
                          <Typography variant="body2" color="text.secondary">
                            Họ và tên
                          </Typography>
                          <Typography variant="body1" sx={{ fontWeight: 500 }}>
                            {user.fullName}
                          </Typography>
                        </Box>
                      </Box>
                    </Grid>

                    <Grid item xs={12} md={6}>
                      <Box display="flex" alignItems="center" sx={{ py: 2 }}>
                        <Email sx={{ mr: 2, color: 'text.secondary' }} />
                        <Box>
                          <Typography variant="body2" color="text.secondary">
                            Email
                          </Typography>
                          <Typography variant="body1" sx={{ fontWeight: 500 }}>
                            {user.email}
                          </Typography>
                        </Box>
                      </Box>
                    </Grid>

                    <Grid item xs={12} md={6}>
                      <Box display="flex" alignItems="center" sx={{ py: 2 }}>
                        <Phone sx={{ mr: 2, color: 'text.secondary' }} />
                        <Box>
                          <Typography variant="body2" color="text.secondary">
                            Số điện thoại
                          </Typography>
                          <Typography variant="body1" sx={{ fontWeight: 500 }}>
                            {user.phone}
                          </Typography>
                        </Box>
                      </Box>
                    </Grid>
                  </Grid>
                )}
              </CardContent>
            </Card>
          </Grid>

          {/* Quick Actions Sidebar */}
          <Grid item xs={12} md={4}>
            <Card sx={{ mb: 3 }}>
              <CardContent>
                <Typography variant="h6" sx={{ mb: 3, fontWeight: 'bold' }}>
                  Thao tác nhanh
                </Typography>
                
                <Box display="flex" flexDirection="column" gap={2}>
                  <Button
                    variant="outlined"
                    startIcon={<DirectionsCar />}
                    fullWidth
                    sx={{ justifyContent: 'flex-start' }}
                  >
                    Đặt xe mới
                  </Button>
                  
                  <Button
                    variant="outlined"
                    startIcon={<History />}
                    fullWidth
                    sx={{ justifyContent: 'flex-start' }}
                  >
                    Lịch sử đặt xe
                  </Button>
                  
                  <Button
                    variant="outlined"
                    startIcon={<Settings />}
                    fullWidth
                    sx={{ justifyContent: 'flex-start' }}
                  >
                    Cài đặt
                  </Button>
                </Box>
              </CardContent>
            </Card>

            {/* Stats Card */}
            <Card>
              <CardContent>
                <Typography variant="h6" sx={{ mb: 3, fontWeight: 'bold' }}>
                  Thống kê
                </Typography>
                
                <Box sx={{ textAlign: 'center' }}>
                  <Typography variant="h3" color="primary" sx={{ fontWeight: 'bold' }}>
                    0
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Chuyến đi đã hoàn thành
                  </Typography>
                </Box>
              </CardContent>
            </Card>
          </Grid>
        </Grid>
      </Container>

      {/* Logout Confirmation Dialog */}
      <Dialog
        open={logoutDialogOpen}
        onClose={() => setLogoutDialogOpen(false)}
        maxWidth="xs"
        fullWidth
      >
        <DialogTitle>
          <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
            Xác nhận đăng xuất
          </Typography>
        </DialogTitle>
        <DialogContent>
          <Typography>
            Bạn có chắc chắn muốn đăng xuất khỏi tài khoản?
          </Typography>
        </DialogContent>
        <DialogActions sx={{ p: 3, pt: 1 }}>
          <Button 
            onClick={() => setLogoutDialogOpen(false)}
            variant="outlined"
          >
            Hủy
          </Button>
          <Button 
            onClick={handleLogout}
            variant="contained"
            color="error"
            startIcon={<ExitToApp />}
          >
            Đăng xuất
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default ProfilePage;