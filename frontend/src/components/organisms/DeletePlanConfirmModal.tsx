import { FormControl, FormLabel, HStack, Input, Modal, ModalBody, ModalCloseButton, ModalContent, ModalFooter, ModalHeader, ModalOverlay, Stack, Text } from "@chakra-ui/react";
import { BaseButton } from "../atoms/BaseButton";
import { Plan } from "../EquipmentDetail";
import axios from "axios";
import { useMessage } from "../../hooks/useMessage";
import { useParams } from "react-router-dom";

type Props = {
  selectedPlan: Plan | null;
  isOpen: boolean;
  onClose: () => void;
  onPlanDelete: (deletedPlans: Array<Plan>) => void;
};

export const DeletePlanConfirmModal = (props: Props) => {
  const { selectedPlan, isOpen, onClose, onPlanDelete } = props;
  const { showMessage } = useMessage();
  const { id } = useParams();

  const onClickDeletePlanExec = async () => {
    let res = await axios.delete(`http://localhost:8080/plans/${selectedPlan?.checkPlanId}`)
      .catch(() => showMessage({ title: "点検計画の削除に失敗しました。", status: "error" }));
    if (res) {
      const response: string = res.data.message;
      showMessage({ title: response, status: "success" });
    }
    axios.get<Array<Plan>>(`http://localhost:8080/equipments/${id}/plans`)
      .then((res) => onPlanDelete(res.data));
      onClose();
    };

  const onClickDeletePlanCancel = () => onClose();

  return (
    <Modal isOpen={isOpen} onClose={onClose} size={"sm"}>
      <ModalOverlay />
      <ModalContent pb={6}>
        <ModalCloseButton />
        <ModalBody mx={4}>
          <p>点検計画を削除しますか？</p>
          <HStack>
            <BaseButton onClick={onClickDeletePlanExec}>はい</BaseButton>
            <BaseButton onClick={onClickDeletePlanCancel}>いいえ</BaseButton>
          </HStack>
        </ModalBody>
      </ModalContent>
    </Modal>
  );

};