import { Box, Divider, HStack, Heading, Input } from "@chakra-ui/react";
import { ChangeEvent, memo, useState } from "react";

type Props = {
  onEquipmentSearch: (name: string, number: string, location: string, deadline: string) => void;
};

export const SearchInput = memo((props: Props) => {
  const { onEquipmentSearch } = props;

  const [inputName, setInputName] = useState("");
  const [inputNumber, setInputNumber] = useState("");
  const [inputLocation, setInputLocation] = useState("");
  const [inputDeadline, setInputDeadline] = useState("");

  // 入力された内容を受け取る
  const onChangeName = (e: ChangeEvent<HTMLInputElement>) => setInputName(e.target.value);
  const onChangeNumber = (e: ChangeEvent<HTMLInputElement>) => setInputNumber(e.target.value);
  const onChangeLocation = (e: ChangeEvent<HTMLInputElement>) => setInputLocation(e.target.value);
  const onChangeDeadline = (e: ChangeEvent<HTMLInputElement>) => setInputDeadline(e.target.value);

  console.log("SearchInputがレンダリングされました");

  // 入力された内容を親コンポーネントに渡す
  onEquipmentSearch(inputName, inputNumber, inputLocation, inputDeadline);

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
});