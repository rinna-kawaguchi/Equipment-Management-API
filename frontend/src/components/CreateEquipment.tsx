import { Box, Divider, FormControl, FormLabel, HStack, Heading, Input, Text } from "@chakra-ui/react";
import { BaseButton } from "./atoms/BaseButton";
import { useNavigate } from "react-router-dom";
import { ChangeEvent, useState } from "react";
import axios from "axios";
import { useMessage } from "../hooks/useMessage";

export const CreateEquipment = () => {
  const { showMessage } = useMessage();

  const [newName, setNewName] = useState("");
  const [newNumber, setNewNumber] = useState("");
  const [newLocation, setNewLocation] = useState("");

  // 入力した内容を設備情報の各項目に渡す
  const onChangeNewName = (e: ChangeEvent<HTMLInputElement>) => setNewName(e.target.value);
  const onChangeNewNumber = (e: ChangeEvent<HTMLInputElement>) => setNewNumber(e.target.value);
  const onChangeNewLocation = (e: ChangeEvent<HTMLInputElement>) => setNewLocation(e.target.value);

  const navigate = useNavigate();

  // 設備検索画面に遷移
  const onClickBackFindPage = () => navigate("/find");

  // Spring BootのAPIを叩いて、前段で入力した内容で設備情報を登録する。
  // できれば登録実行後に登録した設備の詳細画面に遷移するようにしたいが、設備IDの取得方法が分からず。。。
  const onClickCreateEquipment = async () => {
    alert("設備を登録しますか？");
    let res = await axios.post("http://localhost:8080/equipments",
      { "name": newName, "number": newNumber, "location": newLocation })
      .catch(() => showMessage({
        title: "設備の登録に失敗しました。入力に誤りがあります。", status: "error"
      }));
    if (res) {
      const response: string = res.data.message;
      showMessage({ title: response, status: "success" });
    }
  };

  return (
    <Box padding={5}>
      <Heading>新規設備登録</Heading>
      <br />
      <Heading size={"md"}>設備情報詳細</Heading>
      <Divider my={3} />
      <HStack spacing={10}>
        <Box>
          <FormControl>
            <FormLabel>設備名称</FormLabel>
            <Input width={"400px"} placeholder="設備名称" onChange={onChangeNewName} />
            <Text fontSize={"xs"} color={"red.400"}>※ 入力必須　※ 20文字以内で入力してください</Text>
          </FormControl>
        </Box>
        <Box>
          <FormControl>
            <FormLabel>設備番号</FormLabel>
            <Input width={"400px"} placeholder="設備番号" onChange={onChangeNewNumber} />
            <Text fontSize={"xs"} color={"red.400"}>※ 入力必須　※ 20文字以内で入力してください</Text>
          </FormControl>
        </Box>
        <Box>
          <FormControl>
            <FormLabel>設置場所</FormLabel>
            <Input width={"400px"} placeholder="設置場所" onChange={onChangeNewLocation} />
            <Text fontSize={"xs"} color={"red.400"}>※ 入力必須　※ 20文字以内で入力してください</Text>
          </FormControl>
        </Box>
      </HStack>
      <br />
      <br />
      <HStack>
        <BaseButton onClick={onClickBackFindPage}>戻る</BaseButton>
        <BaseButton onClick={onClickCreateEquipment}>登録</BaseButton>
      </HStack>
    </Box>
  );
};