import { FormControl, FormLabel, Input, Modal, ModalBody, ModalCloseButton, ModalContent, ModalFooter, ModalHeader, ModalOverlay, Stack } from "@chakra-ui/react";
import { BaseButton } from "../atoms/BaseButton";
import { ChangeEvent, FC, memo, useState } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
import { History } from "../EquipmentDetail";

type Props = {
  isOpen: boolean;
  onClose: () => void;
  onHistoryCreate: (createdHistories: Array<History>) => void;
};

export const CreateHistoryModal: FC<Props> = memo((props) => {
  const { isOpen, onClose, onHistoryCreate } = props;

  const [createImplementationDate, setCreateImplementationDate] = useState("");
  const [createCheckType, setCreateCheckType] = useState("");
  const [createResult, setCreateResult] = useState("");

  const { id } = useParams();

  // 入力した内容を点検履歴の各項目に渡す
  const onChangeCreateImplementationDate = (e: ChangeEvent<HTMLInputElement>) => setCreateImplementationDate(e.target.value);
  const onChangeCreateCheckType = (e: ChangeEvent<HTMLInputElement>) => setCreateCheckType(e.target.value);
  const onChangeCreateResult = (e: ChangeEvent<HTMLInputElement>) => setCreateResult(e.target.value);

  // Spring BootのAPIを叩いて、前段で入力した内容で指定した設備IDの点検履歴を登録し、登録後の点検履歴を取得して反映する。
  const onClickCreateHistory = async () => {
    alert("点検履歴を追加しますか？");
    let res = await axios.post(`http://localhost:8080/equipments/${id}/histories`,
      {
        "implementationDate": createImplementationDate, "checkType": createCheckType,
        "result": createResult
      });
    const response: Response = res.data.message;
    alert(response);
    axios.get<Array<History>>(`http://localhost:8080/equipments/${id}/histories`)
      .then((res) => onHistoryCreate(res.data));
    onClose();
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
              <Input placeholder="2023-12-31" onChange={onChangeCreateImplementationDate} />
            </FormControl>
            <FormControl>
              <FormLabel>点検種別</FormLabel>
              <Input placeholder="本格点検" onChange={onChangeCreateCheckType} />
            </FormControl>
            <FormControl>
              <FormLabel>点検結果</FormLabel>
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