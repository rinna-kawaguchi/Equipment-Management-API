import { Box, HStack, Table, TableContainer, Tbody, Td, Th, Thead, Tr, useBoolean } from "@chakra-ui/react";
import { memo, useCallback } from "react";
import { BaseButton } from "../../atoms/BaseButton";
import { Plan } from "../../../types/Plan";
import { useSelectPlan } from "../../../hooks/useSelectPlan";
import { UpdatePlanModal } from "./UpdatePlanModal";
import { DeletePlanConfirmModal } from "./DeletePlanConfirmModal";

type Props = {
  plans: Array<Plan>;
  onPlanUpdate: (updatedPlans: Array<Plan>) => void;
  onPlanDelete: (deletedPlans: Array<Plan>) => void;
};

export const Plans = memo((props: Props) => {
  const { plans, onPlanUpdate, onPlanDelete } = props;
  const { onSelectPlan, selectedPlan } = useSelectPlan();

  const [updatePlanFlag, setUpdatePlanFlag] = useBoolean();
  const [deletePlanFlag, setDeletePlanFlag] = useBoolean();

  // useSelectPlanのカスタムフック内のonSelectPlan関数で点検計画を特定しUpdatePlanModalを表示する
  const onClickUpdatePlanModal = useCallback((checkPlanId: number) => {
    onSelectPlan({ checkPlanId: checkPlanId, plans: plans });
    setUpdatePlanFlag.on();
  }, [plans, onSelectPlan]);

  // useSelectPlanのカスタムフック内のonSelectPlan関数で点検計画を特定しDeletePlanConfirmModalを表示する
  const onClickDeletePlan = useCallback((checkPlanId: number) => {
    onSelectPlan({ checkPlanId: checkPlanId, plans: plans });
    setDeletePlanFlag.on();
  }, [plans, onSelectPlan]);

  return (
    <Box>
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
            {plans.map((plan) => (
              <Tr key={plan.checkPlanId}>
                <Td >{plan.checkType}</Td>
                <Td>{plan.period}</Td>
                <Td>{plan.deadline}</Td>
                <Td>
                  <HStack>
                    <BaseButton onClick={() => onClickUpdatePlanModal(plan.checkPlanId)}>
                      修正
                    </BaseButton>
                    <BaseButton onClick={() => onClickDeletePlan(plan.checkPlanId)}>
                      削除
                    </BaseButton>
                  </HStack>
                </Td>
              </Tr>
            ))}
          </Tbody>
        </Table>
      </TableContainer>
      <UpdatePlanModal selectedPlan={selectedPlan} isOpen={updatePlanFlag}
        onClose={setUpdatePlanFlag.off} onPlanUpdate={onPlanUpdate} />
      <DeletePlanConfirmModal selectedPlan={selectedPlan} isOpen={deletePlanFlag}
        onClose={setDeletePlanFlag.off} onPlanDelete={onPlanDelete} />
    </Box>
  );
});