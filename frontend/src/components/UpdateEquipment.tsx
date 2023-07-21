import { Divider, HStack, Heading, Input, Table, TableContainer, Tbody, Td, Th, Thead, Tr, useDisclosure } from "@chakra-ui/react"
import { useNavigate, useParams } from "react-router-dom";
import { BaseButton } from "./atoms/BaseButton";
import { ChangeEvent, useCallback, useEffect, useState } from "react";
import { Equipment } from "./FindEquipment";
import axios from "axios";
import { Plan } from "./EquipmentDetail";
import { useSelectPlan } from "../hooks/useSelectPlan";
import { UpdatePlanModal } from "./organisms/UpdatePlanModal";
import { CreatePlanModal } from "./organisms/CreatePlanModal";

export const UpdateEquipment = () => {
  const { isOpen, onOpen, onClose } = useDisclosure();
  const { onSelectPlan, selectedPlan } = useSelectPlan();

  const [updateName, setUpdateName] = useState("");
  const [updateNumber, setUpdateNumber] = useState("");
  const [updateLocation, setUpdateLocation] = useState("");
  const [updateEquipment, setUpdateEquipment] = useState<Equipment | null>(null);
  const [updateMessage, setUpdateMessage] = useState("")

  const [updatePlans, setUpdatePlans] = useState<Array<Plan>>([]);

  const [createPlanModalOpen, setCreatePlanModalOpen] = useState(false);
  const [updatePlanModalOpen, setUpdatePlanModalOpen] = useState(false);

  const openCreatePlanModal = () => setCreatePlanModalOpen(true);
  const closeCreatePlanModal = () => setCreatePlanModalOpen(false);
  const openUpdatePlanModal = () => setUpdatePlanModalOpen(true);
  const closeUpdatePlanModal = () => setUpdatePlanModalOpen(false);

  const { id } = useParams();

  useEffect(() => {
    axios.get<Equipment>(`http://localhost:8080/equipments/${id}`).then((res) => setUpdateEquipment(res.data))
  }, [id])

  useEffect(() => {
    setUpdateName(updateEquipment?.name ?? "");
    setUpdateNumber(updateEquipment?.number ?? "");
    setUpdateLocation(updateEquipment?.location ?? "");
  }, [updateEquipment]);

  const onChangeUpdateName = (e: ChangeEvent<HTMLInputElement>) => setUpdateName(e.target.value)
  const onChangeUpdateNumber = (e: ChangeEvent<HTMLInputElement>) => setUpdateNumber(e.target.value)
  const onChangeUpdateLocation = (e: ChangeEvent<HTMLInputElement>) => setUpdateLocation(e.target.value)

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

  const onClickBackDetailPage = () => navigate(`/detail/${id}`)

  const onClickUpdate = () => {
    alert("更新しますか？")
    axios.patch(`http://localhost:8080/equipments/${id}`,
      { "name": updateName, "number": updateNumber, "location": updateLocation })
      .then((res) => setUpdateMessage(res.data.message));
    alert(updateMessage);
    navigate(`/detail/${id}`)
  }

  return (
    <div>
      <Heading>設備情報修正</Heading>
      <br />
      <Heading size={"md"}>設備情報詳細</Heading>
      <Divider my={3} />
      <HStack>
        <p>設備名称</p>
        <Input value={updateName} width={"400px"} placeholder="設備名称" onChange={onChangeUpdateName} />
        <p>設備番号</p>
        <Input value={updateNumber} width={"400px"} placeholder="設備番号" onChange={onChangeUpdateNumber} />
        <p>設置場所</p>
        <Input value={updateLocation} width={"400px"} placeholder="設置場所" onChange={onChangeUpdateLocation} />
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
      <BaseButton onClick={onClickBackDetailPage}>戻る</BaseButton>
      <BaseButton onClick={onClickUpdate}>更新</BaseButton>
    </div>
  )
}