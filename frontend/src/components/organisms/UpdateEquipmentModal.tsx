import { ChangeEvent, FC, memo, useEffect, useState } from "react";
import { Equipment } from "../FindEquipment";
import { FormControl, FormLabel, HStack, Input, Modal, ModalBody, ModalCloseButton, ModalContent, ModalFooter, ModalHeader, ModalOverlay, Stack } from "@chakra-ui/react";
import { BaseButton } from "../atoms/BaseButton";
import axios from "axios";
import { useParams } from "react-router-dom";

type Props = {
  updateEquipment: Equipment | null;
  isOpen: boolean;
  onClose: () => void;
}

export const UpdateEquipmentModal: FC<Props> = memo((props) => {
  const { updateEquipment, isOpen, onClose } = props;

  const [updateName, setUpdateName] = useState("");
  const [updateNumber, setUpdateNumber] = useState("");
  const [updateLocation, setUpdateLocation] = useState("");

  const { id } = useParams();

  useEffect(() => {
    setUpdateName(updateEquipment?.name ?? "");
    setUpdateNumber(updateEquipment?.number ?? "");
    setUpdateLocation(updateEquipment?.location ?? "");
  }, [updateEquipment]);

  const onChangeUpdateName = (e: ChangeEvent<HTMLInputElement>) => setUpdateName(e.target.value)
  const onChangeUpdateNumber = (e: ChangeEvent<HTMLInputElement>) => setUpdateNumber(e.target.value)
  const onChangeUpdateLocation = (e: ChangeEvent<HTMLInputElement>) => setUpdateLocation(e.target.value)

  const onClickUpdate = () => {
    alert("設備情報を修正しますか？");
    axios.patch(`http://localhost:8080/equipments/${id}`,
      { "name": updateName, "number": updateNumber, "location": updateLocation });
    onClose();
  }

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
              <Input value={updateName} placeholder="設備名称" onChange={onChangeUpdateName} />
            </FormControl>
            <FormControl>
              <FormLabel>設備番号</FormLabel>
              <Input value={updateNumber} placeholder="設備番号" onChange={onChangeUpdateNumber} />
            </FormControl>
            <FormControl>
              <FormLabel>設置場所</FormLabel>
              <Input value={updateLocation} placeholder="設置場所" onChange={onChangeUpdateLocation} />
            </FormControl>
          </Stack>
        </ModalBody>
        <ModalFooter>
          <BaseButton onClick={onClickUpdate}>修正</BaseButton>
        </ModalFooter>
      </ModalContent>
    </Modal>
  )
});