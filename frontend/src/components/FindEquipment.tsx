import { Box, Button, HStack, Heading, Input, Table, TableContainer, Tbody, Td, Th, Thead, Tr } from "@chakra-ui/react";
import axios from "axios";
import { ChangeEvent, useState } from "react"

type Equipment = {
  equipmentId: number;
  name: string;
  number: string;
  location: string;
}

export const FindEquipment = () => {
  const [name, setName] = useState("");
  const [number, setNumber] = useState("");
  const [location, setLocation] = useState("");
  const [equipments, setEquipments] = useState<Equipment[] | null>(null);

  const onChangeName = (e: ChangeEvent<HTMLInputElement>) => setName(e.target.value)
  const onChangeNumber = (e: ChangeEvent<HTMLInputElement>) => setNumber(e.target.value)
  const onChangeLocation = (e: ChangeEvent<HTMLInputElement>) => setLocation(e.target.value)

  const onClickFindEquipment = () => {
    axios.get<Array<Equipment>>(`http://localhost:8080/equipments?name=${name}&number=${number}&location=${location}`)
      .then((res) => setEquipments(res.data))
  }

  return (
    <Box padding={"20px"}>
      <Heading>設備検索</Heading>
      <br />
      <Heading size='lg'>検索条件入力</Heading>
      <br />
      <HStack>
        <Box>
          <p>設備名称</p>
          <Input width={"400px"} placeholder="設備名称" onChange={onChangeName} />
        </Box>
        <Box>
          <p>設備番号
          </p>
          <Input width={"400px"} placeholder="設備番号" onChange={onChangeNumber} />
        </Box>
        <Box>
          <p>設置場所</p>
          <Input width={"400px"} placeholder="設置場所" onChange={onChangeLocation} />
        </Box>
      </HStack>
      <br />
      <Button colorScheme="teal" variant={"outline"} onClick={onClickFindEquipment}>設備検索</Button>
      <br />
      <br />
      <br />
      <Heading size='lg'>検索結果</Heading>
      <TableContainer>
        <Table variant='simple'>
          <Thead>
            <Tr>
              <Th>設備名称</Th>
              <Th>設備番号</Th>
              <Th>設置場所</Th>
            </Tr>
          </Thead>
          {equipments?.map((equipment) => (
            <Tbody>
              <Tr>
                <Td>{equipment.name}</Td>
                <Td>{equipment.number}</Td>
                <Td>{equipment.location}</Td>
              </Tr>
            </Tbody>
          ))}
        </Table>
      </TableContainer>
    </Box>
  )
}