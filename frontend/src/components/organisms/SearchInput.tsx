import { Box, Divider, HStack, Heading, Input } from "@chakra-ui/react";
import { BaseButton } from "../atoms/BaseButton";
import { ChangeEvent, useState } from "react";
import axios from "axios";
import { Equipment } from "../FindEquipment";

type Props = {
  onEquipmentSearch: (name: string, number: string, location: string, deadline: string) => void;
}

export const SearchInput = (props: Props) => {
  const { onEquipmentSearch } = props;
  const [name, setName] = useState("");
  const [number, setNumber] = useState("");
  const [location, setLocation] = useState("");
  const [deadline, setDeadline] = useState("");

  // 入力された内容を受け取る
  const onChangeName = (e: ChangeEvent<HTMLInputElement>) => setName(e.target.value);
  const onChangeNumber = (e: ChangeEvent<HTMLInputElement>) => setNumber(e.target.value);
  const onChangeLocation = (e: ChangeEvent<HTMLInputElement>) => setLocation(e.target.value);
  const onChangeDeadline = (e: ChangeEvent<HTMLInputElement>) => setDeadline(e.target.value);

  // 入力された内容を親コンポーネントに渡す
  onEquipmentSearch(name, number, location, deadline);

  // 検索条件入力欄の表示
  return (
    <>
      <Heading size='lg'>検索条件入力</Heading>
      <Divider my={3} />
      <HStack spacing={4} >
        <Box>
          <p>設備名称</p>
          <Input width={"400px"} placeholder="ポンプ" onChange={onChangeName} />
        </Box>
        <Box>
          <p>設備番号
          </p>
          <Input width={"400px"} placeholder="C001" onChange={onChangeNumber} />
        </Box>
        <Box>
          <p>設置場所</p>
          <Input width={"400px"} placeholder="Area1" onChange={onChangeLocation} />
        </Box>
        <Box>
          <p>点検期限（点検期限が指定した日付以前の設備を検索）</p>
          <Input width={"400px"} placeholder="2023-12-31（yyyy-mm-ddで入力してください）"
            onChange={onChangeDeadline} />
        </Box>
      </HStack>
    </>
  );
};