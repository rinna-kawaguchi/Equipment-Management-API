import { useParams } from "react-router-dom";
import { memo, useCallback } from "react";

import { Plan } from "../../../types/Plan";
import { useMessage } from "../../../hooks/useMessage";
import { ConfirmModal } from "../../atoms/ConfirmModal";
import { instance } from "../../../axios/config";

type Props = {
  selectedPlan: Plan | null;
  isOpen: boolean;
  onClose: () => void;
  onPlanDelete: (deletedPlans: Array<Plan>) => void;
};

export const DeletePlanConfirmModal = memo((props: Props) => {
  const { selectedPlan, isOpen, onClose, onPlanDelete } = props;
  const { showMessage } = useMessage();
  const { id } = useParams();

  // Spring BootのAPIを叩いて指定したIDの点検計画を削除する
  const onClickDeletePlanExec = useCallback(async () => {
    let res = await instance.delete(`/plans/${selectedPlan?.checkPlanId}`)
      .catch(() => showMessage({ title: "点検計画の削除に失敗しました。", status: "error" }));
    if (res) {
      const response: string = res.data.message;
      showMessage({ title: response, status: "success" });
    }
    instance.get<Array<Plan>>(`/equipments/${id}/plans`)
      .then((res) => onPlanDelete(res.data));
    onClose();
  }, [selectedPlan]);

  return (
    <ConfirmModal isOpen={isOpen} onClose={onClose} onClickExec={onClickDeletePlanExec}>
      点検計画を削除しますか？
    </ConfirmModal>
  );

});