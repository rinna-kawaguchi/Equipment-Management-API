import { Route, Routes } from 'react-router-dom';
import { FindEquipment } from '../components/pages/FindEquipment';
import { CreateEquipment } from '../components/pages/CreateEquipment';
import { EquipmentDetail } from '../components/pages/EquipmentDetail';
import { FC } from 'react';
import { NotFound } from '../components/pages/NotFound';
import { HeaderLayout } from '../components/templates/HeaderLayout';
import { HowToUse } from '../components/pages/HowToUse';

export const Router: FC = () => {
  return (
    <Routes>
      <Route path='/find' element={<HeaderLayout><FindEquipment /></HeaderLayout>} />
      <Route path='/create' element={<HeaderLayout><CreateEquipment /></HeaderLayout>} />
      <Route path='/update/:id' element={<HeaderLayout><EquipmentDetail /></HeaderLayout>} />
      <Route path='/how_to_use' element={<HeaderLayout><HowToUse /></HeaderLayout>} />
      <Route path='*' element={<NotFound />} />
    </Routes>
  );
};