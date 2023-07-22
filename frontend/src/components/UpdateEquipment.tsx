import { Box, Divider, FormControl, FormLabel, HStack, Heading, Input, Table, TableContainer, Tbody, Td, Th, Thead, Tr, useDisclosure } from "@chakra-ui/react"
import { useNavigate, useParams } from "react-router-dom";
import { BaseButton } from "./atoms/BaseButton";
import { ChangeEvent, useCallback, useEffect, useState } from "react";
import { Equipment } from "./FindEquipment";
import axios from "axios";
import { Plan } from "./EquipmentDetail";
import { useSelectPlan } from "../hooks/useSelectPlan";
import { UpdatePlanModal } from "./organisms/UpdatePlanModal";
import { CreatePlanModal } from "./organisms/CreatePlanModal";
import { UpdateEquipmentModal } from "./organisms/UpdateEquipmentModal";

export const UpdateEquipment = () => {
  const { isOpen, onOpen, onClose } = useDisclosure();
  const { onSelectPlan, selectedPlan } = useSelectPlan();
  const [updateEquipment, setUpdateEquipment] = useState<Equipment | null>(null);
  const [updateMessage, setUpdateMessage] = useState("")

  const [updatePlans, setUpdatePlans] = useState<Array<Plan>>([]);

  const [updateEquiipmentModalOpen, setUpdateEquipmentModalOpen] = useState(false);
  const [createPlanModalOpen, setCreatePlanModalOpen] = useState(false);
  const [updatePlanModalOpen, setUpdatePlanModalOpen] = useState(false);

  const openUpdateEquipmentModal = () => setUpdateEquipmentModalOpen(true);
  const closeUpdateEquipmentModal = () => setUpdateEquipmentModalOpen(false);
  const openCreatePlanModal = () => setCreatePlanModalOpen(true);
  const closeCreatePlanModal = () => setCreatePlanModalOpen(false);
  const openUpdatePlanModal = () => setUpdatePlanModalOpen(true);
  const closeUpdatePlanModal = () => setUpdatePlanModalOpen(false);

  const { id } = useParams();

  useEffect(() => {
    axios.get<Equipment>(`http://localhost:8080/equipments/${id}`).then((res) => setUpdateEquipment(res.data))
  }, [id])

  useEffect(() => {
    axios.get<Array<Plan>>(`http://localhost:8080/equipments/${id}/plans`).then((res) => setUpdatePlans(res.data))
  }, [id])

  // useSelectPlanのカスタムフック内のonSelectPlan関数で点検計画を特定しモーダルを表示する
  const onClickUpdatePlanModal = useCallback((checkPlanId: number) => {
    onSelectPlan({ checkPlanId: checkPlanId, plans: updatePlans, openUpdatePlanModal })
  }, [updatePlans, onSelectPlan, openUpdatePlanModal]);

  const navigate = useNavigate();

  const onClickDeletePlan = (checkPlanId: number) => {
    alert("点検計画を削除しますか？")
    axios.delete(`http://localhost:8080/plans/${checkPlanId}`)
    alert("点検計画を削除しました")
    axios.get<Array<Plan>>(`http://localhost:8080/equipments/${id}/plans`).then((res) => setUpdatePlans(res.data))
  };

  const onClickDeleteEquipment = () => {
    alert("この設備と点検計画を削除しますか？");
    axios.delete<string>(`http://localhost:8080/equipments/${id}`)
  }

  const onClickBackFindPage = () => navigate("/find")

  return (
    <div>
      <Heading>設備詳細</Heading>
      <br />
      <Heading size={"md"}>設備情報</Heading>
      <Divider my={3} />
      <BaseButton onClick={openUpdateEquipmentModal}>設備情報修正</BaseButton>
      <UpdateEquipmentModal updateEquipment={updateEquipment} isOpen={updateEquiipmentModalOpen} onClose={closeUpdateEquipmentModal} />
      <br />
      <br />
      <HStack spacing={10}>
        <Box>
          <FormControl>
            <FormLabel>設備名称</FormLabel>
            <Input value={updateEquipment?.name} width={"400px"} backgroundColor={"gray.100"} placeholder="設備名称" />
          </FormControl>
        </Box>
        <Box>
          <FormControl>
            <FormLabel>設備番号</FormLabel>
            <Input value={updateEquipment?.number} width={"400px"} backgroundColor={"gray.100"} placeholder="設備番号" />
          </FormControl>
        </Box>
        <Box>
          <FormControl>
            <FormLabel>設置場所</FormLabel>
            <Input value={updateEquipment?.location} width={"400px"} backgroundColor={"gray.100"} placeholder="設置場所" />
          </FormControl>
        </Box>
      </HStack>
      <br />
      <br />
      <Heading size={"md"}>点検計画</Heading>
      <Divider my={3} />
      <BaseButton onClick={openCreatePlanModal}>点検計画追加</BaseButton>
      <CreatePlanModal isOpen={createPlanModalOpen} onClose={closeCreatePlanModal} />
      <br />
      <TableContainer>
        <Table variant='simple'>
          <Thead>
            <Tr>
              <Th>点検種別</Th>
              <Th>点検周期</Th>
              <Th>点検期限</Th>
              <Th></Th>
            </Tr>
          </Thead>
          <Tbody>
            {updatePlans.map((plan) => (
              <Tr key={plan.checkPlanId}>
                <Td>{plan.checkType}</Td>
                <Td>{plan.period}</Td>
                <Td>{plan.deadline}</Td>
                <Td>
                  <BaseButton onClick={() => onClickUpdatePlanModal(plan.checkPlanId)}>修正</BaseButton>
                  <BaseButton onClick={() => onClickDeletePlan(plan.checkPlanId)}>削除</BaseButton>
                </Td>
              </Tr>
            ))}
          </Tbody>
        </Table>
      </TableContainer>
      <UpdatePlanModal selectedPlan={selectedPlan} isOpen={updatePlanModalOpen} onClose={closeUpdatePlanModal} />
      <br />
      <BaseButton onClick={onClickBackFindPage}>戻る</BaseButton>
      <BaseButton onClick={onClickDeleteEquipment}>削除</BaseButton>
    </div>
  )
}