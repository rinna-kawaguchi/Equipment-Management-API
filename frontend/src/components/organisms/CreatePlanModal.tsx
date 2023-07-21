import { FormControl, FormLabel, HStack, Input, Modal, ModalBody, ModalCloseButton, ModalContent, ModalFooter, ModalHeader, ModalOverlay } from "@chakra-ui/react"
import { BaseButton } from "../atoms/BaseButton"
import { ChangeEvent, FC, useState } from "react";
import axios from "axios";
import { type } from "os";
import { useParams } from "react-router-dom";

type Props = {
  isOpen: boolean;
  onClose: () => void;
}

export const CreatePlanModal: FC<Props> = (props) => {
  const { isOpen, onClose } = props;

  const [createCheckType, setCreateCheckType] = useState("");
  const [createPeriod, setCreatePeriod] = useState("");
  const [createDeadline, setCreateDeadline] = useState("");

  const { id } = useParams();

  const onChangeCreateCheckType = (e: ChangeEvent<HTMLInputElement>) => setCreateCheckType(e.target.value)
  const onChangeCreatePeriod = (e: ChangeEvent<HTMLInputElement>) => setCreatePeriod(e.target.value)
  const onChangeCreateDeadline = (e: ChangeEvent<HTMLInputElement>) => setCreateDeadline(e.target.value)

  const onClickCreatePlan = () => {
    alert("点検計画を追加しますか？")
    axios.post(`http://localhost:8080/equipments/${id}/plans`,
    { "checkType": createCheckType, "period": createPeriod, "deadline": createDeadline });
    onClose();
  }

  return (
    <Modal isOpen={isOpen} onClose={onClose} size={"xl"}>
    <ModalOverlay />
    <ModalContent pb={6}>
      <ModalHeader>点検計画追加</ModalHeader>
      <ModalCloseButton />
      <ModalBody mx={4}>
        <HStack spacing={4}>
          <FormControl>
            <FormLabel>点検種別</FormLabel>
            <Input placeholder="点検種別" onChange={onChangeCreateCheckType} />
          </FormControl>
          <FormControl>
            <FormLabel>点検周期</FormLabel>
            <Input placeholder="点検周期" onChange={onChangeCreatePeriod} />
          </FormControl>
          <FormControl>
            <FormLabel>点検期限</FormLabel>
            <Input placeholder="点検期限" onChange={onChangeCreateDeadline} />
          </FormControl>
        </HStack>
      </ModalBody>
      <ModalFooter>
        <BaseButton onClick={onClickCreatePlan}>点検計画追加</BaseButton>
      </ModalFooter>
    </ModalContent>
  </Modal>
  )
}