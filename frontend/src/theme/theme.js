import { createTheme } from '@mui/material';

const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2', // Material UI blue
      light: '#42a5f5',
      dark: '#1565c0',
      contrastText: '#fff'
    },
    secondary: {
      main: '#3f51b5', // Indigo
      light: '#7986cb',
      dark: '#303f9f',
      contrastText: '#fff'
    },
    background: {
      default: '#f5f7ff',
      paper: '#ffffff'
    }
  },
  typography: {
    fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
  },
  shape: {
    borderRadius: 10
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          textTransform: 'none',
          borderRadius: 8,
          padding: '10px 16px',
          fontSize: '1rem',
          fontWeight: 500
        }
      }
    },
    MuiOutlinedInput: {
      styleOverrides: {
        root: {
          borderRadius: 8,
          '&:hover .MuiOutlinedInput-notchedOutline': {
            borderColor: '#1976d2'
          },
          '&.Mui-focused .MuiOutlinedInput-notchedOutline': {
            borderWidth: 2
          }
        }
      }
    },
    MuiCard: {
      styleOverrides: {
        root: {
          borderRadius: 16,
          boxShadow: '0 8px 40px rgba(0,0,0,0.12)'
        }
      }
    },
    MuiStepLabel: {
      styleOverrides: {
        label: {
          fontSize: '0.875rem',
          '&.Mui-completed': {
            fontWeight: 500
          },
          '&.Mui-active': {
            fontWeight: 600,
            color: '#1976d2'
          }
        }
      }
    }
  }
});

export default theme;