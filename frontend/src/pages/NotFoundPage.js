import React from 'react';
import { Container, Typography, Button, Box } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const NotFoundPage = () => {
  const navigate = useNavigate();

  const handleGoHome = () => {
    navigate('/');
  };

  return (
    <Container maxWidth="md" sx={{ textAlign: 'center', mt: 10 }}>
      <Box>
        <Typography variant="h1" component="div" gutterBottom>
          404
        </Typography>
        <Typography variant="h5" gutterBottom>
          Oops! Trang bạn tìm không tồn tại.
        </Typography>
        <Typography variant="body1" color="text.secondary" paragraph>
          Có thể đường dẫn đã sai hoặc trang đã bị xóa.
        </Typography>
        <Button variant="contained" color="primary" onClick={handleGoHome}>
          Quay về trang chủ
        </Button>
      </Box>
    </Container>
  );
};

export default NotFoundPage;
