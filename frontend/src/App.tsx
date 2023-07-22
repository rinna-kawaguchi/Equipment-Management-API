import { BrowserRouter } from 'react-router-dom';
import './App.css';
import { ChakraProvider } from '@chakra-ui/react';
import { Router } from './router/Router';

function App() {
  return (
    <ChakraProvider>
      <BrowserRouter>
        <Router />
      </BrowserRouter>
    </ChakraProvider>
  );
}

export default App;
