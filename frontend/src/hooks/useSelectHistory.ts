import { useCallback, useState } from "react";
import { History, Plan } from "../components/EquipmentDetail";

type Props = {
  checkHistoryId: number;
  histories: Array<History>;
  openUpdateHistoryModal: () => void;
};

// 選択した点検計画を特定し、モーダルを表示するカスタムフック
export const useSelectHistory = () => {
  const [selectedHistory, setSelectedHistory] = useState<History | null>(null);

  const onSelectHistory = useCallback((props: Props) => {
    const { checkHistoryId, histories, openUpdateHistoryModal } = props;
    const targetHistory = histories.find((history) => history.checkHistoryId === checkHistoryId);
    setSelectedHistory(targetHistory ?? null);
    openUpdateHistoryModal();
  }, []);

  return { onSelectHistory, selectedHistory };
};