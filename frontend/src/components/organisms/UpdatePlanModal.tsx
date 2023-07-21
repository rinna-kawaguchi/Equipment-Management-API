import { FormControl, FormLabel, HStack, Input, Modal, ModalBody, ModalCloseButton, ModalContent, ModalFooter, ModalHeader, ModalOverlay } from "@chakra-ui/react"
import { ChangeEvent, FC, useEffect, useState } from "react";
import axios from "axios";
import { Plan } from "../EquipmentDetail";
import { BaseButton } from "../atoms/BaseButton";

type Props = {
  selectedPlan: Plan | null;
  isOpen: boolean;
  onClose: () => void;
}

export const UpdatePlanModal: FC<Props> = (props) => {
  const { selectedPlan, isOpen, onClose } = props;

  const [updateCheckType, setUpdateCheckType] = useState("");
  const [updatePeriod, setUpdatePeriod] = useState("");
  const [updateDeadline, setUpdateDeadline] = useState("");

  useEffect(() => {
    setUpdateCheckType(selectedPlan?.checkType ?? "");
    setUpdatePeriod(selectedPlan?.period ?? "");
    setUpdateDeadline(selectedPlan?.deadline ?? "");
  }, [selectedPlan])

  const onChangeUpdateCheckType = (e: ChangeEvent<HTMLInputElement>) => setUpdateCheckType(e.target.value);
  const onChangeUpdatePeriod = (e: ChangeEvent<HTMLInputElement>) => setUpdatePeriod(e.target.value);
  const onChangeUpdateDeadline = (e: ChangeEvent<HTMLInputElement>) => setUpdateDeadline(e.target.value);

  const onClickUpdatePlan = () => {
    axios.patch(`http://localhost:8080/plans/${selectedPlan?.checkPlanId}`,
      { "checkType": updateCheckType, "period": updatePeriod, "deadline": updateDeadline });
    alert("点検計画を修正します");
    onClose();
  }

  return (
    <Modal isOpen={isOpen} onClose={onClose} size={"xl"}>
    <ModalOverlay />
    <ModalContent pb={6}>
      <ModalHeader>点検計画修正</ModalHeader>
      <ModalCloseButton />
      <ModalBody mx={4}>
        <HStack spacing={4}>
          <FormControl>
            <FormLabel>点検種別</FormLabel>
            <Input value={updateCheckType} onChange={onChangeUpdateCheckType} />
          </FormControl>
          <FormControl>
            <FormLabel>点検周期</FormLabel>
            <Input value={updatePeriod} onChange={onChangeUpdatePeriod} />
          </FormControl>
          <FormControl>
            <FormLabel>点検期限</FormLabel>
            <Input value={updateDeadline} onChange={onChangeUpdateDeadline} />
          </FormControl>
        </HStack>
      </ModalBody>
      <ModalFooter>
        <BaseButton onClick={onClickUpdatePlan}>点検計画修正</BaseButton>
      </ModalFooter>
    </ModalContent>
  </Modal>
  )
}