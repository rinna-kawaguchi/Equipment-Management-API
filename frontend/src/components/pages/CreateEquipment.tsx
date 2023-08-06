import { memo, useCallback, useState } from "react";
import { Box, Divider, HStack, Heading } from "@chakra-ui/react";
import { useNavigate } from "react-router-dom";

import { BaseButton } from "../atoms/BaseButton";
import { CreateEquipmentInput } from "../molecules/CreateEquipmentInput";
import { useMessage } from "../../hooks/useMessage";
import { instance } from "../../axios/config";

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
  const onClickBackSearchPage = () => navigate("/search");

  // Spring BootのAPIを叩いて、前段で入力した内容で設備情報を登録する。その後、登録した設備の詳細画面に遷移する。
  const onClickCreateEquipment = async () => {
    let res = await instance.post("/equipments",
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
    <Box px={10} py={5}>
      <Heading size="lg">新規設備登録</Heading>
      <Divider my={3} />
      <Box px={3}>
        <CreateEquipmentInput onEquipmentCreate={handleCreateEquipment} />
        <br />
        <br />
        <HStack>
          <BaseButton onClick={onClickBackSearchPage}>戻る</BaseButton>
          <BaseButton onClick={onClickCreateEquipment}>登録</BaseButton>
        </HStack>
      </Box>
    </Box>
  );
});