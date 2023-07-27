import { Box, Divider, HStack, Heading, Input, Table, TableContainer, Text, Tbody, Td, Th, Thead, Tr } from "@chakra-ui/react";
import axios from "axios";
import { ChangeEvent, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { BaseButton } from "./atoms/BaseButton";

export type Equipment = {
  equipmentId: number;
  name: string;
  number: string;
  location: string;
  checkPlanId: number;
  checkType: string;
  deadline: string;
};

export const FindEquipment = () => {
  const [name, setName] = useState("");
  const [number, setNumber] = useState("");
  const [location, setLocation] = useState("");
  const [deadline, setDeadline] = useState("");
  const [equipments, setEquipments] = useState<Array<Equipment>>([]);

  const navigate = useNavigate();

  // 設備登録画面に遷移する
  const onClickCreatePage = () => navigate("/create");

  // 入力した内容を設備情報の各項目に渡す
  const onChangeName = (e: ChangeEvent<HTMLInputElement>) => setName(e.target.value);
  const onChangeNumber = (e: ChangeEvent<HTMLInputElement>) => setNumber(e.target.value);
  const onChangeLocation = (e: ChangeEvent<HTMLInputElement>) => setLocation(e.target.value);
  const onChangeDeadline = (e: ChangeEvent<HTMLInputElement>) => setDeadline(e.target.value);

  // Spring BootのAPIを叩いて、前段で入力した条件に合致する設備情報を取得する。
  const onClickFindEquipment = () => {
    axios.get<Array<Equipment>>(`http://localhost:8080/equipments?name=${name}&number=${number}&location=${location}&deadline=${deadline}`)
      .then((res) => setEquipments(res.data));
  };

  // １ヶ月後の日付をyyyy-mm-dd形式に変換する
  const formatOneMonthAhead = (dt: Date) => {
    var y = dt.getFullYear();
    var m = ('00' + (dt.getMonth() + 2)).slice(-2);
    var d = ('00' + dt.getDate()).slice(-2);
    return (y + '-' + m + '-' + d);
  };

  return (
    <Box padding={5}>
      <HStack spacing={10}>
        <Heading>設備検索</Heading>
        <BaseButton onClick={onClickCreatePage}>新規設備登録</BaseButton>
      </HStack>
      <br />
      <br />
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
      <br />
      <BaseButton onClick={onClickFindEquipment}>検索</BaseButton>
      <br />
      <br />
      <br />
      <Heading size='lg'>検索結果</Heading>
      <Divider my={3} />
      <TableContainer>
        <Table variant='simple'>
          <Thead>
            <Tr>
              <Th>設備名称</Th>
              <Th>設備番号</Th>
              <Th>設置場所</Th>
              <Th>点検種別</Th>
              <Th>点検期限</Th>
            </Tr>
          </Thead>
          <Tbody>
            {equipments?.map((equipment) => (
              <Tr key={equipment.checkPlanId}>
                <Td color={"blue"}>
                  <Link to={`/update/${equipment.equipmentId}`} state={{ id: equipment.equipmentId }}>{equipment.name}</Link>
                </Td>
                <Td >{equipment.number}</Td>
                <Td>{equipment.location}</Td>
                <Td>{equipment.checkType}</Td>
                <Td style={{ color: formatOneMonthAhead(new Date()) >= equipment.deadline ? "red" : "black" }}>
                  {equipment.deadline}
                </Td>
              </Tr>
            ))}
          </Tbody>
        </Table>
      </TableContainer>
    </Box>
  );
};