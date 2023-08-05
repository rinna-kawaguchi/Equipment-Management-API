import { ChangeEvent, FC, memo, useEffect, useState } from "react";
import { FormControl, FormLabel, Input, Modal, ModalBody, ModalCloseButton, ModalContent, ModalFooter, ModalHeader, ModalOverlay, Stack, Text } from "@chakra-ui/react";
import { useParams } from "react-router-dom";

import { Equipment } from "../../types/Equipment";
import { BaseButton } from "../atoms/BaseButton";
import { useMessage } from "../../hooks/useMessage";
import { instance } from "../../axios/config";

type Props = {
  updateEquipment: Equipment | null;
  isOpen: boolean;
  onClose: () => void;
  onEquipmentsUpdate: (updatedEquipments: Equipment) => void;
};

export const UpdateEquipmentModal: FC<Props> = memo((props) => {
  const { updateEquipment, isOpen, onClose, onEquipmentsUpdate } = props;
  const { showMessage } = useMessage();
  const { id } = useParams();

  const [updateName, setUpdateName] = useState("");
  const [updateNumber, setUpdateNumber] = useState("");
  const [updateLocation, setUpdateLocation] = useState("");

  // propsで渡された設備情報を各項目に渡す
  useEffect(() => {
    setUpdateName(updateEquipment?.name ?? "");
    setUpdateNumber(updateEquipment?.number ?? "");
    setUpdateLocation(updateEquipment?.location ?? "");
  }, [updateEquipment, isOpen]);

  // 入力した内容を設備情報の各項目に渡す
  const onChangeUpdateName = (e: ChangeEvent<HTMLInputElement>) => setUpdateName(e.target.value);
  const onChangeUpdateNumber = (e: ChangeEvent<HTMLInputElement>) => setUpdateNumber(e.target.value);
  const onChangeUpdateLocation = (e: ChangeEvent<HTMLInputElement>) => setUpdateLocation(e.target.value);

  // Spring BootのAPIを叩いて、前段で入力した内容で指定した設備IDの設備情報を更新し、更新後の設備情報を取得して反映する
  const onClickUpdate = async () => {
    let res = await instance.patch(`/equipments/${id}`,
      { "name": updateName, "number": updateNumber, "location": updateLocation })
      .catch(() => showMessage({
        title: "設備情報の修正に失敗しました。入力に誤りがあります。", status: "error"
      }));
    if (res) {
      const response: string = res.data.message;
      showMessage({ title: response, status: "success" });
      instance.get<Equipment>(`/equipments/${id}`)
        .then((res) => onEquipmentsUpdate(res.data));
      onClose();
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} size={"lg"}>
      <ModalOverlay />
      <ModalContent pb={6}>
        <ModalHeader>設備情報修正</ModalHeader>
        <ModalCloseButton />
        <ModalBody mx={4}>
          <Stack spacing={4}>
            <FormControl>
              <FormLabel>設備名称</FormLabel>
              <Text fontSize={"xs"} color={"red.400"}>※ 入力必須　※ 20文字以内で入力してください</Text>
              <Input value={updateName} placeholder="設備名称" onChange={onChangeUpdateName} />
            </FormControl>
            <FormControl>
              <FormLabel>設備番号</FormLabel>
              <Text fontSize={"xs"} color={"red.400"}>※ 入力必須　※ 20文字以内で入力してください</Text>
              <Input value={updateNumber} placeholder="設備番号" onChange={onChangeUpdateNumber} />
            </FormControl>
            <FormControl>
              <FormLabel>設置場所</FormLabel>
              <Text fontSize={"xs"} color={"red.400"}>※ 入力必須　※ 20文字以内で入力してください</Text>
              <Input value={updateLocation} placeholder="設置場所" onChange={onChangeUpdateLocation} />
            </FormControl>
          </Stack>
        </ModalBody>
        <ModalFooter>
          <BaseButton onClick={onClickUpdate}>修正</BaseButton>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
});