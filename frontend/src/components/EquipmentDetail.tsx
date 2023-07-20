import { Divider, HStack, Heading, Input, Table, TableContainer, Tbody, Td, Th, Thead, Tr } from "@chakra-ui/react"
import { BaseButton } from "./atoms/BaseButton"
import { useNavigate, useParams } from "react-router-dom";
import { FC, useEffect, useState } from "react";
import { Equipment } from "./FindEquipment";
import axios from "axios";

export type Plan = {
  checkPlanId: number;
  equipmentId: number;
  checkType: string;
  period: string;
  deadline: string;
}

export const EquipmentDetail: FC = () => {
  const [selectedEquipment, setSelectedEquipment] = useState<Equipment | null>(null);
  const [deleteMessage, setDeleteMessage] = useState("")
  const [selectedEquipmentPlans, setSelectedEquipmentPlans] = useState<Array<Plan>>([]);

  const { id } = useParams();

  const navigate = useNavigate();

  const onClickBackFindPage = () => navigate("/find")
  const onClickUpdatePage = () => navigate(`/update/${id}`, { state: { id: id } })

  const onClickDelete = () => {
    alert("この設備を削除しますか？");
    axios.delete<string>(`http://localhost:8080/equipments/${id}`).then((res) => setDeleteMessage(res.data))
    alert(deleteMessage);
  }

  useEffect(() => {
    axios.get<Equipment>(`http://localhost:8080/equipments/${id}`).then((res) => setSelectedEquipment(res.data))
  }, [id])

  useEffect(() => {
    axios.get<Array<Plan>>(`http://localhost:8080/equipments/${id}/plans`).then((res) => setSelectedEquipmentPlans(res.data))
  }, [id])

  return (
    <div>
      <Heading>設備情報詳細</Heading>
      <br />
      <Heading size={"md"}>設備情報詳細</Heading>
      <Divider my={3} />
      <HStack>
        <p>設備名称</p>
        <Input value={selectedEquipment?.name} width={"400px"} backgroundColor={"gray.100"} placeholder="設備名称" />
        <p>設備番号</p>
        <Input value={selectedEquipment?.number} width={"400px"} backgroundColor={"gray.100"} placeholder="設備番号" />
        <p>設置場所</p>
        <Input value={selectedEquipment?.location} width={"400px"} backgroundColor={"gray.100"} placeholder="設置場所" />
      </HStack>
      <br />
      <br />
      <Heading size={"md"}>点検計画</Heading>
      <Divider my={3} />
      <TableContainer>
        <Table variant='simple'>
          <Thead>
            <Tr>
              <Th>点検種別</Th>
              <Th>点検周期</Th>
              <Th>点検期限</Th>
            </Tr>
          </Thead>
          <Tbody>
            {selectedEquipmentPlans.map((plan) => (
              <Tr key={plan.checkPlanId}>
                <Td>{plan.checkType}</Td>
                <Td>{plan.period}</Td>
                <Td>{plan.deadline}</Td>
              </Tr>
            ))}
          </Tbody>
        </Table>
      </TableContainer>
      <br />
      <BaseButton onClick={onClickBackFindPage}>戻る</BaseButton>
      <BaseButton onClick={onClickUpdatePage}>修正</BaseButton>
      <BaseButton onClick={onClickDelete}>削除</BaseButton>
    </div>
  )
}