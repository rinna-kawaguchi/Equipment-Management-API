import { Box, Divider, FormControl, FormLabel, HStack, Heading, Input, Table, TableContainer, Tbody, Td, Th, Thead, Tr } from "@chakra-ui/react";
import { useNavigate, useParams } from "react-router-dom";
import { BaseButton } from "./atoms/BaseButton";
import { FC, memo, useCallback, useEffect, useState } from "react";
import { Equipment } from "./FindEquipment";
import axios from "axios";
import { useSelectPlan } from "../hooks/useSelectPlan";
import { useSelectHistory } from "../hooks/useSelectHistory";
import { UpdatePlanModal } from "./organisms/UpdatePlanModal";
import { CreatePlanModal } from "./organisms/CreatePlanModal";
import { UpdateEquipmentModal } from "./organisms/UpdateEquipmentModal";
import { CreateHistoryModal } from "./organisms/CreateHistoryModal";
import { UpdateHistoryModal } from "./organisms/UpdateHistoryModal";
import { useMessage } from "../hooks/useMessage";
import { DeletePlanConfirmModal } from "./organisms/DeletePlanConfirmModal";

export type Plan = {
  checkPlanId: number;
  equipmentId: number;
  checkType: string;
  period: string;
  deadline: string;
};

export type History = {
  checkHistoryId: number;
  equipmentId: number;
  implementationDate: string;
  checkType: string;
  result: string;
};

export const EquipmentDetail: FC = memo(() => {
  const { onSelectPlan, selectedPlan } = useSelectPlan();
  const { onSelectHistory, selectedHistory } = useSelectHistory();
  const { showMessage } = useMessage();
  const { id } = useParams();

  const [updateEquipment, setUpdateEquipment] = useState<Equipment | null>(null);

  const [updatePlans, setUpdatePlans] = useState<Array<Plan>>([]);

  const [updateHistories, setUpdateHistories] = useState<Array<History>>([]);

  const [updateEquiipmentModalOpen, setUpdateEquipmentModalOpen] = useState(false);

  const [createPlanModalOpen, setCreatePlanModalOpen] = useState(false);
  const [updatePlanModalOpen, setUpdatePlanModalOpen] = useState(false);
  const [deletePlanModalOpen, setDeletePlanModalOpen] = useState(false);

  const [createHistoryModalOpen, setCreateHistoryModalOpen] = useState(false);
  const [updateHistoryModalOpen, setUpdateHistoryModalOpen] = useState(false);

  const openUpdateEquipmentModal = () => setUpdateEquipmentModalOpen(true);
  const closeUpdateEquipmentModal = () => setUpdateEquipmentModalOpen(false);

  const openCreatePlanModal = () => setCreatePlanModalOpen(true);
  const closeCreatePlanModal = () => setCreatePlanModalOpen(false);
  const openUpdatePlanModal = () => setUpdatePlanModalOpen(true);
  const closeUpdatePlanModal = () => setUpdatePlanModalOpen(false);
  const openDeletePlanModal = () => setDeletePlanModalOpen(true);
  const closeDeletePlanModal = () => setDeletePlanModalOpen(false);

  const openCreateHistoryModal = () => setCreateHistoryModalOpen(true);
  const closeCreateHistoryModal = () => setCreateHistoryModalOpen(false);
  const openUpdateHistoryModal = () => setUpdateHistoryModalOpen(true);
  const closeUpdateHistoryModal = () => setUpdateHistoryModalOpen(false);

  // レンダリング確認用
  console.log("レンダリングされました");

  // Spring BootのAPIを叩いて指定した設備IDの設備情報を取得する
  useEffect(() => {
    axios.get<Equipment>(`http://localhost:8080/equipments/${id}`).then((res) => setUpdateEquipment(res.data));
  }, [id]);

  // UpdateEquipmentModalで更新処理が実行されたら、更新後の設備情報を反映する。
  const handleEquipmentUpdate = (updatedEquipments: Equipment) => {
    setUpdateEquipment(updatedEquipments);
  };

  // Spring BootのAPIを叩いて指定した設備IDと紐づく点検計画を取得する
  useEffect(() => {
    axios.get<Array<Plan>>(`http://localhost:8080/equipments/${id}/plans`).then((res) => setUpdatePlans(res.data));
  }, [id]);

  // CreatePlanModalで点検計画が追加されたら、追加後の点検計画を反映する。
  const handlePlanCreate = (createdPlans: Array<Plan>) => {
    setUpdatePlans(createdPlans);
  };

  // useSelectHistoryのカスタムフック内のonSelectPlan関数で点検計画を特定しモーダルを表示する
  const onClickUpdatePlanModal = useCallback((checkPlanId: number) => {
    onSelectPlan({ checkPlanId: checkPlanId, plans: updatePlans });
    openUpdatePlanModal();
  }, [updatePlans, onSelectPlan, openUpdatePlanModal]);

  // UpdateHistoryModalで更新処理が実行されたら、更新後の点検計画を反映する。
  const handleHistoryUpdate = (updatedHistories: Array<History>) => {
    setUpdateHistories(updatedHistories);
  };

  // Spring BootのAPIを叩いて指定したIDの点検計画を削除する
  const onClickDeletePlan = useCallback((checkPlanId: number) => {
    onSelectPlan({ checkPlanId: checkPlanId, plans: updatePlans });
    openDeletePlanModal();
  }, [updatePlans, onSelectPlan, openDeletePlanModal]);

  // DeletePlanConfirmModalで削除処理が実行されたら、削除後の点検履歴を反映する。
  const handlePlanDelete = (deletedPlans: Array<Plan>) => {
    setUpdatePlans(deletedPlans);
  };

  // Spring BootのAPIを叩いて指定した設備IDと紐づく点検履歴を取得する
  useEffect(() => {
    axios.get<Array<History>>(`http://localhost:8080/equipments/${id}/histories`)
      .then((res) => setUpdateHistories(res.data));
  }, [id]);

  // CreatePlanModalで点検計画が追加されたら、追加後の点検計画を反映する。
  const handleHistoryCreate = (createdHistories: Array<History>) => {
    setUpdateHistories(createdHistories);
  };

  // useSelectHistoryのカスタムフック内のonSelectHistory関数で点検履歴を特定しモーダルを表示する
  const onClickUpdateHistoryModal = useCallback((checkHistoryId: number) => {
    onSelectHistory({ checkHistoryId: checkHistoryId, histories: updateHistories, openUpdateHistoryModal });
  }, [updateHistories, onSelectHistory, openUpdateHistoryModal]);

  // UpdateHistoryModalで更新処理が実行されたら、更新後の点検履歴を反映する。
  const handlePlanUpdate = (updatedPlans: Array<Plan>) => {
    setUpdatePlans(updatedPlans);
  };

  // Spring BootのAPIを叩いて指定したIDの点検履歴を削除する
  const onClickDeleteHistory = async (checkHistoryId: number) => {
    alert("点検計画を削除しますか？");
    let res = await axios.delete(`http://localhost:8080/histories/${checkHistoryId}`)
      .catch(() => showMessage({ title: "点検履歴の削除に失敗しました。", status: "error" }));
    if (res) {
      const response: string = res.data.message;
      showMessage({ title: response, status: "success" });
    }
    axios.get<Array<History>>(`http://localhost:8080/equipments/${id}/histories`)
      .then((res) => setUpdateHistories(res.data));
  };

  const navigate = useNavigate();

  // Spring BootのAPIを叩いて指定した設備IDの設備情報、点検計画、点検履歴を削除する。その後設備検索画面に遷移する。
  const onClickDeleteEquipment = async () => {
    alert("この設備と点検計画を削除しますか？");
    let res = await axios.delete(`http://localhost:8080/equipments/${id}/plans`)
      .then(() => axios.delete(`http://localhost:8080/equipments/${id}/histories`))
      .then(() => axios.delete(`http://localhost:8080/equipments/${id}`))
      .catch(() => showMessage({
        title: "設備情報、点検計画、点検履歴の削除に失敗しました。", status: "error"
      }));
    if (res) {
      const response: string = res.data.message;
      showMessage({ title: `${response}。設備検索画面に戻ります。`, status: "success" });
    }
    navigate("/find");
  };

  // 設備検索画面に遷移
  const onClickBackFindPage = () => navigate("/find");

  return (
    <Box padding={5}>
      <Heading>設備詳細</Heading>
      <br />
      <br />
      <HStack spacing={10}>
        <Heading size={"md"}>設備情報</Heading>
        <BaseButton onClick={openUpdateEquipmentModal}>設備情報修正</BaseButton>
        <UpdateEquipmentModal updateEquipment={updateEquipment} isOpen={updateEquiipmentModalOpen} onClose={closeUpdateEquipmentModal} onEquipmentsUpdate={handleEquipmentUpdate} />
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
        <CreatePlanModal isOpen={createPlanModalOpen} onClose={closeCreatePlanModal}
          onPlanCreate={handlePlanCreate} />
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
      <UpdatePlanModal selectedPlan={selectedPlan} isOpen={updatePlanModalOpen}
        onClose={closeUpdatePlanModal} onPlanUpdate={handlePlanUpdate} />
      <DeletePlanConfirmModal selectedPlan={selectedPlan} isOpen={deletePlanModalOpen}
        onClose={closeDeletePlanModal} onPlanDelete={handlePlanDelete} />
      <br />
      <br />
      <HStack spacing={10}>
        <Heading size={"md"}>点検履歴</Heading>
        <BaseButton onClick={openCreateHistoryModal}>点検履歴追加</BaseButton>
        <CreateHistoryModal isOpen={createHistoryModalOpen} onClose={closeCreateHistoryModal}
          onHistoryCreate={handleHistoryCreate} />
      </HStack>
      <Divider my={3} />
      <TableContainer width={900}>
        <Table variant='simple'>
          <Thead>
            <Tr>
              <Th width={250}>実施日</Th>
              <Th width={250}>点検種別</Th>
              <Th width={200}>点検結果</Th>
              <Th></Th>
            </Tr>
          </Thead>
          <Tbody>
            {updateHistories.map((history) => (
              <Tr key={history.checkHistoryId}>
                <Td>{history.implementationDate}</Td>
                <Td >{history.checkType}</Td>
                <Td>{history.result}</Td>
                <Td>
                  <HStack>
                    <BaseButton onClick={() => onClickUpdateHistoryModal(history.checkHistoryId)}>修正</BaseButton>
                    <BaseButton onClick={() => onClickDeleteHistory(history.checkHistoryId)}>削除</BaseButton>
                  </HStack>
                </Td>
              </Tr>
            ))}
          </Tbody>
        </Table>
      </TableContainer>
      <UpdateHistoryModal selectedHistory={selectedHistory} isOpen={updateHistoryModalOpen}
        onClose={closeUpdateHistoryModal} onHistoryUpdate={handleHistoryUpdate} />
      <br />
      <br />
      <HStack>
        <BaseButton onClick={onClickBackFindPage}>戻る</BaseButton>
        <BaseButton onClick={onClickDeleteEquipment}>削除</BaseButton>
      </HStack>
    </Box>
  );
});