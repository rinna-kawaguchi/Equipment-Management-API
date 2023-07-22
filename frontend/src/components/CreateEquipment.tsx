import { Box, Divider, HStack, Heading, Input } from "@chakra-ui/react";
import { BaseButton } from "./atoms/BaseButton";
import { useNavigate } from "react-router-dom";
import { ChangeEvent, useState } from "react";
import axios from "axios";

export const CreateEquipment = () => {
  const [newName, setNewName] = useState("");
  const [newNumber, setNewNumber] = useState("");
  const [newLocation, setNewLocation] = useState("");
  const [createMessage, setCreateMessage] = useState("");

  // 入力した内容を設備情報の各項目に渡す
  const onChangeNewName = (e: ChangeEvent<HTMLInputElement>) => setNewName(e.target.value);
  const onChangeNewNumber = (e: ChangeEvent<HTMLInputElement>) => setNewNumber(e.target.value);
  const onChangeNewLocation = (e: ChangeEvent<HTMLInputElement>) => setNewLocation(e.target.value);

  const navigate = useNavigate();

  // 設備検索画面に遷移
  const onClickBackFindPage = () => navigate("/find");

  // Spring BootのAPIを叩いて、前段で入力した内容で設備情報を登録する。
  // できれば登録実行後に登録した設備の詳細画面に遷移するようにしたいが、設備IDの取得方法が分からず。。。
  const onClickCreateEquipment = () => {
    alert("設備を登録しますか？");
    axios.post("http://localhost:8080/equipments", { "name": newName, "number": newNumber, "location": newLocation });
    alert("設備が登録されました");
  };

  return (
    <Box padding={5}>
      <Heading>新規設備登録</Heading>
      <br />
      <Heading size={"md"}>設備情報詳細</Heading>
      <Divider my={3} />
      <HStack>
        <p>設備名称</p>
        <Input width={"400px"} placeholder="設備名称" onChange={onChangeNewName} />
        <p>設備番号</p>
        <Input width={"400px"} placeholder="設備番号" onChange={onChangeNewNumber} />
        <p>設置場所</p>
        <Input width={"400px"} placeholder="設置場所" onChange={onChangeNewLocation} />
      </HStack>
      <br />
      <br />
      <BaseButton onClick={onClickBackFindPage}>戻る</BaseButton>
      <BaseButton onClick={onClickCreateEquipment}>登録</BaseButton>
    </Box>
  );
};