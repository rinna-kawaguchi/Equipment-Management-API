import { useCallback, useState } from "react";
import { History } from "../types/History";

type Props = {
  checkHistoryId: number;
  histories: Array<History>;
};

// 選択した点検履歴を特定するカスタムフック
export const useSelectHistory = () => {
  const [selectedHistory, setSelectedHistory] = useState<History | null>(null);

  const onSelectHistory = useCallback((props: Props) => {
    const { checkHistoryId, histories } = props;
    const targetHistory = histories.find((history) => history.checkHistoryId === checkHistoryId);
    setSelectedHistory(targetHistory ?? null);
  }, []);

  return { onSelectHistory, selectedHistory };
};