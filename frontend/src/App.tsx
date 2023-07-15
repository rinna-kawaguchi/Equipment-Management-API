import './App.css';
import { FindEquipment } from './components/FindEquipment';
import { ChakraProvider } from '@chakra-ui/react';

function App() {
  return (
    <ChakraProvider>
      <FindEquipment />
    </ChakraProvider>
  );
}

export default App;
