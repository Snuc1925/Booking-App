import React from 'react';
import {
  Box,
  Container,
  Card,
  CardContent,
  Typography,
  TextField,
  Button,
  Link,
  Paper,
  InputAdornment,
  IconButton
} from '@mui/material';
import { Formik, Form, Field } from 'formik';
import * as Yup from 'yup';
import { useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { useState } from 'react';
import { Visibility, VisibilityOff, Email, Lock } from '@mui/icons-material';
import axios from 'axios';
import { toast } from 'react-toastify';
import { setAuthTokens } from '../../redux/authSlice';
import theme from '../../theme/theme';

const LoginPage = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [showPassword, setShowPassword] = useState(false);

  const validationSchema = Yup.object({
    email: Yup.string()
      .email('Email không hợp lệ')
      .required('Vui lòng nhập email'),
    password: Yup.string()
      .min(6, 'Mật khẩu phải có ít nhất 6 ký tự')
      .required('Vui lòng nhập mật khẩu')
  });

  const initialValues = {
    email: '',
    password: ''
  };

  return (
    <Box 
      sx={{ 
        minHeight: '100vh',
        bgcolor: theme.palette.background.default,
        display: 'flex',
        alignItems: 'center',
        py: 4
      }}
    >
      <Container maxWidth="sm">
        <Paper 
          elevation={0} 
          sx={{ 
            p: 2, 
            mb: 3, 
            textAlign: 'center',
            bgcolor: 'transparent'
          }}
        >
          <Typography 
            variant="h3" 
            sx={{ 
              fontWeight: 'bold', 
              color: theme.palette.primary.main,
              mb: 1
            }}
          >
            CarBooking
          </Typography>
          <Typography variant="subtitle1" color="text.secondary">
            Đăng nhập vào tài khoản của bạn
          </Typography>
        </Paper>

        <Card sx={{ boxShadow: theme.shadows[8] }}>
          <CardContent sx={{ p: 4 }}>
            <Typography 
              variant="h4" 
              align="center" 
              sx={{ mb: 4, fontWeight: 'bold' }}
            >
              Đăng Nhập
            </Typography>

            <Formik
              initialValues={initialValues}
              validationSchema={validationSchema}
              onSubmit={async (values, { setSubmitting }) => {
                try {
                  const res = await axios.post(
                    `${process.env.REACT_APP_API_URL}/auth/login`,
                    values
                  );
                  
                    dispatch(setAuthTokens({
                    accessToken: res.data.accessToken,
                    refreshToken: res.data.refreshToken,
                    userId: res.data.id,
                    user: {
                        phone: res.data.phone,
                        fullName: res.data.fullName,
                        email: res.data.email,
                    },
                    }));

                  toast.success('Đăng nhập thành công!');
                  navigate('/');
                } catch (e) {
                  console.error("Login failed:", e);
                  toast.error(e.response?.data?.message || 'Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin.');
                } finally {
                  setSubmitting(false);
                }
              }}
            >
              {({ errors, touched, isSubmitting, values, handleChange, handleBlur }) => (
                <Form>
                  <Box sx={{ mb: 3 }}>
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
                      placeholder="Nhập email của bạn"
                    />
                  </Box>

                  <Box sx={{ mb: 3 }}>
                    <TextField
                      fullWidth
                      name="password"
                      label="Mật khẩu"
                      type={showPassword ? 'text' : 'password'}
                      value={values.password}
                      onChange={handleChange}
                      onBlur={handleBlur}
                      error={touched.password && Boolean(errors.password)}
                      helperText={touched.password && errors.password}
                      InputProps={{
                        startAdornment: (
                          <InputAdornment position="start">
                            <Lock color="action" />
                          </InputAdornment>
                        ),
                        endAdornment: (
                          <InputAdornment position="end">
                            <IconButton
                              onClick={() => setShowPassword(!showPassword)}
                              edge="end"
                            >
                              {showPassword ? <VisibilityOff /> : <Visibility />}
                            </IconButton>
                          </InputAdornment>
                        ),
                      }}
                      placeholder="Nhập mật khẩu của bạn"
                    />
                  </Box>

                  <Button
                    type="submit"
                    fullWidth
                    variant="contained"
                    size="large"
                    disabled={isSubmitting}
                    sx={{ mb: 3, py: 1.5 }}
                  >
                    {isSubmitting ? 'Đang đăng nhập...' : 'Đăng Nhập'}
                  </Button>

                  <Box textAlign="center">
                    <Typography variant="body2" color="text.secondary">
                      Chưa có tài khoản?{' '}
                      <Link
                        component="button"
                        type="button"
                        onClick={() => navigate('/register')}
                        sx={{ 
                          color: theme.palette.primary.main,
                          textDecoration: 'none',
                          fontWeight: 500,
                          '&:hover': {
                            textDecoration: 'underline'
                          }
                        }}
                      >
                        Đăng ký ngay
                      </Link>
                    </Typography>
                  </Box>

                  <Box textAlign="center" sx={{ mt: 2 }}>
                    <Link
                      component="button"
                      type="button"
                      onClick={() => navigate('/')}
                      sx={{ 
                        color: theme.palette.text.secondary,
                        textDecoration: 'none',
                        fontSize: '0.875rem',
                        '&:hover': {
                          textDecoration: 'underline'
                        }
                      }}
                    >
                      ← Quay về trang chủ
                    </Link>
                  </Box>
                </Form>
              )}
            </Formik>
          </CardContent>
        </Card>
      </Container>
    </Box>
  );
};

export default LoginPage;