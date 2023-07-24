import { FormControl, FormLabel, Input, Modal, ModalBody, ModalCloseButton, ModalContent, ModalFooter, ModalHeader, ModalOverlay, Stack } from "@chakra-ui/react";
import { BaseButton } from "../atoms/BaseButton";
import { ChangeEvent, FC, memo, useState } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
import { Plan } from "../UpdateEquipment";

type Props = {
  isOpen: boolean;
  onClose: () => void;
  onPlanCreate: (createdPlans: Array<Plan>) => void;
};

export const CreatePlanModal: FC<Props> = memo((props) => {
  const { isOpen, onClose, onPlanCreate } = props;

  const [createCheckType, setCreateCheckType] = useState("");
  const [createPeriod, setCreatePeriod] = useState("");
  const [createDeadline, setCreateDeadline] = useState("");

  const { id } = useParams();

  // 入力した内容を点検計画の各項目に渡す
  const onChangeCreateCheckType = (e: ChangeEvent<HTMLInputElement>) => setCreateCheckType(e.target.value);
  const onChangeCreatePeriod = (e: ChangeEvent<HTMLInputElement>) => setCreatePeriod(e.target.value);
  const onChangeCreateDeadline = (e: ChangeEvent<HTMLInputElement>) => setCreateDeadline(e.target.value);

  // Spring BootのAPIを叩いて、前段で入力した内容で指定した設備IDの点検計画を登録し、登録後の点検計画を取得して反映する。
  const onClickCreatePlan = () => {
    alert("点検計画を追加しますか？");
    axios.post(`http://localhost:8080/equipments/${id}/plans`,
      { "checkType": createCheckType, "period": createPeriod, "deadline": createDeadline })
      .then(() => {
        axios.get<Array<Plan>>(`http://localhost:8080/equipments/${id}/plans`).then((res) => {
          onPlanCreate(res.data);
        })
      });
    onClose();
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} size={"sm"}>
      <ModalOverlay />
      <ModalContent pb={6}>
        <ModalHeader>点検計画追加</ModalHeader>
        <ModalCloseButton />
        <ModalBody mx={4}>
          <Stack spacing={4}>
            <FormControl>
              <FormLabel>点検種別</FormLabel>
              <Input placeholder="本格点検" onChange={onChangeCreateCheckType} />
            </FormControl>
            <FormControl>
              <FormLabel>点検周期</FormLabel>
              <Input placeholder="1年" onChange={onChangeCreatePeriod} />
            </FormControl>
            <FormControl>
              <FormLabel>点検期限</FormLabel>
              <Input placeholder="2023-12-31" onChange={onChangeCreateDeadline} />
            </FormControl>
          </Stack>
        </ModalBody>
        <ModalFooter>
          <BaseButton onClick={onClickCreatePlan}>点検計画追加</BaseButton>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
});