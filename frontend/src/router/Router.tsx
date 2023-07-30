import { Route, Routes } from 'react-router-dom';
import { FindEquipment } from '../components/pages/FindEquipment';
import { CreateEquipment } from '../components/pages/CreateEquipment';
import { EquipmentDetail } from '../components/EquipmentDetail';
import { FC } from 'react';
import { NotFound } from '../components/NotFound';

export const Router: FC = () => {
  return (
    <Routes>
      <Route path='/find' element={<FindEquipment />} />
      <Route path='/create' element={<CreateEquipment />} />
      <Route path='/update/:id' element={<EquipmentDetail />} />
      <Route path='*' element={<NotFound />} />
    </Routes>
  );
};