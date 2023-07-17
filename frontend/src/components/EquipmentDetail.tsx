import { Divider, HStack, Heading, Input } from "@chakra-ui/react"
import { BaseButton } from "./atoms/BaseButton"
import { useNavigate, useParams } from "react-router-dom";
import { FC, useEffect, useState } from "react";
import { Equipment } from "./FindEquipment";
import axios from "axios";

export const EquipmentDetail: FC = () => {
  const [selectedEquipment, setSelectedEquipment] = useState<Equipment | null>(null);
  const [deleteMessage, setDeleteMessage] = useState("")

  const { id } = useParams();

  const navigate = useNavigate();

  const onClickBackFindPage = () => navigate("/find")
  const onClickUpdatePage = () => navigate(`/update/${id}`, {state: { id: id }})

  const onClickDelete = () => {
    alert("この設備を削除しますか？");
    axios.delete<string>(`http://localhost:8080/equipments/${id}`).then((res) => setDeleteMessage(res.data))
    alert(deleteMessage);
  }

  useEffect(() => {
    axios.get<Equipment>(`http://localhost:8080/equipments/${id}`).then((res) => setSelectedEquipment(res.data))
  }, [])

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
      <BaseButton onClick={onClickBackFindPage}>戻る</BaseButton>
      <BaseButton onClick={onClickUpdatePage}>修正</BaseButton>
      <BaseButton onClick={onClickDelete}>削除</BaseButton>
    </div>
  )
}