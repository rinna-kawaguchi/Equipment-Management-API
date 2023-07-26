import { FormControl, FormLabel, HStack, Input, Modal, ModalBody, ModalCloseButton, ModalContent, ModalFooter, ModalHeader, ModalOverlay, Stack } from "@chakra-ui/react";
import { ChangeEvent, FC, memo, useEffect, useState } from "react";
import axios from "axios";
import { BaseButton } from "../atoms/BaseButton";
import { History, Plan } from "../EquipmentDetail";
import { useParams } from "react-router-dom";

type Props = {
  selectedHistory: History | null;
  isOpen: boolean;
  onClose: () => void;
  onHistoryUpdate: (updatedHistories: Array<History>) => void;
};

export const UpdateHistoryModal: FC<Props> = memo((props) => {
  const { selectedHistory, isOpen, onClose, onHistoryUpdate } = props;

  const [updateImplementationDate, setUpdateImplementationDate] = useState("");
  const [updateCheckType, setUpdateCheckType] = useState("");
  const [updateResult, setUpdateResult] = useState("");

  const { id } = useParams();

  // propsで渡された点検履歴を各項目に渡す
  useEffect(() => {
    setUpdateImplementationDate(selectedHistory?.implementationDate ?? "");
    setUpdateCheckType(selectedHistory?.checkType ?? "");
    setUpdateResult(selectedHistory?.result ?? "");
  }, [selectedHistory]);

  // 入力した内容を点検履歴の各項目に渡す
  const onChangeUpdateImplementationDate = (e: ChangeEvent<HTMLInputElement>) => setUpdateImplementationDate(e.target.value);
  const onChangeUpdateCheckType = (e: ChangeEvent<HTMLInputElement>) => setUpdateCheckType(e.target.value);
  const onChangeUpdateResult = (e: ChangeEvent<HTMLInputElement>) => setUpdateResult(e.target.value);

  // Spring BootのAPIを叩いて、前段で入力した内容で指定したIDの点検履歴を更新し、更新後の点検履歴を取得して反映する
  const onClickUpdateHistory = async () => {
    alert("点検履歴を修正しますか？");
    let res = await axios.patch(`http://localhost:8080/histories/${selectedHistory?.checkHistoryId}`,
      {
        "implementationDate": updateImplementationDate, "checkType": updateCheckType,
        "result": updateResult
      });
    const response: Response = res.data.message;
    alert(response);
    axios.get<Array<History>>(`http://localhost:8080/equipments/${id}/histories`)
      .then((res) => onHistoryUpdate(res.data));
    onClose();
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} size={"sm"}>
      <ModalOverlay />
      <ModalContent pb={6}>
        <ModalHeader>点検履歴修正</ModalHeader>
        <ModalCloseButton />
        <ModalBody mx={4}>
          <Stack spacing={4}>
            <FormControl>
              <FormLabel>実施日</FormLabel>
              <Input value={updateImplementationDate} onChange={onChangeUpdateImplementationDate} />
            </FormControl>
            <FormControl>
              <FormLabel>点検種別</FormLabel>
              <Input value={updateCheckType} onChange={onChangeUpdateCheckType} />
            </FormControl>
            <FormControl>
              <FormLabel>点検結果</FormLabel>
              <Input value={updateResult} onChange={onChangeUpdateResult} />
            </FormControl>
          </Stack>
        </ModalBody>
        <ModalFooter>
          <BaseButton onClick={onClickUpdateHistory}>点検計画修正</BaseButton>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
});