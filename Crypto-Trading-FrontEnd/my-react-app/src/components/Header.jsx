import React from 'react';
import {
  AppBar,
  Toolbar,
  Typography,
  IconButton,
  Box,
  Tooltip
} from '@mui/material';
import AccountBalanceWalletIcon from '@mui/icons-material/AccountBalanceWallet';
import ExitToAppIcon from '@mui/icons-material/ExitToApp';
import { Link } from "react-router-dom";


const Header = ({ balance }) => {
  return (
    <AppBar
      position="sticky"
      sx={{
        backgroundColor: '#000',
        height: { xs: '64px', sm: '70px', md: '80px' }, 
         width: '100vw',
        boxShadow: '0px 2px 8px rgba(0,0,0,0.4)'
      }}
    >
      <Toolbar
        sx={{
          height: '100%',
          display: 'flex',
          justifyContent: 'space-between',
          px: { xs: 2, sm: 3, md: 4 } 
        }}
      >
        
        <Typography
        component={Link}
          to="/home"
          variant="h6"
          sx={{
            fontWeight: 'bold',
            fontSize: { xs: '1.1rem', sm: '1.25rem', md: '1.5rem' }
          }}
        >
          CryptoSim
        </Typography>

        
        <Box
          display="flex"
          alignItems="center"
          gap={{ xs: 1, sm: 2, md: 3 }}
          sx={{ flexWrap: 'wrap' }}
        >
          
          <Typography
  variant="body1"
  sx={{
    fontWeight: 500,
    fontSize: { xs: '0.9rem', sm: '1rem', md: '1.2rem' }
  }}
>
  {typeof balance === "number" ? `$${balance.toFixed(2)}` : "Loading..."}
</Typography>


          <Tooltip title="Crypto Wallet">
            <IconButton component={Link}
              to="/holdings-transactions" color="primary" size="medium">
              <AccountBalanceWalletIcon />
            </IconButton>
          </Tooltip>

          <Tooltip title="Leave">
            <IconButton  component={Link}
              to="/login" color="secondary" size="medium">
              <ExitToAppIcon />
            </IconButton>
          </Tooltip>
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Header;
