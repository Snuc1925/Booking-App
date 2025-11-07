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
import { Formik, Form } from 'formik';
import * as Yup from 'yup';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { 
  Visibility, 
  VisibilityOff, 
  Phone, 
  Email, 
  Person, 
  Lock 
} from '@mui/icons-material';
import axios from 'axios';
import { toast } from 'react-toastify';
import theme from '../../theme/theme';

const RegisterPage = () => {
  const navigate = useNavigate();
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
      .required('Vui lòng nhập số điện thoại'),
    password: Yup.string()
      .min(6, 'Mật khẩu phải có ít nhất 6 ký tự')
      .matches(
        /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)/,
        'Mật khẩu phải chứa ít nhất 1 chữ hoa, 1 chữ thường và 1 số'
      )
      .required('Vui lòng nhập mật khẩu')
  });

  const initialValues = {
    fullName: '',
    email: '',
    phone: '',
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
            Tạo tài khoản mới để bắt đầu
          </Typography>
        </Paper>

        <Card sx={{ boxShadow: theme.shadows[8] }}>
          <CardContent sx={{ p: 4 }}>
            <Typography 
              variant="h4" 
              align="center" 
              sx={{ mb: 4, fontWeight: 'bold' }}
            >
              Đăng Ký
            </Typography>

            <Formik
              initialValues={initialValues}
              validationSchema={validationSchema}
              onSubmit={async (values, { setSubmitting }) => {
                try {
                  const res = await axios.post(
                    `${process.env.REACT_APP_API_URL}/auth/register`,
                    values
                  );
                  
                  toast.success('Đăng ký thành công! Vui lòng đăng nhập.');
                  navigate('/login');
                } catch (e) {
                  console.error("Registration failed:", e);
                  toast.error(
                    e.response?.data?.message || 'Đăng ký thất bại. Vui lòng thử lại.'
                  );
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
                      placeholder="Nhập họ và tên đầy đủ"
                    />
                  </Box>

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
                      placeholder="example@gmail.com"
                    />
                  </Box>

                  <Box sx={{ mb: 3 }}>
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
                      placeholder="Nhập số điện thoại"
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
                      placeholder="Tối thiểu 6 ký tự"
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
                    {isSubmitting ? 'Đang đăng ký...' : 'Đăng Ký'}
                  </Button>

                  <Box textAlign="center">
                    <Typography variant="body2" color="text.secondary">
                      Đã có tài khoản?{' '}
                      <Link
                        component="button"
                        type="button"
                        onClick={() => navigate('/login')}
                        sx={{ 
                          color: theme.palette.primary.main,
                          textDecoration: 'none',
                          fontWeight: 500,
                          '&:hover': {
                            textDecoration: 'underline'
                          }
                        }}
                      >
                        Đăng nhập ngay
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

export default RegisterPage;