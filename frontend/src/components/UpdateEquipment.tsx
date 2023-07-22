import { Box, Divider, FormControl, FormLabel, HStack, Heading, Input, Table, TableContainer, Tbody, Td, Th, Thead, Tr, useDisclosure } from "@chakra-ui/react";
import { useNavigate, useParams } from "react-router-dom";
import { BaseButton } from "./atoms/BaseButton";
import { FC, memo, useCallback, useEffect, useState } from "react";
import { Equipment } from "./FindEquipment";
import axios from "axios";
import { useSelectPlan } from "../hooks/useSelectPlan";
import { UpdatePlanModal } from "./organisms/UpdatePlanModal";
import { CreatePlanModal } from "./organisms/CreatePlanModal";
import { UpdateEquipmentModal } from "./organisms/UpdateEquipmentModal";

export type Plan = {
  checkPlanId: number;
  equipmentId: number;
  checkType: string;
  period: string;
  deadline: string;
};

export const UpdateEquipment: FC = memo(() => {
  const { onSelectPlan, selectedPlan } = useSelectPlan();
  const [updateEquipment, setUpdateEquipment] = useState<Equipment | null>(null);

  const [updatePlans, setUpdatePlans] = useState<Array<Plan>>([]);

  const [updateEquiipmentModalOpen, setUpdateEquipmentModalOpen] = useState(false);
  const [createPlanModalOpen, setCreatePlanModalOpen] = useState(false);
  const [updatePlanModalOpen, setUpdatePlanModalOpen] = useState(false);

  const [message, setMessage] = useState("");

  const openUpdateEquipmentModal = () => setUpdateEquipmentModalOpen(true);
  const closeUpdateEquipmentModal = () => setUpdateEquipmentModalOpen(false);
  const openCreatePlanModal = () => setCreatePlanModalOpen(true);
  const closeCreatePlanModal = () => setCreatePlanModalOpen(false);
  const openUpdatePlanModal = () => setUpdatePlanModalOpen(true);
  const closeUpdatePlanModal = () => setUpdatePlanModalOpen(false);

  const { id } = useParams();

  // レンダリング確認用
  console.log("レンダリングされました");

  // 画面更新
  const onClickReload = () => {
    window.location.reload();
  };

  // Spring BootのAPIを叩いて指定した設備IDの設備情報を取得する
  useEffect(() => {
    axios.get<Equipment>(`http://localhost:8080/equipments/${id}`).then((res) => setUpdateEquipment(res.data));
  }, [id]);

  // Spring BootのAPIを叩いて指定した設備IDと紐づく点検計画を取得する
  useEffect(() => {
    axios.get<Array<Plan>>(`http://localhost:8080/equipments/${id}/plans`).then((res) => setUpdatePlans(res.data));
  }, [id]);

  // useSelectPlanのカスタムフック内のonSelectPlan関数で点検計画を特定しモーダルを表示する
  const onClickUpdatePlanModal = useCallback((checkPlanId: number) => {
    onSelectPlan({ checkPlanId: checkPlanId, plans: updatePlans, openUpdatePlanModal });
  }, [updatePlans, onSelectPlan, openUpdatePlanModal]);

  // Spring BootのAPIを叩いて指定したIDの点検計画を削除する
  const onClickDeletePlan = (checkPlanId: number) => {
    alert("点検計画を削除しますか？");
    axios.delete(`http://localhost:8080/plans/${checkPlanId}`).then((res) => setMessage(res.data));
    alert(message);
    axios.get<Array<Plan>>(`http://localhost:8080/equipments/${id}/plans`).then((res) => setUpdatePlans(res.data));
  };

  const navigate = useNavigate();

  // Spring BootのAPIを叩いて指定した設備IDの設備情報と点検計画を削除する。その後設備検索画面に遷移する。
  const onClickDeleteEquipment = () => {
    alert("この設備と点検計画を削除しますか？");
    axios.delete(`http://localhost:8080/equipments/${id}/plans`);
    axios.delete(`http://localhost:8080/equipments/${id}`);
    alert("設備検索画面に戻ります");
    navigate("/find")
  };

  // 設備検索画面に遷移
  const onClickBackFindPage = () => navigate("/find");

  return (
    <Box padding={5}>
      <HStack spacing={10}>
        <Heading>設備詳細</Heading>
        <BaseButton onClick={onClickReload}>画面更新</BaseButton>
      </HStack>
      <br />
      <br />
      <HStack spacing={10}>
        <Heading size={"md"}>設備情報</Heading>
        <BaseButton onClick={openUpdateEquipmentModal}>設備情報修正</BaseButton>
        <UpdateEquipmentModal updateEquipment={updateEquipment} isOpen={updateEquiipmentModalOpen} onClose={closeUpdateEquipmentModal} />
      </HStack>
      <Divider my={3} />
      <HStack spacing={10}>
        <Box>
          <FormControl>
            <FormLabel>設備名称</FormLabel>
            <Input value={updateEquipment?.name} width={"400px"} placeholder="設備名称" />
          </FormControl>
        </Box>
        <Box>
          <FormControl>
            <FormLabel>設備番号</FormLabel>
            <Input value={updateEquipment?.number} width={"400px"} placeholder="設備番号" />
          </FormControl>
        </Box>
        <Box>
          <FormControl>
            <FormLabel>設置場所</FormLabel>
            <Input value={updateEquipment?.location} width={"400px"} placeholder="設置場所" />
          </FormControl>
        </Box>
      </HStack>
      <br />
      <br />
      <HStack spacing={10}>
        <Heading size={"md"}>点検計画</Heading>
        <BaseButton onClick={openCreatePlanModal}>点検計画追加</BaseButton>
        <CreatePlanModal isOpen={createPlanModalOpen} onClose={closeCreatePlanModal} />
      </HStack>
      <Divider my={3} />
      <TableContainer width={900}>
        <Table variant='simple'>
          <Thead>
            <Tr>
              <Th width={250}>点検種別</Th>
              <Th width={250}>点検周期</Th>
              <Th width={200}>点検期限</Th>
              <Th></Th>
            </Tr>
          </Thead>
          <Tbody>
            {updatePlans.map((plan) => (
              <Tr key={plan.checkPlanId}>
                <Td >{plan.checkType}</Td>
                <Td>{plan.period}</Td>
                <Td>{plan.deadline}</Td>
                <Td>
                  <HStack>
                    <BaseButton onClick={() => onClickUpdatePlanModal(plan.checkPlanId)}>修正</BaseButton>
                    <BaseButton onClick={() => onClickDeletePlan(plan.checkPlanId)}>削除</BaseButton>
                  </HStack>
                </Td>
              </Tr>
            ))}
          </Tbody>
        </Table>
      </TableContainer>
      <UpdatePlanModal selectedPlan={selectedPlan} isOpen={updatePlanModalOpen} onClose={closeUpdatePlanModal} />
      <br />
      <br />
      <HStack>
        <BaseButton onClick={onClickBackFindPage}>戻る</BaseButton>
        <BaseButton onClick={onClickDeleteEquipment}>削除</BaseButton>
      </HStack>
    </Box>
  );
});