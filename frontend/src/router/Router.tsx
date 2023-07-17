import { Route, Routes } from 'react-router-dom';
import { FindEquipment } from '../components/FindEquipment';
import { EquipmentDetail } from '../components/EquipmentDetail';
import { CreateEquipment } from '../components/CreateEquipment';
import { UpdateEquipment } from '../components/UpdateEquipment';
import { FC } from 'react';

export const Router: FC = () => {
  return (
    <Routes>
      <Route path='/find' element={<FindEquipment />} />
      <Route path='/detail/:id' element={<EquipmentDetail />} />
      <Route path='/create' element={<CreateEquipment />} />
      <Route path='/update/:id' element={<UpdateEquipment />} />
    </Routes>
  )
}