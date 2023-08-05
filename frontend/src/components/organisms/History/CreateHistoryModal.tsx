import { ChangeEvent, FC, memo, useState } from "react";
import { FormControl, FormLabel, Input, Modal, ModalBody, ModalCloseButton, ModalContent, ModalFooter, ModalHeader, ModalOverlay, Stack, Text } from "@chakra-ui/react";
import { useParams } from "react-router-dom";

import { BaseButton } from "../../atoms/BaseButton";
import { History } from "../../../types/History";
import { useMessage } from "../../../hooks/useMessage";
import { instance } from "../../../axios/config";

type Props = {
  isOpen: boolean;
  onClose: () => void;
  onHistoryCreate: (createdHistories: Array<History>) => void;
};

export const CreateHistoryModal: FC<Props> = memo((props) => {
  const { isOpen, onClose, onHistoryCreate } = props;
  const { showMessage } = useMessage();
  const { id } = useParams();

  const [createImplementationDate, setCreateImplementationDate] = useState("");
  const [createCheckType, setCreateCheckType] = useState("");
  const [createResult, setCreateResult] = useState("");

  // 入力した内容を点検履歴の各項目に渡す
  const onChangeCreateImplementationDate = (e: ChangeEvent<HTMLInputElement>) => setCreateImplementationDate(e.target.value);
  const onChangeCreateCheckType = (e: ChangeEvent<HTMLInputElement>) => setCreateCheckType(e.target.value);
  const onChangeCreateResult = (e: ChangeEvent<HTMLInputElement>) => setCreateResult(e.target.value);

  // Spring BootのAPIを叩いて、前段で入力した内容で指定した設備IDの点検履歴を登録し、登録後の点検履歴を取得して反映する。
  const onClickCreateHistory = async () => {
    let res = await instance.post(`/equipments/${id}/histories`,
      {
        "implementationDate": createImplementationDate, "checkType": createCheckType,
        "result": createResult
      })
      .catch(() => showMessage({
        title: "点検履歴の追加に失敗しました。入力に誤りがあります。", status: "error"
      }));
    if (res) {
      const response: string = res.data.message;
      showMessage({ title: response, status: "success" });
      instance.get<Array<History>>(`/equipments/${id}/histories`)
        .then((res) => onHistoryCreate(res.data));
      onClose();
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} size={"sm"}>
      <ModalOverlay />
      <ModalContent pb={6}>
        <ModalHeader>点検履歴追加</ModalHeader>
        <ModalCloseButton />
        <ModalBody mx={4}>
          <Stack spacing={4}>
            <FormControl>
              <FormLabel>実施日</FormLabel>
              <Text fontSize={"xs"} color={"red.400"}>※ 入力必須　※ yyyy-mm-ddで入力してください</Text>
              <Input placeholder="2023-12-31" onChange={onChangeCreateImplementationDate} />
            </FormControl>
            <FormControl>
              <FormLabel>点検種別</FormLabel>
              <Text fontSize={"xs"} color={"red.400"}>※ 入力必須　※ 10文字以内で入力してください</Text>
              <Input placeholder="本格点検" onChange={onChangeCreateCheckType} />
            </FormControl>
            <FormControl>
              <FormLabel>点検結果</FormLabel>
              <Text fontSize={"xs"} color={"red.400"}>※ 入力必須　※ 50文字以内で入力してください</Text>
              <Input placeholder="良" onChange={onChangeCreateResult} />
            </FormControl>
          </Stack>
        </ModalBody>
        <ModalFooter>
          <BaseButton onClick={onClickCreateHistory}>点検履歴追加</BaseButton>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
});