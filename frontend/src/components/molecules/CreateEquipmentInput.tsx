import { Box, FormControl, FormLabel, HStack, Input, Text } from "@chakra-ui/react";
import { ChangeEvent, memo, useEffect, useState } from "react";

type Props = {
  onEquipmentCreate: (newName: string, newNumber: string, newLocation: string) => void;
};

export const CreateEquipmentInput = memo((props: Props) => {
  const { onEquipmentCreate } = props;

  const [newName, setNewName] = useState("");
  const [newNumber, setNewNumber] = useState("");
  const [newLocation, setNewLocation] = useState("");

  // 入力された内容を受け取る
  const onChangeNewName = (e: ChangeEvent<HTMLInputElement>) => setNewName(e.target.value);
  const onChangeNewNumber = (e: ChangeEvent<HTMLInputElement>) => setNewNumber(e.target.value);
  const onChangeNewLocation = (e: ChangeEvent<HTMLInputElement>) => setNewLocation(e.target.value);

  // 入力された内容を親コンポーネントに渡す
  useEffect(() => onEquipmentCreate(newName, newNumber, newLocation), 
  [newName, newNumber, newLocation]);

  return (
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
  );
});