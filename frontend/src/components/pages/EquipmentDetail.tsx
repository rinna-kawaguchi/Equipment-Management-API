import { Box, Divider, HStack, Heading } from "@chakra-ui/react";
import { useNavigate, useParams } from "react-router-dom";
import { BaseButton } from "../atoms/BaseButton";
import { FC, memo, useCallback, useEffect, useState } from "react";
import { Equipment } from "../../types/Equipment";
import axios from "axios";
import { CreatePlanModal } from "../organisms/CreatePlanModal";
import { UpdateEquipmentModal } from "../organisms/UpdateEquipmentModal";
import { CreateHistoryModal } from "../organisms/CreateHistoryModal";
import { DeleteEquipmentConfirmModal } from "../organisms/DeleteEquipmentConfirmModal";
import { Plan } from "../../types/Plan";
import { History } from "../../types/History";
import { EquipmentInformation } from "../molecules/EquipmentInformation";
import { Plans } from "../organisms/Plans";
import { Histories } from "../organisms/Histories";

export const EquipmentDetail: FC = memo(() => {
  const { id } = useParams();

  const [selectedEquipment, setSelectedEquipment] = useState<Equipment | null>(null);
  const [plans, setPlans] = useState<Array<Plan>>([]);
  const [histories, setHistories] = useState<Array<History>>([]);

  const [updateEquiipmentModalOpen, setUpdateEquipmentModalOpen] = useState(false);
  const [deleteEquiipmentModalOpen, setDeleteEquipmentModalOpen] = useState(false);
  const [createPlanModalOpen, setCreatePlanModalOpen] = useState(false);
  const [createHistoryModalOpen, setCreateHistoryModalOpen] = useState(false);

  const openUpdateEquipmentModal = () => setUpdateEquipmentModalOpen(true);
  const closeUpdateEquipmentModal = () => setUpdateEquipmentModalOpen(false);
  const openDeleteEquipmentModal = () => setDeleteEquipmentModalOpen(true);
  const closeDeleteEquipmentModal = () => setDeleteEquipmentModalOpen(false);

  const openCreatePlanModal = () => setCreatePlanModalOpen(true);
  const closeCreatePlanModal = () => setCreatePlanModalOpen(false);

  const openCreateHistoryModal = () => setCreateHistoryModalOpen(true);
  const closeCreateHistoryModal = () => setCreateHistoryModalOpen(false);

  // レンダリング確認用
  console.log("EquipmentDetailがレンダリングされました");

  // Spring BootのAPIを叩いて指定した設備IDの設備情報を取得する
  useEffect(() => {
    axios.get<Equipment>(`http://localhost:8080/equipments/${id}`).then((res) => setSelectedEquipment(res.data));
  }, [id]);

  // UpdateEquipmentModalで更新処理が実行されたら、更新後の設備情報を反映する。
  const handleEquipmentUpdate = useCallback((updatedEquipments: Equipment) => {
    setSelectedEquipment(updatedEquipments);
  }, []);

  // Spring BootのAPIを叩いて指定した設備IDと紐づく点検計画を取得する
  useEffect(() => {
    axios.get<Array<Plan>>(`http://localhost:8080/equipments/${id}/plans`)
      .then((res) => setPlans(res.data));
  }, [id]);

  // CreatePlanModalで点検計画が追加されたら、追加後の点検計画を反映する。
  const handlePlanCreate = useCallback((createdPlans: Array<Plan>) => {
    setPlans(createdPlans);
  }, []);

  // UpdatePlanModalで更新処理が実行されたら、更新後の点検計画を反映する。
  const handlePlanUpdate = useCallback((updatedPlans: Array<Plan>) => {
    setPlans(updatedPlans);
  }, []);

  // DeletePlanConfirmModalで削除処理が実行されたら、削除後の点検計画を反映する。
  const handlePlanDelete = useCallback((deletedPlans: Array<Plan>) => {
    setPlans(deletedPlans);
  }, []);

  // Spring BootのAPIを叩いて指定した設備IDと紐づく点検履歴を取得する
  useEffect(() => {
    axios.get<Array<History>>(`http://localhost:8080/equipments/${id}/histories`)
      .then((res) => setHistories(res.data));
  }, [id]);

  // CreatePlanModalで点検履歴が追加されたら、追加後の点検履歴を反映する。
  const handleHistoryCreate = (createdHistories: Array<History>) => {
    setHistories(createdHistories);
  };

  // UpdateHistoryModalで更新処理が実行されたら、更新後の点検履歴を反映する。
  const handleHistoryUpdate = useCallback((updatedHistories: Array<History>) => {
    setHistories(updatedHistories);
  }, []);

  // DeleteHistoryConfirmModalで削除処理が実行されたら、削除後の点検履歴を反映する。
  const handleHistoryDelete = useCallback((deletedHistories: Array<History>) => {
    setHistories(deletedHistories);
  }, []);

  const navigate = useNavigate();

  // 設備検索画面に遷移
  const onClickBackFindPage = () => navigate("/find");

  return (
    <Box padding={5}>
      <Heading>設備詳細</Heading>
      <br />
      <br />
      <HStack spacing={10}>
        <Heading size="lg">設備情報</Heading>
        <BaseButton onClick={openUpdateEquipmentModal}>設備情報修正</BaseButton>
      </HStack>
      <UpdateEquipmentModal updateEquipment={selectedEquipment} isOpen={updateEquiipmentModalOpen} onClose={closeUpdateEquipmentModal} onEquipmentsUpdate={handleEquipmentUpdate} />
      <Divider my={3} />
      <EquipmentInformation selectedEquipment={selectedEquipment} />
      <br />
      <br />
      <HStack spacing={10}>
        <Heading size="lg">点検計画</Heading>
        <BaseButton onClick={openCreatePlanModal}>点検計画追加</BaseButton>
      </HStack>
      <CreatePlanModal isOpen={createPlanModalOpen} onClose={closeCreatePlanModal}
        onPlanCreate={handlePlanCreate} />
      <Divider my={3} />
      <Plans plans={plans} onPlanUpdate={handlePlanUpdate} onPlanDelete={handlePlanDelete} />
      <br />
      <br />
      <HStack spacing={10}>
        <Heading size="lg">点検履歴</Heading>
        <BaseButton onClick={openCreateHistoryModal}>点検履歴追加</BaseButton>
      </HStack>
      <CreateHistoryModal isOpen={createHistoryModalOpen} onClose={closeCreateHistoryModal}
        onHistoryCreate={handleHistoryCreate} />
      <Divider my={3} />
      <Histories histories={histories} onHistoryUpdate={handleHistoryUpdate} onHistoryDelete={handleHistoryDelete} />
      <br />
      <br />
      <HStack>
        <BaseButton onClick={onClickBackFindPage}>戻る</BaseButton>
        <BaseButton onClick={openDeleteEquipmentModal}>削除</BaseButton>
        <DeleteEquipmentConfirmModal isOpen={deleteEquiipmentModalOpen}
          onClose={closeDeleteEquipmentModal} />
      </HStack>
    </Box>
  );
});