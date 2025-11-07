import React from 'react';
import {
  Box,
  Container,
  Typography,
  Button,
  Card,
  CardContent,
  Grid,
  Paper,
  IconButton,
  useTheme
} from '@mui/material';
import {
  DirectionsCar,
  Schedule,
  Star,
  LocationOn,
  Phone,
  Email,
  Search
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';

const HomePage = () => {
  const theme = useTheme();
  const navigate = useNavigate();
  const { user } = useSelector((state) => state.auth);

  const features = [
    {
      icon: <DirectionsCar sx={{ fontSize: 40, color: theme.palette.primary.main }} />,
      title: 'Wide Selection of Cars',
      description: 'Choose from our extensive fleet of vehicles for any occasion'
    },
    {
      icon: <Schedule sx={{ fontSize: 40, color: theme.palette.primary.main }} />,
      title: '24/7 Availability',
      description: 'Book your car anytime, anywhere with our convenient service'
    },
    {
      icon: <Star sx={{ fontSize: 40, color: theme.palette.primary.main }} />,
      title: 'Premium Quality',
      description: 'All our vehicles are well-maintained and regularly serviced'
    }
  ];

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
                color: theme.palette.primary.main 
              }}
            >
              CarBooking
            </Typography>
            
            <Box display="flex" gap={2}>
              {user ? (
                <>
                  <Typography variant="body1" sx={{ alignSelf: 'center' }}>
                    Welcome, {user.fullName}!
                  </Typography>
                  <Button 
                    variant="outlined" 
                    onClick={() => navigate('/profile')}
                  >
                    Profile
                  </Button>
                </>
              ) : (
                <>
                  <Button 
                    variant="outlined" 
                    onClick={() => navigate('/login')}
                  >
                    Login
                  </Button>
                  <Button 
                    variant="contained" 
                    onClick={() => navigate('/register')}
                  >
                    Register
                  </Button>
                </>
              )}
            </Box>
          </Box>
        </Container>
      </Paper>

      {/* Hero Section */}
      <Container maxWidth="lg" sx={{ py: 8 }}>
        <Grid container spacing={4} alignItems="center">
          <Grid item xs={12} md={6}>
            <Typography 
              variant="h2" 
              sx={{ 
                fontWeight: 'bold', 
                mb: 3,
                color: theme.palette.text.primary
              }}
            >
              Book Your Perfect Car
            </Typography>
            <Typography 
              variant="h6" 
              sx={{ 
                mb: 4, 
                color: theme.palette.text.secondary,
                lineHeight: 1.6
              }}
            >
              Experience the freedom of the road with our premium car rental service. 
              Choose from a wide range of vehicles and enjoy hassle-free booking.
            </Typography>
            <Box display="flex" gap={2}>
              <Button 
                variant="contained" 
                size="large"
                startIcon={<Search />}
                onClick={() => navigate(user ? '/browse' : '/login')}
              >
                Browse Cars
              </Button>
              {!user && (
                <Button 
                  variant="outlined" 
                  size="large"
                  onClick={() => navigate('/register')}
                >
                  Get Started
                </Button>
              )}
            </Box>
          </Grid>
          <Grid item xs={12} md={6}>
            <Card 
              sx={{ 
                borderRadius: theme.shape.borderRadius * 2,
                overflow: 'hidden',
                boxShadow: theme.shadows[8]
              }}
            >
              <Box 
                sx={{ 
                  height: 300, 
                  bgcolor: theme.palette.primary.light,
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center'
                }}
              >
                <DirectionsCar sx={{ fontSize: 120, color: 'white' }} />
              </Box>
            </Card>
          </Grid>
        </Grid>
      </Container>

      {/* Features Section */}
      <Container maxWidth="lg" sx={{ py: 6 }}>
        <Typography 
          variant="h4" 
          align="center" 
          sx={{ mb: 6, fontWeight: 'bold' }}
        >
          Why Choose Us?
        </Typography>
        <Grid container spacing={4}>
          {features.map((feature, index) => (
            <Grid item xs={12} md={4} key={index}>
              <Card sx={{ height: '100%', textAlign: 'center', p: 3 }}>
                <CardContent>
                  <Box sx={{ mb: 2 }}>
                    {feature.icon}
                  </Box>
                  <Typography 
                    variant="h6" 
                    sx={{ mb: 2, fontWeight: 600 }}
                  >
                    {feature.title}
                  </Typography>
                  <Typography 
                    variant="body2" 
                    color="text.secondary"
                  >
                    {feature.description}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      </Container>

      {/* Footer */}
      <Paper 
        sx={{ 
          bgcolor: theme.palette.primary.dark, 
          color: 'white', 
          py: 4, 
          mt: 8 
        }}
      >
        <Container maxWidth="lg">
          <Grid container spacing={4}>
            <Grid item xs={12} md={6}>
              <Typography variant="h6" sx={{ mb: 2, fontWeight: 'bold' }}>
                CarBooking
              </Typography>
              <Typography variant="body2" sx={{ mb: 2 }}>
                Your trusted partner for car rental services. 
                We provide quality vehicles and excellent customer service.
              </Typography>
            </Grid>
            <Grid item xs={12} md={6}>
              <Typography variant="h6" sx={{ mb: 2, fontWeight: 'bold' }}>
                Contact Us
              </Typography>
              <Box display="flex" alignItems="center" gap={1} sx={{ mb: 1 }}>
                <Phone fontSize="small" />
                <Typography variant="body2">+1 (555) 123-4567</Typography>
              </Box>
              <Box display="flex" alignItems="center" gap={1} sx={{ mb: 1 }}>
                <Email fontSize="small" />
                <Typography variant="body2">info@carbooking.com</Typography>
              </Box>
              <Box display="flex" alignItems="center" gap={1}>
                <LocationOn fontSize="small" />
                <Typography variant="body2">123 Main St, City, State</Typography>
              </Box>
            </Grid>
          </Grid>
        </Container>
      </Paper>
    </Box>
  );
};

export default HomePage;