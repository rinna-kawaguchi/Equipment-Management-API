import { useCallback, useState } from "react";
import { Plan } from "../components/EquipmentDetail";

type Props = {
  checkPlanId: number;
  plans: Array<Plan>;
};

// 選択した点検計画を特定するカスタムフック
export const useSelectPlan = () => {
  const [selectedPlan, setSelectedPlan] = useState<Plan | null>(null);

  const onSelectPlan = useCallback((props: Props) => {
    const { checkPlanId, plans } = props;
    const targetPlan = plans.find((plan) => plan.checkPlanId === checkPlanId);
    setSelectedPlan(targetPlan ?? null);
  }, []);

  return { onSelectPlan, selectedPlan };
};