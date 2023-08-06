import { Navigate, Route, Routes } from 'react-router-dom';
import { SearchEquipment } from '../components/pages/SearchEquipment';
import { CreateEquipment } from '../components/pages/CreateEquipment';
import { EquipmentDetail } from '../components/pages/EquipmentDetail';
import { FC } from 'react';
import { NotFound } from '../components/pages/NotFound';
import { HeaderLayout } from '../components/templates/HeaderLayout';
import { HowToUse } from '../components/pages/HowToUse';

export const Router: FC = () => {
  return (
    <Routes>
      <Route path='/' element={<Navigate replace to='/search' />} />
      <Route path='/search' element={<HeaderLayout><SearchEquipment /></HeaderLayout>} />
      <Route path='/create' element={<HeaderLayout><CreateEquipment /></HeaderLayout>} />
      <Route path='/update/:id' element={<HeaderLayout><EquipmentDetail /></HeaderLayout>} />
      <Route path='/how_to_use' element={<HeaderLayout><HowToUse /></HeaderLayout>} />
      <Route path='*' element={<NotFound />} />
    </Routes>
  );
};