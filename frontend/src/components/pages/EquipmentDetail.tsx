import { FC, memo, useCallback, useEffect, useState } from "react";
import { Box, Divider, HStack, Heading, useBoolean } from "@chakra-ui/react";
import { useNavigate, useParams } from "react-router-dom";

import { BaseButton } from "../atoms/BaseButton";
import { EquipmentInformation } from "../molecules/EquipmentInformation";
import { CreatePlanModal } from "../organisms/plan/CreatePlanModal";
import { UpdateEquipmentModal } from "../organisms/UpdateEquipmentModal";
import { CreateHistoryModal } from "../organisms/History/CreateHistoryModal";
import { DeleteEquipmentConfirmModal } from "../organisms/DeleteEquipmentConfirmModal";
import { Plans } from "../organisms/plan/Plans";
import { Histories } from "../organisms/History/Histories";
import { Equipment } from "../../types/Equipment";
import { Plan } from "../../types/Plan";
import { History } from "../../types/History";
import { instance } from "../../axios/config";

export const EquipmentDetail: FC = memo(() => {
  const { id } = useParams();

  const [selectedEquipment, setSelectedEquipment] = useState<Equipment | null>(null);
  const [plans, setPlans] = useState<Array<Plan>>([]);
  const [histories, setHistories] = useState<Array<History>>([]);

  const [updateEquipmentFlag, setUpdateEquipmentFlag] = useBoolean();
  const [createPlanFlag, setCreatePlanFlag] = useBoolean();
  const [createHistoryFlag, setCreateHistoryFlag] = useBoolean();
  const [deleteEquipmentFlag, setDeleteEquipmentFlag] = useBoolean();

  // Spring BootのAPIを叩いて指定した設備IDの設備情報を取得する
  useEffect(() => {
    instance.get<Equipment>(`/equipments/${id}`).then((res) => setSelectedEquipment(res.data));
  }, [id]);

  // UpdateEquipmentModalで更新処理が実行されたら、更新後の設備情報を反映する。
  const handleEquipmentUpdate = useCallback((updatedEquipments: Equipment) => {
    setSelectedEquipment(updatedEquipments);
  }, []);

  // Spring BootのAPIを叩いて指定した設備IDと紐づく点検計画を取得する
  useEffect(() => {
    instance.get<Array<Plan>>(`/equipments/${id}/plans`)
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
    instance.get<Array<History>>(`/equipments/${id}/histories`)
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
  const onClickBackSearchPage = () => navigate("/Search");

  return (
    <Box px={10} py={5}>
      <Heading size="lg">設備詳細</Heading>
      <Divider my={3} />
      <Box px={3}>
        <HStack spacing={10}>
          <Heading size="md">設備情報</Heading>
          <BaseButton onClick={setUpdateEquipmentFlag.on}>設備情報修正</BaseButton>
        </HStack>
        <UpdateEquipmentModal updateEquipment={selectedEquipment} isOpen={updateEquipmentFlag} onClose={setUpdateEquipmentFlag.off} onEquipmentsUpdate={handleEquipmentUpdate} />
        <Divider my={3} />
        <EquipmentInformation selectedEquipment={selectedEquipment} />
        <br />
        <br />
        <HStack spacing={10}>
          <Heading size="md">点検計画</Heading>
          <BaseButton onClick={setCreatePlanFlag.on}>点検計画追加</BaseButton>
        </HStack>
        <CreatePlanModal isOpen={createPlanFlag} onClose={setCreatePlanFlag.off}
          onPlanCreate={handlePlanCreate} />
        <Divider my={3} />
        <Plans plans={plans} onPlanUpdate={handlePlanUpdate} onPlanDelete={handlePlanDelete} />
        <br />
        <br />
        <HStack spacing={10}>
          <Heading size="md">点検履歴</Heading>
          <BaseButton onClick={setCreateHistoryFlag.on}>点検履歴追加</BaseButton>
        </HStack>
        <CreateHistoryModal isOpen={createHistoryFlag} onClose={setCreateHistoryFlag.off}
          onHistoryCreate={handleHistoryCreate} />
        <Divider my={3} />
        <Histories histories={histories} onHistoryUpdate={handleHistoryUpdate} onHistoryDelete={handleHistoryDelete} />
        <br />
        <br />
        <HStack>
          <BaseButton onClick={onClickBackSearchPage}>戻る</BaseButton>
          <BaseButton onClick={setDeleteEquipmentFlag.on}>削除</BaseButton>
          <DeleteEquipmentConfirmModal isOpen={deleteEquipmentFlag}
            onClose={setDeleteEquipmentFlag.off} />
        </HStack>
      </Box>
    </Box>
  );
});