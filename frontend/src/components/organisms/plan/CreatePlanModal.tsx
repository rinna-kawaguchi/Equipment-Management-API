import { FormControl, FormLabel, Input, Modal, ModalBody, ModalCloseButton, ModalContent, ModalFooter, ModalHeader, ModalOverlay, Stack, Text } from "@chakra-ui/react";
import { BaseButton } from "../../atoms/BaseButton";
import { ChangeEvent, FC, memo, useState } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
import { Plan } from "../../../types/Plan";
import { useMessage } from "../../../hooks/useMessage";

type Props = {
  isOpen: boolean;
  onClose: () => void;
  onPlanCreate: (createdPlans: Array<Plan>) => void;
};

export const CreatePlanModal: FC<Props> = memo((props) => {
  const { isOpen, onClose, onPlanCreate } = props;
  const { showMessage } = useMessage();
  const { id } = useParams();

  const [createCheckType, setCreateCheckType] = useState("");
  const [createPeriod, setCreatePeriod] = useState("");
  const [createDeadline, setCreateDeadline] = useState("");

  // 入力した内容を点検計画の各項目に渡す
  const onChangeCreateCheckType = (e: ChangeEvent<HTMLInputElement>) => setCreateCheckType(e.target.value);
  const onChangeCreatePeriod = (e: ChangeEvent<HTMLInputElement>) => setCreatePeriod(e.target.value);
  const onChangeCreateDeadline = (e: ChangeEvent<HTMLInputElement>) => setCreateDeadline(e.target.value);

  // Spring BootのAPIを叩いて、前段で入力した内容で指定した設備IDの点検計画を登録し、登録後の点検計画を取得して反映する。
  const onClickCreatePlan = async () => {
    let res = await axios.post(`http://localhost:8080/equipments/${id}/plans`,
      { "checkType": createCheckType, "period": createPeriod, "deadline": createDeadline })
      .catch(() => showMessage({
        title: "点検計画の追加に失敗しました。入力に誤りがあります。", status: "error"
      }));
    if (res) {
      const response: string = res.data.message;
      showMessage({ title: response, status: "success" });
    }
    axios.get<Array<Plan>>(`http://localhost:8080/equipments/${id}/plans`)
      .then((res) => onPlanCreate(res.data));
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
              <Text fontSize={"xs"} color={"red.400"}>※ 入力必須　※ 10文字以内で入力してください</Text>
              <Input placeholder="本格点検" onChange={onChangeCreateCheckType} />
            </FormControl>
            <FormControl>
              <FormLabel>点検周期</FormLabel>
              <Text fontSize={"xs"} color={"red.400"}>※ 入力必須　※ 10文字以内で入力してください</Text>
              <Input placeholder="1年" onChange={onChangeCreatePeriod} />
            </FormControl>
            <FormControl>
              <FormLabel>点検期限</FormLabel>
              <Text fontSize={"xs"} color={"red.400"}>※ yyyy-mm-ddで入力してください</Text>
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