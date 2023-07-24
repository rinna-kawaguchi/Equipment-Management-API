import { Route, Routes } from 'react-router-dom';
import { FindEquipment } from '../components/FindEquipment';
import { CreateEquipment } from '../components/CreateEquipment';
import { EquipmentDetail } from '../components/EquipmentDetail';
import { FC } from 'react';

export const Router: FC = () => {
  return (
    <Routes>
      <Route path='/find' element={<FindEquipment />} />
      <Route path='/create' element={<CreateEquipment />} />
      <Route path='/update/:id' element={<EquipmentDetail />} />
    </Routes>
  );
};