import { memo, useCallback, useState } from "react";
import { Box, Divider, HStack, Heading } from "@chakra-ui/react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

import { BaseButton } from "../atoms/BaseButton";
import { CreateEquipmentInput } from "../molecules/CreateEquipmentInput";
import { useMessage } from "../../hooks/useMessage";

export const CreateEquipment = memo(() => {
  const { showMessage } = useMessage();

  const [newName, setNewName] = useState("");
  const [newNumber, setNewNumber] = useState("");
  const [newLocation, setNewLocation] = useState("");

  // CreateEquipmentInputで入力された内容を設備情報の各項目に渡す
  const handleCreateEquipment = useCallback((newName: string, newNumber: string, newLocation: string) => {
    setNewName(newName);
    setNewNumber(newNumber);
    setNewLocation(newLocation);
  }, []);

  const navigate = useNavigate();

  // 設備検索画面に遷移
  const onClickBackFindPage = () => navigate("/find");

  // Spring BootのAPIを叩いて、前段で入力した内容で設備情報を登録する。その後、登録した設備の詳細画面に遷移する。
  const onClickCreateEquipment = async () => {
    let res = await axios.post("http://localhost:8080/equipments",
      { "name": newName, "number": newNumber, "location": newLocation })
      .catch(() => showMessage({
        title: "設備の登録に失敗しました。入力に誤りがあります。", status: "error"
      }));
    if (res) {
      const response: string = res.data.message;
      showMessage({ title: `${response}。設備詳細画面に遷移します。`, status: "success" });
      const newId = res.data.newId;
      navigate(`/update/${newId}`);
    }
  };

  return (
    <Box padding={5}>
      <Heading>新規設備登録</Heading>
      <br />
      <Heading size="lg">設備情報詳細</Heading>
      <Divider my={3} />
      <CreateEquipmentInput onEquipmentCreate={handleCreateEquipment} />
      <br />
      <br />
      <HStack>
        <BaseButton onClick={onClickBackFindPage}>戻る</BaseButton>
        <BaseButton onClick={onClickCreateEquipment}>登録</BaseButton>
      </HStack>
    </Box>
  );
});