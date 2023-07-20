import { Divider, FormControl, FormLabel, HStack, Heading, Input, Modal, ModalBody, ModalCloseButton, ModalContent, ModalFocusScope, ModalFooter, ModalHeader, ModalOverlay, Stack, Table, TableContainer, Tbody, Td, Th, Thead, Tr, useDisclosure } from "@chakra-ui/react"
import { useNavigate, useParams } from "react-router-dom";
import { BaseButton } from "./atoms/BaseButton";
import { ChangeEvent, useCallback, useEffect, useState } from "react";
import { Equipment } from "./FindEquipment";
import axios from "axios";
import { Plan } from "./EquipmentDetail";
import { useSelectPlan } from "../hooks/useSelectPlan";

export const UpdateEquipment = () => {
  const { isOpen, onOpen, onClose } = useDisclosure();
  const { onSelectPlan, selectedPlan } = useSelectPlan();

  const [updateName, setUpdateName] = useState("");
  const [updateNumber, setUpdateNumber] = useState("");
  const [updateLocation, setUpdateLocation] = useState("");
  const [updateEquipment, setUpdateEquipment] = useState<Equipment | null>(null);
  const [updateMessage, setUpdateMessage] = useState("")

  const [createCheckType, setCreateCheckType] = useState("");
  const [createPeriod, setCreatePeriod] = useState("");
  const [createDeadline, setCreateDeadline] = useState("");

  const [updatePlans, setUpdatePlans] = useState<Array<Plan>>([]);
  const [updateCheckType, setUpdateCheckType] = useState("");
  const [updatePeriod, setUpdatePeriod] = useState("");
  const [updateDeadline, setUpdateDeadline] = useState("");

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

  const onChangeCreateCheckType = (e: ChangeEvent<HTMLInputElement>) => setCreateCheckType(e.target.value)
  const onChangeCreatePeriod = (e: ChangeEvent<HTMLInputElement>) => setCreatePeriod(e.target.value)
  const onChangeCreateDeadline = (e: ChangeEvent<HTMLInputElement>) => setCreateDeadline(e.target.value)

  const onClickCreatePlan = () => {
    alert("点検計画を追加しますか？")
    axios.post(`http://localhost:8080/equipments/${id}/plans`,
    { "checkType": createCheckType, "period": createPeriod, "deadline": createDeadline });
    closeCreatePlanModal();
  }

  useEffect(() => {
    axios.get<Array<Plan>>(`http://localhost:8080/equipments/${id}/plans`).then((res) => setUpdatePlans(res.data))
  }, [id, onClickCreatePlan])

  const onClickUpdatePlanModal = useCallback((checkPlanId: number) => {
    onSelectPlan({ checkPlanId: checkPlanId, plans: updatePlans, openUpdatePlanModal })
  }, [updatePlans, onSelectPlan, openUpdatePlanModal]);

  useEffect(() => {
    setUpdateCheckType(selectedPlan?.checkType ?? "");
    setUpdatePeriod(selectedPlan?.period ?? "");
    setUpdateDeadline(selectedPlan?.deadline ?? "");
  }, [selectedPlan])

  const onChangeUpdateCheckType = (e: ChangeEvent<HTMLInputElement>) => setUpdateCheckType(e.target.value);
  const onChangeUpdatePeriod = (e: ChangeEvent<HTMLInputElement>) => setUpdatePeriod(e.target.value);
  const onChangeUpdateDeadline = (e: ChangeEvent<HTMLInputElement>) => setUpdateDeadline(e.target.value);

  const navigate = useNavigate();

  const onClickUpdatePlan = () => {
    axios.patch(`http://localhost:8080/plans/${selectedPlan?.checkPlanId}`,
      { "checkType": updateCheckType, "period": updatePeriod, "deadline": updateDeadline });
    alert("点検計画を修正します");
    closeUpdatePlanModal();
    axios.get<Array<Plan>>(`http://localhost:8080/equipments/${id}/plans`).then((res) => setUpdatePlans(res.data))
  }

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
      <Modal isOpen={createPlanModalOpen} onClose={closeCreatePlanModal} size={"xl"}>
        <ModalOverlay />
        <ModalContent pb={6}>
          <ModalHeader>点検計画追加</ModalHeader>
          <ModalCloseButton />
          <ModalBody mx={4}>
            <HStack spacing={4}>
              <FormControl>
                <FormLabel>点検種別</FormLabel>
                <Input placeholder="点検種別" onChange={onChangeCreateCheckType} />
              </FormControl>
              <FormControl>
                <FormLabel>点検周期</FormLabel>
                <Input placeholder="点検周期" onChange={onChangeCreatePeriod} />
              </FormControl>
              <FormControl>
                <FormLabel>点検期限</FormLabel>
                <Input placeholder="点検期限" onChange={onChangeCreateDeadline} />
              </FormControl>
            </HStack>
          </ModalBody>
          <ModalFooter>
            <BaseButton onClick={onClickCreatePlan}>点検計画追加</BaseButton>
          </ModalFooter>
        </ModalContent>
      </Modal>
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
      <Modal isOpen={updatePlanModalOpen} onClose={closeUpdatePlanModal} size={"xl"}>
        <ModalOverlay />
        <ModalContent pb={6}>
          <ModalHeader>点検計画修正</ModalHeader>
          <ModalCloseButton />
          <ModalBody mx={4}>
            <HStack spacing={4}>
              <FormControl>
                <FormLabel>点検種別</FormLabel>
                <Input value={updateCheckType} onChange={onChangeUpdateCheckType} />
              </FormControl>
              <FormControl>
                <FormLabel>点検周期</FormLabel>
                <Input value={updatePeriod} onChange={onChangeUpdatePeriod} />
              </FormControl>
              <FormControl>
                <FormLabel>点検期限</FormLabel>
                <Input value={updateDeadline} onChange={onChangeUpdateDeadline} />
              </FormControl>
            </HStack>
          </ModalBody>
          <ModalFooter>
            <BaseButton onClick={onClickUpdatePlan}>点検計画修正</BaseButton>
          </ModalFooter>
        </ModalContent>
      </Modal>
      <br />
      <BaseButton onClick={onClickBackDetailPage}>戻る</BaseButton>
      <BaseButton onClick={onClickUpdate}>更新</BaseButton>
    </div>
  )
}