import { ChangeEvent, FC, memo, useCallback, useEffect, useState } from "react";
import { FormControl, FormLabel, Input, Modal, ModalBody, ModalCloseButton, ModalContent, ModalFooter, ModalHeader, ModalOverlay, Stack, Text } from "@chakra-ui/react";
import { useParams } from "react-router-dom";

import { BaseButton } from "../../atoms/BaseButton";
import { Plan } from "../../../types/Plan";
import { useMessage } from "../../../hooks/useMessage";
import { instance } from "../../../axios/config";

type Props = {
  selectedPlan: Plan | null;
  isOpen: boolean;
  onClose: () => void;
  onPlanUpdate: (updatedPlans: Array<Plan>) => void;
};

export const UpdatePlanModal: FC<Props> = memo((props) => {
  const { selectedPlan, isOpen, onClose, onPlanUpdate } = props;
  const { showMessage } = useMessage();
  const { id } = useParams();

  const [updateCheckType, setUpdateCheckType] = useState("");
  const [updatePeriod, setUpdatePeriod] = useState("");
  const [updateDeadline, setUpdateDeadline] = useState("");

  // propsで渡された点検計画を各項目に渡す
  useEffect(() => {
    setUpdateCheckType(selectedPlan?.checkType ?? "");
    setUpdatePeriod(selectedPlan?.period ?? "");
    setUpdateDeadline(selectedPlan?.deadline ?? "");
  }, [selectedPlan, isOpen]);

  // 入力した内容を設備情報の各項目に渡す
  const onChangeUpdateCheckType = (e: ChangeEvent<HTMLInputElement>) => setUpdateCheckType(e.target.value);
  const onChangeUpdatePeriod = (e: ChangeEvent<HTMLInputElement>) => setUpdatePeriod(e.target.value);
  const onChangeUpdateDeadline = (e: ChangeEvent<HTMLInputElement>) => setUpdateDeadline(e.target.value);

  // Spring BootのAPIを叩いて、前段で入力した内容で指定したIDの点検計画を更新し、更新後の点検計画を取得して反映する
  const onClickUpdatePlan = useCallback(async () => {
    let res = await instance.patch(`/plans/${selectedPlan?.checkPlanId}`,
      { "checkType": updateCheckType, "period": updatePeriod, "deadline": updateDeadline })
      .catch(() => showMessage({
        title: "点検計画の修正に失敗しました。入力に誤りがあります。", status: "error"
      }));
    if (res) {
      const response: string = res.data.message;
      showMessage({ title: response, status: "success" });
      instance.get<Array<Plan>>(`/equipments/${id}/plans`)
        .then((res) => onPlanUpdate(res.data));
      onClose();
    }
  }, [updateCheckType, updatePeriod, updateDeadline]);

  return (
    <Modal isOpen={isOpen} onClose={onClose} size={"sm"}>
      <ModalOverlay />
      <ModalContent pb={6}>
        <ModalHeader>点検計画修正</ModalHeader>
        <ModalCloseButton />
        <ModalBody mx={4}>
          <Stack spacing={4}>
            <FormControl>
              <FormLabel>点検種別</FormLabel>
              <Text fontSize={"xs"} color={"red.400"}>※ 入力必須　※ 10文字以内で入力してください</Text>
              <Input value={updateCheckType} onChange={onChangeUpdateCheckType} />
            </FormControl>
            <FormControl>
              <FormLabel>点検周期</FormLabel>
              <Text fontSize={"xs"} color={"red.400"}>※ 入力必須　※ 10文字以内で入力してください</Text>
              <Input value={updatePeriod} onChange={onChangeUpdatePeriod} />
            </FormControl>
            <FormControl>
              <FormLabel>点検期限</FormLabel>
              <Text fontSize={"xs"} color={"red.400"}>※ yyyy-mm-ddで入力してください</Text>
              <Input value={updateDeadline} onChange={onChangeUpdateDeadline} />
            </FormControl>
          </Stack>
        </ModalBody>
        <ModalFooter>
          <BaseButton onClick={onClickUpdatePlan}>点検計画修正</BaseButton>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
});