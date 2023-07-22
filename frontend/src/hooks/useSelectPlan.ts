import { useCallback, useState } from "react"
import { Plan } from "../components/UpdateEquipment";

type Props = {
  checkPlanId: number;
  plans: Array<Plan>;
  openUpdatePlanModal: () => void;
}

// 選択した点検計画を特定し、モーダルを表示するカスタムフック
export const useSelectPlan = () => {
  const [selectedPlan, setSelectedPlan] = useState<Plan | null>(null);

  const onSelectPlan = useCallback((props: Props) => {
    const { checkPlanId, plans, openUpdatePlanModal } = props;
    const targetPlan = plans.find((plan) => plan.checkPlanId === checkPlanId);
    setSelectedPlan(targetPlan ?? null);
    openUpdatePlanModal();
  }, [])

  return { onSelectPlan, selectedPlan };
}